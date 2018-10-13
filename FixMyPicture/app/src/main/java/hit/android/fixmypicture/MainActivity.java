package hit.android.fixmypicture;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    private LevelsContainer levelsContainer ;
    private HashMap<String, ArrayList<LevelSettings>> levels ;
    private ArrayList<LevelSettings> currentPuzzlesList ;
    private SharedPreferences sp ;
    private int currentPuzzle ;
    private int currentLevelIdx ;
    private String[] levelsProgression ;
    private MediaPlayer bgmPlayer;

    public static final String SP_CURR_PUZZLE = "currentPuzzle" ;
    public static final String SP_CURR_USER_PREF_NAME = "currentUser" ;
    public static final String SP_CURR_LEVEL_IDX = "currIdx" ;
    public static final String INTENT_PIC_ID = "PicId" ;

    public static final String INTENT_DIMENSION = "Dimension" ;
    public static final String INTENT_CALLED_BY = "calledBy" ;
    public static final String INTENT_CURR_LEVEL_NAME = "level_name" ;
    public static final String INTENT_CURR_LEVEL_IDX = "level_idx" ;
    public static final String INTENT_CURR_LEVEL_PROGRESSION = "level_progression" ;
    public static final String INTENT_TOP_SCORE = "top_score" ;
    public static final String INTENT_LEVEL_TIME = "level_time" ;
    public static final String CALLED_BY_STORY_MESSAGE = "story" ;
    public static final String CALLED_BY_STORY_FREE_GAME = "free_game" ;
    public static final String DISPLAY_TUTORIAL = "display_tutorial" ;
    public static final String FACTS_ARRAY = "facts_array" ;

    private void initSharedData() {
        levelsContainer = LevelsContainer.getInstance(this) ;
        levels = levelsContainer.getLevelContainer();
        levelsProgression = levelsContainer.getLevelsProgression() ;

        sp = getSharedPreferences(SP_CURR_USER_PREF_NAME, Context.MODE_PRIVATE) ;
        currentPuzzle = sp.getInt(SP_CURR_PUZZLE, 0) ;
        currentLevelIdx = sp.getInt(SP_CURR_LEVEL_IDX, 0) ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        initSharedData();


        bgmPlayer = MediaPlayer.create(MainActivity.this, R.raw.main_bgm);
        bgmPlayer.start();
        ImageButton playButton = findViewById(R.id.play_btn);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent playIntent = new Intent(MainActivity.this,FreeModeScreen.class);
                MainActivity.this.startActivity(playIntent);
            }
        });

        ImageButton newGame = findViewById(R.id.story_new_game) ;
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPuzzle = 0 ;
                currentPuzzlesList = levels.get(levelsProgression[0]) ;
                LevelSettings lvl = currentPuzzlesList.get(currentPuzzle) ;
                Intent intent = new Intent(getBaseContext(), PlayScreen.class);
                intent.putExtra(INTENT_PIC_ID, lvl.getPicId());
                intent.putExtra(INTENT_DIMENSION, lvl.getDimension());
                intent.putExtra(INTENT_CALLED_BY, CALLED_BY_STORY_MESSAGE) ;
                intent.putExtra(INTENT_CURR_LEVEL_NAME, lvl.getName()) ;
                intent.putExtra(DISPLAY_TUTORIAL,1);
                SharedPreferences.Editor editor = sp.edit() ;
                editor.putInt(SP_CURR_PUZZLE, 0) ;
                editor.putInt(SP_CURR_LEVEL_IDX, 0) ;
                editor.apply();
                startActivity(intent);
            }
        });

        ImageButton continueGame = findViewById(R.id.story_continue_btn) ;
        continueGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp = getSharedPreferences(SP_CURR_USER_PREF_NAME, Context.MODE_PRIVATE) ;
                currentPuzzle = sp.getInt(SP_CURR_PUZZLE, 0) ;
                currentLevelIdx = sp.getInt(SP_CURR_LEVEL_IDX, 1) ;
                if(currentLevelIdx == levelsProgression.length)
                {
                    Toast.makeText(getApplicationContext(), R.string.your_the_best, Toast.LENGTH_SHORT).show();
                }
                else if (currentLevelIdx != 0 || currentPuzzle != 0) {
                    currentPuzzlesList = levels.get(levelsProgression[currentLevelIdx]);
                    LevelSettings lvl = currentPuzzlesList.get(currentPuzzle);

                    Intent intent = new Intent(getBaseContext(), PlayScreen.class);
                    intent.putExtra(INTENT_PIC_ID, lvl.getPicId());
                    intent.putExtra(INTENT_DIMENSION, lvl.getDimension());
                    intent.putExtra(INTENT_CALLED_BY, CALLED_BY_STORY_MESSAGE) ;
                    intent.putExtra(INTENT_CURR_LEVEL_NAME, lvl.getName()) ;
                    startActivity(intent);

                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.no_saved_game, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        bgmPlayer.pause();
        bgmPlayer.seekTo(0);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        bgmPlayer.start();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.quite_game)
                .setMessage(R.string.quite_game_msg)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        moveTaskToBack(true);
                    }

                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}

