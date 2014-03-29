package com.dazpetty.zeroz;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Zplayer extends Zactor {

	public void create() {
	

	}

	@Override
	public void initActor(TiledMapTileLayer cLayer, Vector2 actorstart) {
		worldpos = actorstart;
		collisionLayer = cLayer;
		initialized = true;
	}
	@Override
	public void dispose() {
	//	runTextureAtlas.dispose();
	//	idleTextureAtlas.dispose();
	//	backWalkTextureAtlas.dispose();
		rightArmTexture.dispose();
		leftArmTexture.dispose();
	}
}

/*
 * is going right if facing enemy hasTarget
 */

