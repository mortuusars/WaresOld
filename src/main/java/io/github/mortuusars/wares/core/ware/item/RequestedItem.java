package io.github.mortuusars.wares.core.ware.item;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Optional;

public class RequestedItem extends ItemData{

    public static Codec<RequestedItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Registry.ITEM.byNameCodec().fieldOf("item").orElse(Items.AIR).forGetter(RequestedItem::getItem),
                    TagKey.codec(Registry.ITEM_REGISTRY).optionalFieldOf("tag", null).forGetter(RequestedItem::getTag),
                    CompoundTag.CODEC.fieldOf("nbt").orElse(null).forGetter(RequestedItem::getNbt),
                    Codec.intRange(1, Integer.MAX_VALUE).fieldOf("count").forGetter(RequestedItem::getCount),
                    Codec.BOOL.fieldOf("ignoreNBT").orElse(false).forGetter(RequestedItem::isIgnoreNBT),
                    Codec.BOOL.fieldOf("ignoreDamage").orElse(false).forGetter(RequestedItem::isIgnoreDamage))
            .apply(instance, RequestedItem::new));

    private final int count;
    private final boolean ignoreNBT;
    private final boolean ignoreDamage;

    public RequestedItem(Item item, TagKey<Item> tag, CompoundTag nbt, int count, boolean ignoreNBT, boolean ignoreDamage) {
        super(item, tag, nbt);
        Preconditions.checkArgument(count > 0, "Count should be larger than 0. Value: {}", count);
        this.count = count;
        this.ignoreNBT = ignoreNBT;
        this.ignoreDamage = ignoreDamage;
    }

    public int getCount() {
        return count;
    }

    public boolean isIgnoreNBT() {
        return ignoreNBT;
    }

    public boolean isIgnoreDamage() {
        return ignoreDamage;
    }

    @Override
    public String toString() {
        return "RequestedItem{" +
                "item=" + getItem() +
                ", tag=" + getTag() +
                ", nbt=" + getNbt() +
                ", count=" + count +
                ", ignoreNBT=" + ignoreNBT +
                ", ignoreDamage=" + ignoreDamage +
                '}';
    }
}
