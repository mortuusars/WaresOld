package io.github.mortuusars.wares.content.items;

import com.google.gson.Gson;
import io.github.mortuusars.wares.Wares;
import io.github.mortuusars.wares.core.Ware;
import io.github.mortuusars.wares.setup.ModItems;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class PurchaseOrderItem extends Item {
    public PurchaseOrderItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.hasTag();
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (context.getLevel().isClientSide)
            return InteractionResult.SUCCESS;

        if (!stack.is(this))
            return InteractionResult.FAIL;

        if (!context.isSecondaryUseActive() && stack.hasTag()){
            CompoundTag tag = stack.getTag();
            String serializedWare = tag.get("Ware").getAsString();
            Ware ware = new Gson().fromJson(serializedWare, Ware.class);
            context.getPlayer().sendMessage(new TextComponent(ware.toString()), Util.NIL_UUID);

            return InteractionResult.SUCCESS;
        }

        if (stack.hasTag()){
            CompoundTag tag = stack.getTag();
            String serializedWare = tag.get("Ware").getAsString();
            Ware ware = new Gson().fromJson(serializedWare, Ware.class);

            Inventory inventory = context.getPlayer().getInventory();
            int slot = inventory.findSlotMatchingItem(stack);
            inventory.removeItem(stack);
            ItemStack bill = new ItemStack(ModItems.BILL_OF_LADING.get());
            CompoundTag billTag = new CompoundTag();
            billTag.putString("Ware", serializedWare);
            bill.setTag(billTag);
            inventory.add(slot, bill);
            context.getPlayer().playSound(SoundEvents.VILLAGER_WORK_CARTOGRAPHER, 0.5f, 1f + context.getLevel().random.nextFloat(-0.3f, 0.3f));
        }
        else {
            NonNullList<Ware> wares = Wares.getWares();
            Ware randomWare = wares.get(new Random().nextInt(0, wares.size()));
            CompoundTag tag = new CompoundTag();
            tag.putString("Ware", new Gson().toJson(randomWare));
            stack.setTag(tag);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        return super.use(pLevel, pPlayer, pUsedHand);
    }


}
