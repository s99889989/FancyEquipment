package com.daxton.fancyequipment.gui.equipmentbar;

public class SetShow {


	//右鍵切換裝備顯示
	public static void right(ClickEqm outfit){

		if(outfit.eqmString.equalsIgnoreCase("Armor_Pants")){
			if(outfit.show){
				outfit.show = false;
				outfit.playerEqmData.bodyEntity.equipment(null, "HEAD");
			}else {
				if(outfit.playerEqmData.eqm_Map.get("Armor_Pants") != null){
					outfit.show = true;
					outfit.playerEqmData.bodyEntity.equipment(outfit.playerEqmData.eqm_Map.get("Armor_Pants"), "HEAD");
				}
			}

		}
		if(outfit.eqmString.equalsIgnoreCase("Armor_Back")){
			if(outfit.show){
				outfit.show = false;
				outfit.playerEqmData.bodyEntity.equipment(null, "OFFHAND");
			}else {
				if(outfit.playerEqmData.eqm_Map.get("Armor_Back") != null){
					outfit.show = true;
					outfit.playerEqmData.bodyEntity.equipment(outfit.playerEqmData.eqm_Map.get("Armor_Back"), "OFFHAND");
				}
			}

		}
		if(outfit.eqmString.equalsIgnoreCase("Armor_Tail")){
			if(outfit.show){
				outfit.show = false;
				outfit.playerEqmData.bodyEntity.equipment(null, "MAINHAND");
			}else {
				if(outfit.playerEqmData.eqm_Map.get("Armor_Tail") != null){
					outfit.show = true;
					outfit.playerEqmData.bodyEntity.equipment(outfit.playerEqmData.eqm_Map.get("Armor_Tail"), "MAINHAND");
				}
			}

		}
	}

}
