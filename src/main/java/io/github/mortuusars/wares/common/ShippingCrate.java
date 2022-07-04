package io.github.mortuusars.wares.common;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.wares.common.blockentities.ShippingCrateBlockEntity;
import io.github.mortuusars.wares.setup.ModBlocks;
import io.github.mortuusars.wares.setup.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.Containers;
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
import java.util.stream.IntStream;

public class ShippingCrate {

    public static final Method _containerGetItemsMethod;

    static {
        _containerGetItemsMethod = ObfuscationReflectionHelper.findMethod(RandomizableContainerBlockEntity.class, "m_7086_"); // getItems
    }

    public static void convertToShippingCrate(Level level, BlockPos pos, BlockState clickedBlockState, ItemStack heldItemStack){

        if (!heldItemStack.is(ModItems.BILL_OF_LADING.get()))
            throw new IllegalArgumentException("heldItemStack should be 'wares:bill_of_lading'. Value: " + heldItemStack.getItem().getRegistryName());

        if (level.isClientSide){
            //TODO sound
            spawnConvertedParticles(level, pos);
            return;
        }

        NonNullList<ItemStack> oldItems = NonNullList.create();

        BlockEntity oldBlockEntity = level.getBlockEntity(pos);
        if (oldBlockEntity != null)
            oldItems = getBlockEntityItemStacks(oldBlockEntity);

        level.removeBlockEntity(pos);
        level.removeBlock(pos, false);

        Block newBlock = ModBlocks.SHIPPING_CRATE.get();
        BlockState newBlockState = newBlock.withPropertiesOf(clickedBlockState);

        level.setBlock(pos, newBlockState, Block.UPDATE_ALL);

        if (level.getBlockEntity(pos) instanceof ShippingCrateBlockEntity shippingCrateEntity){
            shippingCrateEntity.setBillStack(heldItemStack);

            // Transfer previous block's items to new container:
            for (int i = 0; i < Math.min(oldItems.size(), ShippingCrateBlockEntity.SLOTS); i++) {
                if (i == ShippingCrateBlockEntity.BILL_SLOT_INDEX)
                    continue;

                shippingCrateEntity.setItem(i, oldItems.get(i));
                oldItems.set(i, ItemStack.EMPTY);
            }
        }
        else
            LogUtils.getLogger().error("Conversion failed: blockEntity at '{}' is not ShippingCrateBlockEntity.", pos);

        for (ItemStack itemStack : oldItems)
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), itemStack);
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
}
