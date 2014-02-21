package com.dazpetty.zeroz;

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
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ZerozGame implements ApplicationListener {
	private OrthographicCamera camera;
	private CameraInputController cameraController;
	private SpriteBatch batch;
	
	private Texture dpadtex;
	private Sprite dpadsprite;
	private Sprite playersprite;
	private TiledMap map;
	private TiledMapRenderer renderer;
	public TiledMapTileLayer collisionLayer;
	private Vector2 playerstart = new Vector2(4,4);
	private Zphysics physics;
	
	
	/*private static final int        FRAME_COLS = 4;         // #1
	private static final int        FRAME_ROWS = 6;         // #2
	  
	Animation                       walkAnimation;          // #3
	Texture                         walkSheet;              // #4
	TextureRegion[]                 walkFrames;             // #5
	SpriteBatch                     spriteBatch;            // #6
	TextureRegion                   currentFrame;           // #7*/
	float stateTime;
	
	//Array<Vector3> waypoints;
	//Vector<Vector3f>
	private Zplayer zplayer;// = new Zactor();
	Vector3 camVector = new Vector3(0,0,0);

	
	@Override
	public void create() {	
		
		
	/*	walkSheet = new Texture(Gdx.files.internal("data/gfx/punk/run.png"));     // #9
		   TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / 
				   FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);                                // #10
       walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
       int index = 0;
       for (int i = 0; i < FRAME_ROWS; i++) {
               for (int j = 0; j < FRAME_COLS; j++) {
                       walkFrames[index++] = tmp[i][j];
               }
       }*/
   /*    walkAnimation = new Animation(0.025f, walkFrames);              // #11
       spriteBatch = new SpriteBatch();                                // #12
       stateTime = 0f;                                                 // #13*/
		
       
       
		map = new TmxMapLoader().load("data/testmap.tmx");	
		
		collisionLayer = (TiledMapTileLayer) map.getLayers().get("Layer 0");
		
		zplayer = new Zplayer();
		physics = new Zphysics();
		zplayer.initActor(collisionLayer, playerstart);//, Gdx.files.internal("data/player.png"));
		zplayer.create();
		
		//worldController = new WorldController();
		//zplayer.Velx = 0;
		//zplayer.posy = 4;
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		renderer = new OrthogonalTiledMapRenderer(map, 1f / 32f);
		
		//camera = new OrthographicCamera(1, h/w);
		camera = new OrthographicCamera(1, h/w);
		camera.setToOrtho(false, (w / h) * 10, 10);
		camera.update();
		//cameraController = new CameraInputController(camera);
		//Gdx.input.setInputProcessor(cameraController);
		
		batch = new SpriteBatch();
		
		dpadtex = new Texture(Gdx.files.internal("data/dpad.png"));
		//playertex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		dpadtex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		
		TextureRegion dpadtexreg = new TextureRegion(dpadtex, 0, 0, 128, 128);
		
		dpadsprite = new Sprite(dpadtexreg);
		dpadsprite.setSize(1f,1f);//0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		dpadsprite.setOrigin(dpadsprite.getWidth()/2, dpadsprite.getHeight()/2);
		dpadsprite.setPosition(0f,0f);//-sprite.getWidth()/2, -sprite.getHeight()/2);
		
		/*playersprite = new Sprite(zplayer.currentFrame);
		playersprite.setSize(1f,2f);//0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		playersprite.setOrigin(playersprite.getWidth()/2, playersprite.getHeight()/2);
		playersprite.setPosition(2f,1f);//-sprite.getWidth()/2, -sprite.getHeight()/2);*/
		
		//map = TiledLoader.createMap(Gdx.files.internal("tiles/tiles.tmx"));
	//    atlas = new SimpleTileAtlas(map, Gdx.files.internal("tiles/"));
	  //  tileMapRenderer = new TileMapRenderer(map, atlas, 128, 128, 8, 8);
	}

	@Override
	public void dispose() {
		batch.dispose();
		dpadtex.dispose();
		zplayer.dispose();	
		map.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(0.4f, 0.25f, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		
		
        
        
        
		batch.setProjectionMatrix(camera.combined);

		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
				Gdx.app.exit();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			zplayer.goRight();
	     }
		
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			zplayer.goLeft();
	     }
		
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			zplayer.goJump();
	     }
		
		physics.doPhysics(zplayer);
		
	//	playersprite.setPosition(zplayer.position.x,zplayer.position.y);
		
		renderer.setView(camera);
		camera.update();
		
		renderer.render();
		
		camera.position.set(zplayer.position.x,zplayer.position.y,0);
		
		batch.begin();
		dpadsprite.draw(batch);
	//	playersprite.draw(batch);
		batch.end();
		zplayer.draw(1,1);
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