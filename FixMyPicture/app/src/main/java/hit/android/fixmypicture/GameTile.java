package hit.android.fixmypicture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class GameTile extends ImageView {

    public boolean isEmptyTile() {
        return emptyTile;
    }

    public void setEmptyTile(boolean emptyTile) {
        this.emptyTile = emptyTile;
    }

    private boolean emptyTile;

    public int getOriginalXCord() {
        return originalXCord;
    }

    public void setOriginalXCord(int originalXCord) {
        this.originalXCord = originalXCord;
    }

    public int getOriginalYCord() {
        return originalYCord;
    }

    public void setOriginalYCord(int originalYCord) {
        this.originalYCord = originalYCord;
    }

    private int originalXCord;
    private int originalYCord;

    public int getCurrentXCord() {
        return currentXCord;
    }

    public void setCurrentXCord(int currentXCord) {
        this.currentXCord = currentXCord;
    }

    public int getCurrentYCord() {
        return currentYCord;
    }

    public void setCurrentYCord(int currentYCord) {
        this.currentYCord = currentYCord;
    }

    private int currentXCord;
    private int currentYCord;


    public GameTile(final Context context) {
        super(context);
        init();
    }

    public GameTile(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameTile(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setBackgroundResource(R.drawable.image_border);
    }


}