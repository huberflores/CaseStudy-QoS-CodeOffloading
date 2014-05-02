package com.codeoff.casestudy.tile;

import java.util.ArrayList;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.codeoff.casestudy.ResourceManager;

public class TileManager {
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	
	public TileManager(VertexBufferObjectManager vbom) {
		tiles.add(new Tile("Grass", 1, 0, 0, ResourceManager.getInstance().grass.getWidth(), ResourceManager.getInstance().grass.getHeight(), 5f, 0f, 0.5f, ResourceManager.getInstance().grass, vbom));
		tiles.add(new Tile("Grass Box", 2, 0, 0, ResourceManager.getInstance().grass_box.getWidth(), ResourceManager.getInstance().grass_box.getHeight(), 5f, 0f, 0.5f, ResourceManager.getInstance().grass_box, vbom));
		tiles.add(new Tile("Grass Box Wide", 3, 0, 0, ResourceManager.getInstance().grass_box_wide.getWidth(), ResourceManager.getInstance().grass_box_wide.getHeight(), 5f, 0f, 0.5f, ResourceManager.getInstance().grass_box_wide, vbom));
		tiles.add(new Tile("Entrance", 4, 0, 0, ResourceManager.getInstance().entrance.getWidth(), ResourceManager.getInstance().entrance.getHeight(), 5f, 0f, 0.5f, ResourceManager.getInstance().entrance, vbom));
		tiles.add(new Tile("Snow", 5, 0, 0, ResourceManager.getInstance().snow.getWidth(), ResourceManager.getInstance().snow.getHeight(), 5f, 0f, 1f, ResourceManager.getInstance().snow, vbom));
		tiles.add(new Tile("Snow spikes", 6, 0, 0, ResourceManager.getInstance().snow_spikes.getWidth(), ResourceManager.getInstance().snow_spikes.getHeight(), 5f, 0f, 1f, ResourceManager.getInstance().snow_spikes, vbom));
	}
	
	public Tile getTileById(int id) {
		for (Tile t : tiles) {
			if (t.getId() == id) {
				return t;
			}
		}
		return null;
	}
}
