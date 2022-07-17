package io.github.mortuusars.wares.setup;

import io.github.mortuusars.wares.common.mailbox.MailboxMenu;
import io.github.mortuusars.wares.common.parcel.ParcelMenu;
import io.github.mortuusars.wares.common.shipping_crate.ShippingCrateMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.RegistryObject;

public class ModContainers {

    public static final RegistryObject<MenuType<MailboxMenu>> MAILBOX = Registries.MENU_TYPES.register(
            "mailbox",
            () -> IForgeMenuType.create((windowId, inv, data) -> new MailboxMenu(windowId, inv, data.readBlockPos()) ));

    public static final RegistryObject<MenuType<ShippingCrateMenu>> SHIPPING_CRATE = Registries.MENU_TYPES.register(
            "shipping_crate",
            () -> IForgeMenuType.create((windowId, inv, data) -> new ShippingCrateMenu(windowId, inv, data.readBlockPos()) ));

    public static final RegistryObject<MenuType<ParcelMenu>> PAYMENT_PARCEL = Registries.MENU_TYPES.register(
            "payment_parcel",
            () -> IForgeMenuType.create((windowId, inv, data) -> new ParcelMenu(windowId, inv, data.readBlockPos()) ));

    public static void register() { }
}
