package com.stelch.games2.BlazeWars.Inventories;

import com.stelch.games2.BlazeWars.Utils.text;
import com.stelch.games2.BlazeWars.varables.itemUpgrades;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static com.stelch.games2.BlazeWars.varables.itemUpgrades.*;
import static org.bukkit.Material.*;

public class specials implements Listener {

    private static Inventory shop;

    private static ArrayList<Inventory> menus=new ArrayList<>();

    // 11,12,13,14,15,16,17,29,30,31,33,34,35,38,39

    public static Inventory getShop(Player player) {
        specials.shop = header.format(Bukkit.createInventory(null,9*6, text.f("&cSkully's Tools")),false);

        Item close = new Item(Material.BARRIER,"&cBack");
        close.setOnClick(new Item.click() {
            @Override
            public void run(Player p) {
                p.openInventory((new shop()).getShop(p));
            }
        });

        Item tnt = new Item(Material.TNT,"&bTNT");
        tnt.setLore(new String[]{
                "&r",
                "&aCost: &64 Gold"
        });
        tnt.setAmount(1);
        tnt.setOnClick(new Item.click(){public void run(Player p){if(specials.doCharge(p, GOLD_INGOT,4))p.getInventory().addItem(new ItemStack(Material.TNT,1));}});

        Item epearl = new Item(ENDER_PEARL,"&bEnder Pearl");
        epearl.setLore(new String[]{
                "&r",
                "&aCost: &c2 Blaze Rod"
        });
        epearl.setAmount(1);
        epearl.setOnClick(new Item.click(){public void run(Player p){if(specials.doCharge(p, BLAZE_ROD,2))p.getInventory().addItem(new ItemStack(ENDER_PEARL,1));}});

        Item water = new Item(WATER_BUCKET,"&bWater");
        water.setLore(new String[]{
                "&r",
                "&aCost: &62 Gold"
        });
        water.setAmount(1);
        water.setOnClick(new Item.click(){public void run(Player p){if(specials.doCharge(p, GOLD_INGOT,2))p.getInventory().addItem(new ItemStack(WATER_BUCKET,1));}});

        Item fireball = new Item(FIRE_CHARGE,"&bFireball");
        fireball.setLore(new String[]{
                "&r",
                "&aCost: &f40 Iron"
        });
        fireball.setAmount(1);
        fireball.setOnClick(new Item.click(){public void run(Player p){if(specials.doCharge(p, IRON_INGOT,40))p.getInventory().addItem(new ItemStack(FIRE_CHARGE,1));}});

        Item bridge_egg = new Item(EGG,"&bBridge Egg");
        bridge_egg.setLore(new String[]{
                "&r",
                "&aCost: &c2 Blaze Rod"
        });
        bridge_egg.setAmount(1);
        bridge_egg.setOnClick(new Item.click(){public void run(Player p){if(specials.doCharge(p, BLAZE_ROD,2))p.getInventory().addItem(new ItemStack(EGG,1));}});

        Item golden_apple = new Item(GOLDEN_APPLE,"&bGolden Apple");
        golden_apple.setLore(new String[]{
                "&r",
                "&aCost: &62 Gold"
        });
        golden_apple.setAmount(1);
        golden_apple.setOnClick(new Item.click(){public void run(Player p){if(specials.doCharge(p, GOLD_INGOT,2))p.getInventory().addItem(new ItemStack(GOLDEN_APPLE,1));}});


        specials.shop.setItem(0,close.spigot());
        specials.shop.setItem(19,tnt.spigot());
        specials.shop.setItem(20,epearl.spigot());
        //specials.shop.setItem(21,water.spigot());
        //specials.shop.setItem(22,fireball.spigot());
        //specials.shop.setItem(23, bridge_egg.spigot());
        specials.shop.setItem(23,golden_apple.spigot());

        specials.menus.add(specials.shop);
        return specials.shop;
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
