package io.github.mortuusars.wares.data.providers;

import io.github.mortuusars.wares.Wares;
import io.github.mortuusars.wares.setup.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class ModBlockStatesProvider extends BlockStateProvider {
    public ModBlockStatesProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Wares.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {


        BlockModelBuilder deliveryTableModel = models().withExistingParent("delivery_table", mcLoc("minecraft:block/block"))
                .texture("leg", mcLoc("block/netherite_block"))
                .texture("wood", mcLoc("block/spruce_planks"))
                .element()
                .from(4, 0, 4)
                .to(12, 3, 12)
                .allFaces(((direction, faceBuilder) -> faceBuilder.texture("#leg")))
                .end()

                .element()
                .from(6, 3, 6)
                .to(10, 13, 10)
                .allFaces(((direction, faceBuilder) -> faceBuilder.texture("#leg")))
                .end()

                .element()
                .from(0, 13, 0)
                .to(16, 16, 16)
                .allFaces(((direction, faceBuilder) -> faceBuilder.texture("#wood")))
                .end();

        simpleBlock(ModBlocks.DELIVERY_TABLE.get(), deliveryTableModel);

        crate();

        BlockModelBuilder shippingCrateModel = models()
                .withExistingParent(blockPath(ModBlocks.SHIPPING_CRATE), modLoc("block/" + blockPath(ModBlocks.CRATE)))
                .texture("up", modLoc("block/" + blockPath(ModBlocks.SHIPPING_CRATE)));
        directionalBlock(ModBlocks.SHIPPING_CRATE.get(), (blockState -> blockState.getValue(BarrelBlock.OPEN) ?
                models().withExistingParent(blockPath(ModBlocks.SHIPPING_CRATE) + "_opened", modLoc("block/" + blockPath(ModBlocks.CRATE) + "_opened")) :
                shippingCrateModel));
    }

    private void crate() {
        ResourceLocation top = modLoc("block/crate");
        ResourceLocation side = modLoc("block/crate_side");
        ResourceLocation topOpened = modLoc("block/crate_opened");

        BlockModelBuilder crateModel = models()
                .cubeTop(blockPath(ModBlocks.CRATE), side, top)
                .texture("particle", top);

        BlockModelBuilder crateOpenedModel = models()
                .withExistingParent(blockPath(ModBlocks.CRATE) + "_opened", modLoc(blockPath(ModBlocks.CRATE)))
                .texture("up", topOpened);

        directionalBlock(ModBlocks.CRATE.get(), (blockState -> blockState.getValue(BarrelBlock.OPEN) ? crateOpenedModel : crateModel));
    }

    private String blockPath(RegistryObject<? extends Block> registryObject){
        return Objects.requireNonNull(registryObject.get().getRegistryName()).getPath();
    }
}
