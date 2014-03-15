package com.dazpetty.zeroz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Zbullet extends Zobject {

	public boolean initialized = false;
	public boolean isAlive = true;
	protected TiledMapTileLayer collisionLayer;
	public Texture texture;
	public Sprite sprite;
	public int bulletDist = 600;
	public Vector2 position = new Vector2(0, 0);
	public Vector2 velocity = new Vector2(0, 0);
	public Vector2 aimVec = new Vector2(0, 0);
	public Vector2 startpos = new Vector2(0, 0);
	public Vector2 relativepos = new Vector2(0,0);
	public boolean isTargeted = false;
	// SpriteBatch spritebatch;
	// int distance = 0;
	// public float rotDir = 0;
	float speed = 32;

	public void create(TiledMapTileLayer cLayer, Vector2 posVec, double angle, boolean hasTarget) {
		
		isTargeted = hasTarget;
		//isAlive = true;
		collisionLayer = cLayer;

		startpos.x = posVec.x;
		startpos.y = posVec.y;

		position.x = posVec.x;
		position.y = posVec.y;

		relativepos.x = posVec.x;
		relativepos.y = posVec.y;
		// position = posVec;
		// Gdx.graphics.getWidth()/2-width*32,Gdx.graphics.getHeight()/2

		double rad = Math.toRadians(angle);

		velocity.x = (float) (speed * Math.cos(rad));
		velocity.y = (float) (speed * Math.sin(rad));

		System.out.print(angle);

		if (!initialized) {
			initialized = true;
			texture = new Texture(Gdx.files.internal("data/reddot.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

			TextureRegion textureRegion = new TextureRegion(texture, 0, 0, 16,
					16);

			sprite = new Sprite(textureRegion);
			sprite.setSize(0.25f, 0.25f);
			sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
			sprite.setPosition(position.x, position.y+1.5f);
			// spritebatch = new SpriteBatch();
		}
	}

	public void update() {
		if (isAlive){
		position.x += velocity.x;
		position.y += velocity.y;
		
		relativepos.x = position.x - startpos.x;
		relativepos.y = position.y - startpos.y;
		}
		// sprite.setPosition(position.x,position.y);

		if (Math.abs(startpos.x - position.x) > bulletDist
				|| Math.abs(startpos.y - position.y) > bulletDist) {
			isAlive = false;
			//velocity.x = 0;
			//velocity.y = 0;
		}
		/*
		 * spritebatch.begin(); spritebatch.draw(texture, position.x,
		 * position.y); spritebatch.end();
		 */
		// distance++;

	}

	public void draw() {

	};

}
