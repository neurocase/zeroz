package com.dazpetty.zeroz.managers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dazpetty.zeroz.core.DazDebug;
import com.dazpetty.zeroz.entities.CopterTurret;
import com.dazpetty.zeroz.entities.Explosion;
import com.dazpetty.zeroz.entities.PawnEntity;
import com.dazpetty.zeroz.entities.CopterBoss;
import com.dazpetty.zeroz.entities.Destroyable;
import com.dazpetty.zeroz.entities.Door;
import com.dazpetty.zeroz.entities.Drone;
import com.dazpetty.zeroz.entities.EntitySpawner;
import com.dazpetty.zeroz.entities.HUDTarget;
import com.dazpetty.zeroz.entities.HumanSprite;
import com.dazpetty.zeroz.entities.Item;
import com.dazpetty.zeroz.entities.MuzzleFlash;
import com.dazpetty.zeroz.entities.Projectile;
import com.dazpetty.zeroz.entities.Weapon;






/* The EntityManager class manages game entities, the enemies that the player encounters, the player character, the destroyable objects,
 * the doors, explosions, the ProjectileManager and items.
 * 
 * 
 */



public class EntityManager {

	public final int ENEMY_LIMIT = 10;
	

	public final int DESTROYABLE_LIMIT = 10;
	public final int DOOR_LIMIT = 10;
	public final int DRONE_LIMIT = ENEMY_LIMIT;
	public final int ITEM_LIMIT = 20;
	public final int PROJECTILE_LIMIT = 20;
	public final int EXPLOSION_LIMIT = 15;

	public int TOTAL_DESTROYABLES = 0;
	public int TOTAL_DOORS = 0;
	public int TOTAL_EXPLOSIONS = 0;
	public int TOTAL_ITEMS = 0;


	public int enemycount = 0;
	public int dronecount = 0;


	

	public CopterBoss copterBoss;// = new CopterBoss();

	public PawnEntity[] zenemy = new PawnEntity[ENEMY_LIMIT];
	public Destroyable[] destroyable = new Destroyable[DESTROYABLE_LIMIT];
	public Door[] door = new Door[DOOR_LIMIT];
	public Item[] item = new Item[ITEM_LIMIT];
	public Drone[] drone = new Drone[DRONE_LIMIT];
	public Explosion[] explosion = new Explosion[EXPLOSION_LIMIT];

	public HumanSprite humanSprite = new HumanSprite();

	public PawnEntity zplayer;

	public OrthographicCamera camera;
	public World world;
	public HUDTarget hudtarget;
	public LevelManager levelMan;

	public DazContactListener cl;
	public ContactHandler ch;
	public ProjectileManager projMan;
	public ProjectileManager aiProjMan;
	
	MyAssetManager assetMan = new MyAssetManager();;

	public EntityManager(OrthographicCamera camera, World world, LevelManager levelMan) {
		
		projMan = new ProjectileManager(PROJECTILE_LIMIT, world, assetMan);
		aiProjMan = new ProjectileManager(PROJECTILE_LIMIT, world, assetMan);

		this.camera = camera;
		this.world = world;
		this.levelMan = levelMan;
		hudtarget = new HUDTarget();

		ch = new ContactHandler();
		cl = new DazContactListener(ch);
		world.setContactListener(cl);
	}

	public void print(String str) {
		System.out.print(str);
	}

	public int item_poll = 0;

	public Item itemAtLoc(Vector2 vec) {
		item_poll++;
		if (item_poll >= item.length)
			item_poll = 0;
		if (item[item_poll] != null) {
			if (Math.abs(item[item_poll].worldpos.x - vec.x) < 2) {
				if (Math.abs(item[item_poll].worldpos.y - vec.y) < 2) {
					return item[item_poll];
				}
			}
		}
		return null;
	}

	
	
	
	public void createExplosion(float x, float y, int id, float angle) {
		if (TOTAL_EXPLOSIONS > EXPLOSION_LIMIT - 1) {
			TOTAL_EXPLOSIONS = 0;
		}
		explosion[TOTAL_EXPLOSIONS] = new Explosion(x, y, id, angle, assetMan);
	}

