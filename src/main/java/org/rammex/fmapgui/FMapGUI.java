package org.rammex.fmapgui;

import org.bukkit.plugin.java.JavaPlugin;
import org.rammex.fmapgui.commands.FMapCommand;
import org.rammex.fmapgui.commands.FMapReloadCommand;
import org.rammex.fmapgui.events.FMapClickEvent;

public final class FMapGUI extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("fmap").setExecutor(new FMapCommand(this));
        this.getCommand("fmapreload").setExecutor(new FMapReloadCommand(this));

        getLogger().info("FMapGUI has been enabled!");
        getLogger().info("Made by .rammex <3");

        this.getServer().getPluginManager().registerEvents(new FMapClickEvent(this), this);
        this.saveDefaultConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
