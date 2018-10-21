/*
Way of grouping any number of "LevelsSettings" and adding metadata
 */
package hit.android.fixmypicture.data;

import java.util.ArrayList;

import hit.android.fixmypicture.data.Level;

public class LevelsPage {
    private ArrayList<Level> levelsPage ;
    private int pageSize = 0 ;
    private int headerStringIdx ;

    public LevelsPage() {
        levelsPage = new ArrayList<>() ;
    }

    public void setHeaderStringIdx(int headerStringIdx) {
        this.headerStringIdx = headerStringIdx;
    }
    public int getHeaderStringIdx() {

        return headerStringIdx;
    }


    public void addLevel(Level lvl) {
        levelsPage.add(lvl);
        pageSize++ ;
    }

    public Level getLevel(int idx) {
        return levelsPage.get(idx) ;
    }
}
