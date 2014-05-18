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

public class Item {

	public Vector2 worldpos = new Vector2(0,0);
	public Vector2 screenpos = new Vector2(0,0);
	public Vector2 velocity = new Vector2(0,0); 
	Texture texture = null;
	public float height = 2;
	public float width = 1.25f;
	public TextureRegion textureRegion = null;
	public float rotDir = 0;

	public BodyDef bodyDef = new BodyDef();
	public Body body;
	public Sprite sprite;
	public FixtureDef fixtureDef;
	public boolean isAlive = true;
	public int addHealth =  20;
	
	
	public void removeItem(){
		body.setAwake(false);
		body.setActive(false);
		isAlive = false;
	}
	
	/*
	 *  HEALTH ITEM
	 *  0 = health
	 *   
	 */	
	
	
	public void changeWeapon(int weaponid){
		switch (weaponid) {
			case 1:
				
				break;
			case 2:
				
				break;
			default:
		}
		
	}
	
	public String itemType = "";
	public Item(float x, float y, int id, String itemTypeIn ,World world){
		/*
		 *  HEALTH ITEM
		 */	
		itemType = itemTypeIn;
		

		texture = new Texture(
				Gdx.files.internal("data/gfx/items/healthkit.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion itemTexReg = new TextureRegion(texture,
				0, 0, 64, 64);
		
		if(itemType.equals("shotgun")){
			texture = new Texture(
					Gdx.files.internal("data/gfx/items/shotgun.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);	
			itemTexReg = new TextureRegion(texture,
					0, 0, 128, 32);
			
		}

		if(itemType.equals("uzi")){
			texture = new Texture(
					Gdx.files.internal("data/gfx/items/uzi.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);	
			itemTexReg = new TextureRegion(texture,
					0, 0, 128, 32);
			
		}
		
		
		String str = Integer.toString(id);

		
		sprite = new Sprite(itemTexReg);
		if(itemType.equals("shotgun") || itemType.equals("uzi")){
			sprite.setSize(1.5f, 0.5f);
			
		}else{
		sprite.setSize(1f, 1f);
		}
		sprite.setPosition(x, y);
		//sprite.scale(1f);
		bodyDef.position.set(worldpos.x+0.5f, worldpos.y+1f);
		bodyDef.gravityScale = 0;
		body = world.createBody(bodyDef); 
		body.setUserData(id);
		body.setTransform(x+0.5f, y+0.5f, 0);
		
		fixtureDef = new FixtureDef(); 
		fixtureDef.isSensor = true;
	    PolygonShape pBox = new PolygonShape();
	    pBox.setAsBox(0.5f, 0.5f);
	    
	    fixtureDef.shape = pBox;
	    fixtureDef.filter.maskBits = 3;
	    Fixture fixture = body.createFixture(fixtureDef);
    	fixture.setUserData("item");
	}
	
	
}
