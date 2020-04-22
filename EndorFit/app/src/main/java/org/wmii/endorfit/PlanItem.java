package org.wmii.endorfit;

public class PlanItem {
    public String name;
    public String nameLabel;
    public String firstCol;
    public String firstColLabel;
    public String secondCol;
    public String secondColLabel;
    public String thirdCol;
    public String thirdColLabel;

    public PlanItem(String name, String nameLabel, String firstCol, String firstColLabel, String secondCol, String secondColLabel) {
        this.name = name;
        this.nameLabel = nameLabel;
        this.firstCol = firstCol;
        this.firstColLabel = firstColLabel;
        this.secondCol = secondCol;
        this.secondColLabel = secondColLabel;
    }

    public PlanItem(String name, String nameLabel, String firstCol, String firstColLabel, String secondCol, String secondColLabel, String thirdCol, String thirdColLabel) {
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

    public String getNameLabel() {
        return nameLabel;
    }

    public String getFirstCol() {
        return firstCol;
    }

    public String getFirstColLabel() {
        return firstColLabel;
    }

    public String getSecondCol() {
        return secondCol;
    }

    public String getSecondColLabel() {
        return secondColLabel;
    }

    public String getThirdCol() {
        return thirdCol;
    }

    public String getThirdColLabel() {
        return thirdColLabel;
    }
}
