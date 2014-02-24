package com.dazpetty.zeroz;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
	public TiledMapTileLayer npcLayer;
	private Vector2 playerstart = new Vector2(7,7);
	private Zphysics physics;
	public Vector2 aimVec = new Vector2(0,0);
	Set<Zbullet> bullets= new LinkedHashSet<Zbullet>();
	Iterator<Zbullet> itzbullet = bullets.iterator();
	private Vector2 playerpos = new Vector2(0,0); 
	private String npcKey = "npc";
	
	
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
		
		map = new TmxMapLoader().load("data/testmap2.tmx");	
		
		collisionLayer = (TiledMapTileLayer) map.getLayers().get("collision");
		
		npcLayer = (TiledMapTileLayer) map.getLayers().get("npcLayer");
	/*	
	 * 
	 *  this code doesn't work for some reason
	 *  
	 *  
		int lh = 0;
		int lw = 0;
		
		lh = npcLayer.getHeight();
		lw = npcLayer.getWidth();
		
		for (int i = 0; i < lw; i++){
			for (int j = 0; i < lh; j++){
				Cell cell = npcLayer.getCell((int) (i), (int) (j));		
				if (cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(npcKey)){
						
				}
			}
		}*/
		
		zplayer = new Zplayer();
		physics = new Zphysics();
		zplayer.initActor(collisionLayer, playerstart);
		zplayer.create();
		
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
		
		
		
		playerpos.x = (zplayer.position.x + Gdx.graphics.getWidth()/2);
		playerpos.y = (zplayer.position.y + Gdx.graphics.getHeight()/2+zplayer.height*32);
        
        
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
		
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			//aimVec.x = (Gdx.input.getX() * 0.1f + (Gdx.graphics.getWidth()/2));// - zplayer.position.x;
			//aimVec.y = (Gdx.input.getY() * 0.1f + (Gdx.graphics.getHeight()/2));// - zplayer.position.y;
			//zplayer.shoot();
			aimVec.x = (Gdx.input.getX() - Gdx.graphics.getWidth()/2 - 9 - 5);
			aimVec.y = (-(Gdx.input.getY() - Gdx.graphics.getHeight()/2+zplayer.height*32 - 8 -10));
			System.out.print(" Angle:");
			System.out.print(aimVec.angle());
			System.out.print(" ::");
			Zbullet bullet = new Zbullet();
			float an = aimVec.angle();
			bullets.add(bullet);
			bullet.create(collisionLayer, playerpos, an);
			
			//Gdx.graphics.getWidth()/2-width*32,Gdx.graphics.getHeight()/2
	     }
		
		
		
		if(Gdx.input.isTouched()){
			System.out.print("X:");
			System.out.print(Gdx.input.getX() - Gdx.graphics.getWidth()/2);
			aimVec.x = (Gdx.input.getX() - Gdx.graphics.getWidth()/2);
			aimVec.y = (-(Gdx.input.getY() - Gdx.graphics.getHeight()/2+zplayer.height*32));
			System.out.print("    Y:");
			System.out.print(-(Gdx.input.getY() - Gdx.graphics.getHeight()/2 +zplayer.height*32));
			
			System.out.print(" Angle:");
			System.out.print(aimVec.angle());
			System.out.println();
			//if (aimVec.x == 0){
				//a
				
			//}
			//Gdx.graphics.getWidth()/2-width*32,Gdx.graphics.getHeight()/2
		}
		
		physics.doPhysics(zplayer);
		
	//	playersprite.setPosition(zplayer.position.x,zplayer.position.y);
		
		renderer.setView(camera);
		camera.update();
		
		renderer.render();
		
		camera.position.set(zplayer.position.x,zplayer.position.y,0);
		
		batch.begin();
		dpadsprite.draw(batch);
	    for (Zbullet zb : bullets){
	    	zb.update();
		}
	  /*  while(itzbullet.hasNext()){
	    	Zbullet ib = itzbullet.next();
	    	if(ib.distance > 0){
	    		itzbullet.remove();
	    	}

	    }*/
	    
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