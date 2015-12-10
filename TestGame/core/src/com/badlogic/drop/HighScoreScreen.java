package com.badlogic.drop;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
//import com.badlogic.gdx.sql.Database;
//import com.badlogic.gdx.sql.DatabaseCursor;
//import com.badlogic.gdx.sql.SQLiteGdxException;

public class HighScoreScreen implements Screen {
    final Drop game;
    OrthographicCamera camera;
    Vector3 touchPoint;
    Rectangle backBounds;
    private static final String NOT_FOUND = "no name entered";


    public HighScoreScreen (final Drop gam){
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        touchPoint = new Vector3();
        backBounds = new Rectangle(10,425,150,36);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Back to Main Menu", 25, 450);
        game.font.draw(game.batch, "Top 5 High Scores", 300, 400);
        game.font.draw(game.batch, "Name", 200, 375);
        game.font.draw(game.batch, "Score", 350, 375);
        game.font.draw(game.batch, "Level", 450, 375);
        if(game.getPreferences().getNewHighScoreAvail()) {
            game.getPreferences().putHighScoreAvail(false);
            arrangeHighScores();
        }
        float nameX = 200;
        float scoreX = 370;
        float levelX = 465;
        float y = 350;
        if(game.getPreferences().getName1().equals(NOT_FOUND))
        {
            printNotFound(nameX,scoreX, levelX,y);
        }
        else {
            game.font.draw(game.batch, game.getPreferences().getName1(), nameX, y);
            game.font.draw(game.batch, "" + game.getPreferences().getScore1(), scoreX, y);
            game.font.draw(game.batch, "" + game.getPreferences().getLevel1(), levelX, y);
        }
        y = y - 75;
        if (game.getPreferences().getName2().equals(NOT_FOUND))
        {
            printNotFound(nameX ,scoreX,levelX, y);
        }
        else {
            game.font.draw(game.batch, game.getPreferences().getName2(), nameX, y);
            game.font.draw(game.batch, "" + game.getPreferences().getScore2(), scoreX, y);
            game.font.draw(game.batch, "" + game.getPreferences().getLevel2(), levelX, y);
        }
        y = y - 75;
        if (game.getPreferences().getName3().equals(NOT_FOUND))
        {
            printNotFound(nameX,scoreX,levelX,y);
        }
        else {
            game.font.draw(game.batch, game.getPreferences().getName3(), nameX, y);
            game.font.draw(game.batch, "" + game.getPreferences().getScore3(), scoreX, y);
            game.font.draw(game.batch, "" + game.getPreferences().getLevel3(), levelX, y);
        }
        y = y - 75;
        if (game.getPreferences().getName4().equals(NOT_FOUND)) {
            printNotFound(nameX,scoreX,levelX,y);
        }
        else {
            game.font.draw(game.batch, game.getPreferences().getName4(), nameX, y);
            game.font.draw(game.batch, "" + game.getPreferences().getScore4(), scoreX, y);
            game.font.draw(game.batch, "" + game.getPreferences().getLevel4(), levelX, y);
        }
        y = y - 75;
        if (game.getPreferences().getName5().equals(NOT_FOUND))
        {
            printNotFound(nameX,scoreX,levelX,y);
        }
        else {
            game.font.draw(game.batch, game.getPreferences().getName5(), nameX, y);
            game.font.draw(game.batch, "" + game.getPreferences().getScore5(), scoreX, y);
            game.font.draw(game.batch, "" + game.getPreferences().getLevel5(), levelX, y);
        }

        ShapeRenderer renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.rect(backBounds.x, backBounds.y, backBounds.width, backBounds.height);

        game.batch.end();

        renderer.end();


        if (Gdx.input.isTouched()) {
            camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (backBounds.contains(touchPoint.x, touchPoint.y)) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        }

    }
    private void arrangeHighScores () {
        String name = game.getPreferences().getCurName();
        int score = game.getPreferences().getCurScore();
        int level = game.getPreferences().getCurLevel();
        //check if the current score coming in is the highest score and then trickle down what is necessary
        int checkScore = score;
        String checkName = name;
        int checkLevel = level;
        if (checkScore >= game.getPreferences().getScore1()) {
            String tmpName = game.getPreferences().getName1();
            int tmpLevel = game.getPreferences().getLevel1();
            int tmpScore = game.getPreferences().getScore1();
            game.getPreferences().putScore1(checkScore);
            game.getPreferences().putName1(checkName);
            game.getPreferences().putLevel1(checkLevel);
            String name2 = game.getPreferences().getName2();
            int score2 = game.getPreferences().getScore2();
            int level2 = game.getPreferences().getLevel2();
            game.getPreferences().putName2(tmpName);
            game.getPreferences().putLevel2(tmpLevel);
            game.getPreferences().putScore2(tmpScore);
            tmpLevel = game.getPreferences().getLevel3();
            tmpName = game.getPreferences().getName3();
            tmpScore = game.getPreferences().getScore3();
            game.getPreferences().putScore3(score2);
            game.getPreferences().putName3(name2);
            game.getPreferences().putLevel3(level2);
            score2 = game.getPreferences().getScore4();
            name2 = game.getPreferences().getName4();
            level2 = game.getPreferences().getLevel4();
            game.getPreferences().putLevel4(tmpLevel);
            game.getPreferences().putScore4(tmpScore);
            game.getPreferences().putName4(tmpName);
            game.getPreferences().putScore5(score2);
            game.getPreferences().putLevel5(level2);
            game.getPreferences().putName5(name2);


        } else if (checkScore >= game.getPreferences().getScore2()) {
            String tmpName = game.getPreferences().getName2();
            int tmpLevel = game.getPreferences().getLevel2();
            int tmpScore = game.getPreferences().getScore2();
            game.getPreferences().putScore2(checkScore);
            game.getPreferences().putName2(checkName);
            game.getPreferences().putLevel2(checkLevel);
            String name3 = game.getPreferences().getName3();
            int level3 = game.getPreferences().getLevel3();
            int score3 = game.getPreferences().getScore3();
            game.getPreferences().putName3(tmpName);
            game.getPreferences().putLevel3(tmpLevel);
            game.getPreferences().putScore3(tmpScore);
            tmpName = game.getPreferences().getName4();
            tmpLevel = game.getPreferences().getLevel4();
            tmpScore = game.getPreferences().getScore4();
            game.getPreferences().putName4(name3);
            game.getPreferences().putLevel4(level3);
            game.getPreferences().putScore4(score3);
            game.getPreferences().putName5(tmpName);
            game.getPreferences().putLevel5(tmpLevel);
            game.getPreferences().putScore5(tmpScore);


        } else if (checkScore >= game.getPreferences().getScore3()) {
            String tmpName = game.getPreferences().getName3();
            int tmpLevel = game.getPreferences().getLevel3();
            int tmpScore = game.getPreferences().getScore3();
            game.getPreferences().putScore3(checkScore);
            game.getPreferences().putName3(checkName);
            game.getPreferences().putLevel3(checkLevel);
            String name4 = game.getPreferences().getName4();
            int level4 = game.getPreferences().getLevel4();
            int score4 = game.getPreferences().getScore4();
            game.getPreferences().putScore4(tmpScore);
            game.getPreferences().putLevel4(tmpLevel);
            game.getPreferences().putName4(tmpName);
            game.getPreferences().putName5(name4);
            game.getPreferences().putLevel5(level4);
            game.getPreferences().putScore5(score4);
        } else if (checkScore >= game.getPreferences().getScore4()) {
            String tmpName = game.getPreferences().getName4();
            int tmpLevel = game.getPreferences().getLevel4();
            int tmpScore = game.getPreferences().getScore4();
            game.getPreferences().putScore4(checkScore);
            game.getPreferences().putName4(checkName);
            game.getPreferences().putLevel4(checkLevel);
            game.getPreferences().putScore5(tmpScore);
            game.getPreferences().putLevel5(tmpLevel);
            game.getPreferences().putName5(tmpName);
        } else if (checkScore >= game.getPreferences().getScore5()) {
            game.getPreferences().putScore5(checkScore);
            game.getPreferences().putName5(checkName);
            game.getPreferences().putLevel5(checkLevel);
        }
    }
    private boolean isEmpty (int rank) {
        switch (rank) {
            case 1:
                return game.getPreferences().getName1().equals(NOT_FOUND);
            case 2:
                return game.getPreferences().getName2().equals(NOT_FOUND);
            case 3:
                return game.getPreferences().getName3().equals(NOT_FOUND);
            case 4:
                return game.getPreferences().getName4().equals(NOT_FOUND);
            case 5:
                return game.getPreferences().getName5().equals(NOT_FOUND);
            default:
                return false;
        }
    }
    private void printNotFound (float nameX, float scoreX, float levelX, float y) {
        game.font.draw(game.batch, "Currently no high score",nameX,y );
        game.font.draw(game.batch, "N/A", scoreX,y);
        game.font.draw(game.batch, "N/A", levelX,y);
    }

    @Override
    public void resize(int width, int height) {

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

    }
}
