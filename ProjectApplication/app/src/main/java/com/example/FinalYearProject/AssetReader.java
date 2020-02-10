package com.example.FinalYearProject;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class AssetReader {

    private ArrayList<Problem> problemList = new ArrayList<Problem>();
    private ArrayList scoreList = new ArrayList();
    private Context context;
    private String dir;

    public AssetReader(Context context) {
        this.context = context;
        dir = context.getFilesDir().getAbsolutePath();
        loadAssets();
    }

    public ArrayList<Problem> getProblemList() {
        return problemList;
    }

    public ArrayList getScoreList() {
        return scoreList;
    }

    public void loadAssets() {
        readProblems();
        readScores();
    }

    private void readProblems() {
        //Read Problems
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

    private void readScores() {
        //Read Scores
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(dir + File.separator + "scores.txt"));
            ObjectInputStream ois = new ObjectInputStream(fileInputStream);
            scoreList = (ArrayList) ois.readObject();
            ois.close();

            fileInputStream.close();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }


    }
}
