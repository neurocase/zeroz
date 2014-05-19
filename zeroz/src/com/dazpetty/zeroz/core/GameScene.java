package com.dazpetty.zeroz.core;

import java.util.LinkedHashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dazpetty.zeroz.entities.Actor;
import com.dazpetty.zeroz.entities.CopterBoss;
import com.dazpetty.zeroz.entities.Destroyable;
import com.dazpetty.zeroz.entities.Door;
import com.dazpetty.zeroz.entities.Drone;
import com.dazpetty.zeroz.entities.EnemySpawner;
import com.dazpetty.zeroz.entities.HUDTarget;
import com.dazpetty.zeroz.entities.HumanSprite;
import com.dazpetty.zeroz.entities.Item;
import com.dazpetty.zeroz.entities.Weapon;
import com.dazpetty.zeroz.managers.ActorManager;
import com.dazpetty.zeroz.managers.Assets;
import com.dazpetty.zeroz.managers.DazContactListener;
import com.dazpetty.zeroz.managers.InputHandler;
import com.dazpetty.zeroz.managers.OrthoCamController;
import com.dazpetty.zeroz.managers.ParralaxCamera;
import com.dazpetty.zeroz.managers.ProjectileManager;
import com.dazpetty.zeroz.managers.LevelManager;

public class GameScene implements Screen {

	/*
	 * ORDINARY VARIABLES
	 */
	static final float BOX_STEP = 1 / 60f;
	static final int BOX_VELOCITY_ITERATIONS = 6;
	static final int BOX_POSITION_ITERATIONS = 2;
	static final float WORLD_TO_BOX = 0.01f;
	static final float BOX_TO_WORLD = 100f;
	
	public int TOTAL_ITEMS = 0;
	public int TOTAL_DOORS = 0;
	public int TOTAL_DESTROYABLES = 0;
	int bulletsadded = 0;
	
	private int activeBullet = 0;

	public int activeproj = 0;
	private float addextracamx = 0;
	float stateTime;
	private boolean playerShoot = false;
	private boolean giveWorldPos = true;
	private boolean showDebug = false;
	private boolean isLevelScrolling = false;
	private boolean isBossLevel = false;
	boolean initbod = false;
	public int enemycount = 1;
	public int dronecount = 1;
	public long lasttimespawn = System.currentTimeMillis();
	/*
	 * LIBGDX && BOX2D TYPES
	 */
	private static final String TAG = ZerozGame.class.getName();
	private OrthographicCamera camera;
	private CameraInputController cameraController;
	public ParralaxCamera pcamera;
	public OrthoCamController pcamcontroller;
	ShapeRenderer sr = new ShapeRenderer();
	World world = new World(new Vector2(0, -10), true);
	Box2DDebugRenderer debugRenderer;
	/*
	 * TEXTURES AND SPRITES
	 */
	private Texture bgCityBgTex;
	private Texture targettex;
	private TextureRegion bgCityBgTexReg;
	private Sprite targetsprite;
	private Sprite playersprite;
	private Sprite bgCityBackSprite;
	public Texture dirbuttonstex;
	public Sprite dirbuttonssprite;
	public Texture jumpbuttontex;
	public Sprite jumpbuttonsprite;
	public Texture shootbuttontex;
	public Sprite shootbuttonsprite;
	Texture grentex;
	private SpriteBatch batch;
	private SpriteBatch bgbatch;
	
	
	private Texture levelCompleteTex;
	private TextureRegion levelCompleteTexReg;
	private Sprite levelcompletesprite;
	/*
	 * ARRAYS
	 */

	/*
	 * VECTORS
	 */
	Vector3 aimlessVec = new Vector3(0, 0, 1);

	private Vector2 enemystart = new Vector2(30, 7);
	private Vector2 enemystart2 = new Vector2(60, 9);
	
	Vector2 levelcompletepos = new Vector2(0,0);
	Vector3 touchPos = new Vector3(0, 0, 0);
	Vector3 zeroVector3 = new Vector3(0, 0, 0);
	public Vector target = new Vector2(0, 0);
	private Vector2 playerpos = new Vector2(0, 0);
	private Vector3 screenPosZero = new Vector3(0, 0, 0);
	Vector3 camVector = new Vector3(0, 0, 0);
	

