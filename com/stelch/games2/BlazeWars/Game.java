package com.stelch.games2.BlazeWars;

import com.stelch.games2.BlazeWars.Utils.TeamManager;
import com.stelch.games2.BlazeWars.Utils.text;
import com.stelch.games2.BlazeWars.varables.gameState;
import com.stelch.games2.BlazeWars.varables.gameType;
import com.stelch.games2.BlazeWars.varables.teamColors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class Game {

    private String title;

    private int min_players;
    private int max_players;

    private gameState gamestate = gameState.DISABLED;

    private TeamManager teamManager;

    private int start_time = 10;

    private Main handler;

    private boolean allow_spectators=false;

    private boolean auto_start = true;

    private gameType gamemode;

    public Game(String title, gameType gamemode, int min_players, int max_players, Main handler){
        teamManager=new TeamManager();
        this.title=title;
        this.gamemode=gamemode;
        this.min_players=min_players;
        this.max_players=max_players;
        this.handler=handler;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAllow_spectators(boolean allow_spectators) {
        this.allow_spectators = allow_spectators;
    }

    public void setGamemode(gameType gamemode) {
        this.gamemode = gamemode;
    }

    public void setMax_players(int max_players) {
        this.max_players = max_players;
    }

    public void setGamestate(gameState gamestate) {
        this.gamestate = gamestate;
    }

    public void setMin_players(int min_players) {
        this.min_players = min_players;
    }

    public String getTitle() {
        return title;
    }

    public gameState getGamestate() {
        return gamestate;
    }

    public int getMax_players() {
        return max_players;
    }

    public int getMin_players() {
        return min_players;
    }

    public boolean isAllow_spectators() {
        return allow_spectators;
    }

    public gameType getGamemode() {
        return gamemode;
    }


    public boolean canStart() {
        boolean canstart = true;
        if(Bukkit.getOnlinePlayers().size()<this.min_players){canstart=false;}
        if(!this.auto_start){canstart=false;}

        return canstart;
    }

    public void start() {

        // Start countdown
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.handler, new Runnable() {
            @Override
            public void run() {
                setGamestate(gameState.STARTING);
                Bukkit.broadcastMessage(text.f(String.format("&aGAME> &fThe game will begin in %s",start_time)));
                if(start_time==0){
                    Bukkit.getScheduler().cancelTasks(handler);

                    teamManager.assignTeams();

                    for(Map.Entry<Player, teamColors> teamPlayer : teamManager.getPlayers().entrySet()){
                        Player player = teamPlayer.getKey();
                        teamColors team = teamPlayer.getValue();
                        FileConfiguration config = handler.getConfig();
                        Bukkit.broadcastMessage(config.getString(String.format(("maps.%s.spawn.%s.z"),"world","red")));
                        Double x = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.x"),"world",team)));
                        Double y = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.y"),"world",team)));
                        Double z = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.z"),"world",team)));
                        Location teamSpawn=new Location(player.getWorld(),x,y,z);

                        player.teleport(teamSpawn);
                    }

                    for(teamColors s : teamColors.values()){
                        FileConfiguration config = handler.getConfig();
                        Double x = Double.parseDouble(config.getString(String.format(("maps.%s.core.%s.x"),"world",s)));
                        Double y = Double.parseDouble(config.getString(String.format(("maps.%s.core.%s.y"),"world",s)));
                        Double z = Double.parseDouble(config.getString(String.format(("maps.%s.core.%s.z"),"world",s)));
                        Location core=new Location(Bukkit.getWorld("world"),x,y,z);

                        core.getBlock().setType(Material.OBSIDIAN);
                    }

                }
                start_time--;
            }
        },1,25);

    }
}
