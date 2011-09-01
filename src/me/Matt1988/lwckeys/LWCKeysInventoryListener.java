package me.Matt1988.lwckeys;




import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.event.inventory.InventoryClickEvent;
import org.getspout.spoutapi.event.inventory.InventoryListener;

public class LWCKeysInventoryListener extends InventoryListener {
	LWCKeys plugin;
	public LWCKeysInventoryListener(LWCKeys plugin) {
		this.plugin = plugin;
	}
	@Override
	public void onInventoryClick(InventoryClickEvent event) {
		ItemStack itemClicked = event.getItem();
		ItemStack itemHolding = event.getCursor();
		/*
		 * This prevents the stacking of KEY_ITEM
		 * 
		 * Note: This only works if the Spout server side plugin is installed.
		 */
		if (itemHolding != null 
				&& itemClicked != null 
				&& itemHolding.getTypeId() == LWCKeys.KEY_ITEM 
				&& itemClicked.getTypeId() == LWCKeys.KEY_ITEM 
				&& itemClicked.getDurability() != itemHolding.getDurability()) {
			event.setCancelled(true);
			
		}
	}

}
