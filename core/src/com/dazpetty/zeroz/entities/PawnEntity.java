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
import com.dazpetty.zeroz.managers.EntityManager;
import com.dazpetty.zeroz.managers.ProjectileManager;
import com.dazpetty.zeroz.managers.LevelManager;
import com.dazpetty.zeroz.managers.SceneManager;

/*
 * Pawn Entity is the base class for entities controlled by the player or ai,
 *		Scrolling error occurs only if idle and shooting/aiming
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
	public boolean isFlying = false;
	public boolean isGoRight = true;
	public boolean isGoLeft = false;
	public boolean hasTarget = false;
	public boolean hasEnemy = false;
	public boolean isShooting = false;
	public boolean aimOnLadder = false;
	public boolean isLevelScrolling = false;

	/*
	 * INPUT HELPERS
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
	public float jumpSpeed = 16;
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

	public Vector2 worldpos = new Vector2(0, 0);
	public Vector2 velocity = new Vector2(0, 0);
	public Vector3 targetWorldVec = new Vector3(0, 0, 0);
	public Vector3 targetScreenVec = new Vector3(0, 0, 0);

	public Vector3 aimingAt = new Vector3(0, 0, 0);
	public Vector2 actorTarget = new Vector2(0, 0);

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
	public Camera camera;

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
	public void useEntity(EntitySpawner spawner) {

		this.weapon = spawner.weapon;
		worldpos = spawner.worldpos;
		mainbody.setTransform(spawner.worldpos, 0);
		mainbody.setActive(true);
		mainbody.setAwake(true);
		isDisposed = false;
		isOnLadder = false;
		isAlive = true;

		health = startinghealth;
		DazDebug.print("ENTITY IN AT" + worldpos.x + worldpos.y);
	}

	public void attemptShoot(float ang) {
		projMan.setWorld(world);

		if (isAlive) {
			aimAngle = ang;
			projMan.shootProjectile(ang, this);
		}
	}

	public EntitySpawner spawner;
	public SceneManager scene; 
	public EntityManager entMan;

	public PawnEntity(EntityManager entMan, EntitySpawner spawner) {

		this.scene = entMan.scene;
		this.spawner = spawner;
		weapon = new Weapon(1);
		this.entMan = entMan;
		this.camera = entMan.camera;
		this.world = entMan.world;
		this.id = id;
		this.levelMan = entMan.levelMan;

		isLevelScrolling = levelMan.isLevelScrolling;
		projMan = scene.projMan;

		if (levelMan == null) {
			levelMan = (LevelManager) levelMan;
			if (levelMan == null) {
				System.out.println("levelMan is null");
			}
		}

		if (spawner.worldpos.x == 0) {
			spawner.worldpos = (new Vector2(7, 14));
		}
		worldpos = spawner.worldpos;

		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(worldpos);
		bodyDef.type = BodyType.DynamicBody;

		mainbody = world.createBody(bodyDef);
		mainbody.setActive(false);
		mainbody.setAwake(false);
		mainbody.setBullet(true);

		isDisposed = false;
		isAI = spawner.isAI;

		fixtureDef = new FixtureDef();
		fixtureDef.friction = 0;
		fixtureDef.density = 1.5f;
		PolygonShape pBox = new PolygonShape();

		float hadjust = 0.7f;
		Vector2[] collisionShape = new Vector2[5];
		collisionShape[0] = new Vector2(0f, (float) (1.6 - hadjust));
		collisionShape[1] = new Vector2(0.3f, (float) (1 - hadjust));
		collisionShape[2] = new Vector2(0.15f, -0.1f - hadjust);
		collisionShape[3] = new Vector2(-0.15f, -0.1f - hadjust);
		collisionShape[4] = new Vector2(-0.3f, (float) (1 - hadjust));
		pBox.set(collisionShape);
		// pBox.setPosition(new Vector2(0, -0.8f));
		// pBox.setAsBox(0.3f, 0.8f);
		fixtureDef.shape = pBox;

		Filter pawnfilter = fixtureDef.filter;
		pawnfilter.maskBits = 3;
		mainbodyfixture = mainbody.createFixture(fixtureDef);
		mainbody.setBullet(true);
		mainbody.setFixedRotation(true);

		mainbodyfixture.setUserData(this);

		if (isAI) {
			mainbody.setUserData("ai");
		} else {
			mainbody.setUserData("player");
		}
		mainbody.setLinearVelocity(0, 0);

		mainbody.createFixture(fixtureDef);
		pawnFoot = new PawnFoot(this, world, mainbody);

		height = 2;
		width = 1.25f;

		pBox.dispose();
		setupTextures();

		/*
		 * Entites created in the constructor are dead, then they are brought to
		 * life by the below code, this is so that both new entites and
		 * entites "resurrected"  in the array, use the same code when spawning into the
		 * world.
		 */
		useEntity(spawner);
	}

	public void setupTextures() {

		aimLadderTexture = entMan.humanSprite.aimLadderTexture;

		Texture armSwordTexture = entMan.humanSprite.armSwordTexture;
		TextureRegion armSwordTexRegion = new TextureRegion(armSwordTexture, 0,
				0, 128, 64);

		Texture armUziTexture = entMan.humanSprite.armUziTexture;
		TextureRegion armUziTexRegion = new TextureRegion(armUziTexture, 0, 0,
				128, 64);

		Texture armShotgunTexture = entMan.humanSprite.armShotgunTexture;
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

		runTextureAtlas = entMan.humanSprite.runTextureAtlas;
		idleTextureAtlas = entMan.humanSprite.idleTextureAtlas;
		backWalkTextureAtlas = entMan.humanSprite.backWalkTextureAtlas;
		crouchTextureAtlas = entMan.humanSprite.crouchTextureAtlas;
		crouchBackTextureAtlas = entMan.humanSprite.crouchBackTextureAtlas;
		upLadderTextureAtlas = entMan.humanSprite.upLadderTextureAtlas;
		deathTextureAtlas = entMan.humanSprite.deathTextureAtlas;

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
	}

	public void goLeft() {
		if (isAlive) {
			velocity = mainbody.getLinearVelocity();
			if (velocity.x > -MAX_MOVE_SPEED) {
				mainbody.applyLinearImpulse(-moveSpeed, 0, 0, 0, true);
			}
		}
	}

	public void goRight() {
		if (isAlive) {
			velocity = mainbody.getLinearVelocity();
			if (velocity.x < MAX_MOVE_SPEED) {
				mainbody.applyLinearImpulse(moveSpeed, 0, 0, 0, true);
			}
		}
	}

	public void goJumpDown() {
		isGrounded = false;
		isOnLadder = false;

	}

	long lasttimejump = System.currentTimeMillis();

	boolean groundCheck = false;

	public boolean groundCheck() {
		if (pawnFoot.numFootContacts > 0) {
			isGrounded = true;
			return true;

		}
		isGrounded = false;
		return false;
	}

	public boolean checkLadder() {
		if (levelMan.isCellLadder(worldpos.x, worldpos.y)) {
			return true;
		}
		return false;
	}

	public void goJump() {

		if (checkLadder()) {
			isOnLadder = true;
			mainbody.setLinearVelocity(0, 1);

		} else {
			if (System.currentTimeMillis() - lasttimejump > 300) {
				if (groundCheck()) {
					lasttimejump = System.currentTimeMillis();
					mainbody.applyLinearImpulse(0, jumpSpeed, 0, 0, true);

				} else {
					// DazDebug.print("GROUND CHECK FAIL");
				}
			}
		}
	}

	public void velocityCheck() {
		if (mainbody.getLinearVelocity().y > jumpSpeed) {
			mainbody.setLinearVelocity(new Vector2(
					mainbody.getLinearVelocity().x, jumpSpeed));
		}
	}

	public void stopVelocity(boolean b) {
		if (b) {
			// mainbodyfixture.setFriction(1000);
			fixtureDef.friction = 0;
			pawnFoot.footfixtureDef.friction = 1000;
		} else {
			mainbodyfixture.setFriction(1);
			fixtureDef.friction = 1;
			pawnFoot.footfixtureDef.friction = 1;
		}

	}

	public void resetInputFlags() {
		pressUp = false;
		pressDown = false;
		pressRight = false;
		pressLeft = false;
		pressShoot = false;
	}

	boolean nextFall = false;

	public void update(boolean isWorldCoord, Camera camera) {

		velocityCheck();
		// if (numFootContacts != 0){
		if (!isAI) {
			// DazDebug.print("footcontacts:" + pawnFoot.numFootContacts);
		}
		// }
		velocity = mainbody.getLinearVelocity();

		if (velocity.y > 0) {
			pawnFoot.fallMode();
		} else {
			pawnFoot.platformMode();
		}
		/*
		 * CONTROLS
		 */

		if (pressUp) {
			goJump();
		}
		if (pressShoot) {
			quickShoot();
		}

		if (pressDown) {
			pawnFoot.fallMode();
			isCrouching = true;
			if (isOnLadder) {
				mainbody.setLinearVelocity(0, -1);
			}
		}

		/*
		 * if (checkLadder() && !isGrounded){ isOnLadder = true; }
		 */

		if (checkLadder() && isOnLadder) {
			mainbody.setGravityScale(0);
			if (!pressDown && !pressUp) {
				mainbody.setLinearVelocity(0, 0);
			}
		} else if (!checkLadder()) {
			mainbody.setGravityScale(1);
		}

		fixtureDef.filter.categoryBits = 3;

		if (!groundCheck()) {

		} else {

		}

		if (pressRight) {
			goRight();
		} else if (pressLeft) {
			goLeft();
		}

		if (!pressLeft && !pressRight && !pressUp && isGrounded) {
			stopVelocity(true);

			// System.out.println("STOPSTOPSTOPSTOPSTOPSTOP");
		} else {
			stopVelocity(false);
		}
		goDirection = 0;

		calculateFrame();

		if (levelMan == null) {
			System.out.println("levelMan IN ACTOR IS NULL");
		}

		goThruPlatform = false;
		isShooting = false;

		aimingAt.x = 0;
		aimingAt.y = 0;

		if (worldpos.y < -10) {
			isAlive = false;
		}

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
						DazDebug.print("onGround");
					}

					if (contact.getFixtureB().getUserData() != null
							&& contact.getFixtureB().getUserData()
									.equals("ground")) {
						DazDebug.print("onGround");
					}
					return true;
				}

				return false;
			}
		}
		return false;
	}

	public void calculateFrame() {

		float minMove = 2;
		worldpos = mainbody.getPosition();

		if (isAI) {
			boolean facingtarget = true;
			boolean flip = false;
			boolean aimless = false;
			aimingAt.x = 0;
			aimingAt.y = 0;
			aimless = true;
			// isWorldCoord = false;
		}

		if (!armsprite.isFlipX())
			armsprite.flip(true, false);

		boolean facingtarget = true;
		boolean flip = false;
		boolean aimless = false;
		currentFrame++;

		if (aimingAt.x == 0 && aimingAt.y == 0) {
			aimless = true;
			// isWorldCoord = false;
			if (movingdirection == "right")
				aimingAt.x = -2;
			if (movingdirection == "left" && !isLevelScrolling)
				aimingAt.x = 2;
		} else {
			aimingAt.y -= 1;
		}

		if (velocity.x < 0) {
			if (velocity.x < -minMove) {
				movingdirection = "right";
			}
			if (aimless) {
				aimingdirection = "right";
			}
		} else if (velocity.x > 0) {
			if (velocity.x > minMove) {
				movingdirection = "left";
			}
			if (aimless) {
				aimingdirection = "left";
			}
		} else if (velocity.x == 0) {
		}

		if (isLevelScrolling) {
			movingdirection = "left";
		}

		targetScreenVec.x = aimingAt.x;
		targetScreenVec.y = aimingAt.y;
		targetWorldVec.x = aimingAt.x;
		targetWorldVec.y = aimingAt.y;

		if (isAI) {
			camera.project(targetScreenVec);
		} else {
			camera.unproject(targetWorldVec);
		}
		// camera.unproject(targetWorldVec);

		if (aimAngle > 90 && aimAngle < 270) {
			aimingdirection = "right";
		} else {
			aimingdirection = "left";
		}

		// float tx = targetWorldVec.x;
		// float ty = targetWorldVec.y;

		if (Math.abs(velocity.x) > minMove && velocity.y == 0 && isGrounded) {
			state = "run";
		}
		if ((Math.abs(velocity.x) < minMove) && velocity.y == 0 && !isOnLadder) {
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
		if (Math.abs(velocity.x) > minMove
				&& movingdirection == aimingdirection) {
			state = "run";
			if (isCrouching) {
				state = "crouch";
			}
		} else if (Math.abs(velocity.x) > minMove
				&& movingdirection != aimingdirection) {
			state = "runback";
			if (isCrouching) {
				state = "crouchback";
			}
		} else if (Math.abs(velocity.x) < minMove && velocity.y == 0) {
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
		// Vector2 tmpAimVec = new Vector2(targetScreenVec.x,
		// targetScreenVec.y);
		Vector2 tmpAimVec = new Vector2(aimingAt.x, aimingAt.y);
		if (isAI) {
			aimAngle = aimAtPlayer;
		} else {
			aimAngle = tmpAimVec.angle();
			/*
			 * AIMANGLE
			 */

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
		/*
		 * AIM ANGLE
		 */
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
	}
}