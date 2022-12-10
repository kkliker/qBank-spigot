package ru.qship.util;

import org.bukkit.command.CommandSender;

public enum Permission {
    WITHDRAW,
    DEPOSIT,
    DEPOSIT_MASS,
    ADMIN;

    private String permission = "banknotes." + this.name().toLowerCase().replace('_', '.');


    public boolean has(CommandSender sender) {
        return sender.hasPermission(this.permission);
    }
}