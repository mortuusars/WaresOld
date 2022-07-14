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


public class PaymentItemData extends ItemData {
    public static Codec<PaymentItemData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Registry.ITEM.byNameCodec().fieldOf("item").orElse(Items.AIR).forGetter(PaymentItemData::getItem),
                    TagKey.codec(Registry.ITEM_REGISTRY).optionalFieldOf("tag", null).forGetter(PaymentItemData::getTag),
                    CompoundTag.CODEC.fieldOf("nbt").orElse(null).forGetter(PaymentItemData::getNbt),
                    Codec.either(Codec.intRange(1, Integer.MAX_VALUE), IntegerRange.CODEC).fieldOf("count").forGetter(PaymentItemData::getCount),
                    Codec.doubleRange(0.0d, 1.0d).fieldOf("chance").orElse(1d).forGetter(PaymentItemData::getChance))
            .apply(instance, PaymentItemData::new));

    private final Either<Integer, IntegerRange> count;
    private final double chance;

    public PaymentItemData(Item item, TagKey<Item> tag, CompoundTag nbt, Either<Integer, IntegerRange> count, double chance) {
        super(item, tag, nbt);
        this.count = count;
        this.chance = chance;
    }

    public Either<Integer, IntegerRange> getCount() {
        return count;
    }

    public double getChance() {
        return chance;
    }

    @Override
    public String toString() {
        return "PaymentItemData{" +
                "item=" + getItem() +
                ", tag=" + getTag() +
                ", nbt=" + getNbt() +
                ", count=" + count +
                ", chance=" + chance +
                '}';
    }


    public static PaymentItemData of(Item item, int count){
        return new PaymentItemData(item, null, null, Either.left(count), 1.0d);
    }

    public static PaymentItemData of(Item item, IntegerRange countRange){
        return new PaymentItemData(item, null, null, Either.right(countRange), 1.0d);
    }

    public static PaymentItemData of(TagKey<Item> tag, int count){
        return new PaymentItemData(Items.AIR, tag, null, Either.left(count), 1.0d);
    }
}
