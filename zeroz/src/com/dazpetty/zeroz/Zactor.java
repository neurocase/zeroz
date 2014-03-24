package com.dazpetty.zeroz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

/*
 * Zactor: Zactor is the Actor class, all game entities which move are actors,
 * actor is the base class for enemies, ai and the player.
 * 
 */

public class Zactor extends Zobject {

	public boolean isJump = true;
	public boolean isAlive = true;
	public boolean isGrounded = true;
	public boolean isOnLadder = false;
	public boolean canDoubleJump = false;
	public boolean goThruPlatform = false;
	public boolean isCrouching = false;

	public float jumpSpeed = 16;
	public float moveSpeed = 5;
	public int health = 100;
	public float rotdir = 0;
	public float gravMass = 0.6f;
	public float deceleration = 0.6f;
	public String blockedKey = "solid";
	public String platformKey = "platform";
	public String ladderKey = "ladder";
	public boolean initialized = false;
	public Vector2 screenpos = new Vector2(0, 0);
	public Vector2 worldpos = new Vector2(0, 0);
	public Vector2 velocity = new Vector2(0, 0);
	protected TiledMapTileLayer collisionLayer;
	protected boolean isFlying = false;
	public boolean isGoRight = true;
	public boolean isGoLeft = false;
	int wantGoDirection = 0;
	// 0 = nowhere, 1 = right, -1 = left

	public boolean run = false;

	public void initActor(TiledMapTileLayer cLayer, Vector2 actorstart) {
		worldpos = actorstart;
		collisionLayer = cLayer;
		initialized = true;
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textureRegion = new TextureRegion(texture, 0, 0, 64, 128);
	}

	protected boolean isCellBlocked(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(blockedKey);
	}

	protected boolean isCellPlatform(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(platformKey);
	}

	protected boolean isCellLadder(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(ladderKey);
	}

	public void goLeft() {
		if (velocity.y == 0 || isOnLadder) {
			velocity.x -= moveSpeed;
			if (velocity.x < -10)
				velocity.x = -10;
			isGoRight = false;
			run = true;
		}
		if (velocity.y != 0 && velocity.x > -5 && canDoubleJump) {
			velocity.x -= moveSpeed;
		}

	}

	public void goRight() {
		if (velocity.y == 0 || isOnLadder) {
			velocity.x += moveSpeed;
			if (velocity.x > 10)
				velocity.x = 10;
			isGoRight = true;
			run = true;
		}
		if (velocity.y != 0 && velocity.x < 5 && canDoubleJump) {
			velocity.x += moveSpeed;
		}
	}

	public void goJumpDown(){
		isGrounded = false;
		isOnLadder = false;
		canDoubleJump = true;
	}
	
	public void goJump() {

		for (float i = -0.55f; i < 0.55f; i += 0.55f) {
			if (isCellLadder(worldpos.x + i, worldpos.y)) {
				worldpos.x = (int) worldpos.x + i + 0.4f;
			}
		}

		if (isCellLadder(worldpos.x, worldpos.y)) {
			isGrounded = false;
			velocity.y = 4f;
			isOnLadder = true;
			velocity.x = 0;
			run = true;
		} else {
			isOnLadder = false;
		}

		if (isGrounded) {
			canDoubleJump = true;
			if (!isCrouching){
			velocity.y += jumpSpeed;
			}else{
				
			}
			isGrounded = false;
			isOnLadder = false;
		} else if ((velocity.x >= 0 && isCellBlocked(worldpos.x + 0.5f,
				worldpos.y))
				|| (velocity.x <= 0 && isCellBlocked(worldpos.x - 0.5f,
						worldpos.y))) {
			if (canDoubleJump && velocity.y < (jumpSpeed / 2)) {
				if (velocity.x > 0) {
					velocity.x = (-jumpSpeed / 2);
					isGoRight = false;
				} else {
					velocity.x = (jumpSpeed / 2);
					isGoRight = true;
				}
				velocity.y = jumpSpeed - (jumpSpeed / 3.5f);
				canDoubleJump = false;
			}

		}

	}

	public void dispose() {
		// texture.dispose();
	}

}
