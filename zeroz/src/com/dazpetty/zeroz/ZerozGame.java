package com.dazpetty.zeroz;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.ListIterator;
import java.util.Set;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

public class ZerozGame implements ApplicationListener {
	private OrthographicCamera camera;
	private CameraInputController cameraController;
	private SpriteBatch batch;

	private Texture leftarrowtex;
	private Texture rightarrowtex;
	private Texture buttontex;
	private Texture targettex;

	private Sprite targetsprite;
	private Sprite leftarrowsprite;
	private Sprite rightarrowsprite;
	private Sprite buttonsprite;
	private Sprite playersprite;
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
	/*
	 * private static final int FRAME_COLS = 4; // #1 private static final int
	 * FRAME_ROWS = 6; // #2
	 * 
	 * Animation walkAnimation; // #3 Texture walkSheet; // #4 TextureRegion[]
	 * walkFrames; // #5 SpriteBatch spriteBatch; // #6 TextureRegion
	 * currentFrame; // #7
	 */
	float stateTime;

	private Zplayer zplayer;// = new Zactor();
	private Zenemy zenemy;
	Vector3 camVector = new Vector3(0, 0, 0);

	@Override
	public void create() {
			
		for( int i=0; i<250; i++ ) bulletArray[i] = new Zbullet();
		
		map = new TmxMapLoader().load("data/testmap2.tmx");
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

		targettex = new Texture(Gdx.files.internal("data/gfx/target.png"));

		targettex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		leftarrowtex = new Texture(
				Gdx.files.internal("data/gfx/buttons/left.png"));
		leftarrowtex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		rightarrowtex = new Texture(
				Gdx.files.internal("data/gfx/buttons/right.png"));
		rightarrowtex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		buttontex = new Texture(
				Gdx.files.internal("data/gfx/buttons/circle.png"));
		buttontex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		TextureRegion targettexreg = new TextureRegion(targettex, 0, 0, 128,
				128);

		TextureRegion leftarrowtexreg = new TextureRegion(leftarrowtex, 0, 0,
				32, 32);
		TextureRegion rightarrowtexreg = new TextureRegion(rightarrowtex, 0, 0,
				32, 32);
		TextureRegion buttontexreg = new TextureRegion(buttontex, 0, 0, 32, 32);

		targetsprite = new Sprite(targettexreg);
		targetsprite.setSize(2f, 2f);
		targetsprite.setOrigin(0, 0);
		targetsprite.setPosition(0f, 0f);

		leftarrowsprite = new Sprite(leftarrowtexreg);
		leftarrowsprite.setSize(2f, 1f);
		leftarrowsprite.setOrigin(0, 0);
		leftarrowsprite.setPosition(0f, 0f);

		rightarrowsprite = new Sprite(rightarrowtexreg);
		rightarrowsprite.setSize(2f, 1f);
		rightarrowsprite.setOrigin(0, 0);
		rightarrowsprite.setPosition(0f, 0f);

		buttonsprite = new Sprite(buttontexreg);
		buttonsprite.setSize(2f, 1f);
		buttonsprite.setOrigin(0, 0);
		buttonsprite.setPosition(0f, 0f);
	}

	@Override
	public void dispose() {
		targettex.dispose();
		batch.dispose();
		leftarrowtex.dispose();
		rightarrowtex.dispose();
		buttontex.dispose();
		zenemy.dispose();
		zplayer.dispose();
		map.dispose();
	}

