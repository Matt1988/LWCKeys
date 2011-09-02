package me.Matt1988.lwckeys;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.griefcraft.model.Protection;

import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCProtectionInteractEvent;

public class LWCKeysModuleListener extends JavaModule {
	LWCKeys plugin;
	
	public LWCKeysModuleListener(LWCKeys plugin) {
		this.plugin = plugin;
	}

	
	@Override
	public void onProtectionInteract(LWCProtectionInteractEvent event) {
		
		Protection protection = event.getProtection();
		Player player = event.getPlayer();
		
		ItemStack itemInHand = player.getItemInHand();
		
			//Block's Protection ID
			int protectionID = protection.getId();
			
			
			 /* Give the Player a key to the protected block only if the following conditions are met.
			 * 
			 *  A. The player has no items in hand.
			 *  B. The player interacts with a protected block while sneaking 
			 *  C. The player owns the protected block OR is an op.*/
			 				
			if(player.isSneaking())
			{
				if (itemInHand.getTypeId() == 0)
				{
					if (protection.getOwner().equalsIgnoreCase(player.getName()) || player.isOp()) {
						if(player.isOp() 
								&& !(protection.getOwner().equalsIgnoreCase(player.getName())) 
								&& !LWCKeys.OP_MAKE_KEY)
							return;
						ItemStack keyItem = new ItemStack(LWCKeys.KEY_ITEM, 1, (short) mod(protectionID), (byte) mod(protectionID));
						player.getInventory().setItemInHand(keyItem);
						player.sendMessage(plugin.textParse(LWCKeys.KEY_CREATED_TEXT, protection));
						event.setResult(Result.CANCEL);
						
						return;
					}
				}
			}
				
			
			
			 /* Unlocks the Protected Block only if the following conditions are met
			 * 
			 * A. Item in Hand has the same ID as the ID specified in KEY_ITEM
			 * B. The protected blocks ID Mod' by 256 is the same as the items durability value.
			 * 
			 * Note: Minecraft's limitations prevent there from being more than 256 unique keys so
			 * I use a quick and dirty hack to get around it (mod the protection ID by 256 before I compare it with the Key Durability..)
			 * The huge drawback to this was duplicate keys.
			 * Hoping to find a more elegant solution soon. */
			 
			
			if (itemInHand.getTypeId() != LWCKeys.KEY_ITEM)
				return;
			int itemKeyId = itemInHand.getDurability();
			
			
			//If the Key ID(Durability) is a - number, make it useable.
			if (itemKeyId < 0) 
				itemKeyId = 256 + itemKeyId;			
			
			if (mod(protectionID) == itemKeyId && !(event.canAccess())) {
				event.setResult(Result.ALLOW);
				player.sendMessage(plugin.textParse(LWCKeys.KEY_UNLOCK_TEXT, protection));
			}
			
		
			
		
	}
	
	
	
	/*
	 * Simple Modulo operation, takes the Protection ID of the protected block
	 * and subtracts it by 256 until the  ID is less than 256
	 * The remainder afterwards represents the id of the key that can be used to open that protection.
	 */
	
	public int mod(int i) {
		int inte = i;
		
		do {
			if (inte < 256)
				break;
			inte -= 256;
		} while (inte >= 256);
		
		return inte;
		
	}

}
