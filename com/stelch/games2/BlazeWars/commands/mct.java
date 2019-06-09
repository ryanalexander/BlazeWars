package com.stelch.games2.BlazeWars.commands;

import com.stelch.games2.BlazeWars.Utils.text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mct implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if(!(sender instanceof Player)){sender.sendMessage("You must be a player to execute this command.");return false;}

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
                break;
            case "remove":
                sender.sendMessage(text.f("&aMCT> The &eremove&r command is under construction"));
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
