package com.stelch.games2.BlazeWars.Inventories;

import com.stelch.games2.BlazeWars.varables.menuSource;
import com.stelch.games2.core.Utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

import static org.bukkit.Material.*;

public class eggs  implements Listener {

    private static Inventory shop;

    private static ArrayList<Inventory> menus=new ArrayList<>();

    // 11,12,13,14,15,16,17,29,30,31,33,34,35,38,39

    public static Inventory getShop(Player player) {
        eggs.shop = header.format(Bukkit.createInventory(null,9*6, Text.format("&cSkully's Little Friends")),false);

        Item close = new Item(Material.BARRIER,"&cBack");
        close.setOnClick(new Item.click() {
            @Override
            public void run(Player p) {
                p.openInventory((new shop()).getShop(p));
            }
        });


        Item MAGMA_CUBE_SPAWN = new Item(MAGMA_CUBE_SPAWN_EGG,"&bLava Minion");
        MAGMA_CUBE_SPAWN.setLore(new String[]{
                "&r",
                "&aCost: &c30 Gold",
                "&r",
                "&aDuration: &6120 Secs"
        });
        MAGMA_CUBE_SPAWN.setAmount(1);
        MAGMA_CUBE_SPAWN.setOnClick(new Item.click(){public void run(Player p){
            if(eggs.doCharge(p, GOLD_INGOT,30)){
                MAGMA_CUBE_SPAWN.setLore(new String[]{String.format("&aYou are tha sneaky mayn")});
                p.getInventory().addItem(MAGMA_CUBE_SPAWN.spigot());
            }
        }});

        eggs.shop.setItem(0,close.spigot());
        eggs.shop.setItem(19,MAGMA_CUBE_SPAWN.spigot());

        eggs.menus.add(eggs.shop);
        return eggs.shop;
    }

    public static boolean doCharge (Player player, Material mat, int amount) {

        if(player.getInventory().contains(mat,amount)){
            ItemStack payload = new ItemStack(mat);
            for (int i = 1; i < amount; i++) {
                player.getInventory().removeItem(payload);
            }
            player.getInventory().remove(payload);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,1,1);
            return true;
        }else {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
            return false;
        }
    }
}
