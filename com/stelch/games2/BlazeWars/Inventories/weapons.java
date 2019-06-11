package com.stelch.games2.BlazeWars.Inventories;

import com.stelch.games2.BlazeWars.Utils.text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class weapons implements Listener {

    private static Inventory shop;

    private static ArrayList<Inventory> menus=new ArrayList<>();

    // 11,12,13,14,15,16,17,29,30,31,33,34,35,38,39

    public static Inventory getShop(Player player) {
        weapons.shop = header.format(Bukkit.createInventory(null,9*6, text.f("&cSkully's blocks")),false);

        Item close = new Item(Material.BARRIER,"&cBack");
        close.setOnClick(new Item.click() {
            @Override
            public void run(Player p) {
                p.openInventory((new shop()).getShop(p));
            }
        });

        Item stone_sword = new Item(Material.STONE_SWORD,"&bStone Sword");
        stone_sword.setLore(new String[]{
                "&r",
                "&aCost: &f6 Iron"
        });
        stone_sword.setAmount(1);
        stone_sword.setOnClick(new Item.click(){public void run(Player p){if(weapons.doCharge(p,Material.IRON_INGOT,6))p.getInventory().addItem(new ItemStack(Material.STONE_SWORD,1));}});

        Item iron_sword = new Item(Material.IRON_SWORD,"&bIron Sword");
        iron_sword.setLore(new String[]{
                "&r",
                "&aCost: &66 Gold"
        });
        iron_sword.setAmount(1);
        iron_sword.setOnClick(new Item.click(){public void run(Player p){if(weapons.doCharge(p,Material.GOLD_INGOT,6))p.getInventory().addItem(new ItemStack(Material.IRON_SWORD,1));}});

        Item gold_sword = new Item(Material.GOLDEN_SWORD,"&bGolden Sword");
        gold_sword.setLore(new String[]{
                "&r",
                "&aCost: &612 Gold"
        });
        gold_sword.setAmount(1);
        gold_sword.setOnClick(new Item.click(){public void run(Player p){if(weapons.doCharge(p,Material.GOLD_INGOT,12))p.getInventory().addItem(new ItemStack(Material.GOLDEN_SWORD,1));}});

        Item diamond_sword = new Item(Material.DIAMOND_SWORD,"&bDiamond Sword");
        diamond_sword.setLore(new String[]{
                "&r",
                "&aCost: &e4 Blaze Powder"
        });
        diamond_sword.setAmount(1);
        diamond_sword.setOnClick(new Item.click(){public void run(Player p){if(weapons.doCharge(p,Material.BLAZE_POWDER,4))p.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD,1));}});

        Item bow = new Item(Material.BOW,"&bBow");
        bow.setLore(new String[]{
                "&r",
                "&aCost: &612 Gold"
        });
        bow.setAmount(1);
        bow.setOnClick(new Item.click(){public void run(Player p){if(weapons.doCharge(p,Material.BLAZE_POWDER,4))p.getInventory().addItem(new ItemStack(Material.BOW,1));}});

        Item super_bow = new Item(Material.BOW,"&dSuper Bow");
        super_bow.setLore(new String[]{
                "&r",
                "&aCost: &625 Gold"
        });
        super_bow.setAmount(1);
        ItemStack super_bowis = new ItemStack(Material.BOW);
        ItemMeta super_bowmeta = super_bowis.getItemMeta();
        super_bowmeta.addEnchant(Enchantment.ARROW_DAMAGE,2,true);
        super_bowmeta.addEnchant(Enchantment.ARROW_KNOCKBACK,1,true);
        super_bowmeta.setDisplayName(text.f("&d"+player.getName()+"'s Super bow"));
        super_bowis.setItemMeta(super_bowmeta);
        super_bow.setOnClick(new Item.click(){public void run(Player p){if(weapons.doCharge(p,Material.GOLD_INGOT,25))p.getInventory().addItem(super_bowis);}});

        Item arrows = new Item(Material.ARROW,"&bArrow");
        arrows.setLore(new String[]{
                "&r",
                "&aCost: &62 Gold"
        });
        arrows.setAmount(8);
        arrows.setOnClick(new Item.click(){public void run(Player p){if(weapons.doCharge(p,Material.GOLD_INGOT,2))p.getInventory().addItem(new ItemStack(Material.ARROW,8));}});

        weapons.shop.setItem(0,close.spigot());
        weapons.shop.setItem(19,stone_sword.spigot());
        weapons.shop.setItem(20,iron_sword.spigot());
        weapons.shop.setItem(21,gold_sword.spigot());
        weapons.shop.setItem(22,diamond_sword.spigot());
        weapons.shop.setItem(23,bow.spigot());
        weapons.shop.setItem(24,super_bow.spigot());
        weapons.shop.setItem(25,arrows.spigot());

        weapons.menus.add(weapons.shop);
        return weapons.shop;
    }

    public static boolean doCharge (Player player, Material mat, int amount) {

        if(player.getInventory().contains(mat,amount)){
            ItemStack payload = new ItemStack(mat);
            for (int i = 0; i < amount; i++) {
                player.getInventory().removeItem(payload);
            }
            player.getInventory().remove(payload);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME,1,1);
            return true;
        }else {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
            return false;
        }
    }
}