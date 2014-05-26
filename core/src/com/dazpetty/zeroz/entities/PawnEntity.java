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
	 * ORDINARY VARIABLES
	 */
	public int deathanim = 0;
	public float jumpSpeed = 16;
	public float aimAngle = 0;
	public float moveSpeed = 3;
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
	public int MAX_MOVE_SPEED = 10;
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
	/*
	 * LIBGDX/BOX2D OBJECTS
	 */
	protected Camera scenecamera;
	public World world;
	public BodyDef bodyDef = new BodyDef();

	public Body mainbody;
	public Sprite grensprite;
	public FixtureDef fixtureDef;

	// public CircleShape dynamicCircle;
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

		if (mainbody == null) {
			pawnFoot = new PawnFoot(this, world);

			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.KinematicBody;
			mainbody = world.createBody(bodyDef);
		} else {
			mainbody.setActive(true);
			mainbody.setAwake(true);

		}

		worldpos = actorstart;
		initialized = true;

		isDisposed = false;
		isAI = amAI;
		this.world = world;

		fixtureDef = new FixtureDef();
		PolygonShape pBox = new PolygonShape();
		pBox.setAsBox(0.25f, 0.8f);
		fixtureDef.shape = pBox;
		fixtureDef.isSensor = true;
		Fixture fixture = mainbody.createFixture(fixtureDef);

		// footfixture.isSensor();

		mainbody.setUserData(this);

		if (isAI) {
			fixture.setUserData("ai");
		} else {
			fixture.setUserData("player");
		}
		mainbody.setLinearVelocity(0, 0);

		if (!isAI) {
			fixtureDef.filter.maskBits = 7;
			fixtureDef.filter.categoryBits = 8;
		} else {
			fixtureDef.filter.maskBits = 9;
			fixtureDef.filter.categoryBits = 4;
		}
		mainbody.createFixture(fixtureDef);

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

		// armsprite.setPosition(-10-64, -10);

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

		pBox.dispose();

	}



	public void goLeft() {
		if (isAlive) {
			if (isCrouching || state == "crouchback" || state == "crouch") {
				MAX_MOVE_SPEED = 6;
			} else {
				MAX_MOVE_SPEED = 10;
			}
			if (velocity.y == 0 || isOnLadder) {
				velocity.x -= moveSpeed;
				if (velocity.x < -MAX_MOVE_SPEED)
					velocity.x = -MAX_MOVE_SPEED;
				isGoRight = false;
				run = true;
			}
			if (velocity.y != 0 && velocity.x > -5 && canDoubleJump) {
				velocity.x -= moveSpeed;
			}
		}
	}

	public void goRight() {
		if (isAlive) {
			if (isCrouching || state == "crouchback" || state == "crouch") {
				MAX_MOVE_SPEED = 6;
			} else {
				MAX_MOVE_SPEED = 10;
			}
			if (velocity.y == 0 || isOnLadder) {
				velocity.x += moveSpeed;
				if (velocity.x > MAX_MOVE_SPEED)
					velocity.x = MAX_MOVE_SPEED;
				isGoRight = true;
				run = true;
			}
			if (velocity.y != 0 && velocity.x < 5 && canDoubleJump) {
				velocity.x += moveSpeed;
			}
		}
	}

	public void goJumpDown() {
		isGrounded = false;
		isOnLadder = false;

	}

	public void goJump() {
		pressJump = true;
		if (levelMan != null) {
			if (isAlive) {
				for (float i = -0.55f; i < 0.55f; i += 0.55f) {
					if (levelMan.isCellLadder(worldpos.x + i, worldpos.y)) {
						worldpos.x = (int) worldpos.x + i + 0.4f;
					}
				}

				if (levelMan.isCellLadder(worldpos.x, worldpos.y)) {
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
					if (!isCrouching) {
						velocity.y += jumpSpeed;
					} else {

					}
					isGrounded = false;
					isOnLadder = false;
				} else if ((velocity.x >= 0 && levelMan.isCellBlocked(
						worldpos.x + 0.5f, worldpos.y, true))
						|| (velocity.x <= 0 && levelMan.isCellBlocked(
								worldpos.x - 0.5f, worldpos.y, true))) {
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
		} else {
			System.out.println("Error, TileLayerManager is NULL");
		}
	}

	public void update(boolean isWorldCoord, Camera camera, boolean shoot) {

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
		sprite.setPosition(worldpos.x - 0.5f, worldpos.y);
		armsprite.setRotation(aimAngle - 180);
		armsprite.setPosition(worldpos.x - 1.76f, worldpos.y + 1 + armyadd);

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
		mainbody.setTransform(worldpos.x, worldpos.y + 1, 0f);
		pawnFoot.footbody.setTransform(worldpos.x, worldpos.y + 0.1f, 0f);
		if (levelMan == null) {
			System.out.println("levelMan IN ACTOR IS NULL");
		}
		physics.doPhysics(this);
		if (pawnFoot.isOnGround) {
			// DazDebug.print("PAWNENTITY IS ON GROUND");
		}
		// pawnFoot.isOnGround = false;
		pressJump = false;
		goThruPlatform = false;
		isShooting = false;

		aimingAt.x = 0;
		aimingAt.y = 0;
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

	// }

}