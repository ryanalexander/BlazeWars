package com.stelch.games2.BlazeWars.Inventories;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.varables.menuSource;
import com.stelch.games2.BlazeWars.varables.teamColors;
import com.stelch.games2.core.Utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class islandUpgrades implements Listener {

    private static Inventory shop;

    private static ArrayList<Inventory> menus=new ArrayList<>();

    // 11,12,13,14,15,16,17,29,30,31,33,34,35,38,39

    public static Inventory getShop(Player player, menuSource source) {
        islandUpgrades.shop = header.format(Bukkit.createInventory(null,9*6, Text.format("&cBazza's Cart")),false);

        Item close = new Item(Material.BARRIER,"&cBack");
        close.setOnClick(new Item.click() {
            @Override
            public void run(Player p) {
                p.openInventory((new shop()).getShop(p));
            }
        });
        Item mshop = new Item(Material.NETHER_STAR,"&ePlayer Shop");
        mshop.setOnClick(new Item.click() {
            @Override
            public void run(Player p) {
                p.openInventory((new shop()).getShop(p));
            }
        });

        int[] costs = {2, 5, 10, 16};

        Item forge_upgrade = new Item(Material.BREWING_STAND,"Upgrade Forge");
        teamColors team = Main.game.getTeamManager().getTeam(player);
        int level = Main.game.getTeamManager().getSpawner_level(team);
        forge_upgrade.setLore(new String[]{
                "&r",
                ((level<=3)?String.format("&aCost: &6%s Blaze Powder",costs[level]):"&aFully Upgraded"),
                "&r",
                "&aLevel: &e"+level,
                "&aIron: &e"+Main.game.getTeamManager().getSpawner(team,Material.IRON_INGOT).getSpeed(),
                "&aGold: &e"+Main.game.getTeamManager().getSpawner(team,Material.GOLD_INGOT).getSpeed(),
                "&aRod: &e"+((Main.game.getTeamManager().getSpawner_level(team)>=3)?Main.game.getTeamManager().getSpawner(team,Material.BLAZE_ROD).getSpeed():"-")
        });
        forge_upgrade.setOnClick(new Item.click() {
            @Override
            public void run(Player player) {
                teamColors team = Main.game.getTeamManager().getTeam(player);
                int level = Main.game.getTeamManager().getSpawner_level(team);
                if(level>3){player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE,1,1);return;}
                if(doCharge(player,Material.BLAZE_POWDER,costs[level])) {
                    Main.game.getTeamManager().setSpawner_level(team, level + 1);
                    player.openInventory(islandUpgrades.getShop(player, source));
                }
            }
        });

        if(source==menuSource.SHOP) islandUpgrades.shop.setItem(0,close.spigot());
        if(!(source==menuSource.SHOP)) islandUpgrades.shop.setItem(0,mshop.spigot());
        islandUpgrades.shop.setItem(19,forge_upgrade.spigot());

        islandUpgrades.menus.add(islandUpgrades.shop);
        return islandUpgrades.shop;
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
