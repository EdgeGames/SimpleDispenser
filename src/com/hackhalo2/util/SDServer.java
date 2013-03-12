package com.hackhalo2.util;

import com.nijikokun.bukkit.Permissions.Permissions;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

public class SDServer extends ServerListener {
    
    private SimpleDispenser plugin;
    private boolean permissionsFail = false;
    
    public SDServer(final SimpleDispenser p) {
	this.plugin = p;
    }
    
    public void onPluginEnable(PluginEnableEvent e) {
	Plugin test = plugin.getServer().getPluginManager().getPlugin("Permissions");
	if(plugin.permissionsEnabled == false && test != null) { //Permissions check
	    permissionsFail = false;
	    if(SimpleDispenser.Permissions == null) {
		plugin.permissionsType = test.getDescription().getFullName();
		SimpleDispenser.Permissions = ((Permissions)test).getHandler();
		plugin.log.info("[SimpleDispenser] Hooking into Permissions");
		plugin.permissionsEnabled = true;
		test = null;
	    } 
	} else if(plugin.permissionsEnabled != true && permissionsFail != true) {
	    permissionsFail = true;
	    plugin.log.warning("[SimpleDispenser] Permissions not found, defaulting to ops.txt");
	    plugin.permissionsEnabled = false;
	}
    }

    @Override
    public void onPluginDisable(PluginDisableEvent e) {
	Plugin test = plugin.getServer().getPluginManager().getPlugin("Permissions");
	String name = e.getPlugin().getDescription().getName();

	if((plugin.permissionsEnabled != false && SimpleDispenser.Permissions != null && test != null)) {
	    if(name.equals("Permissions")) {
		plugin.log.info("[SimpleDispenser] Unhooking Permissions");
		SimpleDispenser.Permissions = null;
		plugin.permissionsEnabled = false;
	    }
	}
    }

}
