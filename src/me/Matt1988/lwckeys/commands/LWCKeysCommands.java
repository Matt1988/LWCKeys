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
			if (args[0].equalsIgnoreCase("reload") ) {
				//permissions
				
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (!plugin.playerHasPermission(player, "lwckeys.admin.reload", true)) {
						player.sendMessage(ChatColor.RED + "[LWCKeys]: You do not have permission to use this command");
						return true;
					}
				}
				
				plugin.loadconfig();
				sender.sendMessage(ChatColor.DARK_RED + "[LWCKeys] reloaded");
				return true;
			}
			
			if (sender instanceof Player && args[0].equalsIgnoreCase("toggle")) {
				Player player = (Player) sender;
				//Permissions
				if (!plugin.playerHasPermission(player, "lwckeys.general.toggle", false)){
					sender.sendMessage(ChatColor.RED + "[LWCKeys]: You do not have permission to use this command");
					return true;
				}
					
				
					if(plugin.isPlayerDisabled(player.getName())) {
						plugin.remDisabledPlayer(player.getName());
						player.sendMessage(ChatColor.GREEN + "[LWCKeys] - You have turned on keys for ALL of your protections");
						return true;
					}
					
					else {
						plugin.addDisabledPlayer(player.getName());
						player.sendMessage(ChatColor.GREEN + "[LWCKeys] - You have turned off keys for ALL of your protections");
						return true;
					}
			}
			

			
			

		return true;
	}
	

	
	

}
