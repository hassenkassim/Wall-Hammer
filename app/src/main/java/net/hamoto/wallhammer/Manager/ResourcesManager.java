package net.hamoto.wallhammer.Manager;

import android.graphics.Color;

import net.hamoto.wallhammer.MainActivity;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.io.IOException;


/**
 * @author Hassen Kassim
 * @author www.hamoto.net
 * @version 1.0
 *
 * The ResourcesManager is responsible for loading/unloading the game resources (art, fonts, audio).
 */
public class ResourcesManager
{
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------

    private static int   SPR_COLUMN  = 1;
    private static int   SPR_ROWS  = 3;

    private static final ResourcesManager INSTANCE = new ResourcesManager();

    public Engine engine;
    public MainActivity activity;
    public Camera camera;
    public VertexBufferObjectManager vbom;
    TextureManager texmng;

    public Music musicMain;
    public Music musicGame;
    public Music musicGameOver;
    public Music soundWallDestroy;
    public Music soundJump;

    public Font font;
    public Font font2;

    //---------------------------------------------
    // TEXTURES & TEXTURE REGIONS
    //---------------------------------------------
    public ITextureRegion splashText_region;
    public ITextureRegion splashLogo_region;
    public ITextureRegion splashBackground_region;
    public ITextureRegion menu_background_region;
    public ITextureRegion play_region;
    public ITextureRegion options_region;
    public ITextureRegion info_region;
    public ITextureRegion infoBG_region;
    public ITextureRegion ground_region;
    public ITextureRegion cloud1_region;
    public ITextureRegion cloud2_region;
    public ITextureRegion cloud3_region;
    public ITextureRegion cloud4_region;
    public ITextureRegion logo_region;
    public ITextureRegion soundon_region;
    public ITextureRegion soundoff_region;
    public ITextureRegion hammer_region;
    public ITextureRegion wheel_region;
    public ITextureRegion wallSmall_region;
    public ITextureRegion wall_region;
    public ITextureRegion playAgain_region;
    public ITextureRegion share_region;
    public ITextureRegion backToMenu_region;
    public ITextureRegion score_region;
    public ITextureRegion score_region2;
    public ITextureRegion scoreBackground_region;
    public ITextureRegion new_region;
    public ITextureRegion pauseButton_region;
    public ITextureRegion playPauseButton_region;
    public ITextureRegion gameTitle_region;
    public ITextureRegion gameDescription_region;
    public ITextureRegion cancelButton_region;
    public TiledTextureRegion explosion_region;


    BitmapTextureAtlas splashTextTextureAtlas;
    BitmapTextureAtlas splashLogoTextureAtlas;
    BitmapTextureAtlas splashBackgroundTextureAtlas;
    BuildableBitmapTextureAtlas menuTextureAtlas;
    BuildableBitmapTextureAtlas groundTextureAtlas;
    BuildableBitmapTextureAtlas cloud1TextureAtlas;
    BuildableBitmapTextureAtlas cloud2TextureAtlas;
    BuildableBitmapTextureAtlas cloud3TextureAtlas;
    BuildableBitmapTextureAtlas cloud4TextureAtlas;
    BuildableBitmapTextureAtlas logoTextureAtlas;
    BuildableBitmapTextureAtlas soundonTextureAtlas;
    BuildableBitmapTextureAtlas soundoffTextureAtlas;
    BuildableBitmapTextureAtlas hammerTextureAtlas;
    BuildableBitmapTextureAtlas wheelTextureAtlas;
    BuildableBitmapTextureAtlas wallTextureAtlas;
    BuildableBitmapTextureAtlas wallSmallTextureAtlas;
    BuildableBitmapTextureAtlas playAgainTextureAtlas;
    BuildableBitmapTextureAtlas playTextureAtlas;
    BuildableBitmapTextureAtlas optionsTextureAtlas;
    BuildableBitmapTextureAtlas infoTextureAtlas;
    BuildableBitmapTextureAtlas infoBGTextureAtlas;
    BuildableBitmapTextureAtlas shareTextureAtlas;
    BuildableBitmapTextureAtlas backToMenuTextureAtlas;
    BuildableBitmapTextureAtlas scoreTextureAtlas;
    BuildableBitmapTextureAtlas scoreTextureAtlas2;
    BuildableBitmapTextureAtlas scoreBackgroundTextureAtlas;
    BuildableBitmapTextureAtlas newTextureAtlas;
    BuildableBitmapTextureAtlas pauseButtonTextureAtlas;
    BuildableBitmapTextureAtlas playPauseButtonTextureAtlas;
    BuildableBitmapTextureAtlas gameTitleTextureAtlas;
    BuildableBitmapTextureAtlas gameDescriptionTextureAtlas;
    BuildableBitmapTextureAtlas cancelButtonTextureAtlas;
    BitmapTextureAtlas explosionTextureAtlas;






    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

