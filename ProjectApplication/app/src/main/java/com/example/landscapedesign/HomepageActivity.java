package com.example.landscapedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class HomepageActivity extends AppCompatActivity {

    static ArrayList<Problem> pl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        problemReader pr = new problemReader(this);
        pl = pr.getProblemList();

    }

    public void startGame(View view){
        Intent intent = new Intent(this, ProblemActivity.class);
        intent.putExtra("problemID",0);
        startActivity(intent);
    }

}
