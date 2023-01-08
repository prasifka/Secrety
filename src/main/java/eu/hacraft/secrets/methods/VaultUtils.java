package eu.hacraft.secrets.methods;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import eu.hacraft.secrets.Secrets;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class VaultUtils {

    private static Economy econ = null;

    public static void setupEconomy(Plugin plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        econ = rsp.getProvider();
    }

    public static void setBalance(Player player, double balance) {
        EconomyResponse r = econ.depositPlayer(player, balance);
        if (r.transactionSuccess()) {
            player.sendMessage(String.format(Secrets.getPlugin().getConfig().getString("Messages.balanceAddSuccess").replace("&", "§"), econ.format(r.amount)));
        } else {
            player.sendMessage(String.format(Secrets.getPlugin().getConfig().getString("Messages.balanceAddError").replace("&", "§"), econ.format(r.amount)));
        }
    }

}