	/*
	 * TILED MAP TYPES
	 */
	public TiledMapTileLayer collisionLayer;
	public TiledMapTileLayer miscLayer;
	public TiledMap map;
	private TiledMapRenderer renderer;
	/*
	 * MANAGERS, HANDLERS, COLLECTIONS, CUSTOM TYPES
	 */
	//public HUDTarget hudtarget;

	public ActorManager actorMan;
	public WorldLogic worldLogic;
	final ZerozGame game;


	//Vector2 enemyspawner[] = new Vector2[ENEMY_SPAWN_LIMIT];

	
	

	public LevelManager tm;
	public Assets Assets;


	
	
	float viewwidth = Gdx.graphics.getWidth();
	float viewheight = Gdx.graphics.getHeight();
	/*
	 * FUNCTIONS
	 */

	
	public GameScene(final ZerozGame gam) {

		this.game = gam;
		Assets.load();
		Assets.manager.finishLoading();
		/*
		 * SETUP SPRITES AND TEXTURES
		 */
		dirbuttonssprite = new Sprite(Assets.manager.get(Assets.dirbuttons,
				Texture.class));
		dirbuttonssprite.setSize(7.5f, 1f);
		dirbuttonssprite.setOrigin(0, 0);
		dirbuttonssprite.setPosition(30, 30);

		jumpbuttonsprite = new Sprite(Assets.manager.get(Assets.jumpbutton,
				Texture.class));
		jumpbuttonsprite.setSize(1f, 1f);
		jumpbuttonsprite.setOrigin(0, 0);

		shootbuttonsprite = new Sprite(Assets.manager.get(Assets.shootbutton,
				Texture.class));
		shootbuttonsprite.setSize(1f, 1f);
		shootbuttonsprite.setOrigin(0, 0);
		shootbuttonsprite.setPosition(0f, 0f);

		levelCompleteTex = new Texture(
				Gdx.files.internal("data/gfx/hud/levelcomplete.png"));
		levelCompleteTexReg = new TextureRegion(levelCompleteTex, 0, 0, 512, 256);
		levelcompletesprite = new Sprite(levelCompleteTexReg);
		levelcompletesprite.setSize(1f, 1f);
		levelcompletesprite.setOrigin(0, 0);
		
		batch = new SpriteBatch();
		bgCityBgTex = new Texture(
				Gdx.files.internal("data/gfx/background/cityp1.png"));
		bgCityBgTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bgCityBgTexReg = new TextureRegion(bgCityBgTex, 0, 0, 1024, 512);
		targettex = new Texture(Gdx.files.internal("data/gfx/target.png"));
		targettex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion targettexreg = new TextureRegion(targettex, 0, 0, 128,
				128);
		bgCityBackSprite = new Sprite(bgCityBgTexReg);
		bgCityBackSprite.setSize(51.2f, 102.4f);
		bgCityBackSprite.setOrigin(0, 0);
		bgCityBackSprite.setPosition(0f, 0f);
		targetsprite = new Sprite(targettexreg);
		targetsprite.setSize(2f, 2f);
		targetsprite.setOrigin(0, 0);
		targetsprite.setPosition(0f, 0f);
		grentex = new Texture(Gdx.files.internal("data/gfx/reddot.png"));
		grentex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion grentexreg = new TextureRegion(grentex, 0, 0, 128, 128);
		bgbatch = new SpriteBatch();
		
		
		
		LevelManager tm = new LevelManager(game.level);
		
		renderer = new OrthogonalTiledMapRenderer(tm.map, 1f / 32f);
		camera = new OrthographicCamera(1, viewheight / viewwidth);
		camera.setToOrtho(false, (viewwidth / viewheight) * 10, 10);
		camera.update();
		
		actorMan = new ActorManager(camera, world, tm);
		worldLogic = new WorldLogic(camera, actorMan, world, tm);
		
		pcamera = new ParralaxCamera(viewheight * 2f, viewwidth * 0.5f);
		pcamcontroller = new OrthoCamController(pcamera);
		Gdx.input.setInputProcessor(pcamcontroller);
		//}
		
		viewwidth = Gdx.graphics.getWidth();
		viewheight = Gdx.graphics.getHeight();
		
	
	}

	private void wtfc(){
		System.out.println("WHAT THE FUCK CUNT!");
		System.out.println("WHAT THE FUCK CUNT!");
		System.out.println("WHAT THE FUCK CUNT!");
	}
	
	

	public void displayControls() {
		viewwidth = Gdx.graphics.getWidth();
		viewheight = Gdx.graphics.getHeight();
	}

