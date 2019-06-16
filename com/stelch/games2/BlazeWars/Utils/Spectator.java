package com.stelch.games2.BlazeWars.Utils;

import com.stelch.games2.BlazeWars.Main;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Spectator {

    Player player;
    public Spectator(Player player) {
        this.player=player;

        Main.game.spectators.put(player,this);
        player.getInventory().clear();
        player.setAllowFlight(true);
        player.setSilent(true);

    }

    public void leave() {
        Main.game.spectators.remove(player);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.setSilent(false);
        if(player.getGameMode().equals(GameMode.SURVIVAL))player.setAllowFlight(false);
    }
}
