package hit.android.fixmypicture;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class LevelsContainer {
    private static LevelsContainer ourInstance;
    private HashMap<String, ArrayList<LevelSettings>> levelContainer;
    private HashMap<String, Integer> levelDefenition;
    private final String[] levelsProgression = {"elephants_3", "elephants_5", "elephants_7"};
    private HashMap<String, String> picsFacts;
    private Context context;

    public String[] getLevelsProgression() {
        return levelsProgression;
    }

    public static LevelsContainer getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new LevelsContainer(context);
        }
        return ourInstance;
    }

    public HashMap<String, Integer> getLevelDefenition() {
        return levelDefenition;
    }

    public HashMap<String, ArrayList<LevelSettings>> getLevelContainer() {
        if (levelContainer == null) {
            populateLevels();
        }
        return levelContainer;
    }

    private LevelsContainer(Context context) {
        this.context = context;
        fillFactsMapping();
        levelDefenition = new HashMap<>();
        levelDefenition.put("elephants_3", R.string.beginer);
        levelDefenition.put("elephants_5", R.string.advanced);
        levelDefenition.put("elephants_7", R.string.pro);
    }

    private void populateDefaultLevels() {
        //TODO: add more levels
        ArrayList<String> elephantsFacts = new ArrayList<>();
        elephantsFacts.add("elephants1");
        elephantsFacts.add("elephants2");
        elephantsFacts.add("elephants3");
        elephantsFacts.add("elephants3");
        elephantsFacts.add("elephants5");

        ArrayList<String> owlsFacts = new ArrayList<>();
        owlsFacts.add("owls1");
        owlsFacts.add("owls2");
        owlsFacts.add("owls3");
        owlsFacts.add("owls4");
        owlsFacts.add("owls5");

        ArrayList<String> kualasFacts = new ArrayList<>();
        kualasFacts.add("kualas1");
        kualasFacts.add("kualas2");
        kualasFacts.add("kualas3");
        kualasFacts.add("kualas4");
        kualasFacts.add("kualas5");


        levelContainer = new HashMap<>();
        ArrayList<LevelSettings> puzzles = new ArrayList<>();

        //elephants:1_3
        LevelSettings level = new LevelSettings();
        level.setName("1");
        level.setPicId(R.drawable.pic1);
        level.setDimension(3);
        level.setPassed(false);
        level.setTopScore(0);
        level.setPicFacts(elephantsFacts);
        puzzles.add(level);

        //elephants:2_3
        level = new LevelSettings();
        level.setName("2");
        level.setPicId(R.drawable.pic2);
        level.setDimension(3);
        level.setPassed(false);
        level.setTopScore(0);
        level.setPicFacts(elephantsFacts);
        puzzles.add(level);

        //Owls:1_3
        level = new LevelSettings();
        level.setName("3");
        level.setPicId(R.drawable.pic3);
        level.setDimension(3);
        level.setPassed(false);
        level.setTopScore(0);
        level.setPicFacts(owlsFacts);
        puzzles.add(level);

        //Owls:2_3
        level = new LevelSettings();
        level.setName("4");
        level.setPicId(R.drawable.pic4);
        level.setDimension(3);
        level.setPassed(false);
        level.setTopScore(0);
        level.setPicFacts(owlsFacts);
        puzzles.add(level);

        //kualas:1_3
        level = new LevelSettings();
        level.setName("5");
        level.setPicId(R.drawable.pic5);
        level.setDimension(3);
        level.setPassed(false);
        level.setTopScore(0);
        level.setPicFacts(kualasFacts);
        puzzles.add(level);

        //kualas:2_3
        level = new LevelSettings();
        level.setName("6");
        level.setPicId(R.drawable.pic6);
        level.setDimension(3);
        level.setPassed(false);
        level.setTopScore(0);
        level.setPicFacts(kualasFacts);
        puzzles.add(level);

        levelContainer.put("elephants_3", puzzles);
        puzzles = new ArrayList<>();

        //elephants:1_5
        level = new LevelSettings();
        level.setName("7");
        level.setPicId(R.drawable.pic1);
        level.setDimension(5);
        level.setPassed(false);
        level.setTopScore(0);
        level.setPicFacts(elephantsFacts);
        puzzles.add(level);

        //elephants: 2_5
        level = new LevelSettings();
        level.setName("8");
        level.setPicId(R.drawable.pic2);
        level.setDimension(5);
        level.setPassed(false);
        level.setTopScore(0);
        level.setPicFacts(elephantsFacts);
        puzzles.add(level);

        //owls: 1_5
        level = new LevelSettings();
        level.setName("9");
        level.setPicId(R.drawable.pic3);
        level.setDimension(5);
        level.setPassed(false);
        level.setTopScore(0);
        level.setPicFacts(owlsFacts);
        puzzles.add(level);

        //owls:2_5
        level = new LevelSettings();
        level.setName("10");
        level.setPicId(R.drawable.pic4);
        level.setDimension(5);
        level.setPassed(false);
        level.setTopScore(0);
        level.setPicFacts(owlsFacts);
        puzzles.add(level);

        //kualas 1_5
        level = new LevelSettings();
        level.setName("11");
        level.setPicId(R.drawable.pic5);
        level.setDimension(5);
        level.setPassed(false);
        level.setTopScore(0);
        level.setPicFacts(kualasFacts);
        puzzles.add(level);

        //kualas 2_5
        level = new LevelSettings();
        level.setName("12");
        level.setPicId(R.drawable.pic6);
        level.setDimension(5);
        level.setPassed(false);
        level.setTopScore(0);
        level.setPicFacts(kualasFacts);
        puzzles.add(level);

        levelContainer.put("elephants_5", puzzles);
        puzzles = new ArrayList<>();

        //elephants 1_7
        level = new LevelSettings();
        level.setName("13");
        level.setPicId(R.drawable.pic1);
        level.setDimension(7);
        level.setPassed(false);
        level.setTopScore(0);
        level.setPicFacts(elephantsFacts);
        puzzles.add(level);

        //elephants 2_7
        level = new LevelSettings();
        level.setName("14");
        level.setPicId(R.drawable.pic2);
        level.setDimension(7);
        level.setPassed(false);
        level.setTopScore(0);
        level.setPicFacts(elephantsFacts);
        puzzles.add(level);

        //owls: 1_7
        level = new LevelSettings();
        level.setName("15");
        level.setPicId(R.drawable.pic3);
        level.setDimension(7);
        level.setPassed(false);
        level.setTopScore(0);
        level.setPicFacts(owlsFacts);
        puzzles.add(level);

        //owls:2_7
        level = new LevelSettings();
        level.setName("16");
        level.setPicId(R.drawable.pic4);
        level.setDimension(7);
        level.setPassed(false);
        level.setTopScore(0);
        level.setPicFacts(owlsFacts);
        puzzles.add(level);

        //kualas: 1_7
        level = new LevelSettings();
        level.setName("17");
        level.setPicId(R.drawable.pic5);
        level.setDimension(7);
        level.setPassed(false);
        level.setTopScore(0);
        level.setPicFacts(kualasFacts);
        puzzles.add(level);

        //kualas: 2_7
        level = new LevelSettings();
        level.setName("18");
        level.setPicId(R.drawable.pic6);
        level.setDimension(7);
        level.setPassed(false);
        level.setTopScore(0);
        level.setPicFacts(kualasFacts);
        puzzles.add(level);

        levelContainer.put("elephants_7", puzzles);
    }

    private void populateLevels() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String levelsJson = settings.getString("levels_map", null);
        if (levelsJson == null) {
            populateDefaultLevels();
            updateLevels();
        } else {
            java.lang.reflect.Type type = new TypeToken<HashMap<String, ArrayList<LevelSettings>>>() {
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

    public HashMap<String, String> getPicsFacts() {
        return picsFacts;
    }


    private void fillFactsMapping() {
        picsFacts = new HashMap<>();
        picsFacts.put("elephants1", context.getResources().getString(R.string.elephants_fact_a));
        picsFacts.put("elephants2", context.getResources().getString(R.string.elephants_fact_b));
        picsFacts.put("elephants3", context.getResources().getString(R.string.elephants_fact_c));
        picsFacts.put("elephants4", context.getResources().getString(R.string.elephants_fact_d));
        picsFacts.put("elephants5", context.getResources().getString(R.string.elephants_fact_e));
        picsFacts.put("owls1", context.getResources().getString(R.string.owls_fact_a));
        picsFacts.put("owls2", context.getResources().getString(R.string.owls_fact_b));
        picsFacts.put("owls3", context.getResources().getString(R.string.owls_fact_c));
        picsFacts.put("owls4", context.getResources().getString(R.string.owls_fact_d));
        picsFacts.put("owls5", context.getResources().getString(R.string.owls_fact_e));
        picsFacts.put("kualas1", context.getResources().getString(R.string.kualas_fact_a));
        picsFacts.put("kualas2", context.getResources().getString(R.string.kualas_fact_b));
        picsFacts.put("kualas3", context.getResources().getString(R.string.kualas_fact_c));
        picsFacts.put("kualas4", context.getResources().getString(R.string.kualas_fact_d));
        picsFacts.put("kualas5", context.getResources().getString(R.string.kualas_fact_e));
    }
}
