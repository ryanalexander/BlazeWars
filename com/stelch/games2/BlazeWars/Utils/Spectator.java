package com.stelch.games2.BlazeWars.Utils;

import com.stelch.games2.BlazeWars.Inventories.Item;
import com.stelch.games2.BlazeWars.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class Spectator {

    Player player;
    ItemStack[] saved_inventory;

    public Spectator(Player player) {
        this.player=player;

        Main.game.spectators.add(player);
        player.getInventory().clear();
        player.setFlying(true);
        player.setAllowFlight(true);
        player.setInvulnerable(true);

        Item player_teleport = new Item(Material.COMPASS,"&aPlayer Portal");

        player_teleport.setLore(new String[]{
                "&r",
                "&eThis item is used to teleport",
                "&ebetween different players in game"
        });

        player_teleport.setOnClick(new Item.click() {
            @Override
            public void run(Player p) {
                Inventory upgradeMenu = Bukkit.createInventory(null,9*1,text.f("&aPlayer Portal"));
                for(Player player : Bukkit.getOnlinePlayers()){
                    Item skull = new Item(Material.PLAYER_HEAD,String.format("&eTeleport to %s",player.getName()));
                    String headName = player.getName();
                    SkullMeta sm = (SkullMeta)skull.spigot().getItemMeta();
                    skull.setOnClick(new Item.click() {
                        Player target = player;
                        @Override
                        public void run(Player param1Player) {
                            param1Player.teleport(target);
                        }
                    });
                    sm.setOwner(headName);
                    sm.setOwningPlayer(player);
                    skull.spigot().setItemMeta(sm);
                    upgradeMenu.addItem(skull.spigot());
                }
            }
        });

        player.getInventory().setItem(0,player_teleport.spigot());
    }

    public void leave() {
        Main.game.spectators.remove(player);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
    }
}
