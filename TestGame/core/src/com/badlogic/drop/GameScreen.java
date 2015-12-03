package com.badlogic.drop;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
    final Drop game;
    final int LOW = 1;

    Texture dropImage;
    Texture bucketImage;
//    Sound dropSound;
//    Music rainMusic;
    OrthographicCamera camera;
    Rectangle bucket;
    Array<Raindrop> raindrops;
    long lastDropTime;
    int dropsGathered;
    int points_dropped;

    public GameScreen(final Drop gam) {
        this.game = gam;

        // load the images for the droplet and the bucket, 64x64 pixels each

        //TODO
        // change images to platform and balloon
        dropImage = new Texture(Gdx.files.internal("droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));

        // load the drop sound effect and the rain background "music"
//        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
//        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
//        rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // create a Rectangle to logically represent the bucket
        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2; // center the bucket horizontally
        bucket.y = 20; // bottom left corner of the bucket is 20 pixels above
        // the bottom screen edge
        bucket.width = 64;
        bucket.height = 64;

        // create the raindrops array and spawn the first raindrop
        raindrops = new Array<Raindrop>();

        points_dropped = 0;
        spawnRaindrop();
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        boolean isHit = false;
        raindrop.x = MathUtils.random(0, 800 - 64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;

        //Make Raindrop object
        Raindrop r = new Raindrop(raindrop, isHit);

        raindrops.add(r);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops
        game.batch.begin();
        game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);
        game.font.draw(game.batch, "Drops Missed: " + points_dropped, 200, 480);
        game.batch.draw(bucketImage, bucket.x, bucket.y);
        for (Raindrop raindrop : raindrops) {
            game.batch.draw(dropImage, raindrop.getRectangle().x, raindrop.getRectangle().y);
        }
        game.batch.end();


        // process user input
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - 64 / 2;
            bucket.y = touchPos.y - 64 / 2;
        }

        if (Gdx.input.isKeyPressed(Keys.LEFT))
            bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            bucket.x += 200 * Gdx.graphics.getDeltaTime();

        // make sure the bucket stays within the screen bounds
        if (bucket.x < 0)
            bucket.x = 0;
        if (bucket.x > 800 - 64)
            bucket.x = 800 - 64;

        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
            spawnRaindrop();

        // move the raindrops, remove any that are beneath the bottom edge of
        // the screen or that hit the bucket. In the later case we play back
        // a sound effect as well.
        Iterator<Raindrop> iter = raindrops.iterator();
        while (iter.hasNext()) {
            Raindrop raindrop = iter.next();
            raindrop.update();
            // Move raindrop down if not hit
            if(!raindrop.isHit) raindrop.getRectangle().y -= 200 * Gdx.graphics.getDeltaTime();
            else raindrop.getRectangle().y += 200 * Gdx.graphics.getDeltaTime();

            // if raindrop gets to bottom of screen
            if (raindrop.getRectangle().y + 64 < 0) {
                iter.remove();
                points_dropped++;
                if(points_dropped >= LOW) game.setScreen(new GameOverScreen(game, dropsGathered));
            }

            if (raindrop.getRectangle().overlaps(bucket) && !raindrop.getIsHit() ) {
                dropsGathered++;
                raindrop.setIsHit(true);
                //TODO play music
//                dropSound.play();

            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
//        rainMusic.play();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        dropImage.dispose();
        bucketImage.dispose();
//        dropSound.dispose();
//        rainMusic.dispose();
    }

}