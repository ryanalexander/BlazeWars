package com.stelch.games2.BlazeWars.commands;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.Utils.text;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mct implements CommandExecutor {

    Main plugin;
    public mct(Main main){
        plugin=main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if(!(sender instanceof Player)){sender.sendMessage("You must be a player to execute this command.");return false;}
        Player player = (Player)sender;
        Location loc = player.getLocation();
        if(args.length<1){
            sender.sendMessage(new String[]{
                    text.f("&d----[ &aMap Creator Tools &d]----"),
                    text.f("&eThis tool was created for Map Creators to"),
                    text.f("&ehave the ability to specify custom map parameters"),
                    " ",
                    text.f("&e- &a/mct set &d{spawner/blaze/core} {team/id}"),
                    text.f("&e- &a/mct remove &d{spawner/blaze/core} {team/id}"),
                    "",
                    text.f("&e- &a/mct list &d{spawner/blaze/core}")

            });
        }

        switch(args[0]){
            case "set":
                sender.sendMessage(text.f("&aMCT> The &eset&r command is under construction"));
                plugin.getConfig().set(String.format("maps.%s.%s",args[1],args[2]),loc.toString());
                sender.sendMessage(text.f(String.format("&aMCT> &7Saved &e%s&7 location at x:%s y:%s z:%s",args[1],loc.getX(),loc.getY(),loc.getZ())));
                break;
            case "remove":
                sender.sendMessage(text.f("&aMCT> The &eremove&r command is under construction"));
                sender.sendMessage(text.f(String.format("&aMCT> &7Removed &e%s&7 location at x:%s y:%s z:%s",args[1],loc.getX(),loc.getY(),loc.getZ())));
                break;
            case "list":
                sender.sendMessage(text.f("&aMCT> The &elist&r command is under construction"));
                break;
            default:
                sender.sendMessage(text.f("&aMCT> &fThe command specified does not exist."));
        }
        return false;
    }
}
