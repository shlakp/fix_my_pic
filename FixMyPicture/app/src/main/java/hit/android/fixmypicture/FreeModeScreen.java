package hit.android.fixmypicture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hit.android.fixmypicture.data.Level;
import hit.android.fixmypicture.data.LevelProgressionManager;
import hit.android.fixmypicture.data.LevelsContainer;
import hit.android.fixmypicture.data.LevelsPage;

public class FreeModeScreen extends AppCompatActivity {
    private LevelsPage levelsGroup;
    private int currentPage;
    private LevelProgressionManager currentLevelID ;
    private LevelsContainer levelsContainer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_free_mode_screen);

        levelsContainer = LevelsContainer.getInstance(this);
        currentLevelID = new LevelProgressionManager(this) ;

        currentPage = 0;
        levelsGroup = levelsContainer.getLevelContainer().get(currentPage);
        buildLevel();

        findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPage < currentLevelID.getNumOfPages() - 1) {
                    for (int i = 0; i < 6; i++) {
                        if (!levelsGroup.getLevel(i).getPassed()) {
                            Toast.makeText(getApplicationContext(), R.string.not_all_pazzels_solved, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    ++currentPage;
                    levelsGroup = levelsContainer.getLevelContainer().get(currentPage);
                    buildLevel();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.your_the_best, Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.prev_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPage != 0) {
                    --currentPage;
                    levelsGroup = levelsContainer.getLevelContainer().get(currentPage);
                    buildLevel();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.first_level, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initSingleView(ArrayList<ImageButton> btns) {
        for (int i = 0; i < btns.size(); i++) {
            ImageButton btn = btns.get(i);
            if (levelsGroup.getLevel(i).getPassed()) {
                int id = getResources().getIdentifier("pic" + (i + 1), "drawable", getPackageName());
                btn.setImageResource(id);
                btn.setBackgroundResource(R.color.black);
                btn.setAlpha(255);
            } else {
                int id = getResources().getIdentifier("pic" + (i + 1) + "_bw", "drawable", this.getPackageName());
                btn.setImageResource(id);
                btn.setAlpha(64);
            }
        }
    }
    private void buildLevel() {
        TextView header = findViewById(R.id.levels_page_name);
        header.setText(levelsGroup.getHeaderStringIdx());

        ArrayList<ImageButton> btns = new ArrayList<>();

        ImageButton btn1 = findViewById(R.id.img_btn_1);
        btn1.setOnClickListener(new LevelButtonsHandler());
        btns.add(btn1);


        ImageButton btn2 = findViewById(R.id.img_btn_2);
        btn2.setOnClickListener(new LevelButtonsHandler());
        btns.add(btn2);

        ImageButton btn3 = findViewById(R.id.img_btn_3);
        btn3.setOnClickListener(new LevelButtonsHandler());
        btns.add(btn3);

        ImageButton btn4 = findViewById(R.id.img_btn_4);
        btn4.setOnClickListener(new LevelButtonsHandler());
        btns.add(btn4);

        ImageButton btn5 = findViewById(R.id.img_btn_5);
        btn5.setOnClickListener(new LevelButtonsHandler());
        btns.add(btn5);

        ImageButton btn6 = findViewById(R.id.img_btn_6);
        btn6.setOnClickListener(new LevelButtonsHandler());
        btns.add(btn6);

        initSingleView(btns);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    class LevelButtonsHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Level lvl;
            if (view.getId() == R.id.img_btn_1) {
                lvl = levelsGroup.getLevel(0);
                if (lvl.getPassed()) {
                    Intent intent = new Intent(getBaseContext(), PlayScreen.class);
                    currentLevelID.setWorkingPage(currentPage);
                    currentLevelID.setWorkingPuzzle(0);
                    intent.putExtra(MainActivity.INTENT_CALLED_BY, MainActivity.CALLED_BY_STORY_FREE_GAME);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.must_pass_in_story, Toast.LENGTH_SHORT).show();
                }
            }
            if (view.getId() == R.id.img_btn_2) {
                lvl = levelsGroup.getLevel(1);
                if (lvl.getPassed()) {
                    Intent intent = new Intent(getBaseContext(), PlayScreen.class);
                    currentLevelID.setWorkingPage(currentPage);
                    currentLevelID.setWorkingPuzzle(1);
                    intent.putExtra(MainActivity.INTENT_CALLED_BY, MainActivity.CALLED_BY_STORY_FREE_GAME);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.must_pass_in_story, Toast.LENGTH_SHORT).show();
                }
            }
            if (view.getId() == R.id.img_btn_3) {
                lvl = levelsGroup.getLevel(2);
                if (lvl.getPassed()) {
                    Intent intent = new Intent(getBaseContext(), PlayScreen.class);
                    currentLevelID.setWorkingPage(currentPage);
                    currentLevelID.setWorkingPuzzle(2);
                    intent.putExtra(MainActivity.INTENT_CALLED_BY, MainActivity.CALLED_BY_STORY_FREE_GAME);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.must_pass_in_story, Toast.LENGTH_SHORT).show();
                }
            }
            if (view.getId() == R.id.img_btn_4) {
                lvl = levelsGroup.getLevel(3);
                if (lvl.getPassed()) {
                    Intent intent = new Intent(getBaseContext(), PlayScreen.class);
                    currentLevelID.setWorkingPage(currentPage);
                    currentLevelID.setWorkingPuzzle(3);
                    intent.putExtra(MainActivity.INTENT_CALLED_BY, MainActivity.CALLED_BY_STORY_FREE_GAME);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.must_pass_in_story, Toast.LENGTH_SHORT).show();
                }
            }
            if (view.getId() == R.id.img_btn_5) {
                lvl = levelsGroup.getLevel(4);
                if (lvl.getPassed()) {
                    Intent intent = new Intent(getBaseContext(), PlayScreen.class);
                    currentLevelID.setWorkingPage(currentPage);
                    currentLevelID.setWorkingPuzzle(4);
                    intent.putExtra(MainActivity.INTENT_CALLED_BY, MainActivity.CALLED_BY_STORY_FREE_GAME);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.must_pass_in_story, Toast.LENGTH_SHORT).show();
                }
            }
            if (view.getId() == R.id.img_btn_6) {
                lvl = levelsGroup.getLevel(5);
                if (lvl.getPassed()) {
                    Intent intent = new Intent(getBaseContext(), PlayScreen.class);
                    currentLevelID.setWorkingPage(currentPage);
                    currentLevelID.setWorkingPuzzle(5);
                    intent.putExtra(MainActivity.INTENT_CALLED_BY, MainActivity.CALLED_BY_STORY_FREE_GAME);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.must_pass_in_story, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
