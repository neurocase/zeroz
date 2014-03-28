package com.dazpetty.zeroz;

import java.util.LinkedHashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Zscene implements Screen {

	final ZerozGame game;

	private static final String TAG = ZerozGame.class.getName();

	private OrthographicCamera camera;
	private CameraInputController cameraController;
	private SpriteBatch batch;
	private SpriteBatch bgbatch;
	
	public Zparralaxcamera pcamera;
	public Zorthocamcontroller pcamcontroller;
	
	private float addextracamx = 0;
	
	private Texture bgCityBgTex;
	private Texture targettex;
	private TextureRegion bgCityBgTexReg;

	private Sprite targetsprite;
	
	private Sprite playersprite;
	
	
	private Sprite bgCityBackSprite;
	
	
	
	private TiledMap map;
	private TiledMapRenderer renderer;
	public TiledMapTileLayer collisionLayer;
	public TiledMapTileLayer npcLayer;
	private Vector2 playerstart = new Vector2(7, 10);
	private Vector2 enemystart = new Vector2(30, 10);
	private Zphysics physics;

	Vector3 touchPos = new Vector3(0, 0, 0);
	Vector3 zeroVector3 = new Vector3(0, 0, 0);
	Vector3 aimlessVec = new Vector3(0, 0, 1);
	Vector3 playerTarget = new Vector3(0, 0, 0);

	private boolean playerShoot = false;
	private boolean giveWorldPos = true;

	public Zbullet[] bulletArray = new Zbullet[250];
	private int activeBullet = 0;

	private boolean showDebug = false;

	int bulletsadded = 0;
	float viewwidth = 0;
	float viewheight = 0;

	public Vector target = new Vector2(0, 0);
	// Ray ray = camera.getP

	Set<Zbullet> bullets = new LinkedHashSet<Zbullet>();

	// ListIterator<Zbullet> itzbullet = bullets.iterator();
	private Vector2 playerpos = new Vector2(0, 0);
	private String npcKey = "target";
	private Vector3 screenPosZero = new Vector3(0, 0, 0);

	float stateTime;

	private Zplayer zplayer;// = new Zactor();
	private Zenemy zenemy;
	Vector3 camVector = new Vector3(0, 0, 0);

	public Zscene(final ZerozGame gam) {
		this.game = gam;

		for (int i = 0; i < 250; i++)
			bulletArray[i] = new Zbullet();

		map = new TmxMapLoader().load("data/testmap3.tmx");
		collisionLayer = (TiledMapTileLayer) map.getLayers().get("collision");
		npcLayer = (TiledMapTileLayer) map.getLayers().get("npcLayer");

		zenemy = new Zenemy();
		zplayer = new Zplayer();
		physics = new Zphysics();
		zplayer.initActor(collisionLayer, playerstart);
		zplayer.create();

		zenemy.initActor(collisionLayer, enemystart);
		zenemy.create();

		viewwidth = Gdx.graphics.getWidth();
		viewheight = Gdx.graphics.getHeight();

		renderer = new OrthogonalTiledMapRenderer(map, 1f / 32f);

		camera = new OrthographicCamera(1, viewheight / viewwidth);
		camera.setToOrtho(false, (viewwidth / viewheight) * 10, 10);
		camera.update();
		batch = new SpriteBatch();

		
		
		bgCityBgTex = new Texture(Gdx.files.internal("data/gfx/background/cityp1.png"));
		bgCityBgTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bgCityBgTexReg = new TextureRegion(bgCityBgTex, 0,0, 1024, 512);
		
		
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
		
		
        pcamera = new Zparralaxcamera(viewheight*2f, viewwidth*0.5f);
        //pcamera = new Zparralaxcamera(480, 320);
        pcamcontroller = new Zorthocamcontroller(pcamera);
        Gdx.input.setInputProcessor(pcamcontroller);

        bgbatch = new SpriteBatch();

	}

	
	public void checkKeyboard(){
		
		zplayer.isCrouching = false;
		
		
		if (Gdx.input.isKeyPressed(Keys.R)) {
			game.setScreen(new Zmainmenu(game));
			// is this wasting memory?
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			if (!showDebug) {
				showDebug = true;
			} else {
				showDebug = false;
			}
		}
		
		if (Gdx.input.isKeyPressed(Keys.C)) {
			
			Zbullet bullet = new Zbullet();

			if (zplayer.isOnLadder)
				zplayer.velocity.y = 0;

			playerShoot = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			zplayer.goRight();
		}

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			zplayer.goLeft();
		}

		if (Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			zplayer.goJump();
			
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			zplayer.goThruPlatform = true;
			zplayer.goJumpDown();
		}

		float an = 0;

		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			zplayer.isCrouching = true;
		}
	}
	
	public void checkTouch(){
		for (int p = 0; p < 5; p++) {
			boolean wantJump = false;
			boolean wantCrouch = false;
			if (Gdx.input.isTouched(p)) {

				Vector3 touchPos = new Vector3(Gdx.input.getX(p),
						Gdx.input.getY(p), 0);

				float section = touchPos.x / viewwidth;
				boolean inTarget = false;

				if (touchPos.y < viewheight - 64) {
					inTarget = true;
				}

				if (inTarget) {
					camera.unproject(touchPos);
					int i = (int) touchPos.x;
					int j = (int) touchPos.y;

				//	Cell cell = npcLayer.getCell((int) (i), (int) (j));

					/*boolean foundEnemy = false;

					for (int l = -2; l < 2; l++) {
						for (int o = -2; o < 2; o++)
							if ((int) zenemy.worldpos.x + l == i
									&& (int) zenemy.worldpos.y + o == j) {
								// TODO:: TARGET LOCKING
								foundEnemy = true;
							}

					}
					if (!foundEnemy) {
						zplayer.hasEnemy = false;
					}*/
				}
				if (!inTarget) {
					if (section < 0.15) {
						zplayer.goLeft();					
					} else if (section < 0.3) {
						zplayer.goRight();
					}
					if (section >= 0.5f && section <= 0.65f) {
						wantCrouch = true;
					}
					if (section >= 0.65f && section <= 0.8f) {
						wantJump = true;
					}
					if (section > 0.8 && section < 1) {
						playerTarget = aimlessVec;
						playerShoot = true;
					}
				}
				if (inTarget) {
					Vector2 newAimVec = new Vector2();

					newAimVec.x = zplayer.worldpos.x - touchPos.x;
					newAimVec.y = zplayer.worldpos.y - touchPos.y;

					playerTarget.x = newAimVec.x;
					playerTarget.y = newAimVec.y;
					giveWorldPos = false;
					playerShoot = true;
				}
			}
			if (!wantCrouch && wantJump){
				zplayer.goJump();
				zplayer.goThruPlatform = true;
			}
			if (wantCrouch && wantJump){
				zplayer.goThruPlatform = true;
				zplayer.goJumpDown();
			}
			if (wantCrouch && !wantJump){
				zplayer.isCrouching = true;
			}
		}
	}
	
	public void displayControls(){
		viewwidth = Gdx.graphics.getWidth();
		viewheight = Gdx.graphics.getHeight();
		
		game.batch.begin();
		
		game.font.draw(game.batch, "LEFT",viewwidth*0.08f,30);
		game.font.draw(game.batch, "RIGHT",viewwidth*0.15f,30);
		game.font.draw(game.batch, "JUMP/CLIMB",viewwidth*0.3f,30);
		game.font.draw(game.batch, "SHOOT",viewwidth*0.6f,30);
		
		game.batch.end();
	}
	
	public void showDebugInfo(boolean show){
		if (show){
			game.batch.begin();
			String info1 = "ZENEMY X" + zenemy.worldpos.x + ", Y"
					+ zenemy.worldpos.y;

			String info2 = "ZPLAYER X" + zplayer.worldpos.x + ", Y"
					+ zplayer.worldpos.y + " state:" + zplayer.state;

			String info3 = "ACTIVEBULLET X" 
					+ bulletArray[activeBullet].worldpos.x + ", Y"
					+ bulletArray[activeBullet].worldpos.y;
			game.font.draw(game.batch, info1, 20, 320);
			game.font.draw(game.batch, info2, 20, 340);
			game.font.draw(game.batch, info3, 20, 360);
			game.batch.end();
		}
	}
	
	@Override
	public void render(float delta) {
		zplayer.goThruPlatform = false;
		zplayer.isShooting = false;

		Gdx.gl.glClearColor(0.4f, 0.25f, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		
	//new TextureRegion()
		
		// keep camera in foreground layer bounds
				boolean updatePCamera = false;
				
				pcamera.position.x = camera.position.x;
				pcamera.position.y = camera.position.y;
				if (pcamera.position.x < -1024 + pcamera.viewportWidth / 2) {
					pcamera.position.x = -1024 + (int)(pcamera.viewportWidth / 2);
					updatePCamera = true;
				}

				if (pcamera.position.x > 1024 - pcamera.viewportWidth / 2) {
					pcamera.position.x = 1024 - (int)(pcamera.viewportWidth / 2);
					updatePCamera = true;
				}

				if (pcamera.position.y < 0) {
					pcamera.position.y = 0;
					updatePCamera = true;
				}
				// arbitrary height of scene
				if (pcamera.position.y > 400 - pcamera.viewportHeight / 2) {
					pcamera.position.y = 400 - (int)(pcamera.viewportHeight / 2);
					updatePCamera = true;
				}

		
		//bgbatch.setProjectionMatrix(pcamera.calculateParallaxMatrix(0.5f, 1));
		bgbatch.setProjectionMatrix(pcamera.calculateParallaxMatrix(2, 4));
		bgbatch.begin();

		//bgbatch.draw(bgCityBgTexReg, -512, -120);
		bgbatch.draw(bgCityBgTexReg, -(int)(bgCityBgTexReg.getRegionWidth() / 2) -(int)(bgCityBgTexReg.getRegionWidth()), -(int)(bgCityBgTexReg.getRegionHeight() / 2));
		bgbatch.draw(bgCityBgTexReg, -(int)(bgCityBgTexReg.getRegionWidth() / 2), -(int)(bgCityBgTexReg.getRegionHeight() / 2));
		bgbatch.draw(bgCityBgTexReg, -(int)(bgCityBgTexReg.getRegionWidth() / 2) +(int)(bgCityBgTexReg.getRegionWidth()), -(int)(bgCityBgTexReg.getRegionHeight() / 2));
		bgbatch.end();
		
		batch.setProjectionMatrix(camera.combined);

		checkKeyboard();
		checkTouch();
	
		zenemy.update(zplayer);

		physics.doPhysics(zenemy);
		physics.doPhysics(zplayer);
		renderer.setView(camera);
		camera.update();
		renderer.render();
	
		batch.begin();
		
		
		
		
		zplayer.update(playerTarget.x, playerTarget.y, giveWorldPos, camera,
				playerShoot);

		if (playerShoot) {
			if (activeBullet == 100) {
				activeBullet = 0;
			}
			bulletArray[activeBullet].isAlive = true;
			Vector3 tmpVec3 = new Vector3(zplayer.worldpos.x,
					zplayer.worldpos.y, 0);
			bulletArray[activeBullet].screenpos.y = zplayer.worldpos.y;
			bulletArray[activeBullet].screenpos.x = zplayer.worldpos.x;
			bulletArray[activeBullet].fire(tmpVec3.x, tmpVec3.y,
					zplayer.aimAngle);
			activeBullet++;
		}

		for (int i = 0; i < 100; i++) {
			if (bulletArray[i].isAlive) {
				bulletArray[i].update(camera);
				Vector3 tmpVec = new Vector3(bulletArray[i].screenpos.x,
						bulletArray[i].screenpos.y, 0);
				bulletArray[i].sprite.setPosition(tmpVec.x, tmpVec.y);

				
				
				//TODO: check collision of bullet with 
				
				/*
				if ((Math.abs(bulletArray[i].screenpos.x - zenemy.worldpos.x) < 1)) {
					if (Math.abs(bulletArray[i].screenpos.y - zenemy.worldpos.y) < 1) {
						zenemy.goJump();
					}
				}*/
				bulletArray[i].sprite.draw(batch);
			}
		}

		zplayer.sprite.draw(batch);
		if (!zplayer.isOnLadder) {
			zplayer.armsprite.draw(batch);
		}
		if (zplayer.isOnLadder && zplayer.isShooting) {
			zplayer.armsprite.draw(batch);
		}
		
		float extracamx = 0;
		if (zplayer.aimingdirection == "right"){
			if (addextracamx < viewwidth/2){
				addextracamx += 7;
				if (addextracamx < 0){
					addextracamx += 14;
				}
				if (zplayer.velocity.x > 0){
					addextracamx += 7;
				}
			}
			}else if (zplayer.aimingdirection == "left"){
				if (addextracamx > -viewwidth/2){
					addextracamx -= 7;
					if (addextracamx > 0){
						addextracamx -= 14;
					}
					if (zplayer.velocity.x > 0){
						addextracamx -= 7;
					}
				}
			}
		//camera.position.set(zplayer.worldpos.x+(extracamx/100), zplayer.worldpos.y+1.5f, 0);
		camera.position.set(zplayer.worldpos.x+addextracamx/200, zplayer.worldpos.y+1.5f, 0);
		
		batch.end();
		zenemy.draw(camera);

		playerShoot = false;
		playerTarget.x = 0f;
		playerTarget.y = 0f;
		
		
		displayControls();
		showDebugInfo(showDebug);

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
//		leftarrowtex.dispose();
//		rightarrowtex.dispose();
//		buttontex.dispose();
		zenemy.dispose();
		zplayer.dispose();
		map.dispose();
	}

}
