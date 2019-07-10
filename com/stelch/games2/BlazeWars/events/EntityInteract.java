package com.stelch.games2.BlazeWars.events;

import com.stelch.games2.BlazeWars.Game;
import com.stelch.games2.BlazeWars.Inventories.Item;
import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.varables.gameState;
import com.stelch.games2.BlazeWars.varables.lang;
import com.stelch.games2.BlazeWars.varables.teamColors;
import com.stelch.games2.core.Utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Map;

import static org.bukkit.Material.FIRE_CHARGE;
import static org.bukkit.Material.MAGMA_CUBE_SPAWN_EGG;

public class EntityInteract implements Listener {

    @EventHandler
    public void itemPickup(PlayerPickupItemEvent e){
        if(Main.game.spectators.containsKey(e.getPlayer())){e.setCancelled(true);}
    }

    @EventHandler
    public void InventorySwitch(PlayerItemHeldEvent e){

        if(Main.game.invis_players.containsKey(e.getPlayer())){
            Main.game.invis_players.get(e.getPlayer()).setItemInHand(e.getPlayer().getInventory().getItem(e.getNewSlot()));
        }

    }

    @EventHandler
    public void EntityDamage(EntityDamageEvent e){
        if(Main.game.getGamestate().equals(gameState.LOBBY)){e.setCancelled(true);}
    }

    @EventHandler
    public void EntityDamageEntity(EntityDamageByEntityEvent e){
        if(e.getEntity().getType().equals(EntityType.PLAYER)&&Main.game.spectators.containsKey((Player)e.getEntity())){e.setCancelled(true);}
        if(e.getDamager().getType().equals(EntityType.PLAYER)&&Main.game.spectators.containsKey((Player)e.getDamager())){e.setCancelled(true);}
        if(e.getEntity() instanceof ArmorStand){
            if(Main.game.invis_players.containsValue(e.getEntity())){
                for(Map.Entry<Player,ArmorStand> d : Main.game.invis_players.entrySet()){
                    if(d.getValue().equals(e.getEntity())){
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.showPlayer(Main.getPlugin(Main.class), d.getKey());
                            d.getValue().remove();
                        }
                        Main.game.invis_players.remove(d.getKey());
                        return;
                    }
                }
            }
        }else if(e.getEntityType().equals(EntityType.MAGMA_CUBE)){
            MagmaCube magmaCube = (MagmaCube)e.getEntity();
            String name = "";
            magmaCube.setHealth(e.getDamage()-magmaCube.getHealth());
            double h = magmaCube.getHealth();
            for(int i=0;i<magmaCube.getMaxHealth();i++){
                h--;
                if(h>=i){
                    name+="&a:&r";
                }else {
                    name+="&7:&r";
                }
            }
            magmaCube.setCustomName(Text.format(name));
            magmaCube.setCustomNameVisible(true);
        }
    }

    @EventHandler
    public void EntityInteract(PlayerInteractEvent e){
        if(Main.game.spectators.containsKey(e.getPlayer())){e.setCancelled(true);}
        if(!Main.game.getGamestate().equals(gameState.IN_GAME)){return;}
        /*
         * CUSTOM MAGMA CUBE
         */
        if(e.getItem()!=null&&e.getItem().getType().equals(MAGMA_CUBE_SPAWN_EGG)){
            e.setCancelled(true);
            e.getPlayer().getInventory().remove(e.getItem());
            for(int i1=0;i1<=3;i1++) {
                MagmaCube magmaCube = (MagmaCube) e.getPlayer().getLocation().getWorld().spawnEntity(e.getPlayer().getLocation(), EntityType.MAGMA_CUBE);
                magmaCube.setAI(true);
                magmaCube.setHealth(7);
                magmaCube.setSize(1);
                String name = "";
                for (int i = 0; i < magmaCube.getHealth(); i++) {
                    name += "&a:&r";
                }
                magmaCube.setCustomName(Text.format(name));
                magmaCube.setCustomNameVisible(true);
            }
        }
        /*
         * EGG BRIDGE
         */
        if(e.getItem()!=null&&e.getItem().getType().equals(Material.EGG)){
            e.setCancelled(true);
            e.getPlayer().getInventory().remove(e.getItem());
            Egg egg = e.getPlayer().launchProjectile(Egg.class);
            Location eyeLocation = e.getPlayer().getEyeLocation();
            Vector vec = e.getPlayer().getLocation().getDirection();
            egg.teleport(eyeLocation.add(vec));
            new BukkitRunnable(){
                @Override
                public void run() {
                    if(!egg.isDead()){
                        Location l = egg.getLocation().clone();
                        new BukkitRunnable(){
                            @Override
                            public void run() {

                                for(int i=0;i<2;i++){
                                    Location x = l.clone().add(i,0,0);
                                    Location y = l.clone().add(0,i,0);
                                    Location z = l.clone().add(0,0,i);
                                    x.getBlock().setType(Material.WHITE_WOOL);
                                    y.getBlock().setType(Material.WHITE_WOOL);
                                    z.getBlock().setType(Material.WHITE_WOOL);

                                    Main.game.doBlockUpdate(x,x.getBlock());
                                    Main.game.doBlockUpdate(y,y.getBlock());
                                    Main.game.doBlockUpdate(z,z.getBlock());
                                }
                            }
                        }.runTaskLater(Main.getPlugin(Main.class),2L);
                    }else {
                        cancel();
                    }
                }
            }.runTaskTimer(Main.getPlugin(Main.class),0L,0L);
        }


        /*
         * FIREBALL
         */
        if(e.getItem().getType().equals(FIRE_CHARGE)){
            e.setCancelled(true);
            e.getPlayer().getInventory().remove(e.getItem());
            org.bukkit.entity.Fireball fb = e.getPlayer().launchProjectile(org.bukkit.entity.Fireball.class);
            fb.setInvulnerable(true);
            Vector v = e.getPlayer().getLocation().getDirection().multiply(2);
            fb.setVelocity(v);
        }
    }

    @EventHandler
    public void EntityInteractEvent(PlayerInteractEntityEvent e){
        if(Main.game.spectators.containsValue(e.getPlayer())){e.setCancelled(true);}
        if(Main.game.isFunctionEntity(e.getRightClicked())){
            e.setCancelled(true);
            Game.click action = Main.game.getEntityFunction(e.getRightClicked());
            if(action!=null&&e.getPlayer()!=null)
                action.run(e.getPlayer());
        }
    }

    @EventHandler
    public void chestOpen(InventoryOpenEvent e){
        if(Main.game.spectators.containsKey(e.getPlayer())){e.setCancelled(true);}
        if (e.getInventory().getHolder()==null) {return;}
        if(e.getInventory().getType().equals(InventoryType.CHEST)){
            if(Main.game.getGamestate()== gameState.IN_GAME&&e.getInventory().getLocation().getBlock()!=null) {
                teamColors teamChest = Main.game.getTeamManager().getTeamChest(e.getInventory().getLocation().getBlock());
                if (teamChest == null) {
                    return;
                }
                teamColors team = Main.game.getTeamManager().getTeam((Player) e.getPlayer());
                if (!(team.equals(teamChest))&&Main.game.getTeamManager().getCanRespawn(teamChest)) {
                    e.getPlayer().sendMessage(Text.format(String.format(lang.CHEST_TEAM_NOT_ELIMINATED.get(),Main.game.getTeamManager().getTeamColor(teamChest)+teamChest)));
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void eggThrow(PlayerEggThrowEvent e){
        e.setHatching(false);
    }

}
