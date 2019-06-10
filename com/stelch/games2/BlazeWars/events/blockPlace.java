package com.stelch.games2.BlazeWars.events;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.Utils.text;
import com.stelch.games2.BlazeWars.varables.gameState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.TNT;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class blockPlace implements Listener {

    @EventHandler
    public void blockPlace(BlockPlaceEvent e){
        if(Main.game.getGamestate()==gameState.IN_GAME){
            Main.game.doBlockUpdate(e.getBlock().getLocation(),e.getBlock());

            if(e.getBlock().getType()==Material.TNT){
                e.getBlock().setType(Material.AIR);
                TNTPrimed tnt = (TNTPrimed)e.getBlock().getLocation().getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.PRIMED_TNT);
                tnt.setFuseTicks(43);
            }
        }
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent e){
        if(Main.game.getGamestate()==gameState.IN_GAME){
            if(!(Main.game.canBreakBlock(e.getBlock()))){
                e.getPlayer().sendMessage(text.f("&cYou can only break blocks placed by players."));
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void tntExplode(EntityExplodeEvent e){
        for(Block b : e.blockList()){
            if(Main.game.canBreakBlock(b)){
                Main.game.doBlockUpdate(b.getLocation(),b);
                b.setType(Material.AIR);
            }
        }
        e.setCancelled(true);
    }

}
