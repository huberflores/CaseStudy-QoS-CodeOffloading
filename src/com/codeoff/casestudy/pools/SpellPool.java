package com.codeoff.casestudy.pools;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.pool.GenericPool;

import com.codeoff.casestudy.ResourceManager;

public class SpellPool extends GenericPool<Sprite> {
	private ITextureRegion spellTextureRegion;
	private Sprite spellSprite;

	public SpellPool(ITextureRegion spell_region) {
	        spellTextureRegion = spell_region;
    }
	
	@Override
	protected Sprite onAllocatePoolItem() {
		spellSprite = new Sprite(0,0, spellTextureRegion.deepCopy(), ResourceManager.getInstance().vbom) {
			@Override
			protected void preDraw(GLState glState, Camera camera) {
				super.preDraw(glState, camera);
				glState.enableDither();
			}
		};
		return spellSprite; 
	}
	
	protected void onHandleRecycleItem(final Sprite spell) {
		spell.clearEntityModifiers();
		spell.clearUpdateHandlers();
		spell.setVisible(false);
		spell.detachSelf();
		spell.reset();
    }

}