	public void showDebugInfo(boolean show) {
		game.batch.begin();
		String info2 = "";
		if (debugOn) {
			info2 = " Health:" + actorMan.zplayer.health + " actorMan.zplayer X:"
					+ actorMan.zplayer.worldpos.x + ", Y:" + actorMan.zplayer.worldpos.y
					+ " state:" + actorMan.zplayer.state;
		} else {
			info2 = " Health:" + actorMan.zplayer.health;
			if (actorMan.zplayer.health <= 0) {
				info2 = "GAME OVER: YOU DIED";
			}
		}
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),
					0);
			String info4 = "ScreenTouch X:" + touchPos.x + " Y:" + touchPos.y;
			camera.unproject(touchPos);
			String info5 = "ScreenTouch X:" + touchPos.x + " Y:" + touchPos.y;
			game.font.draw(game.batch, info4, 20, 300);
			game.font.draw(game.batch, info5, 20, 280);
		}
		game.font.draw(game.batch, info2, 20, 340);
		game.batch.end();
	}

	public void box2DRender(boolean debug) {
		if (debug) {
			debugRenderer.render(world, camera.combined);
		}
		world.step(1 / 45f, 6, 2);
	}

	public void drawProjectile(ProjectileManager projMan) {
		for (int i = 0; i < actorMan.PROJECTILE_LIMIT; i++) {
			if (projMan.proj[i] != null) {
				if (projMan.proj[i].isAlive) {
					if (projMan.proj[i].isAlive) {
						Vector2 tmpprojpos = projMan.proj[i].body.getPosition();
						projMan.proj[i].projsprite.setPosition(
								tmpprojpos.x - 0.25f, tmpprojpos.y - 0.25f);
						projMan.proj[i].projsprite.draw(batch);
					}
				} else {
					if (!projMan.proj[i].isDead) {
						projMan.KillProjectile(i);
					}
				}
			}
		}
	}

	public boolean debugOn = false;
	//public boolean levelComplete = false;

	@Override
	public void render(float delta) {
		
		actorMan.zplayer.update(worldLogic.inputHandler.giveWorldPos, camera, playerShoot);
		tm = actorMan.zplayer.tm;
		
		//camera = worldLogic.camera;
		
	//	}
		/*
		 * SETUP PARRALAX CAMERA
		 */
		boolean updatePCamera = false;
		pcamera.position.x = camera.position.x;
		pcamera.position.y = camera.position.y;
		if (pcamera.position.x < -1024 + pcamera.viewportWidth / 2) {
			pcamera.position.x = -1024 + (int) (pcamera.viewportWidth / 2);
			updatePCamera = true;
		}

		if (pcamera.position.x > 1024 - pcamera.viewportWidth / 2) {
			pcamera.position.x = 1024 - (int) (pcamera.viewportWidth / 2);
			updatePCamera = true;
		}

		if (pcamera.position.y < 0) {
			pcamera.position.y = 0;
			updatePCamera = true;
		}
		// arbitrary height of scene
		if (pcamera.position.y > 400 - pcamera.viewportHeight / 2) {
			pcamera.position.y = 400 - (int) (pcamera.viewportHeight / 2);
			updatePCamera = true;
		}
		/*
		 * CLEAR BUFFER
		 */
		Gdx.gl.glClearColor(0.4f, 0.25f, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		/*
		 * BEGIN BACKGROUND RENDER
		 */
		bgbatch.setProjectionMatrix(pcamera.calculateParallaxMatrix(2, 4));
		bgbatch.begin();
		bgbatch.draw(bgCityBgTexReg,
				-(int) (bgCityBgTexReg.getRegionWidth() / 2)
						- (int) (bgCityBgTexReg.getRegionWidth()),
				-(int) (bgCityBgTexReg.getRegionHeight() / 2));
		bgbatch.draw(bgCityBgTexReg,
				-(int) (bgCityBgTexReg.getRegionWidth() / 2),
				-(int) (bgCityBgTexReg.getRegionHeight() / 2));
		bgbatch.draw(bgCityBgTexReg,
				-(int) (bgCityBgTexReg.getRegionWidth() / 2)
						+ (int) (bgCityBgTexReg.getRegionWidth()),
				-(int) (bgCityBgTexReg.getRegionHeight() / 2));
		bgbatch.end();

		batch.setProjectionMatrix(camera.combined);
		sr.setProjectionMatrix(camera.combined);

		worldLogic.inputHandler.checkKeyboard();
		worldLogic.inputHandler.checkTouch();
		/*
		 * ^^^^^^^^^^^^^^^^^^^^^ END BACKGROUND RENDER
		 */
		
		/*
		 * BEGIN CORE SPRITE BATCH RENDER LOOP
		 */
		for (int i = 0; i < actorMan.DRONE_LIMIT; i++) {
			if (actorMan.drone[i] != null && actorMan.drone[i].isAlive) {
				actorMan.drone[i].update(actorMan.zplayer);
			}
		}
		renderer.setView(camera);
		renderer.render();
		
		batch.begin();
		
		if (isBossLevel){
			actorMan.copterBoss.bossSprite.draw(batch);
			actorMan.copterBoss.update();
			
			for (int i = 0; i < actorMan.copterBoss.copterTurret.length; i++){
				if (actorMan.copterBoss.copterTurret[i] != null && actorMan.copterBoss.copterTurret[i].isAlive){
					actorMan.copterBoss.copterTurret[i].baseSprite.draw(batch);
					actorMan.copterBoss.copterTurret[i].barrelSprite.draw(batch);
				}
			}
		}
		
		for (int i = 0; i < actorMan.DRONE_LIMIT; i++) {
			if (actorMan.drone[i] != null && actorMan.drone[i].isAlive) {
				actorMan.drone[i].sprite.draw(batch);
			}
		}
		// drone[0].sprite.setPosition(actorMan.zplayer.screenpos.x,
		// actorMan.zplayer.screenpos.y);
		dirbuttonssprite.setPosition(camera.position.x - 8,
				camera.position.y - 5);
		dirbuttonssprite.draw(batch);
		Vector3 tmpVec3 = new Vector3((worldLogic.inputHandler.getXInputPosition("jump")),
				0, 0);
		camera.unproject(tmpVec3);
		tmpVec3.y = camera.position.y - 5;
		jumpbuttonsprite.setPosition(tmpVec3.x, tmpVec3.y);

		tmpVec3.x = (worldLogic.inputHandler.getXInputPosition("shoot"));
		camera.unproject(tmpVec3);
		tmpVec3.y = camera.position.y - 5;
		shootbuttonsprite.setPosition(tmpVec3.x, tmpVec3.y);
		shootbuttonsprite.draw(batch);

		jumpbuttonsprite.draw(batch);

		batch.setColor(Color.RED);
		for (int i = 0; i < actorMan.ENEMY_LIMIT; i++) {
			if (actorMan.zenemy[i] != null && actorMan.zenemy[i].sprite != null
					&& !actorMan.zenemy[i].isDisposed) {
				actorMan.zenemy[i].sprite.draw(batch);
				if (!actorMan.zenemy[i].isOnLadder && actorMan.zenemy[i].isAlive) {
					actorMan.zenemy[i].armsprite.draw(batch);
				}
				if (actorMan.zenemy[i].isOnLadder && actorMan.zenemy[i].isShooting
						&& actorMan.zenemy[i].isAlive) {
					actorMan.zenemy[i].armsprite.draw(batch);
				}
			}
		}
		actorMan.zplayer.sprite.draw(batch);
		/*
		 * NEED TO FIX ARMSPRITE LOADING/NOT LOADING HERE
		 */
		if (!actorMan.zplayer.isOnLadder && actorMan.zplayer.isAlive) {
			actorMan.zplayer.armsprite.draw(batch);
		}
		if (actorMan.zplayer.isOnLadder && actorMan.zplayer.isShooting && actorMan.zplayer.isAlive) {
			actorMan.zplayer.armsprite.draw(batch);
		}
		float extracamx = 0;
		if (actorMan.zplayer.aimingdirection == "left") {
			if (addextracamx < viewwidth / 2) {
				addextracamx += 7;
				if (addextracamx < 0) {
					addextracamx += 14;
				}
				if (actorMan.zplayer.velocity.x > 0) {
					addextracamx += 7;
				}
			}
		} else if (actorMan.zplayer.aimingdirection == "right") {
			if (addextracamx > -viewwidth / 2) {
				addextracamx -= 7;
				if (addextracamx > 0) {
					addextracamx -= 14;
				}
				if (actorMan.zplayer.velocity.x > 0) {
					addextracamx -= 7;
				}
			}
		}
		if (!isLevelScrolling){
		camera.position.set(actorMan.zplayer.worldpos.x + addextracamx / 200,
				actorMan.zplayer.worldpos.y + 1.5f, 0);
		}
		for (int i = 0; i < actorMan.DESTROYABLE_LIMIT; i++) {
			if (actorMan.destroyable[i] != null) {
				actorMan.destroyable[i].sprite.draw(batch);
			}
		}
		for (int i = 0; i < actorMan.DOOR_LIMIT; i++) {
			if (actorMan.door[i] != null) {
				actorMan.door[i].sprite.draw(batch);
			}
		}
		for (int i = 0; i < actorMan.ITEM_LIMIT; i++) {
			if (actorMan.item[i] != null) {
				if (actorMan.item[i].isAlive) {
					actorMan.item[i].sprite.draw(batch);
				}
			}
		}
		/*
		 * Draw
		 */
		drawProjectile(actorMan.projMan);
		drawProjectile(actorMan.aiProjMan);

		if (actorMan.hudtarget.canDraw()){
			actorMan.hudtarget.sprite.draw(batch);
		}
		//boolean levelcomplete = false;
		if (levelcompletepos.x != 0 && levelcompletepos.y != 0){
			if (Math.abs(levelcompletepos.x - actorMan.zplayer.worldpos.x) < 3 && Math.abs(levelcompletepos.y - actorMan.zplayer.worldpos.y) < 3 ){
				tm.isLevelComplete = true;
				levelcompletesprite.setPosition(camera.position.x-5, camera.position.y-2);
				levelcompletesprite.setSize(12f, 6f);
			}
		}
		

		if (tm.isLevelComplete) levelcompletesprite.draw(batch);	

		batch.end();
		/*
		 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ END CORE SPRITE BATCH RENDER LOOP
		 */
		worldLogic.update();
		displayControls();
		showDebugInfo(showDebug);
		box2DRender(debugOn);
		camera.update();
		
		if (Gdx.input.isKeyPressed(Keys.R)) {
			game.setScreen(new MainMenu(game));
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			debugOn = true;
		}
		if (Gdx.input.isKeyPressed(Keys.C)) {
			tm.isLevelComplete = true;
		}
		if (Gdx.input.isTouched()) {
			if (!actorMan.zplayer.isAlive) {
				game.setScreen(new MainMenu(game));
			}
			if (tm.isLevelComplete){
				game.nextLevel();
				game.setScreen(new GameScene(game));
			}
		}
		// BOX 2D DEBUG RENDERER
		debugRenderer = new Box2DDebugRenderer();

		//drone[0] = new Drone(5, 5, world, 0, camera);
		
		

	}
	


	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

		targettex.dispose();
		batch.dispose();
		actorMan.dispose();
		tm.map.dispose();
		sr.dispose();

	}
	
	
	
	
	
}
/*
 * TODO:
 * 
 *   * IMPLEMENT WEAPON CODE
 *   * CREATE A DOOR, WHICH OPENS AFTER A SPAWNER HAS DEPLETED ITS ENEMYSPAWN COUNT, AND THOSE
 *     ENEMIES ARE DEAD
 * 
 *  ---------- KNOWN BUGS ---------- 1. Arm not appearing on ladder, sprite
 * not flipping on ladder 2. Ladder movement not that awesome 3. There is a bug
 * where sometimes a health pickup will not "pickup". It does not seem to be
 * registering the collision between itself and the player, shooting the item
 * seems to increase the chance of this bug occurring. In all cases it seems if
 * the player returns to the item later, he is able to pick it up. 4. Sometimes
 * after comming off a ladder a player seems to get stuck in a wierd sliding
 * state, and cannot receive input(shoot, jump, left, right etc).
 * 
 * ------------------- NOT YET IMPLIMENTED ------------------- 1. Enemies:
 * Flying Drones, Melee Fighters, Shooter, Boss Helicopter 2. Weapons: Items,
 * Graphics, etc. 3. Level: Changing level, winning conditions 4 Controls: Tap
 * on screen does power weapon (heavy mg, rocket, grenade) , tap on shoot button
 * shoots secondary weapon (pistol, uzi). 5. make is so ladder+ground areas act
 * as platforms and that platform ignored if on ladder
 * 
 * -------- TUTORIAL -------- - introduce jumping, crouching, jumping down a
 * platform, ladders - introduce quick shoot weapon, special weapon and opening
 * doors by destroying objects
 */
