package com.codeoff.casestudy.pools;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.codeoff.casestudy.ResourceManager;

public class Monster {
	public Rectangle healthBar;
	public float maxHealth;
	public float currentHealth;
	public float healthBarWidth;
	public boolean isAlive;
	public AnimatedSprite monsterSprite;
	public Body monsterBody;
	public boolean attacking;
	
	public Monster(AnimatedSprite pAnimatedSprite, Body pBody, String name) {
		this.attacking = false;
		this.isAlive = true;
		this.monsterSprite = pAnimatedSprite;
		this.monsterBody = pBody;
		this.healthBarWidth = pAnimatedSprite.getWidth() - 20;
		if (name == "Ghost") {
			this.maxHealth = 100.0f;
			this.currentHealth = 100.0f;
			this.healthBar  = new Rectangle(10, -10, healthBarWidth, 10, ResourceManager.getInstance().vbom);
		} else if (name == "Knight") {
			this.maxHealth = 200.0f;
			this.currentHealth = 200.0f;
			this.healthBar  = new Rectangle(10, -5, healthBarWidth, 5, ResourceManager.getInstance().vbom);
		}
		healthBar.setColor(1, 0, 0);
		monsterSprite.attachChild(healthBar);
	}
	
	public void onHit(float damage) {
		float healthLeft = currentHealth - damage;
		if (healthLeft > 0.0f) {
			currentHealth = healthLeft;
			healthBar.setWidth((float) healthBarWidth * (healthLeft / maxHealth));
		} else if (healthLeft <= 0.0f) {
			healthBar.setWidth(0f);
			MonsterDead();
		}
	}

	private void MonsterDead() {
		isAlive = false;
		Log.i("Monster", "Monster dead");
		
	}

	public Body getMonsterBody() {
		return monsterBody;
	}

	public AnimatedSprite getMonsterSprite() {
		return monsterSprite;
	}
	
}
