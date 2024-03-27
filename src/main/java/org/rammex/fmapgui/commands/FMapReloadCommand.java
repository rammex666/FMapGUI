package org.rammex.fmapgui.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.rammex.fmapgui.FMapGUI;

public class FMapReloadCommand implements CommandExecutor {
    FMapGUI plugin;
    public FMapReloadCommand(FMapGUI plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        this.plugin.reloadConfig();
        return false;
    }
}
