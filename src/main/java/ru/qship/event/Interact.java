package ru.qship.event;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.qship.Note;
import ru.qship.commands.GuiCMD;
import ru.qship.util.NoteManager;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

public class Interact implements Listener {


    ItemStack accept = getSkull("https://textures.minecraft.net/texture/a79a5c95ee17abfef45c8dc224189964944d560f19a44f19f8a46aef3fee4756");

    @EventHandler
    public void playerInventoryClickEvent(InventoryClickEvent e) {







        Player player = (Player) e.getWhoClicked();
        if (e.getCurrentItem() != null) {


            ItemStack stack = e.getCurrentItem();

            if (e.getView().getTitle().equals("Банк")) {

                e.setCancelled(true);

                if (e.getCurrentItem().getItemMeta() != null ) {
                    if (stack.getType() == Material.PLAYER_HEAD && stack.getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Положить на счет")) {


                        SkullMeta acceptmeta = (SkullMeta) accept.getItemMeta();

                        acceptmeta.setDisplayName(ChatColor.GREEN + "Принять");
                        accept.setItemMeta(acceptmeta);


                        Inventory deposite = Bukkit.createInventory(player, 18, "Вставьте купюры в приемник");

                        deposite.setItem(17, accept);

                        player.openInventory(deposite);


                    }


                    if (stack.getType() == Material.PLAYER_HEAD && stack.getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Снять")) {

                        SkullMeta acceptmeta = (SkullMeta) accept.getItemMeta();

                        acceptmeta.setDisplayName(ChatColor.GREEN + "Принять");
                        accept.setItemMeta(acceptmeta);

                        ItemStack paper1 = new ItemStack(Material.PAPER);
                        ItemStack paper10 = new ItemStack(Material.PAPER);
                        ItemStack paper100 = new ItemStack(Material.PAPER);

                        ItemMeta paper1meta = paper1.getItemMeta();
                        ItemMeta paper2meta = paper1.getItemMeta();
                        ItemMeta paper3meta = paper1.getItemMeta();

                        paper1meta.setDisplayName(ChatColor.BLUE + "1" + ChatColor.WHITE + "$");
                        paper1meta.setLore(Collections.singletonList(ChatColor.WHITE + "Обычная 1 долларовая купюра"));

                        paper2meta.setDisplayName(ChatColor.BLUE + "10" + ChatColor.WHITE + "$");
                        paper2meta.setLore(Collections.singletonList(ChatColor.WHITE + "Обычная 10 долларовая купюра"));

                        paper3meta.setDisplayName(ChatColor.BLUE + "100" + ChatColor.WHITE + "$");
                        paper3meta.setLore(Collections.singletonList(ChatColor.WHITE + "Обычная 100 долларовая купюра"));


                        paper1.setItemMeta(paper1meta);
                        paper10.setItemMeta(paper2meta);
                        paper100.setItemMeta(paper3meta);


                        Inventory withdraw = Bukkit.createInventory(player, 27, "Введите сумму");

                        withdraw.setItem(9, paper1);

                        withdraw.setItem(13, paper10);

                        withdraw.setItem(17, paper100);


                        player.openInventory(withdraw);

                    }


                }

            }



if(e.getCurrentItem().getItemMeta() != null) {
    if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Принять")) {


        NoteManager.massDeposit(player);

        e.setCancelled(true);

    }
}





            if(e.getView().getTitle().equals("Введите сумму")){
                e.setCancelled(true);



                ItemStack paper1 = new ItemStack(Material.PAPER);
                ItemStack paper10 = new ItemStack(Material.PAPER);
                ItemStack paper100 = new ItemStack(Material.PAPER);

                ItemMeta paper1meta = paper1.getItemMeta();
                ItemMeta paper2meta = paper1.getItemMeta();
                ItemMeta paper3meta = paper1.getItemMeta();

                paper1meta.setDisplayName(ChatColor.BLUE + "1" + ChatColor.WHITE + "$");
                paper1meta.setLore(Collections.singletonList(ChatColor.WHITE + "Обычная 1 долларовая купюра"));

                paper2meta.setDisplayName(ChatColor.BLUE + "10" + ChatColor.WHITE + "$");
                paper2meta.setLore(Collections.singletonList(ChatColor.WHITE + "Обычная 10 долларовая купюра"));

                paper3meta.setDisplayName(ChatColor.BLUE + "100" + ChatColor.WHITE + "$");
                paper3meta.setLore(Collections.singletonList(ChatColor.WHITE + "Обычная 100 долларовая купюра"));


                paper1.setItemMeta(paper1meta);
                paper10.setItemMeta(paper2meta);
                paper100.setItemMeta(paper3meta);

                if (paper1.equals(stack)) {
                    NoteManager.withdraw(player, 1);
                } else if (paper10.equals(stack)) {
                    NoteManager.withdraw(player, 10);
                } else if (paper100.equals(stack)) {
                    NoteManager.withdraw(player, 100);
                }

            }


        }

    }

    @EventHandler

    public void playercloseinventory(InventoryCloseEvent e){



        if(e.getView().getTitle().equals("Вставьте купюры в приемник")){


            NoteManager.massDeposit((Player) e.getPlayer());


        }


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
