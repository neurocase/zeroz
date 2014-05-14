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
import com.dazpetty.zeroz.entities.Destroyable;
import com.dazpetty.zeroz.entities.Door;
import com.dazpetty.zeroz.entities.Drone;
import com.dazpetty.zeroz.entities.EnemySpawner;
import com.dazpetty.zeroz.entities.HUDTarget;
import com.dazpetty.zeroz.entities.HumanSprite;
import com.dazpetty.zeroz.entities.Item;
import com.dazpetty.zeroz.managers.Assets;
import com.dazpetty.zeroz.managers.DazContactListener;
import com.dazpetty.zeroz.managers.InputHandler;
import com.dazpetty.zeroz.managers.OrthoCamController;
import com.dazpetty.zeroz.managers.ParralaxCamera;
import com.dazpetty.zeroz.managers.ProjectileManager;
import com.dazpetty.zeroz.managers.TiledLayerManager;

public class GameScene implements Screen {

	/*
	 * ORDINARY VARIABLES
	 */
	static final float BOX_STEP = 1 / 60f;
	static final int BOX_VELOCITY_ITERATIONS = 6;
	static final int BOX_POSITION_ITERATIONS = 2;
	static final float WORLD_TO_BOX = 0.01f;
	static final float BOX_TO_WORLD = 100f;
	public final int PROJECTILE_LIMIT = 20;
	public final int ENEMY_LIMIT = 10;
	public final int ENEMY_SPAWN_LIMIT = 20;
	public final int DESTROYABLE_LIMIT = 10;
	public final int DOOR_LIMIT = 10;
	public final int DRONE_LIMIT = ENEMY_LIMIT;
	public final int ITEM_LIMIT = 20;
	public int TOTAL_ITEMS = 0;
	public int TOTAL_DOORS = 0;
	public int TOTAL_DESTROYABLES = 0;
	int bulletsadded = 0;
	float viewwidth = 0;
	float viewheight = 0;
	private int activeBullet = 0;
	public int enemyspawners = 0;
	public int activeproj = 0;
	private float addextracamx = 0;
	float stateTime;
	private boolean playerShoot = false;
	private boolean giveWorldPos = true;
	private boolean showDebug = false;
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
	public Actor[] zenemy = new Actor[ENEMY_LIMIT];
	public Destroyable[] destroyable = new Destroyable[DESTROYABLE_LIMIT];
	public Door[] door = new Door[DOOR_LIMIT];
	public Item[] item = new Item[ITEM_LIMIT];
	public Drone[] drone = new Drone[DRONE_LIMIT];
	/*
	 * VECTORS
	 */
	Vector3 aimlessVec = new Vector3(0, 0, 1);
	private Vector2 playerstart = new Vector2(0, 0);
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
	public HUDTarget hudtarget;
	private Actor zplayer;
	final ZerozGame game;
	public ProjectileManager projMan = new ProjectileManager(PROJECTILE_LIMIT);
	public ProjectileManager aiProjMan = new ProjectileManager(PROJECTILE_LIMIT);
	
	//Vector2 enemyspawner[] = new Vector2[ENEMY_SPAWN_LIMIT];
	EnemySpawner enemyspawner[] = new EnemySpawner[ENEMY_SPAWN_LIMIT];
	
