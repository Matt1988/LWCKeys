package me.Matt1988.lwckeys;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import me.Matt1988.lwckeys.commands.LWCKeysCommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;


import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;



public class LWCKeys extends JavaPlugin {
	public Configuration config;
	
	
	protected static int KEY_ITEM = 348;
	protected static String KEY_CREATED_TEXT = "&2Key created";
	protected static String KEY_UNLOCK_TEXT = "&2Oh you have a key! Here you go.";
	protected static boolean OP_MAKE_KEY = true;
	
	public static PermissionHandler permissions;
	
	protected List<Object> disabledPlayers = new ArrayList<Object>();;
	
	public LWC lwc;
	public final Logger logger = Logger.getLogger("Minecraft");
	
	
    public void onEnable() {
        Plugin lwc = getServer().getPluginManager().getPlugin("LWC");
        loadconfig();
        
        if (lwc != null) {
            lwcInit();
        } else {  
        	getServer().getPluginManager().registerEvent(Event.Type.PLUGIN_ENABLE, new LWCKeysServerListener(this), Priority.Monitor, this);
            info("Waiting for LWC to be enabled...");
        }
        
        Plugin spout = getServer().getPluginManager().getPlugin("Spout");
        if (spout != null) {
        	getServer().getPluginManager().registerEvent(Event.Type.CUSTOM_EVENT, new LWCKeysInventoryListener(this), Priority.Normal, this);
        }
        
        getCommand("lwckeys").setExecutor(new LWCKeysCommands(this));
        info("has successfully loaded");
    }

    public void onDisable() {

    }

    private void info(String message) {
        logger.info("[LWCKeys]: " + message);
    }

	
	/**
	 * Loads the configuration file and sets
	 * corresponding variables.
	 * 
	 * @since v0.1
	 */
	public void loadconfig() {
		this.config = getConfiguration();
		this.config.load();
		KEY_ITEM = this.config.getInt("General.KEY_ITEM", 348);
		KEY_CREATED_TEXT = this.config.getString("General.KEY_CREATED_TEXT", "&4Notice:&f You have created a key for &4%b&f owned by &e%o");
		KEY_UNLOCK_TEXT = this.config.getString("General.KEY_UNLOCK_TEXT", "&4Notice:&f You have unlocked a &4%b&f owned by &e%o");
		OP_MAKE_KEY = this.config.getBoolean("General.OP_MAKE_KEY", true);
		disabledPlayers = this.config.getList("players.disabled");
		if (disabledPlayers == null) {
			getServer().broadcastMessage("test");
			disabledPlayers = new ArrayList<Object>();
		}
		this.config.save();
	}

	/**
	 * saves the config file.
	 * 
	 * @since v0.1
	 */
	public void saveconfig() {
		if (this.config != null) {
			this.config.save();
		}
	}
	
	/**
	 * Checks if player is on the list of disabled players
	 * 
	 * 
	 * @param name
	 * @return
	 * 
	 * @since v0.5
	 */
	public boolean isPlayerDisabled(String name) {
		if(disabledPlayers.contains(name)) {
			return true;
		}
		return false;
		
	}
	
	/**
	 * add a player to the list of disabled players
	 * and save the config.
	 * 
	 * @param name
	 * 
	 * @since v0.5
	 */
	public void addDisabledPlayer(String name) {
		
		disabledPlayers.add(name);
		this.config.setProperty("players.disabled", disabledPlayers);
		this.config.save();
	}
	
	
	/**
	 * Removes 'name' from the list of disabled players
	 * and saves the configuration file.
	 * 
	 * @param name
	 * @return
	 * 
	 * @since v0.5
	 */
	public boolean remDisabledPlayer(String name) {
		
		if (disabledPlayers.contains(name)) {
			disabledPlayers.remove(name);
			this.config.setProperty("players.disabled", disabledPlayers);
			this.config.save();
			return true;
		}
		return false;
	}
	
	/**
	 * Checks a player for permissions
	 * If a permissions plugin is not present
	 * It will check if the player is an OP
	 * 
	 * @param player
	 * Player - The player whose permissions you are checking.
	 * @param node
	 * String - The permission node that you want to check
	 * @param requireOp
	 * Boolean - If true, the plugin will not allow a non-op permission 
	 * in the absence of a permission plugin 
	 * @return true or false
	 * 
	 * @since v0.5
	 */
	public boolean playerHasPermission(Player player, String node, Boolean requireOp) {
		if (this.getServer().getPluginManager().isPluginEnabled("Permissions")) {
			if (permissions == null) {
				Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
				permissions = ((Permissions) permissionsPlugin).getHandler();
			}
			
			if (permissions.has(player, node) || player.isOp()) {
				return true;
			}
			
			return false;
		}
		if (!player.isOp()) {
			if (requireOp) {
				return false;
			}
			else {
				return true;
			}
		}
		return true;
		
	}
	
	
	/**
	 * Initiates the lwc events.
	 * 
	 * @since v0.2
	 */
	public void lwcInit() {
        LWC.getInstance().getModuleLoader().registerModule(this, new LWCKeysModuleListener(this));
        info("Registered Economy Module into LWC successfully! Version: " + getDescription().getVersion());
    }



	
	
	/**
	 * 
	 * Parses color codes in 't' if any and returns the result.
	 * 
	 * @param t
	 * @return
	 * returns a color code parsed string.
	 * 
	 * @since v0.1
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
	

	/**
	 * 
	 * parses color codes and variable codes in 't' if any and returns the results.
	 * 
	 * @param t
	 * @param protection
	 * @return
	 * 
	 * @since v0.2
	 */
	public String textParse(String t, Protection protection) {
		String text = textParse(t);
		String blockName = "Protection";
		String protectionType = "";
		
		if (protection.getType() == 0)
			protectionType = "Public ";
		if (protection.getType() == 1)
			protectionType = "Password ";
		if (protection.getType() == 2)
			protectionType = "Private ";
		
		if (protection.getBlock().getType() == Material.CHEST)
			blockName = "Chest";
		if(protection.getBlock().getTypeId() == 64)
			blockName = "Wooden Door";
		if(protection.getBlock().getTypeId() == 71)
			blockName = "Iron Door";
		if(protection.getBlock().getType() == Material.FURNACE)
			blockName = "Furnace";
		
		text = text.replace("%o", protection.getOwner());
		text = text.replace("%b", protectionType + blockName);
		
		return text;
	}

	
}
