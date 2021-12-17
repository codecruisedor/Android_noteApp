package com.nisarg.ListIt;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

public class UpdateNote extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    static EditText note2, title2;
    DatabaseHelperForNotes databaseHelperForNotes;

    String title, content;
    String timeText;
    int id;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);


        databaseHelperForNotes = new DatabaseHelperForNotes(UpdateNote.this);
        toolbar = findViewById(R.id.updateNote_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        note2 = findViewById(R.id.content_note2);
        title2 = findViewById(R.id.Title2);
        note2.requestFocus();


        getAndSetIntentData();
    }

    public static String getTitleOfNote() {
        return title2.getText().toString();
    }

    public static String getNoteOfNote() {
        return note2.getText().toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_menu, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            //noinspection RestrictedApi
            m.setOptionalIconsVisible(true);

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            Intent intent = new Intent(UpdateNote.this, MainActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.lock_update) {
            LockNote(title2.getText().toString(), note2.getText().toString());
        } else if (itemId == R.id.done_update) {
            DoneNote(title2.getText().toString(), note2.getText().toString());
        } else if (itemId == R.id.share) {
            Intent sendIntent = getActionSendIntent();
            startActivity(sendIntent);
        } else if (itemId == R.id.reminder_update) {
            // DialogFragment timePicker = new TimePickerFragment(title2.getText().toString(), note2.getText().toString());
            //timePicker.show(getSupportFragmentManager(), "time picker");
            Calendar cal = Calendar.getInstance();
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra("beginTime", cal.getTimeInMillis());
            intent.putExtra("allDay", true);
            intent.putExtra("rrule", "FREQ=YEARLY");
            intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
            intent.putExtra("title", note2.getText().toString());
            startActivity(intent);
        } else if (itemId == R.id.delete_update) {
            DeleteNote(id);
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

    private Intent getActionSendIntent() {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, title2.getText().toString() + "\n" + note2.getText().toString());
        sendIntent.setType("text/plain");
        return sendIntent;

    }

    private void DeleteNote(int id) {
        //dialogue for delete or not
        //if yes then delete
        new MaterialAlertDialogBuilder(UpdateNote.this, R.style.CustomMaterialDialog).setTitle("Delete this Note?")
                .setIcon(R.drawable.warning)
                .setPositiveButton(
                        "Yes", (dialogInterface, i) -> {
                            int key_deleted = databaseHelperForNotes.delete(id);
                            Intent delete_intent = new Intent(UpdateNote.this, MainActivity.class);
                            startActivityForResult(delete_intent, 1);
                        }
                ).setNegativeButton("No", (dialogInterface, i) -> {

        }).show();

    }

    private void DoneNote(String t, String n) {
        checkForEmptyConditionsAndProceedForNormalNote(t, n);
    }

    private void checkForEmptyConditionsAndProceedForNormalNote(String title, String note) {
        if (title.isEmpty() && note.isEmpty()) {
            new MaterialAlertDialogBuilder(UpdateNote.this, R.style.CustomMaterialDialog).setTitle("Enter title and note!")
                    .setIcon(R.drawable.warning)
                    .setPositiveButton(
                            "Ok", (dialogInterface, i) -> {
                            }
                    ).show();
        } else {

            if (note.isEmpty()) {
                new MaterialAlertDialogBuilder(UpdateNote.this, R.style.CustomMaterialDialog).setTitle("Enter something in the note!")
                        .setIcon(R.drawable.warning)
                        .setPositiveButton(
                                "Ok", (dialogInterface, i) -> {
                                }
                        ).show();
            } else {
                if (title.isEmpty()) {
                    title = note.substring(0, note.length() / 2) + "..";
                }
                databaseHelperForNotes.update(this.id, title, note);
                Intent intent = new Intent(UpdateNote.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    private void LockNote(String title, String note) {

        Context context = UpdateNote.this;
        SharedPreferences sharedPref_first_time_lock = context.getSharedPreferences(
                getString(R.string.first_time_set_lock), Context.MODE_PRIVATE);

        //SharedPreferences.Editor editor = sharedPref_first_time_lock.edit();
        if ("null".equalsIgnoreCase(sharedPref_first_time_lock.getString(getString(R.string.first_time_set_lock), "null"))) {
            //first time initiating a lock
            //redirect to set up lock activity
            checkForEmptyConditionsAndProceedForLockedNote(title, note);

        } else {
            lock_this_entity(title, note);
        }


    }

    private void lock_this_entity(String title, String note) {
        if (title.isEmpty() && note.isEmpty()) {
            new MaterialAlertDialogBuilder(UpdateNote.this, R.style.CustomMaterialDialog).setTitle("Enter title and note!")
                    .setIcon(R.drawable.warning)
                    .setPositiveButton(
                            "Ok", (dialogInterface, i) -> {
                            }
                    ).show();
        } else {

            if (note.isEmpty()) {
                new MaterialAlertDialogBuilder(UpdateNote.this, R.style.CustomMaterialDialog).setTitle("Enter something in the note!")
                        .setIcon(R.drawable.warning)
                        .setPositiveButton(
                                "Ok", (dialogInterface, i) -> {
                                }
                        ).show();
            } else {
                if (title.isEmpty()) {
                    title = note.substring(0, note.length() / 2) + "..";
                }
                databaseHelperForNotes.delete(id);
                databaseHelperForNotes.insertData_for_locked(title, note, getDateTime());
                showToast("Locked..");
                Intent intent_for_main_activity = new Intent(UpdateNote.this, MainActivity.class);
                startActivity(intent_for_main_activity);
            }
        }
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void checkForEmptyConditionsAndProceedForLockedNote(String title, String note) {
        if (title.isEmpty() && note.isEmpty()) {
            new MaterialAlertDialogBuilder(UpdateNote.this, R.style.CustomMaterialDialog).setTitle("Enter title and note!")
                    .setIcon(R.drawable.warning)
                    .setPositiveButton(
                            "Ok", (dialogInterface, i) -> {
                            }
                    ).show();
        } else {

            if (note.isEmpty()) {
                new MaterialAlertDialogBuilder(UpdateNote.this, R.style.CustomMaterialDialog).setTitle("Enter something in the note!")
                        .setIcon(R.drawable.warning)
                        .setPositiveButton(
                                "Ok", (dialogInterface, i) -> {
                                }
                        ).show();
            } else {
                if (title.isEmpty()) {
                    title = note.substring(0, note.length() / 2) + "..";
                }
                Intent intent_Initiate_Password_FirstTime = new Intent(UpdateNote.this, setQuestions.class);
                intent_Initiate_Password_FirstTime.putExtra("Title", title);
                intent_Initiate_Password_FirstTime.putExtra("Note", note);
                intent_Initiate_Password_FirstTime.putExtra("id", this.id);
                startActivity(intent_Initiate_Password_FirstTime);
            }
        }

    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("title") && getIntent().hasExtra("content")) {
            //get data
            id = getIntent().getIntExtra("id", 0);
            title = getIntent().getStringExtra("title");
            content = getIntent().getStringExtra("content");

            //set data
            note2.setText(content);
            title2.setText(title);
        } else {
            showToast("No data..");
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        updateTimeText(c);
        startAlarm(c);
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationPublisher.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        Objects.requireNonNull(alarmManager).setExact(AlarmManager.RTC_WAKEUP,
                c.getTimeInMillis(), pendingIntent);
    }

    private void updateTimeText(Calendar c) {
        timeText = "Alert set for ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        showToast(timeText);
    }

    public void select_options_products(View view) {

    }
}