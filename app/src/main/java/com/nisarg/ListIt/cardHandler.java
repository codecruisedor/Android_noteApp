package com.nisarg.ListIt;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

public class cardHandler {
    private String mTitle;
    private String mContent;
    private int c;
    private int id;
    private Context context;
    private String date;
    private ImageView mimageView;
    Activity activity;


    public cardHandler(ImageView imageView, Activity activity, Context context, String mTitle, String mContent, String date, int c, int id) {
        this.activity = activity;
        this.context = context;
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.c = c;
        this.id = id;
        this.date = date;
        this.mimageView = imageView;

    }

    public ImageView getMimageView() {
        return mimageView;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public int getC() {
        return c;
    }

    public String getDate() {
        return date;
    }

    public void setC(int c) {
        this.c = c;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
