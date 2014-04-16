package com.dazpetty.zeroz;

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


public class GameScene implements Screen {
	
	
	World world = new World(new Vector2(0, -10), true);
	Box2DDebugRenderer debugRenderer; 
	
	static final float BOX_STEP=1/60f;  
	static final int BOX_VELOCITY_ITERATIONS=6;  
	static final int BOX_POSITION_ITERATIONS=2;  
	static final float WORLD_TO_BOX=0.01f;  
	static final float BOX_TO_WORLD=100f;  
	
	public float ang = 0;
	
	private DazContactListener cl;
	
	public final int PROJECTILE_LIMIT = 40;
	public final int ENEMY_LIMIT = 10;
	public final int ENEMY_SPAWN_LIMIT = 10;  
	
	public int enemyspawners = 0;
	
	final ZerozGame game;

	public int activeproj = 0;
	
	private static final String TAG = ZerozGame.class.getName();

	private OrthographicCamera camera;
	private CameraInputController cameraController;
	private SpriteBatch batch;
	private SpriteBatch bgbatch;
	private HumanSprite humanSprite = new HumanSprite();
	
	public String blockedKey = "solid";
	public String platformKey = "platform";

	public ParralaxCamera pcamera;
	public OrthoCamController pcamcontroller;

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
	public TiledMapTileLayer miscLayer;
	private Vector2 playerstart = new Vector2(7, 7);
	private Vector2 enemystart = new Vector2(30, 7);
	private Vector2 enemystart2 = new Vector2(60, 9);
	
	private Vector2 enemyspawner[] = new Vector2[ENEMY_SPAWN_LIMIT];
	
	Vector3 touchPos = new Vector3(0, 0, 0);
	Vector3 zeroVector3 = new Vector3(0, 0, 0);
	Vector3 aimlessVec = new Vector3(0, 0, 1);
	Vector3 playerTarget = new Vector3(0, 0, 0);

	private boolean playerShoot = false;
	private boolean giveWorldPos = true;
	
	
	public Texture dirbuttonstex;
	
	public Sprite dirbuttonssprite;
	
	
	private int activeBullet = 0;
	public Projectile[] proj = new Projectile[PROJECTILE_LIMIT];
	public EnemyMan[] zenemy = new EnemyMan[ENEMY_LIMIT];
	
	
	private boolean showDebug = false;

	int bulletsadded = 0;
	float viewwidth = 0;
	float viewheight = 0;

	public Vector target = new Vector2(0, 0);
    
	// Ray ray = camera.getP

	// Set<Zbullet> bullets = new LinkedHashSet<Zbullet>();

	// ListIterator<Zbullet> itzbullet = bullets.iterator();
	private Vector2 playerpos = new Vector2(0, 0);
	private String npcKey = "target";
	private String enemyKey = "enemyspawn";
	private Vector3 screenPosZero = new Vector3(0, 0, 0);

	float stateTime;
	
	Texture grentex;
	
	

