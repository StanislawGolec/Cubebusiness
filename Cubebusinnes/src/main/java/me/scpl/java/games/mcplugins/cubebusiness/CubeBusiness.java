package me.scpl.java.games.mcplugins.cubebusiness;

import me.scpl.java.games.mcplugins.cubebusiness.commands.ColourCodesCommand;
import me.scpl.java.games.mcplugins.cubebusiness.commands.CreatePieceCommand;
import me.scpl.java.games.mcplugins.cubebusiness.commands.CubeBusinessCommand;
import me.scpl.java.games.mcplugins.cubebusiness.events.AuctionChatListener;
import me.scpl.java.games.mcplugins.cubebusiness.events.ControllerInteractListener;
import me.scpl.java.games.mcplugins.cubebusiness.events.InventoryLockListener;
import me.scpl.java.games.mcplugins.cubebusiness.events.PieceProtectionListener;
import me.scpl.java.games.mcplugins.cubebusiness.logic.GameManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

import static me.scpl.java.games.mcplugins.cubebusiness.UsefulMethods.colourMsg;

public final class CubeBusiness extends JavaPlugin {

    private GameManager gameManager;

    @Override
    public void onEnable() {
        // Initialize Core Logic
        gameManager = new GameManager(this);

        // Register Commands
        if (getCommand("colourcodes") != null) {
            getCommand("colourcodes").setExecutor(new ColourCodesCommand(this));
        } else {
            getLogger().severe("Command 'colourcodes' not found in plugin.yml!");
        }

        if (getCommand("createpiece") != null) {
            this.getCommand("createpiece").setExecutor(new CreatePieceCommand(this));
        } else {
            getLogger().severe("Command 'createpiece' not found in plugin.yml!");
        }

        if (getCommand("cubebusiness") != null) {
            this.getCommand("cubebusiness").setExecutor(new CubeBusinessCommand(this));
        } else {
            getLogger().severe("Command 'cubebusiness' not found in plugin.yml!");
        }

        // Register Events
        getServer().getPluginManager().registerEvents(new PieceProtectionListener(this), this);
        getServer().getPluginManager().registerEvents(new AuctionChatListener(this), this);
        getServer().getPluginManager().registerEvents(new ControllerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryLockListener(this), this);

        getLogger().info("CubeBusiness plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("CubeBusiness plugin has been disabled!");
    }

    // --- Getters & Setters ---

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    // --- Utilities ---

    /**
     * Sends custom colored messages to specific groups of players.
     */
    @SuppressWarnings("deprecation")
    public static void sendCBMessage(Player player, String messageToPlayer, String messageToOtherPlayers,
            String messageToAllPlayers) {
        if (messageToPlayer != null && !messageToPlayer.isEmpty()) {
            player.sendMessage(colourMsg(messageToPlayer));
        }

        if (messageToOtherPlayers != null && !messageToOtherPlayers.isEmpty()) {
            UUID playerUUID = player.getUniqueId();
            Bukkit.getOnlinePlayers().forEach(p -> {
                if (!playerUUID.equals(p.getUniqueId())) {
                    p.sendMessage(colourMsg(messageToOtherPlayers));
                }
            });
        }

        if (messageToAllPlayers != null && !messageToAllPlayers.isEmpty()) {
            Bukkit.broadcastMessage(colourMsg(messageToAllPlayers));
        }
    }
}