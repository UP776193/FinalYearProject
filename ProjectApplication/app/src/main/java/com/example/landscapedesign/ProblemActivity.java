package com.example.landscapedesign;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;

import static java.security.AccessController.getContext;

public class ProblemActivity extends AppCompatActivity {

    TextView block1, block2, block3, block4, target;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
        target = findViewById(R.id.tvProblemWindow);
        String problem =
                "public void addTo25() {\n" +
                "\t\t\tint x = 0;\n" +
                "\t\t\tfor (int i = 0; i<4; i++) {\n" +
                "\t\t\t\t\t\tx += 5;\n" +
                "\t\t\t}\n" +
                "\t\t\treturn x;\n" +
                "}";
        target.setText(problem);

        block1 = findViewById(R.id.block1);
        block2 = findViewById(R.id.block2);
        block3 = findViewById(R.id.block3);
        block4 = findViewById(R.id.block4);

        block1.setOnLongClickListener(longclickListener);
        block2.setOnLongClickListener(longclickListener);
        block3.setOnLongClickListener(longclickListener);
        block4.setOnLongClickListener(longclickListener);

        target.setOnDragListener(dragListenre);
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

    public void skip(View view) {
        //found @https://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Skip?");
        builder.setMessage("Are you sure you want to skip this question?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                String message = "You have skipped me!";
                TextView problemText = findViewById(R.id.tvProblemWindow);
                problemText.setText(message);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String message = "You have NOT skipped me!";
                TextView problemText = findViewById(R.id.tvProblemWindow);
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

        if(target.getText() == "Block 1 has been dropped here") {
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
}