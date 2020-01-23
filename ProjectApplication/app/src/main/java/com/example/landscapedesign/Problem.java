package com.example.landscapedesign;

import java.util.ArrayList;

public class Problem {

    private int problemID;
    private Block[] blocks = new Block[8];
    private String probDesc;
    private String[][] probLines;
    private String[] solution;

    public Problem(int problemID, String probDesc, String[][] probLines, String[] solution) {
        this.problemID = problemID;
        this.probDesc = probDesc;
        this.probLines = probLines;
        this.solution = solution;
        this.blocks = new Block[8];
    }

    public Problem() {
        defaultProblem();
    }

    public void defaultProblem() {
        this.problemID = 999;
        this.probDesc = "Drag Blocks into their correct position on the algorithm. Expected output from x = 25";
        this.probLines = new String[][]{
                {"public int addTo25() {"},
                {"  ","______","x = 0;"},
                {"  for (int i = 0;i<5;i","______",") {"},
                {"      x += ","______",";"},
                {"  }"},
                {"  return","______",";"},
                {"}"}
        };
        String[] blockTexts = new String[] {
                "x","5","--","int","String","1", "++", "Boolean"
        };

        for(int i=0;i<8;i++) {
            blocks[i] = new Block(blockTexts[i]);
        }

        this.solution = new String []{
                "int","++","5","x"
        };
    }

    public Block[] getBlocks() {
        return blocks;
    }

    public void setBlocks(Block block, int index) {
        this.blocks[index] = block;
    }

    public String getProbDesc() {
        return probDesc;
    }

    public void setProbDesc(String probDesc) {
        this.probDesc = probDesc;
    }

    public String[][] getProbLines() {
        return probLines;
    }

    public void setProbLines(String[][] probLines) {
        this.probLines = probLines;
    }

    public String[] getSolution() {
        return solution;
    }

    public void setSolution(String[] solution) {
        this.solution = solution;
    }

    public int getProblemID() {
        return problemID;
    }

    public void setProblemID(int problemID) {
        this.problemID = problemID;
    }
}
