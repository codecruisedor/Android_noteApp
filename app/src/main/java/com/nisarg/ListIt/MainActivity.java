package com.nisarg.ListIt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/*attribution link:<div>Icons made by <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>*/
//<div>Icons made by <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
//<a target="_blank" href="https://icons8.com/icon/3502/coconut-cocktail">Coconut Cocktail</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
public class MainActivity extends AppCompatActivity {

    ArrayList<cardHandler> cardList, filteredCardList, selectedItems, pinnedItems;
    private RecyclerView recyclerView;
    private cardAdapter adapter;
    public boolean isSelectMode = false;
    private ActionMode mActionMode;
    private static final int REQUEST_CODE_SPEECH_INPUT = 78;

    ImageView imageViewOfChecked;
    ImageView coconutImage;
    View main_view;
    LinearLayout buttonLayout;
    ImageView AddNormalNoteActionButton, AddVoiceNoteActionButton;
    DatabaseHelperForNotes databaseHelperForNotes;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLayout = findViewById(R.id.buttons_layout);
        databaseHelperForNotes = new DatabaseHelperForNotes(MainActivity.this);
        main_view = findViewById(R.id.main_view);
        recyclerView = findViewById(R.id.view_for_notes_list);
        cardList = new ArrayList<>();
        AddNormalNoteActionButton = findViewById(R.id.createNewNote);
        AddVoiceNoteActionButton = findViewById(R.id.createVoiceNote);
        Toolbar toolbar = findViewById(R.id.toolbar);
        imageViewOfChecked = findViewById(R.id.selectedImage);
        coconutImage = findViewById(R.id.coconut);
        selectedItems = new ArrayList<>();
        pinnedItems = new ArrayList<>();


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getNotesAndList();
        setRecyclerViewScrollListener();


