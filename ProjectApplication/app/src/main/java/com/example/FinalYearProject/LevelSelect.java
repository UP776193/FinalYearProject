package com.example.FinalYearProject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.FinalYearProject.HomepageActivity.pl;
import static com.example.FinalYearProject.HomepageActivity.scores;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
            //String text = "Level " + (i+1) + " : " + scores.get(i) + "/100";
            Spannable span = constructSpannable("Level " + (i+1) + " : ", scores.get(i));
            tv.setText(span);
            tv.setLayoutParams(params);
            tv.setOnClickListener(new LevelSelectListener());
            vlayout.addView(tv);
        }
    }

    private Spannable constructSpannable(String header, int score) {
        //header example - Level 1:
        String text = header + score + "/100 ";
        if(score == 100) {
            text += getResources().getString(R.string.star);
        }
        Spannable toSpan = new SpannableString(text);
        toSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.problemText)),0 ,header.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //Setting up colour for "Level 1: "
        toSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.problemText)),header.length() + String.valueOf(score).length()+1, text.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        switch (score) {
            case 100:
                toSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.correctText)),header.length(), header.length() + String.valueOf(score).length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //Setting up colour for score
                toSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.goldstar)),text.length() - 1, text.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case 0:
                toSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBlocks)),header.length(), header.length() + String.valueOf(score).length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //Setting up colour for score
                break;
            default:
                toSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.score)),header.length(), header.length() + String.valueOf(score).length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //Setting up colour for score
                break;
        }
         //Setting up colour the end of the string
        return toSpan;
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

