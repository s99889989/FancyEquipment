package com.daxton.fancyequipment.gui.equipmentbar;

import com.daxton.fancycore.api.gui.button.GuiAction;
import com.daxton.fancycore.api.gui.button.GuiButton;
import com.daxton.fancycore.api.gui.item.GuiItem;
import com.daxton.fancycore.api.item.ItemKeySearch;
import com.daxton.fancycore.manager.PlayerManagerCore;
import com.daxton.fancycore.other.entity.BukkitAttributeSet;
import com.daxton.fancycore.other.playerdata.PlayerDataFancy;
import com.daxton.fancyequipment.FancyEquipment;
import com.daxton.fancyequipment.PlayerEqmData;
import com.daxton.fancyequipment.api.event.EquipmentChangeEvent;
import com.daxton.fancyequipment.config.FileConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ClickEqm implements GuiAction {

	ItemStack itemStack;
	public int oldSlot = -1;
	public final PlayerEqmData playerEqmData;
	public final PlayerDataFancy playerDataFancy;
	//物品欄位置名稱
	public final String eqmString;
	//物品欄需求名稱
	final String restrictionType;
	final int row;
	final int columns;
	boolean show = true;

	//裝備欄按鈕
	public ClickEqm(PlayerEqmData playerEqmData, String eqmString, String restrictionType, ItemStack itemStack, int row, int columns){
		this.playerEqmData = playerEqmData;
		this.eqmString = eqmString;
		this.restrictionType = restrictionType;
		this.itemStack = itemStack;
		this.row = row;
		this.columns = columns;
		this.playerDataFancy = PlayerManagerCore.player_Data_Map.get(playerEqmData.uuid);
	}

	public void execute(ClickType clickType, InventoryAction action, int slot){
		if(clickType == ClickType.LEFT){
			left();
		}
		//右鍵切換裝備顯示
		if(clickType == ClickType.RIGHT){
			SetShow.right(this);
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

		if(restrictionType == null){
			return true;
		}

		//FancyEquipment.fancyEquipment.getLogger().info(restrictionType+" : "+itemType);

		if(restrictionType.equalsIgnoreCase(itemType)){
			return false;
		}
		//這格物品類型去比較物品類型
		return true;
	}

	//移除裝備
	public void removeEqm(Player player){
		if(itemStack != null){
			if(player.getInventory().firstEmpty() == -1){
				return;
			}
			FileConfiguration eqmConfig = FileConfig.config_Map.get("equipment.yml");
			ItemStack eqmStack = GuiItem.valueOf(eqmConfig, eqmString);
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
			removeAttr();
			playerEqmData.eqm_Map.put(eqmString, null);
			if(eqmString.equalsIgnoreCase("Helmet")){
				player.getInventory().setHelmet(null);
			}
			//裝備顯示
			eqmDisplay(null);
			GuiButton eqmButton = GuiButton.ButtonBuilder.getInstance().
				setItemStack(eqmStack).
				setGuiAction(this).
				build();

			playerEqmData.gui.setButton(eqmButton, row, columns);

			playerDataFancy.removeEqmAction(eqmString);
			playerDataFancy.removeCustomValue(eqmString);

			EquipmentChangeEvent equipmentChangeEvent = new EquipmentChangeEvent(player, eqmString, new ItemStack(Material.AIR));
			Bukkit.getPluginManager().callEvent(equipmentChangeEvent);
		}
	}
	//安裝裝備
	public void addEqm(Player player, ItemStack clickItem){
		if(clickItem.getAmount() > 1){
			return;
		}
		//如果裝備欄不是空
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
			playerDataFancy.removeEqmAction(eqmString);
			playerDataFancy.removeCustomValue(eqmString);
		}else {
			if(playerEqmData.selectSlot != -1){
				int iSlot = playerEqmData.selectSlot;
				player.getInventory().setItem(iSlot, null);
			}
		}

		playerEqmData.eqm_Map.put(eqmString, clickItem);
		if(eqmString.equalsIgnoreCase("Helmet")){
			player.getInventory().setHelmet(clickItem);
		}
		//裝備顯示
		eqmDisplay(clickItem);
		GuiButton clickButton = GuiButton.ButtonBuilder.getInstance().
			setItemStack(clickItem).
			setGuiAction(this).
			build();
		playerEqmData.gui.setButton(clickButton, row, columns);

		addttr(clickItem);

		itemStack = clickItem;


		playerEqmData.selectSlot = -1;

		playerDataFancy.addEqmAction(eqmString, itemStack);
		playerDataFancy.addEqmCustomValue(eqmString, itemStack);

		EquipmentChangeEvent equipmentChangeEvent = new EquipmentChangeEvent(player, eqmString, itemStack);
		Bukkit.getPluginManager().callEvent(equipmentChangeEvent);
	}
	//移除屬性
	public void removeAttr(){
		BukkitAttributeSet.removeLabelAttr(playerEqmData.player, eqmString);
	}
	//增加屬性
	public void addttr(ItemStack eqmItem){
		if(eqmItem == null){
			return;
		}
		ItemMeta itemMeta = eqmItem.getItemMeta();
		if(itemMeta.getAttributeModifiers() != null){

			itemMeta.getAttributeModifiers().forEach((attribute, attributeModifier) -> {
				//FancyEquipment.fancyEquipment.getLogger().info("屬性: "+attribute.name()+" : "+attributeModifier.getName());
				String inherit = attribute.name();
				String operation = attributeModifier.getOperation().name();
				double addNumber = attributeModifier.getAmount();
				String label = eqmString;
				BukkitAttributeSet.removeAddAttribute(playerEqmData.player, inherit, operation, addNumber, label);
			});

		}

	}

}
