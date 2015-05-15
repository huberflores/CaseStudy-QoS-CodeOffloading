package com.codeoff.casestudy.level;

import java.io.IOException;
import java.util.ArrayList;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.SAXUtils;
import org.andengine.util.color.Color
import org.andengine.util.level.IEntityLoader;
import org.andengine.util.level.LevelLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.xml.sax.Attributes;

import android.content.res.AssetManager;
import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.codeoff.casestudy.ResourceManager;
import com.codeoff.casestudy.tile.Tile;

public class LevelManager {
	private final LevelLoader levelLoader;
	private final AssetManager assetManager;
	private final ArrayList<Level> levels = new ArrayList<Level>();
	
	private static final String TAG_TILE = "tile";
	private static final String TAG_TILE_ATTRIBUTE_X = "x";
	private static final String TAG_TILE_ATTRIBUTE_Y = "y";
	private static final String TAG_TILE_ATTRIBUTE_TILE = "tile";
	private static final String TAG_FLOOR = "floor";
	private static final String TAG_FLOOR_TYPE = "type";
	private static final String TAG_FLOOR_ATTRIBUTE_X = "x";
	private static final String TAG_FLOOR_ATTRIBUTE_Y = "y";
	private static final String TAG_FLOOR_ATTRIBUTE_HEIGHT = "height";
	private static final String TAG_FLOOR_ATTRIBUTE_WIDTH = "width";
	
	public LevelManager(AssetManager assetManager, int i, Scene scene, PhysicsWorld physicsWorld, VertexBufferObjectManager vbom) {
		levelLoader = new LevelLoader();
		levelLoader.setAssetBasePath("levels/");
		this.assetManager = assetManager;
		addNewLevel(i, "level"+i+".lvl", scene, physicsWorld, vbom);
	}
	
	public ArrayList<Level> getLevels() {
		return levels;
	}
	
	private void addNewLevel(int id, String name, final Scene scene, final PhysicsWorld physicsWorld, final VertexBufferObjectManager vbom) {
		final Level level = new Level(id);
		
		levelLoader.registerEntityLoader(LevelConstants.TAG_LEVEL, new IEntityLoader() {
			
			@Override
			public IEntity onLoadEntity(String name, Attributes attribute) {
				final int width = SAXUtils.getIntAttributeOrThrow(attribute, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
				final int height = SAXUtils.getIntAttributeOrThrow(attribute, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
				SmoothCamera camera = ResourceManager.getInstance().camera;
				camera.setCenterDirect(0,0);
				camera.setBounds(0f, 250.0f, width, -height);
				camera.setBoundsEnabled(true);
				return null;
			}
		});
		
		levelLoader.registerEntityLoader(TAG_TILE, new IEntityLoader() {
			
			@Override
			public IEntity onLoadEntity(String name, Attributes attribute) {
				final int x = SAXUtils.getIntAttributeOrThrow(attribute, TAG_TILE_ATTRIBUTE_X);
				final int y = SAXUtils.getIntAttributeOrThrow(attribute, TAG_TILE_ATTRIBUTE_Y);
				final int id = SAXUtils.getIntAttributeOrThrow(attribute, TAG_TILE_ATTRIBUTE_TILE);
				Tile t = ResourceManager.getInstance().tileManager.getTileById(id);
				level.addTile(t.getInstance(x, y));
				return null;
			}
		});

		levelLoader.registerEntityLoader(TAG_FLOOR, new IEntityLoader() {
			
			@Override
			public IEntity onLoadEntity(String name, Attributes attribute) {
				Rectangle rect = new Rectangle(SAXUtils.getIntAttributeOrThrow(attribute, TAG_FLOOR_ATTRIBUTE_X), SAXUtils.getIntAttributeOrThrow(attribute, TAG_FLOOR_ATTRIBUTE_Y), SAXUtils.getIntAttributeOrThrow(attribute, TAG_FLOOR_ATTRIBUTE_WIDTH), SAXUtils.getIntAttributeOrThrow(attribute, TAG_FLOOR_ATTRIBUTE_HEIGHT), vbom);
				rect.setColor(Color.TRANSPARENT);
				Body rectBody = PhysicsFactory.createBoxBody(physicsWorld, rect, BodyType.StaticBody, PhysicsFactory.createFixtureDef(5f, 0f, 0.5f));
				rectBody.setUserData(SAXUtils.getAttribute(attribute, TAG_FLOOR_TYPE, "Grass"));
				scene.attachChild(rect);
				return null;
			}
		});
		
		try {
			levelLoader.loadLevelFromAsset(assetManager, name);
		} catch (IOException e) {}
		levels.add(level);
	}
	
	public void loadLevel(Scene scene, PhysicsWorld physicsWorld) {
		for (Level level : levels) {
			level.load(scene, physicsWorld);
		}
	}
}
