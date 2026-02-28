package me.scpl.java.games.mcplugins.cubebusiness.logic;

import me.scpl.java.games.mcplugins.cubebusiness.CubeBusiness;
import me.scpl.java.games.mcplugins.cubebusiness.UsefulMethods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BoardManager {

    private final CubeBusiness plugin;
    private final GameManager game;
    private World world;

    // The "Origin" point of your board (Bottom-Left corner)
    private double originX = 0.0;
    private double originY = 100.0;
    private double originZ = 0.0;

    private Location[] spaceLocations;

    public BoardManager(CubeBusiness plugin) {
        this.plugin = plugin;
        this.game = plugin.getGameManager();
        this.world = Bukkit.getWorld("world");

        calculateSpaceLocations();
    }

    /**
     * Dynamically calculates all 40 space locations using a loop and math.
     * No more hardcoded coordinates!
     */
    public void calculateSpaceLocations() {
        spaceLocations = new Location[40];

        for (int i = 0; i < 40; i++) {
            double currentX = originX;
            double currentZ = originZ;
            float yaw = 0.0f;

            // Calculate position based on which side of the board we are on
            if (i < 10) {
                // Bottom edge (Spaces 1-10) moving +X
                currentX += 2 + (i * 4);
                currentZ += 2;
                yaw = 0.0f;
            } else if (i < 20) {
                // Right edge (Spaces 11-20) moving +Z
                currentX += 42;
                currentZ += 2 + ((i - 10) * 4);
                yaw = 90.0f;
            } else if (i < 30) {
                // Top edge (Spaces 21-30) moving -X
                currentX += 42 - ((i - 20) * 4);
                currentZ += 42;
                yaw = 180.0f;
            } else {
                // Left edge (Spaces 31-40) moving -Z
                currentX += 2;
                currentZ += 42 - ((i - 30) * 4);
                yaw = 270.0f;
            }

            // Save the exact center of the space
            spaceLocations[i] = new Location(world, currentX, originY + 1, currentZ, yaw, 0.0f);
        }
    }

    /**
     * Spawns a player's piece on a specific space.
     */
    public UUID spawnPiece(int spaceNumber, String entityName) {
        if (spaceNumber < 1 || spaceNumber > 40)
            spaceNumber = 1;

        Location spawnLocation = spaceLocations[spaceNumber - 1].clone();

        // Adjust facing direction for the piece
        if (spaceNumber < 11)
            spawnLocation.setYaw(spawnLocation.getYaw() + 270.0f);
        else if (spaceNumber < 31 && spaceNumber >= 21)
            spawnLocation.setYaw(spawnLocation.getYaw() + 90.0f);
        else if (spaceNumber < 41)
            spawnLocation.setYaw(spawnLocation.getYaw() + 180.0f);

        Entity entity = world.spawnEntity(spawnLocation, EntityType.valueOf(entityName.toUpperCase()));
        entity.setGravity(false);

        if (entity instanceof LivingEntity living) {
            if (entity instanceof Mob mob) {
                mob.getEquipment().setHelmet(new ItemStack(Material.ACACIA_BUTTON));
            }
            living.setAI(false);
        }

        if (entity instanceof Explosive explosive) {
            explosive.setYield(0f);
        }

        return entity.getUniqueId();
    }

    /**
     * Spawns the Armor Stand holograms using names from the BoardRegistry.
     */
    @SuppressWarnings("deprecation")
    public void spawnArmorStands() {
        // First, clear any old holograms so they don't stack on top of each other!
        for (Entity entity : world.getEntitiesByClass(ArmorStand.class)) {
            if (entity.getScoreboardTags().contains("cb_hologram")) {
                entity.remove();
            }
        }

        for (int i = 0; i < 40; i++) {
            Location loc = spaceLocations[i].clone();
            loc.setY(loc.getY() - 1); // Drop it down slightly

            // Shift the hologram slightly towards the outside of the board
            if (i == 0) {
                loc.add(-2, 0, -2);
            } else if (i < 10) {
                loc.add(0, 0, -2);
            } else if (i == 10) {
                loc.add(2, 0, -2);
            } else if (i < 20) {
                loc.add(2, 0, 0);
            } else if (i == 20) {
                loc.add(2, 0, 2);
            } else if (i < 30) {
                loc.add(0, 0, 2);
            } else if (i == 30) {
                loc.add(-2, 0, 2);
            } else {
                loc.add(-2, 0, 0);
            }

            ArmorStand stand = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);

            // Fetch name from our new Registry!
            String name = BoardRegistry.getSpaceName(i + 1);

            if (name != null) {
                stand.setCustomName(UsefulMethods.colourMsg(name));
                stand.setCustomNameVisible(true);
            }

            stand.setInvulnerable(true);
            stand.setGravity(false);
            stand.setInvisible(true);
            stand.addScoreboardTag("cb_hologram"); // Tag it so we can delete it later
        }
    }

    // --- Getters & Setters ---

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
        calculateSpaceLocations(); // Recalculate if the world changes
    }

    public Location[] getSpaceLocations() {
        return spaceLocations;
    }

    public void setOrigin(double x, double y, double z) {
        this.originX = x;
        this.originY = y;
        this.originZ = z;
        calculateSpaceLocations(); // Recalculate board instantly
    }
}