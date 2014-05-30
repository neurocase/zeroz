package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.math.Vector2;
import com.dazpetty.zeroz.core.DazDebug;
import com.dazpetty.zeroz.managers.EntityManager;
import com.dazpetty.zeroz.managers.SceneManager;

public class EntitySpawner {

	public Vector2 worldpos = new Vector2(0,0);
	public String type = "";
	public long lasttimespawn = 0;
	public int spawncount = 0;
	public boolean isAI = true;
	public int TOTAL_SPAWNS;
	public int delay = 500;
	public Weapon weapon = new Weapon(1); 
	//public String type;
	public boolean triggerOn = false;
	EntityManager entMan;
	int triggerkey = 0;
	
	public SceneManager scene;
	
	public EntitySpawner(float w, float h, String type, int TOTAL_SPAWNS, int triggerkey, EntityManager entMan){
		
		this.scene = entMan.scene;
		
		this.triggerkey = triggerkey;
		DazDebug.print("-=-=-=-=+++++++++++++++++++++++++++++++++++=-=-=");
		DazDebug.print("-=-=-=-SPAWNER::" + type + ":" + TOTAL_SPAWNS + "::-=-=");
		DazDebug.print("-=-=-=-=+++++++++++++++++++++++++++++++++++=-=-=");
		this.entMan = entMan;
		worldpos.x = w;
		worldpos.y = h+ 0.5f;
		this.type = type;
		this.TOTAL_SPAWNS = TOTAL_SPAWNS;
		if (type.equals("player")){
			isAI = false;
			
		}
		if (type.equals("triggered")){
			triggerOn = false;
			//this.TOTAL_SPAWNS = 1;
		}else{
			triggerOn = true;
		}
	}
	public void triggerOn(int triggerkey) {
		if (this.triggerkey == triggerkey){
			triggerOn = true;
			if (type.equals("triggered")){
			DazDebug.print("-=-=-=-=+++++++++++++++++++++++++++++++++++=-=-=");
			DazDebug.print("-=-=-=-=--=-=-=-=-TRIGGERED:" + type + "-=-=-=-=");
			DazDebug.print("-=-=-=-=+++++++++++++++++++++++++++++++++++=-=-=");
				//createActor();
			}
		}
	}
	public void triggerOff() {
		triggerOn = false;
	}
	

	public boolean isReady() {
		long timenow = System.currentTimeMillis();
		if ((timenow - lasttimespawn > delay) || type.equals("triggered")){
			if (spawncount < TOTAL_SPAWNS && triggerOn){
				DazDebug.print("-=-=-=-=+++++++++++++++++++++++++++++++++++=-=-=");
				DazDebug.print(":SPAWNER:" + type + ":|:SPAWNCOUNT:" + spawncount + "");
				DazDebug.print("-=-=-=-=+++++++++++++++++++++++++++++++++++=-=-=");
				return true;
			}
			if (triggerOn = false){
				DazDebug.print("-=--=--=--=--=--=-Spawner not ready-=--=--=--=--=--=--=-");
			}
		}
		return false;
		
	}
	
	public void SpawnSuccessful() {
		long timenow = System.currentTimeMillis();
		lasttimespawn = timenow;
		spawncount++;
		spawnSuccessfull = true;
	}
	
	boolean spawnSuccessfull = false;

	public void createActor() {
		spawnSuccessfull = false;
		if (scene.enemycount >= scene.ENEMY_LIMIT) {
			scene.enemycount = 0;
		}
		if (type.equals("triggered")){
			
			//DazDebug.print("TRIGGER SPAWN TRYING TO CREATE ACTOR");
		}
		
		if (isReady()) {
			
			if (scene.zenemy[scene.enemycount] == null) {
				scene.zenemy[scene.enemycount] = new PawnEntity(entMan, this);
				SpawnSuccessful();
				if (type.equals("triggered") && spawnSuccessfull)
					DazDebug.print("TRIGGER SPAWN SUCCESS");
				

			} else if (!scene.zenemy[scene.enemycount].isAlive
					|| Math.abs(scene.zenemy[scene.enemycount].worldpos.x
							- entMan.zplayer.worldpos.x) > 22) {
				scene.zenemy[scene.enemycount].useEntity(this);
				SpawnSuccessful();
			}
			scene.enemycount++;
			
		}
	}
}
