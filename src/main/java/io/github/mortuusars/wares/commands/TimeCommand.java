package io.github.mortuusars.wares.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.mortuusars.wares.lib.enums.TimeOfDay;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;

import java.time.LocalTime;

public class TimeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        pDispatcher.register(Commands.literal("time")
                /*.requires((commandSourceStack) -> commandSourceStack.hasPermission(2))*/
                .then(Commands.literal("get")
                        .executes(cmd -> {
                            try {
                                return printCurrentTime(cmd.getSource());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                cmd.getSource().sendFailure(new TextComponent("Executing failed: " + ex));
                                return -1;
                            }
                        })));
    }

    private static int printCurrentTime(CommandSourceStack source) {
        ServerLevel level = source.getLevel();
        LocalTime currentTime = TimeOfDay.getCurrentTime(level);

        source.sendSuccess(new TextComponent(level.dayTime() % 24000 + " - " + currentTime.toString()), true);
        return 0;
    }
}
