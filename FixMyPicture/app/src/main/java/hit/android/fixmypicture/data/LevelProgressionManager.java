package hit.android.fixmypicture.data;

import android.content.Context;
import android.content.SharedPreferences;

import hit.android.fixmypicture.MainActivity;

public class LevelProgressionManager {
    private static final int NUM_OF_PUZZLES = 6;
    private static final int NUM_OF_PAGES = 3;
    private static final String SP_PAGE_IDX = "currIdx" ;
    private static final String SP_CURR_PUZZLE = "currentPuzzle" ;
    private static final String SP_PERSISTENT_CURRENT_PAGE = "persistantPage" ;
    private static final String SP_PERSISTENT_CURRENT_PUZZLE = "persistantPuzzle" ;

    private SharedPreferences currentLevelSharedPref;
    private Context context;
    private SharedPreferences.Editor sPeditor ;


    public LevelProgressionManager(Context context) {
        this.context = context;
        currentLevelSharedPref = context.getSharedPreferences(MainActivity.SP_CURR_USER_PREF_NAME, Context.MODE_PRIVATE);
        sPeditor = currentLevelSharedPref.edit() ;
    }



    public int getPersistentPuzzle() {
        return currentLevelSharedPref.getInt(SP_PERSISTENT_CURRENT_PUZZLE, 0);
    }

    public void setPersistentPuzzle(int puzzle) {
        sPeditor.putInt(SP_PERSISTENT_CURRENT_PUZZLE, puzzle) ;
        sPeditor.apply();
    }

    public int getPersistentPage() {
        return currentLevelSharedPref.getInt(SP_PERSISTENT_CURRENT_PAGE, 0);
    }

    public void setPersistentPage(int page) {
        sPeditor.putInt(SP_PERSISTENT_CURRENT_PAGE, page) ;
        sPeditor.apply();
    }



    public void setWorkingPuzzle(int puzzle) {
        sPeditor.putInt(SP_CURR_PUZZLE, puzzle) ;
        sPeditor.apply();
    }

    public int getWorkingPuzzle() {
        return currentLevelSharedPref.getInt(SP_CURR_PUZZLE, 0);
    }


    public int getWorkingPage() {
        return currentLevelSharedPref.getInt(SP_PAGE_IDX, 0);
    }

    public void setWorkingPage(int page) {
        sPeditor.putInt(SP_PAGE_IDX, page) ;
        sPeditor.apply();
    }



    public int getNumOfPages() {
        return NUM_OF_PAGES ;
    }

    public int getNumOfPuzzles() {
        return NUM_OF_PUZZLES ;
    }

    public Boolean incrementLevel() {
        int page = getWorkingPage() ;
        int puzzle = getWorkingPuzzle() ;
        if (puzzle < NUM_OF_PUZZLES - 1) {
            puzzle++;
        } else {
            if (page < NUM_OF_PAGES - 1) {
                puzzle = 0;
                page++;
            } else {
                return false;
            }
        }
        updateLevel(page, puzzle);
        return true ;
    }

    public void saveCurrentLevelToPersistent() {
        setPersistentPage(getWorkingPage());
        setPersistentPuzzle(getWorkingPuzzle());
    }

    public void updateLevel(int page, int puzzle) {
        setWorkingPage(page);
        setWorkingPuzzle(puzzle);
    }
}
