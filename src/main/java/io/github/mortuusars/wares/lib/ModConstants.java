package io.github.mortuusars.wares.lib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Random;

public class ModConstants {

    public static final Random RANDOM = new Random();

    public static final TagKey<Item> EMPTY_ITEM_TAG = ItemTags.create(new ResourceLocation("wares:empty_tag"));

    public static final String WARE_NBT_KEY = "wares:ware";

}
