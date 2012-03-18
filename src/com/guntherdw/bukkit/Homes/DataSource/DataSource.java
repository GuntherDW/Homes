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

package com.guntherdw.bukkit.Homes.DataSource;

import com.guntherdw.bukkit.Homes.Home;
import com.guntherdw.bukkit.Homes.SaveHome;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * @author GuntherDW
 */
public abstract class DataSource {

    public abstract void saveHome(Object homeObject);

    public Home getHome(Player player)
    {
        return this.getHome(player.getName());
    }

    public abstract Home getHome(String player);

    public abstract Map<String, Home> getHomesMap();

    public abstract List<Home> getHomes();

    public abstract List<SaveHome> getSaveHomes();

    public List<SaveHome> getSaveHomes(Player player) {
        return this.getSaveHomes(player.getName());
    }

    public abstract SaveHome getSaveHome(String player, int savehomeid);

    public abstract List<SaveHome> getSaveHomes(String player);

    public abstract boolean deleteHome(String player, int savehomeid);

    public abstract boolean addSavehome(String player, SaveHome savehome);
    public abstract boolean addHome(String player, Home home);

}
