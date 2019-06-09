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

      game = new Game("BlazeWars", gameType.DESTROY,1,8, this);

      game.setAllow_spectators(true);

      PluginManager pm = Bukkit.getPluginManager();
      pm.registerEvents(new com.stelch.games2.BlazeWars.events.playerJoin(this),this);

      game.setGamestate(gameState.LOBBY);

      getCommand("mct").setExecutor(new com.stelch.games2.BlazeWars.commands.mct());

  }
  
  @EventHandler
  public void food(FoodLevelChangeEvent e) { e.setCancelled(true); }
}
