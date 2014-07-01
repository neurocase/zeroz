package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.dazpetty.zeroz.assets.ZeroAssetManager;
import com.dazpetty.zeroz.core.DazDebug;
import com.dazpetty.zeroz.managers.EntityManager;
import com.dazpetty.zeroz.managers.SceneManager;

public class WallTurret {

	public boolean isOpen = false;

	// public Vector2 screenpos = new Vector2(0,0);
	// public Vector2 velocity = new Vector2(0,0);
	// public float height = 2;
	// public float width = 1.25f;
	// public TextureRegion textureRegion = null;

	public Vector2 worldpos = new Vector2(0, 0);

	public float rotDir = 0;

	public Sprite sprite;

	public Sprite flamerturretsprite;
	public TextureRegion flamerturretTexRegion;
	public Texture flamerturretTexture;

	public TextureAtlas flamesTextureAtlas;
	public Sprite flamessprite;

	public Sprite turretbasesprite;
	public TextureRegion turretBaseTexRegion;
	public Texture turretBaseTexture;

	public Sprite turretbarrelsprite;
	public TextureRegion turretbarrelTexRegion;
	public Texture turretbarreltexture;

	private int currentFrame = 1;
	private String currentAtlasKey = new String("0001");

	public Body body;
	public FixtureDef fixtureDef;
	public Fixture doorfix;
	public boolean isAlive = true;
	public int keyValue;
	public boolean openDoor = false;

	public String TurretType = "";

	public float maxangle = 0;
	public float minangle = 0;

	public PawnEntity zplayer;
	public EntityManager entityMan;
	public SceneManager scene;
	public boolean isAI = true;
	// public boolean isAlive1 = true;

	public Weapon weapon = new Weapon(1);
	public int angle = 0;

	boolean alwaysShoot = false;