    /**
     * @param engine Gameengine
     * @param activity
     * @param camera
     * @param vbom
     * <br><br>
     * We use this method at beginning of game loading, to prepare Resources Manager properly,
     * setting all needed parameters, so we can later access them from different classes (eg. scenes)
     */
    public static void prepareManager(Engine engine, MainActivity activity, Camera camera, VertexBufferObjectManager vbom)
    {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }

    public void loadMainResources()
    {
        try {
            loadMainGraphics();
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            e.printStackTrace();
        }
        try {
            loadMainAudio();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadFonts();
    }

    private void loadMainGraphics() throws ITextureAtlasBuilder.TextureAtlasBuilderException {
        texmng = engine.getTextureManager();
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");

        gameDescriptionTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 2524, 1092, TextureOptions.BILINEAR);
        gameDescription_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameDescriptionTextureAtlas, activity, "gameDescription.png");
        gameDescriptionTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        gameDescriptionTextureAtlas.load();

        playTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 356, 356, TextureOptions.BILINEAR);
        play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(playTextureAtlas, activity, "playButton3.png");
        playTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        playTextureAtlas.load();

        optionsTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 172, 172, TextureOptions.BILINEAR);
        options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "settingsButton.png");
        optionsTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        optionsTextureAtlas.load();

        infoTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 356, 356, TextureOptions.BILINEAR);
        info_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(infoTextureAtlas, activity, "infoButton2.png");
        infoTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        infoTextureAtlas.load();


        infoBGTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 989, 664, TextureOptions.BILINEAR);
        infoBG_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(infoBGTextureAtlas, activity, "infoBackground.png");
        infoBGTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        infoBGTextureAtlas.load();

        shareTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 356, 356, TextureOptions.BILINEAR);
        share_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shareTextureAtlas, activity, "shareButton2.png");
        shareTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        shareTextureAtlas.load();

        backToMenuTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 356, 356, TextureOptions.BILINEAR);
        backToMenu_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backToMenuTextureAtlas, activity, "menuButton2.png");
        backToMenuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        backToMenuTextureAtlas.load();

        playAgainTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 356, 356, TextureOptions.BILINEAR);
        playAgain_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(playAgainTextureAtlas, activity, "playAgainButton2.png");
        playAgainTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        playAgainTextureAtlas.load();

        scoreTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 356, 356, TextureOptions.BILINEAR);
        score_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(scoreTextureAtlas, activity, "score2Button2.png");
        scoreTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        scoreTextureAtlas.load();

        scoreTextureAtlas2 = new BuildableBitmapTextureAtlas(texmng, 356, 356, TextureOptions.BILINEAR);
        score_region2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(scoreTextureAtlas2, activity, "score2Button3.png");
        scoreTextureAtlas2.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        scoreTextureAtlas2.load();

        pauseButtonTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 88, 88, TextureOptions.BILINEAR);
        pauseButton_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(pauseButtonTextureAtlas, activity, "pause.png");
        pauseButtonTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        pauseButtonTextureAtlas.load();

        playPauseButtonTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 88, 88, TextureOptions.BILINEAR);
        playPauseButton_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(playPauseButtonTextureAtlas, activity, "playPause.png");
        playPauseButtonTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        playPauseButtonTextureAtlas.load();

        cancelButtonTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 356, 356, TextureOptions.BILINEAR);
        cancelButton_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(cancelButtonTextureAtlas, activity, "cancelButton.png");
        cancelButtonTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        cancelButtonTextureAtlas.load();

        groundTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 256, 256, TextureOptions.REPEATING_BILINEAR);
        ground_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(groundTextureAtlas, activity, "ground.png");
        ground_region.setTextureSize(3000, 256);
        groundTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        groundTextureAtlas.load();


        cloud1TextureAtlas = new BuildableBitmapTextureAtlas(texmng, 286, 170, TextureOptions.REPEATING_BILINEAR);
        cloud1_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(cloud1TextureAtlas, activity, "cloud_a.png");
        cloud1TextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        cloud1TextureAtlas.load();

        cloud2TextureAtlas = new BuildableBitmapTextureAtlas(texmng, 288, 172, TextureOptions.REPEATING_BILINEAR);
        cloud2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(cloud2TextureAtlas, activity, "cloud_b.png");
        cloud2TextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        cloud2TextureAtlas.load();

        cloud3TextureAtlas = new BuildableBitmapTextureAtlas(texmng, 287, 158, TextureOptions.REPEATING_BILINEAR);
        cloud3_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(cloud3TextureAtlas, activity, "cloud_c.png");
        cloud3TextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        cloud3TextureAtlas.load();

        cloud4TextureAtlas = new BuildableBitmapTextureAtlas(texmng, 287, 147, TextureOptions.REPEATING_BILINEAR);
        cloud4_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(cloud4TextureAtlas, activity, "cloud_d.png");
        cloud4TextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        cloud4TextureAtlas.load();

        logoTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 500, 300, TextureOptions.BILINEAR);
        logo_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(logoTextureAtlas, activity, "logo.png");
        logoTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        logoTextureAtlas.load();

        soundonTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 356, 356, TextureOptions.BILINEAR);
        soundon_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(soundonTextureAtlas, activity, "soundOnButton3.png");
        soundonTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        soundonTextureAtlas.load();

        soundoffTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 356, 356, TextureOptions.BILINEAR);
        soundoff_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(soundoffTextureAtlas, activity, "soundOffButton3.png");
        soundoffTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        soundoffTextureAtlas.load();


        scoreBackgroundTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 513, 320, TextureOptions.BILINEAR);
        scoreBackground_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(scoreBackgroundTextureAtlas, activity, "scoreBackground.png");
        scoreBackgroundTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        scoreBackgroundTextureAtlas.load();

        newTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 41, 18, TextureOptions.BILINEAR);
        new_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(newTextureAtlas, activity, "new.png");
        newTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        newTextureAtlas.load();


        gameTitleTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 957, 158, TextureOptions.BILINEAR);
        gameTitle_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTitleTextureAtlas, activity, "gameTitle.png");
        gameTitleTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        gameTitleTextureAtlas.load();

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/general/");

        hammerTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 557, 763, TextureOptions.BILINEAR);
        hammer_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(hammerTextureAtlas, activity, "hammerOhneRad-2.png");
        hammerTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        hammerTextureAtlas.load();

        wheelTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 273, 273, TextureOptions.BILINEAR);
        wheel_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(wheelTextureAtlas, activity, "rad.png");
        wheelTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        wheelTextureAtlas.load();
    }

    private void loadFonts()
    {
        FontFactory.setAssetBasePath("fonts/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 500, 500, TextureOptions.BILINEAR);
        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "fontWallhammer.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
        font.load();

        final ITexture mainFontTexture2 = new BitmapTextureAtlas(activity.getTextureManager(), 500, 500, TextureOptions.BILINEAR);
        font2 = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture2, activity.getAssets(), "mexcellent_rg.ttf", 175, true, Color.WHITE, 2, Color.BLACK);
        font2.load();
    }

    public void unloadMainGraphics()
    {
        menuTextureAtlas.unload();
        cloud1TextureAtlas.unload();
        hammerTextureAtlas.unload();
        groundTextureAtlas.unload();
        logoTextureAtlas.unload();
        wheelTextureAtlas.unload();
        soundoffTextureAtlas.unload();
        soundonTextureAtlas.unload();
        menu_background_region = null;
        play_region = null;
        options_region = null;
        cloud1_region = null;
        ground_region = null;
        logo_region = null;
        soundon_region = null;
        soundoff_region = null;
        hammer_region = null;
        wheel_region = null;
    }

    public void loadGameResources()
    {
        try {
            loadGameGraphics();
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e)
         {
            e.printStackTrace();
        }
        try {
            loadGameAudio();
            loadGameOverAudio();
            loadDestroyWallAudio();
            loadJumpAudio();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadGameGraphics() throws ITextureAtlasBuilder.TextureAtlasBuilderException
    {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
        wallTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 305, 1448, TextureOptions.BILINEAR);
        wall_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(wallTextureAtlas, activity, "wall_bricks.png");
        wallTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        wallTextureAtlas.load();

        wallSmallTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 305, 759, TextureOptions.BILINEAR);
        wallSmall_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(wallSmallTextureAtlas, activity, "wall_unbreakable-01.png");
        wallSmallTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        wallSmallTextureAtlas.load();

        explosionTextureAtlas = new BitmapTextureAtlas(texmng, 1134, 189, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        explosion_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(explosionTextureAtlas, activity, "explosion.png",0,0,6,1);
        explosionTextureAtlas.load();
    }

    public void loadMainAudio() throws IOException {
        musicMain = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity,"mfx/musicMain.ogg");
    }

    public void loadGameAudio() throws IOException {
        musicGame = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity,"mfx/MusicGame.m4a");
    }

    public void loadGameOverAudio() throws IOException {
        musicGameOver = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity,"mfx/musicGameOver.ogg");
    }

    public void loadDestroyWallAudio() throws IOException {
        soundWallDestroy = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity,"mfx/soundWallDestroy.mp3");
    }
    public void loadJumpAudio() throws IOException {
        soundJump = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity,"mfx/jump.mp3");    }

    public void unloadGameRecources()
    {

    }

    public void loadSplashScreen()
    {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/splash/");
        splashBackgroundTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1280, 720, TextureOptions.BILINEAR);
        splashBackground_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashBackgroundTextureAtlas, activity, "background.png", 0, 0);
        splashBackgroundTextureAtlas.load();

        splashLogoTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 595, 595, TextureOptions.BILINEAR);
        splashLogo_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashLogoTextureAtlas, activity, "splashLogo.png", 0, 0);
        splashLogoTextureAtlas.load();

        splashTextTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 3012, 888, TextureOptions.BILINEAR);
        splashText_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextTextureAtlas, activity, "logo.png", 0, 0);
        splashTextTextureAtlas.load();
    }

    public void unloadSplashScreen()
    {
        splashTextTextureAtlas.unload();
        splashText_region = null;
    }


    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------

    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }
}