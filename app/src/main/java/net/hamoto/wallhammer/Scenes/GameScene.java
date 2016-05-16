package net.hamoto.wallhammer.Scenes;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

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
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.align.HorizontalAlign;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * @author Hassen Kassim
 * @version 1.0
 *
 * This Scene shows the Logo while loading the Main Scene.
 */
public class GameScene extends BaseScene implements IOnSceneTouchListener
{
    private HUD gameHUD;
    Sprite cloud1sprite;
    Sprite groundsprite;
    Sprite hammer;
    Sprite rad;
    public static Music musicGame;
    public static Music musicGameOver;
    private Text scoreText;
    private Text gameOverText;
    private long score = 0;
    TouchEvent  GameSceneTouchEvent;
    Scene GameScene;
    PhysicsWorld world;
    private int counter = 0;


    private ArrayList<Sprite> walls;
    final private int COUNT_WALL = 5;

    @Override
    public void createScene()
    {

        initPhysics(); //Physik Welt initiieren
        createBackground(); //Hintergrund erstellen
        addSprites(); //Figuren Einfuegen
        startWalls(COUNT_WALL); //Waende einfuegen starten 100 Waende generieren TODO: Ineffizient! Neuer Algorithmus mit weniger OverHead
        createHUD(); //Score anzeigen
        startbackgroundmusic(); //Hintergrundmusik starten
        createTouchFunction(); //Touch aktivieren
        addToScore(counter);
    }


    private void initPhysics()
    {
        world = new PhysicsWorld(new Vector2(0, -9.81f), false);
        this.registerUpdateHandler(world);
    }


    public void startWalls(int count){
        //create initial 'count' Walls
        walls = new ArrayList<Sprite>();
        int x = 1500;
        for(int i = 0; i < count; i++){
            createWall(x, 300, 64, 256);
            x = x + randInt(300, 1200);

        }

        //collision check
        this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                for(int i = 0; i< COUNT_WALL; i++){
                    //if(hammer.collidesWith(walls.get(i))){
                    //  //TODO:Explosion einbauen?
                    //  walls.get(i).setVisible(false);
                    //  musicGame.stop();
                    //
                    //  if(MainActivity.musicon){
                    //      musicGameOver.play();
                    //  }
                    //  groundsprite.clearEntityModifiers();
                    //  cloud1sprite.clearEntityModifiers();
                    //  rad.clearEntityModifiers();
                    //  walls.get(0).clearEntityModifiers();
                    //  walls.get(1).clearEntityModifiers();
                    //  walls.get(2).clearEntityModifiers();
                    //  walls.get(3).clearEntityModifiers();
                    //  walls.get(4).clearEntityModifiers();
                    //  gameOverText = new Text(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT/2, resourcesManager.font, "Game Over", new TextOptions(HorizontalAlign.LEFT), vbom);
                    //  attachChild(gameOverText);

                    //}
                 if(walls.get(i).getX() + walls.get(i).getWidth() < 0){
                     MainActivity.gameToast("Wall weg");
                     walls.get(i).setX(walls.get(i + 1).getX() + randInt(50,200));
                     walls.get(i).registerEntityModifier(new SequenceEntityModifier(new MoveXModifier(walls.get(i).getX()/300,walls.get(i).getX(),-128)));
                 }

                }
            }

