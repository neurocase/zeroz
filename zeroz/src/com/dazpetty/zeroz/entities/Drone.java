package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;


public class Drone {

	public boolean isAlive = true;

	public Vector2 screenpos = new Vector2(0, 0);
	public Vector2 worldpos = new Vector2(0, 0);
	public Vector2 velocity = new Vector2(0, 0);
	public Vector2 relativepos = new Vector2(0, 0);
	public float distanceFromPlayer = 0;
	public Vector2 targetWorldVec = new Vector2(0, 0);
	public Vector2 bodyOffset = new Vector2(1f, 0.5f);
	
	
	public float acceleration = 1.0f;
	public float height = 1;
	public float width = 1;
	
	public BodyDef bodyDef = new BodyDef();
	public Body body;
	public FixtureDef fixtureDef;
	public CircleShape dynamicCircle;
	private Texture projtex;

	public boolean isDead = false;

	public int id = 0;
	float rad = 0;
	public long spawntime = System.currentTimeMillis();
	public long nowtime = System.currentTimeMillis();

	public Sprite sprite;
	public TextureRegion texRegion;
	public Texture texture;
	public TextureAtlas textureAtlas;
	float stateTime;
	
	public int startinghealth = 100;
	public int health = startinghealth;
	

	//public AtlasRegion atlasTexRegion;
	
	//public AtlasRegion runTexRegion;
	
	private int currentFrame = 1;
	private String currentAtlasKey = new String("0001");
	
	private float aimAtPlayer = 0;
	public float shootDist = 3;
	
	public Camera cam;
	public Drone(float x, float y, World world, int id, Camera camera) {
		cam = camera;
		BodyDef bodyDef = new BodyDef(); 
	    bodyDef.type = BodyType.DynamicBody;
    	body = world.createBody(bodyDef);
		

		textureAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/drone/drone.atlas"));
		AtlasRegion atlasTexRegion = textureAtlas.findRegion("0000");
		
		//atlasTexRegion.setRegion(textureAtlas.findRegion("0000"), 0, 0, 128, 128);
		//atlasTexRegion.setRegionWidth(1);
		//atlasTexRegion.setRegionHeight(32);
		//atlasTexRegion.setV2(2);
		//atlasTexRegion.set
	//	atlasTexRegion.setRegion(u, v, u2, v2);
		
		sprite = new Sprite(atlasTexRegion);
		worldpos = new Vector2(x, y);
		
		Vector3 tmpVec3 = new Vector3(worldpos.x,worldpos.y,0); 
		
		camera.project(tmpVec3);
		//sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		//sprite.setPosition(tmpVec3.x, tmpVec3.y);
	//	sprite.scale(0.1f);
		//sprite.setSize(1, 1);

        System.out.print("!!!!" + atlasTexRegion.getV()+ "," +  atlasTexRegion.getV2());
		//sprite.set
		//bodyDef.position.set(worldpos.x+0.5f, worldpos.y+1f);
		bodyDef.position.set(worldpos.x+bodyOffset.x, worldpos.y+bodyOffset.y);
		bodyDef.gravityScale = 0;
		body = world.createBody(bodyDef); 
		body.setUserData(id);
		fixtureDef = new FixtureDef(); 
		fixtureDef.isSensor = true;

	    PolygonShape pBox = new PolygonShape();
		fixtureDef.shape = pBox;
		pBox.setAsBox(1f, 0.5f);
    	fixtureDef.filter.categoryBits = 2;
	    Fixture fixture = body.createFixture(fixtureDef);
	    fixture.setUserData("drone");

	}
	
	public void updateFrame() {
          currentFrame++;
          if(currentFrame >= 25) 
        	  	currentFrame = 1;
          
          //currentFrame = 1;
          // ATTENTION! String.format() doesnt work under GWT for god knows why...
          currentAtlasKey = String.format("%04d", currentFrame);
          sprite.setRegion(textureAtlas.findRegion(currentAtlasKey));
  		
  		Vector3 tmpVec3 = new Vector3(worldpos.x,worldpos.y,0); 
		
		//cam.project(tmpVec3);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(tmpVec3.x, tmpVec3.y);
		sprite.setSize(2,1);
		sprite.setRotation(rotation);
		//sprite.setRotation(45);
		
		//sprite.setPosition(worldpos.x, worldpos.y);
		//sprite.scale(0.1f);
		//sprite.scale(1f);
	
    }

	public void reUseDrone(Vector2 actorstart){
		body.setActive(true);
    	body.setAwake(true);
    	//isDisposed = false;
    	//isOnLadder = false;
    	isDead = false;
    	isAlive = true;
    	worldpos = actorstart;
    	health = startinghealth;
	}
	

	
	
	public float rotation = 0;  
	public void update(Actor zplayer){
		
		
		body.setTransform(worldpos.x+bodyOffset.x, worldpos.y+bodyOffset.y, 0);
		updateFrame();
	
		
	//	worldpos.x = zplayer.worldpos.x;
	//	worldpos.y = zplayer.worldpos.y;
		
		//sprite.setPosition(zplayer.worldpos.x, worldpos.y);
		
		
		if (isAlive && zplayer.isAlive){
			//type = "enemy";
			float relativetoplayerx = zplayer.worldpos.x - worldpos.x ; 
			float relativetoplayery = zplayer.worldpos.y - worldpos.y;
			relativepos.x = relativetoplayerx;
			relativepos.y = relativetoplayery;
			distanceFromPlayer = (relativetoplayerx *relativetoplayerx) +(relativetoplayery * relativetoplayery);
			distanceFromPlayer = Math.abs((float) Math.sqrt(distanceFromPlayer));
			

			
			targetWorldVec.x = zplayer.worldpos.x;
			targetWorldVec.y = zplayer.worldpos.y;
			aimAtPlayer = relativepos.angle();
			//aimAngle = aimAtPlayer;
			if (relativetoplayerx < -shootDist){
				goLeft();
			}else if (relativetoplayerx > shootDist){
				goRight();
				
			}else{
				attemptShoot(relativepos.angle());
			}
			if (relativetoplayery < -shootDist){
				goDown();
			}else if (relativetoplayery > shootDist){
				goUp();
				
			}else{
				attemptShoot(relativepos.angle());
			}
			//if (relativetoplayery > 2){
				//goJump();
			//}
		}
		
		
	}

	public void goLeft() {
		worldpos.x -= 0.1;
		rotation = 20;
	}

	public void goRight() {
		worldpos.x += 0.1;
		rotation = -20;
	}
	
	public void goUp() {
		worldpos.y += 0.1;
	}

	public void goDown() {
		worldpos.y -= 0.1;
	}
	
	public void attemptShoot(float ang) {
	/*	if (isAlive){
			long timenow = System.currentTimeMillis();
			long a = timenow - lasttimeshoot;
			if (timenow - lasttimeshoot > (50 * 10)) {
				lasttimeshoot = System.currentTimeMillis();
				projMan.activeproj++;
				if (projMan.activeproj == projMan.PROJECTILE_LIMIT - 1)
					projMan.activeproj = 0;
				float speed = 25;
	
				if (projMan.proj[projMan.activeproj] == null) {
					projMan.proj[projMan.activeproj] = new Projectile(this, world, projMan.activeproj,
							ang, speed);
				}
				projMan.proj[projMan.activeproj].reUseProjectile(this, ang, speed);
			}
		} */
	}
	
	public void dispose(){
		textureAtlas.dispose();
	}
	
}
