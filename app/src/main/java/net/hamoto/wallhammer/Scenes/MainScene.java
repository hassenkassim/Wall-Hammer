package net.hamoto.wallhammer.Scenes;

import android.content.Context;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.google.android.gms.games.Games;

import net.hamoto.wallhammer.MainActivity;
import net.hamoto.wallhammer.Manager.PlayGamesManager;
import net.hamoto.wallhammer.Manager.SceneManager;
import net.hamoto.wallhammer.Manager.ResourcesManager;

import org.andengine.audio.music.Music;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Hassen Kassim
 * @version 1.0
 *
 * This Scene shows the Logo while loading the Main Scene.
 */
public class MainScene extends BaseScene
{
    MenuScene menuChildScene;
    final int MENU_PLAY = 0;
    final int MENU_SCORE = 1;
    final int MENU_SOUND_ON = 2;
    final int MENU_SOUND_OFF = 3;
    Sprite logo;
    Sprite cloud1sprite;
    Sprite cloud2sprite;
    Sprite cloud3sprite;
    Sprite cloud4sprite;
    Sprite cloud5sprite;
    Sprite cloud6sprite;
    Sprite cloud7sprite;
    Sprite cloud8sprite;
    Sprite cloud9sprite;
    Sprite cloud10sprite;
    Sprite groundsprite;
    Sprite hammer;
    Sprite rad;
    Sprite gameTitle;
    ButtonSprite musicon;
    ButtonSprite musicoff;
    public static Music musicMain;


    @Override
    public void createScene()
    {
        createBackground();
        addSprites();
        createMenuChildScene();
        startbackgroundmusic();
        setTouchAreaBindingOnActionDownEnabled(true);

    }

    private void createBackground()
    {
        setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
    }

    private void addSprites(){
        addClouds();
        addGround();
        addHammer();
        addLogo();
        addRad();
    }

    private void startbackgroundmusic(){
        musicMain = ResourcesManager.getInstance().musicMain;
        musicMain.setLooping(true);
        musicMain.play();

        if(!MainActivity.musicon){
            musicMain.pause();
        }
    }

