package com.example.landscapedesign;

public class Block {

    private int current;
    private int original;
    private int previous;
    private String text;
    private int ID;


    public Block(int current, int original, int previous, String text, int ID) {
        this.current = current;
        this.original = original;
        this.previous = previous;
        this.text = text;
        this.ID = ID;
    }

    public Block(int blockID, String text, int ID){
        this.current = blockID;
        this.original = blockID;
        this.previous = blockID;
        this.text = text;
        this.ID = ID;
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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
