package io.github.mortuusars.wares.setup;

import io.github.mortuusars.wares.common.crate.CrateBlock;
import io.github.mortuusars.wares.common.delivery_note.DeliveryNoteBlock;
import io.github.mortuusars.wares.common.parcel.ParcelBlock;
import io.github.mortuusars.wares.common.shipping_crate.ShippingCrateBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final RegistryObject<CrateBlock> CRATE = Registries.BLOCKS.register("crate",
            () -> new CrateBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).sound(SoundType.LADDER)));

    public static final RegistryObject<ShippingCrateBlock> SHIPPING_CRATE = Registries.BLOCKS.register("shipping_crate",
            () -> new ShippingCrateBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).sound(SoundType.LADDER)));

    public static final RegistryObject<DeliveryNoteBlock> DELIVERY_NOTE = Registries.BLOCKS.register("delivery_note",
            () -> new DeliveryNoteBlock(BlockBehaviour.Properties.of(Material.CACTUS).sound(SoundType.AZALEA).noOcclusion()));

    public static final RegistryObject<ParcelBlock> PAYMENT_PARCEL = Registries.BLOCKS.register("payment_parcel",
            () -> new ParcelBlock(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.SPORE_BLOSSOM).noOcclusion()));

    public static void register() { }
}
