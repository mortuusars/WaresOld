package io.github.mortuusars.wares.lib.enums;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public enum TimeOfDay implements StringRepresentable {
    MORNING("morning", "06:00", "12:00"),
    AFTERNOON("afternoon", "12:00", "16:00"),
    EVENING("evening", "16:00", "20:00"),
    DAY("day", "06:00", "18:00"),
    NIGHT("night", "18:00", "06:00"),
    ALL_DAY("all_day", "00:00", "23:59");

    public static final Codec<TimeOfDay> CODEC = StringRepresentable.fromEnum(TimeOfDay::values, TimeOfDay::byName);


    private final String name;
    private final LocalTime start;
    private final LocalTime end;

    private static final Map<String, TimeOfDay> BY_NAME = Arrays.stream(TimeOfDay.values()).collect(Collectors.toMap(TimeOfDay::getSerializedName, timeOfDay -> timeOfDay));

    TimeOfDay(String name, String start, String end) {
        this.name = name;
        this.start = LocalTime.parse(start);
        this.end = LocalTime.parse(end);
    }

    @Nullable
    public static TimeOfDay byName(@Nullable String name) {
        return name == null ? null : BY_NAME.get(name.toLowerCase(Locale.ROOT));
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public boolean appliesTo(LocalTime time){
        return time.isAfter(start) && time.isBefore(end);
    }


    public static LocalTime getCurrentTime(Level level){
        int timeTicks = (int)(level.getDayTime() + 6000) % 24000;
        double time = timeTicks / (double)1000;
        int hours = (int)time;
        int minutes = (int)((time - hours) * 60);
        return LocalTime.of(hours, minutes);
    }

    public static TimeOfDay now(Level level){
        LocalTime currentTime = getCurrentTime(level);
        for (TimeOfDay timeOfDay : values()){
            if (timeOfDay.appliesTo(currentTime))
                return timeOfDay;
        }

        return TimeOfDay.ALL_DAY;
    }
}
