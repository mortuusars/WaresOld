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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.management.OperationsException;
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
                    Codec.intRange(0, Integer.MAX_VALUE).fieldOf("experience").forGetter(Ware::experience),
                    DeliveryTime.CODEC.fieldOf("deliveryTime").forGetter(Ware::deliveryTime))
            .apply(instance, Ware::new));

    public void toBuffer(FriendlyByteBuf buffer){
        Optional<Tag> optionalTag = asNBT();
        if (optionalTag.isEmpty()) throw new IllegalArgumentException("Ware was not saved as NBT.");
        buffer.writeNbt((CompoundTag)optionalTag.get());
    }

    public Optional<Tag> asNBT(){
        DataResult<Tag> tagDataResult = Ware.CODEC.encodeStart(NbtOps.INSTANCE, this);
        return tagDataResult.resultOrPartial(error -> LOGGER.error("Saving Ware as nbt failed: " + error));
    }

    public static Optional<Ware> readFromNBT(CompoundTag nbt){
        return Ware.CODEC.parse(NbtOps.INSTANCE, nbt)
                .resultOrPartial(error -> LOGGER.error("Failed to parse Ware from stack NBT: " + error));
    }

    public static boolean hasWareNBTTag(ItemStack stack){
        return stack.getTagElement(ModConstants.WARE_NBT_KEY) != null;
    }

    public static boolean writeToStackNBT(ItemStack stack, Ware ware){
        Optional<Tag> wareTag = ware.asNBT();
        if (wareTag.isPresent()){
            stack.getOrCreateTag().put(ModConstants.WARE_NBT_KEY, wareTag.get());
            return true;
        }
        else
            return false;
    }

    public static Optional<Ware> readFromStackNBT(ItemStack stack){
        if (!stack.hasTag() || !hasWareNBTTag(stack))
            return Optional.empty();

        CompoundTag wareTag = stack.getTagElement(ModConstants.WARE_NBT_KEY);
        return readFromNBT(wareTag);
    }

    public NonNullList<Component> createTooltipInfo(){
        NonNullList<Component> components = NonNullList.create();

        if (!description.title().isBlank()){
            components.add(new TextComponent(description().title()).withStyle(ChatFormatting.GRAY));
            components.add(new TextComponent(""));
        }

        components.add(new TextComponent("Requested:").withStyle(ChatFormatting.GRAY));
        for (var requestedItem : requestedItems){
            BaseComponent item = requestedItem.isTag()
                    ? new TextComponent("#" + requestedItem.tag().location()) :
                    (BaseComponent) requestedItem.item().getName(new ItemStack(requestedItem.item()));
            components.add(new TextComponent(" ")
                    .append(item.withStyle(ChatFormatting.DARK_RED)
                            .append(new TextComponent(" x" + requestedItem.count().left().orElse(-1))).withStyle(ChatFormatting.DARK_RED)));
        }

        components.add(new TextComponent("Payment:").withStyle(ChatFormatting.GRAY));
        for (var paymentItem : paymentItems){
            BaseComponent item = (BaseComponent) paymentItem.getHoverName();
            components.add(new TextComponent(" ")
                    .append(item.withStyle(ChatFormatting.DARK_GREEN)
                            .append(new TextComponent(" x" + paymentItem.getCount())).withStyle(ChatFormatting.DARK_GREEN)));
        }

        if (experience != 0)
            components.add(new TextComponent("Experience: ").withStyle(ChatFormatting.DARK_GRAY)
                    .append(new TextComponent("" + experience).withStyle(ChatFormatting.GREEN)));

        return components;
    }
}
