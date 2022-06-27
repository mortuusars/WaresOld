package io.github.mortuusars.wares.setup;

import io.github.mortuusars.wares.content.items.BillOfLadingItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final RegistryObject<BlockItem> DELIVERY_TABLE = Registries.ITEMS.register("delivery_table",
            () -> new BlockItem(ModBlocks.DELIVERY_TABLE.get(), new Item.Properties().tab(Registries.WARES_CREATIVE_TAB)));

    public static final RegistryObject<BlockItem> CRATE = Registries.ITEMS.register("crate",
            () -> new BlockItem(ModBlocks.CRATE.get(), new Item.Properties().tab(Registries.WARES_CREATIVE_TAB)));

    public static final RegistryObject<BlockItem> SHIPPING_CRATE = Registries.ITEMS.register("shipping_crate",
            () -> new BlockItem(ModBlocks.SHIPPING_CRATE.get(), new Item.Properties().tab(Registries.WARES_CREATIVE_TAB)));


    public static final RegistryObject<Item> BILL_OF_LADING = Registries.ITEMS.register("bill_of_lading",
            () -> new BillOfLadingItem(new Item.Properties().tab(Registries.WARES_CREATIVE_TAB).stacksTo(1)));

    public static void register() { }
}
