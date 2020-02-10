package com.example.FinalYearProject;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import static com.example.FinalYearProject.HomepageActivity.scores;

public class AssetWriter {

    private Context context;

    public AssetWriter(Context context){
        this.context = context;
    }

    public void writeScores() {
        File dir = new File(context.getFilesDir().getAbsolutePath() + File.separator
                + "scores.txt");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(dir));
            out.writeObject(scores);
            out.close();
        }  catch(FileNotFoundException ex) {
            ex.printStackTrace();
        }  catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
