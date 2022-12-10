package ru.qship.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class NoteEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private double amount;
    private NoteEventType eventType;
    private ItemStack noteItem;

    public NoteEvent(Player who, double amount, NoteEventType eventType) {
        super(who);
        this.amount = amount;
        this.eventType = eventType;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public NoteEventType getEventType() {
        return this.eventType;
    }

    public ItemStack getNoteItem() {
        return this.noteItem;
    }

    public void setNoteItem(ItemStack noteItem) {
        this.noteItem = noteItem;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public static enum NoteEventType {
        WITHDRAW,
        DEPOSIT;

        private NoteEventType() {
        }
    }
}