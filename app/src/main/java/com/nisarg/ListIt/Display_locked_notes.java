package com.nisarg.ListIt;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Display_locked_notes extends AppCompatActivity {


    ArrayList<cardHandler> cardList, filteredList;
    private RecyclerView recyclerView;
    private cardAdapter_locked adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Toolbar toolbar;


    ImageView imageViewOfChecked;
    ImageView coconutImage;
    View main_view;
    TextView NoNotesOrListView;
    DatabaseHelperForNotes databaseHelperForNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_locked_notes);


        recyclerView = findViewById(R.id.view_for_notes_list_locked);
        databaseHelperForNotes = new DatabaseHelperForNotes(Display_locked_notes.this);
        main_view = findViewById(R.id.main_view_locked);
        recyclerView = findViewById(R.id.view_for_notes_list_locked);
        cardList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar_locked);
        coconutImage = findViewById(R.id.coconut_locked);
        NoNotesOrListView = findViewById(R.id.textView_fornolist_locked);


/*Integer deleted = databaseHelperForNotes.deleteAllData_for_locked();
        if (deleted<=0){

        }else{
            Toast.makeText(Display_locked_notes.this,
                    "Deleted All data.. ",
                    Toast.LENGTH_SHORT).show();
        }*/

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.back);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getNotesAndList();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }


    @Override
    public void onBackPressed() {
        Intent intentGoBackToMainScreen = new Intent(Display_locked_notes.this, MainActivity.class);
        startActivity(intentGoBackToMainScreen);
        super.onBackPressed();
    }


    private void getNotesAndList() {
        String noteContent, noteTitle, noteDate;
        int noteid;
        Cursor cursor;
        cursor = databaseHelperForNotes.viewDefault_for_locked();
        if (cursor == null || cursor.getCount() <= 0) {

            coconutImage.setVisibility(View.VISIBLE);

        } else {

            recyclerView.setVisibility(View.VISIBLE);
            cursor.moveToLast();
            do {

                noteContent = cursor.getString(cursor.getColumnIndex("CONTENT"));
                noteid = cursor.getInt(cursor.getColumnIndex("_id"));
                noteTitle = cursor.getString(cursor.getColumnIndex("TITLE"));
                noteDate = cursor.getString(cursor.getColumnIndex("DATE"));
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "dd-MM-yyyy HH:mm", Locale.getDefault());
                Date date = new Date();
                String timeStamp;
                String date_current = dateFormat.format(date);
                if (date_current.substring(0, 2).equals(noteDate.substring(0, 2))) {
                    timeStamp = "Today " + noteDate.substring(11);
                } else if (Integer.parseInt(date_current.substring(0, 2)) - Integer.parseInt(noteDate.substring(0, 2)) == 1) {
                    timeStamp = "Yesterday " + noteDate.substring(11);
                } else {
                    String month = noteDate.substring(3, 5);
                    String monthName = new DateFormatSymbols().getMonths()[Integer.parseInt(month) - 1];
                    timeStamp = noteDate.substring(0, 2) + " " + monthName;
                }
                populateCards(Display_locked_notes.this, noteContent, noteTitle, timeStamp, getRandomColor(), noteid);
                cursor.moveToPrevious();
            } while (!cursor.isBeforeFirst());
        }

        recyclerViewConfigure();
    }

    private void recyclerViewConfigure() {
        //configure adapter
        //performance
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        adapter = new cardAdapter_locked(cardList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setmListener(new cardAdapter_locked.OnItemClickListener() {
            @Override
            public void singleClicked(cardAdapter_locked.cardViewHolder holder, cardHandler card) {
                //handle click
                Intent intentForUpdateLockedBNote = new Intent(Display_locked_notes.this, UpdateLockedNote.class);
                intentForUpdateLockedBNote.putExtra("id", card.getId());
                intentForUpdateLockedBNote.putExtra("title", card.getmTitle());
                intentForUpdateLockedBNote.putExtra("content", card.getmContent());

                startActivity(intentForUpdateLockedBNote);
            }
        });

    }

    private int getRandomColor() {

        String[] colors = {"#DBA9A9", "#EAB9B9", "#E7DDDC", "#FFE7E4", "#FFC388", "#FFC388", "#D6D1B4", "#FFEC88", "#F7F4C7", "#D4EC88", "#CCE7C7", "#B1F2EC",
                "#B4ECEA", "#DBF6FA", "#C7E8FD", "#C6BDF8", "#D9A5F9", "#ECDFEC", "#EAB7BD", "#FB7E81", "#F39FA1", "#FFD9E0", "#FF99EB", "#C2BBEA"};
        int idx = new Random().nextInt(colors.length);
        String random = (colors[idx]);
        int col = Color.parseColor(random);
        try {
            return col;
        } catch (NumberFormatException e) {
            return 0;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.lockednotes_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search_locked);
        searchLogic(menuItem);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(Display_locked_notes.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchLogic(MenuItem menuItem) {
        SearchView searchItem = (SearchView) menuItem.getActionView();
        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!cardList.isEmpty()) {
                    filteredList = filter(cardList, newText);
                    adapter.setFilter(filteredList);
                    return true;
                }
                return false;
            }
        });
    }

    private void populateCards(Context con, String noteContent, String noteTitle, String date, int color, int id) {
        cardList.add(new cardHandler(this.imageViewOfChecked, Display_locked_notes.this, con, noteTitle, noteContent, date, color, id));
    }


    private ArrayList<cardHandler> filter(ArrayList<cardHandler> dataList, String newText) {
        newText = newText.toLowerCase();
        String text;
        ArrayList<cardHandler> filteredList = new ArrayList<>();
        for (cardHandler dataFromDataList : dataList) {

            text = dataFromDataList.getmTitle().toLowerCase();
            if (text.contains(newText)) {
                filteredList.add(dataFromDataList);
            }
        }
        return filteredList;
    }

}