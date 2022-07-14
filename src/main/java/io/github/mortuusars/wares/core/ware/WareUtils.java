package io.github.mortuusars.wares.core.ware;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import io.github.mortuusars.wares.Wares;
import io.github.mortuusars.wares.core.utils.serialization.ItemStackJsonSerializer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

import static io.github.mortuusars.wares.Wares.LOGGER;

public class WareUtils {

    private static final Gson WARE_GSON = new GsonBuilder()
            .registerTypeAdapter(ItemStack.class, new ItemStackJsonSerializer())
            .create();

    public static final String WARE_NBT_KEY = "wares:ware";

    public static boolean hasWareNBTTag(ItemStack stack){
        return stack.getTagElement(WARE_NBT_KEY) != null;
    }

    public static boolean writeAsNBT(ItemStack stack, Ware ware){
        DataResult<Tag> tagDataResult = Ware.CODEC.encodeStart(NbtOps.INSTANCE, ware);
        Optional<Tag> wareTag = tagDataResult.resultOrPartial(error -> LOGGER.error("Writing ware to stack nbt failed: " + error));
        if (wareTag.isPresent()){
            stack.getOrCreateTag().put(WARE_NBT_KEY, wareTag.get());
            return true;
        }
        else
            return false;
    }

    public static Optional<Ware> readFromNBT(ItemStack stack){
        if (!stack.hasTag() || !hasWareNBTTag(stack))
            return Optional.empty();

        CompoundTag wareTag = stack.getTagElement(WARE_NBT_KEY);

        return wareTag != null ?
                Ware.CODEC.parse(NbtOps.INSTANCE, wareTag).resultOrPartial(error -> LOGGER.error("Failed to parse Ware from stack NBT: " + error)) :
                Optional.empty();
    }

    public static boolean saveToStackNBT(Ware ware, ItemStack stack){
        Optional<String> serializationResult = serialize(ware);

        if (serializationResult.isEmpty()) {
            LogUtils.getLogger().error("Ware was not saved to NBT of the stack '{}', because it was not serialized properly.", stack);
            return false;
        }

        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(WARE_NBT_KEY, serializationResult.get());
        return true;
    }

    public static Optional<Ware> readWareFromStackNBT(ItemStack stack){
        if (!stack.hasTag())
            return Optional.empty();

        Tag tag = stack.getTag().get(WARE_NBT_KEY);
        if (tag == null)
            return Optional.empty();

        String wareString = tag.getAsString();
        return deserialize(wareString);
    }

    public static Optional<StringTag> getWareNBTTag(ItemStack stack){
        if (stack.hasTag()){
            Tag tag = stack.getTag().get(WARE_NBT_KEY);
            if (tag != null)
                return Optional.of((StringTag)tag);
        }
        return Optional.empty();
    }

    public static void addWareNBTTag(ItemStack stack, CompoundTag wareTag){
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            tag.put(WARE_NBT_KEY, wareTag);
        }
    }

    public static Optional<String> serialize(Ware ware){
        return Optional.ofNullable(WARE_GSON.toJson(ware));
    }

    public static Optional<Ware> deserialize(String json){
        try {
            Ware value = WARE_GSON.fromJson(json, Ware.class);
            return value != null ? Optional.of(value) : Optional.empty();
        }
        catch (JsonSyntaxException ex){
            LogUtils.getLogger().error("Failed to deserialize Ware from json:\nJson string:'{}'\nException: {}", json, ex);
            return Optional.empty();
        }
    }

    public static NonNullList<Component> getWareTooltipInfo(Ware ware){
        NonNullList<Component> components = NonNullList.create();

//        if (!Strings.isNullOrEmpty(ware.title))
//            components.add(new TextComponent(ware.title).withStyle(ChatFormatting.BOLD));
//
//        if (!Strings.isNullOrEmpty(ware.description))
//            components.add(new TextComponent(ware.description).withStyle(ChatFormatting.GRAY));
//
//        if (!Strings.isNullOrEmpty(ware.buyer))
//            components.add(new TextComponent("Seller: ").withStyle(ChatFormatting.DARK_GRAY)
//                    .append(new TextComponent(ware.buyer).withStyle(ChatFormatting.GRAY)));
//
//        components.add(new TextComponent("Requested:").withStyle(ChatFormatting.GRAY));
//        for (var req : ware.requestedItems){
//            BaseComponent name = null;
//            if (req.isTag())
//                name = new TextComponent(req.tag);
//            else {
//                Optional<Item> optionalItem = req.getItem();
//                if (optionalItem.isPresent())
//                    name = (BaseComponent) optionalItem.get().getName(ItemStack.EMPTY);
//                else
//                    name = new TranslatableComponent("gui.unknown_item");
//            }
//            components.add(name.withStyle(ChatFormatting.DARK_GREEN)
//                    .append(new TextComponent(" x" + req.getFinalCount())).withStyle(ChatFormatting.DARK_GREEN));
//        }
//
//        components.add(new TextComponent("Payment:").withStyle(ChatFormatting.GRAY));
//        for (var req : ware.paymentItems){
//            BaseComponent name = null;
//            if (req.isTag())
//                name = new TextComponent(req.tag);
//            else {
//                Optional<Item> optionalItem = req.getItem();
//                if (optionalItem.isPresent())
//                    name = (BaseComponent) optionalItem.get().getName(ItemStack.EMPTY);
//                else
//                    name = new TranslatableComponent("gui.unknown_item");
//            }
//            components.add(name.withStyle(ChatFormatting.GREEN)
//                    .append(new TextComponent(" x" + req.getCount())).withStyle(ChatFormatting.GREEN));
//        }
//
//        if (ware.experience > 0)
//            components.add(new TextComponent("Experience: ").withStyle(ChatFormatting.DARK_GRAY)
//                    .append(new TextComponent("" + ware.experience).withStyle(ChatFormatting.GREEN)));

        return components;
    }
}
