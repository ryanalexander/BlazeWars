package com.stelch.games2.BlazeWars.Utils;

import com.stelch.games2.BlazeWars.Game;
import com.stelch.games2.BlazeWars.varables.lang;
import com.stelch.games2.BlazeWars.varables.teamColors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeamManager {

    private HashMap<Player,teamColors> players = new HashMap<>();
    private ArrayList<teamColors> active_teams = new ArrayList<>();

    private HashMap<teamColors, Block> cores = new HashMap<>();
    private ArrayList<teamColors> cantRespawn = new ArrayList<>();

    private HashMap<teamColors, Integer> scoreboard_lines = new HashMap<>();

    private HashMap<teamColors, Location> forge_location = new HashMap<>();
    private HashMap<teamColors, HashMap<Material,Game.spawnner>> spawners = new HashMap<>();
    private HashMap<teamColors, Integer> spawner_level = new HashMap<>();

    private HashMap<teamColors, Block> team_chests = new HashMap<>();

    private HashMap<teamColors, Entity> team_blaze = new HashMap<>();

    public enum Colors {
        RED("&c"), GREEN("&a"), BLUE("&b"), PINK("&d"), WHITE("&f"), ORANGE("&6"), YELLOW("&e");
        private String color;
        public String getColor() {return this.color;}
        private Colors(String color){this.color = color.toUpperCase();}
    }

    public teamColors getTeam(Player p){
        return players.get(p);
    }

    public ArrayList<teamColors> getActive_teams() { return this.active_teams; }

    public void setCantRespawn (teamColors team, boolean state) {
        if(state){
            cantRespawn.add(team);
        }else {
            cantRespawn.remove(team);
        }
    }
    public boolean isCore(Block block){
        return cores.containsValue(block);
    }
    public teamColors getCore(Block block){
        for(Map.Entry<teamColors, Block> data : this.cores.entrySet()){
            if(data.getValue().equals(block)){
                return data.getKey();
            }
        }
        return null;
    }
    public Block getCore(teamColors team){
        return this.cores.get(team);
    }

    public Block getTeamChest(teamColors team) { return this.team_chests.get(team); }
    public teamColors getTeamChest(Block block) {
        for(Map.Entry<teamColors, Block> data : this.team_chests.entrySet()){
            if(data.getValue().equals(block)){
                return data.getKey();
            }
        }
        return null;
    }

    public void addCore(teamColors team, Block core){
        cores.put(team,core);
    }
    public boolean getCanRespawn(teamColors team) {
        return !cantRespawn.contains(team);
    }
    public String getTeamColor(teamColors team){
        return Colors.valueOf(team.toString().toUpperCase()).getColor();
    }

    public void addBlaze(teamColors team, Entity blaze){
        this.team_blaze.put(team,blaze);
    }

    public Entity getBlaze(teamColors team){
        return this.team_blaze.get(team);
    }

    public void addSpawnners(teamColors team, HashMap<Material,Game.spawnner> spawners) {
        this.spawners.put(team,spawners);
    }
    public Game.spawnner getSpawner(teamColors team, Material material) {
        return this.spawners.get(team).get(material);
    }

    public void setForge_location (teamColors team, Location location){
        this.forge_location.put(team,location);
    }

    public void setTeam_chests (teamColors team, Block block) {this.team_chests.put(team,block);}

    public void setScoreboardLine (teamColors team, int id) {this.scoreboard_lines.put(team,id);}
    public int getScoreboardLine (teamColors team) {return this.scoreboard_lines.get(team);}

    public Integer getSpawner_level(teamColors team) {
        return this.spawner_level.get(team);
    }

    public void setSpawner_level(teamColors team, Integer level) {
        switch (level) {
            case 0:
                this.spawner_level.put(team,level);
                break;
            case 1:
                this.spawner_level.put(team,level);
                this.spawners.get(team).get(Material.IRON_INGOT).setLevel(5);
                this.spawners.get(team).get(Material.GOLD_INGOT).setLevel(4);
                break;
            case 2:
                this.spawner_level.put(team,level);
                this.spawners.get(team).get(Material.IRON_INGOT).setLevel(6);
                this.spawners.get(team).get(Material.GOLD_INGOT).setLevel(5);
                break;
            case 3:
                this.spawner_level.put(team,level);
                this.spawners.get(team).get(Material.IRON_INGOT).setLevel(7);
                this.spawners.get(team).get(Material.GOLD_INGOT).setLevel(6);
                this.spawners.get(team).put(Material.BLAZE_ROD,new Game.spawnner(this.forge_location.get(team),new ItemStack(Material.BLAZE_ROD),0));
                break;
            case 4:
                this.spawner_level.put(team,level);
                this.spawners.get(team).get(Material.IRON_INGOT).setLevel(8);
                this.spawners.get(team).get(Material.GOLD_INGOT).setLevel(7);
                this.spawners.get(team).get(Material.BLAZE_ROD).setLevel(1);
                break;
            default:
                break;
        }
    }
    public void assignTeams(){
        teamColors team;
        int iterator = 0;

        for(Player p : Bukkit.getOnlinePlayers()){
            if(iterator==0){
                players.put(p,teamColors.BLUE);
                p.setDisplayName(getTeamColor(teamColors.BLUE)+p.getName());
                if(!(this.active_teams.contains(teamColors.BLUE)))this.active_teams.add(teamColors.BLUE);
                team=teamColors.BLUE;
                iterator++;
            }else if(iterator==1){
                players.put(p,teamColors.ORANGE);
                p.setDisplayName(getTeamColor(teamColors.ORANGE)+p.getName());
                if(!(this.active_teams.contains(teamColors.ORANGE)))this.active_teams.add(teamColors.ORANGE);
                team=teamColors.ORANGE;
                iterator++;
            }else if(iterator==2){
                players.put(p,teamColors.RED);
                p.setDisplayName(getTeamColor(teamColors.RED)+p.getName());
                if(!(this.active_teams.contains(teamColors.RED)))this.active_teams.add(teamColors.RED);
                team=teamColors.RED;
                iterator++;
            }else if(iterator==3){
                players.put(p,teamColors.WHITE);
                p.setDisplayName(getTeamColor(teamColors.WHITE)+p.getName());
                if(!(this.active_teams.contains(teamColors.WHITE)))this.active_teams.add(teamColors.WHITE);
                team=teamColors.WHITE;
                iterator++;
            }else if(iterator==4){
                players.put(p,teamColors.YELLOW);
                p.setDisplayName(getTeamColor(teamColors.YELLOW)+p.getName());
                if(!(this.active_teams.contains(teamColors.YELLOW)))this.active_teams.add(teamColors.YELLOW);
                team=teamColors.YELLOW;
                iterator++;
            }else {
                players.put(p,teamColors.GREEN);
                p.setDisplayName(getTeamColor(teamColors.GREEN)+p.getName());
                if(!(this.active_teams.contains(teamColors.GREEN)))this.active_teams.add(teamColors.GREEN);
                team=teamColors.GREEN;
                iterator=0;
            }

            p.sendMessage(text.f(String.format(lang.GAME_TEAM_ASSIGNED.get(), Colors.valueOf(team.toString().toUpperCase()).getColor()+team.toString().toUpperCase())));
        }

    }

    public HashMap<Player, teamColors> getPlayers() {
        return players;
    }
}
