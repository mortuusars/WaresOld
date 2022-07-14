package io.github.mortuusars.wares.core.ware;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record WareDescription(String title, String message, String buyer, boolean randomBuyer) {

    public static Codec<WareDescription> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("title", "").forGetter(WareDescription::title),
            Codec.STRING.optionalFieldOf("message", "").forGetter(WareDescription::message),
            Codec.STRING.optionalFieldOf("buyer", "").forGetter(WareDescription::buyer),
            Codec.BOOL.optionalFieldOf("randomBuyer", true).forGetter(WareDescription::randomBuyer))
        .apply(instance, WareDescription::new));

    public static WareDescription EMPTY = new WareDescription("", "", "", true);

}
