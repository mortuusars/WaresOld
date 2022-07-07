package io.github.mortuusars.wares.common;

import com.mojang.logging.LogUtils;
import com.mojang.math.Vector3f;
import io.github.mortuusars.wares.common.blockentities.ShippingCrateBlockEntity;
import io.github.mortuusars.wares.core.ware.Ware;
import io.github.mortuusars.wares.core.ware.WareUtils;
import io.github.mortuusars.wares.core.ware.item.FixedWareItemInfo;
import io.github.mortuusars.wares.setup.ModBlocks;
import io.github.mortuusars.wares.setup.ModItems;
import io.github.mortuusars.wares.utils.PosUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ShippingCrate {

    public static final int SLOTS = 30;

    public static final Method _containerGetItemsMethod = ObfuscationReflectionHelper.findMethod(RandomizableContainerBlockEntity.class, "m_7086_"); // getItems

    public static void convertToShippingCrate(Player player, Level level, BlockPos pos, BlockState clickedBlockState, ItemStack heldItemStack){

        if (!heldItemStack.is(ModItems.PURCHASE_REQUEST.get()))
            throw new IllegalArgumentException(String.format("heldItemStack should be '%s'. Value: %s1",
                    ModItems.PURCHASE_REQUEST.get().getRegistryName(), heldItemStack.getItem().getRegistryName()));

        Optional<Ware> wareOptional = WareUtils.readWareFromStackNBT(heldItemStack);
        if (wareOptional.isEmpty()){
            LogUtils.getLogger().error("Cannot convert '{}' to Shipping Crate: Ware was not deserialized.", clickedBlockState);
            return;
        }

        Ware ware = wareOptional.get();

//        if (level.isClientSide){
//            //TODO sound
//            spawnConvertedParticles(level, pos);
//            return;
//        }

        if (!level.isClientSide){
            NonNullList<ItemStack> oldItems = NonNullList.create();

            BlockEntity oldBlockEntity = level.getBlockEntity(pos);
            if (oldBlockEntity != null)
                oldItems = getBlockEntityItemStacks(oldBlockEntity);

            level.removeBlockEntity(pos);
            level.removeBlock(pos, false);

            BlockState newBlockState = ModBlocks.SHIPPING_CRATE.get().withPropertiesOf(clickedBlockState);

            if (level.setBlock(pos, newBlockState, Block.UPDATE_ALL)){

                var shippingCrateEntity = (ShippingCrateBlockEntity)level.getBlockEntity(pos);
                if (shippingCrateEntity == null)
                    throw new IllegalStateException("ShippingCrate should have its block entity after block is placed.");
                shippingCrateEntity.setWare(ware);

                // Transfer previous block's items to shipping crate slots:
                for (int i = 0; i < Math.min(oldItems.size(), SLOTS); i++) {
                    shippingCrateEntity.setItem(i, oldItems.get(i));
                    oldItems.set(i, ItemStack.EMPTY);
                }

                // Drop old block entity items which were not added to shipping crate:
                for (ItemStack itemStack : oldItems)
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), itemStack);
            }
        }

        if (level.getBlockEntity(pos) instanceof ShippingCrateBlockEntity){
            if (level.isClientSide)
                spawnConvertedParticles(level, pos);

            Vector3f blockC = PosUtils.blockCenter(pos);
            level.playSound(player, blockC.x(), blockC.y(), blockC.z(), SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 0.5f, 1f);
        }
    }

    @SuppressWarnings("unchecked")
    private static NonNullList<ItemStack> getBlockEntityItemStacks(BlockEntity blockEntity) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        Optional<IItemHandler> itemHandlerOptional = blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve();

        if (itemHandlerOptional.isPresent()) {
            IItemHandler handler = itemHandlerOptional.get();
            IntStream.range(0, handler.getSlots())
                    .mapToObj(handler::getStackInSlot)
                    .forEach(stacks::add);
        } else {
            if (blockEntity instanceof RandomizableContainerBlockEntity container) {
                try {
                    stacks.addAll((Collection<? extends ItemStack>) _containerGetItemsMethod.invoke(container));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LogUtils.getLogger().error("Cannot invoke 'm_7086_ - getItems' method using reflection.\n" + e);
                }
            }
        }

        return stacks;
    }

    private static void spawnConvertedParticles(Level level, BlockPos pos){
        Random r = level.getRandom();
        for (int i = 0; i < 30; i++) {
            double x = pos.getX() + 0.5d + r.nextFloat(-0.7f, 0.7f);
            double y = pos.getY() + 0.8d + r.nextFloat(-0.7f, 0.7f);
            double z = pos.getZ() + 0.5d + r.nextFloat(-0.7f, 0.7f);
            double velocity = r.nextFloat(-0.05f, 0.05f);
            level.addParticle(ParticleTypes.POOF, x, y, z, velocity, velocity + 0.05, velocity);
        }
    }

    public static Shipping getShippingResult(Ware ware, NonNullList<ItemStack> items){
        Map<FixedWareItemInfo, Integer> requested = ware.requestedItems.stream().collect(Collectors.toMap(i -> i, i -> 0));
        NonNullList<ItemStack> excessItems = NonNullList.create();

        for (FixedWareItemInfo wareItemInfo : ware.requestedItems){
            for (int i = items.size() - 1; i >= 0 ; i--) {
                ItemStack stack = items.get(i);
                if (wareItemInfo.matches(stack)){
                    int requestedItemCount = wareItemInfo.getCount();
                    int currentCount = requested.get(wareItemInfo) + stack.getCount();

                    if (currentCount >= requestedItemCount){
                        ItemStack excessStack = stack.copy();
                        excessStack.setCount(stack.getCount() - (requestedItemCount - currentCount));
                        excessItems.add(excessStack);
                    }

                    requested.put(wareItemInfo, Math.min(currentCount, requestedItemCount));
                }
            }
        }

        boolean isFulfilled = true;
        for (var req : requested.entrySet()){
            if (req.getKey().count > req.getValue()) {
                isFulfilled = false;
                break;
            }
        }

        return new Shipping(ware.getPaymentStacks(), excessItems, isFulfilled);
    }

    public static class Shipping {

        public NonNullList<ItemStack> paymentItems;
        public NonNullList<ItemStack> excessItems;

        private final boolean isFulfilled;

        public Shipping(NonNullList<ItemStack> paymentItems, NonNullList<ItemStack> excessItems, boolean isFulfilled) {
            this.paymentItems = paymentItems;
            this.excessItems = excessItems;
            this.isFulfilled = isFulfilled;
        }

        public boolean isFulfilled(){
            return isFulfilled;
        }
    }
}
