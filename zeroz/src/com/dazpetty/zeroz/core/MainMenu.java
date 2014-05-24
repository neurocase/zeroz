package com.dazpetty.zeroz.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenu implements Screen {

	final ZerozGame game;

	OrthographicCamera camera;

	public Texture newGameTexture;
	public TextureRegion newGameTextureReg;
	public Sprite newGameSprite;
	
	public Texture continueTexture;
	public TextureRegion continueTextureReg;
	public Sprite continueSprite;
	public static final Skin skin = new Skin(Gdx.files.internal("data/gfx/skin/uiskin.json"));
	
	public int height = Gdx.graphics.getWidth();
	public int width = Gdx.graphics.getHeight();
	
	float xbuttontouchrange = 200;
	float ybuttontouchrange = 50;
	float offsetx = -100;
	float offsety = -30;
	private Stage stage;
	private Stage lsstage;
	

	
	public MainMenu(ZerozGame gam) {
		
		lsstage = new Stage();
		stage = new Stage();
		game = gam;

	
		createMainButtons();
	
		
		

		
		Gdx.input.setInputProcessor(stage);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, height, width);

		newGameTexture = new Texture(("data/gfx/mainmenu/title.png"));
		TextureRegion newGameTextureReg = new TextureRegion(newGameTexture, 0,
				0, 2048, 512);

		newGameSprite = new Sprite(newGameTextureReg);
		
		float imageSize = Gdx.graphics.getWidth()*0.8f;
		newGameSprite.setSize(imageSize, imageSize/4);
		
		
		continueTexture = new Texture(("data/gfx/background/cityp1.png"));
		TextureRegion continueTextureReg = new TextureRegion(continueTexture, 0,
				0, 1024, 512);

		continueSprite = new Sprite(continueTextureReg);
		imageSize = Gdx.graphics.getWidth();
		continueSprite.setSize(imageSize, imageSize/2);
		//newGameSprite.setOrigin(newGameSprite.getWidth()/2, newGameSprite.getHeight()/2);
	}

	public void print(String str) {
		System.out.println(str);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.075f, 0.0f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();

		//print("Camera is :" + camera.position.x + " - " + camera.position.y);

		game.font.draw(game.batch, "ZERO-Z ", 100, 300);

		game.font.draw(game.batch, "Daryl Petty; www.dazpetty.com", 100, 150);
		game.font.draw(game.batch, "Tap to Begin", 100, 100);

		
		//y top is high
		continueSprite.setPosition(0, 0);
		continueSprite.draw(game.batch);
	
		//newGameSprite.setOrigin(0, newGameSprite.getHeight());
		newGameSprite.setPosition((Gdx.graphics.getWidth()/2 - (newGameSprite.getWidth()/2) )+ Gdx.graphics.getWidth()/17 , (Gdx.graphics.getHeight()-newGameSprite.getHeight()));
		newGameSprite.draw(game.batch);
		
	
		game.batch.end();
	
		if (levelSelect){
			createLevelSelectButtons();
			lsstage.draw();
		}else{
			stage.draw();
		}
		
	
	}

	public void createMainButtons(){
		
		float buttonWidth =  Gdx.graphics.getWidth()/4;
		float buttonHeight =  Gdx.graphics.getHeight()/10;
		
		TextButton button = new TextButton("New Game", skin, "default");
		button.setWidth(buttonWidth);
		button.setHeight(buttonHeight);
		button.setPosition(Gdx.graphics.getWidth()/2 - (buttonWidth/2), Gdx.graphics.getHeight()/2);
		
		button.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
               
                game.setScreen(new GameScene(game));
        		dispose();
            }
        });
		
		stage.addActor(button);
		
		button = new TextButton("Continue", skin, "default");
		button.setWidth(buttonWidth);
		button.setHeight(buttonHeight);
		button.setPosition(Gdx.graphics.getWidth()/2 - (buttonWidth/2), (float) (Gdx.graphics.getHeight()/2 - (buttonHeight*1.5)));
		
		button.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
               
               // game.setScreen(new GameScene(game));
        	//	dispose();
            }
        });
		stage.addActor(button);
		
		button = new TextButton("Level Select", skin, "default");
		button.setWidth(buttonWidth);
		button.setHeight(buttonHeight);
		button.setPosition(Gdx.graphics.getWidth()/2 - (buttonWidth/2), (float) (Gdx.graphics.getHeight()/2 - (buttonHeight*3)));
		
		button.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	levelSelect = true;
               
         
            }
        });
		
		stage.addActor(button);

		
	}
	public boolean levelSelect = false;
	
	public final int[] level = new int[]{0,1,2,3,4,5,6,7,8,9};
	public void createLevelSelectButtons(){
		//final int level[] = 0;
		for (int i = 0; i < game.TOTAL_LEVELS; i++){
			level[i] = i;
			float buttonWidth =  Gdx.graphics.getWidth()/10;
			float buttonHeight =  Gdx.graphics.getHeight()/10;
			
			TextButton lsbutton = new TextButton(""+i, skin, "default");
			lsbutton.setWidth(buttonWidth);
			lsbutton.setHeight(buttonHeight);
			lsbutton.setPosition(0 + buttonWidth + buttonWidth * i, Gdx.graphics.getHeight()/2);
			
			
			lsbutton.addListener(new ClickListener(){
	            @Override 
	            public void clicked(InputEvent event, float x, float y){
	                //game.setLevel(level[i]);
	             //   game.setScreen(new GameScene(game));
	        		//dispose();
	            	print("level select doesn't work");
	            }
	        });
			
			
			
			
			lsstage.addActor(lsbutton);
		}
	}
	

	

	@Override
	public void resize(int width, int height) {
		 stage.setViewport(width, height);
		 camera.setToOrtho(false, width, height);

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

	}

	// Rest of class still omitted...

}