	private DazContactListener cl;
	public InputHandler inputHandler = new InputHandler();
	public TiledLayerManager tm;
	public Assets Assets;
	private HumanSprite humanSprite = new HumanSprite();

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
		/*
		 * SETUP CAMERA AND RENDERER
		 */
		// Tiled layer manager for cell (not box2d) based collision.
		TiledLayerManager tm = new TiledLayerManager(game.level);
		viewwidth = Gdx.graphics.getWidth();
		viewheight = Gdx.graphics.getHeight();
		renderer = new OrthogonalTiledMapRenderer(tm.map, 1f / 32f);
		camera = new OrthographicCamera(1, viewheight / viewwidth);
		camera.setToOrtho(false, (viewwidth / viewheight) * 10, 10);
		camera.update();
		pcamera = new ParralaxCamera(viewheight * 2f, viewwidth * 0.5f);
		pcamcontroller = new OrthoCamController(pcamera);
		Gdx.input.setInputProcessor(pcamcontroller);
		/*
		 * SETUP WORLD AND COLLISIONS
		 */
		cl = new DazContactListener();
		world.setContactListener(cl);
		for (int h = 0; h < tm.collisionLayer.getHeight(); h++) {
			for (int w = 0; w < tm.collisionLayer.getWidth(); w++) {

				if (tm.isCellBlocked(w, h, false)) {

					int c = 0;
					while (tm.isCellBlocked(w + c, h, false)) {
						c++;
					}
					BodyDef groundBodyDef = new BodyDef();
					groundBodyDef.position.set(new Vector2(w + c * 0.5f,
							h + 0.5f));
					Body groundBody = world.createBody(groundBodyDef);
					PolygonShape groundBox = new PolygonShape();
					groundBox.setAsBox(c * 0.5f, 0.5f);
					groundBody.createFixture(groundBox, 0.0f);
					FixtureDef fixtureDef = new FixtureDef();
					fixtureDef.shape = groundBox;
					fixtureDef.filter.categoryBits = 2;
					Fixture gfix = groundBody.createFixture(groundBox, 0.0f);
					gfix.setUserData("ground");
					for (int d = 0; d < c - 1; d++) {
						w++;
					}
				}
				if (tm.isCellPlatform(w, h)) {
					int c = 0;
					while (tm.isCellPlatform(w + c, h)) {
						c++;
					}
					BodyDef groundBodyDef = new BodyDef();
					groundBodyDef.position.set(new Vector2(w + c * 0.5f,
							h + 0.75f));
					Body groundBody = world.createBody(groundBodyDef);
					PolygonShape groundBox = new PolygonShape();
					groundBox.setAsBox(c * 0.5f, 0.2f);
					groundBody.createFixture(groundBox, 0.0f);
					FixtureDef fixtureDef = new FixtureDef();
					fixtureDef.shape = groundBox;
					fixtureDef.filter.categoryBits = 1;
					Fixture pfix = groundBody.createFixture(fixtureDef);
					pfix.setUserData("platform");
					for (int d = 0; d < c - 1; d++) {
						w++;
					}
				}
				if (tm.isCellEnemySpawn(w, h)) {
					String type = (String) tm.getEnemyType(w,h);
					enemyspawner[enemyspawners] = new EnemySpawner(w, h, type);
					System.out.println("Spawner Created of Type:" + type + "at" + w + "," + h);
					enemyspawners++;
				}
				if (tm.isCellDestroyable(w, h)) {
					int value = tm.getCellValue(w, h);
					if (TOTAL_DESTROYABLES < DESTROYABLE_LIMIT) {
						destroyable[value] = new Destroyable(w, h, value, world);
						System.out.println("DESTROYABLE ADDED: "
								+ TOTAL_DESTROYABLES);
						TOTAL_DESTROYABLES++;

					}
				}
				if (tm.isCellDoor(w, h)) {
					if (TOTAL_DOORS < DOOR_LIMIT) {
						int value = tm.getCellValue(w, h);
						door[TOTAL_DOORS] = new Door(w, h, value, world);
						TOTAL_DOORS++;
						System.out.println("DOOR ADDED: " + TOTAL_DOORS);
					}
				}
				if (tm.isCellItem(w, h)) {
					if (TOTAL_ITEMS < ITEM_LIMIT) {
						String value = tm.getItemValue(w, h);
						item[TOTAL_ITEMS] = new Item(w, h, TOTAL_ITEMS, world);
						TOTAL_ITEMS++;
						System.out.println(value + " ITEM ADDED: "
								+ TOTAL_ITEMS + "at:" + w + "," + h);
					}
				}
				if (tm.isCellLevelComplete(w, h)) {
					System.out.println("LEVEL COMPLETE AT: x:" + w + "y:" + h);
					levelcompletepos.x = w;
					levelcompletepos.y = h;
				}
				if (tm.isCellPlayerStart(w, h)) {
					System.out.println("PlayerStart at: x" + w + "y:" + h);
					playerstart.x = w;
					playerstart.y = h;
				}
			}
		}
		// CREATE PLAYER
		zplayer = new Actor(camera, world, false, tm, playerstart, 0,
				humanSprite, projMan, "player");
		// INPUTHANDLER
		inputHandler.LoadInputHandler(viewwidth, viewheight, camera, zplayer);
		// BOX 2D DEBUG RENDERER
		debugRenderer = new Box2DDebugRenderer();

