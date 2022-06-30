package io.github.mortuusars.wares;

import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import io.github.mortuusars.wares.core.Ware;
import io.github.mortuusars.wares.core.WareItem;
import io.github.mortuusars.wares.setup.ClientSetup;
import io.github.mortuusars.wares.setup.Registries;
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
        wares.add(new Ware());
        return wares;
    }

    public void onPlayerRightClick(PlayerInteractEvent event){

//        if (event.getPlayer().level.isClientSide)
//            return;
//
//        if (!event.getPlayer().isSecondaryUseActive())
//            return;
//
//        NonNullList<Ware> wares = getWares();
//        Level level = event.getPlayer().getLevel();
//        BlockPos playerPos = event.getPlayer().blockPosition();
//
//        Ware ware = wares.get(new Random().nextInt(0, wares.size()));
//        NonNullList<ItemStack> paymentItemStacks = ware.getPaymentItemStacks();
//
//        event.getPlayer().sendMessage(new TextComponent(paymentItemStacks.size() + " PaymentItem(s):"), UUID.randomUUID());
//
//        for (ItemStack stack : paymentItemStacks){
//            level.addFreshEntity(new ItemEntity(level, playerPos.getX(), playerPos.getY(), playerPos.getZ(), stack));
//            event.getPlayer().sendMessage(new TextComponent(stack.toString()), UUID.randomUUID());
//        }
//
//        if (ware.experience > 0){
//            level.addFreshEntity(new ExperienceOrb(level, playerPos.getX(), playerPos.getY(), playerPos.getZ(), ware.experience));
//            event.getPlayer().sendMessage(new TextComponent(String.format("Dropped %s experience.", ware.experience)), UUID.randomUUID());
//        }
//
//
//        String json = new Gson().toJson(ware);
//        Ware deserWare = new Gson().fromJson(json, Ware.class);
//        LOGGER.info(json);
    }
}
