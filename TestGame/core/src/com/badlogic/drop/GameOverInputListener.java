package com.badlogic.drop;

import com.badlogic.gdx.Input;

/**
 * Created by nmont on 11/19/2015.
 */
public class GameOverInputListener implements Input.TextInputListener {
    private int points;
    public GameOverInputListener (int points)
    {
        this.points = points;
    }
    @Override
    public void input (String text) {
        //Put the points and username into the database/shared preference here
    }

    @Override
    public void canceled () {
    }
}
