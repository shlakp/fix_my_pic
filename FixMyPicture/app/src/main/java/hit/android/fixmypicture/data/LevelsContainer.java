/*
Single instance of all game data describing all levels structure and levels configuration
Save initial data to Shared preferences in first run
Retrieves data from Shared Preferences all other
All game data should be passed by this!!!
 */

package hit.android.fixmypicture.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

import hit.android.fixmypicture.R;


public class LevelsContainer {
    private static LevelsContainer ourInstance;
    private ArrayList<LevelsPage> levelContainer ;
    private Context context;

    private LevelsContainer(Context context) {
        this.context = context;
    }

    public static LevelsContainer getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new LevelsContainer(context);
        }
        return ourInstance;
    }



    public ArrayList<LevelsPage> getLevelContainer() {
        if (levelContainer == null) {
            populateLevels();
        }
        return levelContainer;
    }

    private void populateDefaultLevels() {
        levelContainer = new ArrayList<>();

        //populate page 1
        LevelsPage page = new LevelsPage() ;
        page.setHeaderStringIdx( R.string.beginer );

        //elephants:1_3
        Level level = new Level();
        level.setName("1");
        level.setPicId(R.drawable.pic1);
        level.setDimension(3);
        level.setPassed(false);
        level.setTopScore(0);
        Log.d("fixMyPic", "populateDefaultLevels: adding level " + level.getName());
        page.addLevel(level);

        //elephants:2_3
        level = new Level();
        level.setName("2");
        level.setPicId(R.drawable.pic2);
        level.setDimension(3);
        level.setPassed(false);
        level.setTopScore(0);
        Log.d("fixMyPic", "populateDefaultLevels: adding level" + level.getName());
        page.addLevel(level);

        //Owls:1_3
        level = new Level();
        level.setName("3");
        level.setPicId(R.drawable.pic3);
        level.setDimension(3);
        level.setPassed(false);
        level.setTopScore(0);
        Log.d("fixMyPic", "populateDefaultLevels: adding level" + level.getName());
        page.addLevel(level);

        //Owls:2_3
        level = new Level();
        level.setName("4");
        level.setPicId(R.drawable.pic4);
        level.setDimension(3);
        level.setPassed(false);
        level.setTopScore(0);
        Log.d("fixMyPic", "populateDefaultLevels: adding level" + level.getName());
        page.addLevel(level);

        //kualas:1_3
        level = new Level();
        level.setName("5");
        level.setPicId(R.drawable.pic5);
        level.setDimension(3);
        level.setPassed(false);
        level.setTopScore(0);
        Log.d("fixMyPic", "populateDefaultLevels: adding level" + level.getName());
        page.addLevel(level);

        //kualas:2_3
        level = new Level();
        level.setName("6");
        level.setPicId(R.drawable.pic6);
        level.setDimension(3);
        level.setPassed(false);
        level.setTopScore(0);
        Log.d("fixMyPic", "populateDefaultLevels: adding level" + level.getName());
        page.addLevel(level);

        levelContainer.add(page);
        page = new LevelsPage();
        page.setHeaderStringIdx( R.string.advanced );

        //elephants:1_5
        level = new Level();
        level.setName("7");
        level.setPicId(R.drawable.pic1);
        level.setDimension(5);
        level.setPassed(false);
        level.setTopScore(0);
        page.addLevel(level);

        //elephants: 2_5
        level = new Level();
        level.setName("8");
        level.setPicId(R.drawable.pic2);
        level.setDimension(5);
        level.setPassed(false);
        level.setTopScore(0);
        page.addLevel(level);

        //owls: 1_5
        level = new Level();
        level.setName("9");
        level.setPicId(R.drawable.pic3);
        level.setDimension(5);
        level.setPassed(false);
        level.setTopScore(0);
        page.addLevel(level);

        //owls:2_5
        level = new Level();
        level.setName("10");
        level.setPicId(R.drawable.pic4);
        level.setDimension(5);
        level.setPassed(false);
        level.setTopScore(0);
        page.addLevel(level);

        //kualas 1_5
        level = new Level();
        level.setName("11");
        level.setPicId(R.drawable.pic5);
        level.setDimension(5);
        level.setPassed(false);
        level.setTopScore(0);
        page.addLevel(level);

        //kualas 2_5
        level = new Level();
        level.setName("12");
        level.setPicId(R.drawable.pic6);
        level.setDimension(5);
        level.setPassed(false);
        level.setTopScore(0);
        page.addLevel(level);

        levelContainer.add(page);
        page = new LevelsPage();
        page.setHeaderStringIdx( R.string.pro);

        //elephants 1_7
        level = new Level();
        level.setName("13");
        level.setPicId(R.drawable.pic1);
        level.setDimension(7);
        level.setPassed(false);
        level.setTopScore(0);
        page.addLevel(level);

        //elephants 2_7
        level = new Level();
        level.setName("14");
        level.setPicId(R.drawable.pic2);
        level.setDimension(7);
        level.setPassed(false);
        level.setTopScore(0);
        page.addLevel(level);

        //owls: 1_7
        level = new Level();
        level.setName("15");
        level.setPicId(R.drawable.pic3);
        level.setDimension(7);
        level.setPassed(false);
        level.setTopScore(0);
        page.addLevel(level);

        //owls:2_7
        level = new Level();
        level.setName("16");
        level.setPicId(R.drawable.pic4);
        level.setDimension(7);
        level.setPassed(false);
        level.setTopScore(0);
        page.addLevel(level);

        //kualas: 1_7
        level = new Level();
        level.setName("17");
        level.setPicId(R.drawable.pic5);
        level.setDimension(7);
        level.setPassed(false);
        level.setTopScore(0);
        page.addLevel(level);

        //kualas: 2_7
        level = new Level();
        level.setName("18");
        level.setPicId(R.drawable.pic6);
        level.setDimension(7);
        level.setPassed(false);
        level.setTopScore(0);
        page.addLevel(level);

        levelContainer.add(page);
    }

    private void populateLevels() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        //String levelsJson = settings.getString("levels_map", null);
        String levelsJson = null ;
        if (levelsJson == null) {
            populateDefaultLevels();
            updateLevels();
        } else {
            java.lang.reflect.Type type = new TypeToken<HashMap<String, ArrayList<Level>>>() {
            }.getType();
            Gson gson = new Gson();
            levelContainer = gson.fromJson(levelsJson, type);
        }

    }

    public void updateLevels() {
        Gson gson = new Gson();
        String hashMapString = gson.toJson(levelContainer);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("levels_map", hashMapString).apply();
    }
    public Level getLevel(int pageId, int lvlId) {
        return levelContainer.get(pageId).getLevel(lvlId);
    }
}

