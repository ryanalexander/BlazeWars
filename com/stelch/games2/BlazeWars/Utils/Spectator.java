/*
 *
 * *
 *  *
 *  * © Stelch Games 2019, distribution is strictly prohibited
 *  *
 *  * Changes to this file must be documented on push.
 *  * Unauthorised changes to this file are prohibited.
 *  *
 *  * @author Ryan Wood
 *  * @since 14/7/2019
 *
 */

package com.stelch.games2.BlazeWars.Utils;

import com.stelch.games2.BlazeWars.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Spectator {

    Player player;
    public Spectator(Player player) {
        this.player=player;
        for(Player p : Bukkit.getOnlinePlayers()){
            p.hidePlayer(Main.getPlugin(Main.class),(Player)this.player);
        }

        //Main.game.spectators.put(player,this);
        player.getInventory().clear();
        player.setAllowFlight(true);
        player.setSilent(true);

    }

    public void leave() {
        for(Player p : Bukkit.getOnlinePlayers()){
            player.showPlayer(Main.getPlugin(Main.class),(Player)this.player);
        }
        //Main.game.spectators.remove(player);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.setSilent(false);
        if(player.getGameMode().equals(GameMode.SURVIVAL))player.setAllowFlight(false);
    }
}
