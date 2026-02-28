package me.scpl.java.games.mcplugins.cubebusiness.commands;

import me.scpl.java.games.mcplugins.cubebusiness.CubeBusiness;
import me.scpl.java.games.mcplugins.cubebusiness.gameobjects.CBPlayer;
import me.scpl.java.games.mcplugins.cubebusiness.logic.GameManager;
import me.scpl.java.games.mcplugins.cubebusiness.itemstacks.Controller;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

import static me.scpl.java.games.mcplugins.cubebusiness.CubeBusiness.sendCBMessage;
import static me.scpl.java.games.mcplugins.cubebusiness.UsefulMethods.colourMsg;

public class CubeBusinessCommand implements CommandExecutor {

    private final CubeBusiness plugin;
    private final GameManager game;

    public CubeBusinessCommand(CubeBusiness plugin) {
        this.plugin = plugin;
        this.game = plugin.getGameManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (args == null || args.length == 0) {
            sendUsage(player);
            return true;
        }

        return executeOption(player, command, label, args);
    }

    private boolean executeOption(Player player, Command command, String label, String[] args) {
        String chosenOption = args[0].toLowerCase();

        return switch (chosenOption) {
            case "join" -> handleJoin(player);
            case "leave", "unjoin" -> handleLeave(player);
            case "start" -> handleStart(player, false);
            case "force-start" -> handleStart(player, true);
            case "stop" -> handleStop(player);
            case "as" -> handleAs(player, command, label, args);
            case "cardlist" -> handleCardList(player); // Changed from cardslist to cardlist
            default -> {
                sendUsage(player);
                yield true;
            }
        };
    }

    private void sendUsage(Player player) {
        player.sendMessage(colourMsg(
                "&7Correct command usage: /&bcubebusiness &e(join | leave | start | force-start | stop | cardlist)&7."));
    }

    private boolean handleStart(Player player, boolean force) {
        // 1. Permission check
        if (!player.isOp()) {
            player.sendMessage(colourMsg("&cYou don't have permission!"));
            return true;
        }

        // 2. State check
        if (game.isGameStarted()) {
            player.sendMessage(colourMsg("&cThe game has already started!"));
            return true;
        }

        // 3. Player count check
        ArrayList<CBPlayer> cbPlayers = game.getPlayerManager().getCBPlayers();

        // Failsafe: Don't start if literally zero people are in the game
        if (cbPlayers.isEmpty()) {
            player.sendMessage(colourMsg("&cNo players have joined the game yet!"));
            return true;
        }

        if (!force && cbPlayers.size() < 2) {
            player.sendMessage(
                    colourMsg("&cAt least 2 players are required to start! Use &b/cb force-start&c to test alone."));
            return true;
        }

        // 4. Initialize the Board
        game.getBoardManager().spawnArmorStands();
        game.setGameStarted(true);

        // 5. Setup the players
        for (CBPlayer cbPlayer : cbPlayers) {
            Player p = Bukkit.getPlayer(cbPlayer.getPlayerUUID());
            if (p != null) {
                // Give the player their panel/controller
                p.getInventory().addItem(Controller.getControllerItem());

                // Friendly reminder if they forgot to pick a game piece!
                if (cbPlayer.getPieceUUID() == null) {
                    p.sendMessage(
                            colourMsg("&eRemember to create your piece using the command &b/createpiece <mob>&e!"));
                }
            }
        }

        // 6. Announce the start
        Player firstPlayer = Bukkit.getPlayer(cbPlayers.get(0).getPlayerUUID());
        String firstPlayerName = (firstPlayer != null) ? firstPlayer.getName() : "Unknown";

        sendCBMessage(player,
                null,
                null,
                "&7The game has started! &b" + firstPlayerName + "&7 goes first.");

        return true;
    }

    private boolean handleStop(Player player) {
        if (!player.isOp())
            return true;

        if (game.isGameStarted()) {
            player.sendMessage(colourMsg("&7Stopping the game..."));
            game.setGameStarted(false);

            // Optional: You could call a method here to remove the holograms/pieces when
            // the game stops

        } else {
            player.sendMessage(colourMsg("&cYou cannot stop the game because it hasn't started yet!"));
        }

        return true;
    }

    private boolean handleJoin(Player player) {
        game.handlePlayerJoin(player);
        return true;
    }

    private boolean handleLeave(Player player) {
        game.handlePlayerLeave(player);
        return true;
    }

    private boolean handleAs(Player executor, Command command, String label, String[] args) {
        if (!executor.isOp())
            return true;
        if (args == null || args.length <= 2)
            return true;

        try {
            Player player = Bukkit.getPlayerExact(args[1]);
            args = Arrays.copyOfRange(args, 2, args.length);
            executeOption(player, command, label, args);
            return true;
        } catch (Exception ignored) {
            executor.sendMessage(colourMsg("&cPlayer not found!\n&7Current number of players: &e"
                    + game.getPlayerManager().getCBPlayers().size()));
            return true;
        }
    }

    private boolean handleCardList(Player player) { // Changed method name slightly to match the command
        if (!player.isOp())
            return true;

        // Ensure you import me.scpl.java.games.mcplugins.cubebusiness.guis.CardListGUI
        // at the top of the file!
        // CardListGUI.openFor(player);
        player.sendMessage(colourMsg("&e(WIP) The cards GUI is not fully connected yet."));
        return true;
    }
}