package io.github.mortuusars.wares.utils;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Utils {

    public static class VoxelShape {
        public static net.minecraft.world.phys.shapes.VoxelShape rotateShape(Direction from, Direction to, net.minecraft.world.phys.shapes.VoxelShape shape)
        {
            if (from.getAxis() == Direction.Axis.Y || to.getAxis() == Direction.Axis.Y)
                throw new IllegalArgumentException("Invalid Direction!");

            if (from == to) { return shape; }

            net.minecraft.world.phys.shapes.VoxelShape[] buffer = new net.minecraft.world.phys.shapes.VoxelShape[] { shape, Shapes.empty() };

            int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
            for (int i = 0; i < times; i++)
            {
                buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(
                        buffer[1],
                        Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)
                ));
                buffer[0] = buffer[1];
                buffer[1] = Shapes.empty();
            }

            return buffer[0];
        }
    }

}
