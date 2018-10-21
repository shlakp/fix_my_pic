package hit.android.fixmypicture;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import hit.android.fixmypicture.data.Level;
import hit.android.fixmypicture.data.LevelProgressionManager;
import hit.android.fixmypicture.data.LevelsContainer;


public class WinScreen extends AppCompatActivity {
    private String calledBy ;
    private LevelsContainer levelsContainer ;
    private Level curLevel ;
    private LevelProgressionManager currentLevelID ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.win_screen);


        currentLevelID = new LevelProgressionManager(this) ;
        levelsContainer = LevelsContainer.getInstance(this) ;
        calledBy = getIntent().getStringExtra(MainActivity.INTENT_CALLED_BY) ;
        curLevel = levelsContainer.getLevel(currentLevelID.getWorkingPage(), currentLevelID.getWorkingPuzzle()) ;


        String levelName = curLevel.getName() ;
        int bestScore = curLevel.getTopScore() ;
        int lvlWinTime = getIntent().getIntExtra(PlayScreen.INTENT_LEVEL_TIME, 0) ;


        TextView levelNameView = findViewById(R.id.win_screen_level) ;
        String levelString = getResources().getString(R.string.level) +  " " + levelName ;
        levelNameView.setText(levelString);

        TextView levelTime = findViewById(R.id.win_screen_time) ;
        String timeString = secondsToString(lvlWinTime) ;
        levelTime.setText(timeString);


        TextView bestScoreView = findViewById(R.id.win_screen_space) ;
        String bestScoreTxt =  getResources().getString(R.string.best_score) + secondsToString(bestScore);
        bestScoreView.setText(bestScoreTxt);

        Button backToMain = findViewById(R.id.back_to_main_win_screen) ;
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent ;
                if (calledBy.equals(MainActivity.CALLED_BY_STORY_FREE_GAME)) {
                    intent = new Intent(getBaseContext(), FreeModeScreen.class);
                    startActivity(intent);
                }
                else if (calledBy.equals(MainActivity.CALLED_BY_STORY_MESSAGE)) {
                    if (currentLevelID.incrementLevel()) {
                        currentLevelID.saveCurrentLevelToPersistent();
                    }

                    intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }

            }
        });

        Button continueButton = findViewById(R.id.win_screen_continue) ;
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(calledBy.equals(MainActivity.CALLED_BY_STORY_MESSAGE)) {
                    Intent intent = new Intent(getBaseContext(), PlayScreen.class);
                    intent.putExtra(MainActivity.INTENT_CALLED_BY, calledBy) ;
                    currentLevelID.incrementLevel() ;
                    startActivity(intent);
                }
                else if (calledBy.equals(MainActivity.CALLED_BY_STORY_FREE_GAME)) {
                    Intent intent = new Intent(getBaseContext(), FreeModeScreen.class);
                    intent.putExtra(MainActivity.INTENT_CALLED_BY, calledBy) ;
                    startActivity(intent);
                }
            }
        });

        MediaPlayer player = MediaPlayer.create(this,R.raw.victory);
        player.start();
    }

    private String addZero(int t) {
        String res = Integer.toString(t) ;
        if (t / 10 == 0) {
            res = "0" + res;
        }
        return res ;
    }
    private String secondsToString(int pTime) {
        int hours = pTime / 3600 ;
        pTime %= 3600 ;
        int minutes = pTime / 60 ;
        int seconds = pTime % 60 ;
        return " " + addZero(hours) + ":" + addZero(minutes) + ":" + addZero(seconds) ;
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ImageView winImgWindow = findViewById(R.id.win_image) ;
        AnimationDrawable frameAnimation = (AnimationDrawable) winImgWindow.getBackground();
        frameAnimation.start();
        ImageView winTxtImgWindow = findViewById(R.id.win_image_txt) ;
        AnimationDrawable frameTxtAnimation = (AnimationDrawable) winTxtImgWindow.getBackground();
        frameTxtAnimation.start();

    }
}
