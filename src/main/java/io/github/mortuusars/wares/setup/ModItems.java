package io.github.mortuusars.wares.setup;

import io.github.mortuusars.wares.common.items.PurchaseRequestItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final RegistryObject<BlockItem> CRATE = Registries.ITEMS.register("crate",
            () -> new BlockItem(ModBlocks.CRATE.get(), new Item.Properties().tab(Registries.WARES_CREATIVE_TAB)));

    public static final RegistryObject<BlockItem> SHIPPING_CRATE = Registries.ITEMS.register("shipping_crate",
            () -> new BlockItem(ModBlocks.SHIPPING_CRATE.get(), new Item.Properties().tab(Registries.WARES_CREATIVE_TAB)));

    public static final RegistryObject<BlockItem> DELIVERY_NOTE = Registries.ITEMS.register("delivery_note",
            () -> new BlockItem(ModBlocks.DELIVERY_NOTE.get(), new Item.Properties().tab(Registries.WARES_CREATIVE_TAB).stacksTo(1)));

    public static final RegistryObject<BlockItem> PAYMENT_PARCEL = Registries.ITEMS.register("payment_parcel",
            () -> new BlockItem(ModBlocks.PAYMENT_PARCEL.get(), new Item.Properties().tab(Registries.WARES_CREATIVE_TAB)));


    public static final RegistryObject<Item> PURCHASE_REQUEST = Registries.ITEMS.register("purchase_request",
            () -> new PurchaseRequestItem(new Item.Properties().tab(Registries.WARES_CREATIVE_TAB).stacksTo(1)));

    public static void register() { }
}
