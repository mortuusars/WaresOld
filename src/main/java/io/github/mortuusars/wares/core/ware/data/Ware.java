package io.github.mortuusars.wares.core.ware.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mortuusars.wares.lib.ModConstants;
import io.github.mortuusars.wares.lib.enums.Rarity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

import static io.github.mortuusars.wares.Wares.LOGGER;

public record Ware(WareDescription description, Rarity rarity,
                        List<WareItem> requestedItems, List<ItemStack> paymentItems,
                        int experience, DeliveryTime deliveryTime) {

    public static final Codec<Ware> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    WareDescription.CODEC.optionalFieldOf("description", WareDescription.EMPTY).forGetter(Ware::description),
                    Rarity.CODEC.fieldOf("rarity").orElse(Rarity.COMMON).forGetter(Ware::rarity),
                    WareItem.CODEC.listOf().fieldOf("requestedItems").forGetter(Ware::requestedItems),
                    ItemStack.CODEC.listOf().fieldOf("paymentItems").forGetter(Ware::paymentItems),
                    Codec.INT.fieldOf("experience").forGetter(Ware::experience),
                    DeliveryTime.CODEC.fieldOf("deliveryTime").forGetter(Ware::deliveryTime))
            .apply(instance, Ware::new));


    public static boolean hasWareNBTTag(ItemStack stack){
        return stack.getTagElement(ModConstants.WARE_NBT_KEY) != null;
    }

    public static boolean writeAsNBT(ItemStack stack, Ware ware){
        DataResult<Tag> tagDataResult = Ware.CODEC.encodeStart(NbtOps.INSTANCE, ware);
        Optional<Tag> wareTag = tagDataResult.resultOrPartial(error -> LOGGER.error("Writing Ware to stack nbt failed: " + error));
        if (wareTag.isPresent()){
            stack.getOrCreateTag().put(ModConstants.WARE_NBT_KEY, wareTag.get());
            return true;
        }
        else
            return false;
    }

    public static Optional<Ware> readFromNBT(ItemStack stack){
        if (!stack.hasTag() || !hasWareNBTTag(stack))
            return Optional.empty();

        CompoundTag wareTag = stack.getTagElement(ModConstants.WARE_NBT_KEY);

        return wareTag != null ?
                Ware.CODEC.parse(NbtOps.INSTANCE, wareTag).resultOrPartial(error -> LOGGER.error("Failed to parse Ware from stack NBT: " + error)) :
                Optional.empty();
    }

    public NonNullList<Component> createTooltipInfo(){
        NonNullList<Component> components = NonNullList.create();

        components.add(new TextComponent("Requested:").withStyle(ChatFormatting.GRAY));
        for (var requestedItem : requestedItems){
            MutableComponent item = new TextComponent(requestedItem.toString()).withStyle(ChatFormatting.DARK_RED);
            components.add(item.append(new TextComponent(" x" + requestedItem.count())).withStyle(ChatFormatting.DARK_GREEN));
        }

        components.add(new TextComponent("Payment:").withStyle(ChatFormatting.GRAY));
        for (var paymentItem : paymentItems){
            MutableComponent item = new TextComponent(paymentItem.toString()).withStyle(ChatFormatting.DARK_RED);
            components.add(item.append(new TextComponent(" x" + paymentItem.getCount())).withStyle(ChatFormatting.DARK_GREEN));
        }

        if (experience > 0)
            components.add(new TextComponent("Experience: ").withStyle(ChatFormatting.DARK_GRAY)
                    .append(new TextComponent("" + experience).withStyle(ChatFormatting.GREEN)));

        return components;
    }
}
