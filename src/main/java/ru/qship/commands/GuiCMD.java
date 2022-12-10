package ru.qship.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.clip.placeholderapi.libs.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import ru.qship.BankNotes;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.UUID;

public class GuiCMD implements CommandExecutor {

BankNotes bankNotes = BankNotes.getPlugin(BankNotes.class);

Economy eco = bankNotes.getEconomy();

 ItemStack withdraw = getSkull("https://textures.minecraft.net/texture/7f39ccb5a5cd9babb13097b4f8dbc9f14dc38ac1702a30d97e2aabf907bf15c");
 ItemStack deposite = getSkull("https://textures.minecraft.net/texture/45c0cd717ca9228849652a8ccdf5281a860cb2e79646b56a22bc59110f361da");

 ItemStack money = getSkull("https://textures.minecraft.net/texture/5832fdc38e6a8e05eec396bafb0a81f937a617f15a7509d85dc982dbf0ab5a9f");

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        Component component = Component.text("Банк");

        if (commandSender instanceof Player) {


            String balance = String.valueOf(eco.getBalance((Player) commandSender));

            SkullMeta depositeItemMeta = (SkullMeta) deposite.getItemMeta();
            SkullMeta withdrawItemMeta = (SkullMeta) withdraw.getItemMeta();
            SkullMeta moneyItemMeta = (SkullMeta) money.getItemMeta();


            withdrawItemMeta.setDisplayName(ChatColor.YELLOW + "Снять");
            depositeItemMeta.setDisplayName(ChatColor.YELLOW + "Положить на счет");
            moneyItemMeta.setDisplayName(ChatColor.BLUE + balance + ChatColor.WHITE + "$");

            moneyItemMeta.setLore(Collections.singletonList(ChatColor.WHITE + "Ваш текущий баланс"));



            withdraw.setItemMeta(withdrawItemMeta);
            deposite.setItemMeta(depositeItemMeta);
            money.setItemMeta(moneyItemMeta);


            Player player = (Player) commandSender;

            Inventory inv = Bukkit.createInventory(player,27,"Банк");


            inv.setItem(10,withdraw);
            inv.setItem(16,deposite);
            inv.setItem(13,money);


            player.openInventory(inv);


        }
        return false;
    }


    public ItemStack getSkull(String url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1, (short)3);
        if(url.isEmpty())return head;


        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

}