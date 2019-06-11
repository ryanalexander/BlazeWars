package com.stelch.games2.BlazeWars.Inventories;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class header {

    public static Inventory format(Inventory inv,boolean header){
        ItemStack placeholder = new Item(Material.BLACK_STAINED_GLASS_PANE,"&r").setOnClick(new Item.click() {
            @Override
            public void run(Player param1Player) {

            }
        }).spigot();
        for(int i=0;i<54;i++){inv.setItem(i,placeholder);}

        if(header){
            Item Blocks = new Item(Material.END_STONE,"&6Blocks");
            Item Weapons = new Item(Material.STONE_SWORD,"&6Weapons");
            Item Armor = new Item(Material.IRON_CHESTPLATE,"&6Armor");
            Item Tools = new Item(Material.GOLDEN_PICKAXE,"&6Tools");
            Item Potions = new Item(Material.DRAGON_BREATH,"&6Potions");
            Item Eggs = new Item(Material.MAGMA_CUBE_SPAWN_EGG,"&6Eggs");
            Item Special = new Item(Material.TNT,"&6Special");

            Armor.setLore(new String[]{"&r","&eComing Soon"});
            Potions.setLore(new String[]{"&r","&eComing Soon"});
            Eggs.setLore(new String[]{"&r","&eComing Soon"});
            Special.setLore(new String[]{"&r","&eComing Soon"});

            Blocks.setOnClick(new Item.click(){public void run(Player p){p.openInventory(blocks.getShop(p));}});
            Weapons.setOnClick(new Item.click(){public void run(Player p){p.openInventory(weapons.getShop(p));}});
            Armor.setOnClick(new Item.click(){public void run(Player p){p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE,1,1);}});
            Tools.setOnClick(new Item.click(){public void run(Player p){p.openInventory(tools.getShop(p));}});
            Potions.setOnClick(new Item.click(){public void run(Player p){p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE,1,1);}});
            Eggs.setOnClick(new Item.click(){public void run(Player p){p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE,1,1);}});
            Special.setOnClick(new Item.click(){public void run(Player p){p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE,1,1);}});

            inv.setItem(10,Blocks.spigot());
            inv.setItem(11,Weapons.spigot());
            inv.setItem(12,Armor.spigot());
            inv.setItem(13,Tools.spigot());
            inv.setItem(14,Potions.spigot());
            inv.setItem(15,Eggs.spigot());
            inv.setItem(16,Special.spigot());
        }

        return inv;
    }

}
