package io.github.mortuusars.wares.core.ware.item;

import com.google.common.base.Preconditions;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;
import java.util.Optional;

public class FixedWareItemInfo extends WareItemInfo<FixedWareItemInfo> {

    public int count;

    @Override
    public int getCount() {
        return count;
    }

    public FixedWareItemInfo count(int count){
        Preconditions.checkArgument(count > 0, "count should be larger than 0. Value: {}", count);
        this.count = count;
        return this;
    }

    public boolean matches(ItemStack stack){
        String stackItemRegistryName = Objects.requireNonNull(stack.getItem().getRegistryName()).toString();

        if (this.isTag()){
            Optional<TagKey<Item>> requestedItemTag = this.getTag();
            if (requestedItemTag.isPresent() && stack.is(requestedItemTag.get())){
                return this.count >= stack.getCount(); // No need to check for other properties if requested is a tag.
            }
        }
        else if (!stackItemRegistryName.equals(this.item)){
            return false;
        }

        Optional<ItemStack> requestedStackOptional = this.toItemStack();
        if (requestedStackOptional.isEmpty())
            return false;

        ItemStack requestedStack = requestedStackOptional.get();

        boolean nbtMatches = this.ignoreNbt || ItemStack.tagMatches(stack, requestedStack);
        boolean damageMatches = this.ignoreDamage || !stack.isDamaged();

        return nbtMatches && damageMatches;
    }

    public Optional<ItemStack> toItemStack() {
        Optional<Item> itemOpt = getItem();
        if (itemOpt.isEmpty())
            return Optional.empty();

        Item itemType = itemOpt.get();
        ItemStack stack = new ItemStack(itemType, getCount());

        if (this.hasNbt()){
            try {
                stack.setTag(TagParser.parseTag(tag));
            }
            catch (CommandSyntaxException e) {
                LogUtils.getLogger().warn("Failed to deserialize NBTTag from string: {}\nWareItemInfo: {}.", e.getMessage(), this);
                e.printStackTrace();
            }
        }

        return Optional.of(stack);
    }

    @Override
    public String toString() {
        return "FixedWareItemInfo{" +
                "item='" + item + '\'' +
                ", tag='" + tag + '\'' +
                ", count=" + count +
                ", nbt='" + nbt + '\'' +
                ", ignoreNbt=" + ignoreNbt +
                ", ignoreDamage=" + ignoreDamage +
                '}';
    }
}
