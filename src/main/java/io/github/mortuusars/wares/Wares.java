package io.github.mortuusars.wares;

import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import io.github.mortuusars.wares.commands.WaresCommand;
import io.github.mortuusars.wares.core.ware.PotentialWare;
import io.github.mortuusars.wares.core.ware.WareStorage;
import io.github.mortuusars.wares.setup.ClientSetup;
import io.github.mortuusars.wares.setup.Registries;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.command.ConfigCommand;
import org.slf4j.Logger;

@Mod(Wares.MOD_ID)
public class Wares
{
    public static final String MOD_ID = "wares";
    public static final WareStorage WARE_STORAGE = new WareStorage();
    private static final Logger LOGGER = LogUtils.getLogger();

    public Wares()
    {
        Registries.init();

        // Testing
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerRightClick);

        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::setup);

        // TODO proper ware loading and reloading
        WARE_STORAGE.loadWares();
    }

    public void onPlayerRightClick(PlayerInteractEvent event){

//        if (event.getWorld().isClientSide)
//            return;
//
//        if (event.getHand() == InteractionHand.OFF_HAND)
//            return;
//
//        try{
//            ItemStack held = event.getItemStack();
//            Item item = held.getItem();
//            String stringTag = held.getTag().toString();
//
//            CompoundTag tagParserTag = TagParser.parseTag(stringTag);
//
//            ItemStack resurrected = new ItemStack(item);
//
//            CompoundTag tagFromString = (CompoundTag)JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, JsonParser.parseString(stringTag));
//            resurrected.setTag(tagFromString);
//
//            event.getPlayer().getInventory().add(resurrected);
//        }
//        catch (Throwable t){
//
//        }
    }

    public void onRegisterCommands(RegisterCommandsEvent event){
        WaresCommand.register(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }
}
