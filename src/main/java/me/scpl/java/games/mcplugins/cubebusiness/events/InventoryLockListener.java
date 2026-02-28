package me.scpl.java.games.mcplugins.cubebusiness.events;

import me.scpl.java.games.mcplugins.cubebusiness.CubeBusiness;
import me.scpl.java.games.mcplugins.cubebusiness.guis.GUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryLockListener implements Listener {

    public InventoryLockListener(CubeBusiness plugin) {
        // Constructor just in case you need access to the plugin later
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        // If the inventory being clicked is one of our custom GUIs
        if (e.getInventory().getHolder() instanceof GUI) {

            // Cancel the event so the player can't steal the item
            e.setCancelled(true);

            // Optional: You can add logic here if you want them to be able to click
            // a specific deed to see more info or buy it later!
        }
    }
}