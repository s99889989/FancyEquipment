package com.daxton.fancyequipment.task;


import com.daxton.fancyequipment.config.FileConfig;

public class Reload {
    //重新讀取的一些任務
    public static void execute(){
        //設定檔
        FileConfig.reload();

    }

}
