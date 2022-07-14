package io.github.mortuusars.wares;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import io.github.mortuusars.wares.core.WareDataLoader;
import io.github.mortuusars.wares.core.ware.*;
import io.github.mortuusars.wares.lib.ModPaths;
import net.minecraft.core.NonNullList;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static io.github.mortuusars.wares.Wares.LOGGER;

@Mod.EventBusSubscriber(modid = Wares.MOD_ID)
public class WareStorage implements PreparableReloadListener {

    private WareStorage(){}
    public static final WareStorage INSTANCE = new WareStorage();

    public WareFinalizer wareFinalizer = new WareFinalizer();

    private NonNullList<WareData> wares = NonNullList.create();
    boolean loadDefaultWares = true;

    public ImmutableList<WareData> getAllWares(){
        return ImmutableList.<WareData>builder().addAll(wares).build();
    }

    public Optional<Ware> getRandomWare(){

        //TODO Roll for rarity

        //TODO Weighted random

        if (wares.size() > 0){
            WareData wareData = wares.get(new Random().nextInt(0, wares.size()));
            Ware finalWare = wareFinalizer.finalize(wareData);
            return Optional.of(finalWare);
        }

        return Optional.empty();
    }

    public void loadWares(){
        LOGGER.info("Loading wares...");

        WareDataLoader wareDataLoader = new WareDataLoader();

        NonNullList<WareData> newWares = NonNullList.create();

        try {
            Files.walk(ModPaths.WARES).forEach(path -> {
                if (!Files.isDirectory(path)
                        && path.toString().endsWith(".json")
                        && (!path.startsWith(ModPaths.DEFAULT_WARES) || loadDefaultWares)){ // TODO: Config for default wares.

                    try {
                        String json = Files.readString(path);
                        wareDataLoader.loadWare(json).ifPresent(newWares::add);
                    } catch (Exception e) { e.printStackTrace(); }
                }
            });
        } catch (IOException e) { e.printStackTrace(); }

        wares = newWares;
        LOGGER.info(wares.size() > 0 ? "Loaded " + wares.size() + " ware(s)." : "No wares were loaded.") ;
    }


    // <Reloading>

    @SubscribeEvent
    public static void addReloadListener(AddReloadListenerEvent event)
    {
        event.addListener(INSTANCE);
    }

    @Override
    public @NotNull CompletableFuture<Void> reload(PreparationBarrier pPreparationBarrier, ResourceManager pResourceManager, ProfilerFiller pPreparationsProfiler, ProfilerFiller pReloadProfiler, Executor pBackgroundExecutor, Executor pGameExecutor) {
        return CompletableFuture.allOf(CompletableFuture.runAsync(this::loadWares, pBackgroundExecutor))
                .thenCompose(pPreparationBarrier::wait);
    }
}
