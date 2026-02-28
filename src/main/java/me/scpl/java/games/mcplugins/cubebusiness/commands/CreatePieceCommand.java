package me.scpl.java.games.mcplugins.cubebusiness.commands;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import me.scpl.java.games.mcplugins.cubebusiness.gameobjects.CBPlayer;
import me.scpl.java.games.mcplugins.cubebusiness.logic.GameManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import me.scpl.java.games.mcplugins.cubebusiness.CubeBusiness;

import static me.scpl.java.games.mcplugins.cubebusiness.UsefulMethods.colourMsg;

public class CreatePieceCommand implements CommandExecutor {

    private CubeBusiness plugin;
    private GameManager game;

    private static Set<String> ALL_ENTITY_TYPES = Set.of(
            "ALLAY", "AREA_EFFECT_CLOUD", "ARMOR_STAND", "ARROW", "AXOLOTL", "BAT", "BEE",
            "BLAZE", "BOAT", "CAT", "CAVE_SPIDER", "CHEST_BOAT", "CHICKEN", "COD", "COW",
            "CREEPER", "DOLPHIN", "DONKEY", "DRAGON_FIREBALL", "DROPPED_ITEM", "DROWNED",
            "EGG", "ELDER_GUARDIAN", "ENDER_CRYSTAL", "ENDER_DRAGON", "ENDER_PEARL",
            "ENDER_SIGNAL", "ENDERMAN", "ENDERMITE", "EVOKER", "EVOKER_FANGS", "EXPERIENCE_ORB",
            "FALLING_BLOCK", "FIREBALL", "FIREWORK", "FISHING_HOOK", "FOX", "FROG", "GHAST",
            "GIANT", "GLOW_ITEM_FRAME", "GLOW_SQUID", "GOAT", "GUARDIAN", "HOGLIN", "HORSE",
            "HUSK", "ILLUSIONER", "IRON_GOLEM", "ITEM_FRAME", "LEASH_HITCH", "LIGHTNING",
            "LLAMA", "LLAMA_SPIT", "MAGMA_CUBE", "MARKER", "MINECART", "MINECART_CHEST",
            "MINECART_COMMAND", "MINECART_FURNACE", "MINECART_HOPPER", "MINECART_MOB_SPAWNER",
            "MINECART_TNT", "MULE", "MUSHROOM_COW", "OCELOT", "PAINTING", "PANDA", "PARROT",
            "PHANTOM", "PIG", "PIGLIN", "PIGLIN_BRUTE", "PILLAGER", "PLAYER", "POLAR_BEAR",
            "PRIMED_TNT", "PUFFERFISH", "RABBIT", "RAVAGER", "SALMON", "SHEEP", "SHULKER",
            "SHULKER_BULLET", "SILVERFISH", "SKELETON", "SKELETON_HORSE", "SLIME",
            "SMALL_FIREBALL", "SNOWBALL", "SNOWMAN", "SPECTRAL_ARROW", "SPIDER",
            "SPLASH_POTION", "SQUID", "STRAY", "STRIDER", "TADPOLE", "THROWN_EXP_BOTTLE",
            "TRADER_LLAMA", "TRIDENT", "TROPICAL_FISH", "TURTLE", "UNKNOWN", "VEX", "VILLAGER",
            "VINDICATOR", "WANDERING_TRADER", "WARDEN", "WITCH", "WITHER", "WITHER_SKELETON",
            "WITHER_SKULL", "WOLF", "ZOGLIN", "ZOMBIE", "ZOMBIE_HORSE", "ZOMBIE_VILLAGER",
            "ZOMBIFIED_PIGLIN");

    private static Set<String> IGNORED_ENTITY_TYPES = Set.of(
            "ARROW", // flickers from one position to another
            "BOAT", // teleportation doesn't work
            "CHEST_BOAT", // teleportation doesn't work
            "DRAGON_FIREBALL",
            "DROPPED_ITEM", // can be picked up, is not perfectly centered
            "ENDER_CRYSTAL",
            "ENDER_DRAGON", // too big, faces the wrong direction, other issues
            "ENDER_SIGNAL",
            "EVOKER_FANGS", // disappears after animation
            "EXPERIENCE_ORB", // can be collected, flies towards the player
            "FALLING_BLOCK", // disappears immediately
            "FIREBALL",
            "FIREWORK",
            "FISHING_HOOK", // plugin error
            "GHAST", // too big
            "GIANT", // too big
            "GLOW_ITEM_FRAME", // disappears immediately
            "ITEM_FRAME", // disappears immediately
            "LEASH_HITCH", // disappears on its own
            "MARKER", // invisible
            "MINECART", // teleportation doesn't work
            "MINECART_CHEST",
            "MINECART_COMMAND",
            "MINECART_FURNACE",
            "MINECART_HOPPER",
            "MINECART_MOB_SPAWNER",
            "MINECART_TNT", // explosion
            "PAINTING", // disappears immediately
            "PLAYER",
            "PRIMED_TNT", // terrain explodes, piece flies upward
            "SNOWMAN", // leaves snow behind
            "SMALL_FIREBALL",
            "SPECTRAL_ARROW", // flickers from one position to another
            "TRIDENT", // flickers from one position to another
            "UNKNOWN",
            "WITHER_SKULL");

    public CreatePieceCommand(CubeBusiness plugin) {
        this.plugin = plugin;
        game = plugin.getGameManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Only players can execute this command
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        // Check command arguments
        if (args == null || (args.length != 1)) {
            player.sendMessage(colourMsg(colourMsg(
                    "&7Correct command usage: /&bcreatepiece &e(entity_type e.g. cow, sheep, llama, zombie...)&7.")));
            return true;
        }

        String entityType = args[0].toUpperCase();
        Set<String> entityTypes = getAvailableEntityTypes();

        if (!entityTypes.contains(entityType)) {
            player.sendMessage(colourMsg("&cInvalid entity type!"));
            return true;
        }

        ArrayList<CBPlayer> cbPlayers = game.getPlayerManager().getCBPlayers();

        if (cbPlayers.isEmpty()) {
            player.sendMessage(colourMsg(
                    "&cYou cannot create a piece because you haven't joined the game yet! &7Use &d/&ecb &bjoin"));
            return true;
        }

        // Find the player in the game manager
        CBPlayer foundCBPlayer = null;
        for (CBPlayer cbPlayer : cbPlayers) {
            if (cbPlayer.getPlayerUUID().equals(player.getUniqueId())) {
                foundCBPlayer = cbPlayer;
                break; // Stop after handling the sender player
            }
        }
        if (foundCBPlayer == null) {
            player.sendMessage(colourMsg(
                    "&cYou cannot create a piece because you haven't joined the game yet! &7Use &d/&ecb &bjoin"));
            return true;
        }

        // Remove old piece if exists
        if (foundCBPlayer.getPieceUUID() != null) {
            Entity oldPiece = game.getBoardManager().getWorld().getEntity(foundCBPlayer.getPieceUUID());
            if (oldPiece != null)
                oldPiece.remove();
        }

        // Determine a spawn location and rotation
        UUID newPieceUUID = game.getBoardManager().spawnPiece(foundCBPlayer.getSpace(), entityType);
        // Save piece UUID to player
        foundCBPlayer.setPieceUUID(newPieceUUID);

        return true;
    }

    public static Set<String> getAvailableEntityTypes() {
        return ALL_ENTITY_TYPES.stream()
                .filter(entity -> !IGNORED_ENTITY_TYPES.contains(entity))
                .collect(Collectors.toSet());
    }

}
