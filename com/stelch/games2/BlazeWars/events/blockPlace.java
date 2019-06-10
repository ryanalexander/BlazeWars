package com.stelch.games2.BlazeWars.events;

import com.stelch.games2.BlazeWars.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class blockPlace implements Listener {

    @EventHandler
    public void blockPlace(BlockPlaceEvent e){
        Main.game.doBlockUpdate(e.getBlock().getLocation(),e.getBlock());
    }

    @EventHandler
    public void blockBreak(BlockPlaceEvent e){
        Main.game.doBlockUpdate(e.getBlock().getLocation(),e.getBlock());
    }

}
