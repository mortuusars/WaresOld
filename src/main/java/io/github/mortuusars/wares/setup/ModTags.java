package io.github.mortuusars.wares.setup;

import io.github.mortuusars.wares.Wares;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static final TagKey<Block> SHIPMENT_CONVERTIBLE = BlockTags.create(new ResourceLocation(Wares.MOD_ID, "shipment_convertible"));
}
