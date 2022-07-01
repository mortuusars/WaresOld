package io.github.mortuusars.wares.content;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.wares.setup.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.IntStream;

public class ShippingCrate {

    public static final Method _containerGetItemsMethod;

    static {
        _containerGetItemsMethod = ObfuscationReflectionHelper.findMethod(RandomizableContainerBlockEntity.class, "m_7086_"); // getItems
    }

    public static void convertToShippingCrate(Level level, BlockPos pos, BlockState clickedBlockState, ItemStack heldItemStack){

        if (level.isClientSide){
            spawnConvertedParticles(level, pos);
            return;
        }

        NonNullList<ItemStack> oldItems = NonNullList.create();
        NonNullList<ItemStack> itemsToDrop =  NonNullList.create();

        BlockEntity oldBlockEntity = level.getBlockEntity(pos);
        if (oldBlockEntity != null) {
            oldItems = getOldBlockEntityItems(oldBlockEntity);

            if (oldItems.size() > 26)
                itemsToDrop.addAll(oldItems.subList(26, oldItems.size()));
        }

        level.removeBlockEntity(pos);

        Block newBlock = ModBlocks.SHIPPING_CRATE.get();
        BlockState newBlockState = newBlock.defaultBlockState();

        level.setBlock(pos, newBlockState, Block.UPDATE_ALL);

        BlockEntity newEntity = level.getBlockEntity(pos);
        if (newEntity == null){
            itemsToDrop = oldItems;
            LogUtils.getLogger().error("ShippingCrate block entity was not created.");
        }
        else {

            LazyOptional<IItemHandler> itemHandlerCap = newEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
            if (itemHandlerCap.isPresent()){
                IItemHandler itemHandler = itemHandlerCap.resolve().get();
                for (int i = 0; i < Math.min(oldItems.size(), 27); i++) {
                    itemHandler.insertItem(i, oldItems.get(i), false);
                }
            }
            else
                itemsToDrop = oldItems;
        }

        dropItems(itemsToDrop, level, pos);
    }

    private static void dropItems(NonNullList<ItemStack> items, Level level, BlockPos pos){
        double x = pos.getX() + 0.5f;
        double y = pos.getY() + 0.5f;
        double z = pos.getZ() + 0.5f;
        for (ItemStack excessItemStack : items) {
            level.addFreshEntity(new ItemEntity(level, x, y, z, excessItemStack));
        }
    }

    @SuppressWarnings("unchecked")
    private static NonNullList<ItemStack> getOldBlockEntityItems(BlockEntity blockEntity) {
        NonNullList<ItemStack> oldItems = NonNullList.create();
        Optional<IItemHandler> itemHandlerOptional = blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve();

        if (itemHandlerOptional.isPresent()) {
            IItemHandler handler = itemHandlerOptional.get();
            IntStream.range(0, handler.getSlots())
                    .mapToObj(handler::getStackInSlot)
                    .forEach(oldItems::add);
        } else {
            if (blockEntity instanceof RandomizableContainerBlockEntity container) {
                try {
                    oldItems.addAll((Collection<? extends ItemStack>) _containerGetItemsMethod.invoke(container));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LogUtils.getLogger().error("Cannot invoke 'm_7086_ - getItems' method using reflection.\n" + e);
                }
            }
        }

        return oldItems;
    }

    private static void spawnConvertedParticles(Level level, BlockPos pos){
        Random r = level.getRandom();
        for (int i = 0; i < 20; i++) {
            double x = pos.getX() + 0.5d + r.nextFloat(-0.4f, 0.4f);
            double y = pos.getY() + 0.5d + r.nextFloat(-0.4f, 0.4f);
            double z = pos.getZ() + 0.5d + r.nextFloat(-0.4f, 0.4f);
            double velocity = r.nextFloat(-0.1f, 0.1f);
            level.addParticle(ParticleTypes.POOF, x, y, z, velocity, 0.01d, velocity);
        }
    }
}
