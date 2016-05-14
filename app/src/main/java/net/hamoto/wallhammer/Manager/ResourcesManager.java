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
 * @added 04/28/2016
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

    public Music music;
    public Music music2;

    public Font font;

    //---------------------------------------------
    // TEXTURES & TEXTURE REGIONS
    //---------------------------------------------
    public ITextureRegion splash_region;
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

    BitmapTextureAtlas splashTextureAtlas;
    BuildableBitmapTextureAtlas menuTextureAtlas;
    BuildableBitmapTextureAtlas groundTextureAtlas;
    BuildableBitmapTextureAtlas cloud1TextureAtlas;
    BuildableBitmapTextureAtlas logoTextureAtlas;
    BuildableBitmapTextureAtlas soundonTextureAtlas;
    BuildableBitmapTextureAtlas soundoffTextureAtlas;
    BuildableBitmapTextureAtlas hammerTextureAtlas;
    BuildableBitmapTextureAtlas radTextureAtlas;






    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

    /**
     * @param engine
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
        menuTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 500, 500, TextureOptions.BILINEAR);
        play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "playbtn.png");
        options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "options.png");
        menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
        menuTextureAtlas.load();

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


        soundonTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 200, 200, TextureOptions.BILINEAR);
        soundon_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(soundonTextureAtlas, activity, "soundon.png");
        soundonTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        soundonTextureAtlas.load();


        soundoffTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 200, 200, TextureOptions.BILINEAR);
        soundoff_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(soundoffTextureAtlas, activity, "soundoff.png");
        soundoffTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        soundoffTextureAtlas.load();

        hammerTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 357, 400, TextureOptions.BILINEAR);
        hammer_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(hammerTextureAtlas, activity, "hammerNeu.png");
        hammerTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        hammerTextureAtlas.load();

        radTextureAtlas = new BuildableBitmapTextureAtlas(texmng, 130, 130, TextureOptions.BILINEAR);
        rad_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(radTextureAtlas, activity, "rad.png");
        radTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        radTextureAtlas.load();
    }

    public void loadMainAudio() throws IOException {
        music = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity,"mfx/music.ogg");
    }

    private void loadFonts()
    {
        FontFactory.setAssetBasePath("fonts/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "mexcellent_3d.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
        font.load();
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
        loadGameGraphics();
        try {
            loadGameAudio();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadGameGraphics()
    {
        //TODO: Load new Sprites for GameGaphics like the walls...
    }

    public void loadGameAudio() throws IOException {
        music2 = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity,"mfx/musik_tolgaa.ogg");
    }

    public void unloadGameRecources()
    {

    }




    public void loadSplashScreen()
    {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 700, 700, TextureOptions.BILINEAR);
        splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.jpg", 0, 0);
        splashTextureAtlas.load();
    }

    public void unloadSplashScreen()
    {
        splashTextureAtlas.unload();
        splash_region = null;
    }


    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------

    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }
}