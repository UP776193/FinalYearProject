package com.example.FinalYearProject;

import android.content.Context;
import android.content.res.AssetManager;

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
            DataInputStream textFileStream = new DataInputStream(assetManager.open("problems.txt"));
            Scanner scanner = new Scanner(textFileStream);
            Problem problem;
            while(scanner.hasNextLine()) {
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

                problem = new Problem(problemList.size(), problemDesc, problemLines, solution, blocks);
                problemList.add(problem);
                System.out.println("Setup: Added problem " + (problemList.size() - 1));
            }
        } catch (IOException ex) {
            System.out.println("CRASH: System failed to read text input.");
            return;
        } catch (Exception ex) {
            System.out.println("CRASH: " + ex.getMessage());
            ex.printStackTrace();
        }
        System.out.println("Setup: number of problems" + problemList.size());

    }
}
