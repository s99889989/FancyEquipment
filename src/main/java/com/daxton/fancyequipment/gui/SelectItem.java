package com.daxton.fancyequipment.gui;


import com.daxton.fancycore.FancyCore;
import com.daxton.fancycore.api.gui.button.GuiAction;
import com.daxton.fancyequipment.PlayerEqmData;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;


public class SelectItem implements GuiAction {

	final PlayerEqmData playerEqmData;

	public SelectItem(PlayerEqmData playerEqmData){
		this.playerEqmData = playerEqmData;
	}

	public void execute(ClickType clickType, InventoryAction action, int slot){
		if(clickType == ClickType.LEFT){
			int k = slot - 54;
			if(k < 27){
				k += 9;
			}else {
				k -= 27;
			}

			ItemStack itemStack = playerEqmData.player.getInventory().getItem(k);
			int mainSlot = playerEqmData.mainSlot;
			if(k != mainSlot && itemStack != null){
				playerEqmData.selectSlot = k;
			}

		}
	}


}
