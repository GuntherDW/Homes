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
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author GuntherDW
 */
public class CommandReloadHomes implements iCommand {
    public boolean executeCommand(CommandSender sender, String cmd, List<String> args, Homes plugin) {
        if(sender instanceof Player)
            if(!plugin.check((Player)sender, getPermissionSuffix())) {
                sender.sendMessage(ChatColor.RED + "What do you think you are doing?");
                return true;
            }

        if(args.size()>0) {
            List<Player> ps = plugin.getServer().matchPlayer(args.get(0));
            Player play = null;
            if(ps.size()==1) {
                play = ps.get(0);
            } else {
                sender.sendMessage(ChatColor.RED+"Can't find player!");
            }
            if(play!=null) {
                sender.sendMessage(ChatColor.GREEN + "Reloading "+play.getName()+"'s home!");
                plugin.reloadHomes(play);
            }

        } else {
            sender.sendMessage(ChatColor.GREEN + "Reloading all homes!");
            plugin.reloadHomes();
        }

        return true;
    }

    public String getPermissionSuffix() {
        return "reloadhomes";
    }
}
