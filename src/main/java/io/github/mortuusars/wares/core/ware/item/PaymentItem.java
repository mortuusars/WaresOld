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

public class PaymentItem extends ItemData{

    public static Codec<PaymentItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Registry.ITEM.byNameCodec().fieldOf("item").orElse(Items.AIR).forGetter(PaymentItem::getItem),
                    TagKey.codec(Registry.ITEM_REGISTRY).optionalFieldOf("tag", null).orElse(null).forGetter(PaymentItem::getTag),
                    CompoundTag.CODEC.fieldOf("nbt").orElse(null).forGetter(PaymentItem::getNbt),
                    Codec.intRange(1, Integer.MAX_VALUE).fieldOf("count").forGetter(PaymentItem::getCount))
            .apply(instance, PaymentItem::new));

    private final int count;

    public PaymentItem(Item item, TagKey<Item> tag, CompoundTag nbt, int count) {
        super(item, tag, nbt);
        Preconditions.checkArgument(count > 0, "Count should be larger than 0. Value: {}", count);
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
