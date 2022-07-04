package io.github.mortuusars.wares.utils;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import org.lwjgl.system.CallbackI;

public class PosUtils {
    public static Vector3f blockCenter(BlockPos pos){
        return new Vector3f(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
    }
}
