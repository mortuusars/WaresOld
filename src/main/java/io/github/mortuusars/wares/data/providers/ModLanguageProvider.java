package io.github.mortuusars.wares.data.providers;

import io.github.mortuusars.wares.Wares;
import io.github.mortuusars.wares.core.ware.Ware;
import io.github.mortuusars.wares.setup.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraftforge.common.data.LanguageProvider;

import java.io.IOException;

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
    }

    private void genUK_UA() {
    }
}
