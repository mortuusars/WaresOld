package io.github.mortuusars.wares;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.wares.core.Rarity;
import io.github.mortuusars.wares.core.Ware;
import io.github.mortuusars.wares.core.WareItem;
import io.github.mortuusars.wares.setup.ClientSetup;
import io.github.mortuusars.wares.setup.Registries;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

@Mod(Wares.MOD_ID)
public class Wares
{
    public static final String MOD_ID = "wares";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Wares()
    {
        Registries.init();

        MinecraftForge.EVENT_BUS.addListener(this::onPlayerRightClick);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::setup);
    }

    public static NonNullList<Ware> getWares(){
        NonNullList<Ware> wares = NonNullList.create();

        Ware ware1 = new Ware.Builder()
                .title("Test Ware")
                .description("Desc")
                .experience(2)
                .addRequestedItem(new WareItem.Builder()
                        .item("minecraft:apple")
                        .countRange(12, 34)
                        .build())
                .addRequestedItem(new WareItem.Builder()
                        .tag("minecraft:saplings")
                        .countRange(50, 204)
                        .chance(0.7f)
                        .build())
                .addPaymentItem(new WareItem.Builder()
                        .item("minecraft:emerald")
                        .count(12)
                        .build())
                .build();

        Ware ware2 = new Ware.Builder()
                .title("Test Ware SECOND")
                .description("Desc 2")
                .experience(12)
                .addRequestedItem(new WareItem.Builder()
                        .item("minecraft:anvil")
                        .countRange(12, 34)
                        .build())
                .addRequestedItem(new WareItem.Builder()
                        .tag("minecraft:coals")
                        .countRange(50, 204)
                        .chance(0.2f)
                        .build())
                .addPaymentItem(new WareItem.Builder()
                        .item("minecraft:gold_block")
                        .countRange(10, 30)
                        .build())
                .build();

        Ware ware3 = new Ware.Builder()
                .title("Test Ware SECOND")
                .description("Desc 2")
                .experience(0)
                .addRequestedItem(new WareItem.Builder()
                        .item("minecraft:anvil")
                        .countRange(12, 34)
                        .build())
                .addRequestedItem(new WareItem.Builder()
                        .tag("minecraft:coals")
                        .countRange(50, 204)
                        .chance(0.2f)
                        .build())
                .addPaymentItem(new WareItem.Builder()
                        .item("minecraft:bread")
                        .countRange(1, 4)
                        .build())
                .build();

        wares.add(ware1);
        wares.add(ware2);
        wares.add(ware3);

        return wares;
    }

    public void onPlayerRightClick(PlayerInteractEvent event){
        if (event.getPlayer().level.isClientSide)
            return;

        if (!event.getPlayer().isSecondaryUseActive())
            return;

        NonNullList<Ware> wares = getWares();
        Level level = event.getPlayer().getLevel();
        BlockPos playerPos = event.getPlayer().blockPosition();

        Ware ware = wares.get(new Random().nextInt(0, wares.size()));
        NonNullList<ItemStack> paymentItemStacks = ware.getPaymentItemStacks();

        event.getPlayer().sendMessage(new TextComponent(paymentItemStacks.size() + " PaymentItem(s):"), UUID.randomUUID());

        for (ItemStack stack : paymentItemStacks){
            level.addFreshEntity(new ItemEntity(level, playerPos.getX(), playerPos.getY(), playerPos.getZ(), stack));
            event.getPlayer().sendMessage(new TextComponent(stack.toString()), UUID.randomUUID());
        }

        if (ware.experience > 0){
            level.addFreshEntity(new ExperienceOrb(level, playerPos.getX(), playerPos.getY(), playerPos.getZ(), ware.experience));
            event.getPlayer().sendMessage(new TextComponent(String.format("Dropped %s experience.", ware.experience)), UUID.randomUUID());
        }

    }
}
