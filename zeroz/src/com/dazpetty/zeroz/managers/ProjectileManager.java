package com.dazpetty.zeroz.managers;

import com.badlogic.gdx.physics.box2d.World;
import com.dazpetty.zeroz.entities.HumanEntity;
import com.dazpetty.zeroz.entities.Projectile;
import com.dazpetty.zeroz.entities.Weapon;

public class ProjectileManager {

	public int activeproj = 0;
	public int PROJECTILE_LIMIT = 20;
	public Projectile[] proj = new Projectile[20];
	public World world;

	public ProjectileManager(int proj_limit, World world) {
		PROJECTILE_LIMIT = proj_limit;
		Projectile[] proj = new Projectile[PROJECTILE_LIMIT];
	}

	public void updateProjectiles() {
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

	public void shootProjectile(float ang, HumanEntity entityShooting) {
		if (entityShooting.weapon.ready()) {
			for (int i = 0; i < entityShooting.weapon.shots; i++) {

				prin.t("shooting, " + entityShooting.weapon.weaponName + "," + entityShooting.weapon.shots
						+ " shots, weaponid: " + entityShooting.weapon.weaponid);
				activeproj++;
				if (activeproj == PROJECTILE_LIMIT - 1)
					activeproj = 0;

				if (proj[activeproj] == null) {
					proj[activeproj] = new Projectile(entityShooting, world,
							activeproj, ang, entityShooting.weapon);
				}
				proj[activeproj].reUseProjectile(entityShooting, ang, entityShooting.weapon);
				// return false;
			}
		}
	}

}
