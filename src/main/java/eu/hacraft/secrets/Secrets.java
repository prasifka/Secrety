package eu.hacraft.secrets;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import eu.hacraft.secrets.listeners.InventoryClickListener;
import eu.hacraft.secrets.listeners.SignClickListener;
import eu.hacraft.secrets.listeners.SignCreateListener;
import eu.hacraft.secrets.methods.ItemUtil;
import eu.hacraft.secrets.methods.VaultUtils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Secrets extends JavaPlugin implements Listener {

    public static Logger LOGGER;

    private static Plugin plugin;

    public void onEnable() {
        LOGGER = getLogger();
        plugin = this;
        getLogger().info("------------------------- Secrets " + getDescription().getVersion() + " -------------------------");
        getLogger().info("Secrety " + getDescription().getVersion() + " se nacita [" + Bukkit.getVersion() + "]");
        getLogger().info("Updated 8. 1. 2023");
        if (hasVault()) {
            getLogger().info("Vault nalezen!");
            VaultUtils.setupEconomy(this);
        } else {
            getLogger().info("Vault nenalezen");
        }
        getServer().getPluginManager().registerEvents(new SignClickListener(this), this);
        getServer().getPluginManager().registerEvents(new SignCreateListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getLogger().info("Verze " + getDescription().getVersion() + " nactena!");
        getLogger().info("-----------------------------------------------------------------");
        loadConfig();
        saveConfig();

    }

    public void onDisable() {
        getLogger().info("[PrachSECRETS] Plugin vypnut");
    }

    public void loadConfig() {
        FileConfiguration cfg = getConfig();

        cfg.options().copyDefaults(true);
    }

    public String line1 = getConfig().getString("Sign.line1").replace("&", "§");
    public String line3 = getConfig().getString("Sign.line3").replace("&", "§");
    public String line4 = getConfig().getString("Sign.line4").replace("&", "§");

    public boolean onCommand(CommandSender sender, Command cmd, String cmdlable, String[] args) {
        if (args.length == 0) {
            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("secrets")) {
                File Cmd = new File("plugins/Secrets", "Secrets.yml");
                FileConfiguration cmdfile = YamlConfiguration.loadConfiguration(Cmd);
                File Data = new File("plugins/Secrets", "Users.yml");
                FileConfiguration userfile = YamlConfiguration.loadConfiguration(Data);
                String NothingFoundLore = getConfig().getString("Items.ItemNoSecretFound.Lore").replace("&", "§");
                String NothingFound = getConfig().getString("Items.ItemNoSecretFound.Name").replace("&", "§");
                Material NothingFoundItem = Material.getMaterial(getConfig().getString("Items.ItemNoSecretFound.Type"));
                String SecretColor = getConfig().getString("Items.ItemSecretFound.SecretDisplayColor").replace("&", "§");
                String Found = getConfig().getString("Items.ItemSecretFound.Lore").replace("&", "§");
                String total = getConfig().getString("Items.ItemTotalFoundSecrets.Name").replace("&", "§");
                String totalcolor = getConfig().getString("Items.ItemTotalFoundSecrets.TotalFoundSecretsLoreColor").replace("&", "§");
                List<String> userlist = userfile.getStringList(p.getUniqueId().toString());
                List<String> list = cmdfile.getStringList("Secrets");
                Inventory inv = Bukkit.createInventory(null, 54, "Secrets");
                if (!userfile.contains(p.getUniqueId().toString())) {
                    Inventory invs = Bukkit.createInventory(null, 27, "Secrets");
                    ItemStack none = ItemUtil.Stack(NothingFound, NothingFoundItem, NothingFoundLore, 1);
                    @SuppressWarnings("unused")
                    ItemMeta Itemmeta = none.getItemMeta();
                    invs.setItem(13, none);
                    p.openInventory(invs);
                    return false;
                }
                int i = -1;
                for (String all : userlist) {
                    i++;
                    Material SercetITEM = Material.getMaterial(getConfig().getString("Items.ItemSecretFound.Type"));
                    ItemStack Secret = ItemUtil.Stack(SecretColor + all, SercetITEM, Found, 1);
                    if (getConfig().getBoolean("Items.ItemSecretFound.Enchanted")) {
                        ItemMeta SecretMeta = Secret.getItemMeta();
                        Secret.setItemMeta(SecretMeta);
                    }
                    inv.setItem(i, Secret);
                    if (i == 53) {
                        break;
                    }
                }
                int max = list.size();
                int amoun = userlist.size();
                Material TotalFoundItem = Material.getMaterial(getConfig().getString("Items.ItemTotalFoundSecrets.Type"));
                ItemStack amount = ItemUtil.Stack(total, TotalFoundItem, totalcolor + amoun + " / " + max, 1);
                if (getConfig().getBoolean("Items.ItemSecretFound.Enchanted")) {
                    ItemMeta amountMeta = amount.getItemMeta();
                    amount.setItemMeta(amountMeta);
                }
                inv.setItem(53, amount);
                p.openInventory(inv);
            }
            return false;
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("secrets.reload")) {
                String Reload = getConfig().getString("Messages.Reload").replace("&", "§");
                reloadConfig();
                sender.sendMessage(Reload);
                return true;
            } else {
                String NoPermReload = getConfig().getString("Messages.NoPermReload").replace("&", "§");
                sender.sendMessage(NoPermReload);
            }
        } else if (args[0].equalsIgnoreCase("info")) {
            if (sender.hasPermission("secrets.info")) {
                sender.sendMessage("§a----------§9PrachSecrety§a----------\n" + "§9Prikazy:\n" + "§a/secrets §c- §aGUI se Secrety\n" + "§a/secrets reload §c- §aReload\n" + "§a/secrets info §c- §aTato stranka\n" + "§aVerze: §c" + getDescription().getVersion() + " §aAutor: §c"
                        + getDescription().getAuthors() + "\n" + "§a----------§9PrachSecrety§a----------");
                return true;
            } else {
                sender.sendMessage("§a----------§9PrachSecrety§a----------\n" + "§9Prikazy:\n" + "§a/secrets §c- §aGUI se Secrety\n" + "§a/secrets info §c- §aTato stranka\n" + "§aVerze: §c" + getDescription().getVersion() + " §aAutor: §c" + getDescription().getAuthors() + "\n"
                        + "§a----------§9PrachSecrety§a----------");

            }
        } else {
            String SubCommandnotFound = getConfig().getString("Messages.SubCommandnotFound").replace("&", "§");
            sender.sendMessage(SubCommandnotFound);
            return true;
        }
        return false;
    }

    public static boolean hasVault() {
        try {
            Class.forName("net.milkbowl.vault.Vault");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
