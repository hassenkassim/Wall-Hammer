package net.hamoto.wallhammer.Scenes;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import net.hamoto.wallhammer.MainActivity;
import net.hamoto.wallhammer.Manager.PlayGamesManager;
import net.hamoto.wallhammer.Manager.ResourcesManager;
import net.hamoto.wallhammer.Manager.SceneManager;

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
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.SurfaceGestureDetectorAdapter;
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
public class GameScene extends BaseScene implements IOnSceneTouchListener{

    /*
    *
    *    VARIABLES
    *
     */
    final private int COUNT_WALL = 50;
    final private int WALL0 = 0;
    final private int WALL1 = 1;
    final private int WALL2 = 2;
    final private int WALL0_WIDTH = 64;
    final private int WALL0__HEIGHT = 304;
    final private int WALL1_WIDTH = 64;
    final private int WALL1__HEIGHT = 159;
    final private int WALL2_WIDTH = 64;
    final private int WALL2__HEIGHT = 159;
    final private int WALLS_Y_GROUND = 240;
    final private int WALLS_Y_GROUND2 = 175;
    final private int WALLS_Y_UP = 350;
    final private int DELAYMS = 500;
    final private int TOUCHTIME_MIN = 300;
    final private int TOUCHTIME_MAX = 400;
    final private int LEVEL_MAX = 10;
    final private int GAME_BACK = 0;
    final private int GAME_PLAYAGAIN = 1;
    final private int GAME_SHARE = 2;
    final private int GAME_SCORE = 3;
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
    private Date lastswipeup;
    private Date lastswipedown;
    private static Random rand;
    private int NEXTWALL_MIN;
    private int NEXTWALL_MAX;
    private boolean walloutsidehelper;

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
    private AnimatedSprite explosion;
    private ArrayList<Sprite> walls;
    private ArrayList<Integer> walltypes;

    private Body wheelBody;
    private Body hammerBody;

    private Text scoreText;
    private Text scoreGameOverText;
    private Text gameOverText;
    private Text highscoreText;

    public static Music musicGame;
    public static Music musicGameOver;
    public static Music soundWallDestroy;
    public static Music soundJump;

    private MenuScene GameOverChildScene;

    private SurfaceGestureDetectorAdapter surfaceGestureDetectorAdapter;


    /*
    *
    *    METHODS
    *
     */

    @Override
    public void createScene()
    {
        initVariables(0,0,500, 1.6f);//init variablen
        initPhysics(); //init physics
        createBackground(); //set the background color
        createHUD(); //create the score field
        startBackgroundMusic(); //start background music
        addSprites(); //add figures
        initWalls(COUNT_WALL); //insert walls
        createTouchFunction(); //activate touch
        createGameOverChildScene(); //Create game over window
    }


    private void initVariables(int level, long score, int speed, float wheelspeed){
        NEXTWALL_MAX = 1500;
        NEXTWALL_MIN = 1000;
        walloutsidehelper = false;
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

        soundWallDestroy = ResourcesManager.getInstance().soundWallDestroy;
        soundJump = ResourcesManager.getInstance().soundJump;
        musicGame = ResourcesManager.getInstance().musicGame;

        if(musicGame!=null){
            musicGame.setLooping(true);
            if(MainActivity.musicon){
                musicGame.play();
            }
        }
    }

    private void addSprites(){
        addGround();
        addCloud();
        addHammer();
    }

