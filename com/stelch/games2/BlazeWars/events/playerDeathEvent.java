package com.stelch.games2.BlazeWars.events;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.Utils.TeamManager;
import com.stelch.games2.BlazeWars.Utils.text;
import com.stelch.games2.BlazeWars.varables.gameState;
import com.stelch.games2.BlazeWars.varables.teamColors;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.data.type.Snow;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

import static org.bukkit.entity.EntityType.PLAYER;

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
    public void onDamage(EntityDamageByEntityEvent e) {
        if(!(e.getEntity().getType().equals(PLAYER))){return;}
        Player player = (Player) e.getEntity();
        Player attacker = resolveDamager(e.getDamager());
        if ((player.getHealth()-e.getFinalDamage())<=1) {
            e.setCancelled(true);
            if(Main.game.getGamestate()==gameState.IN_GAME){
                Bukkit.broadcastMessage(
                        text.f(
                                String.format("&eDEATH> &f"+messages[ThreadLocalRandom.current().nextInt(0, messages.length)],
                                        Main.game.getTeamManager().getTeamColor(Main.game.getTeamManager().getTeam(player))+player.getName(),
                                        Main.game.getTeamManager().getTeamColor(Main.game.getTeamManager().getTeam(attacker))+attacker.getName()
                                )
                        ));
                if(attacker!=null)attacker.playSound(attacker.getLocation(), Sound.ENTITY_BAT_DEATH,1,1);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,1,1);

                player.setGameMode(GameMode.SPECTATOR);
                doRespawnPlayer(player);
            }else {
            }
        }
    }

    public void doRespawnPlayer(Player player){
        teamColors team = Main.game.getTeamManager().getTeam(player);
        if(!(Main.game.getTeamManager().getCanRespawn(team))){
            player.sendTitle(text.f("&c&lYou have been eliminated"),text.f(("&eYou may no longer respawn")),2,30,2);
            return;
        }
        new BukkitRunnable(){
            int respawn_time = 5;
            public void run() {
                if(respawn_time<=0){
                    FileConfiguration config = plugin.getConfig();
                    teamColors team = Main.game.getTeamManager().getTeam(player);
                    Double x = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.x"),"world",team)));
                    Double y = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.y"),"world",team)));
                    Double z = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.z"),"world",team)));
                    Float yaw = Float.parseFloat(config.getString(String.format(("maps.%s.spawn.%s.yaw"),"world",team)));
                    Float pitch = Float.parseFloat("0.0");
                    Location teamSpawn=new Location(player.getWorld(),x,y,z,yaw,pitch);
                    player.sendTitle(" ","");
                    player.teleport(teamSpawn);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setHealth(20);
                    cancel();
                }else {
                    player.sendTitle(text.f("&cYou have died"),text.f(String.format("&eRespawning in %s",respawn_time)),0,30,0);
                    respawn_time--;
                }
            }
        }.runTaskTimer(plugin,20L,20L);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(e.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING))e.setCancelled(true);
        if(e.getEntity() instanceof Player&&(((((Player) e.getEntity()).getHealth()-e.getFinalDamage())<=1)||e.getCause().equals(EntityDamageEvent.DamageCause.VOID))) {
            e.getCause();
            Player player = (Player)e.getEntity();
            if((((Player) e.getEntity()).getHealth()-e.getFinalDamage())<=1)e.setCancelled(true);
            Double x = Double.parseDouble(plugin.getConfig().getString("maps.world.mid.x"));
            Double y = Double.parseDouble(plugin.getConfig().getString("maps.world.mid.y"));
            Double z = Double.parseDouble(plugin.getConfig().getString("maps.world.mid.z"));
            Location mid=new Location(player.getWorld(),x,y,z);
            player.teleport(mid);
            player.setGameMode(GameMode.SPECTATOR);
            Bukkit.broadcastMessage(
                    text.f(
                            String.format("&eDEATH> &f"+messages[ThreadLocalRandom.current().nextInt(0, messages.length)],
                                    player.getDisplayName(),
                                    e.getCause().toString()
                            )
                    ));
            doRespawnPlayer(((Player) e.getEntity()).getPlayer());
        }
        if(Main.game.isGameEntity(e.getEntity())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void EntityDamageByBlockEvent(EntityDamageByBlockEvent e){
        e.setCancelled(false);
        return;
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
