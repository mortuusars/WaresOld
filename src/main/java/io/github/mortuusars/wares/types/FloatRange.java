package io.github.mortuusars.wares.types;

import com.google.common.base.Preconditions;

import java.util.Random;

public class FloatRange {
    public float min;
    public float max;

    public static FloatRange ZERO = new FloatRange(0f, 0f);

    public FloatRange(float min, float max) {
        Preconditions.checkArgument(min <= max, "min should be smaller or equal to max. Values: min: {}, max: {}", min, max);
        this.min = min;
        this.max = max;
    }

    public float getRandom(Random random){
        return random.nextFloat(min, max + 0.000001f);
    }
}
