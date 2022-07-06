package io.github.mortuusars.wares.types;

import com.google.common.base.Preconditions;

import java.util.Random;

public class IntRange {
    public int min;
    public int max;

    public static IntRange ZERO = new IntRange(0, 0);

    public IntRange(int min, int max) {
        Preconditions.checkArgument(min <= max, "min should be smaller or equal to max. Values: min: {}, max: {}", min, max);
        this.min = min;
        this.max = max;
    }

    public int getRandom(Random random){
        return random.nextInt(min, ++max);
    }
}