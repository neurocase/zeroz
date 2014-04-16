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
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/*
 * Zactor: Zactor is the Actor class, all game entities which move are actors,
 * actor is the base class for enemies, ai and the player.
 * 
 */

public class Actor{

	public boolean isJump = true;
	public boolean isAlive = true;
	public boolean isGrounded = true;
	
	//isDisposed is for clearing away enemies that are not near the player 
	public boolean isDisposed = false;
	
	public boolean isOnLadder = false;
	public boolean canDoubleJump = false;
	public boolean goThruPlatform = false;
	public boolean isCrouching = false;
	public boolean isAI = false;
	public boolean isDead = false;
	
	public int deathanim = 0;

	public float jumpSpeed = 16;
	public float moveSpeed = 3;
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
	private GamePhysics physics;
	public float aimAtPlayer = 0;
	
	public World world;
	
	public int MAX_MOVE_SPEED = 10;
	
	private static final int FRAME_COLS = 5; // #1
	private static final int FRAME_ROWS = 5; // #2


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
	
	
	public Sprite deathsprite;

	public Sprite sprite;
	public TextureRegion armTexRegion;

	public Texture armTexture;
	public Texture aimLadderTexture;

	private String currentAtlasKey = new String("0000");

	private int currentFrame = 1;
	private TextureAtlas runTextureAtlas;
	private TextureAtlas idleTextureAtlas;
	private TextureAtlas backWalkTextureAtlas;
	private TextureAtlas crouchTextureAtlas;
	private TextureAtlas crouchBackTextureAtlas;
	private TextureAtlas upLadderTextureAtlas;
	
	private TextureAtlas deathTextureAtlas;

	float stateTime;

	private AtlasRegion runTexRegion;

	public boolean hasTarget = false;
	public boolean hasEnemy = false;
	public boolean isShooting = false;
	public Weapon weapon = new Weapon();  
	
	protected Camera scenecamera;
	
	
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
	
	
	//public Bullet[] bulletArray = new Bullet[100];

	
	
