package io.github.mortuusars.wares.core;

import com.google.common.base.Preconditions;
import io.github.mortuusars.wares.utils.ItemUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Random;

public class WareItem {
    public String item;
    public String tag;
    public int countMin = 1;
    public int countMax = 1;
    public float chance = 1f;
    //TODO NBT

    public WareItem(@NotNull String item, @NotNull String tag, int countMin, int countMax, float chance) {
        this.item = item;
        this.tag = tag;
        this.countMin = countMin;
        this.countMax = countMax;

        Preconditions.checkArgument(item.isBlank() && tag.isBlank(), "At least one of the item identifiers - item or tag. {}", this.toString());
        Preconditions.checkArgument(countMin > 0, "countMin should be larger than 0.");
        Preconditions.checkArgument(countMax > 0, "countMin should be larger than 0.");
        Preconditions.checkArgument(countMin <= countMax, "countMin cannot be larger than countMax.");
    }

    public WareItem(@NotNull String item, @NotNull String tag, int count, float chance) {
        this(item, tag, count, ++count, chance);
    }

    public WareItem(@NotNull String item, @NotNull String tag, int count) {
        this(item, tag, count, ++count, 1f);
    }

    public WareItem(){}

    public int getCount(){
        return new Random().nextInt(countMin, ++countMax);
    }

    public NonNullList<ItemStack> createItemStacks(){
        NonNullList<ItemStack> stacks = NonNullList.create();

        if (new Random().nextFloat() > chance)
            return stacks;

        int itemsCount = getCount();

        Optional<Item> itemOptional = getAny();
        if (itemOptional.isPresent())
        {
            Item item = itemOptional.get();
            int maxStackSize = ItemUtils.getMaxStackSize(item);

            // Full stacks
            for (int i = 0; i < itemsCount / maxStackSize; i++) {
                stacks.add(new ItemStack(item, maxStackSize));
            }

            int remainder = itemsCount % maxStackSize;
            if (remainder != 0)
                stacks.add(new ItemStack(item, remainder));
        }

        return stacks;
    }

    public Optional<Item> getItem(){
        if (item == null || item.isBlank())
            return Optional.empty();

        ResourceLocation itemLoc = new ResourceLocation(item);
        return Optional.ofNullable(ForgeRegistries.ITEMS.getValue(itemLoc));
    }

    public Optional<TagKey<Item>> getTag(){
        if (tag == null || tag.isBlank())
            return Optional.empty();

        TagKey<Item> tagKey = ItemTags.create(new ResourceLocation(tag));
        return ForgeRegistries.ITEMS.tags().isKnownTagName(tagKey) ? Optional.of(tagKey) : Optional.empty();
    }

    public Optional<Item> getItemFromTag(){
        Optional<TagKey<Item>> tagKey = getTag();
        return tagKey.flatMap(itemTagKey -> ForgeRegistries.ITEMS.tags().getTag(itemTagKey).stream().findFirst());
    }

    public Optional<Item> getAny(){
        return getItem().or(this::getItemFromTag);
    }

    @Override
    public String toString() {
        if (countMin == countMax)
            return "WareItem{" +
                    "item='" + item + '\'' +
                    ", tag='" + tag + '\'' +
                    ", count=" + countMin +
                    '}';

        return "WareItem{" +
                "item='" + item + '\'' +
                ", tag='" + tag + '\'' +
                ", countMin=" + countMin +
                ", countMax=" + countMax +
                '}';
    }

    public static class Builder{
        private WareItem _item = new WareItem();

        public Builder item(@NotNull String itemRegistryName){
            Preconditions.checkArgument(!itemRegistryName.isBlank(), "itemRegistryName should have a value.");
            _item.item = itemRegistryName;
            return this;
        }

        public Builder tag(@NotNull String tag){
            Preconditions.checkArgument(!tag.isBlank(), "tag should have a value.");
            _item.tag = tag;
            return this;
        }

        public Builder count(int count){
            Preconditions.checkArgument(count > 0, "count should be larger than 0.");
            _item.countMin = count;
            _item.countMax = count;
            return this;
        }

        public Builder countRange(int min, int max){
            Preconditions.checkArgument(min > 0, "min count should be larger than 0.");
            Preconditions.checkArgument(max > 0, "max count should be larger than 0.");
            Preconditions.checkArgument(min <= max, "min should not be larger than max.");
            _item.countMin = min;
            _item.countMax = max;
            return this;
        }

        public Builder chance(float chance){
            _item.chance = chance;
            return this;
        }

        public WareItem build(){
            return _item;
        }
    }
}
