package com.dazpetty.zeroz.entities;

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
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Array;
import com.dazpetty.zeroz.core.DazDebug;
import com.dazpetty.zeroz.core.GamePhysics;
import com.dazpetty.zeroz.managers.EntityManager;
import com.dazpetty.zeroz.managers.ProjectileManager;
import com.dazpetty.zeroz.managers.LevelManager;

/*
 * Pawn Entity is the base class for entities controlled by the player or ai,
 *
 */

public class PawnEntity {
	/*
	 * BOOLEANS
	 */
	boolean isMelee = false;
	public boolean isJump = true;
	public boolean isAlive = true;
	public boolean isGrounded = true;
	public boolean isDisposed = false;
	public boolean isOnLadder = false;
	public boolean canDoubleJump = false;
	public boolean goThruPlatform = false;
	public boolean isCrouching = false;
	public boolean isAI = false;
	// public boolean isDead = false;
	public boolean initialized = false;
	public boolean isFlying = false;
	public boolean isGoRight = true;
	public boolean isGoLeft = false;
	public boolean hasTarget = false;
	public boolean hasEnemy = false;
	public boolean isShooting = false;
	public boolean aimOnLadder = false;
	public boolean amTouching = false;
	public boolean run = false;
	public boolean blinkOn = false;
	public boolean isLevelScrolling = false;
	public boolean pressJump = false;
	/*
	 *  INPUT HELPERS
	 */
	
	public boolean pressUp = false;
	public boolean pressDown = false;
	public boolean pressRight = false;
	public boolean pressLeft = false;
	public boolean pressShoot = false;
	
	
	
	/*
	 * ORDINARY VARIABLES
	 */

	public int goDirection = 0;
	public int deathanim = 0;
	public float jumpSpeed = 10;
	public float aimAngle = 0;
	public float moveSpeed = 0.5f;
	public int startinghealth = 100;
	public int health = 100;
	public float rotdir = 0;
	public float gravMass = 0.6f;
	public float deceleration = 0.6f;
	public int blinking = 60;
	public int BLINK_DURATION = 60;

	/*
	 * Strings
	 */
	public String blockedKey = "solid";
	public String platformKey = "platform";
	public String ladderKey = "ladder";
	protected String movingdirection = "right";
	public String aimingdirection = "right";
	public String state = "idle";
	/*
	 * Vectors
	 */

	// public Vector2 screenpos = new Vector2(0, 0);
	public Vector2 worldpos = new Vector2(0, 0);
	public Vector2 velocity = new Vector2(0, 0);
	public Vector3 targetWorldVec = new Vector3(0, 0, 0);
	public Vector3 targetScreenVec = new Vector3(0, 0, 0);
	// public Vector3 enemyWorldVec = new Vector3(0, 0, 0);
	// public Vector3 enemyScreenVec = new Vector3(0, 0, 0);
	public Vector3 aimingAt = new Vector3(0, 0, 0);
	public Vector2 actorTarget = new Vector2(0, 0);
	// public Vector2 p1 = new Vector2(), p2 = new Vector2(),
	// collision = new Vector2(), normal = new Vector2();
	int wantGoDirection = 0;
	public float aimAtPlayer = 0;
	public int activeBullet = 0;
	public float height = 0;
	public float width = 0;
	public long lasttimeshoot = System.currentTimeMillis();

	/*
	 * ANIMATION TYPES
	 */
	public int MAX_MOVE_SPEED = 5;
	private static final int FRAME_COLS = 5; // #1
	private static final int FRAME_ROWS = 5; // #2
	private String currentAtlasKey = new String("0000");
	// float stateTime;
	private int currentFrame = 1;

	public Sprite rightarmsprite;
	/*
	 * Armsprites
	 */

	public Sprite armsprite;
	public Sprite armswordsprite;
	public Sprite armuzisprite;
	public Sprite armshotgunsprite;

