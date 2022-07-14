package io.github.mortuusars.wares.core.types;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;

import java.util.Random;

public record IntegerRange(int min, int max, int step) {

    public static final Codec<IntegerRange> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("min").forGetter(IntegerRange::min),
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("max").forGetter(IntegerRange::max),
            Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("step", 1).forGetter(IntegerRange::step)
    ).apply(instance, IntegerRange::new));

    public static IntegerRange ZERO = new IntegerRange(0, 0, 1);

    public IntegerRange {
        Preconditions.checkArgument(min <= max, "min should be smaller or equal to max. Values: min: {}, max: {}", min, max);
        Preconditions.checkArgument(step > 0, "Step cannot be less than 0. Value: {}", step);
    }

    public int get(Random random) {
        if (min == max)
            return min;

        int num = Mth.randomBetweenInclusive(random, min, max);
        return (int) (step * Math.round(num / (double)step)); // Rounds to the closest stepped value - ex: 35 with step = 16 -> 32
    }

    @Override
    public String toString() {
        String stepStr = step == 1 ? "" : ", step=" + step;
        return "IntRange{" + "min=" + min + ", max=" + max + stepStr + '}';
    }

    public static IntegerRange of(int min, int max){
        return new IntegerRange(min, max, 1);
    }
}