/*
 *
 * *
 *  *
 *  * © Stelch Games 2019, distribution is strictly prohibited
 *  *
 *  * Changes to this file must be documented on push.
 *  * Unauthorised changes to this file are prohibited.
 *  *
 *  * @author Ryan Wood
 *  * @since 14/7/2019
 *
 */

package com.stelch.games2.BlazeWars;

import com.stelch.games2.core.BukkitCore;
import com.stelch.games2.core.Game.Game;
import com.stelch.games2.core.Game.varables.gameState;
import com.stelch.games2.core.Game.varables.gameType;
import com.stelch.games2.core.InventoryUtils.Item;
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

      game = new Game("BlazeWars", gameType.DESTROY,4,8, this, Bukkit.getWorld("world"));

      BukkitCore.coreChatManager=true;

      PluginManager pm = Bukkit.getPluginManager();
      pm.registerEvents(new com.stelch.games2.BlazeWars.Utils.Forge(this),this);
      pm.registerEvents(new com.stelch.games2.BlazeWars.events.PotionEvent(),this);
      pm.registerEvents(new com.stelch.games2.BlazeWars.Inventories.shop(),this);
      pm.registerEvents(this,this);

      game.GameState(gameState.LOBBY);

  }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        for (Map.Entry<com.stelch.games2.core.InventoryUtils.Item, Item.click> is : Actions.entrySet()) {
            if(is.getKey().spigot().equals(e.getCurrentItem())){
                if(is.getValue()!=null)
                    is.getValue().run((Player)e.getWhoClicked());
                e.setCancelled(true);
                return;
            }
        }
    }
}
