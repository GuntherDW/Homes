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
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author GuntherDW
 */
public interface iCommand {
    public abstract boolean executeCommand(CommandSender sender, String cmd, List<String> args, Homes plugin);

    /**
     * Get the command's main permission node, or null if there isn't one
     * This is without the homes. prefix!
     *
     * @return The node
     */
    public abstract String getPermissionSuffix();
}
