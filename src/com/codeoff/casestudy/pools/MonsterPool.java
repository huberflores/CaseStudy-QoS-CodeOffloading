package com.codeoff.casestudy.pools;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.pool.GenericPool;

import com.codeoff.casestudy.ResourceManager;

public class MonsterPool extends GenericPool<AnimatedSprite> {
	private ITiledTextureRegion monsterTextureRegion;
	private AnimatedSprite monsterSprite;

	public MonsterPool(ITiledTextureRegion enemy_region) {
		monsterTextureRegion = enemy_region;
    }
	
	@Override
	protected AnimatedSprite onAllocatePoolItem() {
		monsterSprite = new AnimatedSprite(0,0, monsterTextureRegion.deepCopy(), ResourceManager.getInstance().vbom) {
			@Override
			protected void preDraw(GLState glState, Camera camera) {
				super.preDraw(glState, camera);
				glState.enableDither();
			}
		};
		return monsterSprite; 
	}
	
	protected void onHandleRecycleItem(final AnimatedSprite monster) {
		monster.clearEntityModifiers();
		monster.clearUpdateHandlers();
		monster.setVisible(false);
		monster.detachSelf();
		monster.reset();
    }

}