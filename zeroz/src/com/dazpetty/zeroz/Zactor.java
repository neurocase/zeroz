package com.dazpetty.zeroz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/*
 * Zactor: Zactor is the Actor class, all game entities which move are actors,
 * actor is the base class for enemies, ai and the player.
 * 
 */

public class Zactor{

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
	TiledMapTileLayer collisionLayer = null;
	boolean isFlying = false;
	public boolean isGoRight = true;
	public boolean isGoLeft = false;
	int wantGoDirection = 0;
	private Zphysics physics;
	// 0 = nowhere, 1 = right, -1 = left

	
	
	
	private static final int FRAME_COLS = 5; // #1
	private static final int FRAME_ROWS = 5; // #2

	public Sprite leftarmsprite;
	public Sprite rightarmsprite;
	public Sprite armsprite;
	public Sprite aimladdersprite;
	public String type = null; 

	public float aimAngle = 0;
	public Sprite idlesprite;
	public Sprite runsprite;
	public Sprite backwalksprite;
	public Sprite crouchsprite;
	public Sprite crouchbacksprite;
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
	private TextureAtlas crouchTextureAtlas;
	private TextureAtlas crouchBackTextureAtlas;
	
	private TextureAtlas upLadderTextureAtlas;

	float stateTime;

	private AtlasRegion runTexRegion;

	public boolean hasTarget = false;
	public boolean hasEnemy = false;
	public boolean isShooting = false;
	
	public boolean aimOnLadder = false;
	public boolean amTouching = false;
	public int activeBullet = 0;

	protected String movingdirection = "right";
	public String aimingdirection = "right";
	public String state = "idle";

	public Vector3 targetWorldVec = new Vector3(0, 0, 0);
	public Vector3 targetScreenVec = new Vector3(0, 0, 0);

	public Vector3 enemyWorldVec = new Vector3(0, 0, 0);
	public Vector3 enemyScreenVec = new Vector3(0, 0, 0);

	
	
	
	
	
	public boolean run = false;
	public float height = 0;
	public float width = 0;
	
	public Zactor(){
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

		TextureRegion aimLadderTexRegion = new TextureRegion(aimLadderTexture,
				0, 0, 128, 128);

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
		
		crouchTextureAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/zman/crouch.atlas"));
		crouchBackTextureAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/zman/crouchback.atlas"));
		
		upLadderTextureAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/zman/upladder.atlas"));

		AtlasRegion runTexRegion = runTextureAtlas.findRegion("0000");
		AtlasRegion idleTexRegion = idleTextureAtlas.findRegion("0000");
		AtlasRegion crouchTexRegion = crouchTextureAtlas.findRegion("0000");
		AtlasRegion crouchBackTexRegion = crouchBackTextureAtlas.findRegion("0000");
		AtlasRegion backWalkTexRegion = backWalkTextureAtlas.findRegion("0000");
		AtlasRegion upLadderTexRegion = upLadderTextureAtlas.findRegion("0000");

		runsprite = new Sprite(runTexRegion);
		runsprite.setPosition(-10, -10);
		runsprite.scale(1f);

		idlesprite = new Sprite(idleTexRegion);
		idlesprite.setPosition(-10, -10);
		idlesprite.scale(1f);
		
		crouchbacksprite = new Sprite(crouchBackTexRegion);
		crouchbacksprite.setPosition(-10, -10);
		crouchbacksprite.scale(1f);
		
		crouchsprite = new Sprite(crouchTexRegion);
		crouchsprite.setPosition(-10, -10);
		crouchsprite.scale(1f);
		
		backwalksprite = new Sprite(backWalkTexRegion);
		backwalksprite.setPosition(-10, -10);
		backwalksprite.scale(1f);

		upladdersprite = new Sprite(upLadderTexRegion);
		upladdersprite.setPosition(-32, -10);
		upladdersprite.scale(1f);
		physics = new Zphysics();
	}

