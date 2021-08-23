package com.daxton.fancyequipment.gui;

import com.daxton.fancycore.api.gui.GuiCloseAction;
import com.daxton.fancyequipment.manager.ManagerEqm;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CloseAction implements GuiCloseAction {

	final Player player;
	//關閉時清除Map物品包包
	public CloseAction(Player player){
		this.player = player;
	}

	@Override
	public void execute() {
		UUID uuid = player.getUniqueId();
		ManagerEqm.player_Item_Slot.remove(uuid);
	}
}
