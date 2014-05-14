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
	
	
	public Item(float x, float y, int id, World world){
		/*
		 *  HEALTH ITEM
		 */
		texture = new Texture(
				Gdx.files.internal("data/gfx/items/healthkit.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion itemTexReg = new TextureRegion(texture,
				0, 0, 64, 64);
		String str = Integer.toString(id);

		
		sprite = new Sprite(itemTexReg);
		sprite.setSize(1f, 1f);
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
