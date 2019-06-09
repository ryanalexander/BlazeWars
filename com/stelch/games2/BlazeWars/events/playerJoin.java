package com.stelch.games2.BlazeWars.events;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.Utils.text;
import javafx.event.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class playerJoin implements Listener {

    Main plugin;
    public playerJoin(Main main){
        plugin=main;
    }

    @org.bukkit.event.EventHandler
    public void playerJoin(PlayerJoinEvent e){

        switch(Main.game.getGamestate()){
            case LOBBY:
                Bukkit.broadcastMessage(text.f(String.format("&aJOIN> &7Welcome &9%s&7, ( &9%s &7/ &9%s &7) for game to start" ,e.getPlayer().getDisplayName(),Bukkit.getOnlinePlayers().size(),Main.game.getMin_players())));
        }

    }
}
