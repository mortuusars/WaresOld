package io.github.mortuusars.wares.core.ware.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mortuusars.wares.core.types.IntegerRange;
import io.github.mortuusars.wares.lib.enums.TimeOfDay;

public record DeliveryTimeData(TimeOfDay timeOfDay, Either<Integer, IntegerRange> days){
    public static Codec<DeliveryTimeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TimeOfDay.CODEC.fieldOf("time").forGetter(DeliveryTimeData::timeOfDay),
            Codec.either(Codec.intRange(0, Integer.MAX_VALUE), IntegerRange.CODEC).fieldOf("days").forGetter(DeliveryTimeData::days))
        .apply(instance, DeliveryTimeData::new));

    public static DeliveryTimeData nextMorning(){
        return new DeliveryTimeData(TimeOfDay.MORNING, Either.left(1));
    }

    public static DeliveryTimeData nextDay(){
        return new DeliveryTimeData(TimeOfDay.DAY, Either.left(1));
    }

}
