package com.daxton.fancyequipment;

import static com.daxton.fancyequipment.config.FileConfig.languageConfig;

import com.daxton.fancycore.api.gui.GUI;
import com.daxton.fancycore.api.gui.GuiButtom;
import com.daxton.fancycore.other.task.PackEntity;
import com.daxton.fancycore.other.task.guise.GuiseEntity;
import com.daxton.fancyequipment.config.FileConfig;
import com.daxton.fancyequipment.gui.CloseAction;
import com.daxton.fancyequipment.gui.Outfit;
import com.daxton.fancyequipment.gui.SelectItem;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerEqmData {
	//玩家
	final public Player player;
	//UUID
	final public UUID uuid;
	//裝備GUI
	public GUI gui;
	//裝備欄
	public Map<String, ItemStack> eqm_Map = new HashMap();
	//特殊部位裝備顯示
	public GuiseEntity slimeEntity;
	public GuiseEntity headEntity;
	public GuiseEntity bodyEntity;
	//
	public BukkitRunnable bukkitRunnable;
	//物品欄
	public ItemStack[] bags = new ItemStack[36];
	//選擇的物品位置
	public int selectSlot = -1;
	//主手位置
	public int mainSlot = 0;
	//玩家裝備資料
	public PlayerEqmData(Player player){
		this.player = player;
		this.uuid = player.getUniqueId();
		//建立玩家設定檔
		createDataFile();
		//設置外觀盔甲架
		display();
		//設置初始裝備設定
		setDefaultEqm();
		//設置裝備欄
		setGui();

	}

	public void display(){

		//身體
		bodyEntity = new GuiseEntity(player.getLocation().add(2,2,0), "ARMOR_STAND", null, false, false, false);
		bodyEntity.setVisible(true);
		bodyEntity.equipment();
		bodyEntity.setArmorStandAngle("rightarm", 0 , 0, 0);
		bodyEntity.setArmorStandAngle("leftarm", 0 , 0, 0);
		bodyEntity.mount(player.getEntityId());

		//頭部
//		slimeEntity = new GuiseEntity(player.getLocation().add(0,2,0), "SLIME", null, true, false , false);
//		PackEntity.slimeSize(slimeEntity.getEntityID(), -4);
//		slimeEntity.setVisible(true);
//		//slimeEntity.mount(player.getEntityId());
//
//		PackEntity.mount(new int[]{bodyEntity.getEntityID(), slimeEntity.getEntityID()}, player.getEntityId());
//
//		headEntity = new GuiseEntity(player.getLocation().add(2,2,0), "ARMOR_STAND", null, false, false, false);
//		headEntity.setVisible(true);
//		headEntity.equipment();
//		headEntity.setArmorStandAngle("rightarm", 0 , 0, 0);
//		headEntity.setArmorStandAngle("leftarm", 0 , 0, 0);
//		headEntity.mount(slimeEntity.getEntityID());


		bukkitRunnable = new BukkitRunnable() {
			int k = 0;
			@Override
			public void run() {
				//double x = player.getLocation().getPitch();
				double y = player.getLocation().getYaw();

				bodyEntity.headRotation(y);

//				headEntity.setArmorStandAngle("head", x, 0, 0);
//				slimeEntity.headRotation(y);
//				new BukkitRunnable() {
//					@Override
//					public void run() {
//						headEntity.headRotation(y);
//					}
//				}.runTaskLater(FancyEquipment.fancyEquipment, 2);


			}
		};
		bukkitRunnable.runTaskTimer(FancyEquipment.fancyEquipment, 0, 1);


		//		PackEntity.createTeam(player.getUniqueId(), ChatColor.BLUE, player.getName());
//		PackEntity.setGlowing(player.getEntityId());
//		PackEntity.setGlowing(slimeEntity.getEntityID());
//		PackEntity.setGlowing(guiseEntity.getEntityID());
//		PackEntity.addEntity(slimeEntity.uuid, ChatColor.BLUE, player.getName());
//		PackEntity.addEntity(guiseEntity.uuid, ChatColor.BLUE, player.getName());
//		PackEntity.upTeam(player.getUniqueId(), ChatColor.BLUE, player.getName());

	}
	public double normalAbsoluteAngleDegrees(double angle) {
		return (angle %= 360) >= 0 ? angle : (angle + 360);
	}
	//把目前背包內容儲存到Map
	public void setBag(){
		int x = 0;
		for(ItemStack i : player.getInventory()){
			if(x < 36){
				bags[x] = i;
			}
			x++;
		}
	}

	//設置GUI
	public void setGui(){
		GUI gui = new GUI(player, 54, languageConfig.getString("Title"));

		FileConfiguration eqmConfig = FileConfig.config_Map.get("equipment.yml");
		eqmConfig.getConfigurationSection("").getKeys(false).forEach(eqmString -> {

			int row = eqmConfig.getInt(eqmString +".SlotRow");
			int columns = eqmConfig.getInt(eqmString +".SlotColumns");
			ItemStack itemStack = GuiButtom.valueOf(eqmConfig, eqmString);
			gui.setItem(itemStack, false, row, columns);
			String restrictionType = eqmConfig.getString(eqmString+".RestrictionType");


			FileConfiguration playerDataConfig = FileConfig.config_Map.get("playerdata/"+uuid+".yml");
			if(playerDataConfig.contains(eqmString)){
				ItemStack playerEqm = playerDataConfig.getItemStack(eqmString);
				gui.setItem(playerEqm, false, row, columns);
				gui.setAction(new Outfit(this, eqmString, restrictionType, playerEqm, row, columns), row, columns);
			}else {
				gui.setAction(new Outfit(this, eqmString, restrictionType, null, row, columns), row, columns);
			}

		});

		//背包內動作
		List<Integer> ing = new ArrayList<>();
		for(int i = 0; i < 36 ; i++){
			gui.addAction(new SelectItem(this), 55, 90, ing);
		}

		//關閉時清除Map物品包包
		gui.setCloseAction(new CloseAction(this));

		gui.setMoveAll(false);
		this.gui = gui;
	}
	//設置主手
	public void setMain(){
		ItemStack mainItem = player.getInventory().getItemInMainHand();
		FileConfiguration eqmConfig = FileConfig.config_Map.get("equipment.yml");
		int row = eqmConfig.getInt("Main"+".SlotRow");
		int columns = eqmConfig.getInt("Main"+".SlotColumns");
		ItemStack itemStack = GuiButtom.valueOf(eqmConfig, "Main");
		if(mainItem.getType() != Material.AIR){
			gui.setItem(mainItem, false, row, columns);
		}else {
			gui.setItem(itemStack, false, row, columns);
		}
	}

	//設置初始裝備
	public void setDefaultEqm(){
		FileConfiguration eqmConfig = FileConfig.config_Map.get("equipment.yml");
		eqmConfig.getConfigurationSection("").getKeys(false).forEach(eqmString-> eqm_Map.put(eqmString, null));
		FileConfiguration dataConfig = getDataFile();
		dataConfig.getConfigurationSection("").getKeys(false).forEach(eqmString->{
			ItemStack itemStack = dataConfig.getItemStack(eqmString);
			eqm_Map.put(eqmString, itemStack);

			if(itemStack != null){
				if(eqmString.equalsIgnoreCase("Armor_Pants")){
					bodyEntity.equipment(itemStack, "HEAD");
				}
				if(eqmString.equalsIgnoreCase("Armor_Back")){
					bodyEntity.equipment(itemStack, "OFFHAND");
				}
				if(eqmString.equalsIgnoreCase("Armor_Tail")){
					bodyEntity.equipment(itemStack, "MAINHAND");
				}
			}

		});


	}

	//建立玩家設定檔
	public void createDataFile(){
		File file = new File(FancyEquipment.fancyEquipment.getDataFolder(),"playerdata/"+uuid+".yml");
		if(!file.exists()){
			try {
				file.createNewFile();
			}catch (IOException exception){
				exception.printStackTrace();
			}
		}
	}
	//獲取玩家設定檔
	public FileConfiguration getDataFile(){
		File file = new File(FancyEquipment.fancyEquipment.getDataFolder(),"playerdata/"+uuid+".yml");
		return YamlConfiguration.loadConfiguration(file);
	}
	//把物品儲存到設定檔
	public void saveEqmConfig(){
		FileConfiguration playerDataConfig = getDataFile();
		File file = new File(FancyEquipment.fancyEquipment.getDataFolder(),"playerdata/"+uuid+".yml");
		eqm_Map.forEach(playerDataConfig::set);
		try {
			playerDataConfig.save(file);
		}catch (IOException exception){
			exception.printStackTrace();
		}
	}

}
