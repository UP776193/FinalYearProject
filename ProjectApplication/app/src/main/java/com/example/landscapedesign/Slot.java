package com.example.landscapedesign;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

@SuppressLint("AppCompatCustomView")
public class Slot extends android.widget.TextView {

    private boolean isEmpty;
    private String currentText;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Slot (Context context) {
        super(context);
        this.isEmpty = true;
        this.currentText = getResources().getString(R.string.slotcode);
        this.setId((int) System.currentTimeMillis() * 2);
        this.setDefaultPadding();
        this.setTextAppearance(R.style.problemText);
        this.setBackgroundResource(R.color.colorSlots);
    }

    public void calcNewPadding() {
        if(this.currentText.length() < 3) {
            this.setPadding(40,20,40,40);
        } else {
            this.setDefaultPadding();
        }
    }

    public void setDefaultPadding() {
        this.setPadding(20, 20, 20, 40);
    }

    public boolean isEmpty() {
        return this.isEmpty;
    }

    public void setEmpty() {
        this.isEmpty = true;
        resetCurrentText();
        this.setText(this.currentText);
        this.setOnTouchListener(null);
    }

    public void setFull(String text) {
        this.isEmpty = false;
        setCurrentText(text);
    }

    public String getCurrentText() {
        return currentText;
    }

    public void setCurrentText(String currentText) {
        this.currentText = currentText;
        calcNewPadding();
        this.setText(this.currentText);
    }

    public void resetCurrentText() {
        this.currentText = getResources().getString(R.string.slotcode);
    }


}
