package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.dazpetty.zeroz.core.DazDebug;
import com.dazpetty.zeroz.managers.MyAssetManager;


/* The Projectile class holds data regarding the type, position and who owns each projectile,
 * projectiles are put into a circular array, managed by the ProjectileManager.
 * 
 * The Projectile class requires the following data:
 * 
 * 		HumanEntity pawn: The entity firing the projectile, for its position and whether or nor it is friendly to 
 * 		the player.
 * 			
 * 
 * 		Projectile Body handles transformations and sprite is assigned to that
 * 
 */


public class Projectile implements Poolable {
	public BodyDef bodyDef = new BodyDef();
	public Body body;
	public Sprite projsprite;
	public FixtureDef fixtureDef;
	public CircleShape dynamicCircle;
	private Texture projtex;
	public boolean isAlive = true;
	public boolean isDead = false;
	public int id = 0;
	float rad = 0;
	public long spawntime = System.currentTimeMillis();
	public long nowtime = System.currentTimeMillis();
	public long lifetime = 4500;
	public Weapon weapon;
	public MyAssetManager assetMan;
	
	
	public PawnEntity pawn;
	
	public float speed = 25;
	
	
	public boolean isAI(){
		return pawn.isAI;
	}
	
	public void killProj(){
		isDead = true;
		isAlive = false;
		body.setAwake(false);
		body.setActive(false);
	}
	
	public void checkAgeKill(){
		nowtime = System.currentTimeMillis();
		if (nowtime - spawntime > weapon.lifetime) killProj(); 
	}
	
	public void reUseProjectile(PawnEntity pawn, float angle,  Weapon newweapon){
		this.pawn = pawn;
		weapon = newweapon;
		speed = weapon.bulletspeed;
		spawntime = System.currentTimeMillis();
		killProj();
		setupFixture(pawn.isAI);
		
		isAlive = true;
		isDead = false;
		
		DazDebug.print("PROJECTILE:" + weapon.weaponName + "is firing");
		
		//weapon.
		float scatterammount = (float) (weapon.accuracyscatter * Math.random());
		
		angle -= (weapon.accuracyscatter/2);
		
		body.setActive(true);
		body.setAwake(true);
		
		rad = (float) Math.toRadians(angle+scatterammount);
		float velx = (float) (speed * Math.cos(rad));
		float vely = (float) (speed * Math.sin(rad));
		
		projsprite.setRotation(angle);
		body.setTransform(pawn.worldpos.x, pawn.worldpos.y+1, angle);
		body.setLinearVelocity(velx,vely);
	}
	
	public Projectile(PawnEntity pawn, World world, int id, float angle,  Weapon weapon, MyAssetManager assetMan) {
		this.assetMan = assetMan;
		this.id = id;
		this.weapon = weapon;
		speed = weapon.bulletspeed;
		bodyDef.type = BodyType.DynamicBody;  
		body = world.createBody(bodyDef);  
		bodyDef.position.set(pawn.worldpos.x, pawn.worldpos.y);  
		isAlive = true;
		isDead = false;
		
		body.setGravityScale(0);
		int str = id;
		body.setUserData(this);
		body.setBullet(true);
				
		
		TextureRegion projtexreg = new TextureRegion(assetMan.projtex, 0, 0,16,
				8);
		
		projsprite = new Sprite(projtexreg);
		projsprite.setSize(0.5f, 0.25f);
		projsprite.setOrigin(projsprite.getWidth() / 2, projsprite.getHeight() / 2);
		projsprite.setPosition(0f, 0f);
		fixtureDef = new FixtureDef(); 
	    dynamicCircle = new CircleShape();  
	 
	    
	    dynamicCircle.setRadius(0.2f);  
	    reUseProjectile(pawn, angle,  weapon);
	}
	
	public void setupFixture(boolean isAI){
		Filter a = fixtureDef.filter;
	    
	    fixtureDef.isSensor = true;
	    
	    fixtureDef.shape = dynamicCircle;  
	    
	    fixtureDef.density = 0.0f;  
	    fixtureDef.friction = 0.3f;  
	    fixtureDef.restitution = 0.6f;
	    
	    if (isAI){
	    	fixtureDef.filter.maskBits = 3;
	    	Fixture fixture = body.createFixture(fixtureDef);
	    	fixture.setUserData("aiproj");	
	    }else{
	    	fixtureDef.filter.maskBits = 3;
	    	Fixture fixture = body.createFixture(fixtureDef);
	    	fixture.setUserData("playerproj");
	    }
	}

	
	public Body getBody(){
		return body;
	}


	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}


	
}