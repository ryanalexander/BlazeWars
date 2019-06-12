package com.stelch.games2.BlazeWars.Utils;

import com.stelch.games2.BlazeWars.Game;
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

    private HashMap<teamColors, Block> cores = new HashMap<>();
    private ArrayList<teamColors> cantRespawn = new ArrayList<>();

    private HashMap<teamColors, Location> forge_location = new HashMap<>();
    private HashMap<teamColors, HashMap<Material,Game.spawnner>> spawners = new HashMap<>();
    private HashMap<teamColors, Integer> spawner_level = new HashMap<>();

    private HashMap<teamColors, Block> team_chests = new HashMap<>();

    private HashMap<teamColors, Entity> team_blaze = new HashMap<>();

    public enum Colors {
        RED("&c"), GREEN("&a"), BLUE("&b"), PINK("&d");
        private String color;
        public String getColor() {return this.color;}
        private Colors(String color){this.color = color;}
    }

    public teamColors getTeam(Player p){
        return players.get(p);
    }

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
                players.put(p,teamColors.blue);
                p.setDisplayName(getTeamColor(teamColors.blue)+p.getName());
                team=teamColors.blue;
                iterator=1;
            }else if (iterator==1){
                players.put(p,teamColors.green);
                p.setDisplayName(getTeamColor(teamColors.green)+p.getName());
                team=teamColors.green;
                iterator=1;
            }else if (iterator==2){
                players.put(p,teamColors.yellow);
                p.setDisplayName(getTeamColor(teamColors.yellow)+p.getName());
                team=teamColors.yellow;
                iterator=1;
            }else {
                players.put(p,teamColors.pink);
                p.setDisplayName(getTeamColor(teamColors.pink)+p.getName());
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
