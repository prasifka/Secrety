package eu.hacraft.secrets.listeners;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import eu.hacraft.secrets.Secrets;
import eu.hacraft.secrets.methods.VaultUtils;

public class SignClickListener implements Listener {
    public static Secrets main;

    public SignClickListener(Secrets main) {
        SignClickListener.main = main;
    }

    @EventHandler
    public void onFound(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        File Cmd = new File("plugins/Secrets", "Secrets.yml");
        FileConfiguration cmdfile = YamlConfiguration.loadConfiguration(Cmd);
        File Data = new File("plugins/Secrets", "Users.yml");
        FileConfiguration userfile = YamlConfiguration.loadConfiguration(Data);
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) && ((e.getClickedBlock().getState() instanceof Sign s))) {
            String Linie1 = main.getConfig().getString("Sign.line1").replace("&", "§");
            if (s.getLine(0).equalsIgnoreCase(Linie1)) {
                String secretname = s.getLine(1);
                List<String> list = cmdfile.getStringList("Secrets");
                if (list.contains(secretname)) {
                    if (!Data.exists()) {
                        userfile.set("active", true);
                        try {
                            userfile.save(Data);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (!userfile.contains(p.getUniqueId().toString())) {
                        userfile.set(p.getUniqueId().toString(), "value");
                        List<String> userlist = userfile.getStringList(p.getUniqueId().toString());
                        String SecretFound = main.getConfig().getString("Messages.SecretFound").replace("&", "§");
                        SecretFound = SecretFound.replace("%SECRET%", secretname);
                        String SecretFoundheader = main.getConfig().getString("Messages.FoundTitleHeader").replace("&", "§");
                        double Time = main.getConfig().getDouble("Settings.Title-Time");
                        SecretFoundheader = SecretFoundheader.replace("%SECRET%", secretname);
                        String SecretFoundsub = main.getConfig().getString("Messages.FoundTitleFooter").replace("&", "§");
                        SecretFoundsub = SecretFoundsub.replace("%SECRET%", secretname);

                        e.getPlayer().sendTitle(SecretFoundheader, SecretFoundsub, 10, (int) Time, 10);

                        String achievment = main.getConfig().getString("Settings.CommandOnFound").replace("&", "§");
                        double money = main.getConfig().getDouble("Settings.Vault.Money");
                        achievment = achievment.replace("%PLAYER%", p.getName());

                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), achievment);
                        if (main.getConfig().getBoolean("Settings.Vault.Enable"))
                            if (Secrets.hasVault() && main.getConfig().getBoolean("Settings.Vault.Enable")) {
                                VaultUtils.setBalance(p, money);
                            }
                        p.sendMessage(SecretFound);
                        userlist.add(secretname);
                        userfile.set(p.getUniqueId().toString(), userlist);
                        try {
                            userfile.save(Data);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        List<String> userlist = userfile.getStringList(p.getUniqueId().toString());
                        if (userlist.contains(secretname)) {
                            String SecretFoundsub = main.getConfig().getString("Messages.AlreadyFound").replace("&", "§");
                            p.sendMessage(SecretFoundsub);
                            return;
                        }
                        String SecretFound = main.getConfig().getString("Messages.SecretFound").replace("&", "§");
                        SecretFound = SecretFound.replace("%SECRET%", secretname);

                        String SecretFoundheader = main.getConfig().getString("Messages.FoundTitleHeader").replace("&", "§");
                        SecretFoundheader = SecretFoundheader.replace("%SECRET%", secretname);

                        String SecretFoundsub = main.getConfig().getString("Messages.FoundTitleFooter").replace("&", "§");
                        SecretFoundsub = SecretFoundsub.replace("%SECRET%", secretname);
                        double Time = main.getConfig().getDouble("Settings.Title-Time");
                        e.getPlayer().sendTitle(SecretFoundheader, SecretFoundsub, 10, (int) Time, 10);


                        String achievment = main.getConfig().getString("Settings.CommandOnFound").replace("&", "§");
                        achievment = achievment.replace("%PLAYER%", p.getName());
                        double money = main.getConfig().getDouble("Settings.Vault.Money");

                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), achievment);
                        if (Secrets.hasVault() && main.getConfig().getBoolean("Settings.Vault.Enable")) {
                            VaultUtils.setBalance(p, money);
                        }
                        p.sendMessage(SecretFound);
                        userlist.add(secretname);
                        userfile.set(p.getUniqueId().toString(), userlist);
                        try {
                            userfile.save(Data);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                    }
                }
            }
        }
    }
}