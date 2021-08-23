package com.daxton.fancyequipment.manager;

import com.daxton.fancycore.api.gui.GUI;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ManagerEqm {

	//GUI
	public static Map<UUID, GUI> gui_Map = new HashMap<>();
	//
	public static Map<UUID, ItemStack[]> player_Items = new HashMap<>();
	//物品位置
	public static Map<UUID, Integer> player_Item_Slot = new HashMap<>();
	//MainSlot
	public static Map<UUID, Integer> player_main_Slot = new HashMap<>();
}
