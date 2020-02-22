package com.example.FinalYearProject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class HomepageActivity extends AppCompatActivity {

    static ArrayList<Problem> pl;
    static ArrayList<Integer> scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        AssetReader ar = new AssetReader(this);
        pl = ar.getProblemList();
        scores = ar.getScoreList();
        int t = totalScore();
        TextView tvTotalScore = findViewById(R.id.tvTotalScore);
        tvTotalScore.setText(String.valueOf(t));
    }

    public void startGame(View view){
        Intent intent = new Intent(this, ProblemActivity.class);
        intent.putExtra("problemID",0);
        startActivity(intent);
        finish();
    }

    public void LevelSelect(View view) {
        Intent intent = new Intent(this, LevelSelect.class);
        startActivity(intent);
        finish();
    }

    private int totalScore() {
        int t = 0;
        for(Integer score: scores) {
            t += score;
        }
        return t;
    }
}
