package com.stelch.games2.BlazeWars.Utils;

import com.stelch.games2.BlazeWars.Game;
import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.core.Utils.Text;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class Forge implements Listener {

    private ArmorStand e = null;

    private ArmorStand e1;

    private Game.spawnner spawner=null;

    private boolean deleted = false;

    private static Main plugin;

    public Forge(Main main) { plugin = main; }

    public Forge(Location location, Material material, int data, boolean is_Small, String text) {
        if(plugin==null){Bukkit.broadcastMessage("&aForge> &cPlugin not specified");}
        ItemStack skull = new ItemStack(material,1);
        this.e = (ArmorStand) Bukkit.getWorld(location.getWorld().getName()).spawnEntity(location, EntityType.ARMOR_STAND);
        System.out.println(String.format("[SPINNING_BLOCK] Created %s block at x: %s y: %s z: %s with speed: %s",material.name(),location.getX(),location.getY(),location.getZ(),"?"));
        this.e.setVisible(false);
        this.e.setGravity(false);
        this.e.teleport(this.e.getLocation());
        this.e.setCanPickupItems(false);
        this.e.setRemoveWhenFarAway(false);
        this.e.getEquipment().setHelmet(skull);
        this.e.setCustomName(Text.format(text));
        this.e.setCustomNameVisible(true);
        this.e.setSmall(is_Small);
        this.addLine("Spawning in 0");
        Main.game.setFunctionEntity(this.e, new Game.click() {
            @Override
            public void run(Player player) {}});
        (new BukkitRunnable() {
            double ticks = 0.0D;
            Location loc = Forge.this.e.getLocation();

            public void run() {
                if (Forge.this.deleted)
                    cancel();
                this.ticks++;
                if(Forge.this.spawner!=null){
                    Forge.this.editLine(Forge.this.spawner.getNextDrop()+" Seconds");
                }
                double change = Math.cos(this.ticks / 10.0D);
                PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook packet = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(Forge.this.e.getEntityId(), ((short)0), (byte)(int)(change * 2.0D), ((short)0), (byte)(int)(this.loc.getYaw() + 0.0D), (byte)(int)((this.loc.getPitch() + 180.0D) / 360.0D), true);
                this.loc.setYaw(this.loc.getYaw() + 7.0F);
                this.loc.setY(change);
                for(Player player : Bukkit.getOnlinePlayers()){
                    ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
                }
            }
        }).runTaskTimerAsynchronously(plugin, 0L, 1L);
        }

        public void setSpawner (Game.spawnner spawner){
            this.spawner=spawner;
        }

        public void addLine(String line) {
            Location l1 = e.getLocation().clone();
            l1.add(0,.5,0);
            this.e1 = (ArmorStand) Bukkit.getWorld(e.getLocation().getWorld().getName()).spawnEntity(l1, EntityType.ARMOR_STAND);
            this.e1.setVisible(false);
            this.e1.setGravity(false);
            this.e1.teleport(l1);
            this.e1.setCanPickupItems(false);
            this.e1.setRemoveWhenFarAway(false);
            this.e1.setCustomName(Text.format(line));
            this.e1.setCustomNameVisible(true);
        }
        public void editLine(String line) {
            this.e1.setCustomName(Text.format(line+" &a[UP]"));
        }
}