		//drone[0] = new Drone(5, 5, world, 0, camera);
		
		hudtarget = new HUDTarget();
	}

	private void wtfc(){
		System.out.println("WHAT THE FUCK CUNT!");
		System.out.println("WHAT THE FUCK CUNT!");
		System.out.println("WHAT THE FUCK CUNT!");
	}
	
	private void createActor(int s, EnemySpawner es) {
		
	
		Vector2 startpos = new Vector2(es.worldpos.x, es.worldpos.y);
		long timenow = System.currentTimeMillis();
		
		if (s == 1) {
			zplayer = new Actor(camera, world, false, tm, startpos, -1,
					humanSprite, projMan, es.enemyType);
		}
		if (s == 2) {
		//	if (es.enemyType == null){
				
			//}
		//	System.out.println("Attempting to create " + es.enemyType);
			/*String enemyType = (String)es.enemyType;
			if (!enemyType.equals("footsoldier")){
				System.out.println(enemyType + " is not footsoldier");
			}*/
			
			if (es.enemyType.equals("footsoldier")){
				//wtfc();
			//	System.out.println();
				
				//long a = timenow - es.lasttimespawn;
				if (timenow - es.lasttimespawn > (50 * 50)
						&& (zenemy[enemycount] == null
								|| zenemy[enemycount].isAlive == false || zenemy[enemycount].isDisposed)) {
					if (zenemy[enemycount] != null
							&& zenemy[enemycount].body != null) {
						System.out.println("destroying enemy body" + enemycount);
						zenemy[enemycount].reUseActor(startpos);
						System.out.println("Spawning Renewing Enemy:" + enemycount
								+ " X:" + startpos.x + "Y" + startpos.y);
					} else {
						System.out.println("Spawning New Enemy:" + es.enemyType + "," + enemycount
								+ " X:" + startpos.x + "Y" + startpos.y);
						/*
						 * I HAVE NO IDEA WHY I NEED TO PASS zplayer.tm INSTEAD OF
						 * tm, BUT IT WONT WORK OTHERWISE.
						 */
						zenemy[enemycount] = new Actor(camera, world, true,
								zplayer.tm, startpos, enemycount, humanSprite,
								aiProjMan, es.enemyType);
					}
					es.lasttimespawn = System.currentTimeMillis();
					enemycount++;
					if (enemycount == ENEMY_LIMIT) {
						enemycount = 0;
					}
				}
			}else if (es.enemyType.equals("paratrooper")){
				
					//long a = timenow - es.lasttimespawn;
					if (timenow - es.lasttimespawn > (50 * 50)
							&& (zenemy[enemycount] == null
									|| zenemy[enemycount].isAlive == false || zenemy[enemycount].isDisposed)) {
						if (zenemy[enemycount] != null
								&& zenemy[enemycount].body != null) {
							System.out.println("destroying enemy body" + enemycount);
							zenemy[enemycount].reUseActor(startpos);
							System.out.println("Spawning Renewing Enemy:" + enemycount
									+ " X:" + startpos.x + "Y" + startpos.y);
						} else {
							System.out.println("Spawning New Enemy:" + es.enemyType + "," + enemycount
									+ " X:" + startpos.x + "Y" + startpos.y);
							/*
							 * I HAVE NO IDEA WHY I NEED TO PASS zplayer.tm INSTEAD OF
							 * tm, BUT IT WONT WORK OTHERWISE.
							 */
							zenemy[enemycount] = new Actor(camera, world, true,
									zplayer.tm, startpos, enemycount, humanSprite,
									aiProjMan, es.enemyType);
						}
						es.lasttimespawn = System.currentTimeMillis();
						enemycount++;
						if (enemycount == ENEMY_LIMIT) {
							enemycount = 0;
						}
					}
				
			}else if (es.enemyType.equals("drone")){
				
				//long a = timenow - es.lasttimespawn;
				if (timenow - es.lasttimespawn > (50 * 50)
						&& (drone[dronecount] == null
								|| drone[dronecount].isAlive == false )){//|| drone[dronecount].isDisposed)) {
					if (drone[dronecount] != null
							&& drone[dronecount].body != null) {
						System.out.println("destroying enemy body" + enemycount);
						drone[dronecount].reUseDrone(startpos);
						System.out.println("Spawning Renewing Enemy:" + enemycount
								+ " X:" + startpos.x + "Y" + startpos.y);
					} else {
						System.out.println("Spawning New Enemy:" + es.enemyType + "," + enemycount
								+ " X:" + startpos.x + "Y" + startpos.y);
						/*
						 * I HAVE NO IDEA WHY I NEED TO PASS zplayer.tm INSTEAD OF
						 * tm, BUT IT WONT WORK OTHERWISE.
						 */
						
						drone[dronecount] = new Drone(startpos.x, startpos.y, world, dronecount, camera);
					}
					es.lasttimespawn = System.currentTimeMillis();
					dronecount++;
					if (dronecount == DRONE_LIMIT) {
						dronecount = 0;
					}
				}
			
		}
		}
	}

	public void displayControls() {
		viewwidth = Gdx.graphics.getWidth();
		viewheight = Gdx.graphics.getHeight();
	}

	public void showDebugInfo(boolean show) {
		game.batch.begin();
		String info2 = "";
		if (debugOn) {
			info2 = " Health:" + zplayer.health + " ZPLAYER X:"
					+ zplayer.worldpos.x + ", Y:" + zplayer.worldpos.y
					+ " state:" + zplayer.state;
		} else {
			info2 = " Health:" + zplayer.health;
			if (zplayer.health <= 0) {
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
		for (int i = 0; i < PROJECTILE_LIMIT; i++) {
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
	public boolean levelComplete = false;

	@Override
	public void render(float delta) {
		
		if (Gdx.input.isKeyPressed(Keys.R)) {
			game.setScreen(new MainMenu(game));
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			debugOn = true;
		}
		/*
		 * UPDATE PLAYER
		 */

		zplayer.update(inputHandler.giveWorldPos, camera, playerShoot);
		/*
		 * SPAWN ENEMIES AT SPAWNPOINT NEAR PLAYER
		 */
	//	if (pollCheck(11)){
			for (int i = 0; i < enemyspawners; i++) {
				if (Math.abs((double) (enemyspawner[i].worldpos.x - zplayer.worldpos.x)) < 20) {
					if (enemyspawner[i] != null ){//&& enemyspawner[i].enemyType == "footsoldier") {
						createActor(2, enemyspawner[i]);
					} else {
						System.out.println("ERROR: There are no enemy spawners");
					}
				}
			}
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

		inputHandler.checkKeyboard();
		inputHandler.checkTouch();
		/*
		 * ^^^^^^^^^^^^^^^^^^^^^ END BACKGROUND RENDER
		 */
		/*
		 * UPDATE ENEMY AI
		 */
		if (pollCheck(6)){
			for (int i = 0; i < ENEMY_LIMIT; i++) {
				if (zenemy[i] != null) {
					zenemy[i].updateAI(zplayer);
				}
			}
			for (int i = 0; i < ENEMY_LIMIT; i++) {
				if (zenemy[i] != null) {
					boolean enemyAim = true;
					if (enemyAim) {
						zenemy[i].update(true, camera, true);
					} else {
						zenemy[i].update(true, camera, true);
					}
					// If too far away from player, dispose of enemy
					if (Math.abs((double) (zenemy[i].worldpos.x - zplayer.worldpos.x)) > 30
							|| (double) (zenemy[i].worldpos.y - zplayer.worldpos.y) > 30) {
						zenemy[i].isDisposed = true;
						zenemy[i].isAlive = false;
					}
				}
			}
		}
	
		/*
		 * BEGIN CORE SPRITE BATCH RENDER LOOP
		 */
		for (int i = 0; i < DRONE_LIMIT; i++) {
			if (drone[i] != null && drone[i].isAlive) {
				drone[i].update(zplayer);
			}
		}

		renderer.setView(camera);
		renderer.render();
		batch.begin();

		for (int i = 0; i < DRONE_LIMIT; i++) {
			if (drone[i] != null && drone[i].isAlive) {
				drone[i].sprite.draw(batch);
			}
		}
		// drone[0].sprite.setPosition(zplayer.screenpos.x,
		// zplayer.screenpos.y);

		dirbuttonssprite.setPosition(camera.position.x - 8,
				camera.position.y - 5);
		dirbuttonssprite.draw(batch);

		Vector3 tmpVec3 = new Vector3((inputHandler.getXInputPosition("jump")),
				0, 0);
		camera.unproject(tmpVec3);
		tmpVec3.y = camera.position.y - 5;
		jumpbuttonsprite.setPosition(tmpVec3.x, tmpVec3.y);

		tmpVec3.x = (inputHandler.getXInputPosition("shoot"));
		camera.unproject(tmpVec3);
		tmpVec3.y = camera.position.y - 5;
		shootbuttonsprite.setPosition(tmpVec3.x, tmpVec3.y);
		shootbuttonsprite.draw(batch);

		jumpbuttonsprite.draw(batch);

		batch.setColor(Color.RED);
		for (int i = 0; i < ENEMY_LIMIT; i++) {
			if (zenemy[i] != null && zenemy[i].sprite != null
					&& !zenemy[i].isDisposed) {
				zenemy[i].sprite.draw(batch);
				if (!zenemy[i].isOnLadder && zenemy[i].isAlive) {
					zenemy[i].armsprite.draw(batch);
				}
				if (zenemy[i].isOnLadder && zenemy[i].isShooting
						&& zenemy[i].isAlive) {
					zenemy[i].armsprite.draw(batch);
				}
			}
		}
		
		
		zplayer.sprite.draw(batch);

		/*
		 * NEED TO FIX ARMSPRITE LOADING/NOT LOADING HERE
		 */
		if (!zplayer.isOnLadder && zplayer.isAlive) {
			zplayer.armsprite.draw(batch);
		}
		if (zplayer.isOnLadder && zplayer.isShooting && zplayer.isAlive) {
			zplayer.armsprite.draw(batch);
		}
		float extracamx = 0;
		if (zplayer.aimingdirection == "left") {
			if (addextracamx < viewwidth / 2) {
				addextracamx += 7;
				if (addextracamx < 0) {
					addextracamx += 14;
				}
				if (zplayer.velocity.x > 0) {
					addextracamx += 7;
				}
			}
		} else if (zplayer.aimingdirection == "right") {
			if (addextracamx > -viewwidth / 2) {
				addextracamx -= 7;
				if (addextracamx > 0) {
					addextracamx -= 14;
				}
				if (zplayer.velocity.x > 0) {
					addextracamx -= 7;
				}
			}
		}
		camera.position.set(zplayer.worldpos.x + addextracamx / 200,
				zplayer.worldpos.y + 1.5f, 0);
		for (int i = 0; i < DESTROYABLE_LIMIT; i++) {
			if (destroyable[i] != null) {
				destroyable[i].sprite.draw(batch);
			}
		}
		for (int i = 0; i < DOOR_LIMIT; i++) {
			if (door[i] != null) {
				door[i].sprite.draw(batch);
			}
		}
		for (int i = 0; i < ITEM_LIMIT; i++) {
			if (item[i] != null) {
				if (item[i].isAlive) {
					item[i].sprite.draw(batch);
				}
			}
		}
		/*
		 * Draw
		 */
		drawProjectile(projMan);
		drawProjectile(aiProjMan);

		if (hudtarget.canDraw()){
			hudtarget.sprite.draw(batch);
		}
		//boolean levelcomplete = false;
		if (Math.abs(levelcompletepos.x - zplayer.worldpos.x) < 3 && Math.abs(levelcompletepos.y - zplayer.worldpos.y) < 3 ){
			levelComplete = true;
			levelcompletesprite.setPosition(camera.position.x-5, camera.position.y-2);
			levelcompletesprite.setSize(12f, 6f);
		}
		
		if (levelComplete) levelcompletesprite.draw(batch);
		
		batch.end();
		

		/*
		 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ END CORE SPRITE BATCH RENDER LOOP
		 */
		displayControls();
		showDebugInfo(showDebug);
		box2DRender(debugOn);
		camera.update();
		/*
		 * DEACTIVATE DOORS, ENEMIES AND DESTROYED OBJECTS
		 */

		if (pollCheck(10)){
			huntClosestEnemy();
			
			Array<Body> projbodies = cl.getBodies();
			for (int i = 0; i < projbodies.size; i++) {
				Body b = projbodies.get(i);
				System.out.println("Destroying :" + b.getUserData());
				projMan.KillProjectile(Integer.parseInt((String) b.getUserData()));
			}
			projbodies.clear();
			Array<Body> aiprojbodies = cl.getAiBodies();
			for (int i = 0; i < aiprojbodies.size; i++) {
				Body b = aiprojbodies.get(i);
				System.out.println("Destroying :" + b.getUserData());
				aiProjMan
						.KillProjectile(Integer.parseInt((String) b.getUserData()));
				if (cl.DamagePlayer()) {
					zplayer.takeDamage(5);
				}
			}
			aiprojbodies.clear();
	
			Array<Body> enemybodies = cl.getEnemies();
			for (int i = 0; i < enemybodies.size; i++) {
				Body b = enemybodies.get(i);
				int t = Integer.parseInt((String) b.getUserData());
				System.out.println("Destroying Enemy:" + b.getUserData());
				zenemy[t].isAlive = false;
				zenemy[t].body.setActive(false);
			}
			enemybodies.clear();
	
			Array<Body> itembodies = cl.getItems();
			for (int i = 0; i < itembodies.size; i++) {
				Body b = itembodies.get(i);
				int t = (Integer) b.getUserData();
				System.out.println("Destroying Item :" + b.getUserData());
				if (item[t].isAlive) {
					zplayer.health += item[t].addHealth;
					item[t].isAlive = false;
					item[t].body.setActive(false);
					if (zplayer.health > 150) {
						zplayer.health = 150;
					}
				}
			}
			itembodies.clear();
	
			Array<Body> dronebodies = cl.getDrones();
			for (int i = 0; i < dronebodies.size; i++) {
				Body b = dronebodies.get(i);
				int t = (Integer) b.getUserData();
				System.out.println("Destroying Drone :" + b.getUserData());
				if (drone[t].isAlive && drone[t] != null) {
					drone[t].isAlive = false;
					drone[t].body.setActive(false);
				}
			}
			dronebodies.clear();
	
			int killKeyValue = 0;
			Array<Body> destroybodies = cl.getDestroyables();
			for (int i = 0; i < destroybodies.size; i++) {
				Body b = destroybodies.get(i);
				int t = Integer.parseInt((String) b.getUserData());
				System.out.println("Destroying Destroyable:" + b.getUserData()
						+ "at Address:" + t);
				if (destroyable[t] != null) {
					destroyable[t].isAlive = false;
					destroyable[t].body.setActive(false);
					destroyable[t].Destroy();
					killKeyValue = destroyable[t].keyValue;
					zplayer.tm.keys[killKeyValue] = true;
					for (int j = 0; j < DOOR_LIMIT; j++) {
						if (door[j] != null) {
							if (zplayer.tm.keys[door[j].keyValue]) {
								door[j].openDoor();
							}
						}
					}
				} else {
					System.out.println("is NULL, destroyable.length ="
							+ destroyable.length);
				}
			}
			destroybodies.clear();
		}
		if (zplayer.worldpos.y < -15) {
			zplayer.health = 0;
		}
		
		if (Gdx.input.isTouched()) {
			if (!zplayer.isAlive) {
				game.setScreen(new MainMenu(game));
			}
			if (levelComplete){
				game.nextLevel();
				game.setScreen(new MainMenu(game));
			}
		}

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
		humanSprite.dispose();
		targettex.dispose();
		batch.dispose();
		for (int i = 0; i < ENEMY_LIMIT; i++) {
			zenemy[i].dispose();
		}
		zplayer.dispose();
		tm.map.dispose();
		sr.dispose();

	}
	
	
	
	
	public void huntClosestEnemy(){
		/*
		 * TODO: CYCLE THROUGH ENEMY ACTORS AND GIVE PLAYER CLOSEST ONE FOR
		 * QUICK SHOOT
		 */
		/*
		 * 
		 *  NEED TO FIX HERE, CODE IS UGLY, WANT TO REFACTOR
		 */
		
		float targetPosX = 0;
		float targetPosY = 0;
		boolean drawTarget = false;
		
		int closest_enemy_right = 0;
		int closest_enemy_left = 0;
		
		float calcdist_right = 999;
		float calcdist_left = 999;
		boolean enemyTooFar_right = false;
		boolean enemyTooFar_left = false;
		boolean targetIsDrone_right = false;
		boolean targetIsDrone_left = false;

		int BIG_LIMIT = ENEMY_LIMIT;
		if (ENEMY_LIMIT < DRONE_LIMIT) {
			BIG_LIMIT = DRONE_LIMIT;
		}

		for (int i = 0; i < BIG_LIMIT; i++) {
			if (zenemy[i] != null && zenemy[i].isAlive
					&& zenemy[i].worldpos.x > zplayer.worldpos.x) {
				if (zenemy[i].distanceFromPlayer < calcdist_right) {
					calcdist_right = zenemy[i].distanceFromPlayer;
					closest_enemy_right = i;
				}
			} else if (zenemy[i] != null && zenemy[i].isAlive
					&& zenemy[i].worldpos.x < zplayer.worldpos.x) {
				if (zenemy[i].distanceFromPlayer < calcdist_left) {
					calcdist_left = zenemy[i].distanceFromPlayer;
					closest_enemy_left = i;
				}
			}
			if (drone[i] != null && drone[i].isAlive
					&& drone[i].worldpos.x > zplayer.worldpos.x) {
				if (drone[i].distanceFromPlayer < calcdist_right) {
					calcdist_right = drone[i].distanceFromPlayer;
					closest_enemy_right = i;
					targetIsDrone_right = true;
				}
			} else if (drone[i] != null && drone[i].isAlive
					&& drone[i].worldpos.x < zplayer.worldpos.x) {
				if (drone[i].distanceFromPlayer < calcdist_left) {
					calcdist_left = drone[i].distanceFromPlayer;
					closest_enemy_left = i;
					targetIsDrone_left = true;
				}
			}

		}
		if (calcdist_right > 9) {
			enemyTooFar_right = true;

		} else {
			enemyTooFar_right = false;
		}
		if (calcdist_left > 9) {
			enemyTooFar_left = true;

		} else {
			enemyTooFar_left = false;
		}

		if (!zplayer.isOnLadder) {
			if (zplayer.isGoRight) {
				if (targetIsDrone_right) {
					if (drone[closest_enemy_right] != null) {
						zplayer.giveQuickTarget(drone[closest_enemy_right]);
						hudtarget.setDrawTarget(drone[closest_enemy_right]);
					}
					if (enemyTooFar_right || drone[closest_enemy_right] == null) {
						zplayer.setTargetToNull();
						hudtarget.dontDraw();
					}
				} else {
					if (zenemy[closest_enemy_right] != null) {
						zplayer.giveQuickTarget(zenemy[closest_enemy_right]);
						hudtarget.setDrawTarget(zenemy[closest_enemy_right]);
					}
					if (enemyTooFar_right || zenemy[closest_enemy_right] == null) {
						zplayer.setTargetToNull();
						hudtarget.dontDraw();
					}
				}
				
			} else {
				if (targetIsDrone_left) {
					if (drone[closest_enemy_left] != null) {
						zplayer.giveQuickTarget(drone[closest_enemy_left]);
						hudtarget.setDrawTarget(drone[closest_enemy_left]);
						
					}
					if (enemyTooFar_left || drone[closest_enemy_left] == null) {
						zplayer.setTargetToNull();
					}
				} else {
					if (zenemy[closest_enemy_left] != null) {
						zplayer.giveQuickTarget(zenemy[closest_enemy_left]);
						hudtarget.setDrawTarget(zenemy[closest_enemy_left]);
					}
					if (enemyTooFar_left || zenemy[closest_enemy_left] == null) {
						zplayer.setTargetToNull();
						hudtarget.dontDraw();
					}
				}
			}
		} else {
			if (calcdist_right < calcdist_left) {
				if (targetIsDrone_right) {
					if (drone[closest_enemy_right] != null) {
						zplayer.giveQuickTarget(drone[closest_enemy_right]);
						hudtarget.setDrawTarget(drone[closest_enemy_right]);
					}
					if (enemyTooFar_right || drone[closest_enemy_right] == null) {
						zplayer.setTargetToNull();
						hudtarget.dontDraw();
					}
				}else{
					if (zenemy[closest_enemy_right] != null) {
						zplayer.giveQuickTarget(zenemy[closest_enemy_right]);
						hudtarget.setDrawTarget(zenemy[closest_enemy_right]);
					}
					if (enemyTooFar_right || zenemy[closest_enemy_right] == null) {
						zplayer.setTargetToNull();
						hudtarget.dontDraw();
					}
				}
			} else {
				if (targetIsDrone_left) {
					if (drone[closest_enemy_left] != null) {
						zplayer.giveQuickTarget(drone[closest_enemy_left]);
						hudtarget.setDrawTarget(drone[closest_enemy_left]);
					}
					if (enemyTooFar_right || drone[closest_enemy_left] == null) {
						zplayer.setTargetToNull();
						hudtarget.dontDraw();
					}
				}else{
					if (zenemy[closest_enemy_left] != null) {
						zplayer.giveQuickTarget(zenemy[closest_enemy_left]);
						hudtarget.setDrawTarget(zenemy[closest_enemy_left]);
					}
					if (enemyTooFar_left || zenemy[closest_enemy_left] == null) {
						zplayer.setTargetToNull();
						hudtarget.dontDraw();
					}
				}
			}
		}
	}
	
	public int loopcount = 0;
	public boolean pollCheck(int v){
		if (loopcount % v == 0){
			return true;
		}
		return false;
	}
}
/*
 * TODO: ---------- KNOWN BUGS ---------- 1. Arm not appearing on ladder, sprite
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
