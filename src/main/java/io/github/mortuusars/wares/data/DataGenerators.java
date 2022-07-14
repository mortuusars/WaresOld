package io.github.mortuusars.wares.data;

import io.github.mortuusars.wares.Wares;
import io.github.mortuusars.wares.data.providers.ModBlockStatesProvider;
import io.github.mortuusars.wares.data.providers.ModBlockTagsProvider;
import io.github.mortuusars.wares.data.providers.ModItemModelsProvider;
import io.github.mortuusars.wares.data.providers.ModLanguageProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Wares.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();

        if (event.includeServer()){
            ModBlockTagsProvider blockTagsGenerator = new ModBlockTagsProvider(generator, event.getExistingFileHelper());
            generator.addProvider(blockTagsGenerator);
//            generator.addProvider(new ModItemTagsProvider(generator, blockTagsGenerator, event.getExistingFileHelper()));
//            generator.addProvider(new ModLootTablesProvider(generator));
//            generator.addProvider(new ModRecipeProvider(generator));
        }

        if (event.includeClient()){
            generator.addProvider(new ModBlockStatesProvider(generator, event.getExistingFileHelper()));
            generator.addProvider(new ModItemModelsProvider(generator, event.getExistingFileHelper()));
            generator.addProvider(new ModLanguageProvider(generator, "en_us"));
//            generator.addProvider(new ModLanguageProvider(generator, "uk_ua"));
        }
    }
}
