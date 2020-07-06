package com.vhbob.excavator;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ExcavatorCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("excavator")) {
			// Confirm args are good
			if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
				if (!sender.hasPermission("excavator.give")) {
					sender.sendMessage("You do not have permission");
					return false;
				}
				if (Bukkit.getPlayer(args[1]) != null && Excavators.inst.getConfig().contains("tools." + args[2])) {
					String toolName = args[2];

					// Create tool
					String type = Excavators.inst.getConfig().getString("tools." + toolName + ".material");
					ItemStack tool = new ItemStack(Material.valueOf(type));
					ItemMeta toolm = tool.getItemMeta();
					toolm.setDisplayName(Excavators.inst.getParsedConfigString("tools." + toolName + ".name"));

					ArrayList<String> lore = new ArrayList<String>();
					for (String s : Excavators.inst.getConfig().getStringList("tools." + toolName + ".lore"))
						lore.add(ChatColor.translateAlternateColorCodes('&', s));

					toolm.setLore(lore);
					tool.setItemMeta(toolm);

					// Give it to player
					Player receiver = Bukkit.getPlayer(args[1]);
					receiver.getInventory().addItem(tool);
					receiver.sendMessage(Excavators.inst.getParsedConfigString("messages.receive"));

					return true;
				} else {
					sender.sendMessage("Usage: /excavator give %player% %tool%");
				}
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					if (sender.hasPermission("excavator.reload")) {
						Excavators.inst.reloadConfig();
						sender.sendMessage(
								ChatColor.translateAlternateColorCodes('&', "&7Successfully reloaded config!"));
						return true;
					} else {
						sender.sendMessage(command.getPermissionMessage());
					}
				} else if (args[0].equalsIgnoreCase("list")) {
					if (sender.hasPermission("excavator.list")) {
						sender.sendMessage("Excavator lists");
						for (String toolType : Excavators.inst.getConfig().getConfigurationSection("tools")
								.getKeys(false)) {
							sender.sendMessage(toolType);
						}
						return true;
					} else {
						sender.sendMessage(command.getPermissionMessage());
					}
				}
			}
		}
		return false;
	}
}
