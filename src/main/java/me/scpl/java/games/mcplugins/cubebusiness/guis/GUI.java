package me.scpl.java.games.mcplugins.cubebusiness.guis;

import me.scpl.java.games.mcplugins.cubebusiness.UsefulMethods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class GUI implements InventoryHolder {

    protected Inventory inventory;

    @SuppressWarnings("deprecation")
    public GUI(int size, String title) {
        // Implementing InventoryHolder (passing 'this') allows us to easily recognize
        // this GUI in click events
        this.inventory = Bukkit.createInventory(this, size, UsefulMethods.colourMsg(title));
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }
}