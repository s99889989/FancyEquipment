package com.daxton.fancyequipment.gui;

import com.daxton.fancycore.api.gui.GuiCloseAction;
import com.daxton.fancyequipment.PlayerEqmData;
import com.daxton.fancyequipment.config.FileConfig;
import org.bukkit.configuration.file.FileConfiguration;

public class CloseAction implements GuiCloseAction {

	final PlayerEqmData playerEqmData;
	//關閉時清除Map物品包包
	public CloseAction(PlayerEqmData playerEqmData){
		this.playerEqmData = playerEqmData;
	}

	@Override
	public void execute() {
		playerEqmData.selectSlot = -1;
		FileConfiguration eqmConfig = FileConfig.config_Map.get("equipment.yml");
		eqmConfig.getConfigurationSection("").getKeys(false).forEach(eqmString->{
			if(!eqmString.equalsIgnoreCase("Main")){
				int row = eqmConfig.getInt(eqmString +".SlotRow");
				int columns = eqmConfig.getInt(eqmString +".SlotColumns");
				Outfit outfit = (Outfit) playerEqmData.gui.getAction(row, columns);
				outfit.oldSlot = -1;
			}

		});
	}
}