	private Actor zplayer;// = new Zactor();
	//private EnemyMan zenemy;
	Vector3 camVector = new Vector3(0, 0, 0);

	
	boolean isCellEnemySpawn(float x, float y) {
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(enemyKey);
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
	
	ShapeRenderer sr = new ShapeRenderer();
	
	public GameScene(final ZerozGame gam) {
		this.game = gam;

		cl = new DazContactListener();
		world.setContactListener(cl);
		
		grentex = new Texture(Gdx.files.internal("data/gfx/reddot.png"));
		grentex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		dirbuttonstex = new Texture(
				Gdx.files.internal("data/gfx/hud/dirbuttons.png"));
		dirbuttonstex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		
		TextureRegion dirbuttonstexreg = new TextureRegion(dirbuttonstex, 0, 0, 256,
				64);
		
		dirbuttonssprite = new Sprite(dirbuttonstexreg);
		dirbuttonssprite.setSize(7f, 1f);
		dirbuttonssprite.setOrigin(0, 0);
		dirbuttonssprite.setPosition(30, 30);
		
		
		//for (int i = 0; i < 100; i++)
			//bulletArray[i] = new Projectile();

		map = new TmxMapLoader().load("data/testmap3.tmx");
		collisionLayer = (TiledMapTileLayer) map.getLayers().get("collision");
		miscLayer = (TiledMapTileLayer) map.getLayers().get("miscLayer");

		//   //zplayer.worldpos
        //enemystart2
		createActor(2, enemystart2);
		createActor(2, enemystart);
		//createActor(2, enemystart2);
		
		zplayer = new Actor(camera, world, false, collisionLayer, playerstart,0, humanSprite);
		


		viewwidth = Gdx.graphics.getWidth();
		viewheight = Gdx.graphics.getHeight();

		renderer = new OrthogonalTiledMapRenderer(map, 1f / 32f);

		camera = new OrthographicCamera(1, viewheight / viewwidth);
		camera.setToOrtho(false, (viewwidth / viewheight) * 10, 10);
		camera.update();
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
		
		TextureRegion grentexreg = new TextureRegion(grentex, 0, 0, 128,
				128);

		pcamera = new ParralaxCamera(viewheight * 2f, viewwidth * 0.5f);
		pcamcontroller = new OrthoCamController(pcamera);
		Gdx.input.setInputProcessor(pcamcontroller);

		bgbatch = new SpriteBatch();       
		
		for (int h = 0; h < collisionLayer.getHeight(); h++){
			for (int w = 0; w < collisionLayer.getWidth(); w++){
				
				if (isCellBlocked(w,h)){
			
						int c = 0;
						while(isCellBlocked(w+c,h)){
							c++;
						}
							BodyDef groundBodyDef =new BodyDef();  
					        groundBodyDef.position.set(new Vector2(w+c*0.5f, h+0.5f));  
					        Body groundBody = world.createBody(groundBodyDef);  
					        PolygonShape groundBox = new PolygonShape();
					        groundBox.setAsBox(c*0.5f, 0.5f);
					        groundBody.createFixture(groundBox, 0.0f);
					        FixtureDef fixtureDef = new FixtureDef();  
					        fixtureDef.shape = groundBox;  
					        fixtureDef.filter.categoryBits = 2;
					        Fixture gfix = groundBody.createFixture(groundBox, 0.0f);
					        gfix.setUserData("ground");
					        for (int d = 0; d < c-1; d++){
					        	w++;	
					        }
				}			
				if (isCellPlatform(w,h)){
					int c = 0;
					while(isCellPlatform(w+c,h)){
						c++;
					}
					BodyDef groundBodyDef =new BodyDef();  
			        groundBodyDef.position.set(new Vector2(w+c*0.5f, h+0.75f));  
			        Body groundBody = world.createBody(groundBodyDef);  
			        PolygonShape groundBox = new PolygonShape();
			        groundBox.setAsBox(c*0.5f, 0.2f);  
			        groundBody.createFixture(groundBox, 0.0f);
			        FixtureDef fixtureDef = new FixtureDef();  
			        fixtureDef.shape = groundBox;  
			        fixtureDef.filter.categoryBits = 1;
			        Fixture pfix = groundBody.createFixture(fixtureDef);  
			        pfix.setUserData("platform");
			        for (int d = 0; d < c-1; d++){
			        	w++;	
			        }
				}
				if (isCellEnemySpawn(w,h)){
					enemyspawner[enemyspawners] = new Vector2(w,h);
					enemyspawners++;
				}
				
			}
		}
		
        debugRenderer = new Box2DDebugRenderer();  
	}

	public int enemycount = 1;
	public long lasttimespawn = System.currentTimeMillis();
	
	private void createActor(int s, Vector2 nstartpos) {
	//	Body pbody;
	//	BodyDef bodyDef = new BodyDef(); 
     //   bodyDef.type = BodyType.DynamicBody;  
          
      //  pbody = world.createBody(bodyDef);  
     Vector2 startpos = new Vector2(nstartpos);
		
        if (s == 1){
        	
        	zplayer = new Actor(camera, world, false, collisionLayer, startpos, -1, humanSprite);
        //	bodyDef.position.set(zplayer.worldpos.x, zplayer.worldpos.y);
        }
		if (s == 2){
			long timenow = System.currentTimeMillis();	
			long a = timenow - lasttimespawn; 
			if (timenow - lasttimespawn > (50 * 50) && (zenemy[enemycount] == null || zenemy[enemycount].isAlive == false || zenemy[enemycount].isDisposed)){
				if (zenemy[enemycount] != null && zenemy[enemycount].body != null){
					System.out.println("destroying enemy body" + enemycount);
					world.destroyBody(zenemy[enemycount].body);
				}
				System.out.println("Spawning Enemy:"+ enemycount +" X:" + startpos.x + "Y" + startpos.y);
				lasttimespawn = System.currentTimeMillis();
				zenemy[enemycount] = new EnemyMan(camera, world,  collisionLayer, startpos, enemycount, humanSprite);
				enemycount++;
				if (enemycount == ENEMY_LIMIT){
					enemycount = 0;
				}
			}
		}
		
	}

	boolean initbod = false;
	FixtureDef fixtureDef;
	CircleShape dynamicCircle;
	public long lasttimeshoot = System.currentTimeMillis();
	
	public void attemptShoot(){
		long timenow = System.currentTimeMillis();	
		long a = timenow - lasttimeshoot; 
		if (timenow - lasttimeshoot > (50 * 10)){
			lasttimeshoot = System.currentTimeMillis();
			activeproj++;
			if (activeproj == PROJECTILE_LIMIT-1) activeproj = 0;	
			float speed = 25;
			
			proj[activeproj] = new Projectile(zplayer, world, activeproj, ang, speed);
			proj[activeproj].body.setTransform(new Vector2(zplayer.worldpos.x, zplayer.worldpos.y+1), 5.5f);
		}
	}
	
	
	public void checkKeyboard() {

		zplayer.isCrouching = false;

		if (Gdx.input.isKeyPressed(Keys.R)) {
			game.setScreen(new MainMenu(game));
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			if (!showDebug) {
				showDebug = true;
			} else {
				showDebug = false;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.U)) {
			attemptShoot();	
		}


		if (Gdx.input.isKeyPressed(Keys.C)) {
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

		if (Gdx.input.isKeyPressed(Input.Keys.UP)
				&& !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			zplayer.goJump();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)
				&& Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			zplayer.goThruPlatform = true;
			zplayer.goJumpDown();
		}

		float an = 0;

		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			zplayer.isCrouching = true;
		}
	}

