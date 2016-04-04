package com.gregdaly.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gregdaly.mariobros.MarioBros;
import com.gregdaly.mariobros.Sprites.Character;
import com.gregdaly.mariobros.Tools.B2WorldCreator;
import com.gregdaly.mariobros.Tools.WorldContactListener;



/**
 * Created by gregorydaly on 3/22/16.
 */
public class PlayScreen implements Screen {

    private MarioBros game;
    private TextureAtlas textureAtlas;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;

    private com.badlogic.gdx.scenes.scene2d.Stage stage;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private Texture blockTexture;
    private Sprite blockSprite;
    private SpriteBatch batch;

    private Music music;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    private Character player;

    private int knobTop = 115;
    private int knobBottom = 85;

    public  PlayScreen(MarioBros game){
        textureAtlas = new TextureAtlas("Mario_and_Enemies.pack");
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT/ MarioBros.PPM, gameCam);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("lvl1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/MarioBros.PPM);
        gameCam.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2,0);

        world = new World(new Vector2(0,0),true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world,map);

        player = new Character(world,this);

        //Create a touchpad skin
        touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("data/touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("data/touchKnob.png"));
        //Create TouchPad Style
        touchpadStyle = new Touchpad.TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        //Create new TouchPad with the created style
        touchpad = new Touchpad(50, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds(15, 15, 200, 200);

        //Create a Stage and add TouchPad
        //stage = new Stage(new ScreenViewport());
        stage = new Stage(new FitViewport(MarioBros.V_WIDTH*3, MarioBros.V_HEIGHT*3));
        stage.addActor(touchpad);
        Gdx.input.setInputProcessor(stage);

        world.setContactListener(new WorldContactListener());

        music = MarioBros.manager.get("audio/music/FRUG.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();

    }

    public TextureAtlas getTextureAtlas(){
        return textureAtlas;
    }

    @Override
    public void show() {

    }

    public void handleInput (float deltaTime) {
        if(touchpad.getKnobY()>150 && touchpad.getKnobX()<115 && touchpad.getKnobX()>85)
            player.b2Body.applyLinearImpulse(new Vector2(0, 0.01f),player.b2Body.getWorldCenter(),true);
        if(touchpad.getKnobX()>150 && touchpad.getKnobY()<115 && touchpad.getKnobY()>85)
            player.b2Body.applyLinearImpulse(new Vector2(0.01f,0), player.b2Body.getWorldCenter(),true);
        if(touchpad.getKnobX()<50 && touchpad.getKnobY()<115 && touchpad.getKnobY()>85)
            player.b2Body.applyLinearImpulse(new Vector2(-0.01f,0), player.b2Body.getWorldCenter(),true);
        if(touchpad.getKnobY()<50 && touchpad.getKnobX()<115 && touchpad.getKnobX()>85)
            player.b2Body.applyLinearImpulse(new Vector2(0,-0.01f),player.b2Body.getWorldCenter(),true);
        if(touchpad.getKnobY()<150 && touchpad.getKnobY()>50 && touchpad.getKnobX()<150 && touchpad.getKnobX()>50)
            player.b2Body.setAwake(false);
    }

    public void update(float deltaTime) {
        handleInput(deltaTime);

        world.step(1 / 60f, 6, 2);

//        Gdx.app.log("X:"+touchpad.getKnobX(),"Y:"+touchpad.getKnobY());

        player.update(deltaTime);

        gameCam.position.x = player.b2Body.getPosition().x;
        gameCam.position.y = player.b2Body.getPosition().y;

        gameCam.update();
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {

        update(delta);

        //clear screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //render game map
        renderer.render();
        //render box3d
        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();
        stage.act(Gdx.graphics.getDeltaTime());

        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
    }
}
