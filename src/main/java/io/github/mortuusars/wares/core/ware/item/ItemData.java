package io.github.mortuusars.wares.core.ware.item;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.Tags;

import java.util.Optional;

public abstract class ItemData {

    private final Item item;
    private final TagKey<Item> tag;
    private final CompoundTag nbt;

    public ItemData(Item item, TagKey<Item> tag, CompoundTag nbt) {
        if (item == Items.AIR && tag != null)
            throw new IllegalArgumentException("Tag should be specified when item is not defined.");
        this.item = item;
        this.tag = tag;
        this.nbt = nbt;
    }

//    public static RecordCodecBuilder.Instance codecBuilder(){
//
//
//        return RecordCodecBuilder.create(instance -> instance.group(
//                Registry.ITEM.byNameCodec().optionalFieldOf("item", Items.AIR).forGetter(ItemData::getItem),
//                TagKey.codec(Registry.ITEM_REGISTRY).optionalFieldOf("tag", null).forGetter(ItemData::getTag),
//                CompoundTag.CODEC.optionalFieldOf("nbt", null).forGetter(ItemData::getNbt)));
//    }

    public Item getItem() {
        return item;
    }

    public TagKey<Item> getTag() {
        return tag;
    }

    public CompoundTag getNbt() {
        return nbt;
    }

    public boolean isItem(){
        return item != Items.AIR;
    }
    public boolean isTag() { return !isItem(); }

    @Override
    public String toString() {
        return "ItemData{" +
                "item=" + item +
                ", tag=" + tag +
                ", nbt=" + nbt +
                '}';
    }
}
