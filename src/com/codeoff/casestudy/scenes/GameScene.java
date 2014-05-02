package com.codeoff.casestudy.scenes;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;

import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.codeoff.casestudy.CoolDown;
import com.codeoff.casestudy.GameActivity;
import com.codeoff.casestudy.MaxStepPhysicsWorld;
import com.codeoff.casestudy.ResourceManager;
import com.codeoff.casestudy.level.Level;
import com.codeoff.casestudy.level.LevelManager;
import com.codeoff.casestudy.pools.Monster;
import com.codeoff.casestudy.pools.MonsterPool;
import com.codeoff.casestudy.pools.SpellPool;
import com.codeoff.casestudy.tile.Tile;

public class GameScene extends BaseScene implements IOnSceneTouchListener {
	private HUD gameHud;
	private PhysicsWorld physicsWorld;
	private Body playerBody;
	private AnimatedSprite playerSprite;
	private static LevelManager levelManager;
	private static int levelID;
	private int groundContact = 0;
	private SpellPool spellPool;
	private MonsterPool ghostPool;
	private ArrayList<Monster> ghostList = new ArrayList<Monster>();
	private MonsterPool KnightPool;
	private ArrayList<Monster> knightList = new ArrayList<Monster>();
	private boolean spellShot = false;
	private float maxHealth = 100.0f;
	private float currentHealth;
	private Rectangle healthBar;
	private Sprite healthBarSprite;
	private float healthBarWidth;
	private boolean playerAlive = true;
	private float px_to_meter_def = PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	private int ghostsKilled;
	private int knightsKilled;
	private ArrayList<Tile> tiles;
	
	public GameScene(int lID, float currentHealth, int ghostsKilled, int knightsKilled) {
		levelID = lID;
		this.ghostsKilled = ghostsKilled;
		this.knightsKilled = knightsKilled;
		if (lID==1) {
			this.currentHealth = maxHealth;
		} else {
			this.currentHealth = currentHealth;
		}
	}

	@Override
	public void createScene() {
		camera.setCenter(GameActivity.getCameraWidth()/2, GameActivity.getCameraHeight()/2);
		levelManager = new LevelManager(activity.getAssets(), levelID);
		createHUD();
		createPhysics();
		addPlayer();
		createLevel();
		tiles = getLevelTiles();
		setBackground();
		spellPool = new SpellPool(ResourceManager.getInstance().spell_region);
		ghostPool = new MonsterPool(ResourceManager.getInstance().ghost_region);
		KnightPool = new MonsterPool(ResourceManager.getInstance().knight_region);
		addEnemies();
		camera.setChaseEntity(playerSprite);
		setOnSceneTouchListener(this);
		
//		See object outlines:
//		attachChild(new DebugRenderer(physicsWorld, vbom));		
	}

	

	@Override
	public void onBackPressed() {
		Toast.makeText(activity, "Back pressed", Toast.LENGTH_SHORT).show();
	    SceneManager.getInstance().setMenuScene();
	}

	@Override
	public void disposeScene() {
		disposeHUD();
		ResourceManager.getInstance().unloadGameResources();
	}
	
	private void setBackground()  {
		if (levelID == 1) {
			ParallaxBackground background = new ParallaxBackground(0, 0, 0);
			background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(0,0, ResourceManager.getInstance().mountain, vbom)));
			background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(0,200, ResourceManager.getInstance().ground, vbom)));
			background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(0,0, ResourceManager.getInstance().trees, vbom)));
			background.setColorEnabled(true);
			background.setColor(150/255.0f, 1.0f, 1.0f);
			setBackground(background);
