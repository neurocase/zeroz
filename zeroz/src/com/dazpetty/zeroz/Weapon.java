package com.dazpetty.zeroz;

import com.badlogic.gdx.utils.Timer;

public class Weapon {


	
	public boolean isHolding = false;
	public int clipSize = 30;
	//public int accuracy = 50;
	
	//accuracy: higher=lessaccurate
	//rateoffire: higher=slower
	// 1 - Pistol, 2 - Uzi, 3 - Shotgun, 4 - Rifle, 5
	
	public int[] bulletspeed = new int[]{100,100,50,3,4,5,6,7,8,9};
	public int[] accuracyscatter = new int[]{10,20,40,3,4,5,6,7,8,40};
	public int[] ammo = new int[]{0,1,2,3,4,5,6,7,8,9};
	public int[] shots = new int[]{1,1,12,3,4,5,6,7,8,12};
	public int[] damage =  new int[]{15,15,2,3,4,5,6,7,8,1};
	public int[] rateoffiredelay = new int[]{30,10,60,3,4,5,6,7,8,2};
	public int[] falloffrate = new int[]{0,1,2,3,4,5,6,7,8,9};

	
	public String name = "defaultWeapon";
	//public int rateoffire = 40;

	
	public long lasttimeshoot = System.currentTimeMillis();
	//texture
	
	public int weaponid = 1;

	//


	public boolean shoot(){
		
		long timenow = System.currentTimeMillis();
		
		long a = timenow - lasttimeshoot; 
		//System.out.print(a + ",");
		if (timenow - lasttimeshoot > (rateoffiredelay[weaponid] * 10)){
			lasttimeshoot = System.currentTimeMillis();
			return true;
		}else{
			return false;	
		}
	}

}