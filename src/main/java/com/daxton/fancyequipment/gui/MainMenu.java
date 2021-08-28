package com.daxton.fancyequipment.gui;

import com.daxton.fancyequipment.PlayerEqmData;
import com.daxton.fancyequipment.manager.ManagerEqm;
import org.bukkit.entity.Player;
import java.util.UUID;


public class MainMenu {

	//打開物品編輯器
	public static void open(Player player){
		UUID uuid = player.getUniqueId();

		PlayerEqmData playerEqmData = ManagerEqm.player_Data.get(uuid);
		playerEqmData.setBag();
		playerEqmData.setMain();
		playerEqmData.gui.open(playerEqmData.gui);

	}


}
