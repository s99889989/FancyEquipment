package com.daxton.fancyequipment.listener;

import com.daxton.fancyequipment.FancyEquipment;
import com.daxton.fancyequipment.PlayerEqmData;
import com.daxton.fancyequipment.config.FileConfig;
import com.daxton.fancyequipment.manager.ManagerEqm;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

import static com.daxton.fancyequipment.config.FileConfig.languageConfig;


public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)//當玩家登入
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        //建立玩家資料
        if(ManagerEqm.player_Data.get(uuid) == null){
            PlayerEqmData playerEqmData = new PlayerEqmData(player);
            playerEqmData.mainSlot = player.getInventory().getHeldItemSlot();
            ManagerEqm.player_Data.put(uuid, playerEqmData);
        }else {
            PlayerEqmData playerEqmData = ManagerEqm.player_Data.get(uuid);
            playerEqmData.mainSlot = player.getInventory().getHeldItemSlot();
        }

        Bukkit.getOnlinePlayers().forEach(player1 -> {
            UUID uuid1 = player1.getUniqueId();
            if(ManagerEqm.player_Data.get(uuid1) != null){
                PlayerEqmData playerEqmData = ManagerEqm.player_Data.get(uuid1);
                playerEqmData.display();
                playerEqmData.setDefaultEqm();
            }
        });

    }
    @EventHandler//當玩家登出
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if(ManagerEqm.player_Data.get(uuid) != null){
            PlayerEqmData playerEqmData = ManagerEqm.player_Data.get(uuid);
            playerEqmData.bukkitRunnable.cancel();
            playerEqmData.bodyEntity.delete();
            playerEqmData.saveEqmConfig();
            ManagerEqm.player_Data.remove(uuid);
        }
    }


    //當按下切換1~9時
    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        int newKey = event.getNewSlot();
        int oldKey = event.getPreviousSlot();
        if(newKey != oldKey){
            //設置玩家主手位置
            PlayerEqmData playerEqmData = ManagerEqm.player_Data.get(uuid);
            playerEqmData.mainSlot = newKey;
        }
    }

    //儲存世界時
    @EventHandler
    public void onWorldSave(WorldSaveEvent event){
        String worldName = event.getWorld().getName();
        if(worldName.equalsIgnoreCase("world")){
            //儲存玩家裝備
            FancyEquipment.fancyEquipment.getLogger().info(languageConfig.getString("LogMessage.StorageEquipment"));
            for(PlayerEqmData playerEqmData : ManagerEqm.player_Data.values()){
                playerEqmData.saveEqmConfig();
            }
        }

    }

    @EventHandler//當使用背包時
    public void onInventoryClick(InventoryClickEvent event){

        int slot = event.getRawSlot();
        int ss = event.getSlot();
        FileConfiguration config = FileConfig.config_Map.get("config.yml");
        if(config.getBoolean("Equipment")){
            Player player = (Player) event.getWhoClicked();
            if(slot == 5 && ss == 39){
                event.setCancelled(true);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(event.getClickedInventory() != null){
                            ItemStack itemStack = event.getClickedInventory().getItem(39);
                            if(itemStack != null){
                                event.getClickedInventory().setItem(39, null);
                            }
                        }
                    }
                }.runTaskLater(FancyEquipment.fancyEquipment, 20);
            }
            if(slot == 6 && ss == 38){
                event.setCancelled(true);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(event.getClickedInventory() != null){
                            ItemStack itemStack = event.getClickedInventory().getItem(38);
                            if(itemStack != null){
                                event.getClickedInventory().setItem(38, null);
                                player.getInventory().addItem(itemStack);
                            }
                        }
                    }
                }.runTaskLater(FancyEquipment.fancyEquipment, 20);
            }
            if(slot == 7 && ss == 37){
                event.setCancelled(true);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(event.getClickedInventory() != null){
                            ItemStack itemStack = event.getClickedInventory().getItem(37);
                            if(itemStack != null){
                                event.getClickedInventory().setItem(37, null);
                                player.getInventory().addItem(itemStack);
                            }
                        }
                    }
                }.runTaskLater(FancyEquipment.fancyEquipment, 20);
            }
            if(slot == 8 && ss == 36){
                event.setCancelled(true);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(event.getClickedInventory() != null){
                            ItemStack itemStack = event.getClickedInventory().getItem(36);
                            if(itemStack != null){
                                event.getClickedInventory().setItem(36, null);
                                player.getInventory().addItem(itemStack);
                            }
                        }
                    }
                }.runTaskLater(FancyEquipment.fancyEquipment, 20);
            }
        }

        //FancyEquipment.fancyEquipment.getLogger().info(slot+" : "+ss);
    }


}