	public void initActor(TiledMapTileLayer cLayer, Vector2 actorstart) {
		worldpos = actorstart;
		collisionLayer = cLayer;
		initialized = true;
		//texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		//textureRegion = new TextureRegion(texture, 0, 0, 64, 128);
	}

	boolean isCellBlocked(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(blockedKey);
	}

	boolean isCellPlatform(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(platformKey);
	}

	 boolean isCellLadder(float x, float y) {
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
	
	public void update(float inx, float iny, boolean isWorldCoord,
			Camera camera, boolean shoot) {
		boolean facingtarget = true;
		boolean flip = false;
		boolean aimless = false;
		currentFrame++;

		if (inx == 0 && iny == 0) {
			aimless = true;
			isWorldCoord = false;
			if (movingdirection == "right")
				inx = -2;
			if (movingdirection == "left")
				inx = 2;
		}

		if (velocity.x > 0) {
			movingdirection = "right";
			if (aimless) {
				aimingdirection = "right";
			}
		} else if (velocity.x < 0) {
			movingdirection = "left";
			if (aimless) {
				aimingdirection = "left";
			}
		} else if (velocity.x == 0) {
		}

		targetScreenVec.x = inx;
		targetScreenVec.y = iny;
		targetWorldVec.x = inx;
		targetWorldVec.y = iny;

		if (isWorldCoord) {
			camera.project(targetScreenVec);
		} else {
			camera.unproject(targetWorldVec);
		}

		if (targetScreenVec.x < 0) {
			aimingdirection = "right";
		} else if (targetScreenVec.x > 0) {
			aimingdirection = "left";
		}

		float tx = targetWorldVec.x;
		float ty = targetWorldVec.y;


		if (velocity.x != 0 && velocity.y == 0 && isGrounded) {
			state = "run";
		}
		if (velocity.x == 0 && velocity.y == 0 && !isOnLadder) {
			state = "idle";
		}
		if (isOnLadder) {
			if (velocity.y == 0) {
				state = "ladderaim";
			} else if (velocity.y > 0) {
				state = "ladderclimb";
			} else if (velocity.y < 0) {
				state = "ladderslide";
			}
		}

		if (velocity.x != 0 && movingdirection == aimingdirection) {
			state = "run";
			if (isCrouching) {
				state = "crouch";
			}
		} else if (velocity.x != 0 && movingdirection != aimingdirection) {
			state = "runback";
			if (isCrouching) {
				state = "crouchback";
			}
		} else if (velocity.x == 0 && velocity.y == 0) {
			state = "idle";
			if (isCrouching){
				state = "crouchidle";
			}
		}
		if (!isGrounded && !isOnLadder && velocity.y != 0) {
			state = "jumping";
		}

		if (isOnLadder && !aimless) {
			state = "ladderaim";
			isShooting = true;
		}

		if (state == "run" || state == "ladderclimb" || state == "runback" || state == "crouch" || state == "crouchback") {
			if (currentFrame > 24) {
				currentFrame = 0;
			}

			currentAtlasKey = String.format("%04d", currentFrame);
			runsprite.setRegion(runTextureAtlas.findRegion(currentAtlasKey));
			upladdersprite.setRegion(upLadderTextureAtlas
					.findRegion(currentAtlasKey));
			backwalksprite.setRegion(backWalkTextureAtlas
					.findRegion(currentAtlasKey));
			crouchbacksprite.setRegion(crouchBackTextureAtlas
					.findRegion(currentAtlasKey));
			crouchsprite.setRegion(crouchTextureAtlas
					.findRegion(currentAtlasKey));
		}
		
		float armyadd = 0;

		if (state == "run") {
			sprite = runsprite;
			isOnLadder = false;

		} else if (state == "runback") {
			sprite = backwalksprite;
			isOnLadder = false;

		} else if (state == "idle") {
			currentFrame = 0;
			currentAtlasKey = String.format("%04d", currentFrame);
			idlesprite.setRegion(idleTextureAtlas.findRegion(currentAtlasKey));
			sprite = idlesprite;
			isOnLadder = false;

		} else if (state == "jumping") {
			currentFrame = 7;
			isOnLadder = false;
			isGrounded = false;
			runsprite.setRegion(runTextureAtlas.findRegion(currentAtlasKey));
			sprite = runsprite;
		} else if (state == "ladderclimb") {
			sprite = upladdersprite;
			isGrounded = false;
		} else if (state == "ladderslide") {
			sprite = upladdersprite;
			isGrounded = false;
		} else if (state == "ladderaim") {
			sprite = aimladdersprite;
			isGrounded = false;
		} else if (state == "crouch"){
			sprite = crouchsprite;
		} else if (state == "crouchback"){
			sprite = crouchbacksprite;
		} else if (state == "crouchidle"){
			currentFrame = 0;
			sprite = crouchsprite;
		}
		
		if (isCrouching && isGrounded){
			armyadd = -0.55f;
		}
		
		if (aimingdirection == "right" && !sprite.isFlipX()) {
			flip = true;
		}

		if (flip) {
			sprite.flip(true, false);

		}

		if (aimingdirection == "right") {
			armsprite = rightarmsprite;
			if (!aimladdersprite.isFlipX())
				aimladdersprite.flip(true, false);
		} else {
			armsprite = leftarmsprite;
			if (aimladdersprite.isFlipX())
				aimladdersprite.flip(true, false);
		}

		Vector2 tmpAimVec = new Vector2(targetScreenVec.x, targetScreenVec.y);
		aimAngle = tmpAimVec.angle();
		sprite.setSize(1f, 1f);
		sprite.setOrigin(sprite.getWidth() / 2, 0);
		sprite.setPosition(worldpos.x - 0.5f, worldpos.y);
		armsprite.setRotation(aimAngle);
		armsprite.setPosition(worldpos.x - 0.76f, worldpos.y + 1 + armyadd);
		
		if (type == "enemy"){
		sprite.setColor(Color.RED);
		}else{
			sprite.setColor(Color.WHITE);	
		}
		
		if (!aimless) {
			isShooting = true;
		} else {
			isShooting = false;
		}
		
		physics.doPhysics(this);
	}
	
	public void update(boolean isWorldCoord, Camera camera, boolean shoot) {
		boolean facingtarget = true;
		boolean flip = false;
		boolean aimless = false;
		currentFrame++;

//		if (inx == 0 && iny == 0) {
		float inx = 0;
		float iny = 0;
		
			aimless = true;
			isWorldCoord = false;
			if (movingdirection == "right")
				inx = -2;
			if (movingdirection == "left")
				inx = 2;
	//	}

		if (velocity.x > 0) {
			movingdirection = "right";
			if (aimless) {
				aimingdirection = "right";
			}
		} else if (velocity.x < 0) {
			movingdirection = "left";
			if (aimless) {
				aimingdirection = "left";
			}
		} else if (velocity.x == 0) {
		}

		targetScreenVec.x = inx;
		targetScreenVec.y = iny;
		targetWorldVec.x = inx;
		targetWorldVec.y = iny;

		if (isWorldCoord) {
			camera.project(targetScreenVec);
		} else {
			camera.unproject(targetWorldVec);
		}

		if (targetScreenVec.x < 0) {
			aimingdirection = "right";
		} else if (targetScreenVec.x > 0) {
			aimingdirection = "left";
		}

		float tx = targetWorldVec.x;
		float ty = targetWorldVec.y;


		if (velocity.x != 0 && velocity.y == 0 && isGrounded) {
			state = "run";
		}
		if (velocity.x == 0 && velocity.y == 0 && !isOnLadder) {
			state = "idle";
		}
		if (isOnLadder) {
			if (velocity.y == 0) {
				state = "ladderaim";
			} else if (velocity.y > 0) {
				state = "ladderclimb";
			} else if (velocity.y < 0) {
				state = "ladderslide";
			}
		}

		if (velocity.x != 0 && movingdirection == aimingdirection) {
			state = "run";
			if (isCrouching) {
				state = "crouch";
			}
		} else if (velocity.x != 0 && movingdirection != aimingdirection) {
			state = "runback";
			if (isCrouching) {
				state = "crouchback";
			}
		} else if (velocity.x == 0 && velocity.y == 0) {
			state = "idle";
			if (isCrouching){
				state = "crouchidle";
			}
		}
		if (!isGrounded && !isOnLadder && velocity.y != 0) {
			state = "jumping";
		}

		if (isOnLadder && !aimless) {
			state = "ladderaim";
			isShooting = true;
		}

		if (state == "run" || state == "ladderclimb" || state == "runback" || state == "crouch" || state == "crouchback") {
			if (currentFrame > 24) {
				currentFrame = 0;
			}

			currentAtlasKey = String.format("%04d", currentFrame);
			runsprite.setRegion(runTextureAtlas.findRegion(currentAtlasKey));
			upladdersprite.setRegion(upLadderTextureAtlas
					.findRegion(currentAtlasKey));
			backwalksprite.setRegion(backWalkTextureAtlas
					.findRegion(currentAtlasKey));
			crouchbacksprite.setRegion(crouchBackTextureAtlas
					.findRegion(currentAtlasKey));
			crouchsprite.setRegion(crouchTextureAtlas
					.findRegion(currentAtlasKey));
		}
		
		float armyadd = 0;

		if (state == "run") {
			sprite = runsprite;
			isOnLadder = false;

		} else if (state == "runback") {
			sprite = backwalksprite;
			isOnLadder = false;

		} else if (state == "idle") {
			currentFrame = 0;
			currentAtlasKey = String.format("%04d", currentFrame);
			idlesprite.setRegion(idleTextureAtlas.findRegion(currentAtlasKey));
			sprite = idlesprite;
			isOnLadder = false;

		} else if (state == "jumping") {
			currentFrame = 7;
			isOnLadder = false;
			isGrounded = false;
			runsprite.setRegion(runTextureAtlas.findRegion(currentAtlasKey));
			sprite = runsprite;
		} else if (state == "ladderclimb") {
			sprite = upladdersprite;
			isGrounded = false;
		} else if (state == "ladderslide") {
			sprite = upladdersprite;
			isGrounded = false;
		} else if (state == "ladderaim") {
			sprite = aimladdersprite;
			isGrounded = false;
		} else if (state == "crouch"){
			sprite = crouchsprite;
		} else if (state == "crouchback"){
			sprite = crouchbacksprite;
		} else if (state == "crouchidle"){
			currentFrame = 0;
			sprite = crouchsprite;
		}
		
		if (isCrouching && isGrounded){
			armyadd = -0.55f;
		}
		
		if (aimingdirection == "right" && !sprite.isFlipX()) {
			flip = true;
		}

		if (flip) {
			sprite.flip(true, false);

		}

		if (aimingdirection == "right") {
			armsprite = rightarmsprite;
			if (!aimladdersprite.isFlipX())
				aimladdersprite.flip(true, false);
		} else {
			armsprite = leftarmsprite;
			if (aimladdersprite.isFlipX())
				aimladdersprite.flip(true, false);
		}

		Vector2 tmpAimVec = new Vector2(targetScreenVec.x, targetScreenVec.y);
		aimAngle = tmpAimVec.angle();
		sprite.setSize(1f, 1f);
		sprite.setOrigin(sprite.getWidth() / 2, 0);
		sprite.setPosition(worldpos.x - 0.5f, worldpos.y);
		armsprite.setRotation(aimAngle);
		armsprite.setPosition(worldpos.x - 0.76f, worldpos.y + 1 + armyadd);
		
		if (type == "enemy"){
		sprite.setColor(Color.RED);
		}else{
			sprite.setColor(Color.WHITE);	
		}
		
		if (!aimless) {
			isShooting = true;
		} else {
			isShooting = false;
		}
		
		physics.doPhysics(this);
	}
	
	
	public void dispose() {
		runTextureAtlas.dispose();
		idleTextureAtlas.dispose();
		backWalkTextureAtlas.dispose();
		rightArmTexture.dispose();
		leftArmTexture.dispose();
	}

}
