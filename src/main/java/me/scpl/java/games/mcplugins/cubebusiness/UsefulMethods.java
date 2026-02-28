package me.scpl.java.games.mcplugins.cubebusiness;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("deprecation")
public class UsefulMethods {

    /**
     * Translates '&' color codes to Minecraft color codes.
     */
    public static String colourMsg(String s) {
        if (s == null)
            return null;
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
     * Translates '&' color codes for a List of strings.
     */
    public static List<String> colourMsg(List<String> list) {
        if (list == null)
            return null;
        List<String> coloredList = new ArrayList<>();
        for (String s : list) {
            coloredList.add(colourMsg(s));
        }
        return coloredList;
    }

    /**
     * Translates '&' color codes for an array of strings.
     */
    public static String[] colourMsg(String[] table) {
        if (table == null)
            return null;
        String[] coloredTable = new String[table.length];
        for (int i = 0; i < table.length; i++) {
            coloredTable[i] = colourMsg(table[i]);
        }
        return coloredTable;
    }

    /**
     * Core method: Creates a custom ItemStack with a colored name and colored lore.
     */
    public static ItemStack createItemStack(String materialName, String name, List<String> lore) {
        // matchMaterial is safer than getMaterial for modern versions
        Material material = Material.matchMaterial(materialName.toUpperCase());
        if (material == null)
            material = Material.PAPER; // Failsafe

        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta != null) {
            if (name != null) {
                itemMeta.setDisplayName(colourMsg(name));
            }
            if (lore != null) {
                itemMeta.setLore(colourMsg(lore));
            }
            item.setItemMeta(itemMeta);
        }

        return item;
    }

    /**
     * Wrapper method: Allows creating an ItemStack using a String[] (or
     * comma-separated strings) for lore.
     */
    public static ItemStack createItemStack(String materialName, String name, String... lore) {
        return createItemStack(materialName, name, Arrays.asList(lore));
    }
}