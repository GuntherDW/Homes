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

import com.guntherdw.bukkit.Homes.Home;
import com.guntherdw.bukkit.Homes.Homes;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author GuntherDW
 */
public class CommandSetHome implements iCommand {
    public boolean executeCommand(CommandSender sender, String cmd, List<String> args, Homes plugin) {
        if(sender instanceof Player)
        {
            Player player = (Player) sender;

            if(plugin.check(player, "sethome"))
            {
                Home home = new Home(player.getName(), player.getLocation());

                plugin.getDataSource().saveHome(home);

                // player.sendMessage("INTO homes (x,y,z,rotX,rotY,world,name) VALUES (?,?,?,?,?,?,?)");
                player.sendMessage(ChatColor.GREEN + "Successfully set your home!");
                plugin.getHomesMap().put(player.getName().toLowerCase(), home);
                plugin.getLogger().info("[Homes] "+player.getName()+" set his home at location world:"+home.getWorld()+" x:"+home.getX()+" y:"+home.getY()+" z:"+home.getZ());
            } else {
                player.sendMessage("You don't have permission to use /sethome!");
            }
        } else {
            sender.sendMessage("You need to be a player to set your home!");
        }
        return true;
    }

    public String getPermissionSuffix() {
        return "sethome";
    }
}
