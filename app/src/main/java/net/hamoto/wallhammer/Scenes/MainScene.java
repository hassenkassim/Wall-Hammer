package net.hamoto.wallhammer.Scenes;

import net.hamoto.wallhammer.MainActivity;
import net.hamoto.wallhammer.Manager.SceneManager;
import net.hamoto.wallhammer.Manager.ResourcesManager;

import org.andengine.audio.music.Music;
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
    Sprite logo;
    Sprite cloud1sprite;
    Sprite groundsprite;
    Sprite hammer;
    Sprite rad;
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
        addGround();
        addCloud();
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

    private void addCloud(){
        cloud1sprite = new Sprite(0, 0, 3072, 512, ResourcesManager.getInstance().cloud1_region, engine.getVertexBufferObjectManager());
        cloud1sprite.setY(600f);
        cloud1sprite.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new MoveXModifier(10f,256,-256))));
        attachChild(cloud1sprite);
    }

    private void addLogo(){
        logo = new Sprite(0, 0, 500, 300, ResourcesManager.getInstance().logo_region, engine.getVertexBufferObjectManager());
        logo.setPosition(MainActivity.GAMEWIDTH/2, 550);
        attachChild(logo);
    }

    private void addHammer(){
        hammer = new Sprite(0, 0, 357, 400, ResourcesManager.getInstance().hammer_region, engine.getVertexBufferObjectManager());
        hammer.setScale(0.5f);
        hammer.setPosition(130, 190);
        attachChild(hammer);
    }

    private void addRad(){
        rad = new Sprite(0, 0, 130, 130, ResourcesManager.getInstance().rad_region, engine.getVertexBufferObjectManager());
        rad.setScale(0.5f);
        rad.setPosition(148, 125);
        rad.registerEntityModifier(new LoopEntityModifier(new RotationModifier(1f, 0f, 359f)));
        attachChild(rad);
    }

    private void addMusicButton(){
        musicon = new ButtonSprite(0, 0, ResourcesManager.getInstance().soundon_region, engine.getVertexBufferObjectManager());
        musicon.setPosition(MainActivity.GAMEWIDTH - 100, 100);

        musicon.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                musicMain.pause();
                MainActivity.musicon = false;
                activity.getSharedPreferences(MainActivity.SETTING, activity.MODE_PRIVATE).edit().putBoolean(MainActivity.SETTING_MUSIC, false).apply();
                musicon.setEnabled(false);
                musicon.setVisible(false);
                musicoff.setEnabled(true);
                musicoff.setVisible(true);
            }
        });
        registerTouchArea(musicon);
        musicon.setScale(0.7f);

        musicoff = new ButtonSprite(0, 0, ResourcesManager.getInstance().soundoff_region, engine.getVertexBufferObjectManager());
        musicoff.setPosition(MainActivity.GAMEWIDTH - 100, 100);

        musicoff.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                musicMain.resume();
                musicon.setEnabled(true);
                musicon.setVisible(true);
                musicoff.setEnabled(false);
                musicoff.setVisible(false);
                MainActivity.musicon = true;
                activity.getSharedPreferences(MainActivity.SETTING, activity.MODE_PRIVATE).edit().putBoolean(MainActivity.SETTING_MUSIC, true).apply();
            }
        });
        registerTouchArea(musicoff);
        musicoff.setScale(0.7f);

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

        menuChildScene.addMenuItem(playMenuItem);
        menuChildScene.addMenuItem(optionsMenuItem);

        menuChildScene.buildAnimations();
        menuChildScene.setBackgroundEnabled(false);

        playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() - 80);
        optionsMenuItem.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY() - 100);

        menuChildScene.setOnMenuItemClickListener(new MenuScene.IOnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
            {
                switch(pMenuItem.getID())
                {
                    case MENU_PLAY:
                        MainActivity.gameToast("PLAY");
                        //TODO CALL GAME SCENE
                        musicMain.pause();
                        SceneManager.getInstance().setScene(SceneManager.SceneType.SCENE_GAME);
                        return true;
                    case MENU_OPTIONS:
                        MainActivity.gameToast("OPTIONS");
                        //TODO CALL SETTING SCENE
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


}