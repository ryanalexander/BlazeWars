package com.stelch.games2.BlazeWars.Inventories;

import com.stelch.games2.BlazeWars.Utils.text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class blocks implements Listener {

    private static Inventory shop;

    private static ArrayList<Inventory> menus=new ArrayList<>();

    // 11,12,13,14,15,16,17,29,30,31,33,34,35,38,39

    public static Inventory getShop(Player player) {
        blocks.shop = header.format(Bukkit.createInventory(null,9*6, text.f("&cSkully's blocks")),false);

        Item close = new Item(Material.BARRIER,"&cBack");
        close.setOnClick(new Item.click() {
            @Override
            public void run(Player p) {
                p.openInventory(com.stelch.games2.BlazeWars.Inventories.shop.getShop(p));
            }
        });

        Item wool = new Item(Material.WHITE_WOOL,"&bWool");
        wool.setAmount(8);
        wool.setOnClick(new Item.click(){public void run(Player p){p.getInventory().addItem(new ItemStack(Material.WHITE_WOOL,8));}});

        Item wooden_planks = new Item(Material.OAK_PLANKS,"&bWooden Planks");
        wooden_planks.setAmount(8);
        wooden_planks.setOnClick(new Item.click(){public void run(Player p){p.getInventory().addItem(new ItemStack(Material.OAK_PLANKS,8));}});

        Item clay = new Item(Material.CLAY,"&bClay");
        clay.setAmount(12);
        clay.setOnClick(new Item.click(){public void run(Player p){p.getInventory().addItem(new ItemStack(Material.CLAY,12));}});

        blocks.shop.setItem(0,close.spigot());
        blocks.shop.setItem(19,wool.spigot());
        blocks.shop.setItem(20,wooden_planks.spigot());
        blocks.shop.setItem(21,clay.spigot());

        blocks.menus.add(blocks.shop);
        return blocks.shop;
    }
}
