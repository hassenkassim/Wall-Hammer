package net.hamoto.wallhammer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import net.hamoto.wallhammer.Manager.SceneManager;
import net.hamoto.wallhammer.Manager.ResourcesManager;
import net.hamoto.wallhammer.Utils.GameHelper;

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

import java.io.IOException;


/**
 * @author Hassen Kassim
 * @author www.hamoto.net
 * @version 1.0
 *
 * The MainActiviy starts the game by setting various parameters and starting the game engine.
 */

public class MainActivity extends BaseGameActivity implements GameHelper.GameHelperListener {


    Camera camera;
    ResourcesManager resourcesManager;
    static Activity main;
    public final static int GAMEWIDTH = 1280;
    public final static int GAMEHEIGHT = 720;
    public final static String SETTING = "PREFS";
    public final static String SETTING_MUSIC = "MUSICON";
    public final static String HIGHSCORE = "HIGHSCORE";


    public static boolean musicon;
    SharedPreferences prefs;
    public static long highscore;
    public static final String LEADERBOARD_ID = "CgkIxPfbyu8CEAIQAA";
    public static final int REQUEST_LEADERBOARD = 123;


    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions)
    {


        main = this;
        setupGoogleAPI();
        musicsetting();
        setHighscore();
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

    @Override
    protected void onStart() {
        super.onStart();
        mHelper.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHelper.onStop();
    }


    private void setupGoogleAPI(){

    }

    private void setHighscore(){
        highscore = getSharedPreferences(HIGHSCORE, MODE_PRIVATE).getLong(HIGHSCORE, 0);
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

    /**
     * Called when sign-in fails. As a result, a "Sign-In" button can be
     * shown to the user; when that button is clicked, call
     *
     * @link{GamesHelper#beginUserInitiatedSignIn . Note that not all calls
     * to this method mean an
     * error; it may be a result
     * of the fact that automatic
     * sign-in could not proceed
     * because user interaction
     * was required (consent
     * dialogs). So
     * implementations of this
     * method should NOT display
     * an error message unless a
     * call to @link{GamesHelper#
     * hasSignInError} indicates
     * that an error indeed
     * occurred.
     */
    @Override
    public void onSignInFailed() {
        Log.i("WALLHAMMER", "SIGNED IN FAILED");
    }

    /**
     * Called when sign-in succeeds.
     */
    @Override
    public void onSignInSucceeded() {
        Log.i("WALLHAMMER", "SIGNED IN SUCCEEDED");
    }

    // The game helper object. This class is mainly a wrapper around this object.
    public static GameHelper mHelper;

    // We expose these constants here because we don't want users of this class
    // to have to know about GameHelper at all.
    public static final int CLIENT_GAMES = GameHelper.CLIENT_GAMES;
    public static final int CLIENT_PLUS = GameHelper.CLIENT_PLUS;
    public static final int CLIENT_ALL = GameHelper.CLIENT_ALL;

    // Requested clients. By default, that's just the games client.
    protected int mRequestedClients = CLIENT_GAMES;

    private final static String TAG = "BaseGameActivity";
    protected boolean mDebugLog = false;

    public GameHelper getGameHelper() {
        if (mHelper == null) {
            mHelper = new GameHelper(this, mRequestedClients);
            mHelper.enableDebugLog(mDebugLog);
        }
        return mHelper;
    }

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        if (mHelper == null) {
            getGameHelper();
        }
        mHelper.setup(this);
    }

    @Override
    protected void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);
        mHelper.onActivityResult(request, response, data);
    }

    protected GoogleApiClient getApiClient() {
        return mHelper.getApiClient();
    }

    protected boolean isSignedIn() {
        return mHelper.isSignedIn();
    }

    protected void beginUserInitiatedSignIn() {
        mHelper.beginUserInitiatedSignIn();
    }

    protected void signOut() {
        mHelper.signOut();
    }

    protected void showAlert(String message) {
        mHelper.makeSimpleDialog(message).show();
    }

    protected void showAlert(String title, String message) {
        mHelper.makeSimpleDialog(title, message).show();
    }

    protected void enableDebugLog(boolean enabled) {
        mDebugLog = true;
        if (mHelper != null) {
            mHelper.enableDebugLog(enabled);
        }
    }

    @Deprecated
    protected void enableDebugLog(boolean enabled, String tag) {
        Log.w(TAG, "BaseGameActivity.enabledDebugLog(bool,String) is " +
                "deprecated. Use enableDebugLog(boolean)");
        enableDebugLog(enabled);
    }

    protected String getInvitationId() {
        return mHelper.getInvitationId();
    }

    protected void reconnectClient() {
        mHelper.reconnectClient();
    }

    protected boolean hasSignInError() {
        return mHelper.hasSignInError();
    }

    protected GameHelper.SignInFailureReason getSignInError() {
        return mHelper.getSignInError();
    }

}