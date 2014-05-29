package com.dazpetty.zeroz.core;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.dazpetty.zeroz.entities.Explosion;
import com.dazpetty.zeroz.entities.HUD;
import com.dazpetty.zeroz.managers.EntityManager;
import com.dazpetty.zeroz.managers.Assets;
import com.dazpetty.zeroz.managers.LevelManager;
import com.dazpetty.zeroz.managers.MyAssetManager;
import com.dazpetty.zeroz.managers.OrthoCamController;
import com.dazpetty.zeroz.managers.ParralaxCamera;
import com.dazpetty.zeroz.managers.ProjectileManager;

public class WorldRenderer {

	
	/*
	 * TEXTURES AND SPRITES
	 */
	private Texture bgCityBgTex;
	private Texture targettex;
	private TextureRegion bgCityBgTexReg;
	private Sprite targetsprite;
	private Sprite playersprite;
	private Sprite bgCityBackSprite;
	
	Texture grentex;
	private SpriteBatch batch;
	private SpriteBatch bgbatch;
	
	private boolean showDebug = true;
	
	Box2DDebugRenderer debugRenderer;
	private Texture levelCompleteTex;
	private TextureRegion levelCompleteTexReg;
	private Sprite levelcompletesprite;
	
	private OrthographicCamera camera;
	private CameraInputController cameraController;
	public ParralaxCamera pcamera;
	public OrthoCamController pcamcontroller;
	
	public World world;
	public EntityManager entityMan;
	public OrthogonalTiledMapRenderer renderer;
	public WorldLogic worldLogic;
	public Explosion explosion;
	public float viewwidth = 0;
	public float viewheight = 0;
	private float addextracamx = 0;
	private HUD hud;
	LevelManager levelMan;
	
	ShapeRenderer sr = new ShapeRenderer();
	
	
	public WorldRenderer(OrthographicCamera camera, World world, EntityManager entityMan, LevelManager levelMan, WorldLogic worldLogic){
		
		viewwidth = Gdx.graphics.getWidth();
		viewheight = Gdx.graphics.getHeight();
		
		this.worldLogic = worldLogic;
		this.levelMan = levelMan;
		this.entityMan = entityMan;
		this.camera = camera;
		this.world = world;
		
		/*
		 * SETUP SPRITES AND TEXTURES
		 */
		 

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
		
		

		
		renderer = new OrthogonalTiledMapRenderer(levelMan.map, 1f / 32f);
		
		
		pcamera = new ParralaxCamera(viewheight * 2f, viewwidth * 0.5f);
		pcamcontroller = new OrthoCamController(pcamera);
		Gdx.input.setInputProcessor(pcamcontroller);
		//}
		
		// BOX 2D DEBUG RENDERER
				debugRenderer = new Box2DDebugRenderer();
				
				//explosion = new Explosion(7f,7f,1,0);
				
				
		
			
			float camheight = 16;
			camera.viewportWidth = 24;
			int height = (int)Gdx.graphics.getHeight();
			int width = (int) Gdx.graphics.getWidth();
			float ratio = (viewheight / viewwidth);
			int camh = (int) (camera.viewportWidth /2);
			camera.viewportHeight = camera.viewportWidth * ratio;
			hud = new HUD(camera, worldLogic.inputHandler);
		
	}
	boolean dynamicCamera = false;
	
