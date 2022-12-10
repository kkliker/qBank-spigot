package ru.qship.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.qship.BankNotes;

import java.io.File;

public enum Lang {
    COMMAND_NO_PERMISSION("&cYou do not have permission for that command"),
    PLAYER_ONLY_COMMAND("This command is only available to players"),
    UNKNOWN_ERROR("&cAn unknown error has happened"),
    ADMIN_RELOAD("&aSuccessfully Reloaded"),
    ADMIN_PLAYER_NOTFOUND("&cPlayer &l{player} &cnot found"),
    INVALID_ARGUMENTS("&cInvalid arguments"),
    INVALID_AMOUNT("&cInvalid amount"),
    INSUFFICIENT_FUNDS("&fНедостаточно средств"),
    DEPOSIT_SUCCESS("&fВы успешно положили на счет &9{value}&f$"),
    DEPOSIT_INVALID_NOTE("&cInvalid note item"),
    WITHDRAW_SUCCESS("&fВы успешно сняли со счета &9{value}&f$");

    private String def;
    private static FileConfiguration lang;
    private static final File LANG_FILE = new File(BankNotes.notes.getDataFolder(), "lang.yml");

    private Lang(String def) {
        this.def = def;
    }

    public static void reload() {
        saveDefault();
        lang = YamlConfiguration.loadConfiguration(LANG_FILE);
    }

    private static void saveDefault() {
        if (!LANG_FILE.exists()) {
            BankNotes.notes.saveResource("lang.yml", false);
        }

    }

    public String getKey() {
        return this.name().toLowerCase().replace("_", "-");
    }

    public String get() {
        String value = lang.getString(this.getKey(), this.def);
        return ChatColor.translateAlternateColorCodes('&', value);
    }

    public String toString() {
        return this.get();
    }

    public ModifiableLang modifiable() {
        return new ModifiableLang();
    }

    public void msg(CommandSender sender) {
        this.msg(sender, this.get());
    }

    public void msg(CommandSender sender, String msg) {
        if (sender instanceof Player) {
            msg = PlaceholderAPI.setPlaceholders((Player)sender, msg);
        }

        if (sender instanceof ConsoleCommandSender) {
            msg = ChatColor.stripColor(msg);
        }

        sender.sendMessage(msg);
    }

    public class ModifiableLang {
        String string;

        private ModifiableLang() {
            this.string = Lang.this.get();
        }

        public ModifiableLang replace(String tag, String replace) {
            this.string = this.string.replace(tag, replace);
            return this;
        }

        public ModifiableLang replace(String tag, Object replace) {
            return this.replace(tag, replace.toString());
        }

        public void msg(Player player) {
            Lang.this.msg(player, this.string);
        }
    }
}
