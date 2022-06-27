package io.github.mortuusars.wares.core;

import java.util.Arrays;
import java.util.Collection;

public class Ware {
    public String title;
    public String description;
    public WareItem[] requested = new WareItem[0];
    public WareItem[] payment = new WareItem[0];
}
