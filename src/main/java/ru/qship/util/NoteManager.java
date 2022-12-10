package ru.qship.util;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.qship.BankNotes;
import ru.qship.Note;
import ru.qship.event.NoteEvent;

import java.util.ListIterator;

public class NoteManager {

    public static boolean deposit(Player player) {
        if (Note.isNote(player.getItemInHand())) {
            Note note = Note.fromItem(player.getItemInHand());
            double value = note.getValue();
            NoteEvent event = new NoteEvent(player, value, NoteEvent.NoteEventType.DEPOSIT);
            event.setNoteItem(player.getItemInHand());
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                value = event.getAmount();
                EconomyResponse ecoResponse = BankNotes.notes.economy.depositPlayer(player, value);
                if (ecoResponse.transactionSuccess()) {
                    player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                    Lang.DEPOSIT_SUCCESS.modifiable().replace("{value}", String.valueOf(value)).msg(player);
                    return true;
                }

                return false;
            }
        }

        return false;
    }

    public static boolean massDeposit(Player player) {
        double accumulatedValue = 0.0;


        if(player.getOpenInventory().getTitle().equals("Вставьте купюры в приемник")) {
            ListIterator var3 = player.getOpenInventory().getTopInventory().iterator();

            while (var3.hasNext()) {


                ItemStack item = (ItemStack) var3.next();
                if (Note.isNote(item)) {
                    Note note = Note.fromItem(item);
                    int noteQuantity = item.getAmount();
                    double value = (double) noteQuantity * note.getValue();
                    NoteEvent event = new NoteEvent(player, value, NoteEvent.NoteEventType.DEPOSIT);
                    event.setNoteItem(item);
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        value = event.getAmount();
                        if (BankNotes.notes.economy.depositPlayer(player, value).transactionSuccess()) {
                            accumulatedValue += value;
                            item.setAmount(0);
                            player.updateInventory();
                        }
                    }
                }
            }
        }
            if (accumulatedValue > 0.0) {
                Lang.DEPOSIT_SUCCESS.modifiable().replace("{value}", accumulatedValue).msg(player);
            }

            return true;
        }



    public static boolean withdraw(Player player, int amount) {
        NoteEvent event = new NoteEvent(player, amount, NoteEvent.NoteEventType.WITHDRAW);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            amount = (int) event.getAmount();
            EconomyResponse ecoResponse = BankNotes.notes.economy.withdrawPlayer(player, amount);
            if (ecoResponse.transactionSuccess()) {
                Note note = new Note(amount);
                if (BankNotes.notes.getConfig().getBoolean("issuer.enable", false)) {
                    note = new Note(amount, player.getUniqueId());
                }
                player.getInventory().addItem(new ItemStack[]{note.getItem()});
                Lang.WITHDRAW_SUCCESS.modifiable().replace("{value}", amount).msg(player);
                return true;
            } else {
                Lang.INSUFFICIENT_FUNDS.msg(player);
                return false;
            }
        } else {
            return false;
        }
    }
}