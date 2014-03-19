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
	
	public float aimAngle = 0;
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
	public int activeBullet = 0;
	
	//private enum direction{left, right};
	//private enum state{run,idle,ladderclimb,ladderaim,jump};
	
	//private String facingdirection = "right";
	private String movingdirection = "right";
	private String aimingdirection = "right";
	public String state = "idle";
	
	
	
	public Vector3 targetWorldVec = new Vector3(0, 0, 0);
	public Vector3 targetScreenVec = new Vector3(0, 0, 0);
	
	public Vector3 enemyWorldVec = new Vector3(0, 0, 0);
	public Vector3 enemyScreenVec = new Vector3(0, 0, 0);
	
	public Zenemy myZenemy = new Zenemy();
	
	

	/*
	 * public enum moveState{ runforwards, runbackwards, idle, jump, die; }
	 */

	//public String moveState = "idle";

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
		worldpos = actorstart;
		collisionLayer = cLayer;
		initialized = true;
	}


	//public void update(Vector3 targinputvec, boolean isWorldCoord, Camera camera, boolean shoot) {
	public void update(float inx, float iny, boolean isWorldCoord, Camera camera, boolean shoot) {
		boolean facingtarget = true;
		boolean flip = false;
		boolean aimless = false;
		currentFrame++;
		
		
		
		if (inx == 0 && iny == 0){
			aimless = true;
			isWorldCoord = false;
			if (movingdirection == "right") inx =  -2; 
			if (movingdirection == "left") inx = 2;
		}
			
		if (velocity.x > 0){
			movingdirection = "right";
			if (aimless){
				aimingdirection = "right";
			}
		}else if (velocity.x < 0){
			movingdirection = "left";
			if (aimless){
				aimingdirection = "left";
			}
		}else if (velocity.x == 0){
		}
		
		
		
		targetScreenVec.x = inx;
		targetScreenVec.y = iny;
		targetWorldVec.x = inx;
		targetWorldVec.y = iny;
		
		if (isWorldCoord){
			camera.project(targetScreenVec);
		}else{
			camera.unproject(targetWorldVec);	
		}
		
		if (targetScreenVec.x < 0){
			aimingdirection = "right";
		}else if (targetScreenVec.x > 0){
			aimingdirection = "left";			
		}
		
		float tx = targetWorldVec.x;
		float ty = targetWorldVec.y;
		
		//System.out.println("TargetScreenX:"+ targetScreenVec.x +" TargetScreenY:"+ targetScreenVec.y);	
		
		if (velocity.x != 0 && velocity.y ==0 && isGrounded){
			state = "run";
		}
		if (velocity.x ==0 && velocity.y == 0 && !isOnLadder){
			state = "idle";
		}
		if (isOnLadder){
			if (velocity.y == 0){
				state = "ladderaim";
			}else if (velocity.y > 0){
				state = "ladderclimb";
			}else if (velocity.y < 0){
				state = "ladderslide";
			}
		}
		
		
		
		if (velocity.x != 0 && movingdirection == aimingdirection){
				state = "run";
		}else if (velocity.x != 0 && movingdirection != aimingdirection){
			state = "runback";
		}else if(velocity.x == 0 && velocity.y == 0){
			state = "idle";
		}
		if (!isGrounded && !isOnLadder && velocity.y != 0){
			state = "jumping";
		}
		
		if (isOnLadder && !aimless){
			state = "ladderaim";
			isShooting = true;
		}
		
		if (state == "run" || state == "ladderclimb" || state == "runback"){
			if (currentFrame > 24) {
				currentFrame = 0;
			}
			
			currentAtlasKey = String.format("%04d", currentFrame);
			runsprite.setRegion(runTextureAtlas.findRegion(currentAtlasKey));
			upladdersprite.setRegion(upLadderTextureAtlas.findRegion(currentAtlasKey));
			backwalksprite.setRegion(backWalkTextureAtlas
					.findRegion(currentAtlasKey));
		}
		
		if (state == "run"){
			sprite = runsprite;
			isOnLadder = false;
		
		} else if (state == "runback"){
			sprite = backwalksprite;
			isOnLadder = false;
		
		} else if (state == "idle"){
			currentFrame = 0;
			currentAtlasKey = String.format("%04d", currentFrame);
			idlesprite.setRegion(idleTextureAtlas.findRegion(currentAtlasKey));
			sprite = idlesprite;	
			isOnLadder = false;
		
		} else if (state == "jumping"){
			currentFrame = 7;
			isOnLadder = false;
			isGrounded = false;
			runsprite.setRegion(runTextureAtlas.findRegion(currentAtlasKey));
			sprite = runsprite;	
		} else if (state == "ladderclimb"){
			sprite = upladdersprite;
			isGrounded = false;
		} else if(state == "ladderslide"){
			sprite = upladdersprite;
			isGrounded = false;
		} else if (state == "ladderaim"){
			sprite = aimladdersprite;
			isGrounded = false;
		}
		
		if (aimingdirection =="right" && !sprite.isFlipX()){
			flip = true;
		}
		
		if (flip){
			//aimladdersprite.flip(true, false);
			sprite.flip(true, false);
			
		}
		
		if (aimingdirection == "right"){
			armsprite = rightarmsprite;
			if (!aimladdersprite.isFlipX()) aimladdersprite.flip(true, false);
		}else{
			armsprite = leftarmsprite;
			if (aimladdersprite.isFlipX()) aimladdersprite.flip(true, false);
		}
		
		Vector2 tmpAimVec = new Vector2(targetScreenVec.x, targetScreenVec.y);
		aimAngle = tmpAimVec.angle(); 
		sprite.setSize(1f, 1f);
		sprite.setOrigin(sprite.getWidth() / 2, 0);
		sprite.setPosition(worldpos.x-0.5f, worldpos.y);
		armsprite.setRotation(aimAngle);
		armsprite.setPosition(worldpos.x-0.76f, worldpos.y + 1);
		
		
		if (!aimless){
			isShooting = true;
		}else{
			isShooting = false;
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

