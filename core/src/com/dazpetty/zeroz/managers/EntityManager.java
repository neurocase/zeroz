package com.dazpetty.zeroz.managers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dazpetty.zeroz.assets.HumanSprite;
import com.dazpetty.zeroz.core.DazDebug;
import com.dazpetty.zeroz.entities.CopterTurret;
import com.dazpetty.zeroz.entities.Explosion;
import com.dazpetty.zeroz.entities.PawnEntity;
import com.dazpetty.zeroz.entities.CopterBoss;
import com.dazpetty.zeroz.entities.Destroyable;
import com.dazpetty.zeroz.entities.Drone;
import com.dazpetty.zeroz.entities.HUDTarget;
import com.dazpetty.zeroz.entities.Item;
import com.dazpetty.zeroz.entities.MuzzleFlash;
import com.dazpetty.zeroz.entities.Projectile;
import com.dazpetty.zeroz.entities.Weapon;
import com.dazpetty.zeroz.nodes.Door;
import com.dazpetty.zeroz.nodes.EntitySpawner;
import com.dazpetty.zeroz.nodes.WorldVolume;

/* The EntityManager class manages game entities, the enemies that the player encounters, the player character, the destroyable objects,
 * the doors, explosions, the ProjectileManager and items.
 * 
 *   NOTE: The Level Manager contains the enemy spawners
 */

public class EntityManager {


	public PawnEntity zplayer;
	
	public HumanSprite humanSprite = new HumanSprite();



	public OrthographicCamera camera;
	public World world;
	public HUDTarget hudtarget;
	public LevelManager levelMan;
	public EventManager eventMan;
	public SceneManager scene;
	public ZeroContactListener cl;
	public ContactHandler ch;


	ZeroAssetManager assetMan = new ZeroAssetManager();;

	public EntityManager(OrthographicCamera camera, World world,
			LevelManager levelMan) {

		this.scene = levelMan.scene;
		eventMan = levelMan.eventMan;
		this.camera = camera;
		this.world = world;
		this.levelMan = levelMan;
		hudtarget = new HUDTarget();

		ch = new ContactHandler();
		cl = new ZeroContactListener(ch);
		world.setContactListener(cl);
	}

	public void print(String str) {
		System.out.print(str);
	}

	public int item_poll = 0;

	public Item itemAtLoc(Vector2 vec) {
		item_poll++;
		if (item_poll >= scene.item.length)
			item_poll = 0;
		if (scene.item[item_poll] != null) {
			if (Math.abs(scene.item[item_poll].worldpos.x - vec.x) < 2) {
				if (Math.abs(scene.item[item_poll].worldpos.y - vec.y) < 2) {
					return scene.item[item_poll];
				}
			}
		}
		return null;
	}

	public void createExplosion(float x, float y, int id, float angle) {
		if (scene.TOTAL_EXPLOSIONS > scene.EXPLOSION_LIMIT - 1) {
			scene.TOTAL_EXPLOSIONS = 0;
		}
		scene.explosion[scene.TOTAL_EXPLOSIONS] = new Explosion(x, y, id, angle, assetMan);
	}

	/*
	 * public void flushSpawners(EntitySpawner spawner){ for (int i = 0; i <
	 * ENEMY_LIMIT; i++){ zenemy[i] = new PawnEntity(this, spawner);
	 * zenemy[i].isAlive = false; } initSpawn = true; }
	 */
	boolean initSpawn = false;


	public void checkBodies() {
		/*
		 * DEACTIVATE DOORS, ENEMIES AND DESTROYED OBJECTS
		 */
		for (int i = 0; i < scene.EXPLOSION_LIMIT; i++) {
			if (scene.explosion[i] != null) {
				scene.explosion[i].update();
			}
		}
		float damage = zplayer.weapon.damage;
		Array<PawnEntity> enemiesToDamage = ch.getEnemiesToDamage();
		if (enemiesToDamage != null) {
			for (int i = 0; i < enemiesToDamage.size; i++) {
				PawnEntity he = (PawnEntity) enemiesToDamage.get(i);
				DazDebug.print("AI: " + he.id + " takes "
						+ zplayer.weapon.damage + " damage.");
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
				DazDebug.print("Damaging Destroyable of key" + dest.getTriggerKey());
				dest.damageDestroyable(damage);
				//int killKeyValue = dest.id;
				if (dest.getTriggerKey() != 0){
				eventMan.CallTriggerValue(dest.getTriggerKey());
				}
				//zplayer.levelMan.keys[killKeyValue] = true;
			/*	createExplosion(dest.worldpos.x - 1, dest.worldpos.y, 1, 0);
				if (!dest.isAlive) {
					DazDebug.print("Destroyable " + dest.id + "is DEAD!");
					createExplosion(dest.worldpos.x - 1, dest.worldpos.y, 0, 0);
				}*/
			}
		}

		Array<Drone> droneToDamage = ch.getDronesToDamage();
		if (droneToDamage != null) {
			for (int i = 0; i < droneToDamage.size; i++) {
				Drone drone = (Drone) droneToDamage.get(i);
				drone.takeDamage(damage);
			}
		}

		Array<WorldVolume> worldVolumesToTrigger = ch.getWorldVolumesToTrigger();
		if (worldVolumesToTrigger != null) {
			for (int i = 0; i < worldVolumesToTrigger.size; i++) {
				WorldVolume wv = (WorldVolume) worldVolumesToTrigger.get(i);
				wv.triggerVolumeOn();
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
		for (int i = 0; i < scene.ENEMY_LIMIT; i++) {
			scene.zenemy[i].dispose();
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
