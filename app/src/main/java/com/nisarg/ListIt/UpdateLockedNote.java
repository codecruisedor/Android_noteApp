package com.nisarg.ListIt;

import android.content.Intent;
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
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

public class UpdateLockedNote extends AppCompatActivity {
    EditText note, title;
    DatabaseHelperForNotes databaseHelperForNotes;

    String title_from_activity, content_from_activity;
    int id_from_activity;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_locked_note);

        databaseHelperForNotes = new DatabaseHelperForNotes(UpdateLockedNote.this);
        toolbar = findViewById(R.id.updateNote_toolbar_locked);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Update Note");

        note = findViewById(R.id.content_note2_locked);
        title = findViewById(R.id.Title2_locked);
        note.requestFocus();

        getAndSetIntentData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_menu_locked, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            //noinspection RestrictedApi
            m.setOptionalIconsVisible(true);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(UpdateLockedNote.this, Display_locked_notes.class);
                startActivity(intent);
                break;
            case R.id.delete_update_locked:
                DeleteNote(id_from_activity);
                break;
            case R.id.unlock:
                //delete from locked database insert into normal database
                unlockNote(id_from_activity, title_from_activity, content_from_activity);
                break;
            case R.id.share_locked:
                new MaterialAlertDialogBuilder(UpdateLockedNote.this, R.style.CustomMaterialDialog).setTitle("Share this locked note?")
                        .setIcon(R.drawable.warning)
                        .setPositiveButton(
                                "Yes", (dialogInterface, i) -> {

                                    Intent sendIntent = getActionSendIntent();
                                    startActivity(sendIntent);
                                }
                        ).setNegativeButton("No", (dialogInterface, i) -> {


                }).show();

                break;
            case R.id.done_update_locked:
                DoneNote(title.getText().toString(), note.getText().toString());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent getActionSendIntent() {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, title.getText().toString() + "\n" + note.getText().toString());
        sendIntent.setType("text/plain");
        return sendIntent;

    }

    private void unlockNote(int id_from_activity, String title, String content) {
        new MaterialAlertDialogBuilder(UpdateLockedNote.this, R.style.CustomMaterialDialog).setTitle("Unlock this Note?")
                .setIcon(R.drawable.warning)
                .setPositiveButton(
                        "Yes", (dialogInterface, i) -> {

                            databaseHelperForNotes.insertData(title, content, getDateTime());
                            databaseHelperForNotes.delete_for_locked(id_from_activity);
                            Intent intent = new Intent(UpdateLockedNote.this, Display_locked_notes.class);
                            startActivity(intent);
                        }
                ).setNegativeButton("No", (dialogInterface, i) -> {


        }).show();

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

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void DoneNote(String title_from_activity, String content_from_activity) {
        //  Toast.makeText(UpdateLockedNote.this,"id: "+this.id_from_activity,Toast.LENGTH_SHORT).show();
        long res = databaseHelperForNotes.update_for_locked(this.id_from_activity, title_from_activity, content_from_activity);
        Intent intent = new Intent(UpdateLockedNote.this, Display_locked_notes.class);
        startActivity(intent);
    }

    private void DeleteNote(int id) {
        //dialogue for delete or not
        //if yes then delete
        new MaterialAlertDialogBuilder(UpdateLockedNote.this, R.style.CustomMaterialDialog).setTitle("Delete this Note?")
                .setIcon(R.drawable.warning)
                .setPositiveButton(
                        "Yes", (dialogInterface, i) -> {
                            int key_deleted = databaseHelperForNotes.delete_for_locked(id);
                            Intent delete_intent = new Intent(UpdateLockedNote.this, Display_locked_notes.class);
                            startActivityForResult(delete_intent, 1);
                        }
                ).setNegativeButton("No", (dialogInterface, i) -> {

        }).show();

    }

    private void getAndSetIntentData() {

        if (getIntent().hasExtra("id") && getIntent().hasExtra("title") && getIntent().hasExtra("content")) {
            //get data

            id_from_activity = getIntent().getIntExtra("id", 0);
            title_from_activity = getIntent().getStringExtra("title");
            content_from_activity = getIntent().getStringExtra("content");

            //set data
            note.setText(content_from_activity);
            title.setText(title_from_activity);

        } else {
            showToast("No data..");
        }
    }
}