	public Sprite aimladdersprite;
	public String type = null;
	public Sprite idlesprite;
	public Sprite runsprite;
	public Sprite backwalksprite;
	public Sprite crouchsprite;
	public Sprite crouchbacksprite;
	public Sprite upladdersprite;
	public Sprite deathsprite;
	public Sprite sprite;
	public TextureRegion armTexRegion;
	public Texture armTexture;
	public Texture aimLadderTexture;
	private TextureAtlas runTextureAtlas;
	private TextureAtlas idleTextureAtlas;
	private TextureAtlas backWalkTextureAtlas;
	private TextureAtlas crouchTextureAtlas;
	private TextureAtlas crouchBackTextureAtlas;
	private TextureAtlas upLadderTextureAtlas;
	private TextureAtlas deathTextureAtlas;
	private AtlasRegion runTexRegion;
	/*
	 * MY OBJECTS
	 */
	private GamePhysics physics;
	public LevelManager levelMan;
	public ProjectileManager projMan;
	public Weapon weapon;
	TiledMapTileLayer collisionLayer = null;
	public PawnFoot pawnFoot;
	public Fixture mainbodyfixture;
	/*
	 * LIBGDX/BOX2D OBJECTS
	 */
	protected Camera scenecamera;
	public World world;
	public BodyDef bodyDef = new BodyDef();
	public Body mainbody;
	public Sprite grensprite;
	public FixtureDef fixtureDef;
	/*
	 * AI's VARIABLES
	 */
	public int id = 0;
	public boolean isJumpy = false;
	public float shootDist = 4;
	float relativetoplayerx = 0;
	float relativetoplayery = 0;
	public Vector2 relativepos = new Vector2(relativetoplayerx,
			relativetoplayery);

	/*
	 * FUNCTIONS
	 */
	public void reUseEntity(Vector2 actorstart, Weapon weapon) {
		this.weapon = weapon;
		mainbody.setActive(true);
		mainbody.setAwake(true);
		isDisposed = false;
		isOnLadder = false;
		// isDead = false;
		isAlive = true;
		worldpos = actorstart;
		health = startinghealth;
	}

	public void attemptShoot(float ang) {
		projMan.setWorld(world);
		if (isAlive) {
			projMan.shootProjectile(ang, this);
		}
	}

