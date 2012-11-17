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

package com.guntherdw.bukkit.Homes;
import com.guntherdw.bukkit.Homes.Commands.iCommand;
import com.guntherdw.bukkit.Homes.DataSource.DataSource;
import com.guntherdw.bukkit.Homes.DataSource.Sources.MySQL;
import com.guntherdw.bukkit.tweakcraft.TweakcraftUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Homes extends JavaPlugin {
    public static PermissionManager perm = null;
    private CommandHandler chandler = new CommandHandler(this);
    public Map<String, Home> homes;
    public List<SaveHome> savehomes;
    public List<String> savehomesTCUtils;
    public TweakcraftUtils tweakcraftutils;
    public PluginDescriptionFile pdfFile = null;
    public DataSource ds;
    public boolean usePermissions = false;

    public SaveHome matchHome(String playername, String homename) {
		SaveHome rt = getSavehome(playername, homename);
		if(rt == null) {
			int delta = Integer.MAX_VALUE;
			for(SaveHome sh : savehomes) {
				if(sh.getName().equalsIgnoreCase(playername)
                && sh.getDescription().toLowerCase().contains(homename)
                && Math.abs(sh.getDescription().length() - homename.length()) < delta) {
					rt = sh;
					delta = Math.abs(sh.getDescription().length() - homename.length());
					if(delta == 0) break;
				}
			}
		}

		return rt;
	}

    public SaveHome getSavehome(String player, String description) {
        for(SaveHome sh : ds.getSaveHomes(player)) {
            if(sh.getDescription().toLowerCase().equals(description.toLowerCase())) {
                return sh;
            }
        }
        return null;
    }

    @Override
    public void onDisable() {
        //PluginDescriptionFile pdfFile = this.getDescription();
        getLogger().info("Homes version " + pdfFile.getVersion() + " is disabled!");
    }

    public DataSource getDataSource() {
        return ds;
    }

    public TweakcraftUtils getTweakcraftutils() {
        return tweakcraftutils;
    }

    @Override
    public void onEnable() {
        savehomesTCUtils = new ArrayList<String>();
        pdfFile = this.getDescription();

        // this.setupDatabase();
        this.ds = new MySQL(this);
        this.reloadHomes();
        this.reloadSavehomes();
        usePermissions = this.getConfig().getBoolean("usePermissions", false);
        if(usePermissions) {
            getLogger().info("Using PermissionsEx for permission.");
            this.setupPermissions();
        } else {
            getLogger().info("Using DinnerPerms for permission.");
        }
        this.setupTCUtils();
        getLogger().info(pdfFile.getName()+" version " + pdfFile.getVersion() + " is enabled!");
    }

    public Map<String, Home> getHomesMap() {
        return homes;
    }

    public void setupPermissions() {
        if(this.usePermissions) {
            Plugin plugin = this.getServer().getPluginManager().getPlugin("PermissionsEx");

            if (perm == null) {
                perm = PermissionsEx.getPermissionManager();
            }
        }
    }

    public void setupTCUtils() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("TweakcraftUtils");

        if (tweakcraftutils == null) {
            if (plugin != null) {
                tweakcraftutils = (TweakcraftUtils) plugin;
            }
        }
    }


    public void reloadHomes() {
        homes = new HashMap<String, Home>();

        homes = this.ds.getHomesMap();
        getLogger().info("[Loaded " + homes.size() + " homes!");
    }

    public void reloadHomes(Player p) {
        Home h = this.ds.getHome(p.getName());
        if(h!=null) {
            homes.put(h.getName().toLowerCase(), h);
        }
        getLogger().info("Loaded " + p.getName() +"'s new home!");

    }

    public void reloadSavehomes() {
        savehomes = this.ds.getSaveHomes();
        getLogger().info("Loaded " + (savehomes!=null?savehomes.size():"0") + " savehomes!");
    }


    public boolean check(Player player, String permNode) {
        return this.checkFull(player, "homes."+permNode);
    }

    public boolean checkFull(Player player, String permNode) {
        
        if(!usePermissions) {
            return player.isOp() ||
                    player.hasPermission(permNode);
        } else if(usePermissions && perm!=null) {
            return player.isOp() ||
                    perm.has(player, permNode);
        } else {
            return true;
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] unfilteredargs) {

        List<String> argsa = new ArrayList<String>();

        for(String a : unfilteredargs) {
            if(a!=null&& !a.isEmpty() && !a.trim().equals("")) {
                argsa.add(a);
            }
        }

        iCommand IC = chandler.getCommand(command.getName());
        if(IC!=null) {
            /* public abstract boolean executeCommand(CommandSender sender, String cmd, List<String> args, Homes plugin); */
            if(!IC.executeCommand(sender, command.getName(), argsa, this)) {
                sender.sendMessage(ChatColor.RED+"Something went wrong, contact an admin!");
            }
            return true;
        }

        return false;
    }
}