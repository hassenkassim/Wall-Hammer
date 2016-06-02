package net.hamoto.wallhammer.Scenes;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import net.hamoto.wallhammer.MainActivity;
import net.hamoto.wallhammer.Manager.PlayGamesManager;
import net.hamoto.wallhammer.Manager.SceneManager;
import net.hamoto.wallhammer.Manager.ResourcesManager;

import org.andengine.audio.music.Music;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.align.HorizontalAlign;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * @author Hassen Kassim
 * @version 1.0
 *
 * This Scene shows the Logo while loading the Main Scene.
 */
public class GameScene extends BaseScene implements IOnSceneTouchListener
{

    /*
    *
    *    VARIABLES
    *
     */
    final private int COUNT_WALL = 5;
    final private int DELAYMS = 1500;
    final private int TOUCHTIME_MIN = 700;
    final private int TOUCHTIME_MAX = 900;
    final private int NEXTWALL_MIN = 800;
    final private int NEXTWALL_MAX = 1500;
    final private int LEVEL_MAX = 12;
    final private int GAME_BACK = 0;
    final private int GAME_PLAYAGAIN = 1;
    final private int GAME_SHARE = 2;
    final private int GAME_SCORE = 3;
    final private int GAME_PAUSE = 4;
    final private int GAME_PLAYPAUSE = 5;
    final private String FACEBOOK = "com.facebook.katana";
    final private String TWITTER = "com.twitter.android";

    private boolean gameOverMusicActive = false;
    private int level;
    private int curwall;
    private int lastwall;
    private int speed;
    private long score;
    private long highscore;
    private float wheelspeed;
    private Date lasttouch;
    private Date actualtouch;
    private static Random rand;

    private PhysicsWorld world;

    private HUD gameHUD;

    private Sprite cloud1sprite;
    private Sprite cloud2sprite;
    private Sprite cloud3sprite;
    private Sprite cloud4sprite;
    private Sprite cloud5sprite;
    private Sprite cloud6sprite;
    private Sprite cloud7sprite;
    private Sprite cloud8sprite;
    private Sprite cloud9sprite;
    private Sprite cloud10sprite;
    private Sprite groundsprite;
    private Sprite hammer;
    private Sprite wheel;
    private Sprite scoreBackground;
    private Sprite newTextSprite;
    private ArrayList<Sprite> walls;

    private Body wheelBody;
    private Body hammerBody;

    private Text scoreText;
    private Text scoreGameOverText;
    private Text gameOverText;
    private Text highscoreText;

    public static Music musicGame;
    public static Music musicGameOver;
    public static Music musicWallDestroy;

    private MenuScene gameChildScene;
    private MenuScene gameChildScenePauseFunction;


    /*
    *
    *    METHODS
    *
     */

    @Override
    public void createScene()
    {
        initVariables(0,0,300, 1.0f);//init variablen
        initPhysics(); //init physics
        createBackground(); //set the background color
        createHUD(); //create the score field
        startBackgroundMusic(); //start background music
        addSprites(); //add figures
        initWalls(COUNT_WALL); //insert walls
        createTouchFunction(); //activate touch
        createGameOverText(); //show game over window
        createGameChildScenePauseFunction();

    }


    private void initVariables(int level, long score, int speed, float wheelspeed){
        setLevel(level);
        setScore(score);
        setSpeed(speed);
        setWheelSpeed(wheelspeed);
    }

    private void initPhysics()
    {
        world = new PhysicsWorld(new Vector2(0, -98.1f), false);
        this.registerUpdateHandler(world);
    }

    private void createBackground()
    {
        this.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
    }

    private void createHUD()
    {
        gameHUD = new HUD();
        scoreText = new Text(20, MainActivity.GAMEHEIGHT-65, resourcesManager.font, "Score: " + score, new TextOptions(HorizontalAlign.LEFT), vbom);
        scoreText.setAnchorCenter(0, 0);
        gameHUD.attachChild(scoreText);
        camera.setHUD(gameHUD);
    }

