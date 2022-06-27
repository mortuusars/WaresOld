package io.github.mortuusars.wares.content;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class ShippingCrate {
    public static void convertToShippingCrate(Level level, BlockPos pos, BlockState clickedBlockState, ItemStack heldItemStack){

        if (level.isClientSide){
            spawnConvertedParticles(level, pos);
            return;
        }



    }

    private static void spawnConvertedParticles(Level level, BlockPos pos){
        Random r = level.getRandom();
        for (int i = 0; i < 20; i++) {
            double x = pos.getX() + 0.5d + r.nextFloat(-0.4f, 0.4f);
            double y = pos.getY() + 0.5d + r.nextFloat(-0.4f, 0.4f);
            double z = pos.getZ() + 0.5d + r.nextFloat(-0.4f, 0.4f);
            double velocity = r.nextFloat(-0.1f, 0.1f);
            level.addParticle(ParticleTypes.POOF, x, y, z, velocity, 0.01d, velocity);
        }
    }
}
