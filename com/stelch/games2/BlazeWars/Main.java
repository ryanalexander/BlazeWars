package com.stelch.games2.BlazeWars;

import com.stelch.games2.BlazeWars.Inventories.Item;
import com.stelch.games2.BlazeWars.commands.game;
import com.stelch.games2.BlazeWars.varables.gameState;
import com.stelch.games2.BlazeWars.varables.gameType;
import com.stelch.games2.core.BukkitCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Main extends JavaPlugin implements Listener {

  public static Game game;

  public static HashMap<Item,Item.click> Actions = new HashMap<>();
  
  public void onEnable() {

      getConfig().options().copyDefaults(true);
      saveConfig();

      game = new Game("BlazeWars", gameType.DESTROY,2,8, this,Bukkit.getWorld("world"));

      BukkitCore.coreChatManager=true;

      game.setAllow_spectators(true);

      PluginManager pm = Bukkit.getPluginManager();
      pm.registerEvents(new com.stelch.games2.BlazeWars.Utils.Forge(this),this);
      pm.registerEvents(new com.stelch.games2.BlazeWars.events.playerJoin(this),this);
      pm.registerEvents(new com.stelch.games2.BlazeWars.events.blockPlace(),this);
      pm.registerEvents(new com.stelch.games2.BlazeWars.events.AsyncChatEvent(),this);
      pm.registerEvents(new com.stelch.games2.BlazeWars.events.playerDeathEvent(this),this);
      pm.registerEvents(new com.stelch.games2.BlazeWars.events.CommandPreProcessEvent(),this);
      pm.registerEvents(new com.stelch.games2.BlazeWars.events.EntityInteract(),this);
      pm.registerEvents(new com.stelch.games2.BlazeWars.events.PlayerMoveEvent(),this);
      pm.registerEvents(new com.stelch.games2.BlazeWars.Inventories.shop(),this);
      pm.registerEvents(this,this);

      game.setGamestate(gameState.LOBBY);

      getCommand("mct").setExecutor(new com.stelch.games2.BlazeWars.commands.mct(this));
      getCommand("game").setExecutor(new game(this));

  }
  
  @EventHandler
  public void food(FoodLevelChangeEvent e) { e.setCancelled(true); }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        for (Map.Entry<Item, Item.click> is : Actions.entrySet()) {
            if(is.getKey().spigot().equals(e.getCurrentItem())){
                if(is.getValue()!=null)
                    is.getValue().run((Player)e.getWhoClicked());
                e.setCancelled(true);
                return;
            }
        }
    }
}
