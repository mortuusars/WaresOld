package io.github.mortuusars.wares.setup;

import io.github.mortuusars.wares.content.blockentities.ShippingCrateBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final RegistryObject<BlockEntityType<ShippingCrateBlockEntity>> SHIPPING_CRATE_BLOCK_ENTITY =
            Registries.BLOCK_ENTITIES.register("shipping_crate_block_entity",
                    () -> BlockEntityType.Builder.of(ShippingCrateBlockEntity::new, ModBlocks.SHIPPING_CRATE.get())
                            .build(null));

    public static void register() {}
}
