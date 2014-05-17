package com.dazpetty.zeroz.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MainMenu implements Screen {

	final ZerozGame game;

	OrthographicCamera camera;

	public Texture newGameTexture;
	public TextureRegion newGameTextureReg;
	public Sprite newGameSprite;
	
	public Texture continueTexture;
	public TextureRegion continueTextureReg;
	public Sprite continueSprite;

	public MainMenu(ZerozGame gam) {
		game = gam;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		newGameTexture = new Texture(("data/gfx/mainmenu/newgame.png"));
		TextureRegion newGameTextureReg = new TextureRegion(newGameTexture, 0,
				0, 512, 128);

		newGameSprite = new Sprite(newGameTextureReg);
		newGameSprite.setSize(256f, 64f);
		
		
		continueTexture = new Texture(("data/gfx/mainmenu/continue.png"));
		TextureRegion continueTextureReg = new TextureRegion(continueTexture, 0,
				0, 512, 128);

		continueSprite = new Sprite(continueTextureReg);
		continueSprite.setSize(256f, 64f);
		//newGameSprite.setOrigin(newGameSprite.getWidth()/2, newGameSprite.getHeight()/2);
	}

	public void print(String str) {
		System.out.println(str);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.06f, 0f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();

		//print("Camera is :" + camera.position.x + " - " + camera.position.y);

		game.font.draw(game.batch, "ZERO-Z ", 100, 300);

		game.font.draw(game.batch, "Daryl Petty; www.dazpetty.com", 100, 150);
		game.font.draw(game.batch, "Tap to Begin", 100, 100);

		
		//y top is high
		newGameSprite.setPosition(400, 240);
		newGameSprite.draw(game.batch);
		
		continueSprite.setPosition(400, 190);
		continueSprite.draw(game.batch);
		
		game.batch.end();
		
		if (Gdx.input.isTouched()) {
			
			
			Vector3 testv = new Vector3 (Gdx.input.getX(), Gdx.input.getY(), 0);
			//print("regular x:" + testv.x + " y:" + testv.y);
			//camera.unproject(testv);
			//print("unprojected x:" + testv.x + " y:" + testv.y);
			camera.project(testv);
			print("project x:" + testv.x + " y:" + testv.y);
			isTouchMenuButton(testv.x, testv.y);
		}
	}

	float xbuttontouchrange = 200;
	float ybuttontouchrange = 50;
	float offsetx = -100;
	float offsety = -30;
	

	public void isTouchMenuButton(float x, float y) {
		//Vector3 touchPos = new Vector3(x,
	//			y, 0);
	//	camera.unproject(touchPos);
		//print("Touch:" + x + "," + y);
		//x = -x;
		//y = -y;
		Stage stage;
		
		Vector3 toucharea = new Vector3 (newGameSprite.getX()-offsetx, newGameSprite.getY()-offsety, 0);
		camera.project(toucharea);
		
		print("toucharea: " + toucharea.x + "," + toucharea.y);
		
		if (Math.abs(x - (toucharea.x)) < xbuttontouchrange
				&& Math.abs(y - toucharea.y) < ybuttontouchrange) {
			print("TTTTTT" + (Math.abs(x - (toucharea.x-offsetx)) + ","
				+ Math.abs(y - toucharea.y-offsety)));
			print("NEW GAME");
			
			game.setScreen(new GameScene(game));
			dispose();
		}
		
		toucharea = new Vector3 (continueSprite.getX()-offsetx, continueSprite.getY()-offsety, 0);
		camera.project(toucharea);
		
		if (Math.abs(x - (toucharea.x)) < xbuttontouchrange
				&& Math.abs(y - toucharea.y) < ybuttontouchrange) {
			print("TTTTTT" + (Math.abs(x - (toucharea.x-offsetx)) + ","
				+ Math.abs(y - toucharea.y-offsety)));
			print("CONTINUE");
		}
		//	game.setScreen(new GameScene(game));
		//	dispose();
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

	}

	// Rest of class still omitted...

}