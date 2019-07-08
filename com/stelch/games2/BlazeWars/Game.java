package com.stelch.games2.BlazeWars;

import com.stelch.games2.BlazeWars.Inventories.shop;
import com.stelch.games2.BlazeWars.Utils.*;
import com.stelch.games2.BlazeWars.varables.*;
import com.stelch.games2.core.API;
import com.stelch.games2.core.BukkitCore;
import com.stelch.games2.core.Utils.Text;
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
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

import static com.stelch.games2.core.API.setGame;
import static com.stelch.games2.core.API.setState;
import static org.bukkit.Material.*;

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

    public HashMap<Player,ArmorStand> invis_players = new HashMap<>();

    private TeamManager teamManager;

    private int start_time = 5;

    private Main handler;

    private boolean allow_spectators=false;

    private boolean auto_start = true;

    private gameType gamemode;

    public Game(String title, gameType gamemode, int min_players, int max_players, Main handler, World map){
        // PLAYERS GAME SERVER
        teamManager=new TeamManager();
        this.title=title;
        this.gamemode=gamemode;
        this.min_players=min_players;
        this.max_players=max_players;
        this.handler=handler;
        this.map=map;
        buildDefaultScoreboard();
        API.setGame("Blaze Wars");
        setState("LOBBY");
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
    public void setMap(World map) { this.map = map; }

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
    public void removeGameEntity(Entity e) {
        this.gameEntities.remove(e);
        e.remove();
    }
    public ArrayList<Entity> getGameEntities() {return this.gameEntities;}

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
        Bukkit.broadcastMessage(Text.format(String.format("&eCongratulations to %s&e team! You won!",this.teamManager.getTeamColor(winner)+winner)));
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
                    player.sendMessage(Text.format("&aGame> &7The game has finished."));
                    player.kickPlayer("[GAMESTATE] The game has finished");
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
        setState("STARTING");
        setGamestate(gameState.STARTING);

        // Start countdown
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.handler, new Runnable() {
            @Override
            public void run() {
                if(getGamestate()!=gameState.STARTING){
                    Bukkit.getScheduler().cancelTasks(handler);
                }
                FileConfiguration config = handler.getConfig();
                Bukkit.broadcastMessage(Text.format(String.format(lang.GAME_BEGIN_IN.get(),start_time)));
                for(Player player : Bukkit.getOnlinePlayers()){
                    player.playSound(player.getLocation(),Sound.BLOCK_NOTE_BLOCK_PLING,1,1);
                }
                if(start_time==0){
                    start_time=5;
                    setGamestate(gameState.IN_GAME);
                    setState("IN-GAME");
                    Bukkit.getScheduler().cancelTasks(handler);

                    teamManager.assignTeams();

                    scoreboard.clear();
                    scoreboard.addBlank();
                    scoreboard.addBlank();
                    scoreboard.addBlank();
                    scoreboard.addBlank();

                    scoreboard.editLine(19," ");
                    scoreboard.editLine(18,"Loading.");

                    new BukkitRunnable(){
                        /* Main game loop */
                        forgeType[] upgrades = new forgeType[]{forgeType.BLAZE_ROD,forgeType.BLAZE_POWDER,forgeType.BLAZE_ROD,forgeType.BLAZE_POWDER,forgeType.BLAZE_ROD,forgeType.BLAZE_POWDER,forgeType.BLAZE_POWDER};
                        int forge_upgrade_level = 0;
                        int forge_upgrade_time = 30;
                        @Override
                        public void run() {
                            if(forge_upgrade_level>=upgrades.length){cancel();}
                            if(forge_upgrade_time<=0){
                                forge_upgrade_time=30;
                                forge_upgrade_level++;
                            }else {
                                forge_upgrade_time--;
                            }
                            scoreboard.editLine(18,upgrades[forge_upgrade_level]+" upgrades in");
                            scoreboard.editLine(17,"&a"+forge_upgrade_time+" seconds");

                        }
                    }.runTaskTimer(handler,0L,20L);

                    for(Entity e : (Main.game.getMap()).getEntities()){
                        if(!(e.getType().equals(EntityType.PLAYER)))
                            e.remove();
                    }

                    for(Map.Entry<Player, teamColors> teamPlayer : teamManager.getPlayers().entrySet()){
                        Player player = teamPlayer.getKey();
                        teamColors team = teamPlayer.getValue();
                        Double x = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.x"),Main.game.getMap().getName(),team)));
                        Double y = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.y"),Main.game.getMap().getName(),team)));
                        Double z = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.z"),Main.game.getMap().getName(),team)));
                        Float yaw = Float.parseFloat(config.getString(String.format(("maps.%s.spawn.%s.yaw"),Main.game.getMap().getName(),team)));
                        Float pitch = Float.parseFloat("0.0");
                        Location teamSpawn=new Location(player.getWorld(),x,y,z,yaw,pitch);
                        player.getInventory().clear();

                        ItemStack armor_helmet = new ItemStack(LEATHER_HELMET);
                        ItemStack armor_chestplate = new ItemStack(LEATHER_CHESTPLATE);
                        LeatherArmorMeta armorHelmetItemMeta = (LeatherArmorMeta) armor_helmet.getItemMeta();
                        LeatherArmorMeta armorChestplateItemMeta = (LeatherArmorMeta) armor_chestplate.getItemMeta();
                        armorHelmetItemMeta.setColor(Main.game.getTeamManager().getTeam(player).getColor());
                        armorChestplateItemMeta.setColor(Main.game.getTeamManager().getTeam(player).getColor());
                        armor_helmet.setItemMeta(armorHelmetItemMeta);
                        armor_chestplate.setItemMeta(armorChestplateItemMeta);
                        player.getInventory().setArmorContents(new ItemStack[]{null,null,armor_chestplate,armor_helmet});
                        player.setHealth(20);
                        player.setVelocity(new Vector(0,0,0));
                        player.teleport(teamSpawn);
                        player.setGameMode(GameMode.SURVIVAL);
                    }

                    for(teamColors team : teamManager.getActive_teams()){

                        teamManager.setScoreboardLine(team,scoreboard.addLine(teamManager.getTeamColor(team)+"&l"+team.toString().charAt(0)+"&r "+(capitalizeFirstLetter(team.toString())+": &a&l✓")));

                        Double x = Double.parseDouble(config.getString(String.format(("maps.%s.core.%s.x"),Main.game.getMap().getName(),team)));
                        Double y = Double.parseDouble(config.getString(String.format(("maps.%s.core.%s.y"),Main.game.getMap().getName(),team)));
                        Double z = Double.parseDouble(config.getString(String.format(("maps.%s.core.%s.z"),Main.game.getMap().getName(),team)));
                        if(x!=null&&y!=null&&z!=null){
                            Location core=new Location(Main.game.getMap(),x,y,z);
                            core.getBlock().setType(Material.NETHER_QUARTZ_ORE);
                            Main.game.getTeamManager().addCore(team,core.getBlock());
                            blockChanges.put(core.getBlock().getLocation(),core.getBlock());
                        }else {
                            Bukkit.broadcastMessage(Text.format(String.format("&cMCT> &7Failed to load &e%s&7 missing &6%s check config and use MCT to set the island",team,"core")));
                        }


                        Double bx = Double.parseDouble(config.getString(String.format(("maps.%s.blaze.%s.x"),Main.game.getMap().getName(),team)));
                        Double by = Double.parseDouble(config.getString(String.format(("maps.%s.blaze.%s.y"),Main.game.getMap().getName(),team)));
                        Double bz = Double.parseDouble(config.getString(String.format(("maps.%s.blaze.%s.z"),Main.game.getMap().getName(),team)));
                        Float byaw = Float.parseFloat(config.getString(String.format(("maps.%s.blaze.%s.yaw"),Main.game.getMap().getName(),team)));
                        Float bpitch = Float.parseFloat("0.0");

                        if(bx!=null&&by!=null&&bz!=null){
                            Location blaze=new Location(Main.game.getMap(),bx,by,bz,byaw,bpitch);
                            Blaze b = (Blaze)Main.game.getMap().spawnEntity(blaze,EntityType.BLAZE);
                            b.setAI(false);
                            b.setCanPickupItems(false);
                            b.setCollidable(false);
                            b.setInvulnerable(true);
                            b.setSilent(true);
                            b.setGliding(false);
                            gameEntities.add(b);
                            teamManager.addBlaze(team,b);
                            teamManager.addBlazeCooldown(b);
                        }else {
                            Bukkit.broadcastMessage(Text.format(String.format("&cMCT> &7Failed to load &e%s&7 missing &6%s check config and use MCT to set the island",team,"blaze")));
                        }


                        Double vx = Double.parseDouble(config.getString(String.format(("maps.%s.shop.%s.x"),Main.game.getMap().getName(),team)));
                        Double vy = Double.parseDouble(config.getString(String.format(("maps.%s.shop.%s.y"),Main.game.getMap().getName(),team)));
                        Double vz = Double.parseDouble(config.getString(String.format(("maps.%s.shop.%s.z"),Main.game.getMap().getName(),team)));
                        Float vyaw = Float.parseFloat(config.getString(String.format(("maps.%s.shop.%s.yaw"),Main.game.getMap().getName(),team)));
                        Float vpitch = Float.parseFloat("0.0");

                        if(vx!=null&&vy!=null&&vz!=null){
                            Location villager=new Location(Main.game.getMap(),vx,vy,vz,vyaw,vpitch);
                            Skeleton v = (Skeleton)Main.game.getMap().spawnEntity(villager,EntityType.SKELETON);
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
                            Bukkit.broadcastMessage(Text.format(String.format("&cMCT> &7Failed to load &e%s&7 missing &6%s check config and use MCT to set the island",team,"shop")));
                        }

                        Double fx = Double.parseDouble(config.getString(String.format(("maps.%s.forge.%s.x"),Main.game.getMap().getName(),team)));
                        Double fy = Double.parseDouble(config.getString(String.format(("maps.%s.forge.%s.y"),Main.game.getMap().getName(),team)));
                        Double fz = Double.parseDouble(config.getString(String.format(("maps.%s.forge.%s.z"),Main.game.getMap().getName(),team)));

                        if(fx!=null&&fy!=null&&fz!=null){
                            Location forge =new Location(Main.game.getMap(),fx,fy,fz);
                            Game.spawnner spawnner_iron = new spawnner(forge,new ItemStack(Material.IRON_INGOT),8,24);
                            Game.spawnner spawnner_gold = new spawnner(forge,new ItemStack(Material.GOLD_INGOT),6,10);

                            HashMap<Material, Game.spawnner> spawners = new HashMap<>();
                            teamManager.setForge_location(team,forge);
                            teamManager.setSpawner_level(team,0);
                            spawners.put(Material.IRON_INGOT,spawnner_iron);
                            spawners.put(Material.GOLD_INGOT,spawnner_gold);

                            teamManager.addSpawnners(team,spawners);
                        }else {
                            Bukkit.broadcastMessage(Text.format(String.format("&cMCT> &7Failed to load &e%s&7 missing &6%s check config and use MCT to set the island",team,"forge")));
                        }

                        Double cx = Double.parseDouble(config.getString(String.format(("maps.%s.chest.%s.x"),Main.game.getMap().getName(),team)));
                        Double cy = Double.parseDouble(config.getString(String.format(("maps.%s.chest.%s.y"),Main.game.getMap().getName(),team)));
                        Double cz = Double.parseDouble(config.getString(String.format(("maps.%s.chest.%s.z"),Main.game.getMap().getName(),team)));
                        String cd = (config.getString(String.format(("maps.%s.chest.%s.direction"),Main.game.getMap().getName(),team)));
                        if(cx!=null&&cy!=null&&cz!=null&&cd!=null){
                            Chest c = (Chest) PlaceBlockWithType(Main.game.getMap(),cx,cy,cz,Material.CHEST,BlockFace.valueOf(cd)).getState();
                            c.getInventory().clear();

                            blockChanges.put(c.getBlock().getLocation(),c.getBlock());
                            Main.game.getTeamManager().setTeam_chests(team,c.getBlock());
                        }else {
                            Bukkit.broadcastMessage(Text.format(String.format("&cMCT> &7Failed to load &e%s&7 missing &6%s check config and use MCT to set the island",team,"chest")));
                        }
                    }

                    List<String> BLAZE_POWDER = config.getStringList(String.format("maps.%s.BLAZE_POWDER",Main.game.getMap().getName()));
                    for(String s : BLAZE_POWDER){
                        String[] args = s.split(":");
                        Location location = new Location((Main.game.getMap()),Double.parseDouble(args[1]),Double.parseDouble(args[2]),Double.parseDouble(args[3]));
                        Forge f = new Forge(location.add(0,1,0), NETHER_BRICK_FENCE,0,false,"&c&lBlaze Powder");
                        Game.spawnner spawnner_mid = new spawnner(location,new ItemStack(Material.BLAZE_POWDER),0,4);
                        f.setSpawner(spawnner_mid);
                    }


                    List<String> MID_FORGE = config.getStringList(String.format("maps.%s.MID_FORGE",Main.game.getMap().getName()));
                    for(String s : MID_FORGE){
                        String[] args = s.split(":");
                        Location location = new Location((Main.game.getMap()),Double.parseDouble(args[1]),Double.parseDouble(args[2]),Double.parseDouble(args[3]));
                        Forge f = new Forge(location.add(0,1,0), GLOWSTONE,0,false,"&c&lBlaze Rod");
                        Game.spawnner spawnner_mid = new spawnner(location,new ItemStack(BLAZE_ROD),0,4);
                        f.setSpawner(spawnner_mid);
                    }

                    scoreboard.addBlank();
                    scoreboard.addLine("&cwww.stelch.gg                      ");

                    BukkitCore.coreChatManager=false;

                    /* Remove waiting lobby */
                    /*
                    double startx=Double.parseDouble(config.getString(String.format(("maps.%s.lobby.%s.x"),"lobby","pos1"))), starty=Double.parseDouble(config.getString(String.format(("maps.%s.lobby.%s.y"),"lobby","pos1"))), startz=Double.parseDouble(config.getString(String.format(("maps.%s.lobby.%s.z"),Main.game.getMap().getName(),"pos1")));
                    double endx=Double.parseDouble(config.getString(String.format(("maps.%s.lobby.%s.x"),"lobby","pos1"))), endy=Double.parseDouble(config.getString(String.format(("maps.%s.lobby.%s.y"),"lobby","pos1"))), endz=Double.parseDouble(config.getString(String.format(("maps.%s.lobby.%s.z"),Main.game.getMap().getName(),"pos1")));
                    for (double x = startx; x < endx; x++) {
                        for (double y = starty; y < endy; y++) {
                            for (double z = startz; z < endz; z++) {
                                Block block = new Location(Main.game.getMap().getName(),x, y, z).getBlock();
                                block.setType(Material.AIR);
                            }
                        }
                    }
                    */
                }
                start_time--;
            }
        },1,25);

    }

    public void stop(GameReason reason) {
        this.gamestate=gameState.RESTARTING;
        this.scoreboard.clear();
        buildDefaultScoreboard();
        setState("RESTARTING");
        Bukkit.broadcastMessage(Text.format(((reason==GameReason.ADMINISTARTOR)?lang.GAME_STOPPED_ADMIN.get():lang.GAME_FINISHED.get())));
        Bukkit.getScheduler().cancelTasks(handler);
        for(Entity e : this.gameEntities){
            e.remove();
        }
        for(Entity e : (Main.game.getMap()).getEntities()){
            if(!(e.getType().equals(EntityType.PLAYER)))
                e.remove();
        }
        for(Map.Entry<Location,Block> b : this.blockChanges.entrySet()){
            b.getKey().getBlock().setType(Material.AIR);
        }
        if(reason.equals(GameReason.ADMINISTARTOR)){
            this.setGamestate(gameState.LOBBY);
            setState("LOBBY");
        }
        BukkitCore.coreChatManager=true;

    }

    public static class spawnner {
        private boolean stopped = false;

        private Location location;

        private long nextDrop=0;

        private Long[] levels = new Long[]{1300L, 1100L, 1000L, 800L, 700L, 400L, 200L, 120L, 80L, 40L};

        private Material type;

        private int level = 0;
        public spawnner(final Location l, final ItemStack m,int level,int max_holding) {
            System.out.println(String.format("[SPAWNER] Created %s spawner at x: %s y: %s z: %s with level: %s",m.getType().name(),l.getX(),l.getY(),l.getZ(),level));
            this.location=l;
            this.level=level;
            this.type=m.getType();
            (new BukkitRunnable() {
                public void run() {
                    nextDrop--;
                    if (Game.spawnner.this.stopped || Main.game.getGamestate() != gameState.IN_GAME)
                        cancel();
                    if(getEntitiesAroundPoint(l,max_holding,m)>=10)
                        return;
                    if(nextDrop<=0){
                        nextDrop=((levels[spawnner.this.level])/20);
                        l.getWorld().dropItem(l, m);
                    }
                }
            }).runTaskTimer(Main.game.handler, 0L, 25L);
        }

        public long getNextDrop() {
            return nextDrop;
        }

        public void setLevel(int level) {
            System.out.println(String.format("[SPAWNER] Updated %s spawner at x: %s y: %s z: %s with level: %s",type.name(),location.getX(),location.getY(),location.getZ(),level));
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

    private void buildDefaultScoreboard() {
        this.scoreboard=new ScoreboardManager("&6&lBLAZE WARS");
        this.scoreboard.addBlank();
        this.scoreboard.addLine("&e&lMAP");
        this.scoreboard.addLine(this.map.getName());
        this.scoreboard.addBlank();
        this.scoreboard.addLine("&e&lPLAYERS");
        this.scoreboard.addLine(String.format("&a:player_count: &7/ &a%s",min_players));
        this.scoreboard.addBlank();
        this.scoreboard.addLine("&b&lGAME:");
        this.scoreboard.addLine("&a"+title.toUpperCase());
        this.scoreboard.addBlank();
        this.scoreboard.addLine("&d&lSERVER:");
        this.scoreboard.addLine("&a"+handler.getConfig().getString("server.name"));
        this.scoreboard.addBlank();
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

    private static Integer getEntitiesAroundPoint(Location location, double radius, ItemStack type) {
        int count = 0;
        for(Entity e : location.getWorld().getNearbyEntities(location, radius, radius, radius)){
            if(e instanceof Item){
                count++;
            }
        }
        return count;
    }

    private static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }
}
