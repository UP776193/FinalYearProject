package com.example.FinalYearProject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.FinalYearProject.HomepageActivity.pl;
import static com.example.FinalYearProject.HomepageActivity.scores;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

@RequiresApi(api = Build.VERSION_CODES.M)
public class LevelSelect extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);
        populateLevels();
    }

    public void populateLevels() {
        LinearLayout vlayout = findViewById(R.id.vlLevels);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) LinearLayout.LayoutParams.WRAP_CONTENT, (int) LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 30, 0, 30);
        params.gravity = 1;
        for(int i = 0;i<pl.size();i++) {
            TextView tv = new TextView(this);
            tv.setTextAppearance(R.style.problemText);
            tv.setTextSize(18);
            tv.setText("Level " + (i+1) + " : " + scores.get(i) + "/100");
            tv.setLayoutParams(params);
            tv.setOnClickListener(new LevelSelectListener());
            vlayout.addView(tv);
        }
    }

    public void Back(View view) {
        Intent intent = new Intent(this, HomepageActivity.class);
        startActivity(intent);
        finish();
    }

    public void toLevel(int level) {
        Intent intent = new Intent(this, ProblemActivity.class);
        intent.putExtra("problemID",level);
        startActivity(intent);
        finish();
    }

    private class LevelSelectListener implements View.OnClickListener {
        public void onClick(View view) {
            TextView selected = (TextView) view;
            String[] text = selected.getText().toString().split(" ");
            int level = Integer.parseInt(text[1]) - 1;
            toLevel(level);
        }
    }

}

