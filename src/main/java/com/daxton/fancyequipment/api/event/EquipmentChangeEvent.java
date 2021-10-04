package com.daxton.fancyequipment.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EquipmentChangeEvent  extends Event {

	private static final HandlerList handlers = new HandlerList();

	public Player player;
	public String eqmSlot;
	public ItemStack itemStack;

	public EquipmentChangeEvent(Player player, String eqmSlot, ItemStack itemStack){
		this.player = player;
		this.eqmSlot = eqmSlot;
		this.itemStack = itemStack;
	}


	public Player getPlayer() {
		return player;
	}

	public String getEqmSlot() {
		return eqmSlot;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	@NotNull
	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

}
