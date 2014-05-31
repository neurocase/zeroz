package com.dazpetty.zeroz.managers;

import com.badlogic.gdx.physics.box2d.World;
import com.dazpetty.zeroz.core.DazDebug;
import com.dazpetty.zeroz.entities.PawnEntity;
import com.dazpetty.zeroz.entities.MuzzleFlash;
import com.dazpetty.zeroz.entities.Projectile;
import com.dazpetty.zeroz.entities.Weapon;

public class ProjectileManager {

	public int activeproj = 0;
	public int PROJECTILE_LIMIT = 20;
	public Projectile[] proj = new Projectile[20];
	public final int MUZZLE_FLASH_LIMIT = 10;
	public int TOTAL_MUZZLE_FLASH = 0;
	
	private int activemuzzflash = 0;
	public MuzzleFlash muzzleflash[] = new MuzzleFlash[MUZZLE_FLASH_LIMIT];
	public World world;
	public ZeroAssetManager assetMan;

	public ProjectileManager(int proj_limit, World world, ZeroAssetManager assetMan) {
		this.assetMan = assetMan;
		PROJECTILE_LIMIT = proj_limit;
		Projectile[] proj = new Projectile[PROJECTILE_LIMIT];
	}

	public void createMuzzFlash(PawnEntity entity) {
		activemuzzflash++;
		if (activemuzzflash > MUZZLE_FLASH_LIMIT-1){
			activemuzzflash = 0;
		}
		muzzleflash[activemuzzflash] = new MuzzleFlash(entity , assetMan);
	}
	
	
	public void updateProjectiles() {
		for (int i = 0; i < MUZZLE_FLASH_LIMIT; i++){
			if (muzzleflash[i] != null){
				muzzleflash[i].update();
			}
		}
		
		
		for (int i = 0; i < PROJECTILE_LIMIT; i++) {
			if (proj[i] != null) {
				proj[i].checkAgeKill();
			}
		}
	}

	public void KillProjectile(int index) {
		if (proj[index] != null) {
			proj[index].killProj();
		}
	}
	public void setWorld(World world){
		this.world = world;
	}

	public void shootProjectile(float ang, PawnEntity entityShooting) {
		if (entityShooting.weapon.ready()) {
			createMuzzFlash(entityShooting);
			long soundId = 1;
			if (SoundManager.soundOn){
			switch (entityShooting.weapon.weaponid){
			//assetMan.pistolSound.setVolume(soundId, 0f);
			
				case 0:
					
					break;
				case 1:
					assetMan.pistolSound.play();
					break;
				case 2:
					assetMan.shotgunSound.play();
					break;
				default:
					assetMan.pistolSound.play();
			}
			}
			for (int i = 0; i < entityShooting.weapon.shots; i++) {

				DazDebug.print("shooting, " + entityShooting.weapon.weaponName + "," + entityShooting.weapon.shots
						+ " shots, weaponid: " + entityShooting.weapon.weaponid);
				activeproj++;
				if (activeproj == PROJECTILE_LIMIT - 1)
					activeproj = 0;

				if (proj[activeproj] == null) {
					proj[activeproj] = new Projectile(entityShooting, world,
							activeproj, ang, entityShooting.weapon, assetMan);
				}
				proj[activeproj].reUseProjectile(entityShooting, ang, entityShooting.weapon);
				// return false;
			}
		}
	}

}
