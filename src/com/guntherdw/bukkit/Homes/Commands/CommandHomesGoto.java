/*
 * Copyright (c) 2012 GuntherDW
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.guntherdw.bukkit.Homes.Commands;

import com.guntherdw.bukkit.Homes.Homes;
import com.guntherdw.bukkit.Homes.SaveHome;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author GuntherDW
 */
public class CommandHomesGoto implements iCommand {
    public boolean gotoHomes(CommandSender sender, String cmd, List<String> args, Homes plugin, boolean skipfirst) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.YELLOW+"Why would you even TRY?");
            return true;
        }
        Player player = (Player) sender;
        
        if(!plugin.check(player, "homes")) {
            player.sendMessage("You don't have permission to use homes!");
            return true;
        }
        String searchstr = "";
        for(int x = skipfirst?0:1; x<args.size(); x++) {
            searchstr+= args.get(x)+" ";
        }
        if(searchstr.length()>0) searchstr = searchstr.trim();
        SaveHome searchhome = plugin.matchHome(player.getName(), searchstr);

        if(searchhome!=null) {

            sender.sendMessage(ChatColor.GREEN+"Teleporting you to Home '"+searchhome.getDescription()+"'!");
            plugin.getLogger().info(""+player.getName()+" teleported to home with name "+searchhome.getDescription());
            if(plugin.getTweakcraftutils()!= null) {
                if(!plugin.savehomesTCUtils.contains(player.getName())) {
                    plugin.getTweakcraftutils().getTelehistory().addHistory(player.getName(), player.getLocation());
                }
            }
            Location loc = searchhome.getLocation();
            player.teleport(loc);

            return true;
        } else {
            sender.sendMessage(ChatColor.GREEN + "No homes by that name found!");
            return false;
        }
    }

    public boolean executeCommand(CommandSender sender, String cmd, List<String> args, Homes plugin) {
        if(args.size()==0) { sender.sendMessage(ChatColor.YELLOW+"What?"); return true; }
        else gotoHomes(sender, cmd, args, plugin, true);

        return true;
    }

    public String getPermissionSuffix() {
        return "homes.homes";
    }
}
