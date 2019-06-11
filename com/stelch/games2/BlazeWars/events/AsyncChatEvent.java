package com.stelch.games2.BlazeWars.events;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.Utils.text;
import com.stelch.games2.BlazeWars.varables.gameState;
import com.stelch.games2.BlazeWars.varables.teamColors;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncChatEvent implements Listener {


    enum TeamColors {
        RED("&c"), GREEN("&a"), BLUE("&b"), PINK("&d");
        private String color;
        public String getColor() {return this.color;}
        private TeamColors(String color){this.color = color;}
    }

    @EventHandler
    public void ChatEvent(AsyncPlayerChatEvent e){
        e.setCancelled(true);
        if(Main.game.getGamestate()== gameState.IN_GAME) {
            teamColors team = Main.game.getTeamManager().getTeam(e.getPlayer());
            Bukkit.broadcastMessage(text.f(String.format("&7[%s&7] &e%s&7: %s", TeamColors.valueOf(team.toString().toUpperCase()).getColor() + team.toString().toUpperCase(), e.getPlayer().getDisplayName(), e.getMessage())));
        }else {
            Bukkit.broadcastMessage(text.f(String.format("&e%s&7: %s", e.getPlayer().getDisplayName(), e.getMessage())));
        }
    }

}