package com.stelch.games2.BlazeWars.Inventories;


import com.stelch.games2.BlazeWars.varables.itemUpgrades;
import com.stelch.games2.core.Utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;

import static org.bukkit.Material.*;

public class potions implements Listener {

    private static Inventory shop;

    private static ArrayList<Inventory> menus=new ArrayList<>();

    // 11,12,13,14,15,16,17,29,30,31,33,34,35,38,39

    public static Inventory getShop(Player player) {
        potions.shop = header.format(Bukkit.createInventory(null,9*6, Text.format("&cSkully's Specials")),false);

        Item close = new Item(Material.BARRIER,"&cBack");
        close.setOnClick(new Item.click() {
            @Override
            public void run(Player p) {
                p.openInventory((new shop()).getShop(p));
            }
        });

        Item invis = new Item(POTION,"&bInvisibility");
        invis.setLore(new String[]{
                "&r",
                "&aCost: &c2 Blaze Rod",
                "&r",
                "&aDuration: &630 Secs"
        });
        invis.setAmount(1);
        invis.setOnClick(new Item.click(){public void run(Player p){
            if(potions.doCharge(p, BLAZE_ROD,2)){
                ItemStack potion = new ItemStack(Material.POTION, 1);

                PotionMeta potionmeta = (PotionMeta) potion.getItemMeta();
                potionmeta.setMainEffect(PotionEffectType.INVISIBILITY);
                PotionEffect speed = new PotionEffect(PotionEffectType.INVISIBILITY, (30*20), 1);
                potionmeta.addCustomEffect(speed, true);
                potionmeta.setDisplayName(Text.format("&bInvisibility"));
                potion.setItemMeta(potionmeta);
                p.getInventory().addItem(potion);
            }
        }
        });

        potions.shop.setItem(0,close.spigot());
        potions.shop.setItem(19,invis.spigot());

        potions.menus.add(potions.shop);
        return potions.shop;
    }

    public static Material nextUpgrade (Player player, itemUpgrades upgrade){
        switch (upgrade) {
            case PICAXE:
                if(player.getInventory().contains(DIAMOND_PICKAXE)){return DIAMOND_PICKAXE;}
                if(player.getInventory().contains(GOLDEN_PICKAXE)){return DIAMOND_PICKAXE;}
                if(player.getInventory().contains(IRON_PICKAXE)){return GOLDEN_PICKAXE;}
                if(player.getInventory().contains(WOODEN_PICKAXE)){return IRON_PICKAXE;}
                return WOODEN_PICKAXE;
            case AXE:
                if(player.getInventory().contains(DIAMOND_AXE)){return DIAMOND_AXE;}
                if(player.getInventory().contains(GOLDEN_AXE)){return DIAMOND_AXE;}
                if(player.getInventory().contains(IRON_AXE)){return GOLDEN_AXE;}
                if(player.getInventory().contains(WOODEN_AXE)){return IRON_AXE;}
                return WOODEN_AXE;
            case SHOVEL:
                if(player.getInventory().contains(DIAMOND_SHOVEL)){return DIAMOND_SHOVEL;}
                if(player.getInventory().contains(GOLDEN_SHOVEL)){return DIAMOND_SHOVEL;}
                if(player.getInventory().contains(IRON_SHOVEL)){return GOLDEN_SHOVEL;}
                if(player.getInventory().contains(WOODEN_SHOVEL)){return IRON_SHOVEL;}
                return WOODEN_SHOVEL;
            default:
                break;
        }
        return null;
    }

    public static Material lastUpgrade (Player player, itemUpgrades upgrade) {
        switch (upgrade) {
            case PICAXE:
                if(player.getInventory().contains(DIAMOND_PICKAXE)){return DIAMOND_PICKAXE;}
                if(player.getInventory().contains(GOLDEN_PICKAXE)){return GOLDEN_PICKAXE;}
                if(player.getInventory().contains(IRON_PICKAXE)){return IRON_PICKAXE;}
                if(player.getInventory().contains(WOODEN_PICKAXE)){return WOODEN_PICKAXE;}
                return null;
            case AXE:
                if(player.getInventory().contains(DIAMOND_AXE)){return DIAMOND_AXE;}
                if(player.getInventory().contains(GOLDEN_AXE)){return GOLDEN_AXE;}
                if(player.getInventory().contains(IRON_AXE)){return IRON_AXE;}
                if(player.getInventory().contains(WOODEN_AXE)){return WOODEN_AXE;}
                return null;
            case SHOVEL:
                if(player.getInventory().contains(DIAMOND_SHOVEL)){return DIAMOND_SHOVEL;}
                if(player.getInventory().contains(GOLDEN_SHOVEL)){return GOLDEN_SHOVEL;}
                if(player.getInventory().contains(IRON_SHOVEL)){return IRON_SHOVEL;}
                if(player.getInventory().contains(WOODEN_SHOVEL)){return WOODEN_SHOVEL;}
                return null;
            default:
                break;
        }
        return null;
    }

    public static ItemStack getPrice (Player player, itemUpgrades upgrade) {
        switch (upgrade) {
            case PICAXE:
                if(player.getInventory().contains(DIAMOND_PICKAXE)){return null;}
                if(player.getInventory().contains(GOLDEN_PICKAXE)){return new ItemStack(Material.GOLD_INGOT,16);}
                if(player.getInventory().contains(IRON_PICKAXE)){return new ItemStack(Material.GOLD_INGOT,8);}
                if(player.getInventory().contains(WOODEN_PICKAXE)){return new ItemStack(Material.IRON_INGOT,20);}
                return new ItemStack(IRON_INGOT,10);
            case AXE:
                if(player.getInventory().contains(DIAMOND_AXE)){return null;}
                if(player.getInventory().contains(GOLDEN_AXE)){return new ItemStack(Material.GOLD_INGOT,16);}
                if(player.getInventory().contains(IRON_AXE)){return new ItemStack(Material.GOLD_INGOT,8);}
                if(player.getInventory().contains(WOODEN_AXE)){return new ItemStack(Material.IRON_INGOT,20);}
                return new ItemStack(IRON_INGOT,10);
            case SHOVEL:
                if(player.getInventory().contains(DIAMOND_SHOVEL)){return null;}
                if(player.getInventory().contains(GOLDEN_SHOVEL)){return new ItemStack(Material.GOLD_INGOT,16);}
                if(player.getInventory().contains(IRON_SHOVEL)){return new ItemStack(Material.GOLD_INGOT,8);}
                if(player.getInventory().contains(WOODEN_SHOVEL)){return new ItemStack(Material.IRON_INGOT,20);}
                return new ItemStack(IRON_INGOT,10);
            default:
                break;
        }
        return null;
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
