package com.daxton.fancyequipment.gui;

import com.daxton.fancycore.api.gui.GUI;
import com.daxton.fancycore.api.gui.GuiAction;
import com.daxton.fancycore.api.gui.GuiButtom;
import com.daxton.fancyequipment.config.FileConfig;
import com.daxton.fancyequipment.manager.ManagerEqm;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Outfit implements GuiAction {

	ItemStack itemStack;
	int oldSlot = 100;
	final Player player;
	final GUI gui;
	final String eqmString;
	final int row;
	final int columns;

	public Outfit(Player player, GUI gui, String eqmString, int row, int columns){
		this.player = player;
		this.gui = gui;
		this.eqmString = eqmString;
		this.row = row;
		this.columns = columns;
	}

	public void execute(ClickType clickType, InventoryAction action, int slot){
		if(clickType == ClickType.LEFT){

			UUID uuid = player.getUniqueId();

			ItemStack clickItem = null;
			if(ManagerEqm.player_Item_Slot.get(uuid) != null){
				int clickSlot = ManagerEqm.player_Item_Slot.get(uuid);
				oldSlot = clickSlot;
				if(ManagerEqm.player_Items.get(uuid) != null){
					ItemStack[] itemStacks = ManagerEqm.player_Items.get(uuid);
					if(itemStacks[clickSlot] != null){
						clickItem = itemStacks[clickSlot];
					}
				}
			}

			if(clickItem != null){
				if(itemStack != null){
					if(ManagerEqm.player_Item_Slot.get(uuid) != null){
						int clickSlot = ManagerEqm.player_Item_Slot.get(uuid);
						player.getInventory().setItem(clickSlot, itemStack);
					}else {
						player.getInventory().addItem(itemStack);
					}
				}

				gui.setItem(clickItem, false, row, columns);

				itemStack = clickItem;
				int iSlot = ManagerEqm.player_Item_Slot.get(uuid);;
				player.getInventory().setItem(iSlot, null);
				ManagerEqm.player_Item_Slot.remove(uuid);

			}else {
				if(itemStack != null){
					FileConfiguration eqmConfig = FileConfig.config_Map.get("equipment.yml");
					ItemStack eqmStack = GuiButtom.valueOf(eqmConfig, eqmString);
					if(oldSlot != 100){
						player.getInventory().setItem(oldSlot, itemStack);
						oldSlot = 100;
					}else {
						player.getInventory().addItem(itemStack);
					}
					itemStack = null;
					gui.setItem(eqmStack, false, row, columns);
				}
			}

		}
	}

}
