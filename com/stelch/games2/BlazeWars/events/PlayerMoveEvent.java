package com.stelch.games2.BlazeWars.events;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.core.Utils.Text;
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
        for(Entity entity : Main.game.getGameEntities()) {
            if(entity.getType().equals(EntityType.BLAZE)){
                if(e.getTo().distanceSquared(entity.getLocation())<100){
                    Blaze blaze = (Blaze)entity;
                    Fireball fb = blaze.launchProjectile(Fireball.class);
                    Vector v = e.getTo().toVector();
                    fb.setVelocity(v.multiply(5));
                    fb.setGlowing(true);
                }
            }
        }
    }

}
