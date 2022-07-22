package io.github.mortuusars.wares.setup;

import io.github.mortuusars.wares.Wares;

public class ModLangKeys {
    public static final String CONTAINER_PURCHASE_REQUEST = key("container", "purchase_request");
    public static final String GUI_PURCHASE_REQUEST_TITLE = key("gui", "purchase_request.title");
    public static final String GUI_PURCHASE_REQUEST_FROM = key("gui", "purchase_request.from");
    public static final String GUI_PURCHASE_REQUEST_EXPERIENCE_TOOLTIP = key("gui","experience_tooltip");

    public static final String CONTAINER_CRATE = key("container", "crate");
    public static final String CONTAINER_SHIPPING_CRATE = key("container", "shipping_crate");

    public static final String CONTAINER_MAILBOX = key("container", "mailbox");
    public static final String GUI_MAILBOX_TRASH_CAN_TOOLTIP = key("gui", "mailbox.trash_can_tooltip");

    public static final String CONTAINER_PARCEL = key("container", "parcel");

    private static String key(String category, String key){
        return category + "." + Wares.MOD_ID + "." + key;
    }
}
