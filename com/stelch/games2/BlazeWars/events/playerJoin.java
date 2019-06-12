package com.stelch.games2.BlazeWars.events;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.Utils.text;
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

        switch(Main.game.getGamestate()){
            case LOBBY:
                Bukkit.broadcastMessage(text.f(String.format("&aJOIN> &7Welcome &9%s&7, ( &9%s &7/ &9%s &7) for game to start" ,e.getPlayer().getDisplayName(),Bukkit.getOnlinePlayers().size(),Main.game.getMin_players())));
                if(Main.game.canStart()){
                    Main.game.start();
                }
                break;
            case STARTING:
                Bukkit.broadcastMessage(text.f(String.format("&aJOIN> &7Welcome &9%s&7, ( &9%s &7/ &9%s &7)" ,e.getPlayer().getDisplayName(),Bukkit.getOnlinePlayers().size(),Main.game.getMin_players())));
                break;
            case IN_GAME:
                if(Main.game.isAllow_spectators()){
                    e.getPlayer().setGameMode(GameMode.SPECTATOR);
                    e.getPlayer().sendTitle(text.f("&cYou are now a spectator"),"This game is already in progress. You may only spectate.");
                }else {
                    e.getPlayer().kickPlayer(text.f("&aJOIN> &7This game has already started. You were returned to the lobby."));
                }
                break;

            default:
                e.getPlayer().kickPlayer(text.f(String.format("&cERROR> &7Failed to join the requested game. [Game-state: %s]",Main.game.getGamestate())));
                break;
        }

    }
}
