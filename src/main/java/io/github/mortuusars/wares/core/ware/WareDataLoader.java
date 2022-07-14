package io.github.mortuusars.wares.core.ware;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import io.github.mortuusars.wares.core.ware.data.WareData;
import net.minecraft.util.GsonHelper;

import java.util.Optional;

import static io.github.mortuusars.wares.Wares.LOGGER;

public class WareDataLoader implements IWareDataLoader {

    public Optional<WareData> loadWare(String wareJson){

        JsonElement wareJsonElement;
        try {
            wareJsonElement = GsonHelper.parse(wareJson);
        } catch (Exception e) {
            LOGGER.error("Cannot parse ware json file to JsonElement: {}", wareJson);
            e.printStackTrace();
            return Optional.empty();
        }

        DataResult<WareData> parsedWareData;

        try {
            parsedWareData = WareData.CODEC.parse(JsonOps.INSTANCE, wareJsonElement);
        } catch (Exception e) {
            LOGGER.error("Cannot parse ware from wareJson: {}", wareJson);
            e.printStackTrace();
            return Optional.empty();
        }

        return parsedWareData.resultOrPartial(e -> LOGGER.error("Parsing failed: {}", e));
    }
}
