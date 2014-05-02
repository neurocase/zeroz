package com.dazpetty.zeroz.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MainMenu implements Screen {

  final ZerozGame game;

    OrthographicCamera camera;


	
public MainMenu(ZerozGame gam) {
	game = gam;

    camera = new OrthographicCamera();
    camera.setToOrtho(false, 800, 480);
	}


@Override
public void render(float delta) {
    Gdx.gl.glClearColor(0.2f, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    camera.update();
    game.batch.setProjectionMatrix(camera.combined);

    game.batch.begin();
    
    game.font.draw(game.batch, "ZERO-Z ", 100, 300);
    
    game.font.draw(game.batch, "Daryl Petty; www.dazpetty.com", 100, 150);
    game.font.draw(game.batch, "Tap to Begin", 100, 100);
    game.batch.end();

    if (Gdx.input.isTouched()) {
        game.setScreen(new GameScene(game));
        dispose();
    }
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

    //Rest of class still omitted...

}