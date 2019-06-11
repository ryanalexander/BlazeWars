package com.stelch.games2.BlazeWars;

import com.stelch.games2.BlazeWars.Inventories.islandUpgrades;
import com.stelch.games2.BlazeWars.Inventories.shop;
import com.stelch.games2.BlazeWars.Utils.TeamManager;
import com.stelch.games2.BlazeWars.Utils.text;
import com.stelch.games2.BlazeWars.varables.gameState;
import com.stelch.games2.BlazeWars.varables.gameType;
import com.stelch.games2.BlazeWars.varables.menuSource;
import com.stelch.games2.BlazeWars.varables.teamColors;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class Game {

    private String title;

    private int min_players;
    private int max_players;

    private gameState gamestate = gameState.DISABLED;

    private ArrayList<Entity> gameEntities=new ArrayList<>();

    private HashMap<Entity,click> EntityFunctions=new HashMap<>();

    private HashMap<Location,Block> blockChanges = new HashMap<>();

    private TeamManager teamManager;

    private int start_time = 5;

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

    public void doBlockUpdate(Location loc, Block b){
        if(!(this.blockChanges.containsKey(loc))){
            this.blockChanges.put(loc,b);
        }
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

    public click getEntityFunction(Entity e) { return this.EntityFunctions.get(e); }

    public TeamManager getTeamManager() {
        return teamManager;
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

    public boolean isGameEntity(Entity e) { return this.gameEntities.contains(e); }

    public boolean isFunctionEntity(Entity e) { return this.EntityFunctions.containsKey(e); }

    public gameType getGamemode() {
        return gamemode;
    }

    public boolean canBreakBlock(Block b) {
        if(this.blockChanges.containsKey(b.getLocation())){
            return true;
        }else {
            return false;
        }
    }


    public boolean canStart() {
        boolean canstart = true;
        if(Bukkit.getOnlinePlayers().size()<this.min_players){canstart=false;}
        if(!this.auto_start){canstart=false;}

        return canstart;
    }

    public void start() {
        setGamestate(gameState.STARTING);

        // Start countdown
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.handler, new Runnable() {
            @Override
            public void run() {
                if(getGamestate()!=gameState.STARTING){
                    Bukkit.getScheduler().cancelTasks(handler);
                }
                Bukkit.broadcastMessage(text.f(String.format("&aGAME> &fThe game will begin in %s",start_time)));
                if(start_time==0){
                    start_time=5;
                    setGamestate(gameState.IN_GAME);
                    Bukkit.getScheduler().cancelTasks(handler);

                    teamManager.assignTeams();

                    for(Map.Entry<Player, teamColors> teamPlayer : teamManager.getPlayers().entrySet()){
                        Player player = teamPlayer.getKey();
                        teamColors team = teamPlayer.getValue();
                        FileConfiguration config = handler.getConfig();
                        Double x = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.x"),"world",team)));
                        Double y = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.y"),"world",team)));
                        Double z = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.z"),"world",team)));
                        Float yaw = Float.parseFloat(config.getString(String.format(("maps.%s.blaze.%s.yaw"),"world",team)));
                        Float pitch = Float.parseFloat("0.0");
                        Location teamSpawn=new Location(player.getWorld(),x,y,z,yaw,pitch);

                        player.setHealth(20);
                        player.setVelocity(new Vector(0,0,0));
                        player.teleport(teamSpawn);
                        player.getInventory().clear();
                        player.setGameMode(GameMode.SURVIVAL);
                    }

                    for(teamColors team : teamColors.values()){
                        FileConfiguration config = handler.getConfig();
                        Double x = Double.parseDouble(config.getString(String.format(("maps.%s.core.%s.x"),"world",team)));
                        Double y = Double.parseDouble(config.getString(String.format(("maps.%s.core.%s.y"),"world",team)));
                        Double z = Double.parseDouble(config.getString(String.format(("maps.%s.core.%s.z"),"world",team)));
                        Location core=new Location(Bukkit.getWorld("world"),x,y,z);
                        Double bx = Double.parseDouble(config.getString(String.format(("maps.%s.blaze.%s.x"),"world",team)));
                        Double by = Double.parseDouble(config.getString(String.format(("maps.%s.blaze.%s.y"),"world",team)));
                        Double bz = Double.parseDouble(config.getString(String.format(("maps.%s.blaze.%s.z"),"world",team)));
                        Float byaw = Float.parseFloat(config.getString(String.format(("maps.%s.blaze.%s.yaw"),"world",team)));
                        Float bpitch = Float.parseFloat("0.0");
                        Location blaze=new Location(Bukkit.getWorld("world"),bx,by,bz,byaw,bpitch);
                        Double vx = Double.parseDouble(config.getString(String.format(("maps.%s.shop.%s.x"),"world",team)));
                        Double vy = Double.parseDouble(config.getString(String.format(("maps.%s.shop.%s.y"),"world",team)));
                        Double vz = Double.parseDouble(config.getString(String.format(("maps.%s.shop.%s.z"),"world",team)));
                        Float vyaw = Float.parseFloat(config.getString(String.format(("maps.%s.shop.%s.yaw"),"world",team)));
                        Float vpitch = Float.parseFloat("0.0");
                        Location villager=new Location(Bukkit.getWorld("world"),vx,vy,vz,vyaw,vpitch);

                        Double fx = Double.parseDouble(config.getString(String.format(("maps.%s.forge.%s.x"),"world",team)));
                        Double fy = Double.parseDouble(config.getString(String.format(("maps.%s.forge.%s.y"),"world",team)));
                        Double fz = Double.parseDouble(config.getString(String.format(("maps.%s.forge.%s.z"),"world",team)));
                        Location forge =new Location(Bukkit.getWorld("world"),fx,fy,fz);

                        Double cx = Double.parseDouble(config.getString(String.format(("maps.%s.chest.%s.x"),"world",team)));
                        Double cy = Double.parseDouble(config.getString(String.format(("maps.%s.chest.%s.y"),"world",team)));
                        Double cz = Double.parseDouble(config.getString(String.format(("maps.%s.chest.%s.z"),"world",team)));
                        Location chest =new Location(Bukkit.getWorld("world"),cx,cy,cz,0F,180F);

                        Double mfx = Double.parseDouble(config.getString(String.format(("maps.%s.forge.%s.x"),"world","map")));
                        Double mfy = Double.parseDouble(config.getString(String.format(("maps.%s.forge.%s.y"),"world","map")));
                        Double mfz = Double.parseDouble(config.getString(String.format(("maps.%s.forge.%s.z"),"world","map")));
                        Location mid_forge =new Location(Bukkit.getWorld("world"),mfx,mfy,mfz,0F,0F);

                        core.getBlock().setType(Material.NETHER_QUARTZ_ORE);
                        chest.getBlock().setType(Material.CHEST);
                        Main.game.getTeamManager().addCore(team,core.getBlock());

                        Chest c = (Chest) chest.getBlock().getState();
                        c.getInventory().clear();

                        blockChanges.put(core.getBlock().getLocation(),core.getBlock());
                        blockChanges.put(core.getBlock().getLocation(),chest.getBlock());
                        Main.game.getTeamManager().setTeam_chests(team,chest.getBlock());

                        Blaze b = (Blaze)Bukkit.getWorld("world").spawnEntity(blaze,EntityType.BLAZE);
                        b.setAI(false);
                        b.setCanPickupItems(false);
                        b.setCollidable(false);
                        b.setInvulnerable(true);
                        b.setSilent(true);
                        b.setGliding(false);
                        gameEntities.add(b);
                        teamManager.addBlaze(team,b);

                        Skeleton v = (Skeleton)Bukkit.getWorld("world").spawnEntity(villager,EntityType.SKELETON);
                        v.setAI(false);
                        v.setCanPickupItems(false);
                        v.setCollidable(false);
                        v.setInvulnerable(true);
                        v.setSilent(true);
                        v.setGliding(false);
                        EntityFunctions.put(v, new click() {
                            @Override
                            public void run(Player player) {
                                if(player!=null){
                                    player.openInventory((new shop()).getShop(player));
                                }
                            }
                        });
                        gameEntities.add(v);

                        Game.spawnner spawnner_iron = new spawnner(forge,new ItemStack(Material.IRON_INGOT),4);
                        Game.spawnner spawnner_gold = new spawnner(forge,new ItemStack(Material.GOLD_INGOT),3);
                        Game.spawnner spawnner_mid = new spawnner(mid_forge,new ItemStack(Material.BLAZE_POWDER),0);

                        HashMap<Material, Game.spawnner> spawners = new HashMap<>();
                        teamManager.setForge_location(team,forge);
                        teamManager.setSpawner_level(team,0);
                        spawners.put(Material.IRON_INGOT,spawnner_iron);
                        spawners.put(Material.GOLD_INGOT,spawnner_gold);

                        teamManager.addSpawnners(team,spawners);


                    }

                }
                start_time--;
            }
        },1,25);

    }

    public void stop() {
        this.gamestate=gameState.RESTARTING;
        Bukkit.broadcastMessage(text.f("&aGame> &7This game has been &c&lSTOPPED&7 by an Administrator."));
        Bukkit.getScheduler().cancelTasks(handler);
        for(Entity e : this.gameEntities){
            e.remove();
        }
        for(Entity e : Bukkit.getServer().getWorld("world").getEntities()){
            if(!(e.getType().equals(EntityType.PLAYER)))
                e.remove();
        }
        for(Map.Entry<Location,Block> b : this.blockChanges.entrySet()){
            b.getKey().getBlock().setType(Material.AIR);
        }
        this.gamestate=gameState.LOBBY;
    }

    public static class spawnner {
        private boolean stopped = false;

        private Long[] levels = new Long[]{1200L,600L,300L,160L,80L,40L,20L,10L,5L};

        private int level = 0;
        public spawnner(final Location l, final ItemStack m,int level) {
            this.level=level;
            (new BukkitRunnable() {
                public void run() {
                    if (Game.spawnner.this.stopped || Main.game.getGamestate() != gameState.IN_GAME)
                        cancel();
                    l.getWorld().dropItem(l, m);
                }
            }).runTaskTimer(Main.game.handler, 0L, this.levels[this.level]);
        }

        public void setLevel(int level) {
            this.level = level;
        }
        public long getSpeed() {
            return levels[level];
        }

        public int getLevel() {
            return level;
        }

        public void stop() { this.stopped = true; }
    }

    public static interface click {
        void run(Player param1Player);
    }

    private static Collection<Entity> getEntitiesAroundPoint(Location location, double radius) {
        return location.getWorld().getNearbyEntities(location, radius, radius, radius);
    }
}
