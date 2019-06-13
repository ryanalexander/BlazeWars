package com.stelch.games2.BlazeWars.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreboardManager {

    private org.bukkit.scoreboard.ScoreboardManager manager;
    private String name;
    private Scoreboard board;
    private Objective objective;
    private HashMap<Integer,String> lines = new HashMap<>();
    private int counter = 32;
    private String payload = " ";
    private int payload_count = 1;

    public ScoreboardManager(String name) {
        this.name=name;
        this.manager = Bukkit.getServer().getScoreboardManager();
        this.board = this.manager.getNewScoreboard();
        this.objective = this.board.registerNewObjective("minigame","dummy",text.f(name));
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void setDisplayname(String name) { this.objective.setDisplayName(text.f(name)); }

    public int addLine(String message) { this.objective.getScore(text.f(message)).setScore(counter);counter--;this.lines.put(counter,text.f(message)); this.update();return counter;}
    public int addBlank() {String message="";for(int i=payload_count;i>0;i--){message+=payload;}addLine(message);this.lines.put(counter,message);payload_count++; this.update();return counter;}

    public void editLine(int line, String message){ this.lines.put(line,text.f(message)); this.update();}

    public void clear() {this.lines.clear();}

    public void update() {
        this.objective.unregister();
        this.objective = this.board.registerNewObjective("minigame","dummy",text.f(this.name));
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        for(Map.Entry<Integer,String> line : lines.entrySet()){
            this.objective.getScore(line.getValue()).setScore(line.getKey());
        }
        for(Player player : Bukkit.getOnlinePlayers()){
            player.setScoreboard(this.board);
        }
    }
}
