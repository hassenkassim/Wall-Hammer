package net.hamoto.wallhammer.Manager;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface;

import net.hamoto.wallhammer.Scenes.BaseScene;
import net.hamoto.wallhammer.Scenes.GameScene;
import net.hamoto.wallhammer.Scenes.LoadingScene;
import net.hamoto.wallhammer.Scenes.SplashScene;
import net.hamoto.wallhammer.Scenes.MainScene;

/**
 * @author Hassen Kassim
 * @author www.hamoto.net
 * @version 1.0
 *
 * The SceneManager is responsible for loading, switching and keeping track of the different scenes of the game.
 */
public class SceneManager
{
    //---------------------------------------------
    // SCENES
    //---------------------------------------------

    private BaseScene splashScene;
    private BaseScene mainScene;
    private BaseScene gameScene;
    private BaseScene loadingScene;

    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------

    private static final SceneManager INSTANCE = new SceneManager();

    private SceneType currentSceneType = SceneType.SCENE_SPLASH;

    private BaseScene currentScene;

    private Engine engine = ResourcesManager.getInstance().engine;

    public enum SceneType
    {
        SCENE_SPLASH,
        SCENE_MAIN,
        SCENE_GAME,
        SCENE_LOADING,
    }

    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

    public void setScene(BaseScene scene)
    {
        engine.setScene(scene);
        currentScene = scene;
        currentSceneType = scene.getSceneType();
    }

    public void setScene(SceneType sceneType)
    {
        switch (sceneType)
        {
            case SCENE_MAIN:
                createMainScene();
                break;
            case SCENE_GAME:
                loadGameScene(engine);
                break;
            case SCENE_SPLASH:
                setScene(splashScene);
                break;
            case SCENE_LOADING:
                setScene(loadingScene);
                break;
            default:
                break;
        }
    }

    //---------------------------------------------
    // SCENE LOADING FUNCTIONS
    //---------------------------------------------

    public void createSplashScene(IGameInterface.OnCreateSceneCallback pOnCreateSceneCallback)
    {
        ResourcesManager.getInstance().loadSplashScreen();
        splashScene = new SplashScene();
        currentScene = splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }

    public void createMainScene()
    {
        ResourcesManager.getInstance().loadMainResources();
        mainScene = new MainScene();
        loadingScene = new LoadingScene();
        setScene(mainScene);
        disposeSplashScene();
    }

    public void loadMainScene(final Engine mEngine)
    {
        setScene(loadingScene);
        gameScene.disposeScene();
        ResourcesManager.getInstance().unloadGameRecources();
        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback()
        {
            public void onTimePassed(final TimerHandler pTimerHandler)
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadMainResources();
                setScene(mainScene);
            }
        }));
    }

    public void loadGameScene(final Engine mEngine)
    {
        setScene(loadingScene);
        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback()
        {
            public void onTimePassed(final TimerHandler pTimerHandler)
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadGameResources();
                gameScene = new GameScene();
                setScene(gameScene);
            }
        }));
    }


    //---------------------------------------------
    // SCENE DISPOSE FUNCTIONS
    //---------------------------------------------

    private void disposeSplashScene()
    {
        ResourcesManager.getInstance().unloadSplashScreen();
        splashScene.disposeScene();
        splashScene = null;
    }

    private void disposeMainScene()
    {
        ResourcesManager.getInstance().unloadMainGraphics();
        mainScene.disposeScene();
        mainScene = null;
    }

    private void disposeGameScene()
    {
        ResourcesManager.getInstance().unloadGameRecources();
        gameScene.disposeScene();
        gameScene = null;
    }

    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------

    public static SceneManager getInstance()
    {
        return INSTANCE;
    }

    public SceneType getCurrentSceneType()
    {
        return currentSceneType;
    }

    public BaseScene getCurrentScene()
    {
        return currentScene;
    }
}
