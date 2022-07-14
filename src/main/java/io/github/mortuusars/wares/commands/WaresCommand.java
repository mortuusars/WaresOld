package io.github.mortuusars.wares.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import io.github.mortuusars.wares.Wares;
import io.github.mortuusars.wares.core.ware.data.Ware;
import io.github.mortuusars.wares.core.ware.data.WareData;
import io.github.mortuusars.wares.setup.ModItems;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

import java.util.Optional;

public class WaresCommand {
    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        pDispatcher.register(Commands.literal("wares")
                .requires((commandSourceStack) -> commandSourceStack.hasPermission(2))
                .then(Commands.literal("randomPurchaseRequest")
                        .executes(cmd -> {
                            try {
                                return giveRandomPurchaseRequest(cmd.getSource());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                cmd.getSource().sendFailure(new TextComponent("Executing failed: " + ex));
                                return -1;
                            }
                        }))
                .then(Commands.literal("list")
                        .executes(cmd -> {
                            try {
                                return showAllWares(cmd.getSource());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                cmd.getSource().sendFailure(new TextComponent("Executing failed: " + ex));
                                return -1;
                            }
                        })));
    }

    private static int showAllWares(CommandSourceStack source){
        ImmutableList<WareData> wares = Wares.WARE_STORAGE.getAllWares();
        source.sendSuccess(new TextComponent( wares.size() + " ware(s) are currently loaded."), true);
        source.sendSuccess(new TextComponent( "Details are printed in log."), true);

        Logger logger = LogUtils.getLogger();
        logger.info("Loaded wares:");
        for (WareData ware : wares) {
            var sb = new StringBuilder();
//            if (ware.title() != null) sb.append("Title: ").append(ware.title()).append("\n");
//            if (ware.description() != null) sb.append("Description: ").append(ware.description()).append("\n");
//            if (ware.buyer() != null) sb.append("Seller: ").append(ware.buyer()).append("\n");
            sb.append("Rarity: ").append(ware.rarity()).append("\n");
            sb.append("Weight: ").append(ware.weight()).append("\n");

            sb.append("Requested Items: ");
            if (ware.requestedItems().size() == 0) sb.append("<-no items->\n");
            for (var item : ware.requestedItems())
                sb.append(item.toString());

            sb.append("Payment Items: ");
            if (ware.paymentItems().size() == 0) sb.append("<-no items->\n");
            for (var item : ware.paymentItems())
                sb.append(item.toString());

            sb.append("Experience: ").append(ware.experience()).append("\n");

            logger.info(sb.toString());
        }

        return 0;
    }

    private static int giveRandomPurchaseRequest(CommandSourceStack source){
//        ServerPlayer player;
//        try {
//            player = source.getPlayerOrException();
//        } catch (CommandSyntaxException e) {
//            source.sendFailure(new TextComponent("Player is required to execute this command"));
//            return -1;
//        }
//
//        Optional<Ware> randomWareOptional = Wares.WARE_STORAGE.getRandomWare();
//        if (randomWareOptional.isEmpty())
//        {
//            source.sendFailure(new TextComponent("Cannot get random ware. No wares is defined or error has occurred."));
//            return -1;
//        }
//        Ware randomWare = randomWareOptional.get();
//        ItemStack requestStack = new ItemStack(ModItems.PURCHASE_REQUEST.get());
//        if (WareUtils.saveToStackNBT(randomWare, requestStack)){
//            int freeSlot = player.getInventory().getFreeSlot();
//            player.getInventory().add(freeSlot, requestStack);
//            source.sendSuccess(new TextComponent("Random request was given."), false);
//            return 0;
//        }
//        else {
//            source.sendFailure(new TextComponent("Cannot get random ware. Saving ware to item NBT failed. Check logs for info."));
//            return -1;
//        }

        return -1;
    }
}