////			setBackground(new Background(Color.CYAN));
//			ParallaxLayer parLayer = new ParallaxLayer(camera, true, (int) camera.getBoundsXMax());
////			Sprite mountain = new Sprite(0,-256, ResourceManager.getInstance().mountain, vbom);
//			Sprite trees = new Sprite(0,-350, ResourceManager.getInstance().trees, vbom);
//			Sprite ground = new Sprite(0,-150, ResourceManager.getInstance().ground, vbom);
////			parLayer.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(1, mountain, false, 1));
//			parLayer.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(1, ground, true));
//			parLayer.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(1, trees, true));
//			parLayer.setParallaxScrollFactor(0.1f);
//			parLayer.setZIndex(-100);
//			attachChild(parLayer);
//			sortChildren();
			
		} else {
			ParallaxBackground background = new ParallaxBackground(0, 0, 0);
			background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(0,0, ResourceManager.getInstance().night, vbom)));
			background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(0,250, ResourceManager.getInstance().ice, vbom)));
			background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(0,0, ResourceManager.getInstance().snowymountain, vbom)));
			background.setColorEnabled(true);
			background.setColor(150/255.0f, 1.0f, 1.0f);
			setBackground(background);
		}
	}
	
	private void createHUD() {
		gameHud = new HUD();
		createControls();
		createJumpControls();
		createHealthBar();
		camera.setHUD(gameHud);
	}
	
	private void disposeHUD() {
		gameHud.detachChildren();
		gameHud.clearTouchAreas();
		gameHud.detachSelf();
		gameHud.dispose();
	}

	private void createPhysics() {
		physicsWorld = new MaxStepPhysicsWorld(60, new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		registerUpdateHandler(physicsWorld);
		physicsWorld.setContactListener(createContactListener());
	}
	
	private void addPlayer() {
		
		final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(50.0f, 0.0f, 0.75f);
		playerSprite = new AnimatedSprite(100,-200, ResourceManager.getInstance().player_region, vbom) {
			@Override
			protected void preDraw(GLState glState, Camera camera) {
				super.preDraw(glState, camera);
				glState.enableDither();
			}
		};
		
//		final float halfWidth = playerSprite.getWidthScaled() * 0.5f / px_to_meter_def;
//		final float halfHeight = playerSprite.getHeightScaled() * 0.5f / px_to_meter_def;
//		final float top = -halfHeight;
//		final float bottom = halfHeight;
//		final float left = -halfWidth + 2.5f / px_to_meter_def;
//		final float right = halfWidth - 2.5f / px_to_meter_def;
//		final float left_l = -halfWidth + 10f / px_to_meter_def;
//		final float right_l = halfWidth - 10f / px_to_meter_def;
//		final float lower = bottom - 2.5f / px_to_meter_def;
		final float width = playerSprite.getWidthScaled() / px_to_meter_def;
		final float height = playerSprite.getHeightScaled() / px_to_meter_def;

		final Vector2[] vertices = {
//			new Vector2(right, top),
//			new Vector2(right, lower),
//			new Vector2(right_l, bottom),
//			new Vector2(left_l, bottom),
//			new Vector2(left, lower),
//			new Vector2(left, top)
			new Vector2(-0.45391f*width, -0.49277f*height),
			new Vector2(+0.43692f*width, -0.49277f*height),
			new Vector2(+0.43692f*width, +0.15023f*height),
			new Vector2(+0.18599f*width, +0.49183f*height),
			new Vector2(-0.18599f*width, +0.49183f*height),
			new Vector2(-0.45391f*width, +0.14521f*height)
		};		
		playerBody = PhysicsFactory.createPolygonBody(physicsWorld, playerSprite, vertices, BodyType.DynamicBody, playerFixtureDef);

//		playerBody = PhysicsFactory.createBoxBody(physicsWorld, playerSprite, BodyType.DynamicBody, playerFixtureDef);

		playerBody.setUserData("Player");
		playerBody.setFixedRotation(true);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(playerSprite, playerBody, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				if (-playerSprite.getY() < 0f && playerAlive) {
					playerDead();
				} else if (currentHealth > 0) {
					healthBar.setWidth((float) healthBarWidth * (currentHealth / maxHealth));
				} else if (currentHealth <= 0) {
					healthBar.setWidth(0f);
					playerDead();
				}
				if (knightList.size() == 0 && ghostList.size() == 0) {
					for (Tile t : tiles) {
						if (t.getName() == "Entrance") {
							t.setCurrentTileIndex(1);
						}
					}
				}
			}
		});
		attachChild(playerSprite);
		sortChildren();
	}
	
	private void playerDead() {
		playerAlive = false;
		camera.setChaseEntity(null);
		SceneManager.getInstance().setScoreScene(String.valueOf(ghostsKilled), String.valueOf(knightsKilled));
//		Text gameOver = new Text(camera.getCenterX(), camera.getCenterY(), ResourceManager.getInstance().font, "Ded", vbom); // text offset
//		attachChild(gameOver);
	}
	
	private void createLevel() {
		levelManager.loadLevel(this, physicsWorld);
	}
	
	public static ArrayList<Tile> getLevelTiles() {
		Level level = null;
		ArrayList<Level> levels = levelManager.getLevels();
		for (Level l : levels) {
			if (l.getId() == levelID) {
				level = l;
			}
		}
		return level.getLevelTiles();
	}
	
	private void createControls() {
		Sprite left = new Sprite(25, camera.getHeight() - (ResourceManager.getInstance().control_left_region.getHeight() + 25), ResourceManager.getInstance().control_left_region, vbom) {
			
			@Override
			public boolean onAreaTouched(final TouchEvent event, final float x, final float y) {
				if (groundContact >= 1) {
					playerBody.setLinearVelocity(-4.0f, playerBody.getLinearVelocity().y);
				} else if (groundContact < 1) {
					playerSprite.stopAnimation(0);
				}
				if ((!playerSprite.isAnimationRunning() || !playerSprite.isFlippedHorizontal()) && (groundContact >= 1)) {
					playerSprite.setFlippedHorizontal(true);
					playerSprite.animate(100);
				}
				if ((event.getAction() == TouchEvent.ACTION_UP) && playerSprite.isAnimationRunning()) {
					int nextTile = playerSprite.getCurrentTileIndex() + 1;
					if ((playerSprite.getCurrentTileIndex() == 7) || (nextTile == 7)) {
						nextTile = 0;
					}
					int len = 8 - nextTile;
					long[] speed = new long[len];
					for (int j = 0; j < speed.length; j++) {
						speed[j]=100;
					}
					playerSprite.animate(speed, nextTile, 7, false, new IAnimationListener() {
						
						@Override
						public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {}
						
						@Override
						public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {}
						
						@Override
						public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {}
						
						@Override
						public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
							pAnimatedSprite.stopAnimation(0);
						}
					});
				}
				return true;
			}
			
			@Override
			protected void preDraw(GLState glState, Camera camera) {
				super.preDraw(glState, camera);
				glState.enableDither();
			}
		};
		
		Sprite right = new Sprite(ResourceManager.getInstance().control_left_region.getWidth() + 25, camera.getHeight() - (ResourceManager.getInstance().control_right_region.getHeight() + 25), ResourceManager.getInstance().control_right_region, vbom) {
			
			@Override
			public boolean onAreaTouched(final TouchEvent event, final float x, final float y) {
				if (groundContact >= 1) {
					playerBody.setLinearVelocity(4.0f, playerBody.getLinearVelocity().y);
				} else if (groundContact < 1) {
					playerSprite.stopAnimation(0);
				}
				if ((!playerSprite.isAnimationRunning() || playerSprite.isFlippedHorizontal()) && (groundContact >= 1)) {
					playerSprite.setFlippedHorizontal(false);
					playerSprite.animate(100);
				}
				if ((event.getAction() == TouchEvent.ACTION_UP) && playerSprite.isAnimationRunning()) {
					int nextTile = playerSprite.getCurrentTileIndex() + 1;
					if ((playerSprite.getCurrentTileIndex() == 7) || (nextTile == 7)) {
						nextTile = 0;
					}
					int len = 8 - nextTile;
					long[] speed = new long[len];
					for (int j = 0; j < speed.length; j++) {
						speed[j]=100;
					}
					playerSprite.animate(speed, nextTile, 7, false, new IAnimationListener() {
						
						@Override
						public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {}
						
						@Override
						public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {}
						
						@Override
						public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {}
						
						@Override
						public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
							pAnimatedSprite.stopAnimation(0);
						}
					});
				}
				return true;
			}
			
			@Override
			protected void preDraw(GLState glState, Camera camera) {
				super.preDraw(glState, camera);
				glState.enableDither();
			}
		};
		
		
		gameHud.registerTouchArea(left);
		gameHud.attachChild(left);
		gameHud.registerTouchArea(right);
		gameHud.attachChild(right);

	}
	
	private void createJumpControls() {
		Sprite jump = new Sprite(camera.getWidth() - ResourceManager.getInstance().control_jump_region.getWidth() - 25, camera.getHeight() - ResourceManager.getInstance().control_jump_region.getHeight() - 25, ResourceManager.getInstance().control_jump_region, vbom) {
			
			@Override
			public boolean onAreaTouched(final TouchEvent event, final float x, final float y) {
				if (event.isActionUp() && (groundContact >= 1)) {
					playerBody.setLinearVelocity(playerBody.getLinearVelocity().x, -10.0f);
//					playerBody.applyLinearImpulse(new Vector2(0, -3000.0f), playerBody.getPosition());
					playerSprite.stopAnimation(0);
				}
				return true;
			}
			
			@Override
			protected void preDraw(GLState glState, Camera camera) {
				super.preDraw(glState, camera);
				glState.enableDither();
			}
		};
		
		gameHud.registerTouchArea(jump);
		gameHud.attachChild(jump);
	}
	
	private ContactListener createContactListener()	{
	    ContactListener contactListener = new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();
				ArrayList<String> set = new ArrayList<String>();
				set.add(x1.getBody().getUserData().toString());
				set.add(x2.getBody().getUserData().toString());
							
				if (set.contains("Player") && set.contains("Grass")){
					groundContact += 1;
					Log.i("Contact", "Player + grass");
				} else if (set.contains("Player") && set.contains("Grass Box")){
					groundContact += 1;
					Log.i("Contact", "Player + Grass Box");
				} else if (set.contains("Player") && set.contains("Grass Box Wide")) {
					Log.i("Contact", "player + Grass Box Wide");
					Log.i("Start Con Check", String.valueOf(Math.abs((playerSprite.getY() + playerSprite.getHeightScaled() + 150))));
					if (Math.abs((playerSprite.getY() + playerSprite.getHeightScaled() + 150)) <= 2.0f) {
						Log.i("Contact", "Player + Grass Box Wide TOP SIDE");
						groundContact += 1;
						Log.i("groundContact", String.valueOf(groundContact));
					}
					
				} else if (set.contains("Player") && set.contains("Entrance")) {
					Log.i("Contact", "Player + Entrance");
					if (ghostList.size() == 0 && knightList.size() == 0) {
						SceneManager.getInstance().setGameScene(2, currentHealth, ghostsKilled, knightsKilled);
					} else {
						Log.i("Level2", "All enemies are not yet killed");
					}
				} else if (set.contains("Player") && set.contains("Ghost")) {
					Log.i("Contact", "Player + Ghost");
				} else if (set.contains("Player") && set.contains("Snow")){
					groundContact += 1;
					Log.i("Contact", "Player + Snow");
				} else if (set.contains("Player") && set.contains("Snow spikes")) {
					playerDead();
					Log.i("Contact", "Player + Snow Spikes");
				} else if (set.contains("Player") && set.contains("Knight")) {
					Log.i("Contact", "Player + Knight");
				} else {
					Log.i("Other contact", "Other cont");
				}
			}

			@Override
			public void endContact(Contact contact) {
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();
				ArrayList<String> set = new ArrayList<String>();
				set.add(x1.getBody().getUserData().toString());
				set.add(x2.getBody().getUserData().toString());
				if (set.contains("Player") && (set.contains("Grass") || set.contains("Grass Box") || set.contains("Snow"))) {
					groundContact -= 1;
				} else if (set.contains("Player") && set.contains("Grass Box Wide") && groundContact == 1) {
					Log.i("End Con Check", String.valueOf(playerSprite.getY() + playerSprite.getHeightScaled() + 150));
					groundContact -= 1;
				}
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {}

			@Override
			public void preSolve(Contact contact, Manifold manifold) {}
	        
	    };
	    return contactListener;
	}
		
	@Override
	public boolean onSceneTouchEvent(Scene scene, TouchEvent tEvent) {
		if (tEvent.getAction() == TouchEvent.ACTION_DOWN && spellShot!=true) {
	        final float touchX = tEvent.getX();
	        final float touchY = tEvent.getY();
	        castSpell(touchX, touchY);
	        return true;
	    }
	    return false;
	}
	
	private void castSpell(final float pX, final float pY) {
		spellShot = true;
		if (!CoolDown.sharedCoolDown().checkValidity()) {
			Log.i("ShootProjectile", "Cooldown");
			spellShot = false;
			return;
		}
		Log.i("ShootProjectile", "shooting spell");
		final Sprite spellSprite = spellPool.obtainPoolItem();
		
	    float offX = pX - (playerSprite.getX() + (playerSprite.getWidth() / 2));
	    float offY = pY - (playerSprite.getY() + 25);
	    if (offX <= 0) {
	    	playerSprite.setFlippedHorizontal(true);
	    	spellSprite.setPosition(playerSprite.getX(), playerSprite.getY() + 25);
	    } else {
	    	playerSprite.setFlippedHorizontal(false);
	    	spellSprite.setPosition(playerSprite.getX() + playerSprite.getWidth(), playerSprite.getY() + 25);
	    }
	    attachChild(spellSprite);
	    
	    
	    float length = (float) Math.sqrt((offX * offX) + (offY * offY));
	    float speed = 400.0f; // 320 px/sec
	    float time = length / speed;
	 
	    MoveModifier mod = new MoveModifier(time, spellSprite.getX(), pX, spellSprite.getY(), pY) {
	        @Override
	        protected void onModifierFinished(IEntity pItem) {
                super.onModifierFinished(pItem);
        		spellPool.recyclePoolItem(spellSprite);
                spellShot = false;
	        }
	        
	        @Override
	        protected void onManagedUpdate(float pSecondsElapsed, IEntity pItem) {
	        	super.onManagedUpdate(pSecondsElapsed, pItem);
	        	for (Tile t: tiles) {
	        		if (spellSprite.collidesWith(t)) {
		        		Log.i("Collision", "Spell + Tile");
		        		spellPool.recyclePoolItem(spellSprite);
		        		spellShot = false;
		        		break;
		        	}
	        	}
	        	for (Monster g: ghostList) {
	        		if (spellSprite.collidesWith(g.monsterSprite)) {
	        			Log.i("Collision", "Spell + Ghost");
	        			g.onHit(35);
	        			spellPool.recyclePoolItem(spellSprite);
	        			spellShot = false;
	        			if (!g.isAlive) {
	        				ghostsKilled += 1;
	        				final PhysicsConnector physCon = physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(g.monsterSprite);
	        				if (physCon!=null) {
	        					physicsWorld.unregisterPhysicsConnector(physCon);
	        					g.monsterBody.setActive(false);
	        					physicsWorld.destroyBody(g.monsterBody);
	        				}
	        				ghostList.remove(g);
		        			ghostPool.recyclePoolItem(g.monsterSprite);
	        			}
	        			break;
	        		}
	        	}
	        	for (Monster g: knightList) {
	        		if (spellSprite.collidesWith(g.monsterSprite)) {
	        			Log.i("Collision", "Spell + Knight");
	        			g.onHit(55);
	        			spellPool.recyclePoolItem(spellSprite);
	        			spellShot = false;
	        			if (!g.isAlive) {
	        				knightsKilled += 1;
	        				final PhysicsConnector physCon = physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(g.monsterSprite);
	        				if (physCon!=null) {
	        					physicsWorld.unregisterPhysicsConnector(physCon);
	        					g.monsterBody.setActive(false);
	        					physicsWorld.destroyBody(g.monsterBody);
	        				}
	        				knightList.remove(g);
		        			KnightPool.recyclePoolItem(g.monsterSprite);
	        			}
	        			break;
	        		}
	        	}
	        }
	    };
	    LoopEntityModifier loopMod = new LoopEntityModifier(new RotationModifier(0.2f, 0, 360));
	    final ParallelEntityModifier parMod = new ParallelEntityModifier(mod, loopMod);
	    spellSprite.registerEntityModifier(parMod);
	}
	
	private void addGhost(float x, float y) {	
		FixtureDef ghostFixtureDef = PhysicsFactory.createFixtureDef(20.0f, 0.0f, 0.0f);
		final AnimatedSprite ghostSprite = ghostPool.obtainPoolItem();
		ghostSprite.setScale(0.5f);
		
		final float width = ghostSprite.getWidthScaled() / px_to_meter_def;
		final float height = ghostSprite.getHeightScaled() / px_to_meter_def;

		final Vector2[] vertices = {
			new Vector2(-0.33000f*width, -0.39231f*height),
			new Vector2(-0.01000f*width, -0.50000f*height),
			new Vector2(+0.34000f*width, -0.36923f*height),
			new Vector2(+0.50000f*width, +0.33077f*height),
			new Vector2(+0.20000f*width, +0.50000f*height),
			new Vector2(-0.14000f*width, +0.49231f*height),
			new Vector2(-0.50000f*width, +0.33846f*height),
		};		
	
//		final Body ghostBody = PhysicsFactory.createBoxBody(physicsWorld, ghostSprite, BodyType.KinematicBody, ghostFixtureDef);
		final Body ghostBody = PhysicsFactory.createPolygonBody(physicsWorld, ghostSprite, vertices, BodyType.KinematicBody, ghostFixtureDef);
		final Monster ghost = new Monster(ghostSprite, ghostBody, "Ghost");
		ghostList.add(ghost);
		ghostBody.setUserData("Ghost");
		
		final Vector2 v2 = Vector2Pool.obtain((x + ghostSprite.getWidth() / 2) / px_to_meter_def, (y + ghostSprite.getHeight() / 2) / px_to_meter_def);
		ghostBody.setTransform(v2, ghostBody.getAngle());
				
		ghostBody.setFixedRotation(true);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(ghostSprite, ghostBody, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				if (Math.abs(ghostSprite.getX() - playerSprite.getX()) <= 400) {
					if (!ghost.attacking && ghostSprite.collidesWith(playerSprite)) {
						Log.i("GHOST", "GHOST + PLAYER");
						ghost.attacking = true;
						ghostBody.setLinearVelocity(0f, 0f);
						
						ghostSprite.animate(new long[]{100,100,100,100}, 6, 9, false, new IAnimationListener() {
							
							@Override
							public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {}
							
							@Override
							public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {}
							
							@Override
							public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {
								if (pNewFrameIndex == 2) {
									currentHealth -= 20;
								}
							}
							
							@Override
							public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
								Log.i("TEST", String.valueOf(ghostSprite.getX() - playerSprite.getX()));
								if (ghostSprite.getX() - playerSprite.getX() <= -1.0f) {
									ghostSprite.setFlippedHorizontal(true);
								} else {
									ghostSprite.setFlippedHorizontal(false);
								}
								ghost.attacking = false;
							}
						});
					} else if (!ghost.attacking && ghostSprite.getX() - playerSprite.getX() <= -1.0f) {
						if (!ghostSprite.isAnimationRunning()) {
							ghostSprite.animate(new long[]{100,100,100,100}, 1, 4, true);
						}
						ghostSprite.setFlippedHorizontal(true);
						ghostBody.setLinearVelocity(2.0f, 0f);
					} else if (!ghost.attacking && ghostSprite.getX() - playerSprite.getX() >= 1.0f) {
						if (!ghostSprite.isAnimationRunning()) {
							ghostSprite.animate(new long[]{100,100,100,100}, 1, 4, true);
						}
						ghostSprite.setFlippedHorizontal(false);
						ghostBody.setLinearVelocity(-2.0f, 0f);
					} else if (!ghost.attacking){
						ghostBody.setLinearVelocity(0, 0f);
						ghostSprite.stopAnimation(0);
					}
				}
			}
		});
		
		attachChild(ghostSprite);
	}
	
	private void addKnight(float x, float y) {
		FixtureDef knightFixtureDef = PhysicsFactory.createFixtureDef(100.0f, 0.0f, 0.75f);
		final AnimatedSprite knightSprite = KnightPool.obtainPoolItem();
		
		final float width = knightSprite.getWidthScaled() / px_to_meter_def;
		final float height = knightSprite.getHeightScaled() / px_to_meter_def;

		final Vector2[] vertices = {
			new Vector2(-0.34253f*width, -0.31498f*height),
			new Vector2(-0.23037f*width, -0.43381f*height),
			new Vector2(-0.00057f*width, -0.50406f*height),
			new Vector2(+0.21816f*width, -0.44108f*height),
			new Vector2(+0.30399f*width, -0.31995f*height),
			new Vector2(+0.30507f*width, +0.28181f*height),
			new Vector2(+0.18216f*width, +0.48677f*height),
			new Vector2(-0.21852f*width, +0.48677f*height),
			
		};		
	
		final Body knightBody = PhysicsFactory.createPolygonBody(physicsWorld, knightSprite, vertices, BodyType.DynamicBody, knightFixtureDef);
//		final Body knightBody = PhysicsFactory.createBoxBody(physicsWorld, knightSprite, BodyType.KinematicBody, knightFixtureDef);
		final Monster knight = new Monster(knightSprite, knightBody, "Knight");
		knightList.add(knight);
		knightBody.setUserData("Knight");
		
		final Vector2 v2 = Vector2Pool.obtain((x + knightSprite.getWidth() / 2) / px_to_meter_def, (y + knightSprite.getHeight() / 2) / px_to_meter_def);
		knightBody.setTransform(v2, knightBody.getAngle());		
		
		knightBody.setFixedRotation(true);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(knightSprite, knightBody, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				if (Math.abs(knightSprite.getX() - playerSprite.getX()) <= 400) {
					if (!knight.attacking && knightSprite.collidesWith(playerSprite)) {
						knight.attacking = true;
						knightBody.setLinearVelocity(0f, 0f);
						
						knightSprite.animate(new long[]{100,100,100,100}, 8, 11, false, new IAnimationListener() {
							
							@Override
							public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {}
							
							@Override
							public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {}
							
							@Override
							public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {
								if (pNewFrameIndex == 2) {
									currentHealth -= 20;
								}
							}
							
							@Override
							public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
								if (knightSprite.getX() - playerSprite.getX() <= -1.0f) {
									knightSprite.setFlippedHorizontal(false);
								} else {
									knightSprite.setFlippedHorizontal(true);
								}
								knight.attacking = false;
							}
						});
					} else if (!knight.attacking && knightSprite.getX() - playerSprite.getX() <= -1.0f && knightBody.getLinearVelocity().y == 0) {
						if (!knightSprite.isAnimationRunning()) {
							knightSprite.animate(new long[]{100,100,100,100,100,100,100,100}, 0, 7, true);
						}
						knightSprite.setFlippedHorizontal(false);
						knightBody.setLinearVelocity(2.5f, knightBody.getLinearVelocity().y);
					} else if (!knight.attacking && knightSprite.getX() - playerSprite.getX() >= 1.0f && knightBody.getLinearVelocity().y == 0) {
						if (!knightSprite.isAnimationRunning()) {
							knightSprite.animate(new long[]{100,100,100,100,100,100,100,100}, 0, 7, true);
						}
						knightSprite.setFlippedHorizontal(true);
						knightBody.setLinearVelocity(-2.5f, knightBody.getLinearVelocity().y);
					} else if (!knight.attacking && knightBody.getLinearVelocity().y == 0){
						knightBody.setLinearVelocity(0, knightBody.getLinearVelocity().y);
						knightSprite.stopAnimation(0);
					}
					if (-knightSprite.getY() - knightSprite.getHeight() <= -2.0f) {
						Log.i("Knight", "Jump");
						if (knightBody.getLinearVelocity().x >= 0) {
							knightBody.setLinearVelocity(6.0f, -5.0f);
						} else {
							knightBody.setLinearVelocity(-6.0f, -5.0f);
						}
					}
				} 
			}
		});
		
		attachChild(knightSprite);
	}
	
	private void createHealthBar() {
		float x = 2*ResourceManager.getInstance().control_left_region.getWidth() + 70;
		float y = camera.getHeight() - ResourceManager.getInstance().healthbar_region.getHeight() - 25;
		healthBarSprite = new Sprite(x, y, ResourceManager.getInstance().healthbar_region, vbom);
		healthBarWidth = healthBarSprite.getWidth();
		healthBar  = new Rectangle(x, y, healthBarWidth, healthBarSprite.getHeight(), vbom);
		healthBar.setColor(1, 0, 0);
		gameHud.attachChild(healthBar);
		gameHud.attachChild(healthBarSprite);
	}

	private void addEnemies() {
		addGhost(800, -250);
		addGhost(1000, -250);
		addGhost(900, -100);
		addKnight(1000, -80);
		addKnight(800, -80);
	}
	
}
