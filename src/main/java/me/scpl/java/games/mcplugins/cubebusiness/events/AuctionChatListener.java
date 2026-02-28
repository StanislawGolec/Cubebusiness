package me.scpl.java.games.mcplugins.cubebusiness.events;

import me.scpl.java.games.mcplugins.cubebusiness.CubeBusiness;
import me.scpl.java.games.mcplugins.cubebusiness.UsefulMethods;
import me.scpl.java.games.mcplugins.cubebusiness.gameobjects.CBPlayer;
import me.scpl.java.games.mcplugins.cubebusiness.logic.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class AuctionChatListener implements Listener {

    private final CubeBusiness plugin;

    public AuctionChatListener(CubeBusiness plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onChatBid(AsyncPlayerChatEvent e) {
        GameManager game = plugin.getGameManager();

        // TODO: We will need to add getActualAction() to GameManager later!
        // if (!"auction".equals(game.getActualAction()) || !game.isGameStarted())
        // return;

        Player p = e.getPlayer();
        UUID playerUUID = p.getUniqueId();

        try {
            int bidValue = Integer.parseInt(e.getMessage());

            // Find the player in the game
            CBPlayer cbPlayer = null;
            for (CBPlayer pl : game.getPlayerManager().getCBPlayers()) {
                if (pl.getPlayerUUID().equals(playerUUID)) {
                    cbPlayer = pl;
                    break;
                }
            }

            if (cbPlayer == null)
                return; // Player typing isn't in the game

            e.setCancelled(true); // Hide the bid from normal chat

            // TODO: We will reinstate auction logic (checking balance, outbidding) here
            // once the Auction system is rebuilt in Phase 3.
            p.sendMessage(UsefulMethods.colourMsg("&aZalicytowałeś: &e" + bidValue + "$"));

        } catch (NumberFormatException ignored) {
            // Not a number, ignore it
        }
    }
}