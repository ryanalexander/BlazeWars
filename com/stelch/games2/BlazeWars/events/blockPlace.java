package com.stelch.games2.BlazeWars.events;

import com.stelch.games2.BlazeWars.Inventories.Item;
import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.Utils.text;
import com.stelch.games2.BlazeWars.varables.gameState;
import com.stelch.games2.BlazeWars.varables.teamColors;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityTNTPrimed;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.TNT;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftTNTPrimed;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

public class blockPlace implements Listener {

    @EventHandler
    public void blockPlace(BlockPlaceEvent e){
        if(Main.game.getGamestate()==gameState.IN_GAME){
            Main.game.doBlockUpdate(e.getBlock().getLocation(),e.getBlock());

            if(e.getBlock().getType()==Material.TNT){
                e.getBlock().setType(Material.AIR);
                TNTPrimed tnt = (TNTPrimed)e.getBlock().getLocation().getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.PRIMED_TNT);
                EntityTNTPrimed nmsTNT = (EntityTNTPrimed) (((CraftTNTPrimed) tnt).getHandle());
                try {
                    Field sourceField = EntityTNTPrimed.class.getDeclaredField("source");
                    sourceField.setAccessible(true);
                    sourceField.set(nmsTNT, (EntityLiving)(((CraftLivingEntity) e.getPlayer()).getHandle()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                tnt.setFuseTicks(43);
            }
        }
    }

    @EventHandler
    public void craft(CraftItemEvent e){
        if(Main.game.getGamestate()==gameState.IN_GAME)
            e.setCancelled(true);
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent e){
        if(Main.game.getGamestate()==gameState.IN_GAME){
            if(e.getBlock().getType().equals(Material.CLAY)){
                e.setDropItems(false);
                Item item = new Item(Material.CLAY_BALL,"&bWell... This is useless");
                item.setAmount(1);
                e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation(),item.spigot());
            }
            if(!(Main.game.canBreakBlock(e.getBlock()))){
                e.getPlayer().sendMessage(text.f("&cYou can only break blocks placed by players."));
                e.setCancelled(true);
            }else {
                if(Main.game.getTeamManager().isCore(e.getBlock())) {
                    if(Main.game.getTeamManager().getCore(Main.game.getTeamManager().getTeam(e.getPlayer())).equals(e.getBlock())){
                        e.getPlayer().sendMessage(text.f("&cHey! That's your core."));
                        e.setCancelled(true);
                        return;
                    }
                    if (Main.game.getTeamManager().getCanRespawn(Main.game.getTeamManager().getCore(e.getBlock()))) {
                        e.setCancelled(true);
                        teamColors team = Main.game.getTeamManager().getCore(e.getBlock());
                        Main.game.getTeamManager().setCantRespawn(team, true);
                        Main.game.getTeamManager().getBlaze(team).remove();
                        e.getPlayer().getWorld().strikeLightning(e.getBlock().getLocation());
                        e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
                        e.getBlock().setType(Material.AIR);
                        Bukkit.broadcastMessage(String.format("%s's core has been destroyed! They are now vulnerable.", team));
                    }
                }
            }
        }
    }

    @EventHandler
    public void tntExplode(EntityExplodeEvent e){
        for(Block b : e.blockList()){
            if(Main.game.canBreakBlock(b)){
                Main.game.doBlockUpdate(b.getLocation(),b);
                if(b.getType()==Material.GLASS||Main.game.getTeamManager().isCore(b)){
                    continue;
                }
                b.setType(Material.AIR);
            }
        }
        e.setCancelled(true);
    }

}
