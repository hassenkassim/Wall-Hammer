package net.hamoto.wallhammer.Scenes;

import net.hamoto.wallhammer.Manager.SceneManager;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import net.hamoto.wallhammer.MainActivity;

public class LoadingScene extends BaseScene
{
    @Override
    public void createScene()
    {
        //ResourcesManager.getInstance().stopAllAudio();
        setBackground(new Background(Color.WHITE));
        attachChild(new Text(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT/2, resourcesManager.font, "Loading...", vbom));

    }

    @Override
    public void onBackKeyPressed()
    {
        //return;
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
