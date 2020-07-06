package com.vhbob.excavator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;

public class BreakEvents implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		// Get player item
		if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
			String itemName = p.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
			// Get tool name
			for (String toolType : Excavators.inst.getConfig().getConfigurationSection("tools").getKeys(false)) {
				String toolName = Excavators.inst.getParsedConfigString("tools." + toolType + ".name");
				if (itemName.equalsIgnoreCase(toolName)) {
					// Get area
					int toolL = Excavators.inst.getConfig().getInt("tools." + toolType + ".area.l") - 1;
					int toolW = Excavators.inst.getConfig().getInt("tools." + toolType + ".area.w");
					int toolH = Excavators.inst.getConfig().getInt("tools." + toolType + ".area.h");

					// Get x,y,z
					Location blockLoc = e.getBlock().getLocation();
					int ox = blockLoc.getBlockX();
					int oy = blockLoc.getBlockY();
					int oz = blockLoc.getBlockZ();

					// Get facing vector and blockface
					Vector facingVec = p.getLocation().getDirection();

					// Setup which modifier to use as length
					int startX, stopX, startY, stopY, startZ, stopZ, xMulti = 1, yMulti = 1, zMulti = 1;

					// Setup start and stop values for each
					if (Math.abs(facingVec.getY()) > 0.6) {
						yMulti = (int) Math.round(facingVec.getY());
						startY = 0;
						stopY = toolL;
						startX = toolW / -2;
						stopX = toolW / 2;
						startZ = toolH / -2;
						stopZ = toolH / 2;
					} else if (Math.abs(facingVec.getX()) > 0.6) {
						xMulti = (int) Math.round(facingVec.getX());
						startX = 0;
						stopX = toolL;
						startY = toolW / -2;
						stopY = toolW / 2;
						startZ = toolH / -2;
						stopZ = toolH / 2;
					} else {
						zMulti = (int) Math.round(facingVec.getZ());
						startZ = 0;
						stopZ = toolL;
						startX = toolW / -2;
						stopX = toolW / 2;
						startY = toolH / -2;
						stopY = toolH / 2;
					}

					// Loop thru blocks
					for (int xMod = startX; xMod <= stopX; ++xMod) {
						for (int yMod = startY; yMod <= stopY; ++yMod) {
							for (int zMod = startZ; zMod <= stopZ; ++zMod) {
								Location loc = new Location(blockLoc.getWorld(), ox + xMod * xMulti, oy + yMod * yMulti,
										oz + zMod * zMulti);
								if (loc.getBlock() != null && isBreakable(loc.getBlock().getType())) {
									loc.getBlock().breakNaturally(p.getInventory().getItemInMainHand());
								}
							}
						}
					}

					return;
				}
			}
		}
	}

	private boolean isBreakable(Material mat) {
		return mat.getBlastResistance() < 100;
	}

}
