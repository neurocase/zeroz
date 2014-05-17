package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.math.Vector2;

public class EnemySpawner {

	public Vector2 worldpos = new Vector2(0,0);
	public String enemyType = "";
	public long lasttimespawn;
	public int spawncount = 1;
	
	public EnemySpawner(float w, float h, String spawnType, int spawncountin){
		worldpos.x = w;
		worldpos.y = h;
		enemyType = spawnType;
		spawncount = spawncountin;
		
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
