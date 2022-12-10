package ru.qship.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.qship.util.Lang;
import ru.qship.util.NoteManager;
import ru.qship.util.Permission;

public class WithdrawCmd implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!Permission.WITHDRAW.has(player)) {
                Lang.COMMAND_NO_PERMISSION.msg(player);
                return true;
            } else {
                if (args.length == 1) {
                    int amount;
                    try {
                        amount = Integer.parseInt(args[0]);
                    } catch (NumberFormatException var8) {
                        Lang.INVALID_AMOUNT.msg(player);
                        return true;
                    }

                    if (amount <= 0.0) {
                        Lang.INVALID_AMOUNT.msg(player);
                        return true;
                    }

                    NoteManager.withdraw(player, amount);
                } else {
                    Lang.INVALID_ARGUMENTS.msg(player);
                }

                return true;
            }
        } else {
            Lang.PLAYER_ONLY_COMMAND.msg(sender);
            return true;
        }
    }
}
