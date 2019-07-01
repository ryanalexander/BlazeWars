package com.stelch.games2.BlazeWars.events;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.core.Utils.Text;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class PlayerMoveEvent implements Listener {

    @EventHandler
    public void onMove(org.bukkit.event.player.PlayerMoveEvent e){
        if(Main.game.invis_players.containsKey(e.getPlayer())){
            Main.game.invis_players.get(e.getPlayer()).teleport(e.getPlayer());
            Location loc = e.getPlayer().getLocation();
            e.getTo().getWorld().spawnParticle(Particle.DRAGON_BREATH,new Location(loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),Float.valueOf(loc.getYaw()+""),Float.valueOf(loc.getPitch()+"")),1,0,0,0,0);
        }
        /*
        for(Entity entity : Main.game.getGameEntities()) {
            if(entity.getType().equals(EntityType.BLAZE)){
                    if(e.getTo().distanceSquared(entity.getLocation())<300){
                    Blaze blaze = (Blaze)entity;
                    Fireball fb = blaze.launchProjectile(Fireball.class);
                    fb.setInvulnerable(true);
                    fb.setIsIncendiary(true);
                    Vector v = entity.getLocation().getDirection().subtract(e.getTo().getDirection().add(new Vector(0,1,0)));
                    fb.setVelocity(v.multiply(5));
                    fb.setGlowing(true);
                    Main.game.getTeamManager().addBlazeCooldown(blaze);

                }
            }
        }*/
    }

}
