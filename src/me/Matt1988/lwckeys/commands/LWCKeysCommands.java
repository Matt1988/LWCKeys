package me.Matt1988.lwckeys.commands;


import me.Matt1988.lwckeys.LWCKeys;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;





public class LWCKeysCommands implements CommandExecutor {
	public LWCKeys plugin;
	public LWCKeysCommands(LWCKeys instance) {
		plugin = instance;
	}
	

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
			/*
			 * Returns if there are no arguments
			 */
			if (args.length == 0) {
				sender.sendMessage(ChatColor.DARK_RED + "Not enough Arguments"); 
				return true;
			}
			
			/*
			 * Reloads the plugins config file.
			 * Works in Console as well.
			 */
			if (args[0].equalsIgnoreCase("reload") && (sender.isOp() || !(sender instanceof Player))) {
				plugin.loadconfig();
				sender.sendMessage(ChatColor.DARK_RED + "[LWCKeys] reloaded");
				return true;
			}
			

			
			

		return true;
	}
	

	
	

}
