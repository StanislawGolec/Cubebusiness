package me.scpl.java.games.mcplugins.cubebusiness.logic;

import me.scpl.java.games.mcplugins.cubebusiness.CubeBusiness;
import me.scpl.java.games.mcplugins.cubebusiness.UsefulMethods;
import me.scpl.java.games.mcplugins.cubebusiness.gameobjects.CBPlayer;
import me.scpl.java.games.mcplugins.cubebusiness.itemstacks.TitleDeed;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PieceManager {

    private final CubeBusiness plugin;
    private final GameManager game;

    public PieceManager(CubeBusiness plugin) {
        this.plugin = plugin;
        this.game = plugin.getGameManager();
    }

    public void teleportToSpace(CBPlayer cbPlayer, int spaceNumber) {
        if (cbPlayer == null || cbPlayer.getPieceUUID() == null)
            return;

        Entity piece = Bukkit.getEntity(cbPlayer.getPieceUUID());
        if (piece == null)
            return;

        Location[] locations = game.getBoardManager().getSpaceLocations();
        Location targetLoc = locations[spaceNumber - 1].clone();

        if (spaceNumber < 11)
            targetLoc.setYaw(targetLoc.getYaw() + 270.0f);
        else if (spaceNumber < 21)
            targetLoc.setYaw(targetLoc.getYaw() + 0.0f);
        else if (spaceNumber < 31)
            targetLoc.setYaw(targetLoc.getYaw() + 90.0f);
        else
            targetLoc.setYaw(targetLoc.getYaw() + 180.0f);

        cbPlayer.setSpace(spaceNumber);
        piece.teleport(targetLoc);
    }

    public void moveBy(CBPlayer cbPlayer, int spacesToMove) {
        int currentSpace = cbPlayer.getSpace();
        int targetSpace = currentSpace + spacesToMove;

        // Handle passing START
        if (targetSpace > 40) {
            targetSpace -= 40;

            cbPlayer.addCash(200);
            Player player = Bukkit.getPlayer(cbPlayer.getPlayerUUID());
            if (player != null) {
                player.sendMessage(UsefulMethods.colourMsg("&aYou passed GO! Collect $200."));
            }
        }

        animateMovement(cbPlayer, targetSpace);
    }

    private void animateMovement(CBPlayer cbPlayer, int targetSpace) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (cbPlayer.getPieceUUID() == null || Bukkit.getEntity(cbPlayer.getPieceUUID()) == null) {
                    this.cancel();
                    return;
                }

                int currentSpace = cbPlayer.getSpace();

                if (currentSpace == targetSpace) {
                    handleLandingOnSpace(cbPlayer, targetSpace);
                    this.cancel();
                    return;
                }

                int nextSpace = (currentSpace < 40) ? currentSpace + 1 : 1;
                teleportToSpace(cbPlayer, nextSpace);

            }
        }.runTaskTimer(plugin, 0L, 10L);
    }

    private void handleLandingOnSpace(CBPlayer cbPlayer, int spaceNumber) {
        Player player = Bukkit.getPlayer(cbPlayer.getPlayerUUID());
        if (player == null)
            return;

        String spaceName = BoardRegistry.getSpaceName(spaceNumber);
        TitleDeed deed = BoardRegistry.getDeed(spaceNumber); // It will be null if it's a space you can't buy

        CubeBusiness.sendCBMessage(
                player,
                "&7You landed on " + spaceName + "&7.",
                "&7Player &b" + player.getName() + "&7 landed on " + spaceName + "&7.",
                null);

        if (deed != null) {
            // Property logic (buy / pay rent) goes here later
            player.sendMessage(
                    UsefulMethods.colourMsg("&7This space can be bought for &a$&e" + deed.getPurchasePrice() + "&7."));
        } else {
            // Special space logic (draw card, pay tax) goes here later
        }

        player.sendMessage(UsefulMethods.colourMsg("&7Use the panel to end your turn."));
    }
}