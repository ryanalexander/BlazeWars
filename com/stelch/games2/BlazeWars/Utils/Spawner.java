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

package com.stelch.games2.BlazeWars.Utils;


import com.stelch.games2.BlazeWars.Game;
import com.stelch.games2.BlazeWars.varables.gameState;
import com.stelch.games2.core.InventoryUtils.Item;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Spawner {
    private boolean stopped = false;

    private Location location;

    private long nextDrop = 0;

    //private Long[] levels = new Long[]{1300L, 1100L, 1000L, 800L, 700L, 400L, 200L, 120L, 80L, 40L};

    private Long[] levels = new Long[]{1400L, 700L, 280L, 260L, 240L, 220L, 200L, 40L, 35L, 30L, 25L, 20L};

    private Material type;

    private int level = 0;

    public Spawner(Game game, final Location l, final ItemStack m, int level, int max_holding) {
        System.out.println(String.format("[SPAWNER] Created %s spawner at x: %s y: %s z: %s with level: %s", m.getType().name(), l.getX(), l.getY(), l.getZ(), level));
        this.location = l;
        this.level = level;
        this.type = m.getType();
        (new BukkitRunnable() {
            public void run() {
                nextDrop--;
                if (Spawner.this.stopped || (game.getGameState()) != gameState.IN_GAME)
                    cancel();
                if (getEntitiesAroundPoint(l, max_holding, m) >= 10)
                    return;
                if (nextDrop <= 0) {
                    nextDrop = ((levels[Spawner.this.level]) / 20);
                    l.getWorld().dropItem(l, m);
                }
            }
        }).runTaskTimer(game.getHandler(), 0L, 25L);
    }

    public long getNextDrop() {
        return nextDrop;
    }

    public void setLevel(int level) {
        System.out.println(String.format("[SPAWNER] Updated %s spawner at x: %s y: %s z: %s with level: %s", type.name(), location.getX(), location.getY(), location.getZ(), level));
        this.level = level;
    }

    public long getSpeed() {
        return levels[level];
    }

    public int getLevel() {
        return level;
    }

    public void stop() {
        this.stopped = true;
    }

    private static Integer getEntitiesAroundPoint(Location location, double radius, ItemStack type) {
        int count = 0;
        for (Entity e : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
            if (e instanceof Item) {
                count++;
            }
        }
        return count;
    }

    private static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }
}