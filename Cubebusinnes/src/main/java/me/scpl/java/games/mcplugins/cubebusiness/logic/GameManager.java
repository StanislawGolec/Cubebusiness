package me.scpl.java.games.mcplugins.cubebusiness.logic;

import me.scpl.java.games.mcplugins.cubebusiness.CubeBusiness;
import me.scpl.java.games.mcplugins.cubebusiness.gameobjects.CBPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static me.scpl.java.games.mcplugins.cubebusiness.CubeBusiness.sendCBMessage;
import static me.scpl.java.games.mcplugins.cubebusiness.UsefulMethods.colourMsg;

public class GameManager {

    private final CubeBusiness plugin;
    private final PlayerManager playerManager;
    private final BoardManager boardManager;
    private final PieceManager pieceManager;

    private boolean gameStarted;
    private int currentTurnIndex; // Tracks whose turn it currently is

    public GameManager(CubeBusiness plugin) {
        this.plugin = plugin;
        this.playerManager = new PlayerManager(plugin);
        this.boardManager = new BoardManager(plugin);
        this.pieceManager = new PieceManager(plugin);
        this.gameStarted = false;
        this.currentTurnIndex = 0;
    }

    // --- Player Management ---

    public void handlePlayerJoin(Player player) {
        ArrayList<CBPlayer> cbPlayers = playerManager.getCBPlayers();
        boolean alreadyJoined = cbPlayers.stream()
                .anyMatch(cbPlayer -> cbPlayer.getPlayerUUID().equals(player.getUniqueId()));

        if (gameStarted) {
            player.sendMessage(colourMsg(
                    "&cYou cannot join because the game has already started! Please wait until it finishes."));
        } else if (alreadyJoined) {
            player.sendMessage(colourMsg(
                    "&cYou have already joined the game!\n&7Current number of players: &e" + cbPlayers.size()));
        } else {
            cbPlayers.add(new CBPlayer(player.getUniqueId()));
            sendCBMessage(player,
                    "&7You have joined the game!",
                    "&b" + player.getName() + " has just joined the game!",
                    "&7Current number of players: &e" + cbPlayers.size());
        }
    }

    public void handlePlayerLeave(Player player) {
        ArrayList<CBPlayer> cbPlayers = playerManager.getCBPlayers();
        boolean alreadyJoined = cbPlayers.stream()
                .anyMatch(cbPlayer -> cbPlayer.getPlayerUUID().equals(player.getUniqueId()));

        if (gameStarted) {
            player.sendMessage(colourMsg(
                    "&cYou cannot leave because the game has already started! Please stay and keep playing!"));
            // Optional: Handle what happens if they forcefully disconnect (e.g.,
            // bankruptcy)
        } else if (!alreadyJoined) {
            player.sendMessage(colourMsg(
                    "&cYou cannot leave because you have not joined the game before!\n&7Current number of players: &e"
                            + cbPlayers.size()));
        } else {
            cbPlayers.removeIf(cbPlayer -> cbPlayer.getPlayerUUID().equals(player.getUniqueId()));
            sendCBMessage(player,
                    "&7You have left the game!",
                    "&b" + player.getName() + " has just left the game!",
                    "&7Current number of players: &e" + cbPlayers.size());
        }
    }

    // --- Turn Logic ---

    /**
     * Passes the turn to the next valid player and announces it in chat.
     */
    public void nextTurn() {
        ArrayList<CBPlayer> players = playerManager.getCBPlayers();

        if (players.isEmpty() || !gameStarted)
            return;

        // 1. Check Win Condition (If only 1 player is left who isn't bankrupt)
        long activePlayers = players.stream().filter(p -> !p.isBankrupt()).count();
        if (activePlayers <= 1) {
            setGameStarted(false);
            CBPlayer winner = players.stream().filter(p -> !p.isBankrupt()).findFirst().orElse(null);

            if (winner != null) {
                Player p = Bukkit.getPlayer(winner.getPlayerUUID());
                String winnerName = (p != null) ? p.getName() : "Unknown";
                sendCBMessage(null, null, null, "&aGame Over! &b" + winnerName + " &ahas won the game!");
            }
            return;
        }

        // 2. Loop until we find the next player who IS NOT bankrupt
        do {
            currentTurnIndex++;
            // Wrap around back to the first player if we reach the end of the list
            if (currentTurnIndex >= players.size()) {
                currentTurnIndex = 0;
            }
        } while (players.get(currentTurnIndex).isBankrupt());

        // 3. Announce the new turn
        CBPlayer currentPlayer = players.get(currentTurnIndex);
        Player bukkitPlayer = Bukkit.getPlayer(currentPlayer.getPlayerUUID());

        if (bukkitPlayer != null) {
            sendCBMessage(
                    bukkitPlayer,
                    "&eIt's your turn! Left-click with your panel to roll the dice.",
                    "&7It is now &b" + bukkitPlayer.getName() + "'s &7turn.",
                    null);
        }
    }

    /**
     * Helper method to get the player whose turn it currently is.
     */
    public CBPlayer getCurrentPlayer() {
        if (playerManager.getCBPlayers().isEmpty())
            return null;
        return playerManager.getCBPlayers().get(currentTurnIndex);
    }

    /**
     * Quick check to see if a specific Bukkit player is the active player.
     */
    public boolean isPlayerTurn(Player player) {
        CBPlayer current = getCurrentPlayer();
        return current != null && current.getPlayerUUID().equals(player.getUniqueId());
    }

    // --- Getters & Setters ---

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public BoardManager getBoardManager() {
        return boardManager;
    }

    public PieceManager getPieceManager() {
        return pieceManager;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
        if (gameStarted) {
            this.currentTurnIndex = 0; // Always reset to player 1 when a new game starts
        }
    }
}