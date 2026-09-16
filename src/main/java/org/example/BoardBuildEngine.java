package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.util.*;

public class BoardBuildEngine {

    private static final int[] CATEGORY_IDS = {9, 10, 11, 12, 13, 14};
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Question[][] BOARD = new Question[5][6];


    public static void main(String[] args) {

        System.out.println("=============================================");
        System.out.println("      INITIALIZING JEOPARDY TRIVIA BOARD     ");
        System.out.println("=============================================");
        System.out.println("[System] Preparing 6 categories. This takes ~30s due to API rate limits.");

        for (int i = 0; i < CATEGORY_IDS.length; i++) {
            int catId = CATEGORY_IDS[i];
            System.out.printf("%n[Loading Category %d/6] Fetching ID %d... ", (i + 1), catId);

            String jsonResponse = fetchCategoryJsonWithRetry(catId);

            buildColumnFromJson(jsonResponse, catId, i);
            System.out.print("Success!");

            if (i < CATEGORY_IDS.length - 1) {
                try {
                    System.out.print(" | Cooldown active... ");
                    Thread.sleep(5500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        for (Question[] row : BOARD) {
            for (Question q : row) {
                System.out.printf("%-20s | ", q.getCategory());
            }
            System.out.println();
        }
    }

    private static void buildColumnFromJson(String json, int catId, int colIndex) {
        if (json == null || json.isBlank()) {
            throw new IllegalStateException("Empty API response for category " + catId);
        }

        List<Question> easyPool = new ArrayList<>();
        List<Question> mediumPool = new ArrayList<>();
        List<Question> hardPool = new ArrayList<>();

        try {
            JsonNode tree = MAPPER.readTree(json);
            int responseCode = tree.path("response_code").asInt(-1);
            JsonNode results = tree.path("results");

            if (responseCode != 0) {
                throw new IllegalArgumentException("OpenTDB returned response_code=" + responseCode + " for category " + catId);
            }
            if (!results.isArray() || results.isEmpty()) {
                throw new IllegalArgumentException("Invalid JSON response for category " + catId + ": empty/missing results array");
            }

            List<Question> questions = MAPPER.readValue(results.traverse(), new TypeReference<List<Question>>() {});
            populatePools(easyPool, mediumPool, hardPool, questions);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse questions for category " + catId, e);
        }

        Collections.shuffle(easyPool);
        Collections.shuffle(mediumPool);
        Collections.shuffle(hardPool);

        BOARD[0][colIndex] = popFromPool(easyPool, "Easy Fallback");   // $200
        BOARD[1][colIndex] = popFromPool(easyPool, "Easy Fallback");   // $400
        BOARD[2][colIndex] = popFromPool(mediumPool, "Medium Fallback"); // $600
        BOARD[3][colIndex] = popFromPool(mediumPool, "Medium Fallback"); // $800
        BOARD[4][colIndex] = popFromPool(hardPool, "Hard Fallback");     // $1000
    }

    private static Question popFromPool(List<Question> pool, String fallbackDiff) {
        if (pool.isEmpty()) {
            System.out.println("[Warning] Pool is empty, using fallback: " + fallbackDiff);
            return new Question("Fallback Question", "Fallback Answer", "Fallback Category", fallbackDiff, 0);
        }
        return pool.remove(0);
    }

    private static String makeHttpRequest(String urlStr) {
        try {
            URL url = new URI(urlStr).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setInstanceFollowRedirects(true);
            int status = conn.getResponseCode();
            InputStream stream = status >= 200 && status < 300 ? conn.getInputStream() : conn.getErrorStream();
            if (stream == null) {
                throw new IOException("HTTP " + status + " with no response body");
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(stream));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) content.append(inputLine);
            in.close();
            if (status < 200 || status >= 300) {
                throw new IOException("HTTP " + status + " for " + urlStr + " body=" + content);
            }
            return content.toString();
        } catch (Exception e) {
            throw new RuntimeException("HTTP request failed for " + urlStr, e);
        }
    }

    private static String fetchCategoryJsonWithRetry(int categoryId) {
        String url = "https://opentdb.com/api.php?amount=12&category=" + categoryId + "&type=multiple";

        RuntimeException lastError = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                String json = makeHttpRequest(url);
                JsonNode tree = MAPPER.readTree(json);
                int responseCode = tree.path("response_code").asInt(-1);
                if (responseCode == 0 && tree.path("results").isArray() && !tree.path("results").isEmpty()) {
                    return json;
                }
                throw new IllegalStateException("OpenTDB response_code=" + responseCode + " for category " + categoryId);
            } catch (Exception e) {
                lastError = new RuntimeException(
                        "Attempt " + attempt + "/3 failed for category " + categoryId,
                        e
                );
                if (attempt < 3) {
                    try {
                        Thread.sleep(1200L * attempt);
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Interrupted while retrying category fetch", interruptedException);
                    }
                }
            }
        }

        throw new RuntimeException("Failed to fetch valid questions for category " + categoryId, lastError);
    }

    private static void populatePools(List<Question> e, List<Question> m, List<Question> h, List<Question> questions) {
        for (Question question : questions) {
            if (question.getDifficulty() == null) {
                continue;
            }

            switch (question.getDifficulty().toLowerCase(Locale.ROOT)) {
                case "easy":
                    e.add(question);
                    break;
                case "medium":
                    m.add(question);
                    break;
                case "hard":
                    h.add(question);
                    break;
                default:
                    break;
            }
        }
    }


    public static List<org.example.Question> createQuestion(int category, String difficulty) {
        String json = fetchJson(category, difficulty);
        try {
            JsonNode tree = MAPPER.readTree(json);
            JsonNode results = tree.path("results");

            if (!results.isArray() || results.isEmpty()) {
                throw new IllegalArgumentException("JSON response did not contain any questions");
            }

            return MAPPER.readValue(results.traverse(), new TypeReference<List<org.example.Question>>() {});

        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON into Question objects", e);
        }
    }

    private static String fetchJson(int category, String difficulty) {
        StringBuilder url = new StringBuilder("https://opentdb.com/api.php?amount=10&type=multiple&category=")
                .append(category);
        if (difficulty != null && !difficulty.isBlank()) {
            url.append("&difficulty=").append(difficulty);
        }
        return makeHttpRequest(url.toString());
    }

}
