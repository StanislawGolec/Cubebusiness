package me.scpl.java.games.mcplugins.cubebusiness.itemstacks;

import me.scpl.java.games.mcplugins.cubebusiness.UsefulMethods;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class TitleDeed {

    private final int id;
    private final SpaceType type;
    private final String name;
    private final String colorSet;

    private final int purchasePrice;
    private final int mortgageValue;

    private final int[] rents;

    private final int housePrice;

    private boolean isMortgaged;
    private int upgradeLevel;

    public TitleDeed(int id, SpaceType type, String name, String colorSet, int purchasePrice, int[] rents,
            int housePrice) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.colorSet = colorSet;
        this.purchasePrice = purchasePrice;
        this.mortgageValue = purchasePrice / 2;
        this.rents = rents;
        this.housePrice = housePrice;

        this.isMortgaged = false;
        this.upgradeLevel = 0;
    }

    public ItemStack getItemStack() {
        List<String> lore = new ArrayList<>();
        lore.add("&7Space: " + id);
        lore.add("&7Set: " + colorSet);
        lore.add("&7Price: &e$" + purchasePrice);

        if (type == SpaceType.RAILROAD) {
            lore.add("&7Rent: &e$" + rents[0]);
            lore.add("&7If 2 stations are owned: &e$" + rents[1]);
            lore.add("&7If 3 stations are owned: &e$" + rents[2]);
            lore.add("&7If 4 stations are owned: &e$" + rents[3]);
        } else if (type == SpaceType.UTILITY) {
            lore.add("&7Rent: Roll x &e" + rents[0]);
            lore.add("&7If both are owned: Roll x &e" + rents[1]);
        } else { // Normal Property
            lore.add("&7Rent: &e$" + rents[0]);
            lore.add("&7With full color set: &e$" + (rents[0] * 2));
            lore.add("&7With 1 House: &e$" + rents[1]);
            lore.add("&7With 2 Houses: &e$" + rents[2]);
            lore.add("&7With 3 Houses: &e$" + rents[3]);
            lore.add("&7With 4 Houses: &e$" + rents[4]);
            lore.add("&7With Hotel: &e$" + rents[5]);
            lore.add("&7House/Hotel Cost: &e$" + housePrice);
            lore.add("&aLeft-Click&7 - Buy Building");
            lore.add("&bRight-Click&7 - Sell Building");
        }

        lore.add(isMortgaged ? "&cMortgaged" : "&aNot Mortgaged");
        lore.add("&eShift&7+&aLeft-Click&7 - Unmortgage");
        lore.add("&eShift&7+&bRight-Click&7 - Mortgage");

        return UsefulMethods.createItemStack("PAPER", name, lore);
    }

    // Getters and Setters
    public void setMortgaged(boolean mortgaged) {
        this.isMortgaged = mortgaged;
    }

    public boolean isMortgaged() {
        return isMortgaged;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public int getMortgageValue() {
        return mortgageValue;
    }

    public int getUpgradeLevel() {
        return upgradeLevel;
    }

    public void setUpgradeLevel(int level) {
        this.upgradeLevel = level;
    }
}