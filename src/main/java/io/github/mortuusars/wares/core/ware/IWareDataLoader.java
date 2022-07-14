package io.github.mortuusars.wares.core.ware;

import io.github.mortuusars.wares.core.ware.data.WareData;

import java.util.Optional;

public interface IWareDataLoader{
    Optional<WareData> loadWare(String json);
}