	public PawnEntity(Camera scam, World world, boolean amAI,
			LevelManager levelMan, Vector2 actorstart, int id,
			EntityManager actorMan, String actorType) {

		weapon = new Weapon(1);

		this.world = world;
		this.id = id;
		this.levelMan = levelMan;

		isLevelScrolling = levelMan.isLevelScrolling;
		projMan = actorMan.projMan;

		if (levelMan == null) {
			levelMan = (LevelManager) levelMan;
			if (levelMan == null) {
				System.out.println("levelMan is null");
			}
		}

		if (actorstart.x == 0) {
			actorstart = (new Vector2(7, 14));
		}
		worldpos = actorstart;

	
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(worldpos);
		bodyDef.type = BodyType.DynamicBody;

		mainbody = world.createBody(bodyDef);
		mainbody.setActive(true);
		mainbody.setAwake(true);
		mainbody.setBullet(true);

		initialized = true;
		isDisposed = false;
		isAI = amAI;

		fixtureDef = new FixtureDef();
		fixtureDef.friction = 0;
		fixtureDef.density = 0;
		//fixtureDef.
		// fixtureDef.
		// fixtureDef.isSensor = true;
		/*
		 * PolygonShape pBox = new PolygonShape(); pBox.setAsBox(0.3f, 1f);
		 * 
		 * Vector2[] triangle = new Vector2[3]; triangle[0]= new Vector2(-0.3f,
		 * 0.2f); triangle[1]= new Vector2(0.3f, 0.2f); triangle[2]= new
		 * Vector2(0,0);
		 * 
		 * pBox.set(triangle);
		 */
		 PolygonShape pBox = new PolygonShape(); 
		 pBox.setAsBox(0.3f, 0.8f);
		
	//	CircleShape dCirc = new CircleShape();
	//	dCirc.setRadius(0.35f);

		fixtureDef.shape = pBox;
		// fixtureDef.isSensor = true;
		Filter pawnfilter = fixtureDef.filter;
		pawnfilter.maskBits = 3;
		mainbodyfixture = mainbody.createFixture(fixtureDef);
		mainbody.setBullet(true);
		mainbody.setFixedRotation(true);
		// footfixture.isSensor();

		mainbodyfixture.setUserData(this);
		
		if (isAI) {
			mainbody.setUserData("ai");
		} else {
			mainbody.setUserData("player");
		}
		mainbody.setLinearVelocity(0, 0);

		/*
		 * if (!isAI) { fixtureDef.filter.maskBits = 7;
		 * fixtureDef.filter.categoryBits = 8; } else {
		 * fixtureDef.filter.maskBits = 9; fixtureDef.filter.categoryBits = 4; }
		 */
		mainbody.createFixture(fixtureDef);
		pawnFoot = new PawnFoot(this, world, mainbody);

		// footfixture.
		height = 2;
		width = 1.25f;
		scenecamera = scam;

		aimLadderTexture = actorMan.humanSprite.aimLadderTexture;

		Texture armSwordTexture = actorMan.humanSprite.armSwordTexture;
		TextureRegion armSwordTexRegion = new TextureRegion(armSwordTexture, 0,
				0, 128, 64);

		Texture armUziTexture = actorMan.humanSprite.armUziTexture;
		TextureRegion armUziTexRegion = new TextureRegion(armUziTexture, 0, 0,
				128, 64);

		Texture armShotgunTexture = actorMan.humanSprite.armShotgunTexture;
		TextureRegion armShotgunTexRegion = new TextureRegion(
				armShotgunTexture, 0, 0, 128, 64);

		armshotgunsprite = new Sprite(armShotgunTexRegion);
		armshotgunsprite.setSize(2f, 1);
		armshotgunsprite.setOrigin(((1.77f)), armshotgunsprite.getHeight() / 2);

		armswordsprite = new Sprite(armSwordTexRegion);
		armswordsprite.setSize(2f, 1);
		armswordsprite.setOrigin(((1.77f)), armswordsprite.getHeight() / 2);

		armuzisprite = new Sprite(armUziTexRegion);
		armuzisprite.setSize(2f, 1);
		armuzisprite.setOrigin(((1.77f)), armuzisprite.getHeight() / 2);

		armsprite = armuzisprite;

		TextureRegion aimLadderTexRegion = new TextureRegion(aimLadderTexture,
				0, 0, 128, 128);

		aimladdersprite = new Sprite(aimLadderTexRegion);
		aimladdersprite.setPosition(-10, -10);
		aimladdersprite.scale(1f);

		runTextureAtlas = actorMan.humanSprite.runTextureAtlas;
		idleTextureAtlas = actorMan.humanSprite.idleTextureAtlas;
		backWalkTextureAtlas = actorMan.humanSprite.backWalkTextureAtlas;
		crouchTextureAtlas = actorMan.humanSprite.crouchTextureAtlas;
		crouchBackTextureAtlas = actorMan.humanSprite.crouchBackTextureAtlas;
		upLadderTextureAtlas = actorMan.humanSprite.upLadderTextureAtlas;
		deathTextureAtlas = actorMan.humanSprite.deathTextureAtlas;

		AtlasRegion runTexRegion = runTextureAtlas.findRegion("0000");
		AtlasRegion idleTexRegion = idleTextureAtlas.findRegion("0000");
		AtlasRegion crouchTexRegion = crouchTextureAtlas.findRegion("0000");
		AtlasRegion crouchBackTexRegion = crouchBackTextureAtlas
				.findRegion("0000");
		AtlasRegion backWalkTexRegion = backWalkTextureAtlas.findRegion("0000");
		AtlasRegion upLadderTexRegion = upLadderTextureAtlas.findRegion("0000");
		AtlasRegion deathTexRegion = deathTextureAtlas.findRegion("0000");

		runsprite = new Sprite(runTexRegion);
		runsprite.scale(1f);

		idlesprite = new Sprite(idleTexRegion);
		idlesprite.scale(1f);

		crouchbacksprite = new Sprite(crouchBackTexRegion);
		crouchbacksprite.scale(1f);

		crouchsprite = new Sprite(crouchTexRegion);
		crouchsprite.scale(1f);

		backwalksprite = new Sprite(backWalkTexRegion);
		backwalksprite.scale(1f);

		deathsprite = new Sprite(deathTexRegion);
		deathsprite.scale(1f);

		upladdersprite = new Sprite(upLadderTexRegion);
		upladdersprite.scale(1f);
		physics = new GamePhysics();

		// pBox.dispose();
		// pBox.dispose();

	}

