package io.github.mortuusars.wares.setup;

import io.github.mortuusars.wares.common.delivery_note.DeliveryNoteBlockEntity;
import io.github.mortuusars.wares.common.payment_parcel.PaymentParcelBlockEntity;
import io.github.mortuusars.wares.common.shipping_crate.ShippingCrateBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final RegistryObject<BlockEntityType<ShippingCrateBlockEntity>> SHIPPING_CRATE =
            Registries.BLOCK_ENTITIES.register("shipping_crate_block_entity",
                    () -> BlockEntityType.Builder.of(ShippingCrateBlockEntity::new, ModBlocks.SHIPPING_CRATE.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<DeliveryNoteBlockEntity>> DELIVERY_NOTE =
            Registries.BLOCK_ENTITIES.register("delivery_note_block_entity",
                    () -> BlockEntityType.Builder.of(DeliveryNoteBlockEntity::new, ModBlocks.DELIVERY_NOTE.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<PaymentParcelBlockEntity>> PAYMENT_PARCEL =
            Registries.BLOCK_ENTITIES.register("payment_parcel_block_entity",
                    () -> BlockEntityType.Builder.of(PaymentParcelBlockEntity::new, ModBlocks.PAYMENT_PARCEL.get())
                            .build(null));

    public static void register() {}
}
