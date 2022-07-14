package io.github.mortuusars.wares.core.ware.item;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mortuusars.wares.core.types.IntegerRange;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Optional;

public class RequestedItemData extends ItemData {

    public static Codec<RequestedItemData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Registry.ITEM.byNameCodec().fieldOf("item").orElse(Items.AIR).forGetter(RequestedItemData::getItem),
                TagKey.codec(Registry.ITEM_REGISTRY).optionalFieldOf("tag", null).forGetter(RequestedItemData::getTag),
                CompoundTag.CODEC.fieldOf("nbt").orElse(null).forGetter(RequestedItemData::getNbt),
                Codec.either(Codec.intRange(1, Integer.MAX_VALUE), IntegerRange.CODEC).fieldOf("count").forGetter(RequestedItemData::getCount),
                Codec.doubleRange(0.0d, 1.0d).fieldOf("chance").orElse(1d).forGetter(RequestedItemData::getChance),
                Codec.BOOL.fieldOf("ignoreNBT").orElse(false).forGetter(RequestedItemData::isIgnoreNBT),
                Codec.BOOL.fieldOf("ignoreDamage").orElse(false).forGetter(RequestedItemData::isIgnoreDamage))
            .apply(instance, RequestedItemData::new));

    private final Either<Integer, IntegerRange> count;
    private final double chance;
    private final boolean ignoreNBT;
    private final boolean ignoreDamage;

    public RequestedItemData(Item item, TagKey<Item> tag, CompoundTag nbt, Either<Integer, IntegerRange> count, double chance, boolean ignoreNBT, boolean ignoreDamage) {
        super(item, tag, nbt);
        this.count = count;
        this.chance = chance;
        this.ignoreNBT = ignoreNBT;
        this.ignoreDamage = ignoreDamage;
    }

    public Either<Integer, IntegerRange> getCount() {
        return count;
    }

    public double getChance() {
        return chance;
    }

    public boolean isIgnoreNBT() {
        return ignoreNBT;
    }

    public boolean isIgnoreDamage() {
        return ignoreDamage;
    }

    @Override
    public String toString() {
        return "RequestedItemData{" +
                "item=" + getItem() +
                ", tag=" + getTag() +
                ", nbt=" + getNbt() +
                ", count=" + count +
                ", chance=" + chance +
                ", ignoreNBT=" + ignoreNBT +
                ", ignoreDamage=" + ignoreDamage +
                '}';
    }
}
