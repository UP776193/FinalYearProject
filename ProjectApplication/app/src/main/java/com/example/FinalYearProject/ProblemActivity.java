package com.example.FinalYearProject;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

import static com.example.FinalYearProject.HomepageActivity.pl;
import static com.example.FinalYearProject.HomepageActivity.scores;

@RequiresApi(api = Build.VERSION_CODES.M)
public class ProblemActivity extends AppCompatActivity {

    private Problem problem;
    private TextView[] blocks; //Used to keep track of movable blocks in activity
    private ArrayList<Slot> slots;
    private Block[] probBlocks; //Used to keep track of where blocks are
    private MediaPlayer clickSoundMP;
    private int problemIndex;
    private int numHints;
    private int score;
    private int highScore;

    //CONSTANTS
    private final int MAX_NUMBER_HINTS = 2;
    private final int MAX_SCORE = 100;
    private final int MIN_SCORE = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        blocks = new TextView[8];
        slots = new ArrayList<>();

        numHints = MAX_NUMBER_HINTS;

        ((Button) findViewById(R.id.btnHint)).setText("Hint (" + numHints + ")");

        problemIndex = getIntent().getIntExtra("problemID",-1);
        problem = pl.get(problemIndex);
        probBlocks = problem.getBlocks();
        printProblemLines();
        setupBlocks();

        try {
            highScore = (int) scores.get(problemIndex);
            ((TextView) findViewById(R.id.tvHighestScore)).setText("Highest Score: " + highScore);
        } catch (ArrayIndexOutOfBoundsException ex) {
            highScore = -1;
            ((TextView) findViewById(R.id.tvHighestScore)).setText("Highest Score: Not Attempted");
        }

        score = MAX_SCORE;
        ((TextView) findViewById(R.id.tvScore)).setText("Score: " + score);

        findViewById(R.id.blockreturn).setOnDragListener(new BlockReturnDragListener());

