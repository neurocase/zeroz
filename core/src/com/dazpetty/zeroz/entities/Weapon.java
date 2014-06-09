package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.dazpetty.zeroz.core.DazDebug;
import com.dazpetty.zeroz.managers.ProjectileManager;

public class Weapon {


	
	public boolean isHolding = false;
	public int clipSize = 30;
	//public int accuracy = 50;
	
	//accuracy: higher=lessaccurate
	//rateoffire: higher=slower
	// 1 - Sword, 2 - Uzi, 3 - Shotgun, 4 - Rifle, 5
	
	public String weaponName = "none";
	
	public String[] weaponnameary = new String[]{"sword","pistol","shotgun","uzi","grenade","five","six","seven","eight","nine"};
	
											//  0   1   2   3   4    5
	public int[] bulletspeedary = new int[]	   {100,20 ,20 ,3   ,10  ,10  ,6,7,8,9};
	public int[] accuracyscatterary = new int[]{10 ,20 ,40 ,3   ,1   ,5   ,6,7,8,40};
	public int[] ammoary = new int[]           {0  ,1  ,2  ,3   ,4   ,5   ,6,7,8,9};
	public int[] shotsary = new int[]          {1  ,1  ,3  ,3   ,1   ,1   ,6,7,8,12};
	public int[] damageary =  new int[]        {15 ,15 ,50 ,3   ,40  ,50  ,6,7,8,1};
	public int[] rateoffiredelayary = new int[]{30 ,25 ,60 ,3   ,60  ,60  ,6,7,8,2};
	public int[] lifetimeary = new int[]       {5  ,800,800,1400,1400,10000,6,7,8,9};
	
	public int bulletspeed = 0;
	public int accuracyscatter = 0;
	public int ammo = 0;
	public int shots = 0;
	public int damage =  0;
	public int rateoffiredelay = 0;
	public int lifetime = 0;
	
	//public ProjectileManager projMan;
	
	public int weaponid = 1;
	
	public Weapon(int i){
		newWeapon(i);
	}
	
	public void newWeapon(int i){
	//	this.projMan = projMan;
		weaponid = i;
		
		if (weaponid == 5){
			hasGravity = true;
		}else{
			hasGravity = false;
		}
		weaponName = weaponnameary[weaponid];
		shots = shotsary[weaponid];
		bulletspeed = bulletspeedary[weaponid];
		accuracyscatter = accuracyscatterary[weaponid];;
		ammo = ammoary[weaponid];
		damage =  damageary[weaponid];
		rateoffiredelay = rateoffiredelayary[weaponid];
		lifetime = lifetimeary[weaponid];
	}
	
	
	public String name = "defaultWeapon";
	//public int rateoffire = 40;

	
	public long lasttimeshoot = System.currentTimeMillis();
	//texture
	public boolean hasGravity = false;
	
	

	public boolean ready(){
		long timenow = System.currentTimeMillis();
		long a = timenow - lasttimeshoot; 
		if (a > (rateoffiredelayary[weaponid] * 10)){
			lasttimeshoot = System.currentTimeMillis();
			return true;
		}
		return false;
			
	}


	
	public void setWeapon(int itemWeaponNumber) {
		//if (item == null){ prin.t("ITEM NULL");}
		weaponid = itemWeaponNumber;
		weaponName = weaponnameary[weaponid];
		shots = shotsary[weaponid];
		bulletspeed = bulletspeedary[weaponid];
		accuracyscatter = accuracyscatterary[weaponid];;
		ammo = ammoary[weaponid];
		damage =  damageary[weaponid];
		rateoffiredelay = rateoffiredelayary[weaponid];
		lifetime = lifetimeary[weaponid];
		
	}

}