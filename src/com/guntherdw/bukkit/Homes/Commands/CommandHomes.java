package com.guntherdw.bukkit.Homes.Commands;

import com.guntherdw.bukkit.Homes.Home;
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
public class CommandHomes implements iCommand {
    // player.sendMessage(ChatColor.GREEN + "Usage: /homes add <alias>|del <alias>|use <alias>|list|tpback");

    private enum homesCommand {
        USE,
        ADD,
        DELETE,
        LIST,
        GOTO,
        TPBACK
    }

    public boolean executeCommand(CommandSender sender, String cmd, List<String> args, Homes plugin) {

        if(!(sender instanceof Player)) {
            sender.sendMessage("You need to be a player to use homes");
            return true;
        }

        Player player = (Player) sender;
        
        if(!plugin.check(player, "homes"))
        {
            player.sendMessage("You don't have permission to use homes!");
            return true;
        }
        String name = "";
        homesCommand hcmd = null;
        if (args.size() >= 2) {
            for (int i = 1; i < args.size(); i++)
                name += args.get(i) + " ";
            name = name.trim();
        }


        boolean skipfirst = false;

        if (args.size() >= 1) {
            String mode = args.get(0).toLowerCase();
            if(mode.equals("use")) hcmd = homesCommand.USE;
            else if(mode.equals("add")) hcmd = homesCommand.ADD;
            else if(mode.equals("remove")) hcmd = homesCommand.DELETE;
            else if(mode.equals("delete")) hcmd = homesCommand.DELETE;
            else if(mode.equals("list")) hcmd = homesCommand.LIST;
            else if(mode.equals("goto")) hcmd = homesCommand.GOTO;
            else if(mode.equals("tpback")) hcmd = homesCommand.TPBACK;
            else { hcmd = homesCommand.USE; skipfirst = true; }
        }

        if(hcmd==null) {
            player.sendMessage(ChatColor.GREEN + "Usage: /homes add <alias>|del <alias>|use <alias>|goto <alias>|list|tpback");
            return true;
        }

        switch(hcmd) {
            case USE:
                this.useHomes(sender, cmd, args, plugin, skipfirst);
                break;
            case ADD:
                this.addHomes(sender, cmd, args, plugin, skipfirst);
                break;
            case DELETE:
                this.delHomes(sender, cmd, args, plugin, skipfirst);
                break;
            case LIST:
                this.listHomes(sender, cmd, args, plugin, skipfirst);
                break;
            case GOTO:
                this.gotoHomes(sender, cmd, args, plugin, skipfirst);
                break;
            case TPBACK:
                this.tpBackToggle(sender, cmd, args, plugin, skipfirst);
                break;
        }

        return true;
    }

    public String getPermissionSuffix() {
        return "homes";
    }

    public boolean addHomes(CommandSender sender, String cmd, List<String> args, Homes plugin, boolean skipfirst){
        String searchstr = "";
        for(int x = skipfirst?0:1; x<args.size(); x++) {
            searchstr+= args.get(x)+" ";
        }
        if(searchstr.length()>0) searchstr = searchstr.trim();
        String nameEscaped = searchstr.replaceAll("%", "\\%").replaceAll("_", "\\_");
        SaveHome searchhome = plugin.getDatabase().find(SaveHome.class).where().ieq("name", ((Player) sender).getName()).ieq("description", searchstr).findUnique();
        if(searchhome==null) { searchhome = new SaveHome(); searchhome.setName(((Player)sender).getName()); searchhome.setDescription(searchstr);}
        searchhome.parseLocation(((Player)sender).getLocation());
        plugin.getDatabase().save(searchhome);

        sender.sendMessage(ChatColor.GREEN + "Home '" + searchhome.getDescription() + "' saved.");
        return true;
    }
    public boolean useHomes(CommandSender sender, String cmd, List<String> args, Homes plugin, boolean skipfirst){
        String searchstr = "";
        for(int x = skipfirst?0:1; x<args.size(); x++) {
            searchstr+= args.get(x)+" ";
        }
        if(searchstr.length()>0) searchstr = searchstr.trim();
        String nameEscaped = searchstr.replaceAll("%", "\\%").replaceAll("_", "\\_");
        Home home = plugin.getDatabase().find(Home.class).where().ieq("name", ((Player)sender).getName()).findUnique();
        if(home==null) { home = new Home(); home.setName(((Player)sender).getName()); }
        List<SaveHome> searchhomelist = plugin.getDatabase().find(SaveHome.class).where().ieq("name", ((Player) sender).getName()).like("description", nameEscaped + "%").findList();
        SaveHome searchhome=null;
        if(searchhomelist.size()>1) {
            String msg = ChatColor.LIGHT_PURPLE + "Found multiple results: ";
            for(SaveHome svh : searchhomelist)
                msg+=svh.getDescription()+", ";
            msg=msg.substring(0, msg.length()-2);

            sender.sendMessage(msg);
            return true;
        } else if(searchhomelist.size()==1) {
            searchhome = searchhomelist.get(0);
        } else {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "No home found for: " + searchstr);
            return true;
        }

