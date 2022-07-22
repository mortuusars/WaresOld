package io.github.mortuusars.wares.data.providers;

import io.github.mortuusars.wares.Wares;
import io.github.mortuusars.wares.setup.ModBlocks;
import io.github.mortuusars.wares.setup.ModLangKeys;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    private final String _locale;

    public ModLanguageProvider(DataGenerator gen, String locale) {
        super(gen, Wares.MOD_ID, locale);
        _locale = locale;
    }

    @Override
    protected void addTranslations() {
        switch (_locale) {
            case "en_us" -> genEN_US();
            case "uk_ua" -> genUK_UA();
            default -> throw new IllegalArgumentException("This locale is not supported for generation.");
        }
    }

    private void genEN_US() {
        add(ModBlocks.CRATE.get(), "Crate");
        add(ModBlocks.SHIPPING_CRATE.get(), "Shipping Crate");

        add(ModLangKeys.CONTAINER_MAILBOX, "Mailbox");
        add(ModLangKeys.CONTAINER_PURCHASE_REQUEST, "Purchase Request");
        add(ModLangKeys.CONTAINER_CRATE, "Crate");
        add(ModLangKeys.CONTAINER_SHIPPING_CRATE, "Shipping Crate");
        add(ModLangKeys.CONTAINER_PARCEL, "Parcel");
        add(ModLangKeys.GUI_MAILBOX_TRASH_CAN_TOOLTIP, "Trash Can");
        add(ModLangKeys.GUI_PURCHASE_REQUEST_TITLE, "Purchase Request");
        add(ModLangKeys.GUI_PURCHASE_REQUEST_FROM, "From");
        add(ModLangKeys.GUI_PURCHASE_REQUEST_EXPERIENCE_TOOLTIP, "Experience reward");
    }

    private void genUK_UA() {
    }
}
