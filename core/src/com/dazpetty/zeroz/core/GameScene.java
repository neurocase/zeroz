package com.dazpetty.zeroz.core;

import java.util.LinkedHashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
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
import com.dazpetty.zeroz.entities.PawnEntity;
import com.dazpetty.zeroz.entities.CopterBoss;
import com.dazpetty.zeroz.entities.Destroyable;
import com.dazpetty.zeroz.entities.Door;
import com.dazpetty.zeroz.entities.Drone;
import com.dazpetty.zeroz.entities.EntitySpawner;
import com.dazpetty.zeroz.entities.HUDTarget;
import com.dazpetty.zeroz.entities.HumanSprite;
import com.dazpetty.zeroz.entities.Item;
import com.dazpetty.zeroz.entities.Weapon;
import com.dazpetty.zeroz.managers.EntityManager;
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

	float stateTime;
	private boolean playerShoot = false;
	private boolean giveWorldPos = true;

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
	World world = new World(new Vector2(0, -10), true);
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
	private Vector2 playerpos = new Vector2(7, 7);
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

	public EntityManager entityMan;
	public WorldLogic worldLogic;
	final ZerozGame game;
	private OrthographicCamera camera;
	private CameraInputController cameraController;
	public ParralaxCamera pcamera;
	public OrthoCamController pcamcontroller;

	public LevelManager tm;
	public Assets assetMan;

	private boolean showDebug = true;
	public boolean debugOn = false;
	
	
	float viewwidth = Gdx.graphics.getWidth();
	float viewheight = Gdx.graphics.getHeight();
	/*
	 * FUNCTIONS
	 */
	public WorldRenderer worldRenderer;
	
	public GameScene(final ZerozGame gam) {

		this.game = gam;
		assetMan.load();
		assetMan.manager.finishLoading();
		
		camera = new OrthographicCamera(1, viewheight / viewwidth);
		camera.setToOrtho(false, (viewwidth / viewheight) * 10, 10);
		camera.update();

		tm = new LevelManager(game.level, entityMan, world);
		
		
		entityMan = new EntityManager(camera, world, tm);
		tm.buildLevel(entityMan);
		worldLogic = new WorldLogic(camera, entityMan, world, tm);
		worldRenderer = new WorldRenderer(camera, world, entityMan, tm, worldLogic);
	
	}
	//public boolean levelComplete = false;

	@Override
	public void render(float delta) {
		
	
		tm = entityMan.levelMan;
		
		//camera = worldLogic.camera;
		
	//	}
		showDebugInfo(true);
		worldLogic.update();
		//entityMan.
		worldRenderer.Render();
		
		worldLogic.inputHandler.checkKeyboard();
		worldLogic.inputHandler.checkTouch();
		
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
			if (!entityMan.zplayer.isAlive) {
				game.setScreen(new MainMenu(game));
			}
			if (tm.isLevelComplete){
				game.nextLevel();
				game.setScreen(new GameScene(game));
			}
		}
	}

	public void showDebugInfo(boolean show) {
		/*game.batch.begin();
		String info2 = "";
		if (debugOn) {
			info2 = " Health:" + entityMan.zplayer.health + " entityMan.zplayer X:"
					+ entityMan.zplayer.worldpos.x + ", Y:" + entityMan.zplayer.worldpos.y
					+ " state:" + entityMan.zplayer.state;
		} else {
			info2 = " Health:" + entityMan.zplayer.health;
			if (entityMan.zplayer.health <= 0) {
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
		game.batch.end();*/
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

	//	targettex.dispose();
		//batch.dispose();
		entityMan.dispose();
		tm.map.dispose();
	//	sr.dispose();

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
 * not flipping on ladder 2. Ladder movement not that awesome 3. Sometimes
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
