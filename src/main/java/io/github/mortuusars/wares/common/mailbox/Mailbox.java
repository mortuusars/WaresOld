package io.github.mortuusars.wares.common.mailbox;

import io.github.mortuusars.wares.Wares;
import io.github.mortuusars.wares.core.ware.data.Ware;
import io.github.mortuusars.wares.setup.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static io.github.mortuusars.wares.Wares.LOGGER;

public class Mailbox {
    public static final int SLOTS = 21;
    public static final int MAIL_SLOTS = 20;
    public static final int TRASH_SLOT_ID = 20;

    public static final LocalTime workStart = LocalTime.parse("07:00");
    public static final LocalTime workEnd = LocalTime.parse("16:00");

    public static ItemStack getRandomPurchaseRequest(List<ItemStack> existingRequests) {
        Optional<Ware> randomWareOptional = Wares.WARE_STORAGE.getRandomWare();
        if (randomWareOptional.isPresent()){
            ItemStack request = new ItemStack(ModItems.PURCHASE_REQUEST.get());
            if (Ware.writeToStackNBT(request, randomWareOptional.get()))
                return request;
            else LOGGER.error("Ware was not written to the stack nbt. New Purchase Request would not be created.");
        }
        return ItemStack.EMPTY;
    }

    public static boolean isWorkingHours(Level level, LocalTime currentTime){
        if (!level.dimensionType().bedWorks() || level.dimensionType().hasFixedTime())
            return true;

        //TODO: working hours config
        return currentTime.isAfter(workStart) && currentTime.isBefore(workEnd);
    }

    public static double getChanceForCurrentTime(LocalTime currentTime){
        long totalMinutes = Duration.between(workStart, workEnd).toMinutes();
        long passedMinutes = Duration.between(workStart, currentTime).toMinutes();

        // Chance starts at 1.0 at the start of a working day and decreases as the day passes, finishing at the 0.0 at the end of a working day.
        double passedToTotalRatio = ((passedMinutes / (double)totalMinutes) - 1.0);
        LOGGER.info("Chance: " + String.format("%.2f", passedToTotalRatio * -1));
        return passedToTotalRatio * -1;
    }
}
