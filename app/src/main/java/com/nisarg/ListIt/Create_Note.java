package com.nisarg.ListIt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Create_Note extends AppCompatActivity {

    EditText note, title;
    DatabaseHelperForNotes databaseHelperForNotes;
    Toolbar toolbar;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create__note);

        constraintLayout = findViewById(R.id.main_view_create_note);
        databaseHelperForNotes = new DatabaseHelperForNotes(Create_Note.this);
        toolbar = findViewById(R.id.createNewNote_toolbar);
        note = findViewById(R.id.content_note);
        title = findViewById(R.id.Title);
        setSupportActionBar(toolbar);
        prepareActionBar();


        note.setCursorVisible(true);
        note.requestFocus();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String multiline_note;
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //showToast("Discarded...");
            Intent intent = new Intent(Create_Note.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.lock) {
            LockNote(title.getText().toString(), note.getText().toString());
        } else if (id == R.id.done) {
            if (note.getLineCount() > 1) {
                float chars = note.getWidth() / (note.getText().length() * note.getTextSize());
                multiline_note = note.getText().toString();
                multiline_note = multiline_note.substring(0, (int) chars);
            } else {
                multiline_note = note.getText().toString();
            }
            DoneNote(title.getText().toString(), note.getText().toString());
        }
        return super.onOptionsItemSelected(item);
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

    public void prepareActionBar() {

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    private void DoneNote(String title, String note) {
        checkForEmptyConditionsAndProceedForNormalNote(title, note);
    }

    private void LockNote(String title, String note) {


        Context context = Create_Note.this;
        SharedPreferences sharedPref_first_time_lock = context.getSharedPreferences(
                getString(R.string.first_time_set_lock), Context.MODE_PRIVATE);

        //SharedPreferences.Editor editor = sharedPref_first_time_lock.edit();
        if ("null".equalsIgnoreCase(sharedPref_first_time_lock.getString(getString(R.string.first_time_set_lock), "null"))) {
            //first time initiating a lock
            //redirect to set up lock activity
            checkForEmptyConditionsAndProceedForLockedNote(title, note);

        } else {
            //not first time means lock that specific entity
            lock_this_entity(title, note);
        }
    }

    private void lock_this_entity(String title, String note) {
        String date = getDateTime();

        if (title.isEmpty() && note.isEmpty()) {
            new MaterialAlertDialogBuilder(Create_Note.this, R.style.CustomMaterialDialog).setTitle("Enter title and note!")
                    .setIcon(R.drawable.warning)
                    .setPositiveButton(
                            "Ok", (dialogInterface, i) -> {
                            }
                    ).show();
        } else {

            if (note.isEmpty()) {
                new MaterialAlertDialogBuilder(Create_Note.this, R.style.CustomMaterialDialog).setTitle("Enter something in the note!")
                        .setIcon(R.drawable.warning)
                        .setPositiveButton(
                                "Ok", (dialogInterface, i) -> {
                                }
                        ).show();
            } else {
                if (title.isEmpty()) {
                    title = note.substring(0, note.length() / 2) + "..";
                }
                databaseHelperForNotes.insertData_for_locked(title, note, date);
                showToast("Locked..");
                Intent intent_for_main_activity = new Intent(Create_Note.this, MainActivity.class);
                startActivity(intent_for_main_activity);
            }

        }

    }

    private void checkForEmptyConditionsAndProceedForNormalNote(String title, String note) {
        String date = getDateTime();

        if (title.isEmpty() && note.isEmpty()) {
            new MaterialAlertDialogBuilder(Create_Note.this, R.style.CustomMaterialDialog).setTitle("Enter title and note!")
                    .setIcon(R.drawable.warning)
                    .setPositiveButton(
                            "Ok", (dialogInterface, i) -> {
                            }
                    ).show();
        } else {

            if (note.isEmpty()) {
                new MaterialAlertDialogBuilder(Create_Note.this, R.style.CustomMaterialDialog).setTitle("Enter something in the note!")
                        .setIcon(R.drawable.warning)
                        .setPositiveButton(
                                "Ok", (dialogInterface, i) -> {
                                }
                        ).show();
            } else {
                if (title.isEmpty()) {
                    title = note.substring(0, note.length() / 2) + "..";
                }
                long insertedId = databaseHelperForNotes.insertData(title, note, date);
                Intent intent = new Intent(Create_Note.this, MainActivity.class);
                startActivity(intent);
            }

        }

    }

    private void checkForEmptyConditionsAndProceedForLockedNote(String title, String note) {
        if (title.isEmpty() && note.isEmpty()) {
            new MaterialAlertDialogBuilder(Create_Note.this, R.style.CustomMaterialDialog).setTitle("Enter title and note!")
                    .setIcon(R.drawable.warning)
                    .setPositiveButton(
                            "Ok", (dialogInterface, i) -> {
                            }
                    ).show();
        } else {

            if (note.isEmpty()) {
                new MaterialAlertDialogBuilder(Create_Note.this, R.style.CustomMaterialDialog).setTitle("Enter something in the note!")
                        .setIcon(R.drawable.warning)
                        .setPositiveButton(
                                "Ok", (dialogInterface, i) -> {
                                }
                        ).show();
            } else {
                if (title.isEmpty()) {
                    title = note.substring(0, note.length() / 2) + "..";
                }
                Intent intent_Initiate_Password_FirstTime = new Intent(Create_Note.this, setQuestions.class);
                intent_Initiate_Password_FirstTime.putExtra("Title", title);
                intent_Initiate_Password_FirstTime.putExtra("Note", note);
                startActivity(intent_Initiate_Password_FirstTime);
            }
        }
    }

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}


