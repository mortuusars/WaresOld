package io.github.mortuusars.wares.core.ware.item;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mortuusars.wares.core.types.IntegerRange;
import io.github.mortuusars.wares.lib.ModConstants;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

import static io.github.mortuusars.wares.Wares.LOGGER;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public record WareItem(Item item, TagKey<Item> tag, Optional<CompoundTag> nbt,
                       Either<Integer, IntegerRange> count, double chance,
                       boolean ignoreNBT, boolean ignoreDamage) {

    public static final TagKey<Item> EMPTY_TAG = ItemTags.create(new ResourceLocation("wares:empty_tag"));

    public static final Codec<WareItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Registry.ITEM.byNameCodec().optionalFieldOf("item", Items.AIR).forGetter(WareItem::item),
            TagKey.codec(Registry.ITEM_REGISTRY).optionalFieldOf("tag", EMPTY_TAG).forGetter(WareItem::tag),
            CompoundTag.CODEC.optionalFieldOf("nbt").forGetter(WareItem::nbt),
            Codec.either(Codec.intRange(1, Integer.MAX_VALUE), IntegerRange.CODEC).optionalFieldOf("count", Either.left(1)).forGetter(WareItem::count),
            Codec.doubleRange(0.0d, 1.0d).optionalFieldOf("chance", 1.0d).forGetter(WareItem::chance),
            Codec.BOOL.optionalFieldOf("ignoreNBT", false).forGetter(WareItem::ignoreNBT),
            Codec.BOOL.optionalFieldOf("ignoreDamage", false).forGetter(WareItem::ignoreDamage))
        .apply(instance, WareItem::new));

    public WareItem(Item item, TagKey<Item> tag, Optional<CompoundTag> nbt, Either<Integer, IntegerRange> count, double chance, boolean ignoreNBT, boolean ignoreDamage) {
        this.item = item;
        this.tag = tag;
        this.nbt = nbt;
        this.count = count;
        this.chance = chance;
        this.ignoreNBT = ignoreNBT;
        this.ignoreDamage = ignoreDamage;

        Preconditions.checkArgument(item != Items.AIR || tag != EMPTY_TAG, "At least one of the following must be specified: item, tag.");
    }

    public WareItem(Item item, Either<Integer, IntegerRange> count) {
        this(item, EMPTY_TAG, Optional.empty(), count, 1.0d, false, false);
        Preconditions.checkArgument(item != Items.AIR);
    }

    public WareItem(TagKey<Item> tag, Either<Integer, IntegerRange> count) {
        this(Items.AIR, tag, Optional.empty(), count, 1.0d, false, false);
        Preconditions.checkArgument(tag != EMPTY_TAG);
    }

    public boolean isItem(){
        return item != Items.AIR;
    }

    public boolean isTag(){
        return !isItem();
    }

    public Item getItemOrFirstItemFromTag(){
        if (isItem())
            return item;

        if (isValidTag())
            return ForgeRegistries.ITEMS.tags().getTag(tag).stream().findFirst().orElseGet(() -> {
                LOGGER.warn("No items in a tag.");
                return Items.AIR;
            });

        return Items.AIR;
    }


    @SuppressWarnings("ConstantConditions")
    public boolean isValidTag(){
        return isTag() && ForgeRegistries.ITEMS.tags().isKnownTagName(tag);
    }


    // <Serialization>

    public String toJson(){
        DataResult<JsonElement> encodeResult = CODEC.encodeStart(JsonOps.INSTANCE, this);
        Optional<JsonElement> jsonElement = encodeResult.resultOrPartial(LOGGER::error);
        return jsonElement.isPresent() ? jsonElement.get().toString() : "";
    }

    public static Optional<WareItem> fromJson(String json){
        JsonObject parsed = GsonHelper.parse(json);
        return CODEC.parse(JsonOps.INSTANCE, parsed).resultOrPartial(LOGGER::error);
    }
}
