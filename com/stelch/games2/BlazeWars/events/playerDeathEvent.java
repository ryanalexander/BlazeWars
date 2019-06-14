package com.stelch.games2.BlazeWars.events;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.Utils.Spectator;
import com.stelch.games2.BlazeWars.Utils.text;
import com.stelch.games2.BlazeWars.varables.teamColors;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class playerDeathEvent implements Listener {

    private String[] messages = new String[]{
        "%s&7 has been slain by %s",
        "%s&7 has been clapped by %s",
        "%s&7 has been wasted by %s",
        "%s&7 has been rekt by %s"
    };

    Main plugin;
    public playerDeathEvent(Main main){
        plugin=main;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        e.setDeathMessage(null);
        e.setDroppedExp(0);
    }

    @EventHandler
    public void EntityDamageEntity(EntityDamageByEntityEvent e){
        switch (e.getEntityType()){
            case PLAYER:
                Player player = (Player)e.getEntity();
                if((player.getHealth()-e.getFinalDamage())<=1){
                    player.setHealth(20);
                    e.setCancelled(true);
                    doDeath(player,String.format(messages[0],player.getDisplayName(),resolveDamager(e.getDamager()).getDisplayName()));
                }
                break;
            default:
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent e){
        if(e.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING)){e.setCancelled(true);}
        switch (e.getEntityType()){
            case PLAYER:
                if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)){return;}
                Player player = (Player)e.getEntity();
                if((player.getHealth()-e.getFinalDamage())<1){
                    e.setCancelled(true);
                    player.setHealth(20);
                    doDeath(player,String.format(messages[0],player.getDisplayName(),e.getCause()));
                }
                break;
            default:
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void doDeath(Player player, String message) {
        Bukkit.broadcastMessage(text.f(message));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,1,1);
        player.teleport(Main.game.getMap().getSpawnLocation());

        if(Main.game.getTeamManager().getCanRespawn(Main.game.getTeamManager().getTeam(player))){
            Spectator spec = new Spectator(player);
            for(Player p : Bukkit.getOnlinePlayers()){
                p.hidePlayer(plugin,player);
            }

            new BukkitRunnable(){
                int timer = 5;
                @Override
                public void run() {
                    player.sendTitle(text.f("&cYou died"),String.format(text.f("&cRespawning in %s Seconds"),timer),0,20,0);
                    timer--;

                    if(timer==0){
                        spec.leave();
                        player.sendTitle("","",0,0,0);
                        FileConfiguration config = plugin.getConfig();
                        teamColors team = Main.game.getTeamManager().getTeam(player);
                        Double x = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.x"),"world",team)));
                        Double y = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.y"),"world",team)));
                        Double z = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.z"),"world",team)));
                        Float yaw = Float.parseFloat(config.getString(String.format(("maps.%s.spawn.%s.yaw"),"world",team)));
                        Float pitch = Float.parseFloat("0.0");
                        Location teamSpawn=new Location(player.getWorld(),x,y,z,yaw,pitch);

                        player.setHealth(20);
                        player.setVelocity(new Vector(0,0,0));
                        player.teleport(teamSpawn);
                        player.getInventory().clear();
                        player.setGameMode(GameMode.SURVIVAL);
                        for(Player p : Bukkit.getOnlinePlayers()){
                            p.showPlayer(plugin,player);
                        }
                        cancel();

                    }
                }
            }.runTaskTimer(plugin,0L,20L);
        }else {
            Spectator spec = new Spectator(player);
            player.sendTitle(text.f("&cEliminated"),"&cYou may no longer respawn",1,5,1);
        }
    }

    public static Player resolveDamager(Entity e) {
        switch (e.getType()){
            case ARROW:
                return ((Player)((Arrow)e).getShooter());
            case PLAYER:
                return (Player)e;
            case SNOWBALL:
                return ((Player)((Snowball)e).getShooter());
            case PRIMED_TNT:
                return ((Player)((TNTPrimed)e).getSource());
            default:
                return null;
        }
    }
}
