package io.github.mortuusars.wares.data.providers;

import io.github.mortuusars.wares.Wares;
import io.github.mortuusars.wares.setup.ModBlocks;
import io.github.mortuusars.wares.setup.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class ModItemModelsProvider extends ItemModelProvider {
    public ModItemModelsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Wares.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(itemPath(ModItems.CRATE), modLoc("block/" + blockPath(ModBlocks.CRATE)));
        withExistingParent(itemPath(ModItems.SHIPPING_CRATE), modLoc("block/" + blockPath(ModBlocks.SHIPPING_CRATE)));

        singleTexture(itemPath(ModItems.PURCHASE_REQUEST), mcLoc("item/generated"), "layer0",
                modLoc("item/" + itemPath(ModItems.PURCHASE_REQUEST)));
//        singleTexture(itemPath(ModItems.BILL_OF_LADING), mcLoc("item/generated"), "layer0",
//                modLoc("item/" + itemPath(ModItems.BILL_OF_LADING)));

        singleTexture(itemPath(ModItems.DELIVERY_NOTE), mcLoc("item/generated"), "layer0", modLoc("item/" + itemPath(ModItems.DELIVERY_NOTE)));
    }

    private String itemPath(RegistryObject<? extends Item> registryObject){
        return Objects.requireNonNull(registryObject.get().getRegistryName()).getPath();
    }

    private String blockPath(RegistryObject<? extends Block> registryObject){
        return Objects.requireNonNull(registryObject.get().getRegistryName()).getPath();
    }
}
