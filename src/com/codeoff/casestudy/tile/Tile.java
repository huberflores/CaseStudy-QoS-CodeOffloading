package com.codeoff.casestudy.tile;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Tile extends TiledSprite {
	
	private final String name;
	private final int id;
	private final float density, width, height, elastic, friction;
	private Body body;
	private float px_to_meter_def = PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	
	public Tile(String name, int id, float x, float y, float width, float height, float density, float elastic, float friction, ITiledTextureRegion texture, VertexBufferObjectManager vbom) {
		super(x,y,width,height, texture, vbom);
		this.name = name;
		this.id = id;
		this.width = width;
		this.height = height;
		this.density = density;
		this.elastic = elastic;
		this.friction = friction;
	}
	
	public void createBodyAndAttach(Scene scene, PhysicsWorld physicsWorld) {
		final FixtureDef tileFixtureDef = PhysicsFactory.createFixtureDef(density, elastic, friction);
		tileFixtureDef.restitution = 0;
		if (name=="Grass Box Wide") {
			Log.i("", "tein uue kasti");
			final float boxWidth = width / px_to_meter_def;
			final float boxHeight = height / px_to_meter_def;
			Vector2[] vertices = {
				new Vector2(-0.51022f*boxWidth, -0.19840f*boxHeight),
				new Vector2(-0.44961f*boxWidth, -0.38050f*boxHeight),
				new Vector2(-0.34117f*boxWidth, -0.49981f*boxHeight),
				new Vector2(+0.37647f*boxWidth, -0.48097f*boxHeight),
				new Vector2(+0.45939f*boxWidth, -0.39934f*boxHeight),
				new Vector2(+0.50405f*boxWidth, -0.26119f*boxHeight),
				new Vector2(+0.50405f*boxWidth, +0.49233f*boxHeight),
				new Vector2(-0.50703f*boxWidth, +0.49233f*boxHeight)
			};
			body = PhysicsFactory.createPolygonBody(physicsWorld, this, vertices, BodyType.StaticBody, tileFixtureDef);
		} else {
			body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.StaticBody, tileFixtureDef);
		}
		body.setUserData(name);
		scene.attachChild(this);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, true));
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public Tile getInstance(float x, float y) {
		return new Tile(name, id, x, y, width, height, density, elastic, friction, getTiledTextureRegion(), getVertexBufferObjectManager());
	}
}
