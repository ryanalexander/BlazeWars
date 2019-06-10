package com.stelch.games2.BlazeWars.events;

import com.stelch.games2.BlazeWars.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EntityInteract implements Listener {

    @EventHandler
    public void EntityInteractEvent(PlayerInteractEntityEvent e){
        if(Main.game.isFunctionEntity(e.getRightClicked())){
            e.setCancelled(true);
            Main.game.getEntityFunction(e.getRightClicked()).run(e.getPlayer());
        }
    }

}