	public void goLeft() {
		if (isAlive) {
			/*
			 * if (isCrouching || state == "crouchback" || state == "crouch") {
			 * MAX_MOVE_SPEED = 6; } else { MAX_MOVE_SPEED = 10; }
			 */
			velocity = mainbody.getLinearVelocity();

		/*	if (velocity.y == 0 || isOnLadder) {
				velocity.x -= moveSpeed;
				if (velocity.x < -MAX_MOVE_SPEED)
					velocity.x = -MAX_MOVE_SPEED;
				isGoRight = false;
				run = true;
			}
			if (velocity.y != 0 && velocity.x > -5 && canDoubleJump) {
				velocity.x = -moveSpeed;
			}*/
			
			if (velocity.x > -MAX_MOVE_SPEED){
				mainbody.applyLinearImpulse(-moveSpeed, 0, 0, 0, true);
			}
			
			
			//mainbody.setLinearVelocity(velocity);

		}
	}

	public void goRight() {
		if (isAlive) {

			velocity = mainbody.getLinearVelocity();
			/*
			 * if (isCrouching || state == "crouchback" || state == "crouch") {
			 * MAX_MOVE_SPEED = 6; } else { MAX_MOVE_SPEED = 10; }
			/* *//*
			if (velocity.y == 0 || isOnLadder) {
				velocity.x += moveSpeed;
				if (velocity.x > MAX_MOVE_SPEED)
					velocity.x = MAX_MOVE_SPEED;
				isGoRight = true;
				run = true;
			}
			if (velocity.y != 0 && velocity.x < 5 && canDoubleJump) {
				velocity.x = moveSpeed;
			}
			*/
			if (velocity.x < MAX_MOVE_SPEED){
				mainbody.applyLinearImpulse(moveSpeed, 0, 0, 0, true);
			}
			//mainbody.setLinearVelocity(velocity);
		}
	}

	public void goJumpDown() {
		isGrounded = false;
		isOnLadder = false;

	}

	long lasttimejump = System.currentTimeMillis();

	boolean groundCheck = false;
	
	public boolean groundCheck(){
		/*if ((levelMan.isCellBlocked(worldpos.x, worldpos.y-0.4f, false))
				||  (levelMan.isCellPlatform(worldpos.x, worldpos.y-0.4f))
				||  (levelMan.isCellDiagonal(worldpos.x, worldpos.y-0.4f)))
				{
			return true;
		}
		
		if ((levelMan.isCellBlocked(worldpos.x, worldpos.y-0.4f, false))
				||  (levelMan.isCellPlatform(worldpos.x, worldpos.y-0.4f))
				||  (levelMan.isCellDiagonal(worldpos.x, worldpos.y-0.4f))){
			return true;
		}
		return false;*/
		if (pawnFoot.numFootContacts > 0){
			return true;
		}
			return false;
	}
	
	public boolean checkLadder(){
		if (levelMan.isCellLadder(worldpos.x, worldpos.y)){
			return true;	
		}
		return false;
	}
	
	
	
	
	public void goJump() {
		
		if (checkLadder()){
			isOnLadder = true;
			
			mainbody.setLinearVelocity(0, 1);
			
		}else{
			if (System.currentTimeMillis() - lasttimejump > 300){
			/*if ((levelMan.isCellBlocked(worldpos.x, worldpos.y-0.4f, false))
						||  (levelMan.isCellPlatform(worldpos.x, worldpos.y-0.4f))
						||  (levelMan.isCellDiagonal(worldpos.x, worldpos.y-0.4f))){*/
				if (groundCheck()){
						lasttimejump = System.currentTimeMillis();
						mainbody.applyLinearImpulse(0, jumpSpeed, 0, 0, true);
				}else{
					//DazDebug.print("GROUND CHECK FAIL");
				}
			}
		}
	}