	public BodyDef bodyDef = new BodyDef();
	public Body body;
	public Sprite grensprite;
	public FixtureDef fixtureDef;
	public CircleShape dynamicCircle;	
	public boolean run = false;
	public float height = 0;
	public float width = 0;
	

      
    
	
	public Actor(Camera scam, World world, boolean amAI, TiledMapTileLayer cLayer, Vector2 actorstart, int id, HumanSprite humanSprite){
		
		BodyDef bodyDef = new BodyDef(); 
	    bodyDef.type = BodyType.DynamicBody;
	    
	    if (body != null){
	    	world.destroyBody(body);
	    }
	    
	    body = world.createBody(bodyDef);  
	    
		worldpos = actorstart;
		collisionLayer = cLayer;
		initialized = true;
		
		isDisposed = false;
		isAI = amAI;
		this.world = world;
		
		body.setUserData(Integer.toString(id));

		fixtureDef = new FixtureDef(); 
	    PolygonShape pBox = new PolygonShape();
	    pBox.setAsBox(0.5f, 1f);
	    fixtureDef.shape = pBox;
	    //Fixture fixture = body.createFixture(pBox, 0.0f);
	    Fixture fixture = body.createFixture(fixtureDef);
	    if (isAI){
	    fixture.setUserData("ai");
	    }else{
	    	fixture.setUserData("player");
	    }
	    body.setLinearVelocity(0, 0);
	    
	    
	    fixtureDef.shape = pBox;  
	    fixtureDef.density = 0.5f;  
	    fixtureDef.friction = 0.3f;  
	    fixtureDef.restitution = 0.6f;
	    if (!isAI){
	    	fixtureDef.filter.maskBits = 7;
	    	fixtureDef.filter.categoryBits = 8;
	    }else{
	    	fixtureDef.filter.maskBits = 9;
	    	fixtureDef.filter.categoryBits = 4;
	    }
	    body.createFixture(fixtureDef);
	    
	//	for (int i = 0; i < 100; i++) bulletArray[i] = new Bullet();
		
		height = 2;
		width = 1.25f;
		scenecamera = scam;
		/*aimLadderTexture = new Texture(
				Gdx.files.internal("data/gfx/zman/ladderlean.png"));
		aimLadderTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);*/

		aimLadderTexture = humanSprite.aimLadderTexture;
		
		/*armTexture = new Texture(
				Gdx.files.internal("data/gfx/zman/arm3.png"));
		armTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);*/
		
		armTexture = humanSprite.armTexture;

		TextureRegion aimLadderTexRegion = new TextureRegion(aimLadderTexture,
				0, 0, 128, 128);

		TextureRegion armTexRegion = new TextureRegion(armTexture, 0,
				0, 64, 64);

		aimladdersprite = new Sprite(aimLadderTexRegion);
		aimladdersprite.setPosition(-10, -10);// Gdx.graphics.getWidth()/2-width*32,Gdx.graphics.getHeight()/2);
		aimladdersprite.scale(1f);

		
		
		armsprite = new Sprite(armTexRegion);
		armsprite.setSize(1, 1);
		armsprite.setOrigin(((armsprite.getWidth() * 0.77f)),
				armsprite.getHeight() / 2);
		armsprite.setPosition(-10, -10);

		//armsprite = new Sprite();

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
		

		deathTextureAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/zman/death1.atlas"));

		AtlasRegion runTexRegion = runTextureAtlas.findRegion("0000");
		AtlasRegion idleTexRegion = idleTextureAtlas.findRegion("0000");
		AtlasRegion crouchTexRegion = crouchTextureAtlas.findRegion("0000");
		AtlasRegion crouchBackTexRegion = crouchBackTextureAtlas.findRegion("0000");
		AtlasRegion backWalkTexRegion = backWalkTextureAtlas.findRegion("0000");
		AtlasRegion upLadderTexRegion = upLadderTextureAtlas.findRegion("0000");
		AtlasRegion deathTexRegion = deathTextureAtlas.findRegion("0000");
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

		deathsprite = new Sprite(deathTexRegion);
		deathsprite.setPosition(-32, -10);
		deathsprite.scale(1f);
		
		upladdersprite = new Sprite(upLadderTexRegion);
		upladdersprite.setPosition(-32, -10);
		upladdersprite.scale(1f);
		physics = new GamePhysics();
		
		
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
		if (isAlive){
			if (isCrouching || state == "crouchback" || state == "crouch"){ MAX_MOVE_SPEED = 6; }else{ MAX_MOVE_SPEED = 10;}
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
		if (isAlive){
			if (isCrouching || state == "crouchback" || state == "crouch"){ MAX_MOVE_SPEED = 6; }else{ MAX_MOVE_SPEED = 10;}
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

	public void goJumpDown(){
		isGrounded = false;
		isOnLadder = false;
		
	}
	
	public void goJump() {
		if (isAlive){
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
		

	}
	
	public void update(float inx, float iny, boolean isWorldCoord,
			Camera camera, boolean shoot) {
		
		
		if (!armsprite.isFlipX()) armsprite.flip(true, false);
		
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
		}else{
			iny-=1;
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

		targetScreenVec.x = inx;
		targetScreenVec.y = iny;
		targetWorldVec.x = inx;
		targetWorldVec.y = iny;

		if (isWorldCoord) {
			camera.project(targetScreenVec);
		} else {
			camera.unproject(targetWorldVec);
		}	
		
		if (aimAngle > 90 && aimAngle < 270){
			aimingdirection = "right";
		}else{
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
		
		if (health < 0){
			isAlive = false;
		}
		
		if (state == "run" || state == "ladderclimb" || state == "runback" || state == "crouch" || state == "crouchback"|| isDead) {
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
		if (!isAlive){
			currentFrame = deathanim;
			sprite = deathsprite;
			currentAtlasKey = String.format("%04d", currentFrame);
			
			if (deathanim < 7 && !isGrounded) deathanim++;
			if (deathanim < 10 && isGrounded) deathanim++;
			deathsprite.setRegion(deathTextureAtlas
					.findRegion(currentAtlasKey));
		}
		
		if (isCrouching && isGrounded){
			armyadd = -0.55f;
		}
		
		if (aimingdirection == "left" && !sprite.isFlipX()) {
			flip = true;
		}

		if (flip && isAlive) {
			sprite.flip(true, false);
		}

		if (aimingdirection == "right") {
			if (armsprite.isFlipY()) armsprite.flip(false, true);
		} else {
			if (!armsprite.isFlipY()) armsprite.flip(false, true);	
			if (aimladdersprite.isFlipX())
				aimladdersprite.flip(true, false);
		}
		
		Vector2 tmpAimVec = new Vector2(targetScreenVec.x, targetScreenVec.y);
		if (isAI){
			aimAngle = aimAtPlayer;	
		}else {
		aimAngle = tmpAimVec.angle();
		}
		sprite.setSize(1f, 1f);
		sprite.setOrigin(sprite.getWidth() / 2, 0);
		sprite.setPosition(worldpos.x - 0.5f, worldpos.y);
		
		armsprite.setRotation(aimAngle-180);
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
		
		body.setTransform(worldpos.x, worldpos.y+1, 0f);
		physics.doPhysics(this);
		
		
		
		
	}

	
	public void update(boolean isWorldCoord, Camera camera, boolean shoot) {
		boolean facingtarget = true;
		boolean flip = false;
		boolean aimless = false;

		float inx = 0;
		float iny = 0;
		
		aimless = true;
		isWorldCoord = false;
	
		update(inx, iny, isWorldCoord,
				camera, shoot);
		//float rand = (float)Math.random() * weapon.accuracy -(weapon.accuracy/2);
	}
	
	public Vector2 p1 = new Vector2(), p2 = new Vector2(), collision = new Vector2(), normal = new Vector2();
	
	public void shoot(){
		if (isAlive && weapon.shoot()){
			
			
			RayCastCallback callback = new RayCastCallback() {

				@Override
				public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
					collision.set(point);
					Actor.this.normal.set(normal).add(point);
					return 0;
				}
			
			};
			
			
			
			
		for (int i = 0; i < weapon.shots[weapon.weaponid]; i++){
			float scatterbullets = (float) (Math.random() * weapon.accuracyscatter[weapon.weaponid] -(weapon.accuracyscatter[weapon.weaponid]/2));  
			p1.x = worldpos.x;
			p1.y = worldpos.y+1.75f;
			//float newy = p1.y + 1;
			
			float ifCrouch = 0;
			
			if (isCrouching){
				ifCrouch = -0.7f;
			}
			
			if (activeBullet == 100) {
				activeBullet = 0;
			}
			
		/*	bulletArray[activeBullet].isAlive = true;
			bulletArray[activeBullet].isPlayers = true;
			Vector3 tmpVec3 = new Vector3(worldpos.x,
					worldpos.y+ifCrouch, 0);
			bulletArray[activeBullet].worldpos.y = worldpos.y;
			bulletArray[activeBullet].worldpos.x = worldpos.x;
			bulletArray[activeBullet].fire(tmpVec3.x, tmpVec3.y,
					aimAngle + scatterbullets, weapon.bulletspeed[weapon.weaponid]);
			p2.x = bulletArray[activeBullet].worldpos.x + (float) (12 * Math.cos(Math.toRadians(aimAngle )));
			p2.y =  bulletArray[activeBullet].worldpos.y + (float) (12 * Math.sin(Math.toRadians(aimAngle + scatterbullets)));*/
	
			world.rayCast(callback, p1,p2);
			
			activeBullet++;
			}
		}
	}
	
	public void takeDamage(int i) {
		//health -= i;		
	}
	
/*	public void drawBullets(Camera camera, SpriteBatch batch){
		for (int i = 0; i < 100; i++) {
			if (bulletArray[i].isAlive) {
				bulletArray[i].update(camera);
				Vector3 tmpVec = new Vector3(bulletArray[i].worldpos.x,
						bulletArray[i].worldpos.y, 0);
				bulletArray[i].sprite.setPosition(tmpVec.x, tmpVec.y);
				bulletArray[i].sprite.draw(batch);
			}
		}
	}*/
	
	
	/*public void checkCollision(Bullet zbullet) {
		// TODO Auto-generated method stub
		
	}*/
	
	
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

}
