package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
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
	
	Texture texture = null;
	Texture textureopen = null;
	public boolean isFriendly = true;
	public Sprite sprite;
	public Sprite spriteopen;
	
	public BodyDef bodyDef = new BodyDef();
	public Body body;
	public FixtureDef fixtureDef;
	public boolean isAlive;
	public int keyValue;
	
	
	
	public void openDoor(){
		body.setAwake(false);
		body.setActive(false);
		sprite = spriteopen;
	}
	
	public Door(int x,int y,int key, World world){
		
		
		
		keyValue = key;
		
		
		textureopen = new Texture(("data/gfx/objects/dooropen.png"));
		textureopen.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		texture = new Texture(("data/gfx/objects/doorlocked.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion texturereg = new TextureRegion(texture, 0, 0, 32,
				64);
		TextureRegion textureregopen = new TextureRegion(textureopen, 0, 0, 32,
				64);
		
		isAlive = true;
		worldpos.x = x;
		worldpos.y = y;
		
		spriteopen = new Sprite(textureregopen);
		spriteopen.setSize(1f, 2f);
		spriteopen.setPosition(worldpos.x, worldpos.y);
		
		sprite = new Sprite(texturereg);
		sprite.setSize(1f, 2f);
		sprite.setPosition(worldpos.x, worldpos.y);
		//bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(worldpos.x+0.5f, worldpos.y+1f);
		bodyDef.gravityScale = 0;
		body = world.createBody(bodyDef); 
		
		fixtureDef = new FixtureDef(); 
		fixtureDef.isSensor = true;
	    PolygonShape pBox = new PolygonShape();
	    pBox.setAsBox(0.5f, 1f);
	    
	   
	    fixtureDef.shape = pBox;
	    Fixture fixture = body.createFixture(fixtureDef);
	}
}
