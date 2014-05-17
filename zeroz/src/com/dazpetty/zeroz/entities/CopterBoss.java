package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class CopterBoss {

	
	public boolean isAlive = true;
	public boolean engineAlive = true;
	
	public CopterTurret copterTurretOne;
	public CopterTurret copterTurretTwo;
	
	public Vector2 screenpos = new Vector2(0, 0);
	public Vector2 worldpos = new Vector2(0, 0);
	public Vector2 startpos = new Vector2(0, 0);
	public Vector2 velocity = new Vector2(0, 0);
	
	public Texture bossTexture;
	public TextureRegion bossTextureReg;
	public Sprite bossSprite;
	
	float rotangleMax = 3f;
	public float acceleration = 0.001f;
	public float rotation = 0;
	
	public float MaxXPos = -10;
	public float MinXPos = -11;
	
	public boolean debug = false;
	
	public int TURRET_LIMIT = 3;
	
	public CopterTurret copterTurret[] = new CopterTurret[TURRET_LIMIT];
	
	//public 
	
	public CopterBoss(float x, float y, World world){
		
		for (int i = 0; i < TURRET_LIMIT; i++){
			copterTurret[i] = new CopterTurret(x, y, world, -3.5f*i, i);
		//copterTurret[1] = new CopterTurret(x, y, world, -7f, 1);
		//copterTurret[2] = new CopterTurret(x, y, world, 0.5f, 2);
		}
		/*textureAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/drone/drone.atlas"));
		AtlasRegion atlasTexRegion = textureAtlas.findRegion("0000");*/
		
		bossTexture = new Texture(("data/gfx/battlecopter/bossmain.png"));
		TextureRegion bossTextureReg = new TextureRegion(bossTexture, 0, 0, 2048,
				1024);
		
		bossSprite = new Sprite(bossTextureReg);
		bossSprite.setSize(18, 9);
		bossSprite.setOrigin(bossSprite.getWidth()/2, 0);
		//bossSprite.setOrigin(originX, originY);
		
		//x = x + bossSprite.getOriginX()/2;
		screenpos.x = x;
		screenpos.y = y;
		
		MinXPos += x;
		MaxXPos += x;
		
		worldpos.x = x;
		worldpos.y = y;
	}
	
	boolean goRight = false;
	
	public float rotangle = 0;
	
	public void update(){
		
		
		for (int i = 0; i < copterTurret.length; i++){
			if (copterTurret[i] != null){
				copterTurret[i].update(this);
				copterTurret[i].update(this);
			}
		}
		
		worldpos.x += velocity.x;
		
	
		
		if (velocity.x < 0){
			if (rotangle < rotangleMax){
			rotangle += 0.1; 
			}
			
		}else{
			if (rotangle > -rotangleMax){
				rotangle -= 0.1; 
			}
			
		}
		bossSprite.setRotation(rotangle);
		
		if (worldpos.x < MinXPos){			
			if (debug) System.out.println("BOSSCOPTER GO RIGHT!");
			goRight = true;
			if (velocity.x < 0){ 
			velocity.x += (acceleration * 2);
			}
		}
		if(goRight && worldpos.x > MaxXPos){
			if (debug) System.out.println("BOSSCOPTER GO LEFT!");
			goRight = false;
			//velocity.x = 0;
			if (velocity.x > 0){ 
				velocity.x -= (acceleration * 2);
				}
		}
		if (debug) System.out.println("BOSSCOPTER X:" + worldpos.x + " Y:" + worldpos.y);
		if (goRight){
			goRight();
			
		}
		if (!goRight){
			goLeft();
		}
		
		//bossSprite.setPosition(worldpos.x, worldpos.y);
		bossSprite.setPosition(worldpos.x, 6);
	}
	
	public void goLeft(){
		if (Math.abs(velocity.x) > -1){
		velocity.x -= acceleration;	
		}
		
	//	worldpos.x -= 0.2;
	}
	public void goRight(){
		if (Math.abs(velocity.x) < 1){
			velocity.x += acceleration;
		}
		
	//	worldpos.x += 0.2;
	}
	
	
	
}
