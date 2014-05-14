package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.math.Vector2;

public class EnemySpawner {

	public Vector2 worldpos = new Vector2(0,0);
	public String enemyType = "";
	public long lasttimespawn;
	
	public EnemySpawner(float w, float h, String spawnType){
		worldpos.x = w;
		worldpos.y = h;
		enemyType = spawnType;
		
	}
	
	
	
}
