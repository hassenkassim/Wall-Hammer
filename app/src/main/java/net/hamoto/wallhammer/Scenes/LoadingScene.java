package net.hamoto.wallhammer.Scenes;

import net.hamoto.wallhammer.Manager.SceneManager;

import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import net.hamoto.wallhammer.MainActivity;

public class LoadingScene extends BaseScene
{
    private Sprite loadingBackground;
    private Sprite loadingLogo;

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
        //setBackground(new Background(Color.WHITE));
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
}
