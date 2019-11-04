package com.example.landscapedesign;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;

public class MainProblemActivity extends AppCompatActivity {

    TextView block1, block2, block3, block4, target;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.problemlayout);
        target = findViewById(R.id.tvProblemWindow);
        String problem =
                "public void addTo25() {\n" +
                "     int x = 0;\n" +
                "     for (int i = 0; i<4; i++) {\n" +
                "           x += 5;\n" +
                "     }\n" +
                "     return x;\n" +
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
            View.DragShadowBuilder myShadwoBuilder = new View.DragShadowBuilder(v);
            v.startDragAndDrop(data, myShadwoBuilder, v, 0);
            return true;
        }
    };

    View.OnDragListener dragListenre = new View.OnDragListener(){

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();

            switch(dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    final View view = (View) event.getLocalState();
                    if (view.getId() == R.id.block1){
                        target.setText("Block 1 has been dragged here");
                    }
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
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
}