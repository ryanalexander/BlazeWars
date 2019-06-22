package com.stelch.games2.BlazeWars.commands;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.core.Lang.en;
import com.stelch.games2.core.PlayerUtils.BukkitGamePlayer;
import com.stelch.games2.core.Utils.Text;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class mct implements CommandExecutor {

    Main plugin;
    public mct(Main main){
        plugin=main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if((sender instanceof Player)&& BukkitGamePlayer.getGamePlayer(sender.getName()).getRank().getLevel()<10){sender.sendMessage(Text.format(en.PERM_NO_PERMISSION));return false;}
        if(!(sender instanceof Player)){sender.sendMessage("You must be a player to execute this command.");return false;}

        Player player = (Player)sender;
        Location loc = player.getLocation();
        if(args.length<1){
            sender.sendMessage(new String[]{
                    Text.format("&d----[ &aMap Creator Tools &d]----"),
                    Text.format("&eThis tool was created for Map Creators to"),
                    Text.format("&ehave the ability to specify custom map parameters"),
                    " ",
                    Text.format("&e- &a/mct set &d{spawn/forge/blaze/core} {team/id}"),
                    Text.format("&e- &a/mct add &d{forge} {mid}"),
                    Text.format("&e- &a/mct remove &d{spawn/forge/blaze/core} {team/id}"),
                    "",
                    Text.format("&e- &a/mct list &d{spawner/blaze/core}")

            });
            return false;
        }

        switch(args[0]){
            case "set":
                plugin.getConfig().set(String.format("maps.%s.%s.%s.x",loc.getWorld().getName(),args[1].toLowerCase(),args[2].toUpperCase()),Double.parseDouble(""+loc.getX()));
                plugin.getConfig().set(String.format("maps.%s.%s.%s.y",loc.getWorld().getName(),args[1].toLowerCase(),args[2].toUpperCase()),Double.parseDouble(""+loc.getY()));
                plugin.getConfig().set(String.format("maps.%s.%s.%s.z",loc.getWorld().getName(),args[1].toLowerCase(),args[2].toUpperCase()),Double.parseDouble(""+loc.getZ()));
                plugin.getConfig().set(String.format("maps.%s.%s.%s.yaw",loc.getWorld().getName(),args[1].toLowerCase(),args[2].toUpperCase()),Double.parseDouble(""+loc.getYaw()));
                plugin.saveConfig();
                sender.sendMessage(Text.format(String.format("&aMCT> &7Saved &e%s&7 team &e%s&7 location at x:%s y:%s z:%s",args[2].toUpperCase(),args[1],loc.getX(),loc.getY(),loc.getZ())));
                break;
            case "remove":
                sender.sendMessage(Text.format("&aMCT> The &eremove&r command is under construction"));
                sender.sendMessage(Text.format(String.format("&aMCT> &7Removed &e%s&7 location at x:%s y:%s z:%s",args[1],loc.getX(),loc.getY(),loc.getZ())));
                break;
            case "add":
                List<String> list = plugin.getConfig().getStringList(String.format("maps.%s.%s.list",((Player) sender).getLocation().getWorld(),args[1].toLowerCase()));
                int id = list.size()+1;
                list.set(id,loc.toString());
                sender.sendMessage(Text.format(String.format("&aMCT> &7Saved &e%s&7 id &e%s&7 location at x:%s y:%s z:%s",args[1].toUpperCase(),id,loc.getX(),loc.getY(),loc.getZ())));
                break;
            case "list":
                sender.sendMessage(Text.format("&aMCT> The &elist&r command is under construction"));
                break;
            case "spawnec":

                break;
            default:
                sender.sendMessage(Text.format("&aMCT> &fThe command specified does not exist."));
        }
        return false;
    }
}
