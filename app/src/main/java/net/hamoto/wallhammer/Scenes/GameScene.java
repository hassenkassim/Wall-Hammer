package net.hamoto.wallhammer.Scenes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import net.hamoto.wallhammer.MainActivity;
import net.hamoto.wallhammer.Manager.SceneManager;
import net.hamoto.wallhammer.Manager.ResourcesManager;

import org.andengine.audio.music.Music;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
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
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
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
    final private int COUNT_WALL = 5;
    final private int DELAYMS = 2000;
    final private int TOUCHTIME_MIN = 200;
    final private int TOUCHTIME_MAX = 500;
    final private String FACEBOOK = "com.facebook.katana";
    final private String TWITTER = "com.twitter.android";

    public static Music musicGame;
    public static Music musicGameOver;

    private HUD gameHUD;
    private Sprite cloud1sprite;
    private Sprite groundsprite;
    private Sprite hammer;
    private Sprite rad;
    private Sprite scoreBackground;
    private Text scoreText;
    private Text scoreGameOverText;
    private long score = 0;
    private PhysicsWorld world;
    private int counter = 0;
    private int curwall;
    private int lastwall;
    private long highscore;
    private Date lasttouch;
    private Date actualtouch;
    private ArrayList<Sprite> walls;
    private Text gameOverText;
    private Text highscoreText;
    private boolean gameOverDisplayed = false;
    MenuScene gameChildScene;
    final int GAME_BACK = 0;
    final int GAME_PLAYAGAIN = 1;
    final int GAME_SHARE = 2;
    final int GAME_SCORE = 3;
    private static SharedPreferences prefs;
    private Body radBody;
    private Body hammerBody;
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
        initPhysics(); //Physik Welt initiieren
        createBackground(); //Hintergrund erstellen
        createHUD(); //Score anzeigen
        startBackgroundMusic(); //Hintergrundmusik starten
        addSprites(); //Figuren Einfuegen
        initWalls(COUNT_WALL); //Waende einfuegen starten COUNT_WALL Waende generieren
        createTouchFunction(); //Touch aktivieren
        createGameOverText(); //GameOverText generieren
    }


    private void initPhysics()
    {
        world = new PhysicsWorld(new Vector2(0, -9.81f), false);
        this.registerUpdateHandler(world);
    }

    private void createBackground()
    {
        this.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
    }

    private void createHUD()
    {
        gameHUD = new HUD();
        scoreText = new Text(20, MainActivity.GAMEHEIGHT-65, resourcesManager.font, "Score: "+score, new TextOptions(HorizontalAlign.LEFT), vbom);
        scoreText.setAnchorCenter(0, 0);
        gameHUD.attachChild(scoreText);
        camera.setHUD(gameHUD);
    }

    private void startBackgroundMusic(){
        musicGame = ResourcesManager.getInstance().musicGame;
        musicGameOver = ResourcesManager.getInstance().musicGameOver;
        musicGame.setLooping(true);
        if(MainActivity.musicon){
            musicGame.play();
        }
    }

    private void addSprites(){
        addGround();
        addClouds();
        addHammer();
        //addRad();
    }


    public void initWalls(int count){
        //create initial 'count' Walls
        walls = new ArrayList<Sprite>();
        int x = 1500;
        for(int i = 0; i < count; i++){
            createWall(x, 250, 64, 320);
            x = x + randInt(300, 1200);
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

    private void checkWallOutside(){
        if(walls.get(curwall).getX() + walls.get(curwall).getWidth() < 0){
            float from = walls.get(lastwall).getX() + randInt(300,1000);
            float to = -128;
            walls.get(curwall).clearEntityModifiers();
            walls.get(curwall).registerEntityModifier(new SequenceEntityModifier(new MoveXModifier((from-to)/300, from,to)));
            //curwall und lastwall aktualisieren
            if(curwall == walls.size()-1) curwall = 0;
            else curwall++;
            if(lastwall == walls.size()-1) lastwall = 0;
            else lastwall++;
        }
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
        float from = walls.get(lastwall).getX() + randInt(300,1000);
        float to = -128;
        walls.get(curwall).clearEntityModifiers();
        walls.get(curwall).registerEntityModifier(new SequenceEntityModifier(new MoveXModifier((from-to)/300, from,to)));
        //curwall und lastwall aktualisieren
        if(curwall == walls.size()-1) curwall = 0;
        else curwall++;
        if(lastwall == walls.size()-1) lastwall = 0;
        else lastwall++;
        addToScore(1);
    }

    private void gameover() {
        stopGame();
        displayGameOverText();
        Log.i("STATUS: ", "GAMEOVER!");
        createGameChildScene();
        musicGameOver.play();
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
                        //MainActivity.gameToast("Back");
                        //TODO CALL MENU SCENE
                        onBackKeyPressed();
                        return true;
                    case GAME_PLAYAGAIN:
                        SceneManager.getInstance().loadGameScene(engine);
                        return true;
                        //TODO CALL GAME SCENE
                    case GAME_SHARE:
                        //MainActivity.gameToast("Share");
                        SharingToSocialMedia("NOPE");
                        return true;
                    case GAME_SCORE:

                    default:
                        return false;
                }
            }
        });

        setChildScene(gameChildScene);
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
        score += i;
        scoreText.setText("Score: " + score);
    }
    private void addGround(){
        groundsprite = new Sprite(0, 0, 3000, 256, ResourcesManager.getInstance().ground_region, engine.getVertexBufferObjectManager());
        groundsprite.setY(-25f);
        groundsprite.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new MoveXModifier(256.0f/300.0f,128,-128))));
        final FixtureDef GROUND_FIX = PhysicsFactory.createFixtureDef(0.0f,0.0f,0.0f);
        Body groundBody = PhysicsFactory.createBoxBody(world, groundsprite, BodyDef.BodyType.StaticBody, GROUND_FIX);
        attachChild(groundsprite);

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


    private void addHammer(){
        hammer = new Sprite(0, 0, ResourcesManager.getInstance().hammer_region, engine.getVertexBufferObjectManager());
        hammer.setScale(0.3f);
        hammer.setPosition(130, 300);
        final FixtureDef HAMMER_FIX = PhysicsFactory.createFixtureDef(1.0f, 0.2f, 0.0f);
        hammerBody = PhysicsFactory.createBoxBody(world, hammer, BodyDef.BodyType.DynamicBody, HAMMER_FIX);
        attachChild(hammer);
        world.registerPhysicsConnector(new PhysicsConnector(hammer, hammerBody, true, false));

        rad = new Sprite(0, 0, ResourcesManager.getInstance().rad_region, engine.getVertexBufferObjectManager());
        rad.setScale(0.3f);
        rad.setPosition(140, 300);
        final FixtureDef RAD_FIX = PhysicsFactory.createFixtureDef(1000.0f, 0.2f, 0.0f);
        radBody = PhysicsFactory.createBoxBody(world, rad, BodyDef.BodyType.DynamicBody, RAD_FIX);
        rad.registerEntityModifier(new LoopEntityModifier(new RotationModifier(1f, 0f, 359f)));
        attachChild(rad);
        world.registerPhysicsConnector(new PhysicsConnector(rad, radBody, true, false));
        //radBody.setLinearVelocity(new Vector2(0.0f, -9.81f));



        final WeldJointDef weldJointDef = new WeldJointDef();
        weldJointDef.initialize(hammerBody, radBody, radBody.getWorldCenter());
        weldJointDef.localAnchorA.set(weldJointDef.localAnchorA.x-0.1f, -3.4f);
        world.createJoint(weldJointDef);




        /*final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(hammerBody, radBody, radBody.getWorldCenter());
        revoluteJointDef.localAnchorA.set(0.1f* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,0.1f * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
        revoluteJointDef.localAnchorB.set(0,0);
        world.createJoint(revoluteJointDef);*/
    }

    private void createWall(int x, int y, int width, int height){
        Sprite wall = new Sprite(x, y, width, height, ResourcesManager.getInstance().wall_region, engine.getVertexBufferObjectManager());
        walls.add(wall);
        final FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0.0f,0.0f,0.0f);
        Body wallBody = PhysicsFactory.createBoxBody(world, wall, BodyDef.BodyType.DynamicBody, WALL_FIX);
        wall.registerEntityModifier(new SequenceEntityModifier(new MoveXModifier((wall.getX()+128.0f)/300.0f,wall.getX(),-128)));
        attachChild(wall);
    }

    private void createGameOverText()
    {
        scoreBackground = new Sprite (0, 0, 431, 318, ResourcesManager.getInstance().scoreBackground_region, engine.getVertexBufferObjectManager());
        gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!", vbom);
        highscoreText = new Text(0, 0, resourcesManager.font, "Highscore: 0", vbom);
        scoreGameOverText = new Text(0, 0, resourcesManager.font, "Score: 0", vbom);

    }

    private void displayGameOverText()
    {

        if(score > MainActivity.highscore){
            MainActivity.highscore = score;
            activity.getSharedPreferences(MainActivity.SETTING2, Context.MODE_PRIVATE).edit().putLong(MainActivity.SETTING_HIGHSCORE, score).apply();

        }

        clearHUD();
        camera.setChaseEntity(null);
        scoreBackground.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT/2 +180);
        scoreBackground.setAlpha(0.95f);
        gameOverText.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT/2 + 260);
        highscoreText.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT/2 + 160);
        scoreGameOverText.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT/2 + 100);
        scoreGameOverText.setText("Score: " + score);
        highscoreText.setText("Highscore: " + MainActivity.highscore);
        gameOverText.setScale(1,2);
        attachChild(scoreBackground);
        attachChild(gameOverText);
        attachChild(highscoreText);
        attachChild(scoreGameOverText);
        gameOverDisplayed = true;
        musicGame.stop();
    }


    @Override
    public void onBackKeyPressed()
    {
        musicGame.stop();
        musicGameOver.stop();
        if(MainActivity.musicon){
            MainScene.musicMain.resume();

        }
        SceneManager.getInstance().loadMainScene(engine);
    }

    private void createTouchFunction()
    {
        setTouchAreaBindingOnActionDownEnabled(true);
        this.setOnSceneTouchListener(this);
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if(pSceneTouchEvent.isActionDown()) {

            hammerBody.applyLinearImpulse(new Vector2(0.0f, 10000.0f),new Vector2(0.0f,0.0f));
            //radBody.applyLinearImpulse(new Vector2(0.0f, 10000.0f),new Vector2(0.0f,0.0f));

            Date now = new Date();
            if(lasttouch!=null){
                if(now.getTime() - lasttouch.getTime()>DELAYMS){
                    lasttouch = now;
                }
                else{
                    Log.i("INFO: ","Touched less then 2 seconds ago!!");
                }
            } else {
                lasttouch = now;
            }
            //MainActivity.gameToast("" + lasttouch);
            //Jump only if the user tapped, not moved his finger or something
            //walls.get(0).setPosition(walls.get(0).getX(),walls.get(0).getY()+200);
            //hammer.setPosition(hammer.getX(),hammer.getY() + 200);
            /*final Entity hammer = this.hammer;//Get player entity here.
            final float jumpDuration = 1;
            final float startY = hammer.getY();
            final float jumpHeight = 20;

            final MoveYModifier moveUpModifier = new MoveYModifier(jumpDuration / 2, startY, startY - jumpHeight); // - since we want the sprite to go up.
            final MoveYModifier moveDownModifier = new MoveYModifier(jumpDuration / 2, startY + jumpHeight, startY);
            final SequenceEntityModifier modifier = new SequenceEntityModifier(moveUpModifier, moveDownModifier);

            hammer.registerEntityModifier(modifier);*/
            return true;
        }
        return false;
    }

    private void clearHUD(){
        if(gameHUD!=null){
            gameHUD.detachChildren();
            gameHUD.detachSelf();
//            gameHUD.dispose();
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