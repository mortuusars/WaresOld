package io.github.mortuusars.wares;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.wares.setup.ClientSetup;
import io.github.mortuusars.wares.setup.Registries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Wares.MOD_ID)
public class Wares
{
    public static final String MOD_ID = "wares";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Wares()
    {
        Registries.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::setup);
    }
}
