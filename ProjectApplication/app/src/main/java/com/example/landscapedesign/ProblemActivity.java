package com.example.landscapedesign;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ProblemActivity extends AppCompatActivity {

    Problem problem;
    Block block;
    RelativeLayout rl;
    TextView block1, block2, block3, block4, block5, block6, block7, block8, slot1, slot2, slot3;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        rl = (RelativeLayout) findViewById(R.id.rlayout);

        block1 = findViewById(R.id.block1);
        block2 = findViewById(R.id.block2);
        block3 = findViewById(R.id.block3);
        block4 = findViewById(R.id.block4);
        block5 = findViewById(R.id.block5);
        block6 = findViewById(R.id.block6);
        block7 = findViewById(R.id.block7);
        block8 = findViewById(R.id.block8);

        block1.setOnTouchListener(new BlockTouchListener());
        block2.setOnTouchListener(new BlockTouchListener());
        block3.setOnTouchListener(new BlockTouchListener());
        block4.setOnTouchListener(new BlockTouchListener());
        block5.setOnTouchListener(new BlockTouchListener());
        block6.setOnTouchListener(new BlockTouchListener());
        block7.setOnTouchListener(new BlockTouchListener());
        block8.setOnTouchListener(new BlockTouchListener());


        problem = new Problem();
        printProblemLines();

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

        View recentView = null;

        for(int i = 0; i<problemLines.length;i++) {

            TextView tv = createTextView(problemLines[i][0]); //Create new text view
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT); //set to wrap content
            if(recentView == null) {
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else {
                params.addRule(RelativeLayout.BELOW, recentView.getId()); //position underneath last object
            }
            tv.setLayoutParams(params);
            View recentListView = tv;
            rl.addView(tv);

            for(int j = 1; j<problemLines[i].length;j++) {

                TextView tvl = createTextView(problemLines[i][j]); //Create new text view
                params = new RelativeLayout.LayoutParams((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT); //set to wrap content
                if(recentView == null) {
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                } else {
                    params.addRule(RelativeLayout.BELOW, recentView.getId()); //position underneath last object
                }
                params.addRule(RelativeLayout.RIGHT_OF, recentListView.getId()); //position underneath last object
                tvl.setLayoutParams(params);
                recentListView = tvl;
                rl.addView(tvl);
            }
            recentView = tv;
        }
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
                    View view = (View) event.getLocalState();
                    view.setVisibility(View.INVISIBLE);
                    TextView dropTarget = (TextView) v; //gets slot
                    TextView dropped = (TextView) view; //gets origin of block
                    dropTarget.setText(dropped.getText());
                    Object tag = dropTarget.getTag();
                    if (tag != null) {
                        int existingID = (Integer) tag;
                        findViewById(existingID).setVisibility(View.VISIBLE);
                    }
                    dropTarget.setTag(dropped.getId());
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