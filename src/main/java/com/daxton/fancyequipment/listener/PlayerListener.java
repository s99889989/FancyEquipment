package com.daxton.fancyequipment.listener;


import com.daxton.fancyequipment.FancyEquipment;
import com.daxton.fancyequipment.manager.ManagerEqm;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;


public class PlayerListener implements Listener {

    @EventHandler//當玩家登入
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        player.getInventory().setHeldItemSlot(0);
        UUID uuid = player.getUniqueId();
        ManagerEqm.player_main_Slot.put(uuid, 0);
    }

    //當按下切換1~9時
    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        int newKey = event.getNewSlot();
        int oldKey = event.getPreviousSlot();
        player.sendMessage(oldKey+" : "+newKey);
        if(newKey != oldKey){
            ManagerEqm.player_main_Slot.put(uuid, newKey);
        }
    }

//    @EventHandler//當使用背包時
//    public void onInventoryClick(InventoryClickEvent event){
//        int slot = event.getRawSlot();
//        int ss = event.getSlot();
//        FancyEquipment.fancyEquipment.getLogger().info(slot+" : "+ss);
//    }


}
