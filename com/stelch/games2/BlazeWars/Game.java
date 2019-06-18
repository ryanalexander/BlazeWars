package com.stelch.games2.BlazeWars;

import com.stelch.games2.BlazeWars.Inventories.shop;
import com.stelch.games2.BlazeWars.Utils.*;
import com.stelch.games2.BlazeWars.varables.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.entity.Blaze;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class Game {

    private String title;

    private int min_players;
    private int max_players;

    private gameState gamestate = gameState.DISABLED;

    private ScoreboardManager scoreboard;

    private World map;

    private ArrayList<Entity> gameEntities=new ArrayList<>();

    private HashMap<Entity,click> EntityFunctions=new HashMap<>();

    private HashMap<Location,Block> blockChanges = new HashMap<>();

    public HashMap<Player, Spectator> spectators = new HashMap<>();

    private TeamManager teamManager;

    private int start_time = 5;

    private Main handler;

    private boolean allow_spectators=false;

    private boolean auto_start = true;

    private gameType gamemode;

    public Game(String title, gameType gamemode, int min_players, int max_players, Main handler, World map){
        // PLAYERS GAME SERVER
        teamManager=new TeamManager();
        this.scoreboard=new ScoreboardManager("&6&lBLAZE WARS");
        this.scoreboard.addBlank();
        this.scoreboard.addLine("&e&lPLAYERS");
        this.scoreboard.addLine(String.format("&a%s &7/ &a%s",Bukkit.getOnlinePlayers().size(),min_players));
        this.scoreboard.addBlank();
        this.scoreboard.addLine("&b&lGAME:");
        this.scoreboard.addLine("&a"+title.toUpperCase());
        this.scoreboard.addBlank();
        this.scoreboard.addLine("&d&lSERVER:");
        this.scoreboard.addLine("&a"+handler.getConfig().getString("server.name"));
        this.scoreboard.addBlank();
        this.title=title;
        this.gamemode=gamemode;
        this.min_players=min_players;
        this.max_players=max_players;
        this.handler=handler;
        this.map=map;
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

    public World getMap() { return this.map; }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public ScoreboardManager getScoreboard() { return scoreboard; }

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
    public void setFunctionEntity(Entity e, click c) { this.EntityFunctions.put(e,c); }

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

    public void doFinishGame() {
        teamColors winner = this.teamManager.getActive_teams().get(0);
        Bukkit.broadcastMessage(text.f(String.format("&eCongratulations to %s&e team! You won!",this.teamManager.getTeamColor(winner)+winner)));
        for(HashMap.Entry<Player,teamColors> ent : this.teamManager.getPlayers().entrySet()){
            if(ent.getValue().equals(winner)){
                Location loc = ent.getKey().getLocation();
                Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();

                fwm.setPower(2);
                fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());

                fw.setFireworkMeta(fwm);
                fw.detonate();

                for(int i = 0;i<24; i++){
                    Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
                    fw2.setFireworkMeta(fwm);
                }
            }
        }

        new BukkitRunnable(){
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()){
                    player.sendMessage(text.f("&aGame> &7The game has finished."));
                    player.kickPlayer("The game has finished");
                }
                Main.game.stop(GameReason.FINISHED);
                Bukkit.shutdown();
            }
        }.runTaskLater(handler,200);
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
                FileConfiguration config = handler.getConfig();
                Bukkit.broadcastMessage(text.f(String.format(lang.GAME_BEGIN_IN.get(),start_time)));
                for(Player player : Bukkit.getOnlinePlayers()){
                    player.playSound(player.getLocation(),Sound.BLOCK_NOTE_BLOCK_PLING,1,1);
                }
                if(start_time==0){
                    start_time=5;
                    setGamestate(gameState.IN_GAME);
                    Bukkit.getScheduler().cancelTasks(handler);

                    teamManager.assignTeams();

                    scoreboard.clear();
                    scoreboard.addBlank();
                    scoreboard.addBlank();

                    for(Map.Entry<Player, teamColors> teamPlayer : teamManager.getPlayers().entrySet()){
                        Player player = teamPlayer.getKey();
                        teamColors team = teamPlayer.getValue();
                        Double x = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.x"),"world",team)));
                        Double y = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.y"),"world",team)));
                        Double z = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.z"),"world",team)));
                        Float yaw = Float.parseFloat(config.getString(String.format(("maps.%s.spawn.%s.yaw"),"world",team)));
                        Float pitch = Float.parseFloat("0.0");
                        Location teamSpawn=new Location(player.getWorld(),x,y,z,yaw,pitch);

                        player.setHealth(20);
                        player.setVelocity(new Vector(0,0,0));
                        player.teleport(teamSpawn);
                        player.getInventory().clear();
                        player.setGameMode(GameMode.SURVIVAL);
                    }

                    for(teamColors team : teamManager.getActive_teams()){

                        teamManager.setScoreboardLine(team,scoreboard.addLine(teamManager.getTeamColor(team)+"&l"+team.toString().charAt(0)+"&r "+(capitalizeFirstLetter(team.toString())+": &a&l✓")));

                        Double x = Double.parseDouble(config.getString(String.format(("maps.%s.core.%s.x"),"world",team)));
                        Double y = Double.parseDouble(config.getString(String.format(("maps.%s.core.%s.y"),"world",team)));
                        Double z = Double.parseDouble(config.getString(String.format(("maps.%s.core.%s.z"),"world",team)));
                        if(x!=null&&y!=null&&z!=null){
                            Location core=new Location(Bukkit.getWorld("world"),x,y,z);
                            core.getBlock().setType(Material.NETHER_QUARTZ_ORE);
                            Main.game.getTeamManager().addCore(team,core.getBlock());
                            blockChanges.put(core.getBlock().getLocation(),core.getBlock());
                        }else {
                            Bukkit.broadcastMessage(String.format("&cMCT> &7Failed to load &e%s&7 missing &6%s check config and use MCT to set the island",team,"core"));
                        }


                        Double bx = Double.parseDouble(config.getString(String.format(("maps.%s.blaze.%s.x"),"world",team)));
                        Double by = Double.parseDouble(config.getString(String.format(("maps.%s.blaze.%s.y"),"world",team)));
                        Double bz = Double.parseDouble(config.getString(String.format(("maps.%s.blaze.%s.z"),"world",team)));
                        Float byaw = Float.parseFloat(config.getString(String.format(("maps.%s.blaze.%s.yaw"),"world",team)));
                        Float bpitch = Float.parseFloat("0.0");

                        if(bx!=null&&by!=null&&bz!=null){
                            Location blaze=new Location(Bukkit.getWorld("world"),bx,by,bz,byaw,bpitch);
                            Blaze b = (Blaze)Bukkit.getWorld("world").spawnEntity(blaze,EntityType.BLAZE);
                            b.setAI(false);
                            b.setCanPickupItems(false);
                            b.setCollidable(false);
                            b.setInvulnerable(true);
                            b.setSilent(true);
                            b.setGliding(false);
                            gameEntities.add(b);
                            teamManager.addBlaze(team,b);
                        }else {
                            Bukkit.broadcastMessage(String.format("&cMCT> &7Failed to load &e%s&7 missing &6%s check config and use MCT to set the island",team,"Blaze"));
                        }


                        Double vx = Double.parseDouble(config.getString(String.format(("maps.%s.shop.%s.x"),"world",team)));
                        Double vy = Double.parseDouble(config.getString(String.format(("maps.%s.shop.%s.y"),"world",team)));
                        Double vz = Double.parseDouble(config.getString(String.format(("maps.%s.shop.%s.z"),"world",team)));
                        Float vyaw = Float.parseFloat(config.getString(String.format(("maps.%s.shop.%s.yaw"),"world",team)));
                        Float vpitch = Float.parseFloat("0.0");

                        if(vx!=null&&vy!=null&&vz!=null){
                            Location villager=new Location(Bukkit.getWorld("world"),vx,vy,vz,vyaw,vpitch);
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
                        }else {
                            Bukkit.broadcastMessage(String.format("&cMCT> &7Failed to load &e%s&7 missing &6%s check config and use MCT to set the island",team,"Shop"));
                        }

                        Double fx = Double.parseDouble(config.getString(String.format(("maps.%s.forge.%s.x"),"world",team)));
                        Double fy = Double.parseDouble(config.getString(String.format(("maps.%s.forge.%s.y"),"world",team)));
                        Double fz = Double.parseDouble(config.getString(String.format(("maps.%s.forge.%s.z"),"world",team)));

                        if(fx!=null&&fy!=null&&fz!=null){
                            Location forge =new Location(Bukkit.getWorld("world"),fx,fy,fz);
                            Game.spawnner spawnner_iron = new spawnner(forge,new ItemStack(Material.IRON_INGOT),4);
                            Game.spawnner spawnner_gold = new spawnner(forge,new ItemStack(Material.GOLD_INGOT),3);

                            HashMap<Material, Game.spawnner> spawners = new HashMap<>();
                            teamManager.setForge_location(team,forge);
                            teamManager.setSpawner_level(team,0);
                            spawners.put(Material.IRON_INGOT,spawnner_iron);
                            spawners.put(Material.GOLD_INGOT,spawnner_gold);

                            teamManager.addSpawnners(team,spawners);
                        }else {
                            Bukkit.broadcastMessage(String.format("&cMCT> &7Failed to load &e%s&7 missing &6%s check config and use MCT to set the island",team,"Forge"));
                        }

                        Double cx = Double.parseDouble(config.getString(String.format(("maps.%s.chest.%s.x"),"world",team)));
                        Double cy = Double.parseDouble(config.getString(String.format(("maps.%s.chest.%s.y"),"world",team)));
                        Double cz = Double.parseDouble(config.getString(String.format(("maps.%s.chest.%s.z"),"world",team)));
                        String cd = (config.getString(String.format(("maps.%s.chest.%s.direction"),"world",team)));
                        if(cx!=null&&cy!=null&&cz!=null&&cd!=null){
                            Chest c = (Chest) PlaceBlockWithType(Bukkit.getWorld("world"),cx,cy,cz,Material.CHEST,BlockFace.valueOf(cd)).getState();
                            c.getInventory().clear();

                            blockChanges.put(c.getBlock().getLocation(),c.getBlock());
                            Main.game.getTeamManager().setTeam_chests(team,c.getBlock());
                        }else {
                            Bukkit.broadcastMessage(String.format("&cMCT> &7Failed to load &e%s&7 missing &6%s check config and use MCT to set the island",team,"Chest"));
                        }
                    }

                    scoreboard.addBlank();
                    scoreboard.addLine("&cwww.stelch.gg");

                    Double mfx = Double.parseDouble(config.getString(String.format(("maps.%s.forge.%s.x"),"world","MAP")));
                    Double mfy = Double.parseDouble(config.getString(String.format(("maps.%s.forge.%s.y"),"world","MAP")));
                    Double mfz = Double.parseDouble(config.getString(String.format(("maps.%s.forge.%s.z"),"world","MAP")));
                    Location mid_forge =new Location(Bukkit.getWorld("world"),mfx,mfy,mfz,0F,0F);
                    Forge f = new Forge(mid_forge.add(0,2,0),Material.BLAZE_ROD,0,true,"&c&lBlaze Rod");
                    Game.spawnner spawnner_mid = new spawnner(mid_forge,new ItemStack(Material.BLAZE_POWDER),0);

                }
                start_time--;
            }
        },1,25);

    }

    public void stop(GameReason reason) {
        this.gamestate=gameState.RESTARTING;
        Bukkit.broadcastMessage(text.f(((reason==GameReason.ADMINISTARTOR)?lang.GAME_STOPPED_ADMIN:lang.GAME_FINISHED)));
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


    private Block PlaceBlockWithType(World w,double x, double y, double z, Material BLK, BlockFace blockFace) {
        Block b = new Location(w,x,y,z).getBlock();
        b.setType(BLK);
        if (BLK == Material.CHEST) {
            BlockData blockData = b.getBlockData();
            ((Directional) blockData).setFacing(blockFace);
        }
        return b;
    }

        public static interface click {
        void run(Player param1Player);
    }

    private static Collection<Entity> getEntitiesAroundPoint(Location location, double radius) {
        return location.getWorld().getNearbyEntities(location, radius, radius, radius);
    }

    private static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }
}
