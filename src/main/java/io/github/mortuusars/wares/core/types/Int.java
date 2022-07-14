package io.github.mortuusars.wares.core.types;

import com.mojang.serialization.Codec;

import java.util.Random;

public record Int(int number) implements IntNumberProvider{
    @Override
    public int get(Random random) {
        return number;
    }
}
