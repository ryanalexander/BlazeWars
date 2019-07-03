package com.stelch.games2.BlazeWars.events;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.varables.lang;
import com.stelch.games2.core.Utils.Text;
import javafx.event.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class playerJoin implements Listener {

    Main plugin;
    public playerJoin(Main main){
        plugin=main;
    }

    @org.bukkit.event.EventHandler
    public void playerJoin(PlayerJoinEvent e){
        e.setJoinMessage(null);
        Main.game.getScoreboard().update();

        switch(Main.game.getGamestate()){
            case LOBBY:
                Bukkit.broadcastMessage(Text.format(String.format(lang.GAME_PLAYER_JOIN.get() ,e.getPlayer().getDisplayName(),Bukkit.getOnlinePlayers().size(),Main.game.getMin_players())));
                if(Main.game.canStart()){
                    Main.game.start();
                }
                break;
            case STARTING:
                Main.game.getScoreboard().update();
                Bukkit.broadcastMessage(Text.format(String.format(lang.GAME_PLAYER_JOIN_STARTING.get() ,e.getPlayer().getDisplayName(),Bukkit.getOnlinePlayers().size(),Main.game.getMin_players())));
                break;
            case IN_GAME:
                if(Main.game.isAllow_spectators()){
                    e.getPlayer().setGameMode(GameMode.SPECTATOR);
                    e.getPlayer().sendTitle(Text.format("&cYou are now a spectator"),"This game is already in progress. You may only spectate.",10,60,10);
                }else {
                    e.getPlayer().kickPlayer(Text.format("&aJOIN> &7This game has already started. You were returned to the lobby."));
                }
                break;

            default:
                e.getPlayer().kickPlayer(Text.format(String.format("&cERROR> &7Failed to join the requested game. [Game-state: %s]",Main.game.getGamestate())));
                break;
        }

    }
}
