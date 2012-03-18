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

import com.guntherdw.bukkit.Homes.Commands.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author GuntherDW
 */
public class CommandHandler {
    public Map<String, iCommand> commandMap = new HashMap<String, iCommand>();
    private Homes plugin;

    public CommandHandler(Homes instance) {
        this.plugin = instance;
        commandMap.clear();
        commandMap.put("home", new CommandHome());
        commandMap.put("hg", new CommandHomesGoto());
        commandMap.put("homes", new CommandHomes());
        commandMap.put("sethome", new CommandSetHome());
        commandMap.put("reloadhomes", new CommandReloadHomes());
    }

    public void addCommand(String cmd, iCommand command) {
        this.addCommand(cmd, command, false);
    }

    public void addCommand(String cmd, iCommand command, boolean override) {
        if(override || !this.commandMap.containsKey(cmd)) {
            commandMap.put(cmd, command);
            /* if(this.plugin.getServer().getPluginCommand(cmd)==null) {
                this.plugin.getServer().
            } */
        }
    }

    public Homes getPlugin() {
        return plugin;
    }

    public Map<String, iCommand> getCommandMap() {
        return commandMap;
    }

    public iCommand getCommand(String command) {
        if (commandMap.containsKey(command)) {
            return commandMap.get(command);
        } else {
            return null;
        }
    }

}
