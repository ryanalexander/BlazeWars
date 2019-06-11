package com.stelch.games2.BlazeWars.events;

import com.stelch.games2.BlazeWars.Game;
import com.stelch.games2.BlazeWars.Inventories.Item;
import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.Utils.text;
import com.stelch.games2.BlazeWars.varables.gameState;
import com.stelch.games2.BlazeWars.varables.teamColors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EntityInteract implements Listener {

    @EventHandler
    public void EntityInteractEvent(PlayerInteractEntityEvent e){
        if(Main.game.isFunctionEntity(e.getRightClicked())){
            e.setCancelled(true);
            Game.click action = Main.game.getEntityFunction(e.getRightClicked());
            if(action!=null&&e.getPlayer()!=null)
                action.run(e.getPlayer());
        }
    }

    @EventHandler
    public void chestOpen(InventoryOpenEvent e){
        if (e.getInventory().getHolder()==null) {return;}
        if(e.getInventory().getType().equals(InventoryType.CHEST)){
            if(Main.game.getGamestate()== gameState.IN_GAME&&e.getInventory().getLocation().getBlock()!=null) {
                teamColors teamChest = Main.game.getTeamManager().getTeamChest(e.getInventory().getLocation().getBlock());
                if (teamChest == null) {
                    return;
                }
                teamColors team = Main.game.getTeamManager().getTeam((Player) e.getPlayer());
                if (!(team.equals(teamChest))) {
                    e.getPlayer().sendMessage(text.f(String.format("&cYou may not open %s's chest until they are eliminated!",teamChest)));
                    e.setCancelled(true);
                }
            }
        }
    }

}