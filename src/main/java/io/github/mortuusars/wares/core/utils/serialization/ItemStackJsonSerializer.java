package io.github.mortuusars.wares.core.utils.serialization;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Type;

public class ItemStackJsonSerializer implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull()){
            LogUtils.getLogger().warn("Null JsonElement when deserializing ItemStack.");
            return ItemStack.EMPTY;
        }

        DataResult<Pair<ItemStack, JsonElement>> decodeResult = ItemStack.CODEC.decode(JsonOps.INSTANCE, json);
        if (decodeResult.get().left().isPresent())
            return decodeResult.get().left().get().getFirst();

        LogUtils.getLogger().error("Failed to deserialize '{}'. ", json);
        return ItemStack.EMPTY;
    }

    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        DataResult<JsonElement> encodeResult = ItemStack.CODEC.encode(src, JsonOps.INSTANCE, null);
        if (encodeResult.get().left().isPresent())
            return encodeResult.get().left().get();

        LogUtils.getLogger().error("Failed to serialize '{}'. ", src);
        return null;
    }
}
