package com.example.FinalYearProject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class HomepageActivity extends AppCompatActivity {

    static ArrayList<Problem> pl;
    static ArrayList scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        AssetReader ar = new AssetReader(this);
        pl = ar.getProblemList();
        scores = ar.getScoreList();
    }

    public void startGame(View view){
        Intent intent = new Intent(this, ProblemActivity.class);
        intent.putExtra("problemID",0);
        startActivity(intent);
    }

}
