package io.github.mortuusars.wares.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.mortuusars.wares.Wares;
import io.github.mortuusars.wares.core.ware.PotentialWare;
import io.github.mortuusars.wares.core.ware.Ware;
import io.github.mortuusars.wares.core.ware.WareUtils;
import io.github.mortuusars.wares.setup.ModItems;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

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
        ImmutableList<PotentialWare> wares = Wares.WARE_STORAGE.getAllWares();
        source.sendSuccess(new TextComponent( wares.size() + " wares is currently loaded."), true);
        source.sendSuccess(new TextComponent( "Details are printed in log."), true);
        return 0;
    }

    private static int giveRandomPurchaseRequest(CommandSourceStack source){
        ServerPlayer player;
        try {
            player = source.getPlayerOrException();
        } catch (CommandSyntaxException e) {
            source.sendFailure(new TextComponent("Player is required to execute this command"));
            return -1;
        }

        Optional<Ware> randomWareOptional = Wares.WARE_STORAGE.getRandomWare();
        if (randomWareOptional.isEmpty())
        {
            source.sendFailure(new TextComponent("Cannot get random ware. No wares is defined or error has occurred."));
            return -1;
        }
        Ware randomWare = randomWareOptional.get();
        ItemStack requestStack = new ItemStack(ModItems.PURCHASE_REQUEST.get());
        if (WareUtils.saveToStackNBT(randomWare, requestStack)){
            int freeSlot = player.getInventory().getFreeSlot();
            player.getInventory().add(freeSlot, requestStack);
            source.sendSuccess(new TextComponent("Random request was given."), false);
            return 0;
        }
        else {
            source.sendFailure(new TextComponent("Cannot get random ware. Saving ware to item NBT failed. Check logs for info."));
            return -1;
        }
    }
}
