package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Door {

	
	
	
	public boolean isOpen = false;
	
	public Vector2 worldpos = new Vector2(0,0);
	public Vector2 screenpos = new Vector2(0,0);
	public Vector2 velocity = new Vector2(0,0); 
	public float height = 2;
	public float width = 1.25f;
	public TextureRegion textureRegion = null;
	public float rotDir = 0;
	
	public Sprite doorsprite;
	
	public Sprite sprite;
	public TextureRegion texRegion;
	public Texture texture;
	public TextureAtlas textureAtlas;
	float stateTime;
	private int currentFrame = 1;
	private String currentAtlasKey = new String("0001");
	
	
	
	
	
	
	public Body body;
	public FixtureDef fixtureDef;
	public Fixture doorfix;
	public boolean isAlive;
	public int keyValue;
	public boolean openDoor = false;
	
	
	public void openDoor(int triggerkeyin){
		
		if (keyValue == triggerkeyin && !openDoor){
			if (body.isAwake() && (body.isActive())){
			body.setAwake(false);
			body.setActive(false);
			openDoor = true;
			}
			
		}
	}
	
	public boolean checkKey(int triggerkeyin){
		if (keyValue == triggerkeyin){
			return true;
		}
		return false;
	}
	
	public void closeDoor(){
		body.setAwake(false);
		body.setActive(false);
		openDoor = false;
	}
	
	public void setFrame(int frame){

	        	  	currentFrame = frame;
	
	          currentAtlasKey = String.format("%04d", currentFrame);
	          sprite.setRegion(textureAtlas.findRegion(currentAtlasKey));
	  		
	  		Vector3 tmpVec3 = new Vector3(worldpos.x,worldpos.y,0); 
	
			sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
			sprite.setPosition(tmpVec3.x, tmpVec3.y);
			sprite.setSize(1,2);
	}
	
	public Door(int x,int y,int key, World world){
		textureAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/objects/door1.atlas"));
		AtlasRegion atlasTexRegion = textureAtlas.findRegion("0001");
		sprite = new Sprite(atlasTexRegion);

		sprite.setSize(1, 2);
		keyValue = key;
		
	
		
		isAlive = true;
		worldpos.x = x;
		worldpos.y = y;


		BodyDef doorbodyDef = new BodyDef();
		doorbodyDef.position.set(worldpos.x+0.5f, worldpos.y+1f);
		doorbodyDef.type = BodyType.StaticBody;
		body = world.createBody(doorbodyDef); 
		
		FixtureDef doorfixDef = new FixtureDef();
		
		
		
		 PolygonShape pBox = new PolygonShape();
		    pBox.setAsBox(0.5f, 1f);

		    doorfixDef.shape = pBox;
		    
		doorfix = body.createFixture(doorfixDef);
		doorfix.setUserData(this);	
		
		doorfixDef.friction = 0;
		
		body.createFixture(doorfixDef);
		setFrame(1);
	}
	//Projectile mask = 3,  fixtureDef.filter.maskBits = 3;


	public void updateFrame() {
		if (openDoor){
			if (currentFrame < 18){
				currentFrame++;
			}
			setFrame(currentFrame);
			
		}
		
	}
}
