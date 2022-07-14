package io.github.mortuusars.wares.lib.enums;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public enum TimeOfDay implements StringRepresentable {
    ALL_DAY("all_day"),
    DAY("day"),
    NIGHT("night"),
    MORNING("morning"),
    AFTERNOON("afternoon"),
    EVENING("evening");

    public static final Codec<TimeOfDay> CODEC = StringRepresentable.fromEnum(TimeOfDay::values, TimeOfDay::byName);

    private static final Map<String, TimeOfDay> BY_NAME = Arrays.stream(TimeOfDay.values()).collect(Collectors.toMap(TimeOfDay::getSerializedName, timeOfDay -> timeOfDay));

    private final String name;

    TimeOfDay(String name) {
        this.name = name;
    }

    @Nullable
    public static TimeOfDay byName(@Nullable String name) {
        return name == null ? null : BY_NAME.get(name.toLowerCase(Locale.ROOT));
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
