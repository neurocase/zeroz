package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;




public class Destroyable {

	
	
	public Vector2 worldpos = new Vector2(0,0);
	public Vector2 screenpos = new Vector2(0,0);
	public Vector2 velocity = new Vector2(0,0); 
	Texture texture = null;
	Texture texturedest = null;
	public float height = 2;
	public float width = 1.25f;
	public TextureRegion textureRegion = null;
	public boolean isFriendly = true;
	public boolean isAI = true;
	public Sprite sprite;
	public Sprite spritedest;
	
	public BodyDef bodyDef = new BodyDef();
	public Body body;
	public FixtureDef fixtureDef;
	public boolean isAlive;
	public int id = 0;
	
	public int health = 50;
	


	public void Destroy(){
		body.setAwake(false);
		body.setActive(false);
		sprite = spritedest;
	}
	
	public Destroyable(int x, int y, int id, World world){
		
		this.id = id;
		texture = new Texture(("data/gfx/objects/keyfuse.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		texturedest = new Texture(("data/gfx/objects/keyfusedestroyed.png"));
		texturedest.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		isAlive = true;
		worldpos.x = x;
		worldpos.y = y;
		TextureRegion texturereg = new TextureRegion(texture, 0, 0, 32,
				64);
		TextureRegion textureregdest = new TextureRegion(texturedest, 0, 0, 32,
				64);
		
		spritedest = new Sprite(textureregdest);
		spritedest.setSize(1f, 2f);
		spritedest.setPosition(worldpos.x, worldpos.y);
		
		sprite = new Sprite(texturereg);
		sprite.setSize(1f, 2f);
		sprite.setPosition(worldpos.x, worldpos.y);
		bodyDef.position.set(worldpos.x+0.5f, worldpos.y+1f);
		bodyDef.gravityScale = 0;
		body = world.createBody(bodyDef); 
		
		fixtureDef = new FixtureDef(); 
		fixtureDef.isSensor = true;
	    PolygonShape pBox = new PolygonShape();
	    pBox.setAsBox(0.5f, 1f);
	    
	    body.setUserData(this);
	    fixtureDef.shape = pBox;
    	fixtureDef.filter.categoryBits = 2;
	    Fixture fixture = body.createFixture(fixtureDef);
	    fixture.setUserData("destroyable");
	}

	public void damageDestroyable(float damage) {
		// TODO Auto-generated method stub
		health -= damage;
		if (health < 0){
			Destroy();
		}
		
	}
}
