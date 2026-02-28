package me.scpl.java.games.mcplugins.cubebusiness.events;

import me.scpl.java.games.mcplugins.cubebusiness.CubeBusiness;
import me.scpl.java.games.mcplugins.cubebusiness.UsefulMethods;
import me.scpl.java.games.mcplugins.cubebusiness.gameobjects.CBPlayer;
import me.scpl.java.games.mcplugins.cubebusiness.logic.GameManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Random;

public class ControllerInteractListener implements Listener {

    private final CubeBusiness plugin;
    private final Random random = new Random();

    public ControllerInteractListener(CubeBusiness plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onControllerUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        GameManager game = plugin.getGameManager();

        // Check if they are holding a stick and the game has started
        if (e.getItem() == null || e.getItem().getType() != Material.STICK)
            return;
        if (!game.isGameStarted())
            return;

        // Find the CBPlayer wrapper
        CBPlayer cbPlayer = null;
        for (CBPlayer pl : game.getPlayerManager().getCBPlayers()) {
            if (pl.getPlayerUUID().equals(player.getUniqueId())) {
                cbPlayer = pl;
                break;
            }
        }

        if (cbPlayer == null) {
            player.sendMessage(UsefulMethods.colourMsg("&cYou are not in the game!"));
            return;
        }

        // Prevent block breaking/placing with the controller
        e.setCancelled(true);

        // --- LEFT CLICK: ROLL DICE ---
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {

            if (cbPlayer.getPieceUUID() == null) {
                player.sendMessage(UsefulMethods.colourMsg("&cYou don't have a piece! Use &d/createpiece"));
                return;
            }

            // Enforce the Turn System!
            if (!game.isPlayerTurn(player)) {
                player.sendMessage(UsefulMethods.colourMsg("&cIt is not your turn!"));
                return;
            }

            // TODO: In the future, we need a 'hasRolled' variable in CBPlayer to prevent
            // rolling 50 times in one turn!

            int roll1 = random.nextInt(6) + 1; // 1 to 6
            int roll2 = random.nextInt(6) + 1;
            int total = roll1 + roll2;

            CubeBusiness.sendCBMessage(player,
                    "&7You rolled a &e" + roll1 + "&7 and a &e" + roll2 + "&7 - total &e" + total + ".",
                    "&7Player &b" + player.getName() + "&7 rolled a &e" + total + "&7.",
                    null);

            // Handle Doubles Logic later
            if (roll1 == roll2) {
                player.sendMessage(UsefulMethods.colourMsg("&7That's a double! You get another roll."));
            }

            // Move the piece using our new PieceManager!
            game.getPieceManager().moveBy(cbPlayer, total);
        }

        // --- RIGHT CLICK: PAY/MANAGE ---
        else if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            // Temporary way to manually end your turn so you can test the game!
            if (player.isSneaking()) {
                player.sendMessage(UsefulMethods.colourMsg("&aYou ended your turn."));
                game.nextTurn();
                return;
            }

            player.sendMessage(UsefulMethods.colourMsg(
                    "&e(WIP) The management panel will open here. (Shift + Right Click to End Turn for now)"));
        }
    }
}