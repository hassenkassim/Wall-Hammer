package net.hamoto.wallhammer.Scenes;

import net.hamoto.wallhammer.MainActivity;
import net.hamoto.wallhammer.Manager.SceneManager;

import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.SurfaceGestureDetectorAdapter;

public class LoadingScene extends BaseScene implements IOnSceneTouchListener
{
    private Sprite loadingBackground;
    private Sprite loadingLogo;

    private SurfaceGestureDetectorAdapter surfaceGestureDetectorAdapter;


    @Override
    public void createScene()
    {
        if(camera.hasHUD()){
            camera.getHUD().detachChildren();
            camera.getHUD().detachSelf();
            camera.getHUD().dispose();
        }
        createBackground();
        addSplashLogo();
        addLoadingText();
    }

    private void createBackground(){
        loadingBackground = new Sprite(0, 0, resourcesManager.splashBackground_region, vbom);
        loadingBackground.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT/2);
        attachChild(loadingBackground);
    }

    private void addSplashLogo(){
        loadingLogo = new Sprite(0, 0, resourcesManager.splashLogo_region, vbom);
        loadingLogo.setScale(0.35f);
        loadingLogo.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT*0.7f);
        loadingLogo.registerEntityModifier(new LoopEntityModifier(new RotationModifier(1f, 0f, 359f)));
        attachChild(loadingLogo);
    }

    private void addLoadingText(){
        attachChild(new Text(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT*0.4f, resourcesManager.font, "Loading...", vbom));
    }

    @Override
    public void onBackKeyPressed()
    {
    }

    @Override
    public SceneManager.SceneType getSceneType()
    {
        return SceneManager.SceneType.SCENE_LOADING;
    }

    @Override
    public void disposeScene()
    {

    }

    /**
     * Called when a {@link TouchEvent} is dispatched to a {@link Scene}.
     *
     * @param pScene           The {@link Scene} that the {@link TouchEvent} has been dispatched to.
     * @param pSceneTouchEvent The {@link TouchEvent} object containing full information about the event.
     * @return <code>true</code> if this {@link IOnSceneTouchListener} has consumed the {@link TouchEvent}, <code>false</code> otherwise.
     */
    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        return surfaceGestureDetectorAdapter.onManagedTouchEvent(pSceneTouchEvent);
    }
}
