package com.example.landscapedesign;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.landscapedesign.Problem;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class problemReader {

    private ArrayList<Problem> problemList = new ArrayList<Problem>();
    private Context context;

    public problemReader(Context context) {
        this.context = context;
        loadAssets();
    }

    public ArrayList<Problem> getProblemList() {
        return problemList;
    }

    public void loadAssets() {
        try {
            AssetManager assetManager = context.getAssets();
            DataInputStream textFileStream = new DataInputStream(assetManager.open("text.txt"));
            Scanner scanner = new Scanner(textFileStream);

            //read problem description
            String problemDesc = scanner.nextLine();

            //read solution
            String[] solution = scanner.nextLine().split(",");

            //read blocks
            String[] blocks = scanner.nextLine().split(",");

            //read number of lines
            int numLines = Integer.parseInt(scanner.nextLine());

            String[][] problemLines = new String[numLines][];
            //read problem lines
            for(int i = 0; i < numLines; i++){
                problemLines[i] = scanner.nextLine().split(",");
            }

            Problem problem = new Problem(problemList.size(), problemDesc, problemLines, solution, blocks);
            problemList.add(problem);

        } catch (IOException ex) {
            return;
        }

    }



}
