package com.nisarg.ListIt;

public class cardHanlder_shoppingCard {

    public String mItemName;
    public int mQuant;
    public String mSize;
    public double mPrice;
    public int mcolor;
    public int id;

    public cardHanlder_shoppingCard(String mItemName, int mQuant, double mPrice, String mSize, int color, int id) {
        this.mItemName = mItemName;
        this.mQuant = mQuant;
        this.mPrice = mPrice;
        this.mSize = mSize;
        this.mcolor = color;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMcolor() {
        return mcolor;
    }

    public void setMcolor(int mcolor) {
        this.mcolor = mcolor;
    }

    public String getmSize() {
        return mSize;
    }

    public void setmSize(String mSize) {
        this.mSize = mSize;
    }

    public String getmItemName() {
        return mItemName;
    }

    public void setmItemName(String mItemName) {
        this.mItemName = mItemName;
    }

    public String getmQuant() {
        return String.valueOf(mQuant);
    }

    public void setmQuant(int mQuant) {
        this.mQuant = mQuant;
    }

    public String getmPrice() {
        return String.valueOf(mPrice);
    }

    public void setmPrice(double mPrice) {
        this.mPrice = mPrice;
    }
}
