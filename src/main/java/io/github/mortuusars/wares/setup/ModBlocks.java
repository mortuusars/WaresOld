package io.github.mortuusars.wares.setup;

import io.github.mortuusars.wares.common.blocks.CrateBlock;
import io.github.mortuusars.wares.common.blocks.DeliveryTableBlock;
import io.github.mortuusars.wares.common.blocks.ShippingCrateBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final RegistryObject<DeliveryTableBlock> DELIVERY_TABLE = Registries.BLOCKS.register("delivery_table",
            () -> new DeliveryTableBlock(BlockBehaviour.Properties.of(Material.WOOD)));

    public static final RegistryObject<CrateBlock> CRATE = Registries.BLOCKS.register("crate",
            () -> new CrateBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).sound(SoundType.LADDER)));

    public static final RegistryObject<ShippingCrateBlock> SHIPPING_CRATE = Registries.BLOCKS.register("shipping_crate",
            () -> new ShippingCrateBlock(BlockBehaviour.Properties.of(Material.WOOD)));

    public static void register() { }
}
