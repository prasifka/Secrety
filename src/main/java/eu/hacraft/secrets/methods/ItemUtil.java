package eu.hacraft.secrets.methods;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtil {
    public static ItemStack Stack(String Display, Material m, String lores, int Anzahl) {
        ItemStack istack52 = new ItemStack(m, Anzahl);
        ItemMeta istackMeta52 = istack52.getItemMeta();
        istackMeta52.setDisplayName(Display);
        List<String> lore = new ArrayList<String>();
        lore.add(lores);
        istackMeta52.setLore(lore);
        istack52.setItemMeta(istackMeta52);
        return istack52;
    }
}
