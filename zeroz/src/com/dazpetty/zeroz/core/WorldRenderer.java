package com.dazpetty.zeroz.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
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
import com.dazpetty.zeroz.managers.ActorManager;
import com.dazpetty.zeroz.managers.Assets;
import com.dazpetty.zeroz.managers.LevelManager;
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
	
	public Texture dirbuttonstex;
	public Sprite dirbuttonssprite;
	
	public Texture jumpbuttontex;
	public Sprite jumpbuttonsprite;
	public Texture shootbuttontex;
	public Sprite shootbuttonsprite;
	Texture grentex;
	private SpriteBatch batch;
	private SpriteBatch bgbatch;
	
	private boolean showDebug = false;
	
	Box2DDebugRenderer debugRenderer;
	private Texture levelCompleteTex;
	private TextureRegion levelCompleteTexReg;
	private Sprite levelcompletesprite;
	
	private OrthographicCamera camera;
	private CameraInputController cameraController;
	public ParralaxCamera pcamera;
	public OrthoCamController pcamcontroller;
	
	public World world;
	public ActorManager actorMan;
	public OrthogonalTiledMapRenderer renderer;
	public WorldLogic worldLogic;
	
	public float viewwidth = 0;
	public float viewheight = 0;
	private float addextracamx = 0;
	LevelManager tm;
	
	ShapeRenderer sr = new ShapeRenderer();
	
	public WorldRenderer(OrthographicCamera camera, World world, ActorManager actorMan, LevelManager tm, WorldLogic worldLogic){
		
		
		viewwidth = Gdx.graphics.getWidth();
		viewheight = Gdx.graphics.getHeight();
		
		this.worldLogic = worldLogic;
		this.tm = tm;
		this.actorMan = actorMan;
		this.camera = camera;
		this.world = world;
		
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
		
		

		
		renderer = new OrthogonalTiledMapRenderer(tm.map, 1f / 32f);
		
		
		pcamera = new ParralaxCamera(viewheight * 2f, viewwidth * 0.5f);
		pcamcontroller = new OrthoCamController(pcamera);
		Gdx.input.setInputProcessor(pcamcontroller);
		//}
		
		// BOX 2D DEBUG RENDERER
				debugRenderer = new Box2DDebugRenderer();
		
		
	}
	
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
		
		if (tm.isBossLevel){
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
		if (!tm.isLevelScrolling){
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
		if (tm.levelcompletepos.x != 0 && tm.levelcompletepos.y != 0){
			if (Math.abs(tm.levelcompletepos.x - actorMan.zplayer.worldpos.x) < 3 && Math.abs(tm.levelcompletepos.y - actorMan.zplayer.worldpos.y) < 3 ){
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
		
		displayControls();
	
		box2DRender(debugOn);
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
		world.step(1 / 45f, 6, 2);
	}



	public boolean debugOn = false;
	
	
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
	
}
