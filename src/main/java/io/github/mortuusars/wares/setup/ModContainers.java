package io.github.mortuusars.wares.setup;

import io.github.mortuusars.wares.inventory.menu.ShippingCrateMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.RegistryObject;

public class ModContainers {
    public static final RegistryObject<MenuType<ShippingCrateMenu>> SHIPPING_CRATE = Registries.MENU_TYPES.register(
                    "shipping_crate",
                    () -> IForgeMenuType.create((windowId, inv, data) -> new ShippingCrateMenu(windowId, inv, data.readBlockPos()) ));

    public static void register() { }
}
