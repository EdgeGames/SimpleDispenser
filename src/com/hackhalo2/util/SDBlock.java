package com.hackhalo2.util;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

public class SDBlock extends BlockListener {
    
    private SimpleDispenser plugin;
    
    public SDBlock(final SimpleDispenser p) {
	this.plugin = p;
    }
    
    @Override
    public void onSignChange(SignChangeEvent e) {
	Player p = e.getPlayer();
	Block b = e.getBlock();
	Chunk c = b.getChunk();
	String s = "";
	if(e.getLine(0).equalsIgnoreCase("Debug")) e.setLine(0, "Debug");
	if(e.getLine(1).equalsIgnoreCase("Item ID:")) {
	    if(!e.getLine(3).isEmpty()) { s = ":"+e.getLine(3); }
	    e.setLine(1, "Item ID:");
	    if(e.getPlayer().isOp() || SimpleDispenser.Permissions.has(p, "dispenser.create")) {
		plugin.log.info("["+plugin.pdf.getName()+"] Dispenser created at {["+p.getWorld().getName()+"], "+p.getName()+":["+
			b.getX()+","+b.getY()+","+b.getZ()+"]"+s+"}");
		p.sendMessage(ChatColor.DARK_GREEN+"Dispenser Created");
	    } else {
		plugin.log.info("["+plugin.pdf.getName()+"] Dispenser attempted at {["+p.getWorld().getName()+"], "+p.getName()+":["+
			b.getX()+","+b.getY()+","+b.getZ()+"]"+s+"}");
		e.setLine(0, "You Don't");
		e.setLine(1, "Have Permission");
		e.setLine(2, "To Make A");
		e.setLine(3, "Dispenser");
		p.getWorld().refreshChunk(c.getX(), c.getZ());
	    }
	} else if(e.getLine(1).equalsIgnoreCase("Loadout:")) {
	    e.setLine(1, "Loadout:");
	    if(e.getPlayer().isOp() || SimpleDispenser.Permissions.has(p, "dispenser.create")) {
		plugin.log.info("["+plugin.pdf.getName()+"] Dispenser created at {["+p.getWorld().getName()+"], "+p.getName()+":["+
			b.getX()+","+b.getY()+","+b.getZ()+"]"+s+"}");
		p.sendMessage(ChatColor.DARK_GREEN+"Dispenser Created");
	    } else {
		plugin.log.info("["+plugin.pdf.getName()+"] Dispenser attempted at {["+p.getWorld().getName()+"], "+p.getName()+":["+
			b.getX()+","+b.getY()+","+b.getZ()+"]"+s+"}");
		e.setLine(0, "You Don't");
		e.setLine(1, "Have Permission");
		e.setLine(2, "To Make A");
		e.setLine(3, "Dispenser");
		p.getWorld().refreshChunk(c.getX(), c.getZ());
	    }
	}
    }
}
