package net.hamoto.wallhammer.Scenes;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import net.hamoto.wallhammer.Manager.SceneManager;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.color.Color;

import net.hamoto.wallhammer.MainActivity;

import java.util.Random;

/**
 * @author Hassen Kassim
 * @version 1.0
 *
 * This Scene shows the Logo while loading the Gameplay Scene.
 */
public class SplashScene extends BaseScene
{
    private Sprite splashBackground;
    private Sprite splashLogo;
    private Sprite splashText;
    private PhysicsWorld world;

    @Override
    public void createScene()
    {
        initPhysics();
        createBackground();
        addGround();
        addSplashText();
        addSplashLogo();
    }

    private void initPhysics(){
        world = new PhysicsWorld(new Vector2(0.0f,-150.0f), false);
        this.registerUpdateHandler(world);
    }

    private void createBackground(){
        Rectangle background = new Rectangle(MainActivity.GAMEWIDTH/2,MainActivity.GAMEHEIGHT/2,MainActivity.GAMEWIDTH, MainActivity.GAMEHEIGHT, this.engine.getVertexBufferObjectManager());
        background.setColor(Color.BLACK);
        attachChild(background);
        splashBackground = new Sprite(0, 0, resourcesManager.splashBackground_region, vbom);
        splashBackground.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT/2);
        splashBackground.setAlpha(0.0f);
        attachChild(splashBackground);
        splashBackground.registerEntityModifier(new AlphaModifier(1.5f,0.0f, 1.0f));
    }

    private void addGround(){
        FixtureDef GROUND_FIX = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
        Rectangle ground = new Rectangle(MainActivity.GAMEWIDTH/2,0,MainActivity.GAMEWIDTH, 50, this.engine.getVertexBufferObjectManager());
        ground.setAlpha(0.0f);
        PhysicsFactory.createBoxBody(world, ground, BodyDef.BodyType.StaticBody, GROUND_FIX);
        attachChild(ground);
    }

    private void addSplashText(){
        splashText = new Sprite(0, 0, resourcesManager.splashText_region, vbom);
        splashText.setScale(0.35f);
        splashText.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT + splashText.getHeight() + 100);
        FixtureDef SPLASHTEXT_FIX = PhysicsFactory.createFixtureDef(0.1f, 0.3f, 0.0f);
        Body splashTextBody = PhysicsFactory.createBoxBody(world, splashText, BodyDef.BodyType.DynamicBody, SPLASHTEXT_FIX);
        attachChild(splashText);
        world.registerPhysicsConnector(new PhysicsConnector(splashText, splashTextBody, true, true));
    }

    private void addSplashLogo(){
        splashLogo = new Sprite(0, 0, resourcesManager.splashLogo_region, vbom);
        splashLogo.setPosition(MainActivity.GAMEWIDTH/2, MainActivity.GAMEHEIGHT*0.7f);
        splashLogo.registerEntityModifier(new LoopEntityModifier(new RotationModifier(2.5f, 0f, 359f)));
        attachChild(splashLogo);
        splashLogo.registerEntityModifier(new AlphaModifier(1.0f, 0.0f, 1.0f));
        splashLogo.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new ScaleModifier(0.25f, 0.35f, 0.30f), new DelayModifier(0.5f), new ScaleModifier(0.25f,0.30f,0.35f),new DelayModifier(0.2f))));
    }


    @Override
    public void onBackKeyPressed()
    {

    }

    @Override
    public SceneManager.SceneType getSceneType()
    {
        return SceneManager.SceneType.SCENE_SPLASH;
    }

    @Override
    public void disposeScene()
    {
        splashLogo.detachSelf();
        splashLogo.dispose();
        splashText.detachSelf();
        splashText.dispose();
        this.detachSelf();
        this.dispose();
    }
}