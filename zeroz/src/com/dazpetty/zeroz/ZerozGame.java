package com.dazpetty.zeroz;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.ListIterator;
import java.util.Set;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

		map = new TmxMapLoader().load("data/testmap2.tmx");
		collisionLayer = (TiledMapTileLayer) map.getLayers().get("collision");
		npcLayer = (TiledMapTileLayer) map.getLayers().get("npcLayer");
		/*
		 * this code doesn't work for some reason
		 * 
		 * int lh = 0; int lw = 0;
		 * 
		 * lh = npcLayer.getHeight(); lw = npcLayer.getWidth();
		 * 
		 * for (int i = 0; i < lw; i++){ for (int j = 0; i < lh; j++){ Cell cell
		 * = npcLayer.getCell((int) (i), (int) (j)); if (cell != null &&
		 * cell.getTile() != null &&
		 * cell.getTile().getProperties().containsKey(npcKey)){
		 * 
		 * } } }
		 */

		/*
		 * Explaining the Aiming system.
		 * 
		 * When I take the mouseInput for aiming, it gives me screen
		 * co-ordinates (from bottom left), from which i add half the width and
		 * height of the screen to get the appropriate angle.
		 * 
		 * When I click on a target I need to convert the screen co-ords to
		 * world co-ords to check for the target at grid location.
		 * 
		 * Once I have the world co-ordinates of the object, I need to project
		 * it back into relative screen co-ordinates to get the new aiming
		 * angle.
		 */

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
			System.out.print(" Angle:");
			System.out.print(zplayer.aimVec.angle());
			System.out.print(" ::");
			Zbullet bullet = new Zbullet();
			if (zplayer.hasTarget) {
				zplayer.updateTarget(camera);
				an = zplayer.aimVec.angle();
			} else {
				if (zplayer.isGoRight) {
					an = 0;
				} else {
					an = 180;
				}
			}

			if (zplayer.isOnLadder)
				zplayer.velocity.y = 0;
			//if (!zplayer.isShooting) {
				bullets.add(bullet);
				bullet.create(collisionLayer, zplayer.position, an,
						zplayer.hasTarget);
				zplayer.isShooting = true;
				
			//}
		}

		if (Gdx.input.isTouched()) {

			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),
					0);

			float section = touchPos.x / viewwidth;
			// System.out.println("Touch at - X:" + touchPos.x + " Y:"
			// + touchPos.y + "SECTION:" + section);
			boolean inTarget = false;

			if (touchPos.y < viewheight - 64) {
				inTarget = true;
			}

			if (inTarget) {
				zplayer.isShooting = true;
				camera.unproject(touchPos);
				int i = (int) touchPos.x;
				int j = (int) touchPos.y;

				Cell cell = npcLayer.getCell((int) (i), (int) (j));

				boolean foundEnemy = false;

				for (int l = -2; l < 2; l++) {
					for (int o = -2; o < 2; o++)
						if ((int) zenemy.position.x + l == i
								&& (int) zenemy.position.y + o == j) {
							System.out.print("ENEMY TARGET ACQUIRED");
							zplayer.giveTarget(zenemy);
							foundEnemy = true;
						}

				}
				if (!foundEnemy) {
					zplayer.hasTarget = false;
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
					// copied code from pressing down
					zplayer.isShooting = true;
					System.out.print(" Angle:");
					System.out.print(zplayer.aimVec.angle());
					System.out.print(" ::");
					Zbullet bullet = new Zbullet();
					if (zplayer.hasTarget) {
						zplayer.updateTarget(camera);
						an = zplayer.aimVec.angle();

					} else {
						if (zplayer.isGoRight) {
							an = 0;
						} else {
							an = 180;
						}
					}

					if (zplayer.isOnLadder)
						zplayer.velocity.y = 0;

					//if (!zplayer.isShooting) {
						bullets.add(bullet);
						bullet.create(collisionLayer,  zplayer.position, an,
								zplayer.hasTarget);
						zplayer.isShooting = true;
					//}

				}

			}
			if (inTarget) {
				Zbullet bullet = new Zbullet();
				Vector2 newAimVec = new Vector2();

				newAimVec.x = zplayer.position.x - touchPos.x;
				newAimVec.y = zplayer.position.y - touchPos.y;

				zplayer.aimVec.x = newAimVec.x;
				zplayer.aimVec.y = newAimVec.y;
				
				float man = (newAimVec.angle() - 180.0f);
				System.out.println("newAimVec:" + man);

				//if (!zplayer.isShooting) {
					bullets.add(bullet);
					bullet.create(collisionLayer, zplayer.position, man,
							zplayer.hasTarget);
					zplayer.isShooting = true;
					zplayer.amTouching = true;
				//}
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

		camera.position.set(zplayer.position.x, zplayer.position.y, 0);

		if (zplayer.hasTarget) {
			zplayer.updateTarget(camera);
			an = zplayer.aimVec.angle();

		}
		// batch.setProjectionMatrix(orthographic);
		batch.begin();

		if (zplayer.hasTarget) {
			targetsprite.setPosition(zenemy.position.x, zenemy.position.y);
			targetsprite.draw(batch);
		}

		leftarrowsprite.draw(batch);
		rightarrowsprite.draw(batch);

		buttonsprite.draw(batch);

		for (Zbullet zb : bullets) {
			if (zb.isAlive) {
				zb.update();
				if ((Math.abs(zb.position.x - zenemy.position.x) < 32)) {

					if (!zb.isTargeted
							&& (zb.position.y >= (zenemy.position.y))
							&& (zb.position.y < (zenemy.position.y + 3))) {
						zenemy.goJump();
					}
					if (zb.isTargeted
							&& Math.abs(zb.position.y - zenemy.position.y + 32) < 32) {
						zenemy.goJump();
					}
				}
			}
			zb.sprite.translateX(zb.velocity.x / 50);
			zb.sprite.translateY(zb.velocity.y / 50);
			zb.sprite.draw(batch);
		}

		zplayer.draw();
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

		batch.end();
		zenemy.draw(camera);

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