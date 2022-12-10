package ru.qship;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ru.qship.commands.BankNotesCmd;
import ru.qship.commands.DepositCmd;
import ru.qship.commands.GuiCMD;
import ru.qship.commands.WithdrawCmd;
import ru.qship.event.Interact;
import ru.qship.util.Lang;
import ru.qship.util.Permission;

import java.util.logging.Logger;

public class BankNotes extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    public static BankNotes notes;
    public Economy economy;

    public void onEnable() {

        if(!setupEconomy()){
            return;
        }

        notes = this;
        this.saveDefaultConfig();
        Lang.reload();
        this.getServer().getServicesManager().getRegistration(Permission.class);
            this.getServer().getPluginManager().registerEvents(new Interact(),this);


            this.getCommand("withdraw").setExecutor(new WithdrawCmd());
            this.getCommand("deposit").setExecutor(new DepositCmd());
            this.getCommand("banknotes").setExecutor(new BankNotesCmd());
            this.getCommand("3414fsafsg54ewdsf").setExecutor(new GuiCMD());
        economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();

        }

    public Economy getEconomy() {
        return economy;
    }



    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }



    }




