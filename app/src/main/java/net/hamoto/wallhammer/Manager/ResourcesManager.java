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

    public Font font;
    //public Font fontBig;

    //---------------------------------------------
    // TEXTURES & TEXTURE REGIONS
    //---------------------------------------------
    public ITextureRegion splashText_region;
    public ITextureRegion splashLogo_region;
    public ITextureRegion splashBackground_region;
    public ITextureRegion menu_background_region;
    public ITextureRegion play_region;
    public ITextureRegion options_region;
    public ITextureRegion ground_region;
    public ITextureRegion cloud1_region;
    public ITextureRegion logo_region;
    public ITextureRegion soundon_region;
    public ITextureRegion soundoff_region;
    public ITextureRegion hammer_region;
    public ITextureRegion rad_region;
    public ITextureRegion wall_region;
    public ITextureRegion playAgain_region;
    public ITextureRegion share_region;
    public ITextureRegion backToMenu_region;
    public ITextureRegion score_region;
    public ITextureRegion scoreBackground_region;


    BitmapTextureAtlas splashTextTextureAtlas;
    BitmapTextureAtlas splashLogoTextureAtlas;
    BitmapTextureAtlas splashBackgroundTextureAtlas;
    BuildableBitmapTextureAtlas menuTextureAtlas;
    BuildableBitmapTextureAtlas groundTextureAtlas;
    BuildableBitmapTextureAtlas cloud1TextureAtlas;
    BuildableBitmapTextureAtlas logoTextureAtlas;
    BuildableBitmapTextureAtlas soundonTextureAtlas;
    BuildableBitmapTextureAtlas soundoffTextureAtlas;
    BuildableBitmapTextureAtlas hammerTextureAtlas;
    BuildableBitmapTextureAtlas radTextureAtlas;
    BuildableBitmapTextureAtlas wallTextureAtlas;
    BuildableBitmapTextureAtlas playAgainTextureAtlas;
    BuildableBitmapTextureAtlas playTextureAtlas;
    BuildableBitmapTextureAtlas optionsTextureAtlas;
    BuildableBitmapTextureAtlas shareTextureAtlas;
    BuildableBitmapTextureAtlas backToMenuTextureAtlas;
    BuildableBitmapTextureAtlas scoreTextureAtlas;
    BuildableBitmapTextureAtlas scoreBackgroundTextureAtlas;






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
      // menuTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 600, 600, TextureOptions.BILINEAR);
      // play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "PlayButton.png");
      // options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "OptionsButton.png");
      // share_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "ShareButton.png");
      // backToMenu_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "MenuButton.png");
      // menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
      // menuTextureAtlas.load();

        playTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 172, 172, TextureOptions.BILINEAR);
        play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(playTextureAtlas, activity, "playButton.png");
        playTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        playTextureAtlas.load();

        optionsTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 172, 172, TextureOptions.BILINEAR);
        options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "settingsButton.png");
        optionsTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        optionsTextureAtlas.load();

        shareTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 172, 172, TextureOptions.BILINEAR);
        share_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shareTextureAtlas, activity, "shareButton.png");
        shareTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        shareTextureAtlas.load();

        backToMenuTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 172, 172, TextureOptions.BILINEAR);
        backToMenu_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backToMenuTextureAtlas, activity, "menuButton.png");
        backToMenuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        backToMenuTextureAtlas.load();

        playAgainTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 172, 172, TextureOptions.BILINEAR);
        playAgain_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(playAgainTextureAtlas, activity, "playAgainButton.png");
        playAgainTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        playAgainTextureAtlas.load();

        scoreTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 172, 172, TextureOptions.BILINEAR);
        score_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(scoreTextureAtlas, activity, "score2Button.png");
        scoreTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        scoreTextureAtlas.load();

        groundTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 256, 256, TextureOptions.REPEATING_BILINEAR);
        ground_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(groundTextureAtlas, activity, "ground.png");
        ground_region.setTextureSize(3000, 256);
        groundTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        groundTextureAtlas.load();


        cloud1TextureAtlas = new BuildableBitmapTextureAtlas(texmng, 512, 512, TextureOptions.REPEATING_BILINEAR);
        cloud1_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(cloud1TextureAtlas, activity, "cloud1.png");
        cloud1_region.setTextureSize(3072, 512);
        cloud1TextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        cloud1TextureAtlas.load();

        logoTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 500, 300, TextureOptions.BILINEAR);
        logo_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(logoTextureAtlas, activity, "logo.png");
        logoTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        logoTextureAtlas.load();

        soundonTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 172, 172, TextureOptions.BILINEAR);
        soundon_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(soundonTextureAtlas, activity, "soundOnButton.png");
        soundonTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        soundonTextureAtlas.load();

        soundoffTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 172, 172, TextureOptions.BILINEAR);
        soundoff_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(soundoffTextureAtlas, activity, "soundOffButton.png");
        soundoffTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        soundoffTextureAtlas.load();


        scoreBackgroundTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 431, 318, TextureOptions.BILINEAR);
        scoreBackground_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(scoreBackgroundTextureAtlas, activity, "scoreBackground.png");
        scoreBackgroundTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        scoreBackgroundTextureAtlas.load();



        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/general/");

        hammerTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 557, 763, TextureOptions.BILINEAR);
        hammer_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(hammerTextureAtlas, activity, "hammerOhneRad.png");
        hammerTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        hammerTextureAtlas.load();

        radTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 273, 273, TextureOptions.BILINEAR);
        rad_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(radTextureAtlas, activity, "rad.png");
        radTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        radTextureAtlas.load();

    }

    private void loadFonts()
    {
        FontFactory.setAssetBasePath("fonts/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "mexcellent_3d.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
        //fontBig = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "mexcellent_3d Kopie.ttf", 70, true, Color.WHITE, 2, Color.BLACK);
        font.load();
        //fontBig.load();
    }

    public void unloadMainGraphics()
    {
        menuTextureAtlas.unload();
        cloud1TextureAtlas.unload();
        hammerTextureAtlas.unload();
        groundTextureAtlas.unload();
        logoTextureAtlas.unload();
        radTextureAtlas.unload();
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
        rad_region = null;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadGameGraphics() throws ITextureAtlasBuilder.TextureAtlasBuilderException
    {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
        wallTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 256, 1280, TextureOptions.BILINEAR);
        wall_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(wallTextureAtlas, activity, "wall.png");
        wallTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        wallTextureAtlas.load();

        //TODO: Load new Sprites for GameGaphics like the walls...
    }

    public void loadMainAudio() throws IOException {
        musicMain = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity,"mfx/musicMain.ogg");
    }

    public void loadGameAudio() throws IOException {
        musicGame = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity,"mfx/musicGame.ogg");
    }

    public void loadGameOverAudio() throws IOException {
        musicGameOver = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity,"mfx/musicGameOver.ogg");
    }

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
        //splashLogoTextureAtlas.unload();
        splashText_region = null;
        //splashLogo_region = null;
    }


    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------

    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }
}