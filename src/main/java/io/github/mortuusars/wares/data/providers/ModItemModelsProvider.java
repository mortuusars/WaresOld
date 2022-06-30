package io.github.mortuusars.wares.data.providers;

import io.github.mortuusars.wares.Wares;
import io.github.mortuusars.wares.setup.ModBlocks;
import io.github.mortuusars.wares.setup.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelsProvider extends ItemModelProvider {
    public ModItemModelsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Wares.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(ModItems.DELIVERY_TABLE.get().getRegistryName().getPath(),
                modLoc("block/" + ModBlocks.DELIVERY_TABLE.get().getRegistryName().getPath()));
        withExistingParent(ModItems.CRATE.get().getRegistryName().getPath(),
                modLoc("block/" + ModBlocks.CRATE.get().getRegistryName().getPath()));
        withExistingParent(ModItems.SHIPPING_CRATE.get().getRegistryName().getPath(),
                mcLoc("block/stripped_oak_wood"));

        singleTexture(ModItems.PURCHASE_REQUEST.get().getRegistryName().getPath(), mcLoc("item/generated"), "layer0",
                modLoc("item/" + ModItems.PURCHASE_REQUEST.get().getRegistryName().getPath()));
        singleTexture(ModItems.BILL_OF_LADING.get().getRegistryName().getPath(), mcLoc("item/generated"), "layer0",
                modLoc("item/" + ModItems.BILL_OF_LADING.get().getRegistryName().getPath()));
    }
}
