package io.github.mortuusars.wares.common.items;

import io.github.mortuusars.wares.Wares;
import io.github.mortuusars.wares.common.PurchaseRequestMenu;
import io.github.mortuusars.wares.common.shipping_crate.ShippingCrate;
import io.github.mortuusars.wares.core.ware.data.Ware;
import io.github.mortuusars.wares.setup.ModItems;
import io.github.mortuusars.wares.setup.ModLangKeys;
import io.github.mortuusars.wares.setup.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class PurchaseRequestItem extends Item {

    public static final Component DISPLAY_NAME = new TranslatableComponent(ModLangKeys.CONTAINER_PURCHASE_REQUEST);

    public PurchaseRequestItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag isAdvanced) {
        Ware.readFromStackNBT(stack).ifPresent(w -> {
            components.addAll(w.createTooltipInfo());
        });
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (!stack.is(this))
            return InteractionResult.PASS;

        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos blockPos = context.getClickedPos();
        BlockState clickedBlockState = level.getBlockState(blockPos);

        if (clickedBlockState.is(ModTags.SHIPMENT_CONVERTIBLE)){
            ShippingCrate.convertToShippingCrate(player, level, blockPos, clickedBlockState, stack);

            if (player != null && !level.isClientSide && !player.isCreative())
                stack.shrink(1);

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        if (!heldStack.is(ModItems.PURCHASE_REQUEST.get()))
            return InteractionResultHolder.pass(heldStack);

        Optional<Ware> wareOptional = Ware.readFromStackNBT(heldStack);

        if (wareOptional.isEmpty()){
            Wares.LOGGER.error("Cannot read Ware from stack nbt. No UI will be shown.");
            return InteractionResultHolder.pass(heldStack);
        }

        Ware ware = wareOptional.get();

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer)
            NetworkHooks.openGui(serverPlayer,
                    new SimpleMenuProvider((id, playerInventory, playerArg) -> new PurchaseRequestMenu(id, playerInventory, ware), DISPLAY_NAME),
                    ware::toBuffer);
        else if (level.isClientSide){
            level.playSound(player, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 1f, 1f);
        }

        return InteractionResultHolder.sidedSuccess(heldStack, level.isClientSide);
    }
}
