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
    private boolean enableDelete;

    PlanItem(String name, String nameLabel, String firstCol, String firstColLabel, String secondCol, String secondColLabel) {
        this.name = name;
        this.nameLabel = nameLabel;
        this.firstCol = firstCol;
        this.firstColLabel = firstColLabel;
        this.secondCol = secondCol;
        this.secondColLabel = secondColLabel;
        this.enableDelete = true;
    }

    PlanItem(String name, String nameLabel, String firstCol, String firstColLabel, String secondCol, String secondColLabel, boolean enableDelete) {
        this.name = name;
        this.nameLabel = nameLabel;
        this.firstCol = firstCol;
        this.firstColLabel = firstColLabel;
        this.secondCol = secondCol;
        this.secondColLabel = secondColLabel;
        this.enableDelete = enableDelete;
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
        this.enableDelete = true;
    }

    PlanItem(String name, String nameLabel, String firstCol, String firstColLabel, String secondCol, String secondColLabel, String thirdCol, String thirdColLabel, boolean enableDelete) {
        this.name = name;
        this.nameLabel = nameLabel;
        this.firstCol = firstCol;
        this.firstColLabel = firstColLabel;
        this.secondCol = secondCol;
        this.secondColLabel = secondColLabel;
        this.thirdCol = thirdCol;
        this.thirdColLabel = thirdColLabel;
        this.enableDelete = enableDelete;
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

    public boolean isEnableDelete() {
        return enableDelete;
    }
}
