package org.wmii.endorfit.DataClasses;

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

    public PlanItem(String name, String nameLabel, String firstCol, String firstColLabel, String secondCol, String secondColLabel) {
        this.name = name;
        this.nameLabel = nameLabel;
        this.firstCol = firstCol;
        this.firstColLabel = firstColLabel;
        this.secondCol = secondCol;
        this.secondColLabel = secondColLabel;
        this.enableDelete = true;
    }

    public PlanItem(String name, String nameLabel, String firstCol, String firstColLabel, String secondCol, String secondColLabel, boolean enableDelete) {
        this.name = name;
        this.nameLabel = nameLabel;
        this.firstCol = firstCol;
        this.firstColLabel = firstColLabel;
        this.secondCol = secondCol;
        this.secondColLabel = secondColLabel;
        this.enableDelete = enableDelete;
    }

   public PlanItem(String name, String nameLabel) {
        this.name = name;
        this.nameLabel = nameLabel;
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
        this.enableDelete = true;
    }

    public PlanItem(String name, String nameLabel, String firstCol, String firstColLabel, String secondCol, String secondColLabel, String thirdCol, String thirdColLabel, boolean enableDelete) {
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

    public  String getNameLabel() {
        return nameLabel;
    }

    public   String getFirstCol() {
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

    public boolean isEnableDelete() {
        return enableDelete;
    }
}
