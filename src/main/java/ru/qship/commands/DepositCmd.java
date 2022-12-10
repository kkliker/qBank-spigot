package ru.qship.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.qship.Note;
import ru.qship.util.Lang;
import ru.qship.util.NoteManager;
import ru.qship.util.Permission;

public class DepositCmd implements CommandExecutor {


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length == 0) {
                if (Permission.DEPOSIT.has(player)) {
                    if (Note.isNote(player.getItemInHand())) {
                        NoteManager.deposit(player);
                    } else {
                        Lang.DEPOSIT_INVALID_NOTE.msg(player);
                    }
                } else {
                    Lang.COMMAND_NO_PERMISSION.msg(player);
                }
            } else if (args[0].equalsIgnoreCase("mass")) {
                if (Permission.DEPOSIT_MASS.has(player)) {
                    NoteManager.massDeposit(player);
                } else {
                    Lang.COMMAND_NO_PERMISSION.msg(player);
                }
            }
        } else {
            Lang.PLAYER_ONLY_COMMAND.msg(sender);
        }

        return true;
    }
}