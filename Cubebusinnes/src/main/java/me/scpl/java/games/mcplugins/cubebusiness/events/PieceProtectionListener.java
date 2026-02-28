package me.scpl.java.games.mcplugins.cubebusiness.events;

import me.scpl.java.games.mcplugins.cubebusiness.CubeBusiness;
import me.scpl.java.games.mcplugins.cubebusiness.gameobjects.CBPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;

public class PieceProtectionListener implements Listener {

    private final CubeBusiness plugin;

    public PieceProtectionListener(CubeBusiness plugin) {
        this.plugin = plugin;
    }

    private boolean isPiece(Entity entity) {
        for (CBPlayer cbPlayer : plugin.getGameManager().getPlayerManager().getCBPlayers()) {
            if (entity.getUniqueId().equals(cbPlayer.getPieceUUID()))
                return true;
        }
        return false;
    }

    @EventHandler
    public void onPieceDamage(EntityDamageEvent e) {
        if (isPiece(e.getEntity()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onPieceExplode(ExplosionPrimeEvent e) {
        if (isPiece(e.getEntity()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onPieceCollision(VehicleEntityCollisionEvent e) {
        if (isPiece(e.getEntity()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onPieceDeath(EntityDeathEvent e) {
        // Just in case a piece somehow dies (e.g. /kill), instantly respawn it!
        for (CBPlayer cbPlayer : plugin.getGameManager().getPlayerManager().getCBPlayers()) {
            if (e.getEntity().getUniqueId().equals(cbPlayer.getPieceUUID())) {
                plugin.getGameManager().getBoardManager().spawnPiece(cbPlayer.getSpace(), e.getEntityType().name());
                break;
            }
        }
    }
}