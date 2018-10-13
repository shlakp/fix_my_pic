package hit.android.fixmypicture;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PlayScreen extends AppCompatActivity {
    private GridLayout gameBoard;
    private GameTile[][] picSlices;
    private MediaPlayer bgmPlayer;
    private int dimension;
    private Integer picId;
    private Integer bgmId;
    private int timeElapsed;
    private String curLvlName ;

    private LevelSettings curLevel ;

    private LevelsContainer levelsObj ;
    private HashMap<String, ArrayList<LevelSettings>> levels ;
    private SharedPreferences sp ;
    private int currentPuzzle ;
    private int currentLevelIdx ;
    private String calledBy ;
    private String displayedFact;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private ActionMode mActionMode;
    private AlertDialog alert;
    private ArrayList<String> picFacts;

    private void setNextLevelAttributes() {
        if (calledBy.equals(MainActivity.CALLED_BY_STORY_MESSAGE)) {
            curLevel = getCurLevel();
            picFacts = curLevel.getPicFacts();
            curLvlName = curLevel.getName();
            picId = curLevel.getPicId();
            dimension = curLevel.getDimension();
        }
        else if (calledBy.equals(MainActivity.CALLED_BY_STORY_FREE_GAME)) {
            curLvlName = getIntent().getStringExtra(MainActivity.INTENT_CURR_LEVEL_NAME);
            picId = getIntent().getIntExtra(MainActivity.INTENT_PIC_ID, 0);
            dimension = getIntent().getIntExtra(MainActivity.INTENT_DIMENSION, 0);
            picFacts = getIntent().getStringArrayListExtra(MainActivity.FACTS_ARRAY);
        }
    }

    private int checkBestScore() {
        int curPzl = currentPuzzle ;
        if (calledBy.equals(MainActivity.CALLED_BY_STORY_FREE_GAME)) {
            curPzl -= 1 ;
        }

        String[] levelsProgression = levelsObj.getLevelsProgression() ;
        if (calledBy.equals(MainActivity.CALLED_BY_STORY_MESSAGE)) {
            if (!levels.get(levelsProgression[currentLevelIdx]).get(curPzl).getPassed()) {
                levels.get(levelsProgression[currentLevelIdx]).get(curPzl).setTopScore(timeElapsed);
                levelsObj.updateLevels();
                return timeElapsed;
            }
        }

        int curTop = levels.get(levelsProgression[currentLevelIdx]).get(curPzl).getTopScore();

        if (curTop < timeElapsed) {

            return curTop ;
        }
        levels.get(levelsProgression[currentLevelIdx]).get(curPzl).setTopScore(timeElapsed);
        levelsObj.updateLevels();
        return timeElapsed ;
    }

    private void setPassed() {
        String[] levelsProgression = levelsObj.getLevelsProgression() ;
        levels.get(levelsProgression[currentLevelIdx]).get(currentPuzzle).setPassed(true);
    }

    private LevelSettings getCurLevel() {
        String[] levelsProgression = levelsObj.getLevelsProgression() ;
        return levels.get(levelsProgression[currentLevelIdx]).get(currentPuzzle) ;
    }

    private void updateSharedPrefference() {
        if (currentPuzzle == 5) {
            currentPuzzle = 0 ;
            currentLevelIdx++ ;
        }
        else {
            currentPuzzle++ ;
        }
        SharedPreferences.Editor editor = sp.edit() ;
        editor.putInt(MainActivity.SP_CURR_PUZZLE, currentPuzzle) ;
        editor.putInt(MainActivity.SP_CURR_LEVEL_IDX, currentLevelIdx) ;
        editor.apply();
    }

    private void handleWin() {
        Intent intent = new Intent(getBaseContext(), WinScreen.class);
        intent.putExtra(MainActivity.INTENT_LEVEL_TIME, timeElapsed) ;
        intent.putExtra(MainActivity.INTENT_CURR_LEVEL_NAME, curLvlName) ;
        intent.putExtra(MainActivity.INTENT_DIMENSION, dimension) ;
        intent.putExtra(MainActivity.INTENT_PIC_ID, picId) ;

        int bestLvlScore = checkBestScore() ;
        intent.putExtra(MainActivity.INTENT_TOP_SCORE, bestLvlScore) ;
        if (calledBy.equals(MainActivity.CALLED_BY_STORY_FREE_GAME)) {
            intent.putExtra(MainActivity.INTENT_CALLED_BY, MainActivity.CALLED_BY_STORY_FREE_GAME) ;
            startActivity(intent);
        }
        else if (calledBy.equals(MainActivity.CALLED_BY_STORY_MESSAGE)) {
            setPassed();
            updateSharedPrefference();
            intent.putExtra(MainActivity.INTENT_CALLED_BY, MainActivity.CALLED_BY_STORY_MESSAGE) ;
            levelsObj.updateLevels();
            startActivity(intent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_play_screen);

        levelsObj = LevelsContainer.getInstance(this);
        levels = levelsObj.getLevelContainer();
        sp = getSharedPreferences(MainActivity.SP_CURR_USER_PREF_NAME, Context.MODE_PRIVATE) ;
        currentPuzzle = sp.getInt(MainActivity.SP_CURR_PUZZLE, 0) ;
        currentLevelIdx = sp.getInt(MainActivity.SP_CURR_LEVEL_IDX, 0) ;

        calledBy = getIntent().getStringExtra(MainActivity.INTENT_CALLED_BY) ;
        if(calledBy.equals(MainActivity.CALLED_BY_STORY_FREE_GAME))
        {
            currentPuzzle = getIntent().getIntExtra(MainActivity.INTENT_CURR_LEVEL_IDX,currentPuzzle);
            currentLevelIdx = getIntent().getIntExtra(MainActivity.INTENT_CURR_LEVEL_PROGRESSION,currentLevelIdx);
        }
        setNextLevelAttributes() ;

        bgmId = R.raw.tropical_bgm;

        TextView lvlName = findViewById(R.id.lvl_name) ;
        String s = getResources().getString(R.string.level) +  " " + curLvlName ;
        lvlName.setText(s);

        ImageView fullPicture = findViewById(R.id.full_picture);
        if(picId != 0)
        {
            fullPicture.setImageResource(picId);
        }
        gameBoard = findViewById(R.id.GameBoard);
        gameBoard.setColumnCount(dimension);
        gameBoard.setRowCount(dimension);
        gameBoard.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final View view = (View) event.getLocalState();
                GameTile currnetTile = (GameTile)view;
                final int index ;

                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_LOCATION:
                        // do nothing if hovering above own position
                        if (view == v) return true;
                        break;
                    case DragEvent.ACTION_DROP:

                        index = calculateNewIndex(event.getX(), event.getY());
                        int x = index % gameBoard.getRowCount() ;
                        int y = index / gameBoard.getRowCount() ;
                        // Check drop target is empty tile and that delta between drop target and tile is legal
                        // arrange matrix accordingly
                        if (picSlices[x][y].isEmptyTile() &&
                                (
                                        ( (currnetTile.getCurrentXCord() - x) >= -1  &&  (currnetTile.getCurrentXCord() - x) <= 1 && (currnetTile.getCurrentYCord() - y) == 0 )
                                                ||
                                                ( (currnetTile.getCurrentYCord() - y) >= -1  &&  (currnetTile.getCurrentYCord() - y) <= 1 && (currnetTile.getCurrentXCord() - x) == 0 )
                                ))
                        {
                            GameTile tmp = picSlices[x][y] ;
                            picSlices[x][y] = currnetTile ;
                            picSlices[currnetTile.getCurrentXCord()][currnetTile.getCurrentYCord()] = tmp ;
                        }
                        if(view != null)
                        {
                            drawBoard();
                            view.setVisibility(View.VISIBLE);
                        }
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        if (!event.getResult()) {
                            view.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                return true ;
            }
            private int calculateNewIndex(float x, float y) {
                // calculate which column to move to
                final float cellWidth = gameBoard.getWidth() / gameBoard.getColumnCount();
                final int column = (int)(x / cellWidth);

                // calculate which row to move to
                final float cellHeight = gameBoard.getHeight() / gameBoard.getRowCount();
                final int row = (int)Math.floor(y / cellHeight);

                // the items in the GridLayout is organized as a wrapping list
                // and not as an actual grid, so this is how to get the new index
                int index = row * gameBoard.getColumnCount() + column;
                if (index >= gameBoard.getChildCount()) {
                    index = gameBoard.getChildCount() - 1;
                }

                return index;
            }
        });
        if(picId != null)
        {
            picSlices = splitBitmap(BitmapFactory.decodeResource(getResources(), picId), dimension, dimension);
        }
        randomizeBoard();
        drawBoard();
        assignTiles();

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);


        bgmPlayer = MediaPlayer.create(PlayScreen.this, bgmId);
        bgmPlayer.start();
        handleTutorial();
    }

    private void solvePuzzle()
    {
        GameTile[][] newArray = new GameTile[picSlices.length][picSlices[0].length];
        for(int i=0;i<picSlices.length;i++)
        {
            for(int j=0;j<picSlices[i].length;j++)
            {
                GameTile tile = picSlices[i][j];
                newArray[tile.getOriginalXCord()][tile.getOriginalYCord()] = tile;
            }
        }
        picSlices = newArray;
    }

    private void setTimer()
    {
        final TextView textView = findViewById(R.id.elapsed_time);

        final Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                //Update and display
                timeElapsed += 1;
                int timeInMills = timeElapsed * 1000;
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeInMills),
                        TimeUnit.MILLISECONDS.toMinutes(timeInMills) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMills)),
                        TimeUnit.MILLISECONDS.toSeconds(timeInMills) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMills)));

                textView.setText(hms);
                //Call this again in one second (1000 milliseconds)
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(r, 1000);
    }

    private void assignTiles()
    {
        for(int i=0;i<gameBoard.getChildCount();i++)
        {
            final GameTile tile = (GameTile)gameBoard.getChildAt(i);
            tile.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (!tile.isEmptyTile() && motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                                view);
                        view.startDrag(data, shadowBuilder, view, 0);
                        view.setVisibility(View.INVISIBLE);

                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
    }

    private boolean validateBoard()
    {
        for(GameTile[] tileRow : picSlices)
        {
            for(GameTile tile : tileRow)
            {
                if(tile.getOriginalXCord() != tile.getCurrentXCord() || tile.getOriginalYCord() != tile.getCurrentYCord())
                {
                    return false;
                }
            }

        }

        return true;
    }

    private void randomizeBoard() {
        long i = dimension * dimension - 1;
        int emptyCellRow =0;
        int emptyCellCol = 0;
        while (i >0)
        {
            int j = (int)Math.floor(Math.random() * i);
            int xi = (int)i % dimension;
            int yi = (int)Math.floor(i / dimension);
            int xj = j % dimension;
            int yj = (int)Math.floor(j / dimension);
            if(picSlices[xi][yi].isEmptyTile())
            {
                emptyCellCol =xi;
                emptyCellRow = yi;
            }
            else if(picSlices[xj][yj].isEmptyTile())
            {
                emptyCellCol = xj;
                emptyCellRow = yj;
            }
            swapTiles(xi, yi, xj, yj);
            i--;

        }
        if (!isSolvable(emptyCellRow + 1))
        {
            if (emptyCellRow == 0 && emptyCellCol <= 1) {
                swapTiles(dimension - 2, dimension- 1, dimension - 1, dimension- 1);
            } else {
                swapTiles(0, 0, 1, 0);
            }
        }
    }

    private void swapTiles(int xi,int yi,int xj,int yj)
    {
        GameTile tempTile = picSlices[xi][yi];
        picSlices[xi][yi] = picSlices[xj][yj];
        picSlices[xi][yi].setCurrentXCord(xi);
        picSlices[xi][yi].setCurrentYCord(yi);
        picSlices[xj][yj] = tempTile;
        tempTile.setCurrentXCord(xj);
        tempTile.setCurrentYCord(yj);
    }

    private void drawBoard()
    {

        gameBoard.removeAllViews();
        for(int i=0;i<picSlices.length;i++)
        {
            for(int j=0;j<picSlices[i].length;j++)
            {
                picSlices[i][j].setCurrentXCord(i);
                picSlices[i][j].setCurrentYCord(j);
                gameBoard.addView(picSlices[i][j]);
            }
        }
        if (validateBoard()) {
            handleWin() ;
        }
    }

    private GameTile[][] splitBitmap(Bitmap bitmap, int xCount, int yCount) {
        // Allocate a two dimensional array to hold the individual images.
        GameTile[][] bitmaps = new GameTile[xCount][yCount];
        int width, height;
        // Divide the original bitmap width by the desired vertical column count
        width = bitmap.getWidth() / xCount;
        // Divide the original bitmap height by the desired horizontal row count
        height = bitmap.getHeight() / yCount;
        // Loop the array and create bitmaps for each coordinate


        for(int i = 0; i < xCount; ++i) {
            for(int j = 0; j < yCount; ++j) {
                // Create the sliced bitmap
                GameTile tile = new GameTile(gameBoard.getContext());

                tile.setImageBitmap(Bitmap.createBitmap(bitmap, i * width, j * height, width, height));
                tile.setOriginalXCord(i);
                tile.setOriginalYCord(j);

                if(xCount == i + 1 && yCount == j + 1)
                {
                    tile.setAlpha(0);
                    tile.setVisibility(View.INVISIBLE);
                    tile.setEmptyTile(true);
                }
                bitmaps[i][j] = tile;
            }
        }
        // Return the array
        return bitmaps;
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
                .setTitle(R.string.quite_level)
                .setMessage(R.string.quite_level_msg)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    }

                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private boolean isSolvable(int emptyRow)
    {
        if (dimension % 2 == 1) {
            return (sumInversions() % 2 == 0);
        } else {
            return ((sumInversions() + dimension - emptyRow) % 2 == 0);
        }
    }

    private int sumInversions()
    {
        int inversions = 0;
        for (int j = 0; j < dimension; ++j) {
            for (int i = 0; i < dimension; ++i) {
                inversions += countInversions(i, j);
            }
        }
        return inversions;
    }

    private int countInversions(int i,int j) {
        int inversions = 0;
        int tileNum = j * dimension + i;
        int lastTile = dimension * dimension;
        int tileValue = picSlices[i][j].getOriginalYCord() * dimension + picSlices[i][j].getOriginalXCord();
        for (int q = tileNum + 1; q < lastTile; ++q) {
            int k = q % dimension;
            int l = (int) Math.floor(q / dimension);

            int compValue = picSlices[k][l].getOriginalYCord() * dimension + picSlices[k][l].getOriginalXCord();
            if (tileValue > compValue && tileValue != (lastTile - 1)) {
                ++inversions;
            }
        }
        return inversions;
    }

    private void handleTutorial()
    {
        int tutorialCheck = getIntent().getIntExtra(MainActivity.DISPLAY_TUTORIAL,0);
        if(tutorialCheck == 1)
        {
            getIntent().putExtra(MainActivity.DISPLAY_TUTORIAL,0);
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.star_on)
                    .setTitle(R.string.welcome)
                    .setMessage(R.string.intro_msg)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new AlertDialog.Builder(PlayScreen.this)
                                    .setIcon(android.R.drawable.star_on)
                                    .setTitle(R.string.how_to_play)
                                    .setMessage(
                                            R.string.tutorial_msg)
                                    .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            setTimer();
                                        }
                                    })
                                    .show();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setTimer();
                        }
                    })
                    .show();
        }
        else
        {
            setTimer();
        }

    }




    private void zoomImageFromThumb(final View thumbView, int imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = findViewById(
                R.id.expanded_image);
        expandedImageView.setImageResource(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.main_layout)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.enlarge_picture:
                        zoomImageFromThumb(findViewById(R.id.full_picture), picId);
                        return true;
                    case R.id.solve_puzzle:
                        solvePuzzle();
                        drawBoard();
                        return true;
                    case R.id.show_fact:
                        alert = new AlertDialog.Builder(PlayScreen.this).create();
                        Random rand = new Random();
                        String factIndex  = picFacts.get(rand.nextInt(picFacts.size()));
                        displayedFact = levelsObj.getPicsFacts().get(factIndex);
                        final TextView input = new TextView(PlayScreen.this);
                        input.setText(displayedFact);
                        input.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                if(mActionMode != null)
                                {
                                    return false;
                                }
                                mActionMode = startSupportActionMode(mActionModeCallback);
                                return true;
                            }
                        });
                        alert.setView(input);
                        alert.show();
                        return true;
                    default:
                        return false;
                }
            }

        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.thumb_menu, popup.getMenu());
        popup.show();
    }

    private final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            alert.dismiss();
            actionMode.getMenuInflater().inflate(R.menu.fact_menu,menu);
            actionMode.setTitle(getString(R.string.choose_option));

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId())
            {
                case R.id.copy_fact:
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(getString(R.string.random_fact), displayedFact);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), R.string.text_copied,Toast.LENGTH_SHORT).show();
                    actionMode.finish();
                    return true;
                case R.id.share_fact:
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT,displayedFact);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.interesting_fact));
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_with_friend)));
                    actionMode.finish();
                    return true;
                    default:
                        return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
        mActionMode = null;
        }
    };



}