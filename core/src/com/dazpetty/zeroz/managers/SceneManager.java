package com.dazpetty.zeroz.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.dazpetty.zeroz.assets.ZeroAssetManager;
import com.dazpetty.zeroz.core.GameScreen;
import com.dazpetty.zeroz.entities.CopterBoss;
import com.dazpetty.zeroz.entities.Destroyable;
import com.dazpetty.zeroz.entities.Drone;
import com.dazpetty.zeroz.entities.Explosion;
import com.dazpetty.zeroz.entities.FlamerTurret;
import com.dazpetty.zeroz.entities.Item;
import com.dazpetty.zeroz.entities.PawnEntity;
import com.dazpetty.zeroz.entities.WallTurret;
import com.dazpetty.zeroz.nodes.Crusher;
import com.dazpetty.zeroz.nodes.Door;
import com.dazpetty.zeroz.nodes.EntitySpawner;
import com.dazpetty.zeroz.nodes.Mover;
import com.dazpetty.zeroz.nodes.PowerCable;
import com.dazpetty.zeroz.nodes.WorldVolume;
import com.dazpetty.zeroz.nodes.ZeroTimer;

/*
 *  Scene Manager maintains the arrays and lists of all the different types of objects in the scene, 
 *  it manages a scene and its objects. I created scene manager different objects, such as doors, projectiles, enemyspawners
 *  and the enemies themselves where in different areas of the code and I wanted to bunch all the arrays together.
 *  
 *  I think there might be a few stray variables out there, in some programs this class is simply called Scene, however, 
 *  I wont be needing to handle multiple scenes at once and I didn't want to conflict with the libGDX/box2d possible usage of 
 *  the word scene 
 */


public class SceneManager {
	
	public int ACTUATOR_LIMIT = 20;

	private static final ZeroAssetManager MyAssetManager = null;

	public int TOTAL_ZEROTIMERS = 0;
	public int KEYS_LIMIT = 10;
	public boolean[] keys = new boolean[KEYS_LIMIT];
	
	public final int DESTROYABLE_LIMIT = ACTUATOR_LIMIT;
	public final int DOOR_LIMIT = ACTUATOR_LIMIT;
	
	public int TOTAL_DESTROYABLES = 0;
	public int TOTAL_DOORS = 0;
	
	
	public Destroyable[] destroyable = new Destroyable[DESTROYABLE_LIMIT];
	public Door[] door = new Door[DOOR_LIMIT];
	
	public final int ENEMY_SPAWNER_LIMIT = ACTUATOR_LIMIT;
	public EntitySpawner playerSpawner;
	public EntitySpawner enemyspawner[] = new EntitySpawner[ACTUATOR_LIMIT];
	
	public int worldvolumecount = 0;
	public final int WORLD_VOLUME_LIMIT = ACTUATOR_LIMIT;
	public WorldVolume worldvolume[] = new WorldVolume[WORLD_VOLUME_LIMIT];
	
	public ProjectileManager projMan;
	public ProjectileManager aiProjMan;
	
	public final int ENEMY_LIMIT = ACTUATOR_LIMIT;
	public final int WALLTURRET_LIMIT = ACTUATOR_LIMIT;
	public final int FLAMER_TURRET_LIMIT = ACTUATOR_LIMIT;
	
	public int CRUSHER_LIMIT = ACTUATOR_LIMIT;
	
	public final int DRONE_LIMIT = ENEMY_LIMIT;
	public final int ITEM_LIMIT = 20;
	public final int PROJECTILE_LIMIT = 20;
	public final int EXPLOSION_LIMIT = 15;
	public int ZERO_TIMER_LIMIT = 20;
	
	public int MOVER_LIMIT = 20;

	public int TOTAL_EXPLOSIONS = 0;
	public int TOTAL_ITEMS = 0;
	
	

	public int enemycount = 0;
	public int dronecount = 0;
	public int wallturretcount = 0;
	public int zerotimercount = 0;
	public int crushercount = 0;
	public int flamerturretcount = 0;

	public CopterBoss copterBoss;

	public PawnEntity[] zenemy = new PawnEntity[ENEMY_LIMIT];

	public Item[] item = new Item[ITEM_LIMIT];
	public Drone[] drone = new Drone[DRONE_LIMIT];
	public Explosion[] explosion = new Explosion[EXPLOSION_LIMIT];
	public WallTurret[] wallturret = new WallTurret[WALLTURRET_LIMIT];
	public FlamerTurret[] flamerturret = new FlamerTurret[FLAMER_TURRET_LIMIT];
	
	public ZeroTimer[] zerotimer = new ZeroTimer[ZERO_TIMER_LIMIT];
	
	public Mover[] mover = new Mover[MOVER_LIMIT];
	
	
	public World world;
	public GameScreen gamescreen;
	public ZeroAssetManager assetMan;

	public int movercountvalue = 0;

	public int POWER_CABLE_LIMIT = 512;
	public PowerCable[] powercable = new PowerCable[POWER_CABLE_LIMIT];
	
	public int powercablecount = 0;

	public Crusher[] crusher = new Crusher[CRUSHER_LIMIT];
	
	public SceneManager(World world, GameScreen gamescreen){
		this.world = world;
		this.gamescreen = gamescreen;
		projMan = new ProjectileManager(PROJECTILE_LIMIT, world, MyAssetManager);
		aiProjMan = new ProjectileManager(PROJECTILE_LIMIT, world, MyAssetManager);
	}
	
	
	
	public void ReportScene(){
		
	}
	
	public void Draw(SpriteBatch batch){
		for (int i = 0; i < powercablecount; i++){
			if (powercable[i].render)
			powercable[i].sprite.draw(batch);
		}
		for (int i = 0; i < crushercount; i++){
			//if (powercable[i].render)
			crusher[i].sprite.draw(batch);
		}
		for (int i = 0; i < flamerturretcount; i++){
			//if (powercable[i].render)
			if (flamerturret[i] != null){
				flamerturret[i].sprite.draw(batch);
				flamerturret[i].flamessprite.draw(batch);
			}
		}
	}
	
	public void Update(){
		for (int i = 0; i < flamerturretcount; i++){
			if (flamerturret[i] != null){
				flamerturret[i].update();
			}
		}
		
	}
	
	
}
