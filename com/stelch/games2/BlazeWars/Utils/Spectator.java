package com.stelch.games2.BlazeWars.Utils;

import com.stelch.games2.BlazeWars.Inventories.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class Spectator {

    ItemStack[] saved_inventory;

    public Spectator(Player player) {
        saved_inventory = player.getInventory().getContents();

        player.getInventory().clear();

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
                    ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                    String headName = player.getName();
                    SkullMeta sm = (SkullMeta)skull.getItemMeta();
                    sm.setOwner(headName);
                    sm.setOwningPlayer(player);
                    sm.setDisplayName(String.format("&6Teleport to %s",player.getName()));
                    skull.setItemMeta(sm);
                    upgradeMenu.addItem(skull);

                }
            }
        });

    }
}
