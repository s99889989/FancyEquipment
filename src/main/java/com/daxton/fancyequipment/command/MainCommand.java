package com.daxton.fancyequipment.command;


import com.daxton.fancyequipment.FancyEquipment;
import static com.daxton.fancyequipment.config.FileConfig.languageConfig;

import com.daxton.fancyequipment.gui.MainMenu;
import com.daxton.fancyequipment.task.Reload;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args){
        if(sender instanceof Player && !sender.isOp()){
            return true;
        }
        //重新讀取設定
        if(args.length == 1) {

            if(args[0].equalsIgnoreCase("gui")){
                if(sender instanceof Player){
                    Player player = (Player) sender;
                    MainMenu.open(player);
                }
            }

            if(args[0].equalsIgnoreCase("reload")){
                //重新讀取的一些程序
                Reload.execute();

                if(sender instanceof Player){
                    Player player = (Player) sender;
                    player.sendMessage(languageConfig.getString("OpMessage.Reload")+"");
                }
                FancyEquipment.fancyEquipment.getLogger().info(languageConfig.getString("LogMessage.Reload"));
            }

        }

        return true;
    }

}
