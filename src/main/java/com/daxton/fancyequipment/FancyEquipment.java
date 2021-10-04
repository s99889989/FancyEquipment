package com.daxton.fancyequipment;


import com.daxton.fancyequipment.command.MainCommand;
import com.daxton.fancyequipment.command.TabCommand;
import com.daxton.fancyequipment.listener.PlayerListener;
import com.daxton.fancyequipment.manager.ManagerEqm;
import com.daxton.fancyequipment.task.Start;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import static com.daxton.fancyequipment.config.FileConfig.languageConfig;
import java.util.Objects;

public final class FancyEquipment extends JavaPlugin {

    public static FancyEquipment fancyEquipment;

    @Override
    public void onEnable() {
        fancyEquipment = this;
        //前置插件
        if(!DependPlugins.depend()){
            fancyEquipment.setEnabled(false);
            return;
        }
        //指令
        Objects.requireNonNull(Bukkit.getPluginCommand("fancyequipment")).setExecutor(new MainCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("fancyequipment")).setTabCompleter(new TabCommand());
        //玩家監聽
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), fancyEquipment);
        //執行任務
        Start.execute();
    }

    @Override
    public void onDisable() {
        //儲存玩家裝備
        if(languageConfig != null){
            FancyEquipment.fancyEquipment.getLogger().info(languageConfig.getString("LogMessage.StorageEquipment"));
        }else {
            FancyEquipment.fancyEquipment.getLogger().info("Store player equipment.");
        }

        for(PlayerEqmData playerEqmData : ManagerEqm.player_Data.values()){
            playerEqmData.saveEqmConfig();
        }
        FancyEquipment.fancyEquipment.getLogger().info("§4FancyEquipment uninstall.");
    }
}
