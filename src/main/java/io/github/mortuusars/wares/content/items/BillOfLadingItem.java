package io.github.mortuusars.wares.content.items;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import io.github.mortuusars.wares.content.ShippingCrate;
import io.github.mortuusars.wares.content.blocks.CrateBlock;
import io.github.mortuusars.wares.core.Ware;
import io.github.mortuusars.wares.core.WareItem;
import io.github.mortuusars.wares.setup.ModBlocks;
import io.github.mortuusars.wares.setup.ModItems;
import io.github.mortuusars.wares.setup.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.IntStream;

public class BillOfLadingItem extends Item {
    public BillOfLadingItem(Properties properties) {
        super(properties);
    }

    public static final Method _containerGetItemsMethod;

    static {
        _containerGetItemsMethod = ObfuscationReflectionHelper.findMethod(RandomizableContainerBlockEntity.class, "m_7086_");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

//        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
//
//        Ware ware = new Ware();
//        ware.title = "test ware";
//
//        ware.requested = new WareItem[]{
//                new WareItem() {
//                    {
//                        item = new ResourceLocation("minecraft:apple");
//                        count = 32;
//                    }
//                },
//                new WareItem() {
//                    {
//                        item = new ResourceLocation("minecraft:barrel");
//                        count = 6;
//                    }
//                }
//        };
//
//        ware.payment = new WareItem[]{
//                new WareItem() {
//                    {
//                        item = new ResourceLocation("minecraft:emerald");
//                        count = 32;
//                    }
//                },
//                new WareItem() {
//                    {
//                        item = new ResourceLocation("minecraft:gold_ingot");
//                        count = 6;
//                    }
//                }
//        };
//
//        Gson gson = new Gson();
//        String serializedWare = gson.toJson(ware);
//
//        CompoundTag tag = new CompoundTag();
//        tag.putString("Ware", serializedWare);
//
//        stack.setTag(tag);

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {

        ItemStack stack = pContext.getPlayer().getItemInHand(pContext.getHand());

        Ware ware = new Ware();
        ware.title = "test ware";

        ware.requested = new WareItem[]{
                new WareItem() {
                    {
                        item = "minecraft:apple";
                        count = 32;
                    }
                },
                new WareItem() {
                    {
                        item = "minecraft:barrel";
                        count = 6;
                    }
                }
        };

        ware.payment = new WareItem[]{
                new WareItem() {
                    {
                        item = "minecraft:emerald";
                        count = 32;
                    }
                },
                new WareItem() {
                    {
                        item = "minecraft:gold_ingot";
                        count = 6;
                    }
                }
        };

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String serializedWare = gson.toJson(ware);



        CompoundTag tag = new CompoundTag();
        tag.putString("Ware", serializedWare);

        stack.setTag(tag);

        return super.useOn(pContext);
    }

    @SuppressWarnings("unchecked")
    @Override
    public InteractionResult onItemUseFirst(ItemStack heldItemStack, UseOnContext context) {

        if (heldItemStack.getItem() != ModItems.BILL_OF_LADING.get())
            return InteractionResult.PASS;

        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState clickedBlockState = level.getBlockState(blockPos);

        if (!clickedBlockState.is(ModTags.SHIPMENT_CONVERTIBLE))
            return InteractionResult.PASS;

        ShippingCrate.convertToShippingCrate(level, blockPos, clickedBlockState, heldItemStack);
        return InteractionResult.SUCCESS;

//        ArrayList<ItemStack> oldItems = new ArrayList<>();
//
//        BlockEntity oldBlockEntity = level.getBlockEntity(blockPos);
//        if (oldBlockEntity != null) {
//            oldItems = getOldBlockEntityItems(oldBlockEntity);
//
//            // Drop excess items on the floor
//            if (oldItems.size() > 26){
//                List<ItemStack> excess = oldItems.subList(26, oldItems.size());
//                double x = blockPos.getX() + 0.5f;
//                double y = blockPos.getY() + 0.5f;
//                double z = blockPos.getZ() + 0.5f;
//                for (ItemStack excessItemStack : excess) {
//                    level.addFreshEntity(
//                            new ItemEntity(level, x, y, z, excessItemStack));
//                }
//            }
//        }
//
//        level.removeBlockEntity(blockPos);
//
//        Block newBlock = ModBlocks.SHIPPING_CRATE.get();
//        BlockState newBlockState = newBlock.defaultBlockState();
//
//        level.setBlock(blockPos, newBlockState, Block.UPDATE_ALL);
//
//        BlockEntity newEntity = level.getBlockEntity(blockPos);
//        if (newEntity == null)
//            LogUtils.getLogger().error("Cannot get ShippingCrate block entity.");
//        else{
//            ArrayList<ItemStack> finalOldItems = oldItems;
//            newEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
//                    for (int i = 0; i < 27; i++) {
//                        if (i < finalOldItems.size() && finalOldItems.get(i) != null)
//                            itemHandler.insertItem(i, finalOldItems.get(i), false);
//                    }
//                }
//            );
//        }
//
//        if (!Objects.requireNonNull(context.getPlayer()).isCreative())
//            heldItemStack.shrink(1);

//        return InteractionResult.SUCCESS;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<ItemStack> getOldBlockEntityItems(BlockEntity blockEntity) {
        ArrayList<ItemStack> oldItems = new ArrayList<>();
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
}