	public WallTurret(float x, float y, String type, int angle, World world,
			EntityManager entityMan, SceneManager scene) {
		Weapon weapon = new Weapon(1);
		this.scene = scene;
		this.entityMan = entityMan;
		this.angle = angle;

		worldpos.x = x;
		worldpos.y = y;

		BodyDef doorbodyDef = new BodyDef();
		doorbodyDef.position.set(worldpos.x + 0.5f, worldpos.y + 0.5f);
		doorbodyDef.type = BodyType.StaticBody;
		body = world.createBody(doorbodyDef);
		FixtureDef doorfixDef = new FixtureDef();
		PolygonShape pBox = new PolygonShape();
		pBox.setAsBox(0.5f, 0.5f);
		doorfixDef.shape = pBox;
		doorfix = body.createFixture(doorfixDef);
		doorfix.setUserData(this);

		doorfixDef.friction = 0;

		TurretType = type;
		turretBaseTexture = ZeroAssetManager.wallturret;
		turretBaseTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion turretBaseTexRegion = new TextureRegion(
				turretBaseTexture, 0, 0, 32, 32);
		turretbasesprite = new Sprite(turretBaseTexRegion);
		turretbasesprite.setPosition(x, y);
		turretbasesprite.setSize(1, 1);

		turretbarreltexture = ZeroAssetManager.wallturretbarrel;
		turretBaseTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion turretbarrelTexRegion = new TextureRegion(
				turretbarreltexture, 0, 0, 16, 64);
		turretbarrelsprite = new Sprite(turretbarrelTexRegion);

		flamerturretTexture = ZeroAssetManager.flameturret;
		flamerturretTexture.setFilter(TextureFilter.Linear,
				TextureFilter.Linear);
		TextureRegion flamerturretTexRegion = new TextureRegion(
				flamerturretTexture, 0, 0, 128, 64);
		flamerturretsprite = new Sprite(flamerturretTexRegion);
		flamerturretsprite.setPosition(x, y);
		flamerturretsprite.setSize(2, 1);

		flamesTextureAtlas = ZeroAssetManager.flamesTexAtlas;

		AtlasRegion atlasTexRegion = flamesTextureAtlas.findRegion("0001");
		flamessprite = new Sprite(atlasTexRegion);

		minangle = angle - 45;
		maxangle = angle + 45;
		// angle = 180;

		if (TurretType.equals("flamer")) {
			if (angle == 180) {
				flamerturretsprite.flip(true, false);
				flamerturretsprite.setPosition(x - 1, y);

			}
		} else if (TurretType.equals("wallturret")) {
			turretbasesprite.setOrigin(0.5f, 0.5f);
			// turretbasesprite.setOrigin(0.2f, 0f);
			turretbasesprite.rotate(angle);
			// turretbarrelsprite.setOrigin(turretbasesprite.getWidth()/2,
			// turretbasesprite.getWidth()/2);
			// turretbarrelsprite.setOrigin(turretbasesprite.getWidth()/2,
			// turretbasesprite.getHeight()/2);
			turretbarrelsprite.setSize(0.4f, 1.6f);
			turretbarrelsprite.setPosition(x, y);
			if (angle == 90) {
				turretbarrelsprite.setPosition(x + 1f, y + 0.25f);
			} else if (angle == 0) {
				turretbarrelsprite.setPosition(x + 0.25f, y);
			} else if (angle == 180) {
				turretbarrelsprite.setPosition(x + 0.5f, y + 0.75f);
			} else if (angle == 270) {
				turretbarrelsprite.setPosition(x, y + 1f);
			}

			turretbarrelsprite.setOrigin(0f, 0f);
			turretbarrelsprite.rotate(angle);
		} else if (TurretType.equals("wallturretfixed")) {

			minangle = angle - 2;
			maxangle = angle + 2;
			TurretType = "wallturret";
			alwaysShoot = true;
			turretbasesprite.setOrigin(0.5f, 0.5f);
			// turretbasesprite.setOrigin(0.2f, 0f);
			turretbasesprite.rotate(angle);
			// turretbarrelsprite.setOrigin(turretbasesprite.getWidth()/2,
			// turretbasesprite.getWidth()/2);
			// turretbarrelsprite.setOrigin(turretbasesprite.getWidth()/2,
			// turretbasesprite.getHeight()/2);
			turretbarrelsprite.setSize(0.4f, 1.6f);
			turretbarrelsprite.setPosition(x, y);
			if (angle == 90) {
				turretbarrelsprite.setPosition(x + 1f, y + 0.25f);
			} else if (angle == 0) {
				turretbarrelsprite.setPosition(x + 0.25f, y);
			} else if (angle == 180) {
				turretbarrelsprite.setPosition(x + 0.5f, y + 0.75f);
			} else if (angle == 270) {
				turretbarrelsprite.setPosition(x, y + 1f);
			}

			turretbarrelsprite.setOrigin(0f, 0f);
			turretbarrelsprite.rotate(angle);
		}

	}

	public void attemptShoot(float ang) {
		scene.aiProjMan.shootProjectile(ang, this);
	}

	float aimangle = 0;

	public void update(PawnEntity zplayer) {
		this.zplayer = zplayer;
		float xpos = zplayer.worldpos.x - worldpos.x;
		float ypos = zplayer.worldpos.y - worldpos.y;
		Vector2 tmpVec = new Vector2(xpos, ypos);
		aimangle = tmpVec.angle() - 90;

		if (aimangle > maxangle) {
			aimangle = maxangle;
		} else if (aimangle < minangle) {
			aimangle = minangle;
		} else {
			attemptShoot(aimangle + 90);
		}

		if (alwaysShoot) {
			attemptShoot(aimangle + 90);
		}

	}

	/*
	 * public float rotate(){ //float rotang = 0;
	 * 
	 * //Vector2 tmpVec = new Vector2(worldpos.x, worldpos.y); return }
	 */

	public void draw(SpriteBatch batch) {
		if (TurretType.equals("flamer")) {
			flamerturretsprite.draw(batch);
		} else if (TurretType.equals("wallturret")) {
			turretbarrelsprite.draw(batch);
			turretbasesprite.draw(batch);
			turretbarrelsprite.setRotation(aimangle);
		}
	}

	// public void update(){

	// }

}
