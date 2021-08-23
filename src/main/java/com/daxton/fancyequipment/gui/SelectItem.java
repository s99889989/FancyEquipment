package com.daxton.fancyequipment.gui;

import com.daxton.fancycore.api.gui.GuiAction;
import com.daxton.fancyequipment.manager.ManagerEqm;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SelectItem implements GuiAction {

	final Player player;

	public SelectItem(Player player){
		this.player = player;
	}

	public void execute(ClickType clickType, InventoryAction action, int slot){
		if(clickType == ClickType.LEFT){
			UUID uuid = player.getUniqueId();
			int k = slot - 54;
			if(k < 27){
				k += 9;
			}else {
				k -= 27;
			}

			ItemStack itemStack = player.getInventory().getItem(k);
			int mainSlot = ManagerEqm.player_main_Slot.get(uuid);
			if(k != mainSlot && itemStack != null){
				ManagerEqm.player_Item_Slot.put(uuid, k);
				//ManagerEqm.player_Item_Map.put(uuid, itemStack);
			}



		}
	}


}