	public void stopVelocity() {
	//	mainbodyfixture.setFriction(100);
		//mainbodyfixture.setFriction(500);
	}

	public void resetInputFlags(){
		 pressUp = false;
		 pressDown = false;
		 pressRight = false;
		 pressLeft = false;
		 pressShoot = false;
	}
	
	boolean nextFall = false;
	public void update(boolean isWorldCoord, Camera camera, boolean shoot) {
		
	//	if (numFootContacts != 0){
		if (!isAI){
			DazDebug.print("footcontacts:" + pawnFoot.numFootContacts);
		}
		//}
		velocity = mainbody.getLinearVelocity();
		
		if (velocity.y > 0){
				pawnFoot.fallMode();
		}else{
			pawnFoot.platformMode();
		}
		if (pressDown){
			pawnFoot.fallMode();
		}

		
		if (checkLadder() && isOnLadder){
			mainbody.setGravityScale(0);
		}else if (!checkLadder()){
			mainbody.setGravityScale(1);
		}
		
		fixtureDef.filter.categoryBits = 3; 
		
		if (!groundCheck()){
			
		}else{
			
		}
		
		
		if (pressUp){
			goJump();
		}
		if (pressShoot){
			quickShoot();
		}
		if (pressDown){
			isCrouching = true;
		}
		
		
		if (pressRight) {
			goRight();
		} else if (pressLeft) {
			goLeft();
		} else if (!pressLeft && !pressRight && Math.abs(velocity.x) > 0.1) {
			stopVelocity();
		}
		goDirection = 0;

		worldpos = mainbody.getPosition();
		float inx = aimingAt.x;
		float iny = aimingAt.y;

		if (isAI) {
			boolean facingtarget = true;
			boolean flip = false;
			boolean aimless = false;
			inx = 0;
			iny = 0;
			aimless = true;
			isWorldCoord = false;
		}

		if (!armsprite.isFlipX())
			armsprite.flip(true, false);

		boolean facingtarget = true;
		boolean flip = false;
		boolean aimless = false;
		currentFrame++;

		if (inx == 0 && iny == 0) {
			aimless = true;
			isWorldCoord = false;
			if (movingdirection == "right")
				inx = -2;
			if (movingdirection == "left" && !isLevelScrolling)
				inx = 2;
		} else {
			iny -= 1;
		}

		if (velocity.x < 0) {
			movingdirection = "right";
			if (aimless) {
				aimingdirection = "right";
			}
		} else if (velocity.x > 0) {
			movingdirection = "left";
			if (aimless) {
				aimingdirection = "left";
			}
		} else if (velocity.x == 0) {
		}

		if (isLevelScrolling) {
			movingdirection = "left";
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

		if (aimAngle > 90 && aimAngle < 270) {
			aimingdirection = "right";
		} else {
			aimingdirection = "left";
		}

		float tx = targetWorldVec.x;
		float ty = targetWorldVec.y;

		if (velocity.x != 0 && velocity.y == 0 && isGrounded) {
			state = "run";
		}
		if (velocity.x == 0 && velocity.y == 0 && !isOnLadder) {
			state = "idle";
			if (isLevelScrolling) {
				movingdirection = "left";
				state = "run";
				if (aimingdirection == "left") {
					state = "runback";
				}
			}
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
			if (isLevelScrolling) {
				state = "run";
				movingdirection = "left";
			}
			if (isCrouching) {
				state = "crouchidle";
				if (isLevelScrolling) {
					state = "crouchrun";
				}
			}
		}
		if (!isGrounded && !isOnLadder && velocity.y != 0) {
			state = "jumping";
		}

		if (isOnLadder && !aimless) {
			state = "ladderaim";
			isShooting = true;
		}

		if (state == "run" || state == "ladderclimb" || state == "runback"
				|| state == "crouch" || state == "crouchback" || !isAlive) {
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
		} else if (state == "crouch") {
			sprite = crouchsprite;
		} else if (state == "crouchback") {
			sprite = crouchbacksprite;
		} else if (state == "crouchidle") {
			currentFrame = 0;
			sprite = crouchsprite;
		}
		if (!isAlive) {
			currentFrame = deathanim;
			sprite = deathsprite;
			currentAtlasKey = String.format("%04d", currentFrame);

			if (deathanim < 7 && !isGrounded)
				deathanim++;
			if (deathanim < 10 && isGrounded)
				deathanim++;
			deathsprite
					.setRegion(deathTextureAtlas.findRegion(currentAtlasKey));
		}

		if (isCrouching && isGrounded) {
			armyadd = -0.55f;
		}

		if (aimingdirection == "left" && !sprite.isFlipX()) {
			flip = true;
		}

		if (flip && isAlive) {
			sprite.flip(true, false);
		}

		if (aimingdirection == "right") {
			if (armsprite.isFlipY())
				armsprite.flip(false, true);
		} else {
			if (!armsprite.isFlipY())
				armsprite.flip(false, true);
			if (aimladdersprite.isFlipX())
				aimladdersprite.flip(true, false);
		}

		Vector2 tmpAimVec = new Vector2(targetScreenVec.x, targetScreenVec.y);
		if (isAI) {
			aimAngle = aimAtPlayer;
		} else {
			aimAngle = tmpAimVec.angle();
		}
		switch (weapon.weaponid) {
		case 0:
			armsprite = armswordsprite;
			break;
		case 1:
			armsprite = armuzisprite;
			break;
		case 2:
			armsprite = armshotgunsprite;
			break;
		}

		sprite.setSize(1f, 1f);
		sprite.setOrigin(sprite.getWidth() / 2, 0);
		sprite.setPosition(worldpos.x - 0.5f, worldpos.y - 1f);
		armsprite.setRotation(aimAngle - 180);
		armsprite.setPosition(worldpos.x - 1.76f, worldpos.y + armyadd);

		if (type == "enemy") {
			sprite.setColor(Color.RED);
		} else {
			sprite.setColor(Color.WHITE);
		}
		if (!aimless) {
			isShooting = true;
		} else {
			isShooting = false;
		}
		// mainbody.setTransform(worldpos.x, worldpos.y + 1, 0f);
		// pawnFoot.footbody.setTransform(worldpos.x, worldpos.y + 0.1f, 0f);
		// pawnFoot.footbody.
		if (levelMan == null) {
			System.out.println("levelMan IN ACTOR IS NULL");
		}
	//	physics.doPhysics(this);
		// if (pawnFoot.isOnGround) {
		// DazDebug.print("PAWNENTITY IS ON GROUND");
		// }
		// pawnFoot.isOnGround = false;
		/*
		 * if (isPlayerGrounded() && !isAI){ DazDebug.print("playergrounded");
		 * isGrounded = true; }
		 */
		pressJump = false;
		goThruPlatform = false;
		isShooting = false;

		aimingAt.x = 0;
		aimingAt.y = 0;
		
		resetInputFlags();
	}

	public float distanceFromPlayer = 0;

	public void updateAI(PawnEntity zplayer) {
		// attemptShoot(0);
		if (isAlive && zplayer.isAlive) {
			isAI = true;
			type = "enemy";
			float relativetoplayerx = zplayer.worldpos.x - worldpos.x;
			float relativetoplayery = zplayer.worldpos.y - worldpos.y;
			relativepos.x = relativetoplayerx;
			relativepos.y = relativetoplayery;
			distanceFromPlayer = (relativetoplayerx * relativetoplayerx)
					+ (relativetoplayery * relativetoplayery);
			distanceFromPlayer = Math
					.abs((float) Math.sqrt(distanceFromPlayer));

			targetWorldVec.x = zplayer.worldpos.x;
			targetWorldVec.y = zplayer.worldpos.y;
			aimAtPlayer = relativepos.angle();
			aimAngle = aimAtPlayer;
			if (relativetoplayerx < -shootDist) {
				goLeft();
			} else if (relativetoplayerx > shootDist) {
				goRight();

			} else {
				attemptShoot(relativepos.angle());
			}
			if (relativetoplayery > 2) {
				goJump();
			}
			if (isGrounded && isJumpy) {
				goJump();
			}
		}

		
	}

	public void takeDamage(float damage) {
		health -= damage;
		if (health <= 0) {
			isAlive = false;
			mainbody.setActive(false);
			mainbody.setAwake(false);
			isOnLadder = false;
		}
	}

	public void dispose() {
		runTextureAtlas.dispose();
		idleTextureAtlas.dispose();
		backWalkTextureAtlas.dispose();
		armTexture.dispose();
		aimLadderTexture.dispose();
		crouchTextureAtlas.dispose();
		crouchBackTextureAtlas.dispose();
		upLadderTextureAtlas.dispose();
		deathTextureAtlas.dispose();
	}

	float distanceFromTarget = 0;
	private boolean targetIsNull = false;
	public int numFootContacts = 0;


	public void giveQuickTarget(PawnEntity ztarget) {
		float relativetoplayerx = ztarget.worldpos.x - worldpos.x;
		float relativetoplayery = ztarget.worldpos.y - worldpos.y;
		targetIsNull = false;
		// the actorTarget vector is relative to the player, as if the player is
		// at 0,0

		actorTarget.x = relativetoplayerx;
		actorTarget.y = relativetoplayery;
	}

	public void giveQuickTarget(Drone ztarget) {
		float relativetoplayerx = ztarget.worldpos.x - worldpos.x;
		float relativetoplayery = ztarget.worldpos.y - worldpos.y;
		targetIsNull = false;
		// the actorTarget vector is relative to the player, as if the player is
		// at 0,0

		actorTarget.x = relativetoplayerx;
		actorTarget.y = relativetoplayery;
	}

	public void quickShoot() {
		if (!targetIsNull) {
			attemptShoot(actorTarget.angle());
			aimingAt.x = actorTarget.x;
			aimingAt.y = actorTarget.y;
		} else {
			if (movingdirection == "right") {
				attemptShoot(180);
			} else {
				attemptShoot(0);
			}
		}
	}

	public void setTargetToNull() {
		targetIsNull = true;
		// TODO Auto-generated method stub

	}

	public void Pickup(Item pickUpItem) {
		if (pickUpItem.isWeapon) {
			int holdId = weapon.weaponid;
			weapon.setWeapon(pickUpItem.itemWeaponNumber);
			pickUpItem.dropWeapon(holdId);
		}
	}

	private boolean isPlayerGrounded() {// (float deltaTime) {
		// groundedPlatform = null;
		Array<Contact> contactList = world.getContactList();
		for (int i = 0; i < contactList.size; i++) {
			Contact contact = contactList.get(i);
			if (contact.isTouching()
					&& (contact.getFixtureA() == mainbodyfixture || contact
							.getFixtureB() == mainbodyfixture)) {

				Vector2 pos = worldpos;
				WorldManifold manifold = contact.getWorldManifold();
				boolean below = true;
				for (int j = 0; j < manifold.getNumberOfContactPoints(); j++) {
					below &= (manifold.getPoints()[j].y < pos.y - 1.5f);
				}

				if (below) {
					if (contact.getFixtureA().getUserData() != null
							&& contact.getFixtureA().getUserData()
									.equals("ground")) {
						// groundedPlatform =
						// (Platform)contact.getFixtureA().getBody().getUserData();
						DazDebug.print("onGround");
					}

					if (contact.getFixtureB().getUserData() != null
							&& contact.getFixtureB().getUserData()
									.equals("ground")) {
						// groundedPlatform =
						// (Platform)contact.getFixtureB().getBody().getUserData();
						DazDebug.print("onGround");
					}
					return true;
				}

				return false;
			}
		}
		return false;
	}

	// }

}