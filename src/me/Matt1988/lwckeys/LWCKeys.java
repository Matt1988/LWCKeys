package me.Matt1988.lwckeys;


import java.util.logging.Logger;

import me.Matt1988.lwckeys.commands.LWCKeysCommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;


import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import com.griefcraft.model.Protection;



public class LWCKeys extends JavaPlugin {
	public Configuration config;
	
	
	protected static int KEY_ITEM = 348;
	protected static String KEY_CREATED_TEXT = "&2Key created";
	protected static String KEY_UNLOCK_TEXT = "&2Oh you have a key! Here you go.";
	protected static boolean OP_MAKE_KEY = true;
	
	public LWC lwc = null;
	public final Logger logger = Logger.getLogger("Minecraft");


	@Override
	public void onDisable() {
		logger.info("[LWCKeys] has been disabled]");
	}

	@Override
	public void onEnable() {
		//load config
		loadconfig();
		
		PluginManager pm = Bukkit.getServer().getPluginManager();
		logger.info("[LWCKeys] successfully enabled");	
		pm.registerEvent(Event.Type.PLAYER_INTERACT, new LWCKeysPlayerListener(this), Event.Priority.Monitor, this);
		pm.registerEvent(Event.Type.CUSTOM_EVENT, new LWCKeysInventoryListener(this), Event.Priority.Monitor, this);
		
		getCommand("lwckeys").setExecutor(new LWCKeysCommands(this));
		
		
		
		
		
		
		
		
	}
	
	/*
	 * loadconfig()
	 * 
	 * This load's the config file and sets corresponding variables.
	 */
	public void loadconfig() {
		this.config = getConfiguration();
		this.config.load();
		KEY_ITEM = this.config.getInt("General.KEY_ITEM", 348);
		KEY_CREATED_TEXT = this.config.getString("General.KEY_CREATED_TEXT", "&2Key made for protection owned by &4%p.");
		KEY_UNLOCK_TEXT = this.config.getString("General.KEY_UNLOCK_TEXT", "&2You have unlocked a chest belonging to &4%p.");
		OP_MAKE_KEY = this.config.getBoolean("General.OP_MAKE_KEY", true);
		this.config.save();
	}

	/*
	 * lwcHook()
	 * 
	 * this returns true if lwc is hooked
	 * and if lwc isn't hooked it will hook it and return true.
	 * 
	 * if the lwc plugin isn't installed on the server, this will return false.
	 */
	public boolean lwcHook() {
		Plugin lwcPlugin = getServer().getPluginManager().getPlugin("LWC");
		if (lwcPlugin == null)
			return false;
		
		
		if (lwc == null)
		{
			
			if(lwcPlugin != null) {
			    lwc = ((LWCPlugin) lwcPlugin).getLWC();
			    return true;
			}
			
		}
		return true;
	}
	
	
	/*
	 * This is the text parser.
	 * 
	 * It's meant mostly for the config file messages but I guess it can have other applications to.
	 * 
	 * 
	 */
	public String textParse(String t) {
		String text = t;
		text = text.replace("&0", ChatColor.BLACK.toString());
		text = text.replace("&1", ChatColor.DARK_BLUE.toString());
		text = text.replace("&2", ChatColor.DARK_GREEN.toString());
		text = text.replace("&3", ChatColor.DARK_AQUA.toString());
		text = text.replace("&4", ChatColor.DARK_RED.toString());
		text = text.replace("&5", ChatColor.LIGHT_PURPLE.toString());
		text = text.replace("&6", ChatColor.GOLD.toString());
		text = text.replace("&7", ChatColor.GRAY.toString());
		text = text.replace("&8", ChatColor.DARK_GRAY.toString());
		text = text.replace("&9", ChatColor.BLUE.toString());
		text = text.replace("&a", ChatColor.GREEN.toString());
		text = text.replace("&b", ChatColor.AQUA.toString());
		text = text.replace("&c", ChatColor.RED.toString());
		text = text.replace("&e", ChatColor.YELLOW.toString());
		text = text.replace("&f", ChatColor.WHITE.toString());
		
		return text;
	}
	
	public String textParse(String t, Protection protection) {
		String text = textParse(t);
		
		text = text.replace("%p", protection.getOwner());
		
		return text;
	}

	
}
