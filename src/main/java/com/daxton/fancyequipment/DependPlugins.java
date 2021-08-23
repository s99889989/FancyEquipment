package com.daxton.fancyequipment;


import static com.daxton.fancyequipment.config.FileConfig.languageConfig;

import com.daxton.fancyequipment.config.FileConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class DependPlugins {

    public static boolean depend(){

        FancyEquipment fancyEquipment = FancyEquipment.fancyEquipment;

        if (Bukkit.getServer().getPluginManager().getPlugin("FancyCore") != null && Bukkit.getPluginManager().isPluginEnabled("FancyCore")){
            //設定檔
            FileConfig.execute();
            fancyEquipment.getLogger().info(languageConfig.getString("LogMessage.LoadFancyCore"));
        }else {
            fancyEquipment.getLogger().severe("*** FancyCore is not installed or not enabled. ***");
            fancyEquipment.getLogger().severe("*** FancyItemsy will be disabled. ***");
            return false;
        }

        return true;
    }

}
