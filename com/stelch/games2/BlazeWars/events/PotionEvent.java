package com.stelch.games2.BlazeWars.events;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.core.Utils.Text;
import net.minecraft.server.v1_14_R1.ChatMessageType;
import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PotionEvent implements Listener {

    @EventHandler
    public void PotionEvent (EntityPotionEffectEvent e){
        e.setCancelled(true);

        if(e.getNewEffect().getType().equals(PotionEffectType.INVISIBILITY)){
            ArmorStand as = (ArmorStand) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.ARMOR_STAND);
            as.setVisible(false);
            as.setItemInHand(((Player)e.getEntity()).getItemOnCursor());
            Main.game.invis_players.put((Player)e.getEntity(),as);
            for(Player player : Bukkit.getOnlinePlayers()){
                player.hidePlayer(Main.getPlugin(Main.class),(Player)e.getEntity());
            }
            new BukkitRunnable(){
                int counter = 1;
                @Override
                public void run() {
                    counter=counter+1;
                    CraftPlayer player = (CraftPlayer) e.getEntity();

                    IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" +Text.format(String.format("&aInvisible for &6%s&as.",30-(counter/20)))+ "\"}");
                    PacketPlayOutChat packet = new PacketPlayOutChat(comp, ChatMessageType.GAME_INFO);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                    if((30-(counter/20))<=0||(!Main.game.invis_players.containsKey(player))){cancel();}
                }
            }.runTaskTimer(Main.getPlugin(Main.class),0,1);
            new BukkitRunnable(){
                @Override
                public void run() {
                    if(Main.game.invis_players.containsKey((Player)e.getEntity())) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.showPlayer(Main.getPlugin(Main.class), (Player) e.getEntity());
                            Main.game.invis_players.get((Player)e.getEntity()).remove();
                        }
                        Main.game.invis_players.remove((Player) e.getEntity());
                    }
                }
            }.runTaskLater(Main.getPlugin(Main.class),(30*20));
        }
    }

}