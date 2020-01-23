package com.example.landscapedesign;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.SyncFailedException;
import java.util.ArrayList;

public class ProblemActivity extends AppCompatActivity {

    private Problem problem;
    private LinearLayout vlayout;
    private TextView[] blocks; //Used to keep track of movable blocks in activity
    private ArrayList<Slot> slots;
    private Block[] probBlocks; //Used to keep track of where blocks are
    private MediaPlayer clickSoundMP;
    private TextView blockReturn;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        vlayout = (LinearLayout) findViewById(R.id.vlayout);

        blocks = new TextView[8];
        slots = new ArrayList<Slot>();

        //Set up blocks
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
        System.out.println(String.format("Setup: Activity Blocks created."));


        problem = new Problem();
        probBlocks = problem.getBlocks();
        printProblemLines();
        setupBlocks();

        blockReturn = findViewById(R.id.blockreturn);
        blockReturn.setOnDragListener(new BlockReturnDragListener());

        clickSoundMP = MediaPlayer.create(this, R.raw.click);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private TextView createTextView(String text) {
        TextView tv = new TextView(this);
        tv.setId((int) System.currentTimeMillis());
        tv.setText(text);
        tv.setPadding(20,20,20,40);
        tv.setTextAppearance(R.style.problemText);
        return tv;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Slot createSlot() {
        Slot slot = new Slot(this);
        slot.setId((int) System.currentTimeMillis() * 2);
        slot.setPadding(20,20,20,40);
        slot.setTextAppearance(R.style.problemText);
        slot.setBackgroundResource(R.color.colorSlots);
        slot.setOnDragListener(new SlotDragListener());
        slots.add(slot);
        return slot;
    }

    public void skip(View view) {
        //found @https://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Skip?");
        builder.setMessage("Are you sure you want to skip this question?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
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

    public void viewProblem(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
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

    public void submit(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (checkCorrect()) {
            builder.setTitle("CORRECT");
            builder.setMessage("Well Done, that's correct!");
            builder.setNeutralButton("Next Question", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            builder.setTitle("INCORRECT");
            builder.setMessage("There appears to be a block in the wrong place.");
            builder.setNeutralButton("Try ", new DialogInterface.OnClickListener() {
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

            if(slots.get(i).getText() != solution[i]) {
                correct = false;
            }

        }
        return correct;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void printProblemLines() {
        String[][] problemLines = problem.getProbLines();

        for(int i =0;i<problemLines.length;i++) {

            LinearLayout hlayout = new LinearLayout(this);
            hlayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            hlayout.setOrientation(LinearLayout.HORIZONTAL);

            vlayout.addView(hlayout);

            for(int j = 0;j<problemLines[i].length;j++) {
                String t = problemLines[i][j];
                if(t == "______") {
                    //Create a slot here
                    Slot text = createSlot();
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) LinearLayout.LayoutParams.WRAP_CONTENT, (int) LinearLayout.LayoutParams.WRAP_CONTENT);
                    text.setLayoutParams(params);
                    text.setText(problemLines[i][j]);

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

    public void setupBlocks() {

       for(int i = 0; i < 8; i++){
           //fill blocks with text
           blocks[i].setVisibility(View.VISIBLE);
           if(i >=  probBlocks.length) {
               blocks[i].setVisibility(View.INVISIBLE);
               System.out.println(String.format("Setup: Set Block %d to invisible.", i));
           } else {
               blocks[i].setText(probBlocks[i].getText());
               probBlocks[i].setOriginal(blocks[i].getId());
               probBlocks[i].setCurrent(blocks[i].getId());
               probBlocks[i].setPrevious(blocks[i].getId());
               System.out.println(String.format("Setup: Added \"%s\" block.", (String) probBlocks[i].getText()));
           }
       }
        System.out.println(String.format("Setup: Activity Blocks setup complete."));
    }

    public int findProbBlock(String text) {
        int index = -1;
        for(int i=0; i<probBlocks.length; i++){
            if(probBlocks[i].getText() == text) {
                //we have found our block
                index = i;
            }
        }
        return index;
    }

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
                        int blockIndex = findProbBlock(text); //find the problem block this text matches to
                        probBlocks[blockIndex].setPrevious(probBlocks[blockIndex].getCurrent()); //set the previous location of the block to its current before updating current
                        probBlocks[blockIndex].setCurrent(target.getId()); //set the current location of the block to current slot

                        if(((Slot) target).isEmpty()) {
                            //set full
                            //set text
                        } else {
                            TextView original = findViewById(probBlocks[blockIndex].getOriginal()); //find the original location of the targets previous block before overriding
                            String oldtext = (String) ((Slot) target).getText(); //get the old blocks text
                            int oldblockIndex = findProbBlock(oldtext); //get the old blocks index
                            enableTextView((TextView) findViewById(probBlocks[oldblockIndex].getOriginal())); //resets overridden block back to enabled
                            probBlocks[oldblockIndex].setCurrent(probBlocks[oldblockIndex].getOriginal());
                            probBlocks[oldblockIndex].setPrevious(probBlocks[oldblockIndex].getOriginal());
                        }

                        boolean isBlock = false;
                        for(int j=0;j<8;j++) {
                            if(blocks[j].getId() == dropped.getId()){
                                isBlock = true;
                            }
                        }

                        if(isBlock) {
                            disableTextView(dropped);
                            System.out.println("Blocks: previous used to be a block");
                        } else {
                            ((Slot) dropped).setEmpty();
                            System.out.println("Blocks: previous used to be a slot");
                        }

                        ((Slot) target).setText(text); //set the slot text to the new blocks text
                        ((Slot) target).setFull();
                        target.setOnTouchListener(new BlockTouchListener());
                        System.out.println("Dropped: set target text to " + text);

                        clickSoundMP.start();

                    } catch (ClassCastException ex) {
                        System.out.println("CRASH: Attempted to cast a TextView to Slot. id of textview is: " + target.getId() + "\n" + ex.getMessage());
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

    public void disableTextView (TextView tv) {
        tv.setAlpha((float) 0.5);
        tv.setOnTouchListener(null);
    }

    public void enableTextView (TextView tv) {
        tv.setAlpha((float) 1);
        tv.setOnTouchListener(new BlockTouchListener());
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
                        for(int i = 0; i<blocks.length; i++) {
                            if(dropped.getId() == blocks[i].getId()) {
                                //previous was a block, do nothing
                                isBlock = true;
                            }
                        }

                        if(isBlock) {
                            //do nothing
                        } else {
                            ((Slot) dropped).setEmpty(); //set previous to empty
                            int blockIndex = findProbBlock(text); //find the problem block this text matches to
                            enableTextView((TextView) findViewById(probBlocks[blockIndex].getOriginal())); //resets overridden block back to enabled
                            probBlocks[blockIndex].setCurrent(probBlocks[blockIndex].getOriginal());
                            probBlocks[blockIndex].setPrevious(probBlocks[blockIndex].getOriginal());

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