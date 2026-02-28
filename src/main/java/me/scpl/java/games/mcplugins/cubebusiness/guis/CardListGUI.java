package me.scpl.java.games.mcplugins.cubebusiness.guis;

import me.scpl.java.games.mcplugins.cubebusiness.itemstacks.TitleDeed;
import me.scpl.java.games.mcplugins.cubebusiness.logic.BoardRegistry;
import org.bukkit.entity.Player;

public class CardListGUI extends GUI {

    public CardListGUI() {
        super(45, "&8Title Deeds List");

        // Map the Space IDs to the specific Inventory Slots based on your original
        // layout
        int[] streetIds = { 2, 4, 7, 9, 10, 12, 14, 15, 17, 19, 20, 22, 24, 25, 27, 28, 30, 32, 33, 35, 38, 40 };
        int[] railroadIds = { 6, 16, 26, 36 };
        int[] utilityIds = { 13, 29 };

        // Add Streets (Slots 0 to 21)
        for (int i = 0; i < streetIds.length; i++) {
            addDeedToSlot(streetIds[i], i);
        }

        // Add Railroads (Slots 36 to 39)
        for (int i = 0; i < railroadIds.length; i++) {
            addDeedToSlot(railroadIds[i], 36 + i);
        }

        // Add Utilities (Slots 43 to 44)
        for (int i = 0; i < utilityIds.length; i++) {
            addDeedToSlot(utilityIds[i], 43 + i);
        }
    }

    /**
     * Helper method to grab the deed from the registry and put it in the inventory
     */
    private void addDeedToSlot(int spaceId, int slot) {
        TitleDeed deed = BoardRegistry.getDeed(spaceId);
        if (deed != null) {
            setItem(slot, deed.getItemStack());
        }
    }

    /**
     * Static helper to quickly open the GUI for a player
     */
    public static void openFor(Player player) {
        CardListGUI gui = new CardListGUI();
        gui.open(player);
    }
}