package net.hamoto.wallhammer.Scenes;

import android.content.Context;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import net.hamoto.wallhammer.MainActivity;
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
    final int MENU_OPTIONS = 1;
    final int MENU_SOUND_ON = 2;
    final int MENU_SOUND_OFF = 3;
    Sprite logo;
    Sprite cloud1sprite;
    Sprite groundsprite;
    Sprite hammer;
    Sprite rad;
    ButtonSprite musicon;
    ButtonSprite musicoff;
    public static Music musicMain;
    private ArrayList<Sprite> clouds;
    private int curcloud;
    private int lastcloud;
    final private int COUNT_CLOUDS = 8;
    final private int minCloudDiff = 400;
    final private int maxCloudDiff = 650;

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
        addMusicButton();
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
        logo = new Sprite(0, 0, 500, 300, ResourcesManager.getInstance().logo_region, engine.getVertexBufferObjectManager());
        logo.setPosition(MainActivity.GAMEWIDTH/2, 550);
        attachChild(logo);
    }

    private void createCloud(int a, int x, int y, int width, int height){
        if(a == 0) {
            Sprite cloud = new Sprite(x, y, width, height, ResourcesManager.getInstance().cloud1_region, engine.getVertexBufferObjectManager());
            clouds.add(cloud);
            //final FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0.0f,0.0f,0.0f);
            //Body wallBody = PhysicsFactory.createBoxBody(world, wall, BodyDef.BodyType.DynamicBody, WALL_FIX);
            cloud.registerEntityModifier(new SequenceEntityModifier(new MoveXModifier((cloud.getX() + 300.0f) / 50, cloud.getX(), -300)));
            attachChild(cloud);
        }else if(a == 1){
            Sprite cloud = new Sprite(x, y, width, height, ResourcesManager.getInstance().cloud2_region, engine.getVertexBufferObjectManager());
            clouds.add(cloud);
            cloud.registerEntityModifier(new SequenceEntityModifier(new MoveXModifier((cloud.getX() + 300) / 50, cloud.getX(), -300)));
            attachChild(cloud);
        }else if(a == 2){
            Sprite cloud = new Sprite(x, y, width, height, ResourcesManager.getInstance().cloud3_region, engine.getVertexBufferObjectManager());
            clouds.add(cloud);
            cloud.registerEntityModifier(new SequenceEntityModifier(new MoveXModifier((cloud.getX() + 300) / 50, cloud.getX(), -300)));
            attachChild(cloud);
        }else{
            Sprite cloud = new Sprite(x, y, width, height, ResourcesManager.getInstance().cloud4_region, engine.getVertexBufferObjectManager());
            clouds.add(cloud);
            cloud.registerEntityModifier(new SequenceEntityModifier(new MoveXModifier((cloud.getX() + 300) / 50, cloud.getX(), -300)));
            attachChild(cloud);
        }

    }

    public void initClouds(int count){
        //create initial 'count' Walls
        clouds = new ArrayList<Sprite>();
        int x = 300;
        int cloudKind = 0;
        for(int i = 0; i < count; i++){
            createCloud(cloudKind, x, 600, 285, 156);
            x = x + randInt(minCloudDiff, maxCloudDiff);
            cloudKind = randInt(0,3);
        }
        curcloud = 0;
        lastcloud = clouds.size()-1;


    }


    private void checkCloudOutside(){
        if(clouds.get(curcloud).getX() < - 150){
            float from = clouds.get(lastcloud).getX() + randInt(minCloudDiff,maxCloudDiff);
            float to = -300;
            clouds.get(curcloud).clearEntityModifiers();
            clouds.get(curcloud).registerEntityModifier(new SequenceEntityModifier(new MoveXModifier((from-to)/50, from,to)));
            //curwall und lastwall aktualisieren
            if(curcloud == clouds.size()-1) curcloud = 0;
            else curcloud++;
            if(lastcloud == clouds.size()-1) lastcloud = 0;
            else lastcloud++;
        }
    }

    private void addClouds(){
        initClouds(COUNT_CLOUDS);
        //checkCloudOutside();
       this.registerUpdateHandler(new IUpdateHandler() {
           @Override
           public void onUpdate(float pSecondsElapsed) {
               //checkWallOutside();
               checkCloudOutside();
           }

           @Override
           public void reset() {

           }

       });
    }

  // private void addCloud(){
  //     cloud1sprite = new Sprite(0, 0, 285, 156, ResourcesManager.getInstance().cloud1_region, engine.getVertexBufferObjectManager());
  //     cloud1sprite.setY(600f);
  //     cloud1sprite.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new MoveXModifier(10f,256,-256))));
  //     attachChild(cloud1sprite);
  // }


    private void addHammer(){
        hammer = new Sprite(0, 0, ResourcesManager.getInstance().hammer_region, engine.getVertexBufferObjectManager());
        hammer.setScale(0.3f);
        hammer.setPosition(130, 190);
        attachChild(hammer);
    }

    private void addRad(){
        rad = new Sprite(0, 0, 273, 273, ResourcesManager.getInstance().rad_region, engine.getVertexBufferObjectManager());
        rad.setScale(0.3f);
        rad.setPosition(148, 125);
        rad.registerEntityModifier(new LoopEntityModifier(new RotationModifier(1f, 0f, 359f)));
        attachChild(rad);
    }

    private void addMusicButton(){
        musicon = new ButtonSprite(0, 0, ResourcesManager.getInstance().soundon_region, engine.getVertexBufferObjectManager());
        musicon.setPosition(MainActivity.GAMEWIDTH/2 + 200, MainActivity.GAMEHEIGHT/2 - 80);

        musicon.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                musicMain.pause();
                MainActivity.musicon = false;
                activity.getSharedPreferences(MainActivity.SETTING, Context.MODE_PRIVATE).edit().putBoolean(MainActivity.SETTING_MUSIC, false).apply();
                musicon.setEnabled(false);
                musicon.setVisible(false);
                musicoff.setEnabled(true);
                musicoff.setVisible(true);
            }
        });
        registerTouchArea(musicon);


        musicoff = new ButtonSprite(0, 0, ResourcesManager.getInstance().soundoff_region, engine.getVertexBufferObjectManager());
        musicoff.setPosition(MainActivity.GAMEWIDTH/2 + 200, MainActivity.GAMEHEIGHT/2 - 80);

        musicoff.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                musicMain.resume();
                musicon.setEnabled(true);
                musicon.setVisible(true);
                musicoff.setEnabled(false);
                musicoff.setVisible(false);
                MainActivity.musicon = true;
                activity.getSharedPreferences(MainActivity.SETTING, Context.MODE_PRIVATE).edit().putBoolean(MainActivity.SETTING_MUSIC, true).apply();
            }
        });
        registerTouchArea(musicoff);


        if(MainActivity.musicon){
            musicoff.setVisible(false);
            musicoff.setEnabled(false);
        } else{
            musicon.setVisible(false);
            musicon.setEnabled(false);
        }
        attachChild(musicon);
        attachChild(musicoff);
    }

    private void createMenuChildScene()
    {
        menuChildScene = new MenuScene(camera);
        menuChildScene.setPosition(0, 0);

        final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.5f, 1);
        final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_OPTIONS, resourcesManager.options_region, vbom), 1.5f, 1);
        final IMenuItem musicOnMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_SOUND_ON, resourcesManager.soundon_region, vbom), 1.5f, 1);
        final IMenuItem musicOffMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_SOUND_OFF, resourcesManager.soundoff_region, vbom), 1.5f, 1);

        menuChildScene.addMenuItem(playMenuItem);
        menuChildScene.addMenuItem(optionsMenuItem);
        menuChildScene.addMenuItem(musicOnMenuItem);
        menuChildScene.addMenuItem(musicOffMenuItem);


        menuChildScene.buildAnimations();
        menuChildScene.setBackgroundEnabled(false);

        final float SOUNDXVISIBLE = MainActivity.GAMEWIDTH/2 + 200;
        final float SOUNDXINVISIBLE = -200;

        playMenuItem.setPosition(MainActivity.GAMEWIDTH/2 - 200, MainActivity.GAMEHEIGHT/2 - 80);
        optionsMenuItem.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT/2 - 80);
        musicOnMenuItem.setPosition(SOUNDXVISIBLE, MainActivity.GAMEHEIGHT/2 - 80);
        musicOffMenuItem.setPosition(SOUNDXINVISIBLE, MainActivity.GAMEHEIGHT/2 - 80);


        menuChildScene.setOnMenuItemClickListener(new MenuScene.IOnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
            {
                switch(pMenuItem.getID())
                {
                    case MENU_PLAY:
                        //MainActivity.gameToast("PLAY");
                        //TODO CALL GAME SCENE
                        musicMain.pause();
                        SceneManager.getInstance().setScene(SceneManager.SceneType.SCENE_GAME);
                        return true;
                    case MENU_OPTIONS:
                        //MainActivity.gameToast("OPTIONS");
                        //TODO CALL SETTING SCENE
                        return true;
                    case MENU_SOUND_ON:
                        //MainActivity.gameToast("SOUND ON");
                        musicOnMenuItem.setX(SOUNDXINVISIBLE);
                        musicOffMenuItem.setX(SOUNDXVISIBLE);
                        return true;
                    case MENU_SOUND_OFF:
                        //MainActivity.gameToast("SOUND OFF");
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