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

        Item invis = new Item(POTION,"&bInvisibility Potion");
        invis.setLore(new String[]{
                "&r",
                "&aCost: &c2 Blaze Rod",
                "&r",
                "&aDuration: &630 Secs"
        });
        invis.setAmount(1);
        invis.setOnClick(new Item.click(){public void run(Player p){
            if(com.stelch.games2.BlazeWars.Inventories.shop.doCharge(p, BLAZE_ROD,2)){
                ItemStack potion = new ItemStack(Material.POTION, 1);

                PotionMeta potionmeta = (PotionMeta) potion.getItemMeta();
                potionmeta.setMainEffect(PotionEffectType.INVISIBILITY);
                PotionEffect speed = new PotionEffect(PotionEffectType.INVISIBILITY, (30*20), 1);
                potionmeta.addCustomEffect(speed, true);
                potionmeta.setDisplayName(Text.format("&bInvisibility Potion"));
                potion.setItemMeta(potionmeta);
                p.getInventory().addItem(potion);
            }
        }
        });


        Item jump = new Item(POTION,"&bJump Juice");
        jump.setLore(new String[]{
                "&r",
                "&aCost: &c1 Blaze Rod",
                "&r",
                "&aDuration: &630 Secs"
        });
        jump.setAmount(1);
        jump.setOnClick(new Item.click(){public void run(Player p){
            if(com.stelch.games2.BlazeWars.Inventories.shop.doCharge(p, BLAZE_ROD,1)){
                ItemStack potion = new ItemStack(Material.POTION, 1);

                PotionMeta potionmeta = (PotionMeta) potion.getItemMeta();
                potionmeta.setMainEffect(PotionEffectType.JUMP);
                PotionEffect speed = new PotionEffect(PotionEffectType.JUMP, (30*20), 3);
                potionmeta.addCustomEffect(speed, true);
                potionmeta.setColor(Color.LIME);
                potionmeta.setDisplayName(Text.format("&bJump Juice"));
                potion.setItemMeta(potionmeta);
                p.getInventory().addItem(potion);
            }
        }
        });

        potions.shop.setItem(0,close.spigot());
        potions.shop.setItem(19,invis.spigot());
        potions.shop.setItem(20,jump.spigot());

        potions.menus.add(potions.shop);
        return potions.shop;
    }
}
