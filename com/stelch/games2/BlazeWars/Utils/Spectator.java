package com.stelch.games2.BlazeWars.Utils;

import com.stelch.games2.BlazeWars.Main;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Spectator {

    Player player;
    public Spectator(Player player) {
        this.player=player;

        Main.game.spectators.add(player);
        player.getInventory().clear();
        //player.setFlying(true);
        //player.setAllowFlight(true);
    }

    public void leave() {
        Main.game.spectators.remove(player);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
    }
}
