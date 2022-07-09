package io.github.mortuusars.wares.common.delivery_note;

import io.github.mortuusars.wares.core.Delivery;
import io.github.mortuusars.wares.setup.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DeliveryNoteBlockEntity extends BlockEntity {

    private Delivery delivery;

    public DeliveryNoteBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DELIVERY_NOTE_BLOCK_ENTITY.get(), pos, blockState);
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        this.setChanged();
    }

//    @Override
//    protected void saveAdditional(CompoundTag tag) {
//        super.saveAdditional(tag);
//        tag.
//    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState blockState, T blockEntity) {
        if (blockEntity instanceof DeliveryNoteBlockEntity deliveryNoteEntity && deliveryNoteEntity.delivery != null){
             if (level.getDayTime() % 24000 >= deliveryNoteEntity.delivery.deliveryTime){
                 Containers.dropContents(level, pos, new SimpleContainer(
                         deliveryNoteEntity.delivery.ware.getPaymentStacks().toArray(new ItemStack[0])));
                 level.removeBlock(pos, false);
             }
        }
    }

    public void dropNote() {
//        this.level
    }
}