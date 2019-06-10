package com.stelch.games2.BlazeWars.events;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.Utils.text;
import com.stelch.games2.BlazeWars.varables.gameState;
import com.stelch.games2.BlazeWars.varables.teamColors;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

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
    public void onDamage(EntityDamageByEntityEvent e) {
        if(e.getEntity().getType() != PLAYER){
            Bukkit.broadcastMessage("Damage negated as non-player entity");
            return;
        }
        Player player = (Player) e.getEntity();
        Player attacker = (Player) e.getDamager();
        Bukkit.broadcastMessage(String.format("Calculation of health: %s",(player.getHealth()-e.getFinalDamage())));
        if ((player.getHealth()-e.getFinalDamage())<=1) {
            e.setCancelled(true);
            if(Main.game.getGamestate()==gameState.IN_GAME){
                Bukkit.broadcastMessage(
                        text.f(
                                String.format("&eDEATH> &f"+messages[ThreadLocalRandom.current().nextInt(0, messages.length + 1)],
                                        player.getDisplayName(),
                                        attacker.getDisplayName()
                                )
                        ));
                attacker.playSound(attacker.getLocation(), Sound.ENTITY_BAT_DEATH,1,1);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,1,1);

                player.setGameMode(GameMode.SPECTATOR);
                player.sendTitle("You have died","Respawning in 10s");

                player.setHealth(20);
            }else {
                Bukkit.broadcastMessage("Not in valid game state");
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(Main.game.isGameEntity(e.getEntity())){
            e.setCancelled(true);
        }
    }
}
