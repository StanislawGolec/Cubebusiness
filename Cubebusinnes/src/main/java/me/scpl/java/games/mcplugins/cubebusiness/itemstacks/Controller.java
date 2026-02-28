package me.scpl.java.games.mcplugins.cubebusiness.itemstacks;

import me.scpl.java.games.mcplugins.cubebusiness.UsefulMethods;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class Controller {

    public static ItemStack getControllerItem() {
        return UsefulMethods.createItemStack(
                Material.STICK.name(),
                "&d&lPanel",
                Arrays.asList(
                        "&aLeft-Click&7 - Roll / End Turn",
                        "&bRight-Click&7 - Pay",
                        "&eShift&7+&aLeft-Click&7 - Buy",
                        "&eShift&7+&bRight-Click&7 - Auction"));
    }
}