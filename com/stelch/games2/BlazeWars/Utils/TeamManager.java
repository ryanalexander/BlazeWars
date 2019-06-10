package com.stelch.games2.BlazeWars.Utils;

import com.stelch.games2.BlazeWars.varables.teamColors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TeamManager {

    private HashMap<Player,teamColors> players = new HashMap<>();



    public void assignTeams(){
        teamColors team;
        int iterator = 0;

        for(Player p : Bukkit.getOnlinePlayers()){
            if(iterator==0){
                players.put(p,teamColors.blue);
                team=teamColors.blue;
                iterator=1;
            }else {
                players.put(p,teamColors.pink);
                team=teamColors.pink;
                iterator=0;
            }

            p.sendMessage(text.f(String.format("&aTeams> &7You have been assigned to &e&l%s&7 team.", team.toString().toUpperCase())));
        }

    }

    public HashMap<Player, teamColors> getPlayers() {
        return players;
    }
}
