package me.Matt1988.lwckeys;




import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;


import com.griefcraft.model.Protection;

public class LWCKeysPlayerListener extends PlayerListener {
	LWCKeys plugin;
	public LWCKeysPlayerListener(LWCKeys plugin) {
		this.plugin = plugin;
	}
	
	
	public void onPlayerInteract(PlayerInteractEvent event) {
		/*
		 * If the clicked block is null then
		 * return out of the function
		 * this prevents errors in the console.
		 */
		if (event.getClickedBlock() == null) 
			return;

		
		/*
		 * if (plugin.lwcHook())
		 * 
		 * this returns true if lwc is hooked
		 * and if lwc isn't hooked it will go ahead and hook it and return true.
		 * 
		 * if the lwc plugin isn't installed on the server, this will return false.
		 */
		if (plugin.lwcHook()) {
			ItemStack itemInHand = event.getPlayer().getItemInHand();
			Protection protection = plugin.lwc.findProtection(event.getClickedBlock());
			if (protection != null) {
				//Block's Protection ID
				int protKeyID = protection.getId();
				
				/*
				 * Give the Player a key to the protected block only if the following conditions are met.
				 * 
				 *  A. The player has no items in hand.
				 *  B. The player Right Clicked the protected block.
				 *  C. The player is Sneaking.
				 *  D. The player owns the protected block OR is an op.
				 */				
				if(event.getAction() == Action.RIGHT_CLICK_BLOCK 
						&& event.getPlayer().isSneaking())
				{
					if (itemInHand.getTypeId() == 0)
					{
						if (protection.getOwner().equalsIgnoreCase(event.getPlayer().getName()) || event.getPlayer().isOp()) {
							if(event.getPlayer().isOp() 
									&& !(protection.getOwner().equalsIgnoreCase(event.getPlayer().getName())) 
									&& !LWCKeys.OP_MAKE_KEY)
								return;
							ItemStack keyItem = new ItemStack(LWCKeys.KEY_ITEM, 1, (short) mod(protKeyID), (byte) mod(protKeyID));
							event.getPlayer().getInventory().setItemInHand(keyItem);
							event.getPlayer().sendMessage(plugin.textParse(LWCKeys.KEY_CREATED_TEXT, protection));
							event.setCancelled(true);
							
							return;
						}
					}
				}
				
				
				/*
				 * Unlocks the Protected Block only if the following conditions are met
				 * 
				 * A. Item in Hand has the same ID as the ID specified in KEY_ITEM
				 * B. The protected blocks ID Mod' by 256 is the same as the items durability value.
				 * 
				 * Note: Minecraft's limitations prevent me from making more than 256 unique keys so
				 * there will be duplicate keys, as far as I know there is currently no way around this.
				 */
				
				if (itemInHand.getTypeId() != LWCKeys.KEY_ITEM)
					return;
				int itemKeyId = itemInHand.getDurability();
				if (itemKeyId < 0) 
					itemKeyId = 256 + itemKeyId;
				
				if (mod(protKeyID) == itemKeyId && event.isCancelled()) {
					event.setCancelled(false);
					event.getPlayer().sendMessage(plugin.textParse(LWCKeys.KEY_UNLOCK_TEXT, protection));
				}
				
			}
			
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
