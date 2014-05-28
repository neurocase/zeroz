package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.math.Vector2;

public class EntitySpawner {

	public Vector2 worldpos = new Vector2(0,0);
	public String type = "";
	public long lasttimespawn;
	public int spawncount = 1;
	public boolean isAI = true;
	//public String type;
	
	public EntitySpawner(float w, float h, String type, int spawncount){
		worldpos.x = w;
		worldpos.y = h;
		this.type = type;
		this.spawncount = spawncount;
		if (type.equals("player")){
			isAI = false;
		}
	}
	
	public boolean attemptSpawn(long timenow){
		if (lasttimespawn == 0){
			lasttimespawn = timenow;
		}
		 if (timenow - lasttimespawn > (50 * 50) && spawncount > 0){
			 spawncount--;
			 return true;
		 }else{
			 return false;
		 }
	}
	
	
	
}
