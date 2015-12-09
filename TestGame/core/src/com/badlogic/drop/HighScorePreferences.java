package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class HighScorePreferences  {

    private static final String PREF_NAME = "Game Preferences";
    private static final String NAME_KEY1 = "name1";
    private static final String NAME_KEY2 = "name2";
    private static final String NAME_KEY3 = "name3";
    private static final String NAME_KEY4 = "name4";
    private static final String NAME_KEY5 = "name5";
    private static final String SCORE_KEY1 = "score1";
    private static final String SCORE_KEY2 = "score2";
    private static final String SCORE_KEY3 = "score3";
    private static final String SCORE_KEY4 = "score4";
    private static final String SCORE_KEY5 = "score5";
    private static final String LEVEL_KEY1 = "level1";
    private static final String LEVEL_KEY2 = "level2";
    private static final String LEVEL_KEY3 = "level3";
    private static final String LEVEL_KEY4 = "level4";
    private static final String LEVEL_KEY5 = "level5";
    private static final String CUR_NAME = "name";
    private static final String CUR_SCORE = "score";
    private static final String CUR_LEVEL = "level";
    private static final String NOT_FOUND = "no name entered";
    private static final String NEW_HIGHSCORE_AVAIL = "newHighScore";
    private static final int DEFAULT_VALUE = 0;
    private static final String CUR_STATE_SCORE = "statescore";
    private static final String CUR_STATE_LEVEL = "statelevel";

    private Preferences pref;

    public HighScorePreferences () {

    }
    protected Preferences getPref() {
        if (pref == null) {
            pref = Gdx.app.getPreferences(PREF_NAME);
        }
        return pref;
    }
    public String getName1 () {
        return getPref().getString(NAME_KEY1, NOT_FOUND);
    }
    public String getName2 () {
        return getPref().getString(NAME_KEY2, NOT_FOUND);
    }
    public String getName3 () {
        return getPref().getString(NAME_KEY3, NOT_FOUND);
    }
    public String getName4 () {
        return getPref().getString(NAME_KEY4, NOT_FOUND);
    }
    public String getName5 () {
        return getPref().getString(NAME_KEY5, NOT_FOUND);
    }
    public int getScore1 () {
        return getPref().getInteger(SCORE_KEY1, DEFAULT_VALUE);
    }
    public int getScore2 () {
        return getPref().getInteger(SCORE_KEY2, DEFAULT_VALUE);
    }
    public int getScore3 () {
        return getPref().getInteger(SCORE_KEY3, DEFAULT_VALUE);
    }
    public int getScore4 () {
        return getPref().getInteger(SCORE_KEY4, DEFAULT_VALUE);
    }
    public int getScore5 () {
        return getPref().getInteger(SCORE_KEY5, DEFAULT_VALUE);
    }
    public int getLevel1 () {
        return getPref().getInteger(LEVEL_KEY1, DEFAULT_VALUE);
    }
    public int getLevel2 () {
        return getPref().getInteger(LEVEL_KEY2, DEFAULT_VALUE);
    }
    public int getLevel3 () {
        return getPref().getInteger(LEVEL_KEY3, DEFAULT_VALUE);
    }
    public int getLevel4 () {
        return getPref().getInteger(LEVEL_KEY4, DEFAULT_VALUE);
    }
    public int getLevel5 () {
        return getPref().getInteger(LEVEL_KEY5, DEFAULT_VALUE);
    }
    public String getCurName () {
        return getPref().getString(CUR_NAME, NOT_FOUND);
    }
    public int getCurScore () {
        return getPref().getInteger(CUR_SCORE, DEFAULT_VALUE);
    }
    public int getCurLevel () {
        return getPref().getInteger(CUR_LEVEL, DEFAULT_VALUE);
    }
    public boolean getNewHighScoreAvail () {
        return getPref().getBoolean(NEW_HIGHSCORE_AVAIL);
    }
    public int getCurStateScore () {
        return getPref().getInteger(CUR_STATE_SCORE, DEFAULT_VALUE);
    }
    public int getCurStateLevel () {
        return getPref().getInteger(CUR_STATE_LEVEL, 1);
    }

    public void putName1 (String name) {
        getPref().putString(NAME_KEY1, name);
        getPref().flush();
    }
    public void putName2 (String name) {
        getPref().putString(NAME_KEY2, name);
        getPref().flush();
    }
    public void putName3 (String name) {
        getPref().putString(NAME_KEY3, name);
        getPref().flush();
    }
    public void putName4 (String name) {
        getPref().putString(NAME_KEY4, name);
        getPref().flush();
    }
    public void putName5 (String name) {
        getPref().putString(NAME_KEY5, name);
        getPref().flush();
    }
    public void putScore1 (int score)
    {
        getPref().putInteger(SCORE_KEY1, score);
        getPref().flush();
    }
    public void putScore2 (int score)
    {
        getPref().putInteger(SCORE_KEY2, score);
        getPref().flush();
    }
    public void putScore3 (int score)
    {
        getPref().putInteger(SCORE_KEY3, score);
        getPref().flush();
    }
    public void putScore4 (int score)
    {
        getPref().putInteger(SCORE_KEY4, score);
        getPref().flush();
    }
    public void putScore5 (int score)
    {
        getPref().putInteger(SCORE_KEY5, score);
        getPref().flush();
    }
    public void putLevel1 (int level)
    {
        getPref().putInteger(LEVEL_KEY1, level);
        getPref().flush();
    }
    public void putLevel2 (int level)
    {
        getPref().putInteger(LEVEL_KEY2, level);
        getPref().flush();
    }
    public void putLevel3 (int level)
    {
        getPref().putInteger(LEVEL_KEY3, level);
        getPref().flush();
    }
    public void putLevel4 (int level)
    {
        getPref().putInteger(LEVEL_KEY4, level);
        getPref().flush();
    }
    public void putLevel5 (int level)
    {
        getPref().putInteger(LEVEL_KEY5, level);
        getPref().flush();
    }
    public void putCurName (String name) {
        getPref().putString(CUR_NAME, name);
        getPref().flush();
    }
    public void putCurScore (int score) {
        getPref().putInteger(CUR_SCORE, score);
        getPref().flush();
    }
    public void putCurLevel (int level) {
        getPref().putInteger(CUR_LEVEL, level);
        getPref().flush();
    }
    public void putHighScoreAvail (boolean available) {
        getPref().putBoolean(NEW_HIGHSCORE_AVAIL,available);
        getPref().flush();
    }
    public void putCurStateScore (int score) {
        getPref().putInteger(CUR_STATE_SCORE, score);
        getPref().flush();
    }
    public void putCurStateLevel (int level) {
        getPref().putInteger(CUR_STATE_LEVEL, level);
        getPref().flush();
    }



}