	public void checkTouch() {
		for (int p = 0; p < 5; p++) {
			boolean wantJump = false;
			boolean wantCrouch = false;
			if (Gdx.input.isTouched(p)) {

				
				float t1 = 0.25f;
				float c1 = 0.14f;
				float c2 = 0.36f;
				float t2 = 0.5f;
				//float t3 = 0.6f;
				float t4 = 0.55f;
				float t5 = 0.9f;
				
				Vector3 touchPos = new Vector3(Gdx.input.getX(p),
						Gdx.input.getY(p), 0);

				float section = touchPos.x / viewwidth;
				boolean inTarget = false;

				if (touchPos.y < viewheight - 96) {
					inTarget = true;
				}

				if (inTarget) {
					camera.unproject(touchPos);
					int i = (int) touchPos.x;
					int j = (int) touchPos.y;
				}
				if (!inTarget) {
					
					if (!(section > 0.2 && section < 0.3)){
						if (section < t1) {
							zplayer.goLeft();
						} else if (section < t2) {
							zplayer.goRight();
						}
					}
					if (section >= c1 && section <= c2) {
						wantCrouch = true;
					}
					if (section >= t4 && section <= t5) {
						wantJump = true;
					}
					if (section > t5 && section < 1) {
						playerTarget = aimlessVec;
						playerShoot = true;
					}
				}
				if (inTarget) {
					Vector2 newAimVec = new Vector2();

					newAimVec.x = touchPos.x - zplayer.worldpos.x;
					newAimVec.y = touchPos.y - zplayer.worldpos.y;
					
					playerTarget.x = newAimVec.x;
					playerTarget.y = newAimVec.y;
					
					
					newAimVec.y -= 1;
					ang = newAimVec.angle();
					
					attemptShoot();
					
					giveWorldPos = false;
					playerShoot = true;
				}
			}
			if (!wantCrouch && wantJump) {
				zplayer.goJump();
				zplayer.goThruPlatform = true;
			}
			if (wantCrouch && wantJump) {
				zplayer.goThruPlatform = true;
				zplayer.goJumpDown();
			}
			if (wantCrouch && !wantJump) {
				zplayer.isCrouching = true;
			}
		}
	}
	
	public void displayControls() {
		viewwidth = Gdx.graphics.getWidth();
		viewheight = Gdx.graphics.getHeight();

		game.batch.begin();
	
		game.font.draw(game.batch, "JUMP/CLIMB", viewwidth * 0.37f, 30);
		game.font.draw(game.batch, "SHOOT", viewwidth * 0.5f, 30);
		game.batch.end();
	}

	public void showDebugInfo(boolean show) {
		if (show) {
			game.batch.begin();
			String info1 = "ZENEMY X:" + zenemy[enemycount-1].worldpos.x + ", Y:"
					+ zenemy[enemycount-1].worldpos.y + " state:" + zenemy[enemycount-1].state + " angle:"
					+ zenemy[enemycount-1].aimAtPlayer;

			String info2 = "ZPLAYER X:" + zplayer.worldpos.x + ", Y:"
					+ zplayer.worldpos.y + " state:" + zplayer.state
					+ " angle:" + zenemy[enemycount-1].aimAngle;

			if (Gdx.input.isTouched()) {
				Vector3 touchPos = new Vector3(Gdx.input.getX(),
						Gdx.input.getY(), 0);

				String info4 = "ScreenTouch X:" + touchPos.x + " Y:"
						+ touchPos.y;
				camera.unproject(touchPos);
				String info5 = "ScreenTouch X:" + touchPos.x + " Y:"
						+ touchPos.y;

				game.font.draw(game.batch, info4, 20, 300);
				game.font.draw(game.batch, info5, 20, 280);
			}
			game.font.draw(game.batch, info1, 20, 320);
			game.font.draw(game.batch, info2, 20, 340);
			//game.font.draw(game.batch, info3, 20, 360);
			game.batch.end();
		}
	}


