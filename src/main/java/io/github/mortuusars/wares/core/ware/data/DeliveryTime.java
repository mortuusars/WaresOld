package io.github.mortuusars.wares.core.ware.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mortuusars.wares.lib.enums.TimeOfDay;

public record DeliveryTime(TimeOfDay timeOfDay, int days){
    public static Codec<DeliveryTime> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    TimeOfDay.CODEC.fieldOf("time").forGetter(DeliveryTime::timeOfDay),
                    Codec.intRange(0, Integer.MAX_VALUE).fieldOf("days").forGetter(DeliveryTime::days))
            .apply(instance, DeliveryTime::new));
}
