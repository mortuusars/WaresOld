package io.github.mortuusars.wares.setup;

import io.github.mortuusars.wares.Wares;

public class ModLangKeys {
    public static final String GUI_PURCHASE_REQUEST_TITLE = key("gui", "purchase_request.title");
    public static final String GUI_PURCHASE_REQUEST_FROM = key("gui", "purchase_request.from");
    public static final String GUI_PURCHASE_REQUEST_EXPERIENCE_TOOLTIP = key("gui","experience_tooltip");

    private static String key(String category, String key){
        return category + "." + Wares.MOD_ID + "." + key;
    }
}