            @Override
            public void reset() {

            }
        });

    }

    private void createWall(int x, int y, int width, int height){
        Sprite wall = new Sprite(x, y, width, height, ResourcesManager.getInstance().wall_region, engine.getVertexBufferObjectManager());
        walls.add(wall);
        final FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0.0f,0.0f,0.0f);
        Body wallBody = PhysicsFactory.createBoxBody(world, wall, BodyDef.BodyType.DynamicBody, WALL_FIX);
        wall.registerEntityModifier(new SequenceEntityModifier(new MoveXModifier(wall.getX()/300,wall.getX(),-128)));
        attachChild(wall);
    }



    private void createBackground()
    {
        this.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
    }

    private void createHUD()
    {
        gameHUD = new HUD();
        scoreText = new Text(20, MainActivity.GAMEHEIGHT-65, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        scoreText.setAnchorCenter(0, 0);
        scoreText.setText("Score: 0");
        gameHUD.attachChild(scoreText);
        camera.setHUD(gameHUD);
    }


    private void addToScore(int i)
    {
        score += i;
        scoreText.setText("Score: " + score);
    }

    private void addSprites(){
        addGround();
        addCloud();
        addHammer();
        addRad();
    }

    private void startbackgroundmusic(){
        musicGame = ResourcesManager.getInstance().musicGame;
        musicGameOver = ResourcesManager.getInstance().musicGameOver;
        musicGame.setLooping(true);
        if(MainActivity.musicon){
            musicGame.play();
        }
    }

    private void addGround(){
        groundsprite = new Sprite(0, 0, 3000, 256, ResourcesManager.getInstance().ground_region, engine.getVertexBufferObjectManager());
        groundsprite.setY(-25f);
        groundsprite.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new MoveXModifier(1f,128,-128))));
        final FixtureDef GROUND_FIX = PhysicsFactory.createFixtureDef(0.0f,0.0f,0.0f);
        Body groundBody = PhysicsFactory.createBoxBody(world, groundsprite, BodyDef.BodyType.StaticBody, GROUND_FIX);
        attachChild(groundsprite);

    }

    private void addCloud(){
        cloud1sprite = new Sprite(0, 0, 3072, 512, ResourcesManager.getInstance().cloud1_region, engine.getVertexBufferObjectManager());
        cloud1sprite.setY(600f);
        cloud1sprite.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new MoveXModifier(10f,256,-256))));
        attachChild(cloud1sprite);
    }

    private void addHammer(){
        hammer = new Sprite(0, 0, 357, 400, ResourcesManager.getInstance().hammer_region, engine.getVertexBufferObjectManager());
        hammer.setScale(0.5f);
        hammer.setPosition(130, 190);
        final FixtureDef HAMMER_FIX = PhysicsFactory.createFixtureDef(100.0f, 0.2f, 0.0f);
        Body hammerBody = PhysicsFactory.createBoxBody(world, hammer, BodyDef.BodyType.DynamicBody, HAMMER_FIX);
        attachChild(hammer);
        world.registerPhysicsConnector(new PhysicsConnector(hammer, hammerBody, true, false));
    }

    private void addRad(){
        rad = new Sprite(0, 0, 130, 130, ResourcesManager.getInstance().rad_region, engine.getVertexBufferObjectManager());
        rad.setScale(0.5f);
        rad.setPosition(148, 125);
        rad.registerEntityModifier(new LoopEntityModifier(new RotationModifier(1f, 0f, 359f)));
        //final FixtureDef RAD_FIX = PhysicsFactory.createFixtureDef(100.0f, 0.2f, 0.0f);
        //Body radBody = PhysicsFactory.createBoxBody(world, rad, BodyDef.BodyType.DynamicBody, RAD_FIX);
        //world.registerPhysicsConnector(new PhysicsConnector(rad, radBody, true, false));
        attachChild(rad);

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

    @Override
    public SceneManager.SceneType getSceneType()
    {
        return SceneManager.SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene()
    {
        this.detachSelf();
        this.dispose();
    }




    private void createTouchFunction()
    {
        setTouchAreaBindingOnActionDownEnabled(true);
        this.setOnSceneTouchListener(this);
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        MainActivity.gameToast("Touch TEST");
        if(pSceneTouchEvent.isActionDown()) { //Jump only if the user tapped, not moved his finger or something
            //walls.get(0).setPosition(walls.get(0).getX(),walls.get(0).getY()+200);
            hammer.setPosition(hammer.getX(),hammer.getY() + 200);
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


}