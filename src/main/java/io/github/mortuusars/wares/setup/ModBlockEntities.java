package io.github.mortuusars.wares.setup;

import io.github.mortuusars.wares.common.delivery_note.DeliveryNoteBlockEntity;
import io.github.mortuusars.wares.common.mailbox.MailboxBlockEntity;
import io.github.mortuusars.wares.common.parcel.ParcelBlockEntity;
import io.github.mortuusars.wares.common.shipping_crate.ShippingCrateBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final RegistryObject<BlockEntityType<MailboxBlockEntity>> MAILBOX =
            Registries.BLOCK_ENTITIES.register("mailbox_block_entity",
                    () -> BlockEntityType.Builder.of(MailboxBlockEntity::new, ModBlocks.MAILBOX.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ShippingCrateBlockEntity>> SHIPPING_CRATE =
            Registries.BLOCK_ENTITIES.register("shipping_crate_block_entity",
                    () -> BlockEntityType.Builder.of(ShippingCrateBlockEntity::new, ModBlocks.SHIPPING_CRATE.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<DeliveryNoteBlockEntity>> DELIVERY_NOTE =
            Registries.BLOCK_ENTITIES.register("delivery_note_block_entity",
                    () -> BlockEntityType.Builder.of(DeliveryNoteBlockEntity::new, ModBlocks.DELIVERY_NOTE.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ParcelBlockEntity>> PAYMENT_PARCEL =
            Registries.BLOCK_ENTITIES.register("payment_parcel_block_entity",
                    () -> BlockEntityType.Builder.of(ParcelBlockEntity::new, ModBlocks.PAYMENT_PARCEL.get())
                            .build(null));

    public static void register() {}
}
