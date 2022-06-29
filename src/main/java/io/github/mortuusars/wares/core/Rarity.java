package io.github.mortuusars.wares.core;

import java.util.Arrays;

public enum Rarity {
    COMMON(20),
    UNCOMMON(12),
    RARE(5),
    EXTREMELY_RARE(1);

    private final int weight;

    Rarity(int weight) {
        this.weight = weight;
    }

    public int getWeight(){
        return this.weight;
    }

    public float getChance(){
        return weight / (float)Arrays.stream(Rarity.values()).mapToInt(r -> r.weight).sum();
    }
}