	public void Render(){
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
		Gdx.gl.glClearColor(0.075f, 0.0f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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

	
		/*
		 * ^^^^^^^^^^^^^^^^^^^^^ END BACKGROUND RENDER
		 */
		
		/*
		 * BEGIN CORE SPRITE BATCH RENDER LOOP
		 */
		for (int i = 0; i < entityMan.DRONE_LIMIT; i++) {
			if (entityMan.drone[i] != null && entityMan.drone[i].isAlive) {
				entityMan.drone[i].update(entityMan.zplayer);
			}
		}
		renderer.setView(camera);
		renderer.render();
		
		batch.begin();
		
		
		
		if (levelMan.isBossLevel){
			entityMan.copterBoss.bossSprite.draw(batch);
			entityMan.copterBoss.update();
			
			for (int i = 0; i < entityMan.copterBoss.copterTurret.length; i++){
				if (entityMan.copterBoss.copterTurret[i] != null && entityMan.copterBoss.copterTurret[i].isAlive){
					entityMan.copterBoss.copterTurret[i].baseSprite.draw(batch);
					entityMan.copterBoss.copterTurret[i].barrelSprite.draw(batch);
				}
			}
		}
		
		for (int i = 0; i < entityMan.EXPLOSION_LIMIT; i++) {
			if (entityMan.explosion[i] != null && entityMan.explosion[i].isAlive) {
				entityMan.explosion[i].sprite.draw(batch);
			}
		}
		
		for (int i = 0; i < entityMan.DRONE_LIMIT; i++) {
			if (entityMan.drone[i] != null && entityMan.drone[i].isAlive) {
				entityMan.drone[i].sprite.draw(batch);
			}
		}
		// drone[0].sprite.setPosition(actorMan.zplayer.screenpos.x,
		// actorMan.zplayer.screenpos.y);
		
		hud.update(camera);
		//dazdebug.print("camera.viewportHeight: " + camera.viewportHeight + 14);
		hud.healthsprite.draw(batch);
		
		
		hud.dirbuttonssprite.draw(batch);
	
		hud.shootbuttonsprite.draw(batch);

		hud.jumpbuttonsprite.draw(batch);

		batch.setColor(Color.RED);
		for (int i = 0; i < entityMan.ENEMY_LIMIT; i++) {
			if (entityMan.zenemy[i] != null && entityMan.zenemy[i].sprite != null
					&& !entityMan.zenemy[i].isDisposed) {
				entityMan.zenemy[i].sprite.draw(batch);
				if (!entityMan.zenemy[i].isOnLadder && entityMan.zenemy[i].isAlive) {
					entityMan.zenemy[i].armsprite.draw(batch);
				}
				if (entityMan.zenemy[i].isOnLadder && entityMan.zenemy[i].isShooting
						&& entityMan.zenemy[i].isAlive) {
					entityMan.zenemy[i].armsprite.draw(batch);
				}
			}
		}
		entityMan.zplayer.sprite.draw(batch);
		/*
		 * NEED TO FIX ARMSPRITE LOADING/NOT LOADING HERE
		 */
		if (!entityMan.zplayer.isOnLadder && entityMan.zplayer.isAlive) {
			entityMan.zplayer.armsprite.draw(batch);
		}
		if (entityMan.zplayer.isOnLadder && entityMan.zplayer.isShooting && entityMan.zplayer.isAlive) {
			entityMan.zplayer.armsprite.draw(batch);
		}
		float extracamx = 0;
		if (entityMan.zplayer.aimingdirection == "left") {
			if (addextracamx < viewwidth / 2) {
				addextracamx += 7;
				if (addextracamx < 0) {
					addextracamx += 14;
				}
				if (entityMan.zplayer.velocity.x > 0) {
					addextracamx += 7;
				}
			}
		} else if (entityMan.zplayer.aimingdirection == "right") {
			if (addextracamx > -viewwidth / 2) {
				addextracamx -= 7;
				if (addextracamx > 0) {
					addextracamx -= 14;
				}
				if (entityMan.zplayer.velocity.x > 0) {
					addextracamx -= 7;
				}
			}
		}
		if (!dynamicCamera){
			addextracamx = 0;
		}
		if (!levelMan.isLevelScrolling){
		camera.position.set(entityMan.zplayer.worldpos.x + addextracamx / 200,
				entityMan.zplayer.worldpos.y + 1.5f, 100);
		//camera.rotate(1);
		
		}
		for (int i = 0; i < entityMan.DESTROYABLE_LIMIT; i++) {
			if (entityMan.destroyable[i] != null) {
				entityMan.destroyable[i].sprite.draw(batch);
			}
		}
		for (int i = 0; i < entityMan.DOOR_LIMIT; i++) {
			if (entityMan.door[i] != null) {
				entityMan.door[i].sprite.draw(batch);
				entityMan.door[i].updateFrame();
			}
		}
		for (int i = 0; i < entityMan.ITEM_LIMIT; i++) {
			if (entityMan.item[i] != null) {
				if (entityMan.item[i].isAlive) {
					entityMan.item[i].sprite.draw(batch);
				}
			}
		}
		/*
		 * Draw
		 */
		drawProjectile(entityMan.projMan);
		drawProjectile(entityMan.aiProjMan);

		
		if (entityMan.hudtarget.canDraw()){
			entityMan.hudtarget.sprite.draw(batch);
		}
		//boolean levelcomplete = false;
		if (levelMan.levelcompletepos.x != 0 && levelMan.levelcompletepos.y != 0){
			if (Math.abs(levelMan.levelcompletepos.x - entityMan.zplayer.worldpos.x) < 3 && Math.abs(levelMan.levelcompletepos.y - entityMan.zplayer.worldpos.y) < 3 ){
				levelMan.isLevelComplete = true;
				levelcompletesprite.setPosition(camera.position.x-5, camera.position.y-2);
				levelcompletesprite.setSize(12f, 6f);
			}
		}
		

		if (levelMan.isLevelComplete) levelcompletesprite.draw(batch);	

		batch.end();
		/*
		 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ END CORE SPRITE BATCH RENDER LOOP
		 */
		
		displayControls();
	
		box2DRender(showDebug);
		camera.update();
		
		
	}
	
	
	public void displayControls() {
		viewwidth = Gdx.graphics.getWidth();
		viewheight = Gdx.graphics.getHeight();
	}

	public void box2DRender(boolean debug) {
		if (debug) {
			debugRenderer.render(world, camera.combined);
		}
		world.step(1 / 30f, 1, 1);
		
	}

	public boolean debugOn = false;
	
	
	public void drawProjectile(ProjectileManager projMan) {
		for (int i = 0; i < projMan.PROJECTILE_LIMIT; i++) {
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
		for (int i = 0; i < projMan.MUZZLE_FLASH_LIMIT; i++) {
			if (projMan.muzzleflash[i] != null) {
				if (projMan.muzzleflash[i].isAlive) {
					projMan.muzzleflash[i].sprite.draw(batch);
				}
			}
		}
		dazdebug.tick();
	}
	DazDebug dazdebug = new DazDebug();
	long ticker = 0;
}
