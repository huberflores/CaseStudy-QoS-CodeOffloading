package com.codeoff.casestudy.scenes;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import com.codeoff.casestudy.ResourceManager;

public class ScoreScene extends BaseScene implements IOnMenuItemClickListener {
	private MenuScene score_scene;
	private final int MENU_BACK = 0;
	private String ghostsKilled;
	private String knightsKilled;

	public ScoreScene(String ghostsKilled, String knightsKilled) {
		this.ghostsKilled = ghostsKilled;
		this.knightsKilled = knightsKilled;
	}
	
	@Override
	public void createScene() {
		createScore();
	}

	@Override
	public void onBackPressed() {
		System.exit(0);
	}

	@Override
	public void disposeScene() {
		score_scene.detachChildren();
		score_scene.clearTouchAreas();
		score_scene.detachSelf();
		score_scene.dispose();
		ResourceManager.getInstance().unloadScoreResources();
	}
	
	public void createScore() {
		score_scene = new MenuScene(camera);
		score_scene.setPosition(0, 0);
		Text ghostsKilledText = new Text(camera.getWidth() / 2 + 25, 180, ResourceManager.getInstance().font, ghostsKilled, vbom);
		Text knightsKilledText = new Text(camera.getWidth() / 2 + 25, 290, ResourceManager.getInstance().font, knightsKilled, vbom);
		final IMenuItem backItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_BACK, ResourceManager.getInstance().back_button, vbom), 1.05f, 1f);
		Sprite scoreBoard = new Sprite(0, 0, ResourceManager.getInstance().scoreboard, vbom);
		score_scene.attachChild(scoreBoard);
		score_scene.addMenuItem(backItem);
		backItem.setPosition(camera.getWidth() / 2 - (backItem.getWidth() / 2), camera.getHeight() - backItem.getHeight() - 25);
		score_scene.attachChild(ghostsKilledText);
		score_scene.attachChild(knightsKilledText);
		score_scene.setBackgroundEnabled(false);
				
		score_scene.setOnMenuItemClickListener(this);
		setChildScene(score_scene);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene scene, IMenuItem item, float localX, float localY) {
		if (item.getID() == 0) {
			SceneManager.getInstance().setMenuScene();
			return true;
		} else {
			return false;
		}
	}

}
