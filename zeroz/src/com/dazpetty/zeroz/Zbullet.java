package com.dazpetty.zeroz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Zbullet implements Zobject {

	public boolean initialized = false;
	public boolean isAlive = false;
	protected TiledMapTileLayer collisionLayer;
	public Texture texture;
	public Sprite sprite;
	public int bulletDist = 600;
	public Vector2 worldpos = new Vector2(0, 0);
	public Vector2 screenpos = new Vector2(0, 0);
	public Vector2 velocity = new Vector2(0, 0);
	public Vector2 aimVec = new Vector2(0, 0);
	public Vector2 startpos = new Vector2(0, 0);
	public Vector2 relativepos = new Vector2(0,0);
	
	public Vector3 tmpVec3 = new Vector3(0,0,0);
	public boolean isTargeted = false;
	// SpriteBatch spritebatch;
	// int distance = 0;
	// public float rotDir = 0;
	float speed = 32;
	public boolean isPlayers = true;

	public void clean(){
		worldpos = new Vector2(0, 0);
		screenpos = new Vector2(0, 0);
		velocity = new Vector2(0, 0);
		aimVec = new Vector2(0, 0);
		startpos = new Vector2(0, 0);
		relativepos = new Vector2(0,0);
		
		
	}
	
	public Zbullet(){
		initialized = true;
		texture = new Texture(Gdx.files.internal("data/reddot.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		TextureRegion textureRegion = new TextureRegion(texture, 0, 0, 16,
				16);
	
		sprite = new Sprite(textureRegion);
		sprite.setSize(0.25f, 0.25f);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(screenpos.x, screenpos.y);
	}
	
	
	public void fire(float x, float y, float angle) {
		angle = angle -180;
		clean();
		Vector2 posVec = new Vector2 (x, y+1.5f);
		startpos.x = posVec.x;
		startpos.y = posVec.y;

		screenpos.x = posVec.x;
		screenpos.y = posVec.y;

		relativepos.x = posVec.x;
		relativepos.y = posVec.y;

		double rad = Math.toRadians(angle);

		velocity.x = (float) (speed * Math.cos(rad));
		velocity.y = (float) (speed * Math.sin(rad));
		sprite.setPosition(screenpos.x, screenpos.y);	
	}

	public void update(Camera camera) {
		if (isAlive){
		screenpos.x += (velocity.x /100);
		screenpos.y += (velocity.y / 100);
		
		tmpVec3 = new Vector3(screenpos.x,screenpos.y,0);
		camera.unproject(tmpVec3);
		worldpos.x = tmpVec3.x;
		worldpos.y = tmpVec3.y;
		 
		relativepos.x = screenpos.x - startpos.x;
		relativepos.y = screenpos.y - startpos.y;
		}
		if (Math.abs(startpos.x - screenpos.x) > bulletDist
				|| Math.abs(startpos.y - screenpos.y) > bulletDist) {
			isAlive = false;
		}
	}

	public void draw() {

	}
}
