package eu.hacraft.secrets.listeners;

import java.io.IOException;

import eu.hacraft.secrets.Secrets;
import eu.hacraft.secrets.methods.SecretCreator;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignCreateListener implements Listener {
    public static Secrets main;

    public SignCreateListener(Secrets main) {
        SignCreateListener.main = main;
    }

    @EventHandler
    public void onSigncreate(SignChangeEvent e) throws IndexOutOfBoundsException, IOException {
        Player p = e.getPlayer();
        if ((p.hasPermission("secret.admin")) && (e.getLine(0).equalsIgnoreCase("[secret]"))) {
            String Secretcreate = main.getConfig().getString("Messages.SecretCreated").replace("&", "§");
            Secretcreate = Secretcreate.replace("%SECRET%", e.getLine(1));

            SecretCreator.createSecret(e.getLine(1));
            p.sendMessage(Secretcreate);
            e.setLine(0, main.Linie1);
            e.setLine(2, main.Linie3);
        }
    }
}
