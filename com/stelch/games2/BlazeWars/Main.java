package com.stelch.games2.BlazeWars;

import com.stelch.games2.BlazeWars.varables.gameState;
import com.stelch.games2.BlazeWars.varables.gameType;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

  public static Game game;
  
  public void onEnable() {

      getConfig().options().copyDefaults(true);
      saveConfig();

      game = new Game("BlazeWars", gameType.DESTROY,2,8, this);

      game.setAllow_spectators(true);

      PluginManager pm = Bukkit.getPluginManager();
      pm.registerEvents(new com.stelch.games2.BlazeWars.events.playerJoin(this),this);
      pm.registerEvents(new com.stelch.games2.BlazeWars.events.blockPlace(),this);
      pm.registerEvents(new com.stelch.games2.BlazeWars.events.AsyncChatEvent(),this);
      pm.registerEvents(new com.stelch.games2.BlazeWars.events.playerDeathEvent(this),this);
      pm.registerEvents(new com.stelch.games2.BlazeWars.events.CommandPreProcessEvent(),this);
      pm.registerEvents(new com.stelch.games2.BlazeWars.events.EntityInteract(),this);

      game.setGamestate(gameState.LOBBY);

      getCommand("mct").setExecutor(new com.stelch.games2.BlazeWars.commands.mct(this));
      getCommand("admin").setExecutor(new com.stelch.games2.BlazeWars.commands.admin(this));

  }
  
  @EventHandler
  public void food(FoodLevelChangeEvent e) { e.setCancelled(true); }
}
