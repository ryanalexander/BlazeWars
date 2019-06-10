package com.stelch.games2.BlazeWars.Inventories;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.Utils.text;
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
        shop = Bukkit.createInventory(null,9*6, text.f("&cSkully"));

        Integer[] placeholder_pos ={0,1,2,3,4,5,6,7,8,9,17,18,19,20,21,22,23,24,25,26,27,31,35,36,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53};

        ItemStack placeholder = new Item(Material.BLACK_STAINED_GLASS_PANE,"&r").spigot();
        for(Integer i:placeholder_pos){shop.setItem(i,placeholder);}

        Item Blocks = new Item(Material.END_STONE,"&6Blocks");
        Item Weapons = new Item(Material.STONE_SWORD,"&6Weapons");
        Item Armor = new Item(Material.IRON_CHESTPLATE,"&6Armor");
        Item Tools = new Item(Material.GOLDEN_PICKAXE,"&6Tools");
        Item Potions = new Item(Material.DRAGON_BREATH,"&6Potions");
        Item Eggs = new Item(Material.MAGMA_CUBE_SPAWN_EGG,"&6Eggs");
        Item Special = new Item(Material.TNT,"&6Special");

        Blocks.setOnClick(new Item.click() {
            @Override
            public void run(Player p) {
                p.sendMessage("Fuck off Binary. You're really fucking gay. Don't touch my block ;)");
                p.closeInventory();
            }
        });
        Bukkit.broadcastMessage(String.format("Added %s to %s &r[ActionCount:%s]",Blocks.is.getType(),shop.getName(), Main.Actions.size()));

        ItemStack BlockIs = Blocks.spigot();

        shop.setItem(10,BlockIs);
        shop.setItem(11,Weapons.spigot());
        shop.setItem(12,Armor.spigot());
        shop.setItem(13,Tools.spigot());
        shop.setItem(14,Potions.spigot());
        shop.setItem(15,Eggs.spigot());
        shop.setItem(16,Special.spigot());

        menus.add(shop);

        return shop;
    }
}
