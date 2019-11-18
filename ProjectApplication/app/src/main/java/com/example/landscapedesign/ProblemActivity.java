package com.example.landscapedesign;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class ProblemActivity extends AppCompatActivity {

    TextView block1, block2, block3, block4, block5, block6, block7, block8, slot1, slot2, slot3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        block1 = findViewById(R.id.block1);
        block2 = findViewById(R.id.block2);
        block3 = findViewById(R.id.block3);
        block4 = findViewById(R.id.block4);
        block5 = findViewById(R.id.block5);
        block6 = findViewById(R.id.block6);
        block7 = findViewById(R.id.block7);
        block8 = findViewById(R.id.block8);

        slot1 = findViewById(R.id.slot1);
        slot2 = findViewById(R.id.slot2);
        slot3 = findViewById(R.id.slot3);

        //block1.setOnLongClickListener(longclickListener);
        //block2.setOnLongClickListener(longclickListener);
        //block3.setOnLongClickListener(longclickListener);
        //block4.setOnLongClickListener(longclickListener);
        //block5.setOnLongClickListener(longclickListener);
        //block6.setOnLongClickListener(longclickListener);
        //block7.setOnLongClickListener(longclickListener);
        //block8.setOnLongClickListener(longclickListener);

        block1.setOnTouchListener(new BlockTouchListener());
        block2.setOnTouchListener(new BlockTouchListener());
        block3.setOnTouchListener(new BlockTouchListener());
        block4.setOnTouchListener(new BlockTouchListener());
        block5.setOnTouchListener(new BlockTouchListener());
        block6.setOnTouchListener(new BlockTouchListener());
        block7.setOnTouchListener(new BlockTouchListener());
        block8.setOnTouchListener(new BlockTouchListener());

        slot1.setOnDragListener(new SlotDragListener());
        slot2.setOnDragListener(new SlotDragListener());
        slot3.setOnDragListener(new SlotDragListener());

        //target.setOnDragListener(dragListenre);
    }

    private final class BlockTouchListener implements View.OnTouchListener {

        @RequiresApi(api = Build.VERSION_CODES.N)
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
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
                    TextView dropTarget = (TextView) v;
                    TextView dropped = (TextView) view;
                    dropTarget.setText(dropped.getText());
                    Object tag = dropTarget.getTag();
                    if(tag != null) {
                        int existingID = (Integer)tag;
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


    View.OnLongClickListener longclickListener = new View.OnLongClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("","");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
            v.startDragAndDrop(data, myShadowBuilder, v, 0);
            return true;
        }
    };

    /*
    View.OnDragListener dragListenre = new View.OnDragListener(){

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();

            switch(dragEvent) {
                case DragEvent.ACTION_DRAG_STARTED:
                    target.setText("A block is being dragged");
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    final View view = (View) event.getLocalState();
                    if (view.getId() == R.id.block1){
                        target.setText("Block 1 has been dropped here");
                    } else if (view.getId() == R.id.block2) {
                        target.setText("Block 2 has been dropped here");
                    } else if (view.getId() == R.id.block3) {
                        target.setText("Block 3 has been dropped here");
                    } else if (view.getId() == R.id.block4) {
                        target.setText("Block 4 has been dropped here");
                    }
                    break;
            }
            return true;
        }
    };

     */

    public void skip(View view) {
        //found @https://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Skip?");
        builder.setMessage("Are you sure you want to skip this question?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                String message = "You have skipped me!";
                TextView problemText = findViewById(R.id.slot1);
                problemText.setText(message);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String message = "You have NOT skipped me!";
                TextView problemText = findViewById(R.id.slot1);
                problemText.setText(message);
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

        if(checkCorrect()) {
            builder.setTitle("CORRECT");
            builder.setMessage("Block 1 is the correct answer");
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
}