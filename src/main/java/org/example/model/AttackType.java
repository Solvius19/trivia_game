package org.example.model;

public enum AttackType {
    NONE(0),
    BLOCK_OUT(100),
    SCRAMBLE(200),
    SKIP(300),
    SELECT_NEXT(500),
    TAX(700),
    SWAP(2000);

    private final int cost;

    AttackType(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }
}