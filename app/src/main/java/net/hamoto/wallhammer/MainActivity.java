package net.hamoto.wallhammer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import net.hamoto.wallhammer.Manager.SceneManager;
import net.hamoto.wallhammer.Manager.ResourcesManager;
import net.hamoto.wallhammer.Scenes.GameScene;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.io.IOException;


/**
 * @author Hassen Kassim
 * @author www.hamoto.net
 * @version 1.0
 *
 * The MainActiviy starts the game by setting various parameters and starting the game engine.
 */

public class MainActivity extends BaseGameActivity {

    Camera camera;
    ResourcesManager resourcesManager;
    static Activity main;
    public final static int GAMEWIDTH = 1280;
    public final static int GAMEHEIGHT = 720;
    public final static String SETTING = "PREFS";
    public final static String SETTING2 = "PREFS2";
    public final static String SETTING_MUSIC = "MUSICON";
    public final static String SETTING_HIGHSCORE = "HIGHSCORE";

    public static boolean musicon;
    SharedPreferences prefs;
    public static long highscore;
    SharedPreferences prefs2;

    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions)
    {
        main = this;
        //gameToast("Game Started!");
        musicsetting();
        highscoreSetting();
        return new LimitedFPSEngine(pEngineOptions, 60);
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        camera = new Camera(0, 0, GAMEWIDTH, GAMEHEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(GAMEWIDTH, GAMEHEIGHT), this.camera);
        engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException
    {
        ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
        resourcesManager = ResourcesManager.getInstance();
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
        SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
    }

    private void highscoreSetting(){
        prefs2 = getSharedPreferences(SETTING2, MODE_PRIVATE);
        highscore = prefs2.getLong(SETTING_HIGHSCORE, 1);
    }

    private void musicsetting(){
        prefs = getSharedPreferences(SETTING, MODE_PRIVATE);
        musicon = prefs.getBoolean(SETTING_MUSIC, true);
    }

    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
    {
        mEngine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback()
        {
            public void onTimePassed(final TimerHandler pTimerHandler)
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                SceneManager.getInstance().setScene(SceneManager.SceneType.SCENE_MAIN);
            }
        }));
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    //Method for generating Toast messages as they need to run on UI thread
    public static void gameToast(final String msg) {
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(main, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //TODO: Testen mit verschiedenen Handys
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
        }
        return false;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        getEngine().getMusicManager().setMasterVolume(0);
        getEngine().getSoundManager().setMasterVolume(0);
    }

    @Override
    protected synchronized void onResume()
    {
        super.onResume();
        System.gc();
        getEngine().getMusicManager().setMasterVolume(1);
        getEngine().getSoundManager().setMasterVolume(1);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        System.exit(0);
    }

}