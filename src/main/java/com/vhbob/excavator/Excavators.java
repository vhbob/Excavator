package com.vhbob.excavator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Excavators extends JavaPlugin {

	public static Excavators inst;

	@Override
	public void onEnable() {
		inst = this;
		saveDefaultConfig();
		Bukkit.getPluginManager().registerEvents(new BreakEvents(), this);
		getCommand("Excavator").setExecutor(new ExcavatorCommand());
	}

	// This method will take in and parse a String from the config
	public String getParsedConfigString(String path) {
		if (getConfig().contains(path))
			return ChatColor.translateAlternateColorCodes('&', getConfig().getString(path));
		return null;
	}

}
