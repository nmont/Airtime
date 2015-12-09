package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
//import com.badlogic.gdx.sql.Database;
//import com.badlogic.gdx.sql.DatabaseCursor;
//import com.badlogic.gdx.sql.DatabaseFactory;
//import com.badlogic.gdx.sql.SQLiteGdxException;


/**
 * Created by nmont on 11/19/2015.
 */
public class GameOverInputListener implements Input.TextInputListener {
    private int score;
    private int level;
    final Drop game;
    private String inputString;
//    Database databaseHandler;
//    public static final String TABLE_SCOREBOARD = "ScoreBoard";
//    public static final String COLUMN_ID = "id";
//    public static final String COLUMN_NAME = "name";
//    public static final String COLUMN_SCORE = "score";
//    public static final String COLUMN_LEVEL = "level";
//
//    private static final String DATABASE_NAME = "ScoreBoard.db";
//    private static final int DATABASE_VERSION = 1;
//    //Database creation sql statement
//    private static final String DATABASE_CREATE = "create table if not exists "
//            + TABLE_SCOREBOARD + "(" + COLUMN_ID
//            + " integer primary key autoincrement, " + COLUMN_NAME
//            + " text not null, " + COLUMN_SCORE + " text not null, "
//            + COLUMN_LEVEL + " text not null);";

    public GameOverInputListener (final Drop gam)
    {
//        score = scor;
//        level = lev;
        game = gam;
    }
    @Override
    public void input (String name) {
        inputString = name;

        //Put the name, score, and level into the database
//        databaseHandler = DatabaseFactory.getNewDatabase(DATABASE_NAME,DATABASE_VERSION,DATABASE_CREATE,null);
//        databaseHandler.setupDatabase();
//        try {
//            databaseHandler.openOrCreateDatabase();
//            databaseHandler.execSQL(DATABASE_CREATE);
//            databaseHandler.execSQL("INSERT INTO ScoreBoard ('"+ COLUMN_NAME+"','"+ COLUMN_SCORE + "', '"
//                    + COLUMN_LEVEL +"') VALUES ('" + name + "','" + score + "','"+ level +"')");
//        }
//        catch (SQLiteGdxException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void canceled () {
    }

    public String getInput () {
        return inputString;
    }


}
