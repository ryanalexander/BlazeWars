package com.stelch.games2.BlazeWars.events;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.Utils.Spectator;
import com.stelch.games2.BlazeWars.varables.gameState;
import com.stelch.games2.BlazeWars.varables.teamColors;
import com.stelch.games2.core.Utils.Text;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class playerDeathEvent implements Listener {

    private String[] messages = new String[]{
        "%s&7 has been slain by %s",
        "%s&7 has been clapped by %s",
        "%s&7 has been wasted by %s",
        "%s&7 has been rekt by %s"
    };

    private String[] fall = new String[]{
            "%s&7 fell and hit their head",
            "&7Balance is not %s&7's strong point",
            "%s&7 didn't notice the edge",
            "%s&7 was given swift end by the ground"
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
        if(!(Main.game.getGamestate().equals(gameState.IN_GAME))) return;
        switch (e.getEntityType()){
            case PLAYER:
                Player player = (Player)e.getEntity();
                if((player.getHealth()-e.getFinalDamage())<=1){
                    player.setHealth(20);
                    e.setCancelled(true);
                    doDeath(player,String.format(messages[0],player.getDisplayName(),resolveDamager(e.getDamager()).getDisplayName()),(Player) e.getDamager());
                }
                break;
            default:
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent e){
        if(e.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING)){e.setCancelled(true);}
        if(!(Main.game.getGamestate().equals(gameState.IN_GAME))) return;
        switch (e.getEntityType()){
            case PLAYER:
                if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)){return;}
                Player player = (Player)e.getEntity();
                if(Main.game.spectators.containsKey(player)) {
                    e.setCancelled(true);
                    return;
                }
                if((player.getHealth()-e.getFinalDamage())<1){
                    e.setCancelled(true);
                    player.setHealth(20);
                    if(e.getCause().equals(EntityDamageEvent.DamageCause.VOID)||e.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
                        Random rand = new Random();
                        doDeath(player,String.format(fall[rand.nextInt(fall.length)],player.getDisplayName(),e.getCause()),null);
                    }else {
                        Random rand = new Random();
                        doDeath(player,String.format(messages[rand.nextInt(messages.length)],player.getDisplayName(),e.getCause()),null);
                    }
                }
                break;
            default:
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void doDeath(Player player, String message, Player damager) {
        teamColors team = Main.game.getTeamManager().getTeam(player);
        Bukkit.broadcastMessage(Text.format(message));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,1,1);
        player.setHealth(20);
        player.teleport(Main.game.getMap().getSpawnLocation());
        player.setAllowFlight(true);
        player.setFlying(true);

        if(Main.game.getTeamManager().getCanRespawn(Main.game.getTeamManager().getTeam(player))){
            Spectator spec = new Spectator(player);

            new BukkitRunnable(){
                int timer = 5;
                @Override
                public void run() {
                    player.sendTitle(Text.format("&cYou died"),String.format(Text.format("&aRespawning in %s Seconds"),timer),0,60,0);
                    timer--;

                    if(timer<=0){
                        cancel();
                        spec.leave();
                        player.sendTitle("","",0,0,0);
                        FileConfiguration config = plugin.getConfig();
                        Double x = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.x"),"world",team)));
                        Double y = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.y"),"world",team)));
                        Double z = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.z"),"world",team)));
                        Float yaw = Float.parseFloat(config.getString(String.format(("maps.%s.spawn.%s.yaw"),"world",team)));
                        Float pitch = Float.parseFloat("0.0");
                        Location teamSpawn=new Location(player.getWorld(),x,y,z,yaw,pitch);

                        player.setHealth(20);
                        player.setVelocity(new Vector(0,0,0));
                        player.teleport(teamSpawn);
                        if(damager!=null){
                            damager.sendMessage(new String[]{
                                    Text.format("&aYou have been given an inventory")
                            });
                            damager.getInventory().addItem(player.getInventory().getContents());
                        }
                        player.getInventory().clear();
                        player.setGameMode(GameMode.SURVIVAL);
                        player.setAllowFlight(false);
                        player.setFlying(false);
                        for(Player p : Bukkit.getOnlinePlayers()){
                            p.showPlayer(plugin,player);
                        }

                    }
                }
            }.runTaskTimer(plugin,0L,20L);
        }else {
            Spectator spec = new Spectator(player);
            player.sendTitle(Text.format("&cEliminated"),Text.format("&fYou may no longer respawn"),1,60,1);
            Main.game.getTeamManager().doEliminatePlayer(team,player);
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
