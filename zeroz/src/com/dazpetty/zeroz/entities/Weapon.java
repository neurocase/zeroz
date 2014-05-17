package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.utils.Timer;

public class Weapon {


	
	public boolean isHolding = false;
	public int clipSize = 30;
	//public int accuracy = 50;
	
	//accuracy: higher=lessaccurate
	//rateoffire: higher=slower
	// 1 - Pistol, 2 - Uzi, 3 - Shotgun, 4 - Rifle, 5
	
	public int[] bulletspeedary = new int[]{100,50,50,3,4,5,6,7,8,9};
	public int[] accuracyscatterary = new int[]{10,20,40,3,4,5,6,7,8,40};
	public int[] ammoary = new int[]{0,1,2,3,4,5,6,7,8,9};
	public int[] shotsary = new int[]{1,1,12,3,4,5,6,7,8,12};
	public int[] damageary =  new int[]{15,15,2,3,4,5,6,7,8,1};
	public int[] rateoffiredelayary = new int[]{30,10,60,3,4,5,6,7,8,2};
	public int[] lifetimeary = new int[]{4000,100,2,3,4,5,6,7,8,9};
	
	public int bulletspeed = 0;
	public int accuracyscatter = 0;
	public int ammo = 0;
	public int shots = 0;
	public int damage =  0;
	public int rateoffiredelay = 0;
	public int lifetime = 0;
	
	public int weaponid = 1;
	
	public Weapon(int i){
		weaponid = i;
		bulletspeed = bulletspeedary[weaponid];
		accuracyscatter = accuracyscatterary[weaponid];;
		ammo = ammoary[weaponid];
		shots = shotsary[weaponid];
		damage =  damageary[weaponid];
		rateoffiredelay = rateoffiredelayary[weaponid];
		lifetime = lifetimeary[weaponid];
	}
	
	
	public String name = "defaultWeapon";
	//public int rateoffire = 40;

	
	public long lasttimeshoot = System.currentTimeMillis();
	//texture
	
	

	public boolean shoot(){
		
		long timenow = System.currentTimeMillis();
		long a = timenow - lasttimeshoot; 
		//System.out.print(a + ",");
		if (timenow - lasttimeshoot > (rateoffiredelayary[weaponid] * 10)){
			lasttimeshoot = System.currentTimeMillis();
			return true;
		}else{
			return false;	
		}
	}

}