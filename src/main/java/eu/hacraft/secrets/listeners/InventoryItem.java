package eu.hacraft.secrets.listeners;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import eu.hacraft.secrets.Secrets;

public class InventoryItem implements Listener {

    private final Secrets Plugin;

    public InventoryItem(Secrets p) {
        Plugin = p;
    }

    @EventHandler
    public void ongetItembyJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        if (e.getPlayer().hasPermission("secrets.item.get")) {

            Material ItemType = Material.getMaterial(Plugin.getConfig().getString("Items.SecretItem.Type"));
            String ItemName = Plugin.getConfig().getString("Items.SecretItem.Name").replace("&", "§");
            ItemStack Item = new ItemStack(ItemType, 1);
            ItemMeta Itemmeta = Item.getItemMeta();
            Itemmeta.setDisplayName(ItemName);

            ArrayList<String> lore = new ArrayList<String>();
            lore.add(Plugin.getConfig().getString("Items.SecretItem.Lore").replace("&", "§"));
            Itemmeta.setLore(lore);
            Item.setItemMeta(Itemmeta);
            p.getInventory().setItem(Plugin.getConfig().getInt("Items.SecretItem.Slot"), Item);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (!e.getPlayer().hasPermission("secrets.item.drop")) {
            Material mat = Material.getMaterial(Plugin.getConfig().getString("Items.SecretItem.Type"));
            if (e.getItemDrop().getItemStack().getType().equals(mat) && e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(Plugin.getConfig().getString("Items.SecretItem.Name").replace("&", "§"))) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void OnItemClick(PlayerInteractEvent e) {
        if (e.getPlayer().hasPermission("secrets.item.use")) {
            Material mat = Material.getMaterial(Plugin.getConfig().getString("Items.SecretItem.Type"));
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Player p = e.getPlayer();
                if (p.getInventory().getItemInMainHand().getType().equals(mat)) {
                    if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(Plugin.getConfig().getString("Items.SecretItem.Name").replace("&", "§"))) {
                        p.performCommand("secrets");
                    }
                }
            }
        }
    }
}