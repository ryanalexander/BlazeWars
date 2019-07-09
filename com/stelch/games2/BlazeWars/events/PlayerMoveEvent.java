package com.stelch.games2.BlazeWars.events;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.core.Utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

public class PlayerMoveEvent implements Listener {

    @EventHandler
    public void onMove(org.bukkit.event.player.PlayerMoveEvent e) {
        if (Main.game.invis_players.containsKey(e.getPlayer())) {
            Main.game.invis_players.get(e.getPlayer()).teleport(e.getPlayer());
            Location loc = e.getPlayer().getLocation();
            e.getTo().getWorld().spawnParticle(Particle.DRAGON_BREATH, new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), Float.valueOf(loc.getYaw() + ""), Float.valueOf(loc.getPitch() + "")), 1, 0, 0, 0, 0);
        }

        for (Entity entity : Main.game.getGameEntities()) {
            if (entity.getType().equals(EntityType.BLAZE)) {
                if (e.getTo().distanceSquared(entity.getLocation()) < 500&&Main.game.getTeamManager().getBlazeCooldown(entity)) {
                    if(Main.game.spectators.containsKey(e.getPlayer())){continue;}
                    if(Main.game.getTeamManager().getBlaze(Main.game.getTeamManager().getTeam((Player)e.getPlayer()))==entity)
                        return;
                    Blaze blaze = (Blaze) entity;
                    Fireball fb = blaze.launchProjectile(Fireball.class);
                    fb.setInvulnerable(true);
                    Vector from = new Vector(blaze.getLocation().getX(), blaze.getLocation().getY(), blaze.getLocation().getZ());
                    Vector to  = new Vector(e.getTo().getX(), e.getTo().getY(), e.getTo().getZ());
                    Vector v = to.subtract(from).subtract(new Vector(0,2,0));
                    fb.setVelocity(v);

                    Main.game.getTeamManager().addBlazeCooldown(blaze);

                }
            }
        }
    }

    @EventHandler
    public void idk (EntityExplodeEvent e){

    }
}