    private void addGround(){
        groundsprite = new Sprite(0, 0, 3000, 256, ResourcesManager.getInstance().ground_region, engine.getVertexBufferObjectManager());
        groundsprite.setY(-25f);
        groundsprite.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new MoveXModifier(1f,128,-128))));
        attachChild(groundsprite);
    }

    private void addLogo(){
        gameTitle = new Sprite(0, 0, 958, 212, ResourcesManager.getInstance().gameTitle_region, engine.getVertexBufferObjectManager());
        gameTitle.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT/2 + 180);
        gameTitle.setScale(0.95f);
        attachChild(gameTitle);
    }


    private void addClouds(){

        float a = 2698;
        float duration = 30;

        cloud1sprite = new Sprite(450, 0, 285, 156, ResourcesManager.getInstance().cloud4_region, engine.getVertexBufferObjectManager());
        cloud1sprite.setY(600f);
        cloud1sprite.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new MoveXModifier(duration, cloud1sprite.getX(), -a + cloud1sprite.getX()))));
        attachChild(cloud1sprite);

        cloud2sprite = new Sprite(cloud1sprite.getX() + 500, 0, 285, 156, ResourcesManager.getInstance().cloud3_region, engine.getVertexBufferObjectManager());
        cloud2sprite.setY(600f);
        cloud2sprite.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new MoveXModifier(duration, cloud2sprite.getX(), -a + cloud2sprite.getX()))));
        attachChild(cloud2sprite);

        cloud3sprite = new Sprite(cloud2sprite.getX() + 600, 0, 285, 156, ResourcesManager.getInstance().cloud1_region, engine.getVertexBufferObjectManager());
        cloud3sprite.setY(600f);
        cloud3sprite.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new MoveXModifier(duration, cloud3sprite.getX(), -a + cloud3sprite.getX()))));
        attachChild(cloud3sprite);

        cloud4sprite = new Sprite(cloud3sprite.getX() + 550, 0, 285, 156, ResourcesManager.getInstance().cloud3_region, engine.getVertexBufferObjectManager());
        cloud4sprite.setY(600f);
        cloud4sprite.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new MoveXModifier(duration, cloud4sprite.getX(), -a + cloud4sprite.getX()))));
        attachChild(cloud4sprite);

        cloud5sprite = new Sprite(cloud4sprite.getX() + 450, 0, 285, 156, ResourcesManager.getInstance().cloud2_region, engine.getVertexBufferObjectManager());
        cloud5sprite.setY(600f);
        cloud5sprite.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new MoveXModifier(duration, cloud5sprite.getX(), -a + cloud5sprite.getX()))));
        attachChild(cloud5sprite);

        cloud6sprite = new Sprite(cloud5sprite.getX() + 600, 0, 285, 156, ResourcesManager.getInstance().cloud4_region, engine.getVertexBufferObjectManager());
        cloud6sprite.setY(600f);
        cloud6sprite.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new MoveXModifier(duration, cloud6sprite.getX(), -a + cloud6sprite.getX()))));
        attachChild(cloud6sprite);

        cloud7sprite = new Sprite(cloud6sprite.getX() + 500, 0, 285, 156, ResourcesManager.getInstance().cloud3_region, engine.getVertexBufferObjectManager());
        cloud7sprite.setY(600f);
        cloud7sprite.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new MoveXModifier(duration, cloud7sprite.getX(), -a + cloud7sprite.getX()))));
        attachChild(cloud7sprite);

        cloud8sprite = new Sprite(cloud7sprite.getX() + 600, 0, 285, 156, ResourcesManager.getInstance().cloud1_region, engine.getVertexBufferObjectManager());
        cloud8sprite.setY(600f);
        cloud8sprite.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new MoveXModifier(duration, cloud8sprite.getX(), -a + cloud8sprite.getX()))));
        attachChild(cloud8sprite);

        cloud9sprite = new Sprite(cloud8sprite.getX() + 550, 0, 285, 156, ResourcesManager.getInstance().cloud3_region, engine.getVertexBufferObjectManager());
        cloud9sprite.setY(600f);
        cloud9sprite.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new MoveXModifier(duration, cloud9sprite.getX(), -a + cloud9sprite.getX()))));
        attachChild(cloud9sprite);

        cloud10sprite = new Sprite(cloud9sprite.getX() + 600, 0, 285, 156, ResourcesManager.getInstance().cloud2_region, engine.getVertexBufferObjectManager());
        cloud10sprite.setY(600f);
        cloud10sprite.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new MoveXModifier(duration, cloud10sprite.getX(), -a + cloud10sprite.getX()))));
        attachChild(cloud10sprite);
    }


    private void addHammer(){
        hammer = new Sprite(0, 0, ResourcesManager.getInstance().hammer_region, engine.getVertexBufferObjectManager());
        hammer.setScale(0.3f);
        hammer.setPosition(143.0f, 226.0f);
        attachChild(hammer);
    }

    private void addRad(){
        rad = new Sprite(0, 0, 273, 273, ResourcesManager.getInstance().wheel_region, engine.getVertexBufferObjectManager());
        rad.setScale(0.3f);
        rad.setPosition(150, 117);
        rad.registerEntityModifier(new LoopEntityModifier(new RotationModifier(1f, 0f, 359f)));
        attachChild(rad);
    }

    private void musicOn(){
        musicMain.resume();
        MainActivity.musicon = true;
        activity.getSharedPreferences(MainActivity.SETTING, Context.MODE_PRIVATE).edit().putBoolean(MainActivity.SETTING_MUSIC, true).apply();
    }

    private void musicOff(){
        musicMain.pause();
        MainActivity.musicon = false;
        activity.getSharedPreferences(MainActivity.SETTING, Context.MODE_PRIVATE).edit().putBoolean(MainActivity.SETTING_MUSIC, false).apply();

    }


    private void createMenuChildScene()
    {
        menuChildScene = new MenuScene(camera);
        menuChildScene.setPosition(0, 0);

        final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.5f, 1);
        final IMenuItem scoreMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_SCORE, resourcesManager.score_region, vbom), 1.5f, 1);
        final IMenuItem musicOnMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_SOUND_ON, resourcesManager.soundon_region, vbom), 1.5f, 1);
        final IMenuItem musicOffMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_SOUND_OFF, resourcesManager.soundoff_region, vbom), 1.5f, 1);

        menuChildScene.addMenuItem(playMenuItem);
        menuChildScene.addMenuItem(scoreMenuItem);
        menuChildScene.addMenuItem(musicOnMenuItem);
        menuChildScene.addMenuItem(musicOffMenuItem);


        menuChildScene.buildAnimations();
        menuChildScene.setBackgroundEnabled(false);

        final float SOUNDXVISIBLE = MainActivity.GAMEWIDTH/2 + 200;
        final float SOUNDXINVISIBLE = -200;

        playMenuItem.setPosition(MainActivity.GAMEWIDTH/2 - 200, MainActivity.GAMEHEIGHT/2 - 80);
        scoreMenuItem.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT/2 - 80);
        musicOnMenuItem.setPosition(SOUNDXVISIBLE, MainActivity.GAMEHEIGHT/2 - 80);
        musicOffMenuItem.setPosition(SOUNDXINVISIBLE, MainActivity.GAMEHEIGHT/2 - 80);


        menuChildScene.setOnMenuItemClickListener(new MenuScene.IOnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
            {
                switch(pMenuItem.getID())
                {
                    case MENU_PLAY:
                        musicMain.pause();
                        SceneManager.getInstance().setScene(SceneManager.SceneType.SCENE_GAME);
                        return true;
                    case MENU_SCORE:
                        PlayGamesManager.showLeaderboard();
                        return true;
                    case MENU_SOUND_ON:
                        musicOff();
                        musicOnMenuItem.setX(SOUNDXINVISIBLE);
                        musicOffMenuItem.setX(SOUNDXVISIBLE);
                        return true;
                    case MENU_SOUND_OFF:
                        musicOn();
                        musicOnMenuItem.setX(SOUNDXVISIBLE);
                        musicOffMenuItem.setX(SOUNDXINVISIBLE);
                        return true;
                    default:
                        return false;
                }
            }
        });

        setChildScene(menuChildScene);
    }

    @Override
    public void onBackKeyPressed()
    {
        activity.finish();
    }

    @Override
    public SceneManager.SceneType getSceneType()
    {
        return SceneManager.SceneType.SCENE_MAIN;
    }

    @Override
    public void disposeScene()
    {
        menuChildScene.detachSelf();
        menuChildScene.dispose();
        this.detachSelf();
        this.dispose();
    }

    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }


}