        home.parseHome(searchhome);
        plugin.getDatabase().save(home);
        plugin.getHomesMap().put(((Player)sender).getName().toLowerCase(), home);

        sender.sendMessage(ChatColor.GREEN + "Home '" + searchhome.getDescription() + "' loaded. It's your /home now!");

        return true;
    }
    public boolean gotoHomes(CommandSender sender, String cmd, List<String> args, Homes plugin, boolean skipfirst){
        String searchstr = "";
        for(int x = skipfirst?0:1; x<args.size(); x++) {
            searchstr+= args.get(x)+" ";
        }
        if(searchstr.length()>0) searchstr = searchstr.trim();
        String nameEscaped = searchstr.replaceAll("%", "\\%").replaceAll("_", "\\_");
        List<SaveHome> searchhomelist = plugin.getDatabase().find(SaveHome.class).where().ieq("name", ((Player) sender).getName()).like("description", nameEscaped + "%").findList();
        SaveHome searchhome=null;
        if(searchhomelist.size()>1) {
            String msg = ChatColor.LIGHT_PURPLE + "Found multiple results: ";
            for(SaveHome svh : searchhomelist)
                msg+=svh.getDescription()+", ";
            msg=msg.substring(0, msg.length()-2);

            sender.sendMessage(msg);
            return true;
        } else if(searchhomelist.size()==1) {
            searchhome = searchhomelist.get(0);
        } else {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "No home found for: " + searchstr);
            return true;
        }

        sender.sendMessage(ChatColor.GREEN+"Teleporting you to Home '"+searchhome.getDescription()+"'!");
        if(plugin.getTweakcraftutils()!= null) {
            if(!plugin.savehomesTCUtils.contains(((Player)sender).getName())) {
                plugin.getTweakcraftutils().getTelehistory().addHistory(((Player)sender).getName(), ((Player)sender).getLocation());
            }
        }
        Location loc = searchhome.toLocation();
        ((Player)sender).teleport(loc);

        return true;
    }
    public boolean delHomes(CommandSender sender, String cmd, List<String> args, Homes plugin, boolean skipfirst){
        String searchstr = "";
        for(int x = skipfirst?0:1; x<args.size(); x++) {
            searchstr+= args.get(x)+" ";
        }
        if(searchstr.length()>0) searchstr = searchstr.trim();
        String nameEscaped = searchstr.replaceAll("%", "\\%").replaceAll("_", "\\_");
        // Home home = plugin.getDatabase().find(Home.class).where().ieq("name", ((Player)sender).getName()).findUnique();
        // if(home==null) { home = new Home(); home.setName(((Player)sender).getName()); }
        List<SaveHome> searchhomelist = plugin.getDatabase().find(SaveHome.class).where().ieq("name", ((Player) sender).getName()).like("description", nameEscaped + "%").findList();
        if(searchhomelist.size()>1) {
            String msg = ChatColor.LIGHT_PURPLE + "Found multiple results: ";
            for(SaveHome svh : searchhomelist)
                msg+=svh.getDescription()+", ";
            msg=msg.substring(0, msg.length()-2);
            sender.sendMessage(msg);
            return true;
        } else if(searchhomelist.size()==1) {
            SaveHome sh = searchhomelist.get(0);
            plugin.getDatabase().delete(SaveHome.class, sh.getId());
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Home '"+sh.getDescription()+"' deleted!");
        } else {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "No home found for: " + searchstr);
            return true;
        }
        return true;
    }
    public boolean listHomes(CommandSender sender, String cmd, List<String> args, Homes plugin, boolean skipfirst){
        String msg = ChatColor.GREEN.toString();
        List<SaveHome> searchhomelist = plugin.getDatabase().find(SaveHome.class).where().ieq("name", ((Player) sender).getName()).findList();
        if (searchhomelist.size()>0) {
            msg += "Your homes: " + ChatColor.WHITE;

            for(SaveHome svh : searchhomelist)
                msg += svh.getDescription()+", ";
            msg=msg.substring(0, msg.length()-2);

        } else {
            msg += "No homes found";
        }
        sender.sendMessage(msg);
        return true;
    }
    public boolean tpBackToggle(CommandSender sender, String cmd, List<String> args, Homes plugin, boolean skipfirst){
        if(plugin.checkFull((Player) sender, "tweakcraftutils.tpback")) {
            if(plugin.savehomesTCUtils.contains(((Player)sender).getName())) {
                plugin.savehomesTCUtils.remove(((Player)sender).getName());
                sender.sendMessage(ChatColor.GOLD+"Going home will set a tpback entry!");
            } else {
                plugin.savehomesTCUtils.add(((Player)sender).getName());
                sender.sendMessage(ChatColor.GOLD+"Going home will no longer set a tpback entry!");
            }
        } else {
            sender.sendMessage("You don't have permission to tpback, so this would be useless!");
        }
        return true;
    }

}
