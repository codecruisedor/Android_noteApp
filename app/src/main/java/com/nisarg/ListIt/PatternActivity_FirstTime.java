package com.nisarg.ListIt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class PatternActivity_FirstTime extends AppCompatActivity {

    private PatternLockView mPatternLockView;
    private PatternLockViewListener mPatternLockViewListener;
    private static boolean done_once;
    private String pattern1 = "", pattern2 = "";
    private TextView textView_for_lock;
    private String title, content;
    private DatabaseHelperForNotes databaseHelperForNotes;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pattern);
        mPatternLockView = findViewById(R.id.pattern_lock_view);
        textView_for_lock = findViewById(R.id.textview_for_lock);
        done_once = false;
        databaseHelperForNotes = new DatabaseHelperForNotes(PatternActivity_FirstTime.this);

        getIntentData();
        Context context = PatternActivity_FirstTime.this;

        SharedPreferences sharedPref_first_time_lock = context.getSharedPreferences(getString(R.string.first_time_set_lock), MODE_PRIVATE);
        SharedPreferences sharedPref_Pattern = context.getSharedPreferences(getString(R.string.pattern), MODE_PRIVATE);


        SharedPreferences.Editor editorForFirstTimeLock = sharedPref_first_time_lock.edit();
        SharedPreferences.Editor editorForPattern = sharedPref_Pattern.edit();


        mPatternLockViewListener = new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                //ask the pattern twice to confirm it

                if (!done_once) {

                    pattern1 = PatternLockUtils.patternToString(mPatternLockView, pattern);
                    textView_for_lock.setText("Enter once more");
                    done_once = true;

                } else {
                    pattern2 = PatternLockUtils.patternToString(mPatternLockView, pattern);
                    if (pattern1.equalsIgnoreCase(pattern2)) {

                        mPatternLockView.setCorrectStateColor(getResources().getColor(R.color.design_default_color_secondary));
                        showToast("Successs...");

                        editorForPattern.putString(getString(R.string.pattern), pattern1);
                        editorForFirstTimeLock.putString(getString(R.string.first_time_set_lock), "not null");
                        editorForPattern.apply();
                        editorForFirstTimeLock.apply();

                        Intent intent_ForDisplayLockedNotes = new Intent(PatternActivity_FirstTime.this, Display_locked_notes.class);
                        startActivity(intent_ForDisplayLockedNotes);

                    } else {
                        textView_for_lock.setText("Set security code:");
                        done_once = false;
                    }
                }
            }

            @Override
            public void onCleared() {

            }
        };
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);
    }

    public void showToast(String str) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toastl_layout_custom, (ViewGroup) findViewById(R.id.toast_root));
        TextView toastText = layout.findViewById(R.id.toast_text);
        toastText.setText(str);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private void insertDataIntoDatabase(boolean flag) {

        String date = getDateTime();
        if (flag) {
            databaseHelperForNotes.delete(id);
        }
        databaseHelperForNotes.insertData_for_locked(title, content, date);

    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void getIntentData() {
        boolean flag = false;
        if (getIntent().hasExtra("Title") && getIntent().hasExtra("Note")) {
            content = getIntent().getStringExtra("Note");
            title = getIntent().getStringExtra("Title");
            if (getIntent().hasExtra("id")) {
                id = getIntent().getIntExtra("id", 0);
                flag = true;
            }
            insertDataIntoDatabase(flag);
        }
    }

}