	public void createActor(int s, EntitySpawner spawner) {
		long timenow = System.currentTimeMillis();

		if (spawner.type.equals("player")){
			DazDebug.print("SPAWNING PLAYER AT, X:" + spawner.worldpos.x + " Y:" + spawner.worldpos.y);
			zplayer = new PawnEntity(this, spawner);
		}
	}

	public void checkBodies() {

		/*
		 * DEACTIVATE DOORS, ENEMIES AND DESTROYED OBJECTS
		 */
		
		for (int i = 0; i < EXPLOSION_LIMIT; i++){
			if (explosion[i] != null){
				explosion[i].update();
			}
		}
		
		
		
		float damage = zplayer.weapon.damage;
		Array<PawnEntity> enemiesToDamage = ch.getEnemiesToDamage();
		if (enemiesToDamage != null) {
			for (int i = 0; i < enemiesToDamage.size; i++) {
				PawnEntity he = (PawnEntity) enemiesToDamage.get(i);
				DazDebug.print("AI: " + he.id + " takes " + zplayer.weapon.damage
						+ " damage.");
				he.takeDamage(damage);
			}
		}

		Array<Projectile> projToRemove = ch.getProjToRemove();
		if (projToRemove != null) {
			for (int i = 0; i < projToRemove.size; i++) {
				Projectile proj = (Projectile) projToRemove.get(i);
				DazDebug.print("Kill Projectile:" + proj.id);
				createExplosion(proj.getBody().getPosition().x, proj.getBody()
						.getPosition().y, 0, (float) (Math.toDegrees(proj
						.getBody().getAngle())));
				proj.killProj();
			}
		}

		Array<Destroyable> destroyablesToDamage = ch.getDestroyablesToDamage();
		if (destroyablesToDamage != null) {
			for (int i = 0; i < destroyablesToDamage.size; i++) {
				Destroyable dest = (Destroyable) destroyablesToDamage.get(i);
				DazDebug.print("Damaging Destroyable " + dest.id);
				dest.damageDestroyable(damage);
				int killKeyValue = dest.id;
				zplayer.levelMan.keys[killKeyValue] = true;
				createExplosion(dest.worldpos.x-1, dest.worldpos.y, 1, 0);
				if (!dest.isAlive){
					DazDebug.print("Destroyable " + dest.id + "is DEAD!");
					createExplosion(dest.worldpos.x-1, dest.worldpos.y, 0, 0);
				}
				for (int j = 0; j < DOOR_LIMIT; j++) {
					if (door[j] != null) {
						if (zplayer.levelMan.keys[door[j].keyValue]) {
							door[j].openDoor();
						}
					}
				}
			}
		}

		Array<Drone> droneToDamage = ch.getDronesToDamage();
		if (droneToDamage != null) {
			for (int i = 0; i < droneToDamage.size; i++) {
				Drone drone = (Drone) droneToDamage.get(i);
				drone.takeDamage(damage);
			}
		}

		Array<CopterTurret> copterTurretToDamage = ch.getCopterTurretToDamage();
		if (copterTurretToDamage != null) {
			for (int i = 0; i < copterTurretToDamage.size; i++) {
				CopterTurret tur = (CopterTurret) copterTurretToDamage.get(i);
				tur.takeDamage(damage);
			}
		}

		ch.clearCollisions();

	}

	public void dispose() {
		for (int i = 0; i < ENEMY_LIMIT; i++) {
			zenemy[i].dispose();
		}
		humanSprite.dispose();
		zplayer.dispose();
	}

	public void Pickup(Item pickUpItem) {
		DazDebug.print("player picking up " + pickUpItem.itemType);
		if (pickUpItem.isWeapon) {
			int holdId = zplayer.weapon.weaponid;
			zplayer.weapon.weaponid = pickUpItem.itemWeaponNumber;
			pickUpItem.itemWeaponNumber = holdId;
			pickUpItem.dropWeapon(holdId);
		} else {
			if (pickUpItem.itemType.equals("health")) {
				DazDebug.print("Give player health" + pickUpItem.addHealth);
				zplayer.health += pickUpItem.addHealth;
				pickUpItem.removeItem();
			}
		}

	}

}
