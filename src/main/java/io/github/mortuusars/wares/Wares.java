package io.github.mortuusars.wares;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import io.github.mortuusars.wares.commands.WaresCommand;
import io.github.mortuusars.wares.core.types.IntegerRange;
import io.github.mortuusars.wares.core.ware.*;
import io.github.mortuusars.wares.core.ware.item.PaymentItem;
import io.github.mortuusars.wares.core.ware.item.PaymentItemData;
import io.github.mortuusars.wares.core.ware.item.RequestedItemData;
import io.github.mortuusars.wares.core.ware.item.WareItem;
import io.github.mortuusars.wares.lib.ModPaths;
import io.github.mortuusars.wares.lib.enums.Rarity;
import io.github.mortuusars.wares.lib.enums.TimeOfDay;
import io.github.mortuusars.wares.setup.ClientSetup;
import io.github.mortuusars.wares.setup.ModItems;
import io.github.mortuusars.wares.setup.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.JsonUtils;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.command.ConfigCommand;
import org.apache.commons.codec.language.bm.Lang;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

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
