package com.daxton.fancyequipment.gui;

import com.daxton.fancycore.api.gui.GuiAction;
import com.daxton.fancycore.api.gui.GuiButtom;
import com.daxton.fancycore.api.item.ItemKeySearch;
import com.daxton.fancyequipment.PlayerEqmData;
import com.daxton.fancyequipment.config.FileConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

public class Outfit implements GuiAction {

	ItemStack itemStack;
	public int oldSlot = -1;
	final PlayerEqmData playerEqmData;
	final String eqmString;
	final String restrictionType;
	final int row;
	final int columns;
	boolean show = true;

	//裝備欄按鈕
	public Outfit(PlayerEqmData playerEqmData, String eqmString, String restrictionType, ItemStack itemStack, int row, int columns){
		this.playerEqmData = playerEqmData;
		this.eqmString = eqmString;
		this.restrictionType = restrictionType;
		this.itemStack = itemStack;
		this.row = row;
		this.columns = columns;
	}

	public void execute(ClickType clickType, InventoryAction action, int slot){
		if(clickType == ClickType.LEFT){
			left();
		}
		//右鍵切換裝備顯示
		if(clickType == ClickType.RIGHT){
			right();
		}

	}

	//左鍵裝裝備
	public void left(){
		Player player = playerEqmData.player;
		ItemStack clickItem = getEqmItem();
		if(clickItem != null){
			//檢查裝是否符合類型
			if(isType(clickItem)){
				return;
			}
			//安裝裝備
			addEqm(player, clickItem);
		}else {
			//移除裝備
			removeEqm(player);
		}
	}

	//右鍵切換裝備顯示
	public void right(){
		if(eqmString.equalsIgnoreCase("Armor_Pants")){
			if(show){
				show = false;
				playerEqmData.bodyEntity.equipment(null, "HEAD");
			}else {
				if(playerEqmData.eqm_Map.get("Armor_Pants") != null){
					show = true;
					playerEqmData.bodyEntity.equipment(playerEqmData.eqm_Map.get("Armor_Pants"), "HEAD");
				}
			}

		}
		if(eqmString.equalsIgnoreCase("Armor_Back")){
			if(show){
				show = false;
				playerEqmData.bodyEntity.equipment(null, "OFFHAND");
			}else {
				if(playerEqmData.eqm_Map.get("Armor_Back") != null){
					show = true;
					playerEqmData.bodyEntity.equipment(playerEqmData.eqm_Map.get("Armor_Back"), "OFFHAND");
				}
			}

		}
		if(eqmString.equalsIgnoreCase("Armor_Tail")){
			if(show){
				show = false;
				playerEqmData.bodyEntity.equipment(null, "MAINHAND");
			}else {
				if(playerEqmData.eqm_Map.get("Armor_Tail") != null){
					show = true;
					playerEqmData.bodyEntity.equipment(playerEqmData.eqm_Map.get("Armor_Tail"), "MAINHAND");
				}
			}

		}
	}

	//裝備顯示
	public void eqmDisplay(ItemStack clickItem){
		show = clickItem != null;
		if(eqmString.equalsIgnoreCase("Armor_Pants")){
			playerEqmData.bodyEntity.equipment(clickItem, "HEAD");
			return;
		}
		if(eqmString.equalsIgnoreCase("Armor_Back")){
			playerEqmData.bodyEntity.equipment(clickItem, "OFFHAND");
			return;
		}
		if(eqmString.equalsIgnoreCase("Armor_Tail")){
			playerEqmData.bodyEntity.equipment(clickItem, "MAINHAND");
		}
	}
	//獲取裝備
	public ItemStack getEqmItem(){
		if(playerEqmData.selectSlot != -1){
			int clickSlot = playerEqmData.selectSlot;
			oldSlot = clickSlot;
			ItemStack[] itemStacks = playerEqmData.bags;
			if(itemStacks[clickSlot] != null){
				return itemStacks[clickSlot];
			}
		}
		return null;
	}
	//檢查裝是否符合類型
	public boolean isType(ItemStack clickItem){
		//獲取物品ID
		String clickItemID = ItemKeySearch.getCustomAttributes(clickItem, "itemid");

		//獲取物品類型
		String itemType = "";
		if(!clickItemID.isEmpty()){
			itemType = clickItemID.split("\\.")[0];
		}
		//這格物品類型去比較物品類型
		return restrictionType == null || restrictionType.equalsIgnoreCase(itemType);
	}

	//移除裝備
	public void removeEqm(Player player){
		if(itemStack != null){
			if(player.getInventory().firstEmpty() == -1){
				return;
			}
			FileConfiguration eqmConfig = FileConfig.config_Map.get("equipment.yml");
			ItemStack eqmStack = GuiButtom.valueOf(eqmConfig, eqmString);
			if(oldSlot != -1){
				player.getInventory().setItem(oldSlot, itemStack);
				oldSlot = -1;
			}else {
				int x = 0;
				for(ItemStack itemStack1 : player.getInventory()){
					if(itemStack1 == null && x < 36){
						player.getInventory().setItem(x, itemStack);
						break;
					}
					x++;
				}
				playerEqmData.setBag();
			}
			itemStack = null;
			playerEqmData.eqm_Map.put(eqmString, null);
			if(eqmString.equalsIgnoreCase("Helmet")){
				player.getInventory().setHelmet(null);
			}
			//裝備顯示
			eqmDisplay(null);

			playerEqmData.gui.setItem(eqmStack, false, row, columns);
		}
	}
	//安裝裝備
	public void addEqm(Player player, ItemStack clickItem){

		if(itemStack != null){
			//檢查玩家背包是否有空位，沒有就不執行
			if(player.getInventory().firstEmpty() == -1){
				return;
			}

			if(playerEqmData.selectSlot != -1){
				int clickSlot = playerEqmData.selectSlot;
				player.getInventory().setItem(clickSlot, itemStack);
			}else {
				player.getInventory().addItem(itemStack);
			}
		}
		playerEqmData.eqm_Map.put(eqmString, clickItem);
		if(eqmString.equalsIgnoreCase("Helmet")){
			player.getInventory().setHelmet(clickItem);
		}
		//裝備顯示
		eqmDisplay(clickItem);

		playerEqmData.gui.setItem(clickItem, false, row, columns);

		itemStack = clickItem;
		int iSlot = playerEqmData.selectSlot;
		player.getInventory().setItem(iSlot, null);

		playerEqmData.selectSlot = -1;
	}



}
