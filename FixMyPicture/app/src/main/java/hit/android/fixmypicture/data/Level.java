package hit.android.fixmypicture.data;

import java.util.ArrayList;

public class Level {

    private String name;
    private int dimension;
    private int picId;
    private int topScore;
    private Boolean isPassed;



    public void setPassed(Boolean isPassed) {
        this.isPassed = isPassed;
    }

    public Boolean getPassed() {
        return isPassed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public int getTopScore() {
        return topScore;
    }

    public void setTopScore(int topScore) {
        this.topScore = topScore;
    }

}
