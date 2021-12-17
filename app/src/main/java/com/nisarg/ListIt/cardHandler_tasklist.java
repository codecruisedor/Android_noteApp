package com.nisarg.ListIt;

import com.google.android.material.checkbox.MaterialCheckBox;

public class cardHandler_tasklist {

    private String task;
    private String priority;
    private int id;
    private final MaterialCheckBox checkBox;
    private String date;

    public cardHandler_tasklist(MaterialCheckBox materialCheckBox, int id, String task, String priority, String date) {
        this.id = id;
        this.task = task;
        this.priority = priority;
        this.checkBox = materialCheckBox;
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public MaterialCheckBox getCheckBox() {
        return this.checkBox;
    }


}


