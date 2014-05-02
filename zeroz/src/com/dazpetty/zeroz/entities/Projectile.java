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

public class Projectile implements Poolable {
	public BodyDef bodyDef = new BodyDef();
	public Body body;
	public Sprite projsprite;
	public FixtureDef fixtureDef;
	public CircleShape dynamicCircle;
	private Texture projtex;
	public boolean isAlive = true;
	public boolean isDead = false;
	//public boolean isAI = false;
	public int id = 0;
	float rad = 0;
	public long spawntime = System.currentTimeMillis();
	public long nowtime = System.currentTimeMillis();
	
	
	public void killProj(){
		isDead = true;
		isAlive = false;
		body.setAwake(false);
		body.setActive(false);
	}
	
	public void checkAgeKill(){
		nowtime = System.currentTimeMillis();
		if (nowtime - spawntime > 4500) killProj(); 
	}
	
	public void reUseProjectile(Actor act, float angle, float speed){
		
		spawntime = System.currentTimeMillis();
		killProj();
		setupFixture(act.isAI);
		isAlive = true;
		isDead = false;
		
		
		body.setActive(true);
		body.setAwake(true);
		
		rad = (float) Math.toRadians(angle);
		float velx = (float) (speed * Math.cos(rad));
		float vely = (float) (speed * Math.sin(rad));
		
		body.setTransform(act.worldpos.x, act.worldpos.y+1, angle);
		body.setLinearVelocity(velx,vely);
	}
	
	public Projectile(Actor act, World world, int id, float angle, float speed) {
		bodyDef.type = BodyType.DynamicBody;  
		body = world.createBody(bodyDef);  
		bodyDef.position.set(act.worldpos.x, act.worldpos.y);  
		isAlive = true;
		isDead = false;
		
		body.setGravityScale(0);
		String str = Integer.toString(id);
		body.setUserData(str);
		body.setBullet(true);
		//bodyDef.
		rad = (float) Math.toRadians(angle);
		float velx = (float) (speed * Math.cos(rad));
		float vely = (float) (speed * Math.sin(rad));
		
		projtex = new Texture(Gdx.files.internal("data/gfx/reddot.png"));
		projtex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion projtexreg = new TextureRegion(projtex, 0, 0, 16,
				16);
		projsprite = new Sprite(projtexreg);
		projsprite.setSize(0.5f, 0.5f);
		projsprite.setOrigin(projsprite.getWidth() / 2, projsprite.getHeight() / 2);
		projsprite.setPosition(0f, 0f);
		fixtureDef = new FixtureDef(); 
	    dynamicCircle = new CircleShape();  
	    body.setTransform(act.worldpos.x, act.worldpos.y+1, angle);
	    body.setLinearVelocity(velx,vely);
	    
	    dynamicCircle.setRadius(0.2f);  
	    setupFixture(act.isAI);
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