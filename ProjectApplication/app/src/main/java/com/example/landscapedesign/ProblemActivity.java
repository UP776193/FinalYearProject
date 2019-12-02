package com.example.landscapedesign;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
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

    Problem problem;
    LinearLayout vlayout;
    TextView[] blocks; //Used to keep track of movable blocks in activity
    ArrayList<TextView> slots;
    Block[] probBlocks; //Used to keep track of where blocks are

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        vlayout = (LinearLayout) findViewById(R.id.vlayout);

        blocks = new TextView[8];
        slots = new ArrayList<TextView>();

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

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private TextView createTextView(String text) {
        TextView tv = new TextView(this);
        tv.setId((int) System.currentTimeMillis());
        tv.setText(text);
        tv.setPadding(20,20,20,20);
        tv.setTextAppearance(R.style.problemText);
        if(text.contains("______")){
            tv.setText("      ");
            tv.setBackgroundResource(R.color.colorSlots);
            tv.setOnDragListener(new SlotDragListener());
        }
        return tv;
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
        builder.setTitle("Drag the blocks into their correct position on the algorithm. Expected Output from x = 25");

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
            builder.setMessage("com.example.landscapedesign.Block 1 is the correct answer");
            builder.setNeutralButton("Next Question", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            builder.setTitle("INCORRECT");
            builder.setMessage("The dragged block is incorrect");
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
        return true;
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
                TextView text = createTextView(problemLines[i][j]);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) LinearLayout.LayoutParams.WRAP_CONTENT, (int) LinearLayout.LayoutParams.WRAP_CONTENT);
                text.setLayoutParams(params);
                text.setText(problemLines[i][j]);

                hlayout.addView(text);
                System.out.println(String.format("Setup: Added \"%s\" line.", (String) text.getText()));
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
        public boolean onDrag(View v, DragEvent event) {
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
                    TextView view = (TextView) event.getLocalState();
                    String text = (String) view.getText();

                    int index = findProbBlock(text); //find the problem block this text matches to
                    System.out.println("Blocks: index is " + index);

                    probBlocks[index].setPrevious(probBlocks[index].getCurrent());
                    probBlocks[index].setCurrent(v.getId()); //set current to this slots id

                    TextView original = findViewById(probBlocks[index].getOriginal());
                    original.setVisibility(View.INVISIBLE);

                    TextView current = findViewById(probBlocks[index].getCurrent());

                    if(current.getText() == "______") {
                        System.out.println("Blocks: current used to be an empty slot");
                        //do nothing
                    } else {
                        System.out.println("Blocks: current used to be an full slot");
                        int iindex = findProbBlock((String) current.getText());
                        findViewById(probBlocks[iindex].getOriginal()).setVisibility(View.VISIBLE);
                        probBlocks[iindex].setCurrent(probBlocks[iindex].getOriginal());
                        probBlocks[iindex].setPrevious(probBlocks[iindex].getOriginal());
                    }

                    current.setText(probBlocks[index].getText());
                    current.setOnTouchListener(new BlockTouchListener());

                    TextView previous = findViewById(probBlocks[index].getPrevious());
                    boolean isBlock = false;

                    for(int j=0;j<8;j++) {
                        if(blocks[j].getId() == previous.getId()){
                            isBlock = true;
                        }
                    }

                    if(isBlock) {
                        previous.setVisibility(View.INVISIBLE);
                        System.out.println("Blocks: previous used to be a block");
                    } else {
                        previous.setText("______");
                        previous.setOnTouchListener(null);
                        System.out.println("Blocks: previous used to be a slot");
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