	@Override
	public void render() {
		
		zplayer.isShooting = false;
		
		Gdx.gl.glClearColor(0.4f, 0.25f, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);

		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			zplayer.goRight();
		}

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			zplayer.goLeft();
		}

		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			zplayer.goJump();
		}

		float an = 0;
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			Zbullet bullet = new Zbullet();

			if (zplayer.isOnLadder) zplayer.velocity.y = 0;
			
			playerShoot = true;
				
			
		}

		if (Gdx.input.isTouched()) {

			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),
					0);

			float section = touchPos.x / viewwidth;
			boolean inTarget = false;

			if (touchPos.y < viewheight - 64) {
				inTarget = true;
			}

			if (inTarget) {
				camera.unproject(touchPos);
				int i = (int) touchPos.x;
				int j = (int) touchPos.y;

				Cell cell = npcLayer.getCell((int) (i), (int) (j));

				boolean foundEnemy = false;

				for (int l = -2; l < 2; l++) {
					for (int o = -2; o < 2; o++)
						if ((int) zenemy.worldpos.x + l == i
								&& (int) zenemy.worldpos.y + o == j) {
							//TODO:: TARGET LOCKING
							foundEnemy = true;
						}

				}
				if (!foundEnemy) {
					zplayer.hasEnemy = false;
				}
			}
			if (!inTarget) {
			
				if (section < 0.15) {
					zplayer.goLeft();
				} else if (section < 0.3) {
					zplayer.goRight();
				}

				if (section >= 0.5f && section <= 0.75f) {
					zplayer.goJump();
				}

				if (section > 0.75 && section < 1) {
						zplayer.velocity.y = 0;
						playerTarget = aimlessVec;
						playerShoot = true;
				}

			}
			if (inTarget){
				Vector2 newAimVec = new Vector2();

				newAimVec.x = zplayer.worldpos.x - touchPos.x;
				newAimVec.y = zplayer.worldpos.y - touchPos.y;

				
				playerTarget.x = newAimVec.x;
				playerTarget.y = newAimVec.y;
				giveWorldPos = false;
				playerShoot = true;
			}
		}

		zenemy.update(zplayer);

		physics.doPhysics(zenemy);
		physics.doPhysics(zplayer);

		renderer.setView(camera);
		camera.update();

		renderer.render();
		screenPosZero = zeroVector3;
		screenPosZero.y += viewheight;
		camera.unproject(screenPosZero);
		leftarrowsprite.setPosition(screenPosZero.x, screenPosZero.y);

		screenPosZero = zeroVector3;
		screenPosZero.y += viewheight;
		screenPosZero.x += ((viewwidth / 8));
		camera.unproject(screenPosZero);
		rightarrowsprite.setPosition(screenPosZero.x, screenPosZero.y);

		screenPosZero = zeroVector3;
		screenPosZero.y += viewheight;
		screenPosZero.x += (viewwidth - (viewwidth / 4));
		camera.unproject(screenPosZero);
		buttonsprite.setPosition(screenPosZero.x, screenPosZero.y);

		camera.position.set(zplayer.worldpos.x, zplayer.worldpos.y, 0);

		batch.begin();
		zplayer.update(playerTarget.x,playerTarget.y, giveWorldPos, camera, playerShoot);

		leftarrowsprite.draw(batch);
		rightarrowsprite.draw(batch);
		
		
		for (int i = 0; i < 100; i++){ 
			if (!bulletArray[i].isAlive){
			//	bulletArray[i].fire(position, aimAngle);
				break;
			}	
		}
		
		if(playerShoot){
			if (activeBullet == 100){
				activeBullet = 0;
			}
			bulletArray[activeBullet].isAlive = true;
			Vector3 tmpVec3 = new Vector3(zplayer.worldpos.x,zplayer.worldpos.y,0);
			//camera.unproject(tmpVec3);
			bulletArray[activeBullet].screenpos.y = zplayer.worldpos.y;
			bulletArray[activeBullet].screenpos.x = zplayer.worldpos.x;
			bulletArray[activeBullet].fire(tmpVec3.x, tmpVec3.y, zplayer.aimAngle);
			//System.out.println(tmpVec3.x +","+ tmpVec3.y +","+ zplayer.aimAngle);
			activeBullet++;
		}
		
		for (int i = 0; i < 100; i++){
			if (bulletArray[i].isAlive){
				bulletArray[i].update(camera);
				Vector3 tmpVec = new Vector3 (bulletArray[i].screenpos.x, bulletArray[i].screenpos.y, 0);
				//camera.project(tmpVec);
				bulletArray[i].sprite.setPosition(tmpVec.x, tmpVec.y);

				if ((Math.abs(bulletArray[i].screenpos.x - zenemy.worldpos.x)<1)){
					if (Math.abs(bulletArray[i].screenpos.y - zenemy.worldpos.y) < 1){
						zenemy.goJump();
					}
				}
				//bulletArray[i].sprite.translateX(bulletArray[i].velocity.x / 50);
				//bulletArray[i].sprite.translateY(bulletArray[i].velocity.y / 50);
				
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
		
		/*
		 * TODO: WANT TO REMOVE BULLETS FROM SET TO SAVE MEMORY
		 */
		
		buttonsprite.draw(batch);
		batch.end();
		zenemy.draw(camera);
		
		playerShoot = false;
		playerTarget.x = 0f;
		playerTarget.y = 0f;
		System.out.println("ZENEMY X" + zenemy.worldpos.x + ", Y" + zenemy.worldpos.y);
		System.out.println("ZPLAYER X" + zplayer.worldpos.x + ", Y" + zplayer.worldpos.y);
		System.out.println("ACTIVEBULLET X" + bulletArray[activeBullet].worldpos.x + ", Y" + bulletArray[activeBullet].worldpos.y);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}