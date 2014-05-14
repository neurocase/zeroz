package com.dazpetty.zeroz.managers;

import com.dazpetty.zeroz.entities.Projectile;

public class ProjectileManager {
	
	public int activeproj = 0;
	public int PROJECTILE_LIMIT = 20;
	public Projectile[] proj = new Projectile[20];
	
	public ProjectileManager(int proj_limit){
		PROJECTILE_LIMIT = proj_limit;
		Projectile[] proj = new Projectile[PROJECTILE_LIMIT];
	}
	
	public void KillProjectile(int index){
		proj[index].killProj();
	}
	
}
