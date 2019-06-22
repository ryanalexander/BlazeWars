package com.stelch.games2.BlazeWars.events;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.varables.GameReason;
import com.stelch.games2.BlazeWars.varables.gameState;
import com.stelch.games2.core.Lang.en;
import com.stelch.games2.core.PlayerUtils.BukkitGamePlayer;
import com.stelch.games2.core.Utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandPreProcessEvent implements Listener {
    @EventHandler
    public void CommandPreProcessEvent(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().startsWith("/rl") || e
                .getMessage().startsWith("/reload") || e
                .getMessage().startsWith("/stop") && Main.game.getGamestate()==  gameState.IN_GAME) {
            if((e.getPlayer() instanceof Player)&& BukkitGamePlayer.getGamePlayer(e.getPlayer().getName()).getRank().getLevel()<10){e.getPlayer().sendMessage(Text.format(en.PERM_NO_PERMISSION));return;}

            e.getPlayer().sendMessage(Text.format("&aWarn> &7That command has been delayed by the Mini-Games"));
            e.setCancelled(true);
            Main.game.stop(GameReason.ADMINISTARTOR);
            Bukkit.getServer().reload();
            Bukkit.getServer().reloadData();
            e.getPlayer().sendMessage(Text.format("&aServer> &7The server has been reloaded"));
        }
    }
}
