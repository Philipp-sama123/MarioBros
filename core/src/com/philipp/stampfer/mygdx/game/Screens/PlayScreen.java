package com.philipp.stampfer.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.philipp.stampfer.mygdx.game.MarioBros;
import com.philipp.stampfer.mygdx.game.Scenes.Hud;
import com.philipp.stampfer.mygdx.game.Sprites.Goomba;
import com.philipp.stampfer.mygdx.game.Sprites.Mario;
import com.philipp.stampfer.mygdx.game.Tools.WorldContactListener;
import com.philipp.stampfer.mygdx.game.Tools.WorldCreator;

public class PlayScreen implements Screen {

    private MarioBros marioBrosGame;

    private TextureAtlas textureAtlas;

    private OrthographicCamera gameCamera;
    private Viewport gameViewport;
    private Hud hud;

    // Box 2D Variables
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    //sprites
    private Mario player;
    private Goomba goomba;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private Music music;

    public PlayScreen(MarioBros marioBrosGame) {
        textureAtlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.marioBrosGame = marioBrosGame;

        //create cam used to follow mario through cam world
        gameCamera = new OrthographicCamera();

        //create a FitViewport to maintain virtual acpect ratio
        gameViewport = new FitViewport(
                MarioBros.VISUAL_WIDTH / MarioBros.PIXELS_PER_METER,
                MarioBros.VISUAL_HEIGHT / MarioBros.PIXELS_PER_METER,
                gameCamera
        );

        gameViewport.apply();
        //create game HUD for scores/timers/level_info
        hud = new Hud(marioBrosGame.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PIXELS_PER_METER);

        gameCamera.position.set(gameViewport.getWorldWidth() / 2, gameViewport.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        box2DDebugRenderer = new Box2DDebugRenderer();

        new WorldCreator(this);

        // creates Mario in the World
        player = new Mario(this);

        world.setContactListener(new WorldContactListener());
        music = MarioBros.assetManager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();

        goomba = new Goomba(this, 5.64f, .16f);
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void show() {

    }

    public void update(float dt) {
        // handle user input first
        handleInput(dt);

        // takes 1 step in the physics simulation -- 60 times per second
        world.step(1 / 60f, 6, 2);

        player.update(dt);
        goomba.update(dt);
        hud.update(dt);
        // attach gameCamera to players x coordinate
        gameCamera.position.x = player.body2d.getPosition().x;

        gameCamera.update();

        renderer.setView(gameCamera); // render what game cam can see
    }

    private void handleInput(float dt) { // TODO: 31.03.19 touch input 
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.body2d.applyLinearImpulse(new Vector2(0, 4f), player.body2d.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) &&
                (player.body2d.getLinearVelocity().x <= MarioBros.MAXIMUM_SPEED)) {
            player.body2d.applyLinearImpulse(new Vector2(0.1f, 0), player.body2d.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) &&
                (player.body2d.getLinearVelocity().x >= -MarioBros.MAXIMUM_SPEED)) {
            player.body2d.applyLinearImpulse(new Vector2(-0.1f, 0), player.body2d.getWorldCenter(), true);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render map
        renderer.render();

        // renders Box2D Debug lines
        box2DDebugRenderer.render(world, gameCamera.combined);

        marioBrosGame.batch.setProjectionMatrix(gameCamera.combined);
        marioBrosGame.batch.begin();

        player.draw(marioBrosGame.batch);
        goomba.draw(marioBrosGame.batch);

        marioBrosGame.batch.end();

        marioBrosGame.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
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
        box2DDebugRenderer.dispose();
        hud.dispose();
    }
}
