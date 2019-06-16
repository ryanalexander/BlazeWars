package com.stelch.games2.BlazeWars.commands;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.Utils.Spectator;
import com.stelch.games2.BlazeWars.Utils.text;
import com.stelch.games2.BlazeWars.varables.gameState;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class admin implements CommandExecutor {

    Main plugin;
    public admin(Main main){
        plugin=main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if(!(sender instanceof Player)){sender.sendMessage("You must be a player to execute this command.");return false;}
        Player player = (Player)sender;
        if(args.length<1){
            sender.sendMessage(new String[]{
                    text.f("&d----[ &cAdmin &d]----"),
                    " ",
                    text.f("&e- &a/admin game &d{start/stop/state}"),

            });
            return false;
        }
        if(args[0].equalsIgnoreCase("game")){
            switch(args[1]){
                case "stop":
                    if(Main.game.getGamestate()==gameState.IN_GAME){
                        Main.game.stop();
                    }else {
                        player.sendMessage(text.f("&aADMIN> &7There is no current game in progress."));
                    }
                    break;
                case "start":
                    if(Main.game.getGamestate()==gameState.LOBBY){
                        Main.game.start();
                    }else {
                        player.sendMessage(text.f("&aADMIN> &7There is already a game in progress."));
                    }
                    break;
                case "state":
                    player.sendMessage(text.f(String.format("&aADMIN> &7The current GameState is \"&e%s&7\".",Main.game.getGamestate())));
                    break;
                case "specator":
                    player.sendMessage(text.f(String.format("&aAdmin> &7You have now %s&7 flight",((Main.game.spectators.containsKey(player)?"&cdisabled":"&aenabled")))));
                    if(Main.game.spectators.containsKey(player)){
                        Main.game.spectators.get(player).leave();
                    }else {
                        new Spectator(player);
                    }
                    break;
                default:
                    sender.sendMessage(text.f("&aADMIN> &fThe command specified does not exist."));
            }
            return false;
        }
        return false;
    }
}
