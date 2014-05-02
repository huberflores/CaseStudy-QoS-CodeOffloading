package com.codeoff.casestudy;

import java.io.IOException;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.ui.activity.BaseGameActivity;

import android.view.KeyEvent;
import android.widget.Toast;

import com.codeoff.casestudy.scenes.SceneManager;

public class GameActivity extends BaseGameActivity {
//	private BoundCamera camera;
	private SmoothCamera camera;
	protected static final int CAMERA_WIDTH = 800;
	protected static final int CAMERA_HEIGHT = 480;
	
	public static int getCameraWidth() {
		return CAMERA_WIDTH;
	}

	public static int getCameraHeight() {
		return CAMERA_HEIGHT;
	}
	
	
	@Override
	public EngineOptions onCreateEngineOptions() {
//		camera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		camera = new SmoothCamera(0, -CAMERA_HEIGHT, CAMERA_WIDTH, CAMERA_HEIGHT, 200.0f, 50.0f, 1f);
		camera.setCenterDirect(0,0);
		EngineOptions engineOptions = new EngineOptions(true,ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH,CAMERA_HEIGHT), camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		engineOptions.getRenderOptions().setDithering(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);		
		if (!MultiTouch.isSupported(this)) {
			Toast.makeText(this, "Multitouch not supported by this device.", Toast.LENGTH_LONG).show();
			System.exit(0);
		}		
		return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback cb)
			throws IOException {
		ResourceManager.prepareManager(getEngine(), this, camera, this.getVertexBufferObjectManager());
		cb.onCreateResourcesFinished();
		
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback cb) throws IOException {
		SceneManager.getInstance().setMenuScene(cb);
	}

	@Override
	public void onPopulateScene(Scene arg0, OnPopulateSceneCallback cb)
			throws IOException {
		cb.onPopulateSceneFinished();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SceneManager.getInstance().getCurrentScene().onBackPressed();
		}
		return false;
	}
	
}
