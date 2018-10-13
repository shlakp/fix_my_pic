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


public class WinScreen extends AppCompatActivity {
    private String levelName ;
    private int winTime ;
    private int picId ;
    private int dimension ;
    private String calledBy ;
    private int bestScore ;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.win_screen);
        TextView levelNameView = findViewById(R.id.win_screen_level) ;
        TextView levelTime = findViewById(R.id.win_screen_time) ;
        levelName = getIntent().getStringExtra(MainActivity.INTENT_CURR_LEVEL_NAME) ;
        winTime = getIntent().getIntExtra(MainActivity.INTENT_LEVEL_TIME, 0) ;
        calledBy = getIntent().getStringExtra(MainActivity.INTENT_CALLED_BY) ;
        dimension = getIntent().getIntExtra(MainActivity.INTENT_DIMENSION, 0) ;
        picId = getIntent().getIntExtra(MainActivity.INTENT_PIC_ID, 0) ;
        bestScore = getIntent().getIntExtra(MainActivity.INTENT_TOP_SCORE, 0) ;

        String levelString = getResources().getString(R.string.level) +  " " + levelName ;
        levelNameView.setText(levelString);

        String timeString = secondsToString(winTime) ;
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
                    intent.putExtra(MainActivity.INTENT_PIC_ID, picId);
                    intent.putExtra(MainActivity.INTENT_DIMENSION, dimension);
                    intent.putExtra(MainActivity.INTENT_CALLED_BY, MainActivity.CALLED_BY_STORY_MESSAGE) ;
                    intent.putExtra(MainActivity.INTENT_CURR_LEVEL_NAME, levelName) ;
                    startActivity(intent);
                }
                else if (calledBy.equals(MainActivity.CALLED_BY_STORY_FREE_GAME)) {
                    Intent intent = new Intent(getBaseContext(), FreeModeScreen.class);
                    intent.putExtra(MainActivity.INTENT_CALLED_BY, MainActivity.CALLED_BY_STORY_FREE_GAME) ;
                    startActivity(intent);
                }
            }
        });

        MediaPlayer player = MediaPlayer.create(this,R.raw.victory);
        player.start();
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
