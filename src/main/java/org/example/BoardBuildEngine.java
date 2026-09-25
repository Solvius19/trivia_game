package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class BoardBuildEngine {

    private static final int[] VALID_CATEGORY_IDS = {9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32};
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Question[][] BOARD = new Question[5][6];


    public static Question[][] buildBoard() {
        HashSet<Integer> CATEGORY_IDS = generateCategories();

        System.out.println("=============================================");
        System.out.println("           INITIALIZING TRIVIA BOARD         ");
        System.out.println("=============================================");
        System.out.println("[System] Preparing 6 categories. This takes ~30s due to API rate limits.");;

        int count = 1;
        int total = CATEGORY_IDS.size();
        for (int catId : CATEGORY_IDS) {
            System.out.printf("%n[Loading Category %d/%d] Fetching ID %d... ", count, total, catId);

            String jsonResponse = fetchCategoryJsonWithRetry(catId);
            buildColumnFromJson(jsonResponse, catId, count - 1);
            System.out.print("Success!");

            if (count < total) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            count++;
        }
        return BOARD;
    }

    private static HashSet<Integer> generateCategories() {
        // pick 6 unique random category IDs from VALID_CATEGORY_IDS
        HashSet<Integer> selected = new HashSet<>();
        while (selected.size() < 6) {
            int rand = (int) (Math.random() * 23) + 9;
            if (selected.contains(rand)) {
                continue;
            }
            selected.add(rand);
        }
        return selected;
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
        return pool.removeFirst();
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
            BufferedReader input = new BufferedReader(new InputStreamReader(stream));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = input.readLine()) != null) content.append(inputLine);
            input.close();
            if (status < 200 || status >= 300) {
                throw new IOException("HTTP " + status + " for " + urlStr + " body=" + content);
            }
            return content.toString();
        } catch (Exception e) {
            throw new RuntimeException("HTTP request failed for " + urlStr, e);
        }
    }

    private static String fetchCategoryJsonWithRetry(int categoryId) {
        String url = "https://opentdb.com/api.php?amount=20&category=" + categoryId + "&type=multiple";

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


    public static List<Question> createQuestion(int category, String difficulty) {
        String json = fetchJson(category, difficulty);
        try {
            JsonNode tree = MAPPER.readTree(json);
            JsonNode results = tree.path("results");

            if (!results.isArray() || results.isEmpty()) {
                throw new IllegalArgumentException("JSON response did not contain any questions");
            }

            return MAPPER.readValue(results.traverse(), new TypeReference<List<Question>>() {});

        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON into Question objects", e);
        }
    }

    private static String fetchJson(int category, String difficulty) {
        StringBuilder url = new StringBuilder("https://opentdb.com/api.php?amount=20&type=multiple&category=")
                .append(category);
        if (difficulty != null && !difficulty.isBlank()) {
            url.append("&difficulty=").append(difficulty);
        }
        return makeHttpRequest(url.toString());
    }

}
