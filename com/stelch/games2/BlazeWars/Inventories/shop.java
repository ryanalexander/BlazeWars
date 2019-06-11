package com.stelch.games2.BlazeWars.Inventories;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.Utils.text;
import com.stelch.games2.BlazeWars.varables.menuSource;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class shop implements Listener {

    private Inventory shop;

    private ArrayList<Inventory> menus=new ArrayList<>();

    // 11,12,13,14,15,16,17,29,30,31,33,34,35,38,39

    public Inventory getShop(Player player) {
        this.shop = header.format(Bukkit.createInventory(null,9*6, text.f("&cSkully")),true);

        Item islandUpgrades = new Item(Material.NETHER_STAR,"&eIsland Upgrades");
        islandUpgrades.setLore(new String[]{
                "&r",
                "&bThis menu contains all of",
                "&bthe upgradable features",
                "&bfor you island, and Blaze."
        });
        islandUpgrades.setOnClick(new Item.click() {
            @Override
            public void run(Player p) {
                p.openInventory(com.stelch.games2.BlazeWars.Inventories.islandUpgrades.getShop(p, menuSource.SHOP));
            }
        });

        this.shop.setItem(40,islandUpgrades.spigot());


        this.menus.add(this.shop);
        return this.shop;
    }
}
