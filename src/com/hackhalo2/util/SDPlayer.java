package com.hackhalo2.util;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SDPlayer extends PlayerListener {
    //The Arena Dispenser Globals
    private final int[] HELM = {298, 306, 314, 310, 302};
    private final int[] CHEST = {299, 307, 315, 311, 303};
    private final int[] LEG = {300, 308, 316, 312, 304};
    private final int[] SHOE = {301, 309, 317, 313, 305};
    private final int[] WEAPON = {268, 272, 267, 283, 276};

    private SimpleDispenser plugin;
    private boolean debug = false;

    public SDPlayer(final SimpleDispenser p) {
	this.plugin = p;
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent e) {
	if(e.getAction().toString().equals("LEFT_CLICK_BLOCK")) {
	    Player p = e.getPlayer();
	    Block b = e.getClickedBlock();
	    String st = "";
	    Chunk c = b.getChunk();
	    PlayerInventory inv = p.getInventory();
	    if(b.getType().equals(Material.WALL_SIGN) ||
		    b.getType().equals(Material.SIGN) ||
		    b.getType().equals(Material.SIGN_POST)) {
		Sign s = (Sign)b.getState();
		if(s.getLine(0).equalsIgnoreCase("Debug")) { debug = true; }
		if(s.getLine(1).equalsIgnoreCase("Item ID:")) {
		    if(debug) p.sendMessage("Starting Loadout Script...");
		    if(!s.getLine(3).isEmpty()) { 
			st = " with Permission "+ s.getLine(3);
			if(debug) p.sendMessage("Permissions: "+s.getLine(3));
		    }
		    int item = 0;
		    short damage = 0;
		    try {
			if(s.getLine(2).matches("(\\d+)")) {
			    item = Integer.parseInt(s.getLine(2).trim());
			    damage = 0;
			    if(debug) p.sendMessage("No Damage value given, setting to 0");
			} else if(s.getLine(2).matches("(\\d+:\\d+)")) {
			    String[] strBreak = s.getLine(2).split(":");
			    item = Integer.parseInt(strBreak[0].trim());
			    damage = Short.parseShort(strBreak[1].trim());
			    if(debug) p.sendMessage("Damage Value: "+damage);
			} else throw new NumberFormatException();
		    } catch (NumberFormatException ex) {
			if(debug) p.sendMessage("Error caught, Disabing...");
			s.setLine(1, "ERROR");
			plugin.log.info("["+plugin.pdf.getName()+"] Dispenser disabled at {["+p.getWorld().getName()+"], "+p.getName()+":["+
				b.getX()+","+b.getY()+","+b.getZ()+"]"+st+"}");
			p.getWorld().refreshChunk(c.getX(), c.getZ());
			return;
		    }
		    if(inv.firstEmpty() == -1) {
			p.sendMessage("Your inventory is full. Please clear some space, and try again.");
			if(debug) p.sendMessage("Full Inventory");
		    } else if(s.getLine(3).isEmpty() || 
			    (s.getLine(3).equalsIgnoreCase("journeyman") && SimpleDispenser.Permissions.has(p, "dispenser.journeyman")) || 
			    (s.getLine(3).equalsIgnoreCase("apprentice") && SimpleDispenser.Permissions.has(p, "dispenser.apprentice"))) {
			inv.addItem(new ItemStack[] { new ItemStack(item, 64, damage) });
			if(!s.getLine(3).isEmpty()) { st = " with Permission "+ s.getLine(3); }
			if(debug) p.sendMessage("Item Given Sucessfully");
		    } else {
			p.sendMessage("You do not have the permission to use this Dispenser");
			plugin.log.info("["+plugin.pdf.getName()+"] Tried to Give Item "+item+" to "+p.getName()+st+" Failed; Incorrect Permissions");
			if(debug) p.sendMessage("Incorrect Permissions");
		    }
		    e.setCancelled(true);
		} else if(s.getLine(1).equalsIgnoreCase("Loadout:") && (!s.getLine(2).isEmpty() && !s.getLine(3).isEmpty())) {
		    if(debug) p.sendMessage("Starting to run Loadout script...");
		    int loadout1 = 0;
		    int loadout2 = 0;
		    if(inv.firstEmpty() == -1) {
			p.sendMessage("Your inventory is full. Please clear some space, and try again.");
			return;
		    }
		    
		    //Figure out the first loadout
		    if(s.getLine(2).toLowerCase().startsWith("i")) {
			loadout1 = 1;
		    } else if(s.getLine(2).toLowerCase().startsWith("g")) {
			loadout1 = 2;
		    } else if(s.getLine(2).toLowerCase().startsWith("d")) {
			loadout1 = 3;
		    } else if(s.getLine(2).toLowerCase().startsWith("c")) {
			loadout1 = 4;
		    } else loadout1 = 0; //default to the lowest rank
		    
		    //Figure out the second loadout
		    if(s.getLine(3).toLowerCase().startsWith("s")) {
			loadout2 = 1;
		    } else if(s.getLine(3).toLowerCase().startsWith("i")) {
			loadout2 = 2;
		    } else if(s.getLine(3).toLowerCase().startsWith("g")) {
			loadout2 = 3;
		    } else if(s.getLine(3).toLowerCase().startsWith("d")) {
			loadout2 = 4;
		    } else loadout2 = 0; //default to the lowest rank
		    
		    if(debug) p.sendMessage("Loadouts: "+loadout1+" "+loadout2);
		    
		    if(debug) p.sendMessage("Adding Armour...");
		    //Add the armour
		    inv.setHelmet(new ItemStack(HELM[loadout1], 1, (short)0));
		    inv.setChestplate(new ItemStack(CHEST[loadout1], 1, (short)0));
		    inv.setLeggings(new ItemStack(LEG[loadout1], 1, (short)0));
		    inv.setBoots(new ItemStack(SHOE[loadout1], 1, (short)0));
		    
		    if(debug) p.sendMessage("Adding Weapon");
		    //Add the weapon, or replace the current one
		    for(int i = 0; i < WEAPON.length; i++) {
			if(inv.contains(WEAPON[i])) {
			    if(debug) p.sendMessage("Removing Previous Weapon");
			    inv.remove(new ItemStack(WEAPON[i]));
			    break;
			}
		    }
		    inv.addItem(new ItemStack[] { new ItemStack(WEAPON[loadout2], 1, (short)0) });
		} else if(s.getLine(2).equalsIgnoreCase("Team")) {
		    short damage;
		    if(debug) p.sendMessage("Starting Team Script...");
		    if(s.getLine(1).equalsIgnoreCase("red")) {
			damage = 14;
		    } else if(s.getLine(1).equalsIgnoreCase("blue")) {
			damage = 3;
		    } else if(s.getLine(1).equalsIgnoreCase("yellow")) {
			damage = 4;
		    } else if(s.getLine(1).equalsIgnoreCase("green")) {
			damage = 5;
		    } else {
			if(debug) p.sendMessage("No/unreconized team color, Disabling...");
			s.setLine(2, "ERROR");
			plugin.log.info("["+plugin.pdf.getName()+"] Dispenser disabled at {["+p.getWorld().getName()+"], "+p.getName()+":["+
				b.getX()+","+b.getY()+","+b.getZ()+"]"+st+"}");
			p.getWorld().refreshChunk(c.getX(), c.getZ());
			return;
		    }
		    if(debug) p.sendMessage("Damage value: "+damage);
		    inv.setHelmet(new ItemStack(35, 1, damage));
		    p.sendMessage(ChatColor.GREEN+"Team Set!");
		}
	    }
	    p.getWorld().refreshChunk(c.getX(), c.getZ());
	}
	if(debug) debug = false;
    }
}
