package ru.qship;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Note {
    private double value;
    private UUID uuid;
    private String issuerOverride;

    public Note(double value) {
        this.value = value;
    }

    public Note(double value, UUID uuid) {
        this.value = value;
        this.uuid = uuid;
    }

    public Note(double value, String issuerOverride) {
        this.value = value;
        this.issuerOverride = issuerOverride;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public ItemStack getItem() {
        Material material = Material.matchMaterial(BankNotes.notes.getConfig().getString("note.material", "PAPER"));
        if (material == null) {
            material = Material.PAPER;
        }

        ItemStack bukkitItem = new ItemStack(material);
        ItemMeta meta = bukkitItem.getItemMeta();
        meta.setDisplayName(this.parsePlaceholders(BankNotes.notes.getConfig().getString("note.name", "BankNote")));
        meta.setLore(this.parsePlaceholders(BankNotes.notes.getConfig().getStringList("note.lore")));
        bukkitItem.setItemMeta(meta);
        NBTItem nbtItem = new NBTItem(bukkitItem);
        nbtItem.setDouble("value", this.value);
        if (BankNotes.notes.getConfig().getBoolean("issuer.enable", false)) {
            if (this.uuid != null) {
                nbtItem.setString("issuer", this.uuid.toString());
            } else {
                nbtItem.setString("issuer", "admin");
            }
        }

        return nbtItem.getItem();
    }

    public static Note fromItem(ItemStack item) {
        if (isNote(item)) {
            NBTItem nbtItem = new NBTItem(item);
            double value = nbtItem.getDouble("value");
            if (nbtItem.hasKey("issuer")) {
                return nbtItem.getString("issuer").equalsIgnoreCase("admin") ? new Note(value, BankNotes.notes.getConfig().getString("issuer.override", "")) : new Note(value, UUID.fromString(nbtItem.getString("issuer")));
            } else {
                return new Note(value);
            }
        } else {
            return null;
        }
    }

    public static boolean isNote(ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            NBTItem nbtItem = new NBTItem(item);
            return nbtItem.hasKey("value");
        } else {
            return false;
        }
    }

    public String parsePlaceholders(String string) {
        if (this.uuid != null) {
            string = string.replace("{issuer}", Bukkit.getOfflinePlayer(this.uuid).getName());
        } else if (this.issuerOverride != null) {
            string = string.replace("{issuer}", this.issuerOverride);
        }

        string = string.replace("{value}", String.valueOf(this.getValue()));
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public List<String> parsePlaceholders(List<String> list) {
        List<String> parsed = new ArrayList();
        Iterator var3 = list.iterator();

        while(var3.hasNext()) {
            String string = (String)var3.next();
            parsed.add(this.parsePlaceholders(string));
        }

        return parsed;
    }
}