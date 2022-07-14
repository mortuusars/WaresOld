package io.github.mortuusars.wares.core;

import io.github.mortuusars.wares.core.ware.WareData;

import java.util.Optional;

public interface IWareDataLoader{
    Optional<WareData> loadWare(String json);
}
