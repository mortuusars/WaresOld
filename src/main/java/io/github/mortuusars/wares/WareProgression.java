package io.github.mortuusars.wares;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.wares.common.ShippingCrate;
import io.github.mortuusars.wares.common.blockentities.DeliveryNoteBlockEntity;
import io.github.mortuusars.wares.core.Delivery;
import io.github.mortuusars.wares.core.ware.Ware;
import io.github.mortuusars.wares.setup.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class WareProgression {
    public static void shipWare(Ware ware, Player player, Level level, BlockPos pos, ShippingCrate.Shipping shippingResult) {
        BlockState noteBlockState = ModBlocks.DELIVERY_NOTE.get().defaultBlockState();

        Delivery delivery = new Delivery(ware);
        delivery.excessItems = shippingResult.excessItems;
        delivery.deliveryTime = ware.deliveryTime.getRandom(level.random);
        delivery.shippingDay = (int)(level.getDayTime() % 24000);

        level.setBlock(pos, noteBlockState, Block.UPDATE_ALL);

        if ( !(level.getBlockEntity(pos) instanceof DeliveryNoteBlockEntity deliveryEntity) ){
            level.removeBlock(pos, false);
            LogUtils.getLogger().error("Failed to place delivery note: DeliveryNoteBlockEntity was not found on pos '{}'", pos);
            return;
        }

        deliveryEntity.setDelivery(delivery);
    }
}
