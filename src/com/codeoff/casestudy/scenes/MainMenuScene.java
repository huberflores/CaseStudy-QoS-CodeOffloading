package com.codeoff.casestudy.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.util.color.Color;

import com.codeoff.casestudy.ResourceManager;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener {

	private MenuScene menu;
	private final int MENU_PLAY = 0;
	private final int MENU_EXIT = 1;
	
	@Override
	public void createScene() {
		setBackground(new Background(Color.WHITE));
		createMenu();
	}

	@Override
	public void onBackPressed() {
		System.exit(0);
	}

	@Override
	public void disposeScene() {
		ResourceManager.getInstance().unloadMenuResources();
	}
	
	private void createMenu() {
		menu = new MenuScene(camera);
		menu.setPosition(0, 0);
		final IMenuItem playItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, ResourceManager.getInstance().play_button_region, vbom), 1.05f, 1f);
		final IMenuItem exitItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_EXIT, ResourceManager.getInstance().exit_button_region, vbom), 1.05f, 1f);		
		menu.addMenuItem(playItem);
		menu.addMenuItem(exitItem);
		menu.buildAnimations();
		menu.setBackgroundEnabled(false);
		playItem.setPosition(playItem.getX(), playItem.getY() - 20);
		exitItem.setPosition(exitItem.getX(), exitItem.getY());		
		menu.setOnMenuItemClickListener(this);
		setChildScene(menu);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene scene, IMenuItem item, float localX, float localY) {
		switch(item.getID()) {
		case MENU_PLAY:
			SceneManager.getInstance().setGameScene(1,0,0,0);
//			SceneManager.getInstance().setGameScene(2,100,0,0);
			return true;
		case MENU_EXIT:
			System.exit(0);
			return true;
			
		}
		return false;
	}
	
}