        clickSoundMP = MediaPlayer.create(this, R.raw.click);
    }

    public void onDestroy() {
        //save scores before closing this activity
        AssetWriter aw = new AssetWriter(this);
        aw.writeScores();
        super.onDestroy();
    }

    //SETUP FUNCTIONS

    public void setupBlocks() {
        blocks[0] = findViewById(R.id.block0);
        blocks[0].setOnTouchListener(new BlockTouchListener());
        blocks[1] = findViewById(R.id.block1);
        blocks[1].setOnTouchListener(new BlockTouchListener());
        blocks[2] = findViewById(R.id.block2);
        blocks[2].setOnTouchListener(new BlockTouchListener());
        blocks[3] = findViewById(R.id.block3);
        blocks[3].setOnTouchListener(new BlockTouchListener());
        blocks[4] = findViewById(R.id.block4);
        blocks[4].setOnTouchListener(new BlockTouchListener());
        blocks[5] = findViewById(R.id.block5);
        blocks[5].setOnTouchListener(new BlockTouchListener());
        blocks[6] = findViewById(R.id.block6);
        blocks[6].setOnTouchListener(new BlockTouchListener());
        blocks[7] = findViewById(R.id.block7);
        blocks[7].setOnTouchListener(new BlockTouchListener());

        for (TextView _block : blocks) { _block.setVisibility(View.VISIBLE); }
        int numBlocks = probBlocks.length;
        for(int i=0; i<numBlocks;i++) {
            blocks[i].setText(probBlocks[i].getText());
            probBlocks[i].setOriginal(blocks[i].getId());
            probBlocks[i].resetBlock();
        }
        for(int j=7; j>= numBlocks; j--) { blocks[j].setVisibility(View.INVISIBLE); }
    }

    public void printProblemLines() {
        String[][] problemLines = problem.getProbLines();
        LinearLayout vlayout = findViewById(R.id.vlayout);
        for(int i =0;i<problemLines.length;i++) {
            LinearLayout hlayout = new LinearLayout(this);
            hlayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            hlayout.setOrientation(LinearLayout.HORIZONTAL);
            vlayout.addView(hlayout);

            for(int j = 0;j<problemLines[i].length;j++) {
                if(problemLines[i][j].equals(getResources().getString(R.string.slotcode))) {
                    //Create a slot here
                    Slot text = createSlot();
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) LinearLayout.LayoutParams.WRAP_CONTENT, (int) LinearLayout.LayoutParams.WRAP_CONTENT);
                    text.setLayoutParams(params);
                    hlayout.addView(text);
                    System.out.println(String.format("Setup: Added \"%s\" line.", (String) text.getText()));
                } else {
                    //Create a regular text view here
                    TextView text = createTextView(problemLines[i][j]);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) LinearLayout.LayoutParams.WRAP_CONTENT, (int) LinearLayout.LayoutParams.WRAP_CONTENT);
                    text.setLayoutParams(params);
                    text.setText(problemLines[i][j]);
                    hlayout.addView(text);
                    System.out.println(String.format("Setup: Added \"%s\" line.", (String) text.getText()));
                }
            }
        }
        System.out.println(String.format("Setup: Problem Lines setup complete."));
    }

    private TextView createTextView(String text) {
        TextView tv = new TextView(this);
        tv.setId((int) System.currentTimeMillis());
        tv.setText(text);
        tv.setPadding(20,40,20,40);
        tv.setTextAppearance(R.style.problemText);
        return tv;
    }

    private Slot createSlot() {
        Slot slot = new Slot(this);
        slot.setOnDragListener(new SlotDragListener());
        slots.add(slot);
        return slot;
    }

    //SKIP FUNCTIONS

    public void skip(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Skip?");
        builder.setMessage("Are you sure you want to skip this question?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                nextProblem();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //VIEW PROBLEM FUNCTIONS

    public void viewProblem(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(problem.getProbDesc());
        builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //BACK FUNCTIONS

    public void back(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Go Back");
        builder.setMessage("Are you sure you want to return to the homepage? All unsaved progress will be lost.");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Return to the Homepage
                Intent intent = new Intent(getBaseContext(), HomepageActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //SUBMIT FUNCTIONS

    public void submit(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (checkCorrect()) {
            builder.setTitle("CORRECT");
            builder.setMessage("Well Done, that's correct!");
            builder.setNeutralButton("Next Question", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    nextProblem();
                    dialog.dismiss();
                }
            });
        } else {
            builder.setTitle("INCORRECT");
            builder.setMessage("There appears to be a block in the wrong place.");
            builder.setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean checkCorrect() {
        String[] solution = problem.getSolution();
        boolean correct = true;
        for(int i = 0; i<slots.size(); i++){
            if(!slots.get(i).getText().equals(solution[i])) {
                correct = false;
            }
        }
        return correct;
    }

    public void nextProblem() {
        Intent intent = new Intent(this, ProblemActivity.class);
        if(problemIndex + 1 == pl.size()) {
            problemIndex = 0;
        } else {
            problemIndex++;
        }
        intent.putExtra("problemID", problemIndex);
        startActivity(intent);
        finish();
    }

    public void giveHint(View view) {
        //update score
        updateScore(-25);

        //take a random slot
        int slotID = (new Random()).nextInt(slots.size());
        Slot slot = slots.get(slotID);

        if(!slot.isEmpty()) {
            //reset original block
            String currentText = slot.getCurrentText();
            resetBlock(currentText);
        }

        String[] solution = problem.getSolution();
        String correctAnswer = solution[slotID];

        int blockID;
        try {
            blockID = findProbBlock(correctAnswer);
            disableBlock(blocks[blockID]);
        } catch(MissingBlockException ex) {
            ex.printStackTrace();
        }

        slot.setCorrect();
        slot.setCurrentText(solution[slotID]);

        for(Slot _slot : slots) {
            if(_slot.getText().equals(slot.getCurrentText()) && _slot != slot) {
                _slot.resetCurrentText();
                _slot.setOnTouchListener(null);
                _slot.setEmpty();
            }
        }
        numHints -= 1;
        if(numHints == 0) {
            //set hint button to be disabled if run out of hints
            findViewById(R.id.btnHint).setEnabled(false);
            ((Button) findViewById(R.id.btnHint)).setText("Hint");
        } else {
            ((Button) findViewById(R.id.btnHint)).setText("Hint (" + numHints + ")");
        }
    }

    //MISC FUNCTIONS

    public int findProbBlock(String text) throws MissingBlockException{
        for(int i=0; i<probBlocks.length; i++){
            if(probBlocks[i].getText().equals(text)) {
                //we have found our block
                return i;
            }
        }
        throw new MissingBlockException("CRASH: Block not found");
    }

    public void disableBlock(TextView tv) {
        tv.setAlpha((float) 0.5);
        tv.setOnTouchListener(null);
    }

    public void enableBlock(TextView tv) {
        tv.setAlpha((float) 1);
        tv.setOnTouchListener(new BlockTouchListener());
    }

    private void resetBlock(String blocktext) {
        try {
            int oldblockIndex = findProbBlock(blocktext); //get the old blocks index
            enableBlock((TextView) findViewById(probBlocks[oldblockIndex].getOriginal())); //resets overridden block back to enabled
            probBlocks[oldblockIndex].setCurrent(probBlocks[oldblockIndex].getOriginal());
            probBlocks[oldblockIndex].setPrevious(probBlocks[oldblockIndex].getOriginal());

        } catch (MissingBlockException ex) {
            ex.printStackTrace();
            System.out.println("CRASH: Unable to reset block.");
        }
    }

    private void updateScore(int amount) {
        score += amount;
        if(score > MAX_SCORE) {
            score = MAX_SCORE;
        } else if(score < MIN_SCORE) {
            score = MIN_SCORE;
        }

        scores.add(problemIndex, score);
        ((TextView) findViewById(R.id.tvScore)).setText("Score: " + score);
    }

    //LISTENER FUNCTIONS

    private final class BlockTouchListener implements View.OnTouchListener {
        @RequiresApi(api = Build.VERSION_CODES.N)
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDragAndDrop(data, shadowBuilder, view, 0);
                return true;
            } else {
                return false;
            }
        }
    }

    private class SlotDragListener implements View.OnDragListener {
        public boolean onDrag(View target, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DROP:
                    //handle the dragged view being dropped over a drop view
                    TextView dropped = (TextView) event.getLocalState(); //this is the object that was dropped
                    String text = (String) dropped.getText(); //get the text of the dropped object
                    try {
                        probBlocks[findProbBlock(text)].updateCurrent(target.getId()); //set the current location of the block to current slot

                        if(((Slot) target).isEmpty()) {
                            ((Slot) target).setFull(text); //set the slot text to the new blocks text
                            target.setOnTouchListener(new BlockTouchListener());
                        } else {
                            resetBlock((String) ((Slot) target).getText());
                        }

                        boolean isBlock = false;
                        for(int j=0;j<8;j++) {
                            if(blocks[j].getId() == dropped.getId()){
                                isBlock = true;
                            }
                        }

                        if(isBlock) {
                            disableBlock(dropped);
                        } else {
                            ((Slot) dropped).setEmpty();
                        }

                        clickSoundMP.start();
                    } catch (ClassCastException ex) {
                        ex.printStackTrace();
                        System.out.println("CRASH: Attempted to cast a TextView to Slot. id of textview is: " + target.getId() + "\n" + ex.getMessage());
                    } catch (MissingBlockException ex) {
                        ex.printStackTrace();
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //no action necessary
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    private class BlockReturnDragListener implements View.OnDragListener {
        public boolean onDrag(View target, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DROP:
                    //handle the dragged view being dropped over a drop view
                    TextView dropped = (TextView) event.getLocalState(); //this is the object that was dropped
                    String text = (String) dropped.getText(); //get the text of the dropped object

                    try {
                        boolean isBlock = false;
                        for (TextView block : blocks) {
                            if (dropped.getId() == block.getId()) {
                                //previous was a block, do nothing
                                isBlock = true;
                            }
                        }

                        if(!isBlock) {
                            ((Slot) dropped).setEmpty(); //set previous to empty
                            resetBlock(text);
                            clickSoundMP.start();
                        }
                    } catch (ClassCastException ex) {
                        System.out.println("CRASH: Previous slot is somehow now type slot");
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //no action necessary
                    break;
                default:
                    break;
            }
            return true;
        }
    }
}