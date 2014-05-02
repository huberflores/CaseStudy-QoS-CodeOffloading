package com.codeoff.casestudy.scenes;

import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import android.util.Log;

import com.codeoff.casestudy.ResourceManager;

public class SceneManager {

	private BaseScene menuScene;
	private BaseScene gameScene;
	private BaseScene scoreScene;
	
	private static final SceneManager INSTANCE = new SceneManager();
	
	private BaseScene currentScene;
	private Engine engine = ResourceManager.getInstance().engine;
	
	public enum SceneType {
		SCENE_MENU,
		SCENE_GAME,
		SCENE_SCORE
	}
	
	public void setMenuScene(OnCreateSceneCallback cb) {
		menuScene = new MainMenuScene();
		setScene(menuScene);
		ResourceManager.getInstance().loadMenuResources();
		currentScene.createScene();
		cb.onCreateSceneFinished(menuScene);
	}
	
	public void setMenuScene() {
		menuScene = new MainMenuScene();
		setScene(menuScene);
		ResourceManager.getInstance().loadMenuResources();
		currentScene.createScene();
	}
	
	public static SceneManager getInstance() {
		return INSTANCE;
	}
	
	public void setScene(BaseScene scene) {
		if (currentScene != null) {
			Log.i("setScene", "dispose");
			currentScene.disposeScene();
		}
		engine.setScene(scene);
		currentScene = scene;
	}
	
	public void setGameScene(int levelID, float currentHealth, int ghostsKilled, int knightsKilled) {
		gameScene = new GameScene(levelID, currentHealth, ghostsKilled, knightsKilled);
		setScene(gameScene);
		ResourceManager.getInstance().loadGameResource();
		ResourceManager.getInstance().loadTileManager();
		currentScene.createScene();
	}
	
	public void setScoreScene(String ghostsKilled, String knightsKilled) {
		scoreScene = new ScoreScene(ghostsKilled, knightsKilled);
		setScene(scoreScene);
		ResourceManager.getInstance().loadScoreResource();
		currentScene.createScene();
	}
	
	public void setScene(SceneType type) {
		switch (type) {
		case SCENE_MENU:
			setScene(menuScene);
			break;
		case SCENE_GAME:
			setScene(gameScene);
			break;
		case SCENE_SCORE:
			setScene(scoreScene);
			break;
		}
	}
	
	public BaseScene getCurrentScene() {
		return currentScene;
	}
	
}
