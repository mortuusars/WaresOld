package io.github.mortuusars.wares.lib.enums;

import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.StringRepresentable;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public enum Rarity implements StringRepresentable {
    //TODO: weights to config
    COMMON("common", 20),
    UNCOMMON("uncommon", 12),
    RARE("rare", 5),
    EPIC("epic", 1),
    TREASURE("treasure", 0);

    public static final Codec<Rarity> CODEC = StringRepresentable.fromEnum(Rarity::values, Rarity::byName);

    private static final Map<String, Rarity> BY_NAME = Arrays.stream(Rarity.values()).collect(Collectors.toMap(Rarity::getSerializedName, rarity -> rarity));

    private final String name;
    private final int weight;

    Rarity(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public int getWeight(){
        return this.weight;
    }

    public float getChance(){
        return weight / (float)Arrays.stream(Rarity.values()).mapToInt(r -> r.weight).sum();
    }

    @Nullable
    public static Rarity byName(@Nullable String name) {
        return name == null ? null : BY_NAME.get(name.toLowerCase(Locale.ROOT));
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public String getKey(){
        return "gui.wares.rarity." + name;
    }
}
