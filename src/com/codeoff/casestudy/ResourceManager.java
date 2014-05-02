package com.codeoff.casestudy;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import com.codeoff.casestudy.tile.TileManager;

public class ResourceManager {
	
	private static final ResourceManager INSTANCE = new ResourceManager();
	public Engine engine;
	public GameActivity activity;
	public SmoothCamera camera;
	public VertexBufferObjectManager vbom;
	
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	public ITextureRegion play_button_region;
	public ITextureRegion exit_button_region;
	
	private BuildableBitmapTextureAtlas gameTextureAtlas;
	public ITiledTextureRegion player_region;
	public ITextureRegion spell_region;
	public ITextureRegion control_left_region;
	public ITextureRegion control_right_region;
	public ITextureRegion control_jump_region;
	public ITextureRegion healthbar_region;

	private BuildableBitmapTextureAtlas tileTextureAtlas;
	public ITiledTextureRegion grass;
	public ITiledTextureRegion grass_box_wide;
	public ITiledTextureRegion grass_box;
	public ITiledTextureRegion entrance;
	public ITiledTextureRegion snow;
	public ITiledTextureRegion snow_spikes;
	private BuildableBitmapTextureAtlas backgroundTextureAtlas;
	public ITextureRegion mountain;
	public ITextureRegion trees;
	public ITextureRegion ground;
	public ITextureRegion snowymountain;
	public ITextureRegion ice;
	public ITextureRegion night;
	
	private BuildableBitmapTextureAtlas monsterTextureAtlas;
	public ITiledTextureRegion ghost_region;
	public ITiledTextureRegion knight_region;
	
	private BuildableBitmapTextureAtlas scoreTextureAtlas;
	public ITextureRegion scoreboard;
	public ITextureRegion back_button;
	
//	private BitmapTextureAtlas grassTextureAtlas;
//	public ITextureRegion grass2_region;
	
	public TileManager tileManager;
	
	public Font font;
	public Font font_small;
	
	
	public void loadMenuResources() {
		loadFonts();
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		play_button_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play_button.png");
		exit_button_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "exit_button.png");
		
		try {
			menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,1,0));
			menuTextureAtlas.load();
		} catch (Exception e) {
			Debug.e(e);
		}
	}
	
	public void unloadMenuResources() {
		menuTextureAtlas.unload();
		menuTextureAtlas = null;
	}
	
	public void loadGameResource() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "wizard.png", 8, 1);
		spell_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "spell.png");
		control_left_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "left_button.png");
		control_right_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "right_button.png");
		control_jump_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "jump_button.png");
		healthbar_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "healthbar.png");
		backgroundTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 2048, TextureOptions.BILINEAR);
		mountain = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundTextureAtlas, activity, "mountain.png");
		trees = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundTextureAtlas, activity, "trees.png");
		ground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundTextureAtlas, activity, "ground.png");
		snowymountain = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundTextureAtlas, activity, "snowymountains.png");
		ice = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundTextureAtlas, activity, "ice.png");
		night = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundTextureAtlas, activity, "night.png");
		monsterTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		ghost_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(monsterTextureAtlas, activity, "ghost.png", 5, 2);
		knight_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(monsterTextureAtlas, activity, "knight.png", 8, 2);
		try {
			gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,1,0));
			gameTextureAtlas.load();
			backgroundTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,1,0));
			backgroundTextureAtlas.load();
			monsterTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,1,0));
			monsterTextureAtlas.load();
		} catch (Exception e) {
			Debug.e(e);
		}
	}
	
	public void unloadGameResources() {
		gameTextureAtlas.unload();
		gameTextureAtlas = null;
	}
	
	public void loadTileResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/tiles/");
		tileTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 1024, TextureOptions.BILINEAR);
//		grass_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tileTextureAtlas, activity, "grass.png"); 
//		grass_platform_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tileTextureAtlas, activity, "grass_platform.png");
		grass = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(tileTextureAtlas, activity, "grass_large.png", 1, 1);
		grass_box = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(tileTextureAtlas, activity, "grass_box.png", 1, 1);
		grass_box_wide = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(tileTextureAtlas, activity, "grass_box_wide.png", 1, 1);
		entrance = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(tileTextureAtlas, activity, "entrances.png", 2, 1);
		snow = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(tileTextureAtlas, activity, "snow_large.png", 1, 1);
		snow_spikes = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(tileTextureAtlas, activity, "snow_spikes.png", 1, 1);
		try {
			tileTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,1,0));
			tileTextureAtlas.load();
		} catch (Exception e) {
			Debug.e(e);
		}
	}
	
	
	public void loadFonts() {
		FontFactory.setAssetBasePath("fonts/");
		font = FontFactory.createFromAsset(activity.getFontManager(), activity.getTextureManager(), 256, 256, activity.getAssets(), "Roboto_Regular.ttf", 46, true, android.graphics.Color.WHITE);
		font_small = FontFactory.createFromAsset(activity.getFontManager(), activity.getTextureManager(), 256, 256, activity.getAssets(), "Roboto_Regular.ttf", 20, true, android.graphics.Color.WHITE);
		font.load();
		font_small.load();
	}
	
	public void loadTileManager() {
		loadTileResources();
		tileManager = new TileManager(vbom);
	}
	
	public static void prepareManager(Engine engine, GameActivity activity, SmoothCamera camera, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
	}
	
	public static ResourceManager getInstance() {
		return INSTANCE;
	}

	public void loadScoreResource() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		scoreTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		scoreboard = BitmapTextureAtlasTextureRegionFactory.createFromAsset(scoreTextureAtlas, activity, "score.png");
		back_button = BitmapTextureAtlasTextureRegionFactory.createFromAsset(scoreTextureAtlas, activity, "back_button.png");
		try {
			scoreTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,1,0));
			scoreTextureAtlas.load();
		} catch (Exception e) {
			Debug.e(e);
		}
	}
	
	public void unloadScoreResources() {
		scoreTextureAtlas.unload();
		scoreTextureAtlas = null;
	}
	
	
}
