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





//        CompoundTag tag = new CompoundTag();
//        tag.putString("Ware", serializedWare);
//
//        stack.setTag(tag);

        return super.useOn(pContext);
    }

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

        if (!level.isClientSide && !Objects.requireNonNull(context.getPlayer()).isCreative())
            heldItemStack.shrink(1);

        return InteractionResult.SUCCESS;
    }


}
