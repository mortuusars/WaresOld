package io.github.mortuusars.wares;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.wares.commands.WaresCommand;
import io.github.mortuusars.wares.core.ware.WareStorage;
import io.github.mortuusars.wares.setup.ClientSetup;
import io.github.mortuusars.wares.setup.Registries;
import net.minecraft.world.InteractionHand;
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

    public static final WareStorage WARE_STORAGE = WareStorage.INSTANCE;
    public static final Logger LOGGER = LogUtils.getLogger();

    public Wares()
    {
        Registries.init();

        // Testing
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerRightClick);

        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::setup);
    }

    public void onPlayerRightClick(PlayerInteractEvent event){

        if (event.getWorld().isClientSide || !event.getPlayer().isSecondaryUseActive() || event.getHand() == InteractionHand.OFF_HAND)
            return;

    }

    public void onRegisterCommands(RegisterCommandsEvent event){
        WaresCommand.register(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }
}
