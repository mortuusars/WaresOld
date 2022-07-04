package io.github.mortuusars.wares.common.items;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.wares.core.ware.Ware;
import io.github.mortuusars.wares.core.ware.WareUtils;
import io.github.mortuusars.wares.setup.ModItems;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class PurchaseRequestItem extends Item {
    public PurchaseRequestItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag isAdvanced) {
        Optional<Ware> ware = WareUtils.readWareFromStackNBT(stack);
        ware.ifPresent(w -> {
            components.add(new TextComponent(""));
            NonNullList<Component> wareTooltipInfo = WareUtils.getWareTooltipInfo(w);
            components.addAll(wareTooltipInfo);
        });
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (!stack.is(this))
            return InteractionResult.FAIL;

        Player player = context.getPlayer();
        Optional<Ware> wareOptional = WareUtils.readWareFromStackNBT(stack);

        if (context.isSecondaryUseActive()){
            Inventory playerInventory = player.getInventory();
            if (wareOptional.isEmpty()){
                player.sendMessage(new TextComponent("This request is invalid."), Util.NIL_UUID);
                playerInventory.removeItem(stack);
                return InteractionResult.FAIL;
            }

            int requestSlot = playerInventory.findSlotMatchingItem(stack);
            ItemStack billStack = new ItemStack(ModItems.BILL_OF_LADING.get());
            if (WareUtils.saveToStackNBT(wareOptional.get(), billStack)){
                playerInventory.setItem(requestSlot, billStack);
                context.getLevel().playSound(player, player.blockPosition(), SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.PLAYERS, 0.6f, 1f);
            }
        }
        else if (!context.getLevel().isClientSide)
            wareOptional.ifPresent(ware -> LogUtils.getLogger().info("Ware:\n{}", ware));

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        LogUtils.getLogger().warn("USED");
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
