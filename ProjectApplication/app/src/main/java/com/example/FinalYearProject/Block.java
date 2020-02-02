package com.example.FinalYearProject;

public class Block {

    private int current;
    private int original;
    private int previous;
    private String text;


    public Block(int current, int original, int previous, String text){
        this.current = current;
        this.original = original;
        this.previous = previous;
        this.text = text;
    }

    public Block(int blockID, String text){
        this.current = blockID;
        this.original = blockID;
        this.previous = blockID;
        this.text = text;
    }

    public Block(String text){
        this.text = text;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getOriginal() {
        return original;
    }

    public void setOriginal(int original) {
        this.original = original;
    }

    public int getPrevious() {
        return previous;
    }

    public void setPrevious(int previous) {
        this.previous = previous;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
