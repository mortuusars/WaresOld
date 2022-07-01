package io.github.mortuusars.wares.core.ware.item;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.Optional;

/**
 * Base class for Ware item.<br>
 * Item field has higher priority than Tag: if both are specified - item will be chosen.
 */
@SuppressWarnings("unchecked")
public abstract class WareItemInfo<T extends WareItemInfo<T>> {
    public String item;
    public String tag;
    public String nbt;
    public boolean ignoreNbt;
    public boolean ignoreDamage;

    public abstract int getCount();

    public boolean isTag(){
        return (item == null || item.isBlank()) && (tag != null && !tag.isBlank());
    }

    public boolean hasNbt(){
        return nbt != null && !nbt.isBlank();
    }


    public Optional<Item> getItem(){
        if (item != null && !item.isBlank()){
            ResourceLocation itemLoc = new ResourceLocation(item);
            Item itemResult = ForgeRegistries.ITEMS.getValue(itemLoc);
            if (itemResult != null)
                return Optional.of(itemResult);
            else LogUtils.getLogger().warn("Item '{}' not found.", item);
        }
        return Optional.empty();
    }

    public Optional<Item> getItemOrItemFromTag(){
        return getItem().or(this::getItemOrItemFromTag);
    }

    public Optional<Item> getItemFromTag(){
        Optional<TagKey<Item>> tagKey = getTag();
        return tagKey.flatMap(itemTagKey -> ForgeRegistries.ITEMS.tags().getTag(itemTagKey).stream().findFirst());
    }

    public Optional<TagKey<Item>> getTag(){
        if (tag == null || tag.isBlank())
            return Optional.empty();

        TagKey<Item> tagKey = ItemTags.create(new ResourceLocation(tag));
        if (Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).isKnownTagName(tagKey))
            return Optional.of(tagKey);
        else LogUtils.getLogger().warn("Tag '{}' is not a registered tag.", tag);

        return Optional.empty();
    }


    public T item(String itemRegistryName){
        item = itemRegistryName;
        return (T) this;
    }

    public T tag(String tagRegistryName){
        tag = tagRegistryName;
        return (T) this;
    }

    public T nbt(String nbtTag){
        nbt = nbtTag;
        return (T) this;
    }

    public T ignoresNbt(boolean isIgnored){
        ignoreNbt = isIgnored;
        return (T) this;
    }

    public T ignoresDamage(boolean isIgnored){
        ignoreDamage = isIgnored;
        return (T) this;
    }

    @Override
    public String toString() {
        return "WareItemInfo{" +
                "item='" + item + '\'' +
                ", tag='" + tag + '\'' +
                ", nbt='" + nbt + '\'' +
                ", ignoreNbt=" + ignoreNbt +
                ", ignoreDamage=" + ignoreDamage +
                '}';
    }
}
