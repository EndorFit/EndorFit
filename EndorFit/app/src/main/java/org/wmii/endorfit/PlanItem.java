package org.wmii.endorfit;

public class PlanItem {
    public String name;
    private String nameLabel;
    private String firstCol;
    private String firstColLabel;
    private String secondCol;
    private String secondColLabel;
    private String thirdCol;
    private String thirdColLabel;

    PlanItem(String name, String nameLabel, String firstCol, String firstColLabel, String secondCol, String secondColLabel) {
        this.name = name;
        this.nameLabel = nameLabel;
        this.firstCol = firstCol;
        this.firstColLabel = firstColLabel;
        this.secondCol = secondCol;
        this.secondColLabel = secondColLabel;
    }

    PlanItem(String name, String nameLabel, String firstCol, String firstColLabel, String secondCol, String secondColLabel, String thirdCol, String thirdColLabel) {
        this.name = name;
        this.nameLabel = nameLabel;
        this.firstCol = firstCol;
        this.firstColLabel = firstColLabel;
        this.secondCol = secondCol;
        this.secondColLabel = secondColLabel;
        this.thirdCol = thirdCol;
        this.thirdColLabel = thirdColLabel;
    }


    public String getName() {
        return name;
    }

    String getNameLabel() {
        return nameLabel;
    }

    String getFirstCol() {
        return firstCol;
    }

    String getFirstColLabel() {
        return firstColLabel;
    }

    String getSecondCol() {
        return secondCol;
    }

    String getSecondColLabel() {
        return secondColLabel;
    }

    String getThirdCol() {
        return thirdCol;
    }

    String getThirdColLabel() {
        return thirdColLabel;
    }
}