	public void box2DRender() {
		
		
		debugRenderer.render(world, camera.combined);  
        world.step(1/45f, 6, 2);
        
	}
	@Override
    public void render(float delta) {
		boolean updatePCamera = false;
		
		
		
		for (int i = 0; i < enemyspawners; i++){
			if (Math.abs((double)(enemyspawner[i].x - zplayer.worldpos.x)) < 20){
				createActor(2,enemyspawner[i]);
			}
		}
		
		
		zplayer.goThruPlatform = false;
		zplayer.isShooting = false;

		Gdx.gl.glClearColor(0.4f, 0.25f, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

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
		
		checkKeyboard();
		checkTouch();
		
		
		for (int i = 0; i < ENEMY_LIMIT; i++){
			if (zenemy[i] != null){
			zenemy[i].updateAI(zplayer);
			}
		}
		
		
		for (int i = 0; i < ENEMY_LIMIT; i++){
			if (zenemy[i] != null){
				boolean enemyAim = true;
				if (enemyAim) {
					zenemy[i].update(zenemy[i].relativepos.x, zenemy[i].relativepos.y, true,
							camera, true);
				} else {
					zenemy[i].update(true, camera, true);
				}
				if (Math.abs((double)(zenemy[i].worldpos.x - zplayer.worldpos.x)) > 30 || (double)(zenemy[i].worldpos.y - zplayer.worldpos.y) > 30){
					zenemy[i].isDisposed = true;
					zenemy[i].isAlive = false;
				}
			}
		}
		
		//for 
		
		
		zplayer.update(playerTarget.x, playerTarget.y, giveWorldPos, camera,
				playerShoot);

		renderer.setView(camera);

		renderer.render();

		batch.begin();
		
		dirbuttonssprite.setPosition(camera.position.x-8, camera.position.y-5);
		dirbuttonssprite.draw(batch);
		
		if (playerShoot) {
			zplayer.shoot();
		}		

		batch.setColor(Color.RED);
		for (int i = 0; i < ENEMY_LIMIT; i++){
			if (zenemy[i] != null && zenemy[i].sprite != null && !zenemy[i].isDisposed){
				zenemy[i].sprite.draw(batch);
				if (!zenemy[i].isOnLadder && zenemy[i].isAlive) {
					zenemy[i].armsprite.draw(batch);
				}
				if (zenemy[i].isOnLadder && zenemy[i].isShooting && zenemy[i].isAlive) {
					zenemy[i].armsprite.draw(batch);
				}
			}
		}
		zplayer.sprite.draw(batch);
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
		
		for (int i = 0; i < PROJECTILE_LIMIT; i++){
			if (proj[i] != null){
				if (proj[i].isAlive){
					 if (proj[i].isAlive){
						Vector2 tmpgrenpos = proj[i].body.getPosition();
						proj[i].projsprite.setPosition(tmpgrenpos.x-0.25f, tmpgrenpos.y-0.25f);
						proj[i].projsprite.draw(batch);
					 }
				}else{
					if (!proj[i].isDead){
						proj[i].isDead = true;
					world.destroyBody(proj[i].body);
					}
				}
			}
		}
		
		batch.end();
		playerShoot = false;
		playerTarget.x = 0f;
		playerTarget.y = 0f;

		displayControls();
		showDebugInfo(showDebug);
		box2DRender();
		camera.update();
		
		
		Array<Body> projbodies = cl.getBodies();
		for(int i = 0; i < projbodies.size; i++) {
			Body b = projbodies.get(i);
			System.out.println("Destroying :" + b.getUserData());
			proj[Integer.parseInt((String) b.getUserData())].isAlive = false;
		}
		projbodies.clear();
		
		Array<Body> enemybodies = cl.getEnemies();
		for(int i = 0; i < enemybodies.size; i++) {
			Body b = enemybodies.get(i);
			int t = Integer.parseInt((String) b.getUserData());
			System.out.println("Destroying :" + b.getUserData());
			zenemy[t].isAlive = false;
			zenemy[t].body.setActive(false);
		}
		enemybodies.clear();
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
		for (int i = 0; i < ENEMY_LIMIT; i++){
			zenemy[i].dispose();	
		}
		zplayer.dispose();
		map.dispose();
		sr.dispose();
		
	}

}
