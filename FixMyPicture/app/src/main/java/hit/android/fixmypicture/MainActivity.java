package hit.android.fixmypicture;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import hit.android.fixmypicture.data.LevelProgressionManager;


public class MainActivity extends AppCompatActivity {

    public static final String SP_CURR_USER_PREF_NAME = "currentUser" ;

    public static final String INTENT_CALLED_BY = "calledBy" ;

    public static final String CALLED_BY_STORY_MESSAGE = "story" ;
    public static final String CALLED_BY_STORY_FREE_GAME = "free_game" ;
    public static final String DISPLAY_TUTORIAL = "display_tutorial" ;
    private MediaPlayer bgmPlayer;
    private LevelProgressionManager currLevelManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        currLevelManager = new LevelProgressionManager(this) ;
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
                Intent intent = new Intent(getBaseContext(), PlayScreen.class);
                currLevelManager.setWorkingPuzzle(0);
                currLevelManager.setWorkingPage(0);
                currLevelManager.setPersistentPuzzle(0);
                currLevelManager.setPersistentPage(0);
                intent.putExtra(INTENT_CALLED_BY, CALLED_BY_STORY_MESSAGE) ;
                startActivity(intent);
            }
        });

        ImageButton continueGame = findViewById(R.id.story_continue_btn) ;
        continueGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPuzzle = currLevelManager.getPersistentPuzzle() ;
                int currentPage = currLevelManager.getPersistentPage() ;
                if(currentPage == currLevelManager.getNumOfPages() && currentPuzzle == currLevelManager.getNumOfPuzzles())
                {
                    Toast.makeText(getApplicationContext(), R.string.your_the_best, Toast.LENGTH_SHORT).show();
                }
                else if (currentPage != 0 || currentPuzzle != 0) {
                    currLevelManager.setWorkingPage(currentPage);
                    currLevelManager.setWorkingPuzzle(currentPuzzle);
                    Intent intent = new Intent(getBaseContext(), PlayScreen.class);
                    intent.putExtra(INTENT_CALLED_BY, CALLED_BY_STORY_MESSAGE) ;
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

