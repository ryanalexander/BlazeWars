/*
 *
 * *
 *  *
 *  * © Stelch Games 2019, distribution is strictly prohibited
 *  *
 *  * Changes to this file must be documented on push.
 *  * Unauthorised changes to this file are prohibited.
 *  *
 *  * @author Ryan Wood
 *  * @since 14/7/2019
 *
 */

package com.stelch.games2.BlazeWars.Inventories;


import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.varables.itemUpgrades;
import com.stelch.games2.core.Utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

import static org.bukkit.Material.*;

public class armor implements Listener {

    private static Inventory shop;

    private static ArrayList<Inventory> menus=new ArrayList<>();

    // 11,12,13,14,15,16,17,29,30,31,33,34,35,38,39

    public static Inventory getShop(Player player) {
        armor.shop = header.format(Bukkit.createInventory(null,9*6, Text.format("&cSkully's Clothes Collection")),false);

        Item close = new Item(Material.BARRIER,"&cBack");
        close.setOnClick(new Item.click() {
            @Override
            public void run(Player p) {
                p.openInventory((new shop()).getShop(p));
            }
        });

        Item armor_chain = new Item(CHAINMAIL_CHESTPLATE,"&7Chainmail Armor");
        armor_chain.setLore(new String[]{
                "&r",
                "&aCost: &f40 Iron",
        });
        armor_chain.setAmount(1);
        armor_chain.setOnClick(new Item.click(){public void run(Player p){
            if(com.stelch.games2.BlazeWars.Inventories.shop.doCharge(p, IRON_INGOT,40)){
                ItemStack armor_leggings = new ItemStack(CHAINMAIL_LEGGINGS);
                ItemStack armor_boots = new ItemStack(CHAINMAIL_BOOTS);
                p.getInventory().setArmorContents(new ItemStack[]{armor_boots,armor_leggings,player.getInventory().getChestplate(),player.getInventory().getHelmet()});
            }
        }
        });

        Item armor_iron = new Item(IRON_CHESTPLATE,"&fIron Armor");
        armor_iron.setLore(new String[]{
                "&r",
                "&aCost: &612 Gold",
        });
        armor_iron.setAmount(1);
        armor_iron.setOnClick(new Item.click(){public void run(Player p){
            if(com.stelch.games2.BlazeWars.Inventories.shop.doCharge(p, GOLD_INGOT,12)){
                ItemStack armor_leggings = new ItemStack(IRON_LEGGINGS);
                ItemStack armor_boots = new ItemStack(IRON_BOOTS);
                p.getInventory().setArmorContents(new ItemStack[]{armor_boots,armor_leggings,player.getInventory().getChestplate(),player.getInventory().getHelmet()});
            }
        }
        });

        Item armor_diamond = new Item(DIAMOND_CHESTPLATE,"&bDiamond Armor");
        armor_diamond.setLore(new String[]{
                "&r",
                "&aCost: &c6 Blaze Rod",
        });
        armor_diamond.setAmount(1);
        armor_diamond.setOnClick(new Item.click(){public void run(Player p){
            if(com.stelch.games2.BlazeWars.Inventories.shop.doCharge(p, BLAZE_ROD,6)){
                ItemStack armor_leggings = new ItemStack(DIAMOND_LEGGINGS);
                ItemStack armor_boots = new ItemStack(DIAMOND_BOOTS);
                p.getInventory().setArmorContents(new ItemStack[]{armor_boots,armor_leggings,player.getInventory().getChestplate(),player.getInventory().getHelmet()});
            }
        }
        });

        armor.shop.setItem(0,close.spigot());
        armor.shop.setItem(19,armor_chain.spigot());
        armor.shop.setItem(20,armor_iron.spigot());
        armor.shop.setItem(21,armor_diamond.spigot());

        armor.menus.add(armor.shop);
        return armor.shop;
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
}