    private void startBackgroundMusic(){
        musicWallDestroy = ResourcesManager.getInstance().musicDestroyWall;
        musicGame = ResourcesManager.getInstance().musicGame;
        musicGame.setLooping(true);
        if(MainActivity.musicon){
            musicGame.play();

        }
    }

    private void addSprites(){
        addGround();
        addCloud();
        addHammer();
    }

    public void initWalls(int count){
        //create initial 'count' Walls
        walls = new ArrayList<Sprite>();
        int x = NEXTWALL_MAX;
        for(int i = 0; i < count; i++){
            createWall(x, 240, 64, 320);
            x = x + randInt(NEXTWALL_MIN, NEXTWALL_MAX);
        }
        curwall = 0;
        lastwall = walls.size()-1;
        //start the collision check
        initCollisionCheck();
    }

    private void initCollisionCheck(){
        //collision check
        this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                //checkWallOutside();
                checkHammerWallCollision();
            }

            @Override
            public void reset() {

            }

        });
    }

    private void checkHammerWallCollision(){
        if(lasttouch!=null){
            if(hammer.collidesWith(walls.get(curwall))){
                actualtouch = new Date();
                long a = actualtouch.getTime() - lasttouch.getTime();
                if(a<TOUCHTIME_MAX&&a>TOUCHTIME_MIN) {
                    Log.i("INFO: ", "Touched " + a + "ms before collision -> Destroy Wall and continue!");
                    destroyWall();
                }
                else {
                    //Gameover
                    Log.i("INFO: ", "Touched " + a + "ms before collision -> GAMEOVER! 1");
                    gameover();
                }
                lasttouch=null;
            }
        } else {
            if (hammer.collidesWith(walls.get(curwall))) {
                Log.i("INFO: ", "No touch at all -> GAMEOVER! 3");
                gameover();
            }
        }
    }

    private void destroyWall(){
        float from = walls.get(lastwall).getX() + randInt(NEXTWALL_MIN,NEXTWALL_MAX);
        float to = -128;
        walls.get(curwall).clearEntityModifiers();
        walls.get(curwall).setX(from);
        walls.get(curwall).registerEntityModifier(new MoveXModifier((from-to)/speed, from,to));
        //curwall und lastwall aktualisieren
        if(curwall == walls.size()-1) curwall = 0;
        else curwall++;
        if(lastwall == walls.size()-1) lastwall = 0;
        else lastwall++;
        addToScore(1);
        checklevel();
        if(MainActivity.musicon){
            musicWallDestroy.play();
        }

    }


    private void checklevel(){
        if(score%3 == 0&&level<LEVEL_MAX){
            levelup();
            MainActivity.gameToast("LEVEL UP");
        }
    }

    private void levelup(){
        incSpeed(50);
        incWheelSpeed(0.2f);
        adjustAnimations();
    }

    private void adjustAnimations(){
        for(int i = 0; i<walls.size(); i++){
            Sprite wall = walls.get(i);
            float from = wall.getX();
            float to = -128;
            wall.clearEntityModifiers();
            wall.registerEntityModifier(new MoveXModifier((from-to)/speed, from,to));
        }

        groundsprite.clearEntityModifiers();
        float from = groundsprite.getX();
        float to = -128;
        groundsprite.registerEntityModifier(new SequenceEntityModifier(new MoveXModifier((from+to)/speed, from, to), new LoopEntityModifier(new SequenceEntityModifier(new MoveXModifier(256.0f/speed,128,-128)))));

        wheel.clearEntityModifiers();
        wheel.registerEntityModifier(new LoopEntityModifier(new RotationModifier(1f/wheelspeed, 0f, 359f)));

    }

    private void gameover() {
        stopGame();
        displayGameOverText();
        Log.i("STATUS: ", "GAMEOVER!");
        createGameChildScene();
        musicGameOver = ResourcesManager.getInstance().musicGameOver;
        if(MainActivity.musicon){
            musicGameOver.play();
            gameOverMusicActive = true;
        }

    }

    private void createGameChildScenePauseFunction(){
        gameChildScenePauseFunction = new MenuScene(camera);
        gameChildScenePauseFunction.setPosition(0, 0);

        gameChildScenePauseFunction.buildAnimations();
        gameChildScenePauseFunction.setBackgroundEnabled(false);

        final IMenuItem pauseGameItem = new ScaleMenuItemDecorator(new SpriteMenuItem(GAME_PAUSE, resourcesManager.pauseButton_region, vbom), 1.5f, 1);
        final IMenuItem playPauseGameItem = new ScaleMenuItemDecorator(new SpriteMenuItem(GAME_PLAYPAUSE, resourcesManager.playPauseButton_region, vbom), 1.5f, 1);
        final IMenuItem backGameItem = new ScaleMenuItemDecorator(new SpriteMenuItem(GAME_BACK, resourcesManager.backToMenu_region, vbom), 1.5f, 1);
        final IMenuItem playagainGameItem = new ScaleMenuItemDecorator(new SpriteMenuItem(GAME_PLAYAGAIN, resourcesManager.playAgain_region, vbom), 1.5f, 1);
        final IMenuItem shareGameItem = new ScaleMenuItemDecorator(new SpriteMenuItem(GAME_SHARE, resourcesManager.share_region, vbom), 1.5f, 1);
        final IMenuItem scoreGameItem = new ScaleMenuItemDecorator(new SpriteMenuItem(GAME_SCORE, resourcesManager.score_region, vbom), 1.5f, 1);


        gameChildScenePauseFunction.addMenuItem(pauseGameItem);
        gameChildScenePauseFunction.addMenuItem(playPauseGameItem);
        gameChildScenePauseFunction.addMenuItem(backGameItem);
        gameChildScenePauseFunction.addMenuItem(playagainGameItem);
        gameChildScenePauseFunction.addMenuItem(shareGameItem);
        gameChildScenePauseFunction.addMenuItem(scoreGameItem);

        final float PAUSEXINVISIBLE = - 4000;
        final float PAUSEXVISIBLE = MainActivity.GAMEWIDTH -100;

        pauseGameItem.setPosition(PAUSEXVISIBLE, MainActivity.GAMEHEIGHT - 90);
        playPauseGameItem.setPosition(PAUSEXINVISIBLE, MainActivity.GAMEHEIGHT - 90);
        backGameItem.setPosition(PAUSEXINVISIBLE, MainActivity.GAMEHEIGHT/2 - 90);
        playagainGameItem.setPosition(PAUSEXINVISIBLE, MainActivity.GAMEHEIGHT/2 - 90);
        shareGameItem.setPosition(PAUSEXINVISIBLE, MainActivity.GAMEHEIGHT/2 - 90);
        scoreGameItem.setPosition(PAUSEXINVISIBLE, MainActivity.GAMEHEIGHT/2 - 90);


        gameChildScenePauseFunction.setOnMenuItemClickListener(new MenuScene.IOnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
            {
                switch(pMenuItem.getID())
                {
                    case GAME_PAUSE:
                        pauseGame();
                        pauseGameItem.setX(PAUSEXINVISIBLE);
                        playPauseGameItem.setX(PAUSEXVISIBLE);
                        backGameItem.setPosition(MainActivity.GAMEWIDTH/2 - 100, MainActivity.GAMEHEIGHT/2 - 90);
                        playagainGameItem.setPosition(MainActivity.GAMEWIDTH/2 - 300, MainActivity.GAMEHEIGHT/2 - 90);
                        shareGameItem.setPosition(MainActivity.GAMEWIDTH/2 + 300, MainActivity.GAMEHEIGHT/2 - 90);
                        scoreGameItem.setPosition(MainActivity.GAMEWIDTH/2 + 100, MainActivity.GAMEHEIGHT/2 - 90);
                        return true;
                    case GAME_PLAYPAUSE:
                        resumeGame();
                        playPauseGameItem.setX(PAUSEXINVISIBLE);
                        pauseGameItem.setX(PAUSEXVISIBLE);
                        backGameItem.setPosition(PAUSEXINVISIBLE, MainActivity.GAMEHEIGHT/2 - 90);
                        playagainGameItem.setPosition(PAUSEXINVISIBLE, MainActivity.GAMEHEIGHT/2 - 90);
                        shareGameItem.setPosition(PAUSEXINVISIBLE, MainActivity.GAMEHEIGHT/2 - 90);
                        scoreGameItem.setPosition(PAUSEXINVISIBLE, MainActivity.GAMEHEIGHT/2 - 90);
                        return true;
                    case GAME_BACK:
                        goBack();
                        return true;
                    case GAME_PLAYAGAIN:
                        SceneManager.getInstance().loadGameScene(engine);
                        return true;
                    case GAME_SHARE:
                        SharingToSocialMedia("NOPE");
                        return true;
                    case GAME_SCORE:
                        PlayGamesManager.showLeaderboard();
                    default:
                        return false;
                }
            }
        });

        setChildScene(gameChildScenePauseFunction);
    }
    public void pauseGame(){
        if(MainActivity.musicon){
             musicGame.pause();
        }

        this.setIgnoreUpdate(true);
    }

    public void resumeGame(){
        if(MainActivity.musicon){
            musicGame.resume();
        }

        this.setIgnoreUpdate(false);
    }




    private void createGameChildScene()
        {
        gameChildScene = new MenuScene(camera);
        gameChildScene.setPosition(0, 0);

        final IMenuItem backGameItem = new ScaleMenuItemDecorator(new SpriteMenuItem(GAME_BACK, resourcesManager.backToMenu_region, vbom), 1.5f, 1);
        final IMenuItem playagainGameItem = new ScaleMenuItemDecorator(new SpriteMenuItem(GAME_PLAYAGAIN, resourcesManager.playAgain_region, vbom), 1.5f, 1);
        final IMenuItem shareGameItem = new ScaleMenuItemDecorator(new SpriteMenuItem(GAME_SHARE, resourcesManager.share_region, vbom), 1.5f, 1);
        final IMenuItem scoreGameItem = new ScaleMenuItemDecorator(new SpriteMenuItem(GAME_SCORE, resourcesManager.score_region, vbom), 1.5f, 1);

        gameChildScene.addMenuItem(backGameItem);
        gameChildScene.addMenuItem(playagainGameItem);
        gameChildScene.addMenuItem(shareGameItem);
        gameChildScene.addMenuItem(scoreGameItem);

        gameChildScene.buildAnimations();
        gameChildScene.setBackgroundEnabled(false);

        backGameItem.setPosition(backGameItem.getX() - 100, MainActivity.GAMEHEIGHT/2 - 90);
        playagainGameItem.setPosition(playagainGameItem.getX() - 300, MainActivity.GAMEHEIGHT/2 - 90);
        shareGameItem.setPosition(shareGameItem.getX() + 300, MainActivity.GAMEHEIGHT/2 - 90);
        scoreGameItem.setPosition(scoreGameItem.getX() + 100, MainActivity.GAMEHEIGHT/2 - 90);

        gameChildScene.setOnMenuItemClickListener(new MenuScene.IOnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
            {
                switch(pMenuItem.getID())
                {
                    case GAME_BACK:
                        goBack();
                        return true;
                    case GAME_PLAYAGAIN:
                        SceneManager.getInstance().loadGameScene(engine);
                        musicGameOver.stop();
                        return true;
                    case GAME_SHARE:
                        SharingToSocialMedia("NOPE");
                        return true;
                    case GAME_SCORE:
                        PlayGamesManager.showLeaderboard();
                    default:
                        return false;
                }
            }
        });

        setChildScene(gameChildScene);
    }

    private void goBack(){
        musicGame.stop();
        if(gameOverMusicActive) {
            musicGameOver.stop();
        }
        if(MainActivity.musicon){
            MainScene.musicMain.resume();
        }
        SceneManager.getInstance().loadMainScene(engine);
    }

    /*
    Facebook - "com.facebook.katana"
    Twitter - "com.twitter.android"
    Instagram - "com.instagram.android"
    Pinterest - "com.pinterest"
     */
    public void SharingToSocialMedia(String application) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Neues Highscore!");
        intent.putExtra(Intent.EXTRA_TEXT, "Ich hab einen neuen Highscore aufgestellt: " + score + ". \n Kannst du es mit mir aufnehmen?");
        if(application.equals("NOPE")){
            activity.startActivity(intent);
        } else{
            boolean installed = appInstalledOrNot(application);
            if (installed) {
                intent.setPackage(application);
                activity.startActivity(intent);
            } else {
                MainActivity.gameToast("App not installed yet");
            }
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = activity.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }


    private void stopGame(){
        this.setIgnoreUpdate(true);
        musicGame.stop();
    }


    private void addToScore(int i) {
        setScore(score + i);
        scoreText.setText("Score: " + score);
    }
    private void addGround(){
        groundsprite = new Sprite(0, 0, 3000, 256, ResourcesManager.getInstance().ground_region, engine.getVertexBufferObjectManager());
        groundsprite.setY(-25f);
        groundsprite.registerEntityModifier(new LoopEntityModifier(new MoveXModifier(256.0f/speed,128,-128)));
        final FixtureDef GROUND_FIX = PhysicsFactory.createFixtureDef(0.0f,0.0f,0.0f);
        Body groundBody = PhysicsFactory.createBoxBody(world, groundsprite, BodyDef.BodyType.StaticBody, GROUND_FIX);
        groundBody.setTransform(groundBody.getPosition().x, groundBody.getPosition().y-0.85f, 0.0f);
        attachChild(groundsprite);

    }
    private void addCloud(){


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
        hammer.setAnchorCenter(0.5f,0.0f);
        hammer.setOffsetCenterY(0.15f);
        hammer.setPosition(250.0f, 300.0f);
        final FixtureDef HAMMER_FIX = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
        hammerBody = PhysicsFactory.createBoxBody(world, hammer, BodyDef.BodyType.DynamicBody, HAMMER_FIX);
        attachChild(hammer);
        world.registerPhysicsConnector(new PhysicsConnector(hammer, hammerBody, true, false));

        wheel = new Sprite(0, 0, ResourcesManager.getInstance().wheel_region, engine.getVertexBufferObjectManager());
        wheel.setScale(0.3f);
        wheel.setPosition(250.0f, 300.0f);
        final FixtureDef WHEEL_FIX = PhysicsFactory.createFixtureDef(1.0f, 0.0f, 0.0f);
        wheelBody = PhysicsFactory.createBoxBody(world, wheel, BodyDef.BodyType.DynamicBody, WHEEL_FIX);
        wheel.registerEntityModifier(new LoopEntityModifier(new RotationModifier(1f/wheelspeed, 0f, 359f)));
        attachChild(wheel);
        world.registerPhysicsConnector(new PhysicsConnector(wheel, wheelBody, true, false));

        final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = hammerBody;
        revoluteJointDef.bodyB = wheelBody;
        revoluteJointDef.localAnchorA.set(new Vector2(0.25f, -3.4f));
        revoluteJointDef.localAnchorB.set(new Vector2(0.0f, 0.0f));
        revoluteJointDef.collideConnected = false;
        revoluteJointDef.referenceAngle = 90.0f;
        revoluteJointDef.lowerAngle = -10.0f;
        revoluteJointDef.upperAngle = 10.0f;
        world.createJoint(revoluteJointDef);
    }

    private void createWall(int x, int y, int width, int height){
        Sprite wall = new Sprite(x, y, width, height, ResourcesManager.getInstance().wall_region, engine.getVertexBufferObjectManager());
        walls.add(wall);
        final FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0.0f,0.0f,0.0f);
        Body wallBody = PhysicsFactory.createBoxBody(world, wall, BodyDef.BodyType.DynamicBody, WALL_FIX);
        wall.registerEntityModifier(new MoveXModifier((wall.getX()+128.0f)/speed,wall.getX(),-128));
        attachChild(wall);
    }

    private void createGameOverText()
    {
        scoreBackground = new Sprite (0, 0, 474, 318, ResourcesManager.getInstance().scoreBackground_region, engine.getVertexBufferObjectManager());
        gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!", vbom);
        highscoreText = new Text(0, 0, resourcesManager.font, "Highscore: 0", vbom);
        scoreGameOverText = new Text(0, 0, resourcesManager.font, "Score: 0", vbom);
        newTextSprite = new Sprite (0, 0, 41, 18, ResourcesManager.getInstance().new_region, engine.getVertexBufferObjectManager());
    }

    private void displayGameOverText()
    {
        highscore = getHighscore();

        if(score > highscore){
            activity.getSharedPreferences(MainActivity.HIGHSCORE, Context.MODE_PRIVATE).edit().putLong(MainActivity.HIGHSCORE, score).apply();
            highscore = score;
            newTextSprite.setScale(1.4f);
            newTextSprite.setPosition(MainActivity.GAMEWIDTH/2 - 186, MainActivity.GAMEHEIGHT/2 + 165);
            PlayGamesManager.postHighscore(score);
        }

        clearHUD();
        camera.setChaseEntity(null);
        scoreBackground.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT/2 +180);
        scoreBackground.setAlpha(0.95f);
        gameOverText.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT/2 + 260);
        highscoreText.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT/2 + 160);
        scoreGameOverText.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT/2 + 100);
        scoreGameOverText.setText("Score: " + score);
        highscoreText.setText("Highscore: " + highscore);
        gameOverText.setScale(1,2);
        attachChild(scoreBackground);
        attachChild(gameOverText);
        attachChild(highscoreText);
        attachChild(scoreGameOverText);
        attachChild(newTextSprite);
        musicGame.stop();
    }



    @Override
    public void onBackKeyPressed()
    {
        //pauseGame();
    }

    private void createTouchFunction()
    {
        setTouchAreaBindingOnActionDownEnabled(true);
        this.setOnSceneTouchListener(this);
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if(pSceneTouchEvent.isActionDown()) {
            Date now = new Date();
            if(lasttouch!=null){
                if(now.getTime() - lasttouch.getTime()>DELAYMS){
                    hammerHit(now);

                }
                else{
                    Log.i("INFO: ","Touched less then 2 seconds ago!!");
                }
            } else {
                hammerHit(now);
            }
            return true;
        }
        return false;
    }

    private void hammerHit(Date now){
        lasttouch = now;
        hammer.registerEntityModifier(new SequenceEntityModifier(new RotationModifier(0.5f, 0.0f, -45.0f), new DelayModifier(0.2f), new RotationModifier(0.1f,-45.0f, 22.5f),new RotationModifier(0.25f, 22.5f, 15.0f),  new RotationModifier(0.25f,15.0f, 0.0f)));
    }

    private void clearHUD(){
        if(gameHUD!=null){
            gameHUD.detachChildren();
            gameHUD.detachSelf();
        }
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(int min, int max) {
        rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private void setLevel(int level){
        this.level = level;
    }

    private void setScore(long score){
        this.score = score;
    }

    private void setSpeed(int speed){
        this.speed = speed;
    }

    private void setWheelSpeed(float wheelspeed){
        this.wheelspeed = wheelspeed;
    }

    private void incSpeed(int incspeed){
        this.speed += incspeed;
    }

    private void decSpeed(int decspeed){
        this.speed -= decspeed;
    }

    private void incWheelSpeed(float incWheelspeed){
        this.wheelspeed += incWheelspeed;
    }

    private void decWheelSpeed(int decWheelspeed){
        this.wheelspeed -= decWheelspeed;
    }

    private long getHighscore(){
        return activity.getSharedPreferences(MainActivity.HIGHSCORE, Context.MODE_PRIVATE).getLong(MainActivity.HIGHSCORE, 0);
    }

    @Override
    public SceneManager.SceneType getSceneType()
    {
        return SceneManager.SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene()
    {
        clearHUD();
        this.detachSelf();
        this.dispose();
    }

}