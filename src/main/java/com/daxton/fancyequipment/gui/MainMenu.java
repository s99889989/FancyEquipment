package com.daxton.fancyequipment.gui;

import com.daxton.fancycore.api.gui.GUI;
import com.daxton.fancycore.api.gui.GuiButtom;
import com.daxton.fancyequipment.config.FileConfig;
import com.daxton.fancyequipment.manager.ManagerEqm;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.daxton.fancyequipment.config.FileConfig.languageConfig;

public class MainMenu {

	//打開物品編輯器
	public static void open(Player player){
		UUID uuid = player.getUniqueId();
		setBag(player);

		if(ManagerEqm.gui_Map.get(uuid) == null){
			GUI gui = new GUI(player, 54, languageConfig.getString("Title"));

			FileConfiguration eqmConfig = FileConfig.config_Map.get("equipment.yml");
			eqmConfig.getConfigurationSection("").getKeys(false).forEach(eqmString -> {

				int row = eqmConfig.getInt(eqmString +".SlotRow");
				int columns = eqmConfig.getInt(eqmString +".SlotColumns");
				ItemStack itemStack = GuiButtom.valueOf(eqmConfig, eqmString);
				gui.setItem(itemStack, false, row, columns);

				if(eqmString.equalsIgnoreCase("Main")){
					ItemStack mainItem = player.getInventory().getItemInMainHand();
					if(mainItem.getType() != Material.AIR){
						gui.setItem(mainItem, false, row, columns);
					}
				}else {
					gui.setAction(new Outfit(player, gui, eqmString, row, columns), row, columns);
				}

			});

			List<Integer> ing = new ArrayList<>();
			for(int i = 0; i < 36 ; i++){
				gui.addAction(new SelectItem(player), 55, 90, ing);
			}

			//關閉時清除Map物品包包
			gui.setCloseAction(new CloseAction(player));

			gui.setMoveAll(false);
			ManagerEqm.gui_Map.put(uuid, gui);
			gui.open(gui);
		}else {
			GUI gui = ManagerEqm.gui_Map.get(uuid);

			gui.open(gui);
		}



	}
	//把目前背包內容儲存到Map
	public static void setBag(Player player){
		UUID uuid = player.getUniqueId();
		ItemStack[] ii = new ItemStack[36];
		int x = 0;
		for(ItemStack i : player.getInventory()){
			if(x < 36){
				ii[x] = i;
			}
			x++;
		}
		ManagerEqm.player_Items.put(uuid, ii);
	}

}