        /*Integer deleted = databaseHelperForNotes.deleteAllData();
        if (deleted<=0){

        }else{
            Toast.makeText(MainActivity.this,
                    "Deleted All data.. ",
                    Toast.LENGTH_SHORT).show();
        }
*/

    }

    private void setRecyclerViewScrollListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int x, int y, int oldX, int oldY) {
                    int scroll = y - oldY;
                    if (scroll > 0) {
                        buttonLayout.setVisibility(View.GONE);
                    } else if (scroll < 0) {
                        buttonLayout.setVisibility(View.VISIBLE);
                    } else {
                        buttonLayout.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    private void getNotesAndList() {
        String noteContent, noteTitle, noteDate, pinnedOrNot;
        int noteid;
        Cursor cursor;
        cursor = databaseHelperForNotes.viewDefault();
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
                populateCards(MainActivity.this, noteContent, noteTitle, timeStamp, getRandomColor(), noteid);
                cursor.moveToPrevious();
            } while (!cursor.isBeforeFirst());
        }
        setDeleteSwipeAction();
        recyclerViewConfigure();
    }


    private void recyclerViewConfigure() {
        //configure adapter
        recyclerView = findViewById(R.id.view_for_notes_list);
        //performance
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new cardAdapter(cardList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //set up clicks
        adapter.setmListener(new cardAdapter.OnItemClickListener() {

            @Override
            public void singleClicked(cardAdapter.cardViewHolder holder, cardHandler card) {

                if (isSelectMode) {
                    if (selectedItems.contains(card)) {
                        selectedItems.remove(card);
                        mActionMode.setTitle(selectedItems.size() + " selected");
                        holder.imageView.setVisibility(View.GONE);
                        if (selectedItems.size() == 0) {
                            isSelectMode = false;
                            mActionMode.finish();
                            holder.imageView.setVisibility(View.GONE);
                        }
                    } else {
                        selectedItems.add(card);
                        mActionMode.setTitle(selectedItems.size() + " selected");
                        holder.imageView.setVisibility(View.VISIBLE);
                    }

                } else {

                    Intent intentOfUpdate = new Intent(MainActivity.this, UpdateNote.class);
                    intentOfUpdate.putExtra("id", card.getId());
                    intentOfUpdate.putExtra("title", card.getmTitle());
                    intentOfUpdate.putExtra("content", card.getmContent());
                    startActivity(intentOfUpdate);

                }
            }

            @Override
            public void longClicked(cardAdapter.cardViewHolder holder, cardHandler card) {

                if (!isSelectMode) {
                    isSelectMode = true;
                    selectedItems.add(card);
                    holder.imageView.setVisibility(View.VISIBLE);
                    if (mActionMode == null) {
                        mActionMode = startSupportActionMode(callback);
                    }
                }
            }
        });

    }

    public void EmptyNotesView() {
        recyclerView.setVisibility(View.INVISIBLE);
        coconutImage.setVisibility(View.VISIBLE);
    }


    private final ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.multiselect, menu);
            mode.setTitle(selectedItems.size() + " selected");
            //check for pinned items selected or not
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.deleteMulti) {
                isSelectMode = false;

                for (cardHandler s : selectedItems) {
                    databaseHelperForNotes.delete(s.getId());
                    cardList.remove(s);
                    adapter.notifyDataSetChanged();
                }

                if (cardList.isEmpty()) {
                    EmptyNotesView();
                }
                selectedItems.clear();
                mode.finish();
            }
            return true;
        }


        @Override
        public void onDestroyActionMode(ActionMode mode) {
            isSelectMode = false;
            mActionMode = null;
            if (!selectedItems.isEmpty()) {
                for (cardHandler s : selectedItems) {
                    try {
                        s.getMimageView().setVisibility(View.GONE);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                selectedItems.clear();
                adapter.notifyDataSetChanged();
            }
            mode.finish();
        }
    };


    private void changeAdapterPositions(ArrayList<cardHandler> selectedItems) {
        int index = 0;
        for (cardHandler s : selectedItems) {
            int positionOfPinnedCard = cardList.indexOf(s);
            cardList.set(positionOfPinnedCard, cardList.get(index));
            cardList.set(index++, s);
        }
    }


    private void populateCards(Context con, String noteContent, String noteTitle, String date, int color, int id) {
        cardList.add(new cardHandler(this.imageViewOfChecked, MainActivity.this, con, noteTitle, noteContent, date, color, id));
    }

    private void setDeleteSwipeAction() {

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                DeleteItem(viewHolder);
            }

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void DeleteItem(RecyclerView.ViewHolder viewHolder) {

        int id = adapter.getCardHandlerArrayList().get(viewHolder.getAdapterPosition()).getId();
        int id_of_deleted = databaseHelperForNotes.delete(id);
        cardList.remove(viewHolder.getAdapterPosition());
        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        if (cardList.isEmpty()) {
            EmptyNotesView();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "dd-MM-yyyy HH:mm", Locale.getDefault());
                Date date = new Date();
                String date_current = dateFormat.format(date);
                if (Objects.requireNonNull(result).get(0) != null) {
                    int id = databaseHelperForNotes.insertData(result.get(0), result.get(0), date_current);
                }
                recreate();

            }
        } else if (requestCode == 1) {
            recreate();
        }
    }


    public int getRandomColor() {

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

        inflater.inflate(R.menu.mainmenu, menu);
        MenuItem menuItemForSearch = menu.findItem(R.id.app_bar_search);

        searchLogic(menuItemForSearch);

        return super.onCreateOptionsMenu(menu);
    }


    private void searchLogic(MenuItem menuItem) {
        if (cardList.isEmpty()) {
            return;
        }
        SearchView searchItem = (SearchView) menuItem.getActionView();
        searchItem.setQueryHint("search");
        searchItem.requestFocus();
        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!cardList.isEmpty()) {
                    filteredCardList = filter(cardList, newText);
                    adapter.setFilter(filteredCardList);
                    return true;
                }
                return false;
            }

        });
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


    public void createNewNote(View view) {
        Intent noteIntent = new Intent(MainActivity.this, Create_Note.class);
        startActivity(noteIntent);
    }

    public void createNewVoiceNote(View view) {
        Intent intent
                = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak...");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast
                    .makeText(MainActivity.this, " " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                    .show();
        }
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


    @SuppressLint("RestrictedApi")
    public void list_selection(View view) {
        @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(MainActivity.this);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.access_lists, menuBuilder);
        @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(this, menuBuilder, view);
        optionsMenu.setForceShowIcon(true);
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.access_locked_notes) {// Handle option1 Click
                    SharedPreferences first_time = MainActivity.this.getSharedPreferences(getString(R.string.first_time_set_lock), MODE_PRIVATE);
                    if ("null".equalsIgnoreCase(first_time.getString(getString(R.string.first_time_set_lock), "null"))) {

                        Intent intent_Initiate_Password_FirstTime = new Intent(MainActivity.this, setQuestions.class);
                        startActivity(intent_Initiate_Password_FirstTime);

                    } else {
                        Intent intent_Initiate_Password = new Intent(MainActivity.this, enterPattern.class);
                        startActivity(intent_Initiate_Password);
                    }
                    return true;
                } else if (itemId == R.id.access_about) {// Handle option2 Click
                    Intent intent_About = new Intent(MainActivity.this, About.class);
                    startActivity(intent_About);
                    return true;
                } else if (itemId == R.id.access_shopping_lists) {
                    Intent intent_About = new Intent(MainActivity.this, Lists_shopping.class);
                    startActivity(intent_About);
                    return true;
                } else if (itemId == R.id.access_wish_lists) {
                    Intent intent_About = new Intent(MainActivity.this, taskList.class);
                    startActivity(intent_About);
                    return true;
                }
                return false;
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {

            }
        });

        optionsMenu.show();


    }
}