package com.dazpetty.zeroz;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Zplayer extends Zactor {

	private static final int FRAME_COLS = 5; // #1
	private static final int FRAME_ROWS = 5; // #2

	public Sprite leftarmsprite;
	public Sprite rightarmsprite;
	public Sprite armsprite;
	public Sprite aimladdersprite;
	

	public Sprite idlesprite;
	public Sprite runsprite;
	public Sprite backwalksprite;
	public Sprite upladdersprite;
	

	public Sprite sprite;
	public TextureRegion armTexRegion;

	public Texture rightArmTexture;
	public Texture leftArmTexture;
	public Texture aimLadderTexture;

	private String currentAtlasKey = new String("0000");

	private int currentFrame = 1;
	private TextureAtlas runTextureAtlas;
	private TextureAtlas idleTextureAtlas;
	private TextureAtlas backWalkTextureAtlas;
	
	private TextureAtlas upLadderTextureAtlas;

	float stateTime;

	private AtlasRegion runTexRegion;

	public boolean hasTarget = false;
	public boolean hasEnemy = false;
	public boolean isShooting = false;
	public boolean aimOnLadder = false;
	public boolean amTouching = false;
	
	public Vector2 aimVec = new Vector2(0, 0);
	public Vector2 targetVec = new Vector2(0, 0);
	public Vector3 aimTargVec = new Vector3(0, 0, 0);
	public Zenemy myZenemy = new Zenemy();

	/*
	 * public enum moveState{ runforwards, runbackwards, idle, jump, die; }
	 */

	public String moveState = "idle";

	public void create() {
		height = 2;
		width = 1.25f;
		
		aimLadderTexture = new Texture(
				Gdx.files.internal("data/gfx/zman/ladderlean.png"));
		aimLadderTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		rightArmTexture = new Texture(
				Gdx.files.internal("data/gfx/zman/armemptyright.png"));
		rightArmTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		leftArmTexture = new Texture(
				Gdx.files.internal("data/gfx/zman/armemptyleft.png"));
		leftArmTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		TextureRegion aimLadderTexRegion = new TextureRegion(aimLadderTexture, 0,
				0, 128, 128);
		
		TextureRegion rightArmTexRegion = new TextureRegion(rightArmTexture, 0,
				0, 64, 64);
		TextureRegion leftArmTexRegion = new TextureRegion(leftArmTexture, 0,
				0, 64, 64);

		aimladdersprite = new Sprite(aimLadderTexRegion);
		aimladdersprite.setPosition(-10, -10);// Gdx.graphics.getWidth()/2-width*32,Gdx.graphics.getHeight()/2);
		aimladdersprite.scale(1f);
		
		leftarmsprite = new Sprite(leftArmTexRegion);
		leftarmsprite.setSize(1, 1);
		leftarmsprite.setOrigin(((leftarmsprite.getWidth() * 0.77f)),
				leftarmsprite.getHeight() / 2);
		leftarmsprite.setPosition(-10, -10);

		rightarmsprite = new Sprite(rightArmTexRegion);
		rightarmsprite.setSize(1, 1);
		rightarmsprite.setOrigin(((rightarmsprite.getWidth() * 0.77f)),
				rightarmsprite.getHeight() / 2);
		rightarmsprite.setPosition(-10, -10);

		armsprite = new Sprite();

		runTextureAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/zman/run.atlas"));
		idleTextureAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/zman/idle.atlas"));

		backWalkTextureAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/zman/backwalk.atlas"));
		
		upLadderTextureAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/zman/upladder.atlas"));

		AtlasRegion runTexRegion = runTextureAtlas.findRegion("0000");
		AtlasRegion idleTexRegion = idleTextureAtlas.findRegion("0000");
		AtlasRegion backWalkTexRegion = backWalkTextureAtlas.findRegion("0000");
		AtlasRegion upLadderTexRegion = upLadderTextureAtlas.findRegion("0000");

		
		
		
		runsprite = new Sprite(runTexRegion);
		runsprite.setPosition(-10, -10);// Gdx.graphics.getWidth()/2-width*32,Gdx.graphics.getHeight()/2);
		runsprite.scale(1f);

		idlesprite = new Sprite(idleTexRegion);
		idlesprite.setPosition(-10, -10);
		idlesprite.scale(1f);

		backwalksprite = new Sprite(backWalkTexRegion);
		backwalksprite.setPosition(-10, -10);
		backwalksprite.scale(1f);
		
		upladdersprite = new Sprite(upLadderTexRegion);
		upladdersprite.setPosition(-32, -10);
		upladdersprite.scale(1f);
	}

	@Override
	public void initActor(TiledMapTileLayer cLayer, Vector2 actorstart) {
		position = actorstart;
		collisionLayer = cLayer;
		initialized = true;
	}

	public void shoot(float angle) {

	}

	public void draw() {
	//	isShooting = false;
		boolean flipSprite = false;

		
		
		
		//isOnLadder
		//isFacingEnemy
		//isGoLeft/isGoRight
		//armRotation
		//targetLocation
		
		
		
		if (run || (isOnLadder && velocity.y > 0)) {
			currentFrame++;
			if (currentFrame > 24) {
				currentFrame = 0;
			}
			if (!isGrounded && !isOnLadder) currentFrame = 7;
			currentAtlasKey = String.format("%04d", currentFrame);
			runsprite.setRegion(runTextureAtlas.findRegion(currentAtlasKey));
			upladdersprite.setRegion(upLadderTextureAtlas.findRegion(currentAtlasKey));
			backwalksprite.setRegion(backWalkTextureAtlas
					.findRegion(currentAtlasKey));
			if ((targetVec.x > position.x && isGoRight)
					|| (targetVec.x < position.x && !isGoRight) || !hasTarget) {
				sprite = runsprite;
			} else {
				sprite = backwalksprite;
				flipSprite = true;
			}
		} else {
			currentFrame++;
			if (currentFrame > 3) {
				currentFrame = 0;
			}
			currentAtlasKey = String.format("%04d", currentFrame);
			idlesprite.setRegion(idleTextureAtlas.findRegion(currentAtlasKey));
			sprite = idlesprite;

		}

		
		/*		[FOLLOWING CODE SETS CORRECT ANIMATION]
		 * 
		 * 		Do you have a target?
		 * 		If so, I need to make sure you are facing your target and make you walk backwards
		 * 		If moving away from it.
		 * 
		 * 		If you don't have a target, then you don't need to walk backwards, because you only target whats directly 
		 * 		in front of you.
		 * 
		 * 		armmov variable gently offsets the position of the arm to make it fit on the shoulder
		 * 
		 * 		aimVec is the vector between the player and the target
		 */
		
		float armmov = 0;
		armsprite.setRotation((aimVec.angle() - 180));
		float armrot = armsprite.getRotation();
		
		
		if (hasTarget) {
			armmov = -0.77f;
			if (hasTarget && targetVec.x < position.x) {
				if  (isGoRight && sprite.isFlipX()) {
					sprite.flip(true, false);
				}
				
				
				if (armrot > 180) {
					armsprite = rightarmsprite;
					armsprite.setRotation(armrot);
				} else {
					armsprite = leftarmsprite;
					armsprite.setRotation(armrot);
				}
			}
			
			if (hasTarget && targetVec.x > position.x) {
				if (isGoRight && !sprite.isFlipX()) {
					sprite.flip(true, false);
				}
				if (!isGoRight && !sprite.isFlipX()) {
					sprite.flip(true, false);
				}
				if (armrot > 180) {
					armsprite = leftarmsprite;
					armsprite.setRotation(armrot);
				} else {
					armsprite = rightarmsprite;
					armsprite.setRotation(armrot);
				}
			}
		} else {
			if (isGoRight) {				
				if (!sprite.isFlipX()) {
					sprite.flip(true, false);
				}
				armsprite = rightarmsprite;
				armsprite.setRotation(180);
				
				armmov = -0.8f;
				if (isGoLeft) {
					isGoLeft = true;
				}
			} else {
				if (sprite.isFlipX()) {
					sprite.flip(true, false);
				}
				armsprite = leftarmsprite;
				
				armsprite.setRotation(0);
				if (!isGoLeft) {
					armmov = -0.65f;
					isGoLeft = false;
				}
			}
		}
		if (amTouching){
		armsprite.setRotation(armrot-180);
		}
		amTouching = false;
		
		if (isOnLadder && velocity.x == 0){
			sprite = upladdersprite; 
		}
		if (velocity.x != 0){
			isOnLadder = false;
		}
		
		
		if (isOnLadder && isShooting){
			
			sprite = aimladdersprite;
		}
		
		sprite.setSize(1f, 1f);
		sprite.setOrigin(sprite.getWidth() / 2, 0);
		sprite.setPosition(position.x-0.5f, position.y);
		armsprite.setPosition(position.x + armmov, position.y + 1);
		
		
		run = false;
		//if(isOnLadder) run = true;

	}

	public void giveTarget(Vector3 newTargetVec, int xt, int yt) {
		hasTarget = true;

		targetVec.x = xt;
		targetVec.y = yt;

		aimVec.x = newTargetVec.x;
		aimVec.y = newTargetVec.y;
	}

	public void giveTarget(Zenemy zenemy) {
		myZenemy = zenemy;
		hasEnemy = true;
		hasTarget = true;
		System.out.println("have enemy at X:" + myZenemy.position.x + " Y:"
				+ myZenemy.position.y);

	}

	public void updateTarget(Camera camera) {
		
		if (hasEnemy) {
			targetVec.x = myZenemy.position.x + 0.5f;
			targetVec.y = myZenemy.position.y + 1.25f;
		}else {
			Vector3 tmpTargVec = new Vector3(aimVec.x, aimVec.y, 0);
			camera.project(tmpTargVec);
			targetVec.x = tmpTargVec.x;
			targetVec.y = tmpTargVec.y;
			
		}

		aimTargVec.x = targetVec.x;
		aimTargVec.y = targetVec.y;
		camera.project(aimTargVec);
		aimVec.x = aimTargVec.x - Gdx.graphics.getWidth() / 2 - 16;
		aimVec.y = aimTargVec.y - Gdx.graphics.getHeight() / 2 - height * 32;

		System.out.println();
		System.out.println(Math.abs(position.x - targetVec.x));

		if (Math.abs(position.x - targetVec.x) > 18) {
			hasTarget = false;
			hasEnemy = false;
		}
		if (Math.abs(position.y - targetVec.y) > 18) {
			hasTarget = false;
			hasEnemy = false;
		}

	}

	@Override
	public void dispose() {
		runTextureAtlas.dispose();
		idleTextureAtlas.dispose();
		backWalkTextureAtlas.dispose();
		rightArmTexture.dispose();
		leftArmTexture.dispose();
	}
}

/*
 * is going right if facing enemy hasTarget
 */

