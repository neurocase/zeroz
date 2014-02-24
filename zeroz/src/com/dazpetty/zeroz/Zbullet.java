package com.dazpetty.zeroz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Zbullet extends Zobject{
	
	public boolean initialized = false;
	protected TiledMapTileLayer collisionLayer;
	public Texture texture;
	public Sprite sprite;
	public Vector2 position = new Vector2(0,0);
	public Vector2 velocity = new Vector2(0,0); 
	public Vector2 aimVec = new Vector2(0,0);
	SpriteBatch                     spritebatch; 
	int distance = 0;
	//public float rotDir = 0;
	float speed = 12;
	
	public void create(TiledMapTileLayer cLayer, Vector2 posVec, double angle){
		collisionLayer = cLayer;
		initialized = true;
		position.x = posVec.x;
		position.y = posVec.y;
		//position = posVec;
		//Gdx.graphics.getWidth()/2-width*32,Gdx.graphics.getHeight()/2
		
		double rad = Math.toRadians(angle);
		
		velocity.x = (float) (speed * Math.cos(rad));
		velocity.y = (float) (speed * Math.sin(rad));
		
		
		System.out.print(angle);
		texture = new Texture(Gdx.files.internal("data/reddot.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion textureRegion = new TextureRegion(texture, 0, 0, 16, 16);
		
		sprite = new Sprite(textureRegion);
	//	sprite.setSize(1f,1f);
	//	sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
	//	sprite.setPosition(position.x,position.y);
		spritebatch = new SpriteBatch();	
	}
	
	public void update(){
		position.x +=  velocity.x;
		position.y +=  velocity.y;
		spritebatch.begin();
		spritebatch.draw(texture, position.x, position.y);
		spritebatch.end();
		distance++;
	}

	public void draw() {
					
					
		
	};
	
	

}
