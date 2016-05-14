package net.hamoto.wallhammer.Scenes;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import net.hamoto.wallhammer.MainActivity;
import net.hamoto.wallhammer.Manager.SceneManager;
import net.hamoto.wallhammer.Manager.ResourcesManager;

import org.andengine.audio.music.Music;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.Entity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
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
    public static Music music;
    private Text scoreText;
    private int score = 0;
    TouchEvent  GameSceneTouchEvent;
    Scene GameScene;

    PhysicsWorld world;

    @Override
    public void createScene()
    {
        initPhysics();
        createBackground();
        addSprites();
        createHUD();
        startbackgroundmusic();
        setTouchAreaBindingOnActionDownEnabled(true);
        createTouchFunction();
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
        //addRad();
    }

    private void startbackgroundmusic(){
        music = ResourcesManager.getInstance().music2;
        music.setLooping(true);
        if(MainActivity.musicon){
            music.play();
        }
    }

    private void addGround(){
        groundsprite = new Sprite(0, 0, 3000, 256, ResourcesManager.getInstance().ground_region, engine.getVertexBufferObjectManager());
        groundsprite.setY(-25f);
        groundsprite.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new MoveXModifier(1f,128,-128))));
        final FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0.0f,0.0f,0.0f);
        Body groundBody = PhysicsFactory.createBoxBody(world, groundsprite, BodyDef.BodyType.StaticBody, WALL_FIX);
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
        hammer.setPosition(130, 500);
        final FixtureDef HAMMER_FIX = PhysicsFactory.createFixtureDef(100.0f, 0.5f, 0.0f);
        Body hammerBody = PhysicsFactory.createBoxBody(world, hammer, BodyDef.BodyType.DynamicBody, HAMMER_FIX);
        attachChild(hammer);
        world.registerPhysicsConnector(new PhysicsConnector(hammer, hammerBody, true, false));
    }

    private void addRad(){
        rad = new Sprite(0, 0, 130, 130, ResourcesManager.getInstance().rad_region, engine.getVertexBufferObjectManager());
        rad.setScale(0.5f);
        rad.setPosition(148, 125);
        rad.registerEntityModifier(new LoopEntityModifier(new RotationModifier(1f, 0f, 359f)));
        attachChild(rad);
    }


    @Override
    public void onBackKeyPressed()
    {
        music.stop();
        if(MainActivity.musicon){
            MainScene.music.resume();
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
        this.setOnSceneTouchListener(this);
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        MainActivity.gameToast("Touch TEST");
        if(pSceneTouchEvent.isActionDown()) { //Jump only if the user tapped, not moved his finger or something

            hammer.setPosition(300,300);
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



}