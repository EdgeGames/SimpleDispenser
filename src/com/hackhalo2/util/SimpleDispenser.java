package com.hackhalo2.util;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import com.nijiko.permissions.PermissionHandler;

public class SimpleDispenser extends JavaPlugin {
    
    //The Various Listeners
    private SDPlayer pListener = new SDPlayer(this);
    private SDBlock bListener = new SDBlock(this);
    private SDServer sListener = new SDServer(this);
    
    //The Logger
    public Logger log;
    
    //Permissions Stuff
    public String permissionsType; //name of the hooked Permissions plugin
    public static PermissionHandler Permissions = null; //for Permissions
    public boolean permissionsEnabled = false; //enabled boolean
    
    //Other various crap
    public PluginDescriptionFile pdf;

    @Override
    public void onDisable() {
	log.info("["+pdf.getName()+"] Disabled");
    }

    @Override
    public void onEnable() {
	log = this.getServer().getLogger();
	pdf = this.getDescription();
	PluginManager pm = this.getServer().getPluginManager();
	pm.registerEvent(org.bukkit.event.Event.Type.PLUGIN_ENABLE, sListener, org.bukkit.event.Event.Priority.Monitor, this);
	pm.registerEvent(org.bukkit.event.Event.Type.PLUGIN_DISABLE, sListener, org.bukkit.event.Event.Priority.Monitor, this);
        pm.registerEvent(org.bukkit.event.Event.Type.PLAYER_INTERACT, pListener, org.bukkit.event.Event.Priority.Normal, this);
        pm.registerEvent(org.bukkit.event.Event.Type.SIGN_CHANGE, bListener, org.bukkit.event.Event.Priority.Normal, this);
        log.info("["+pdf.getName()+"] Enabled");
    }

}
