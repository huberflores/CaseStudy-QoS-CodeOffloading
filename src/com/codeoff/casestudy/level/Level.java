package com.codeoff.casestudy.level;

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.codeoff.casestudy.tile.Tile;

public class Level {
	private final int id;
	private final ArrayList<Tile> levelTiles = new ArrayList<Tile>();
	
	public ArrayList<Tile> getLevelTiles() {
		return levelTiles;
	}

	public Level(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void addTile(Tile t) {
		levelTiles.add(t);
	}
	
	public void load(Scene scene, PhysicsWorld physicsWorld) {
		for (Tile t : levelTiles) {
			t.createBodyAndAttach(scene, physicsWorld);
		}
	}
}
