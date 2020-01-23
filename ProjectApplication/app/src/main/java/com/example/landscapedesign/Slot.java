package com.example.landscapedesign;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class Slot extends android.widget.TextView {

    private boolean isEmpty;
    private String currentText;

    public Slot (Context context) {
        super(context);
        this.isEmpty = true;
        this.currentText = "______";
    }

    public boolean isEmpty() {
        return this.isEmpty;
    }

    public void setEmpty() {
        this.isEmpty = true;
        this.currentText = "______";
        this.setText(this.currentText);
        this.setOnTouchListener(null);
    }

    public void setFull() {
        this.isEmpty = false;
    }

    public String getCurrentText() {
        return currentText;
    }

    public void setCurrentText(String currentText) {
        this.currentText = currentText;
    }

    public void resetCurrentText() {
        this.currentText = "______";
    }


}