    public void initWalls(int count){

        /*Create coupe Walls
            Wall Types:
                        - WALL0 : Standard Wall: Gross, Breakable
                        - WALL1 : Klein Wall Oben: Klein, oben, unbreakable: Spieler muss sich ducken
                        - WALL2 : Klein Wall Unten: Klein, unten, unbreakable: Spieler muss springen
        */

        //create initial 'count' Walls
        walls = new ArrayList<Sprite>();
        walltypes = new ArrayList<Integer>();
        int walltype;
        int x = NEXTWALL_MAX; //X-Coordinates of current Wall
        for(int i = 0; i < count; i++){
            walltype = randInt(0,2);
            switch (walltype){
                case WALL0:
                    createWall(WALL0, x, WALLS_Y_GROUND);
                    break;
                case WALL1:
                    createWall(WALL1, x, WALLS_Y_UP);
                    break;
                case WALL2:
                    createWall(WALL2, x, WALLS_Y_GROUND2);
                    break;
                default:
                    break;
            }
            x = x + randInt(NEXTWALL_MIN, NEXTWALL_MAX); //set new X-Coordinate
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
                checkWallOutside();
                checkHammerWallCollision();
            }

            @Override
            public void reset() {

            }

        });
    }

    private void checkExplosionDetach(){
        if(explosion!=null&&explosion.getCurrentTileIndex() == explosion.getTileCount()-1){
            detachChild(explosion);
        }
    }

    private void checkWallOutside(){

        if(walloutsidehelper==false&&walls.get(curwall).getX() + walls.get(curwall).getWidth()/2 < 200){
            walloutsidehelper = true;
            addToScore(1);
            checklevel();
        }
        if(walls.get(curwall).getX() + walls.get(curwall).getWidth()/2 < - 20){
            walloutsidehelper = false;
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
        }
    }


    private void checkHammerWallCollision(){
        Sprite currentwall = walls.get(curwall);
        int currentwalltype = walltypes.get(curwall).intValue();

        switch (currentwalltype){
            case WALL0:
                if(hammer.collidesWith(currentwall)||wheel.collidesWith(currentwall)){
                    if(lasttouch==null) {
                        gameover();
                    }
                    else{
                        actualtouch = new Date();
                        long a = actualtouch.getTime() - lasttouch.getTime();
                        if(a<(TOUCHTIME_MAX-3*level)&&a>(TOUCHTIME_MIN+3*level)) {
                            //Log.i("INFO: ", "Touched " + a + "ms before collision -> Destroy Wall and continue!");
                            destroyWall();
                        }
                        else {
                            //Log.i("INFO: ", "Touched " + a + "ms before collision -> GAMEOVER! 1");
                            gameover();
                        }
                        lasttouch=null;
                    }
                }
                break;
            case WALL1:
                if(hammer.collidesWith(currentwall)){
                    gameover();
                }
                break;
            case WALL2:
                if(wheel.collidesWith(currentwall)){
                    gameover();
                }
                break;
            default:
                break;
        }
    }

    private void destroyWall(){
        float coordX = walls.get(curwall).getX() + (walls.get(curwall).getWidth()/2);
        explosion = new AnimatedSprite(0, 0,resourcesManager.explosion_region , engine.getVertexBufferObjectManager());
        explosion.setPosition(coordX, 200);
        attachChild(explosion);
        explosion.animate(50, false, new AnimatedSprite.IAnimationListener() {
            @Override
            public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {

            }

            @Override
            public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {

            }

            @Override
            public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {

            }

            @Override
            public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
                detachChild(explosion);
            }
        });

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
        playWallDestroySound();
    }


    private void playWallDestroySound(){
        if(MainActivity.musicon){
            soundWallDestroy.play();
        }
    }

    private void playJumpSound() {
        if (MainActivity.musicon) {
            //if(soundWallDestroyActive == true){
            //    soundWallDestroy.stop();
            //    soundWallDestroy.play();
            //}else if(soundJumpActive == true){
            //    soundJump.stop();
            //    soundWallDestroy.play();
            //}else{
            //    soundWallDestroy.play();
            //
            soundJump.play();
        }
    }

    private void checklevel(){
        if(score%3 == 0&&level<LEVEL_MAX){
            levelup();
        }
    }

    private void levelup(){
        setLevel(level+1);
        incSpeed(50);
        incWheelSpeed(0.2f);
        setNextWallMin();
        adjustAnimations();
    }

    private void setNextWallMin(){
        NEXTWALL_MIN -=50;
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

    private void gameover(){
        stopGame();
        displayGameOverText();
        //Log.i("STATUS: ", "GAMEOVER!");
        /*createGameChildScene();
        musicGameOver = ResourcesManager.getInstance().musicGameOver;
        if(MainActivity.musicon){
            musicGameOver.play();
            gameOverMusicActive = true;
        }*/
    }

    private void createGameOverButtons(){
        GameOverChildScene = new MenuScene(camera);
        GameOverChildScene.setPosition(0, 0);

        GameOverChildScene.buildAnimations();
        GameOverChildScene.setBackgroundEnabled(false);

        final IMenuItem backGameItem = new ScaleMenuItemDecorator(new SpriteMenuItem(GAME_BACK, resourcesManager.backToMenu_region, vbom), 1.5f, 1);
        final IMenuItem playagainGameItem = new ScaleMenuItemDecorator(new SpriteMenuItem(GAME_PLAYAGAIN, resourcesManager.playAgain_region, vbom), 1.5f, 1);
        final IMenuItem shareGameItem = new ScaleMenuItemDecorator(new SpriteMenuItem(GAME_SHARE, resourcesManager.share_region, vbom), 1.5f, 1);
        final IMenuItem scoreGameItem = new ScaleMenuItemDecorator(new SpriteMenuItem(GAME_SCORE, resourcesManager.score_region, vbom), 1.5f, 1);


        GameOverChildScene.addMenuItem(backGameItem);
        GameOverChildScene.addMenuItem(playagainGameItem);
        GameOverChildScene.addMenuItem(shareGameItem);
        GameOverChildScene.addMenuItem(scoreGameItem);


        backGameItem.setPosition(MainActivity.GAMEWIDTH/2 - 100, MainActivity.GAMEHEIGHT/2 - 90);
        playagainGameItem.setPosition(MainActivity.GAMEWIDTH/2 - 300, MainActivity.GAMEHEIGHT/2 - 90);
        shareGameItem.setPosition(MainActivity.GAMEWIDTH/2 + 300, MainActivity.GAMEHEIGHT/2 - 90);
        scoreGameItem.setPosition(MainActivity.GAMEWIDTH/2 + 100, MainActivity.GAMEHEIGHT/2 - 90);


        GameOverChildScene.setOnMenuItemClickListener(new MenuScene.IOnMenuItemClickListener() {
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

        setChildScene(GameOverChildScene);
        GameOverChildScene.setPosition(MainActivity.GAMEWIDTH, MainActivity.GAMEHEIGHT);
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
        intent.putExtra(Intent.EXTRA_TEXT, "Ich hab einen neuen Highscore aufgestellt: " + score + ". \nKannst du es mit mir aufnehmen?");
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
        final FixtureDef WHEEL_FIX = PhysicsFactory.createFixtureDef(0.7f, 0.0f, 0.0f);
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

    private void createWall(int walltype, int x, int y){
        Sprite wall = null;
        switch (walltype){
            case WALL0:
                wall = new Sprite(x, y, WALL0_WIDTH, WALL0__HEIGHT, ResourcesManager.getInstance().wall_region, engine.getVertexBufferObjectManager());
                break;
            case WALL1:
                wall = new Sprite(x, y, WALL1_WIDTH, WALL1__HEIGHT, ResourcesManager.getInstance().wallSmall_region, engine.getVertexBufferObjectManager());
                break;
            case WALL2:
                wall = new Sprite(x, y, WALL2_WIDTH, WALL2__HEIGHT, ResourcesManager.getInstance().wallSmall_region, engine.getVertexBufferObjectManager());
                break;
            default:
                break;
        }
        if(wall != null) {
            walls.add(wall);
            walltypes.add(walltype);
            wall.registerEntityModifier(new MoveXModifier((wall.getX() + 128.0f) / speed, wall.getX(), -128));
            attachChild(wall);
        }
    }



    private void createGameOverChildScene() {
        scoreBackground = new Sprite(0, 0, 511, 318, ResourcesManager.getInstance().scoreBackground_region, engine.getVertexBufferObjectManager());
        gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!", vbom);
        highscoreText = new Text(0, 0, resourcesManager.font, "Highscore: 0", vbom);
        scoreGameOverText = new Text(0, 0, resourcesManager.font, "Score: 0", vbom);
        newTextSprite = new Sprite(0, 0, 35, 18, ResourcesManager.getInstance().new_region, engine.getVertexBufferObjectManager());

        scoreBackground.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT/2 +180);
        scoreBackground.setAlpha(0.95f);
        gameOverText.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT/2 + 260);
        highscoreText.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT/2 + 160);
        scoreGameOverText.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT/2 + 100);
        scoreGameOverText.setText("Score: " + score);
        highscoreText.setText("Highscore: " + highscore);
        attachChild(scoreBackground);
        attachChild(gameOverText);
        attachChild(highscoreText);
        attachChild(scoreGameOverText);
        attachChild(newTextSprite);
        scoreBackground.setVisible(false);
        gameOverText.setVisible(false);
        highscoreText.setVisible(false);
        scoreGameOverText.setVisible(false);
        newTextSprite.setVisible(false);

        createGameOverButtons();

    }

    private void displayGameOverText()
    {
        highscore = getHighscore();

        if(score > highscore){
            activity.getSharedPreferences(MainActivity.HIGHSCORE, Context.MODE_PRIVATE).edit().putLong(MainActivity.HIGHSCORE, score).apply();
            highscore = score;
            newTextSprite.setScale(1.4f);
            newTextSprite.setPosition(MainActivity.GAMEWIDTH/2 - 224, MainActivity.GAMEHEIGHT/2 + 175);
            newTextSprite.setVisible(true);
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
        scoreBackground.setVisible(true);
        gameOverText.setVisible(true);
        highscoreText.setVisible(true);
        scoreGameOverText.setVisible(true);

        GameOverChildScene.setPosition(0,0);
        GameOverChildScene.setBackgroundEnabled(false);

        musicGame.stop();
    }


    @Override
    public void onBackKeyPressed()
    {
    }

    private void createTouchFunction()
    {
        if(Looper.myLooper()==null){
            Looper.prepare();
        }
        surfaceGestureDetectorAdapter = new SurfaceGestureDetectorAdapter(activity) {

            @Override
            protected boolean onSingleTap() {
                //Log.i("Touch Event" , "SINGLE_TAP");

                Date now = new Date();
                if(lasttouch!=null){
                    if(now.getTime() - lasttouch.getTime()>DELAYMS){
                        hammerHit(now);
                    }
                    else{
                        //Log.i("INFO: ","Touched less then 2 seconds ago!!");
                    }
                } else {
                    hammerHit(now);
                }
                return false;
            }

            @Override
            protected boolean onSwipeDown() {
                //Log.i("Touch Event" , "SWIPE_DOWN");
                if(lastswipedown==null) { //initial jump
                    lastswipedown = new Date();
                    duck();
                } else if((new Date().getTime())-lastswipedown.getTime()>(1000-(level*50))){ //lock swipe up for duck time
                    lastswipedown = new Date();
                    duck();
                }
                return false;
            }

            @Override
            protected boolean onSwipeUp() {
                //Log.i("Touch Event" , "SWIPE_UP ");
                if(lastswipeup==null) { //initial jump
                    lastswipeup = new Date();
                    jump();
                } else if((new Date().getTime())-lastswipeup.getTime()>800){ //lock swipe up for 0.8 seconds
                    lastswipeup = new Date();
                    jump();
                }
                return false;
            }
        };
        surfaceGestureDetectorAdapter.setEnabled(true);

        setTouchAreaBindingOnActionDownEnabled(true);
        this.setOnSceneTouchListener(this);
    }

    //hammer jump function
    private void jump(){
        //Log.i("Player -> " , "Jump!");
        wheelBody.setLinearVelocity(new Vector2(0.0f,55.0f-level));
        playJumpSound();
    }

    //hammer duck function
    private void duck(){
        float factor = 0.05f;
        float step1 = 0.2f;
        float step2 = 0.7f-(level*factor);
        float step3 = 0.2f;
        float step4 = 0.05f;
        hammer.registerEntityModifier(new SequenceEntityModifier(new RotationModifier(step1, 0.0f, -80.0f), new DelayModifier(step2), new RotationModifier(step3,-90.0f, 5.0f),new RotationModifier(step4, 5.0f, 0.0f))); //0.55
    }

    //hammer hit function
    private void hammerHit(Date now){
        lasttouch = now;
        hammer.registerEntityModifier(new SequenceEntityModifier(new RotationModifier(0.2f, 0.0f, -22.5f), new DelayModifier(0.1f), new RotationModifier(0.05f,-22.5f, 30f),new RotationModifier(0.1f, 30f, 15.0f),  new RotationModifier(0.1f,15.0f, 0.0f))); //0.55
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

    private void incWheelSpeed(float incWheelspeed){
        this.wheelspeed += incWheelspeed;
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