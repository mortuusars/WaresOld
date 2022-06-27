package io.github.mortuusars.wares.data.providers;

import io.github.mortuusars.wares.Wares;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelsProvider extends ItemModelProvider {
    public ModItemModelsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Wares.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent("delivery_table", modLoc("block/delivery_table"));
        withExistingParent("crate", modLoc("block/crate"));
        withExistingParent("shipping_crate", mcLoc("block/stripped_oak_wood"));

        singleTexture("bill_of_lading", mcLoc("item/generated"), "layer0", modLoc("item/bill_of_lading"));
    }
}
