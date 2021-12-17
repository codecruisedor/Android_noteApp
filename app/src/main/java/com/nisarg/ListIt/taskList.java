package com.nisarg.ListIt;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class taskList extends AppCompatActivity {


    ArrayList<cardHandler_tasklist> task_array_list;
    private RecyclerView recyclerView;
    private cardAdapter_taksLIst adapter;
    private DatabaseHelperForNotes databaseHelperForNotes;
    private ImageView coconutImage;
    public LinearLayout layout_for_wish_litsts;
    public TextView textView;
    MaterialCheckBox mCheckBox;
    SharedPreferences sorted_by;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_lists);

        databaseHelperForNotes = new DatabaseHelperForNotes(taskList.this);
        Toolbar toolbar = findViewById(R.id.toolbar_task_list);
        layout_for_wish_litsts = findViewById(R.id.layout_for_wish_litsts);
        textView = findViewById(R.id.textView_for_no_taskList);
        textView.setVisibility(View.GONE);
        task_array_list = new ArrayList<>();
        mCheckBox = findViewById(R.id.checkbox_wish_list);
        sorted_by = (taskList.this).getSharedPreferences("sorted_by", MODE_PRIVATE);


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = findViewById(R.id.view_for_task_list);
        coconutImage = findViewById(R.id.coconut_wish_list);
        getTaskList();


    }

    private void getTaskList() {

        Cursor cursor = databaseHelperForNotes.viewDefault_for_task_list();
        String date, task, priority;
        int id;
        if (cursor == null || cursor.getCount() <= 0) {
            coconutImage.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            cursor.moveToLast();
            do {
                date = cursor.getString(cursor.getColumnIndex("DATE"));
                task = cursor.getString(cursor.getColumnIndex("TASK"));
                priority = cursor.getString(cursor.getColumnIndex("PRIORITY"));
                id = cursor.getInt(cursor.getColumnIndex("_id"));

                inflate_This_Item(id, date, task, priority);

                cursor.moveToPrevious();
            } while (!cursor.isBeforeFirst());
            if (sorted_by.getString("sorted_or_not", "null").equalsIgnoreCase("sorted_by_priority")) {
                sortByPriority();
            } else {
                configureRecyclerView(task_array_list);
            }
        }

    }

    private void configureRecyclerView(ArrayList<cardHandler_tasklist> array) {
        //performance
        recyclerView.setHasFixedSize(true);
        adapter = new cardAdapter_taksLIst(array, taskList.this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);


        adapter.setmListener(new cardAdapter_taksLIst.OnItemClickListener() {
            @Override
            public void singleClicked_task(cardAdapter_taksLIst.cardViewHolder holder, cardHandler_tasklist card, int pos) {

                MaterialAlertDialogBuilder editTask = new MaterialAlertDialogBuilder(taskList.this, R.style.CustomMaterialDialog);
                editTask.setTitle("  Edit task");
                editTask.setIcon(R.drawable.add_icon);

                View viewInflated = LayoutInflater.from(taskList.this).inflate(R.layout.entertask_dialogue, (ViewGroup) getWindow().getDecorView().getRootView(), false);
                TextInputEditText textInputLayout = viewInflated.findViewById(R.id.enter_task);
                textInputLayout.setText(card.getTask());
                RadioGroup radioGroup = viewInflated.findViewById(R.id.priority_radio_group);
                if (card.getPriority().equalsIgnoreCase("high")) {
                    radioGroup.check(R.id.high_priority);
                } else {
                    radioGroup.check(R.id.normal_priority);
                }
                editTask.setView(viewInflated);
                editTask.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String updatedTask = textInputLayout.getText().toString();
                        databaseHelperForNotes.update_task(card.getId(), updatedTask, radioGroup.getCheckedRadioButtonId());
                        task_array_list.set(holder.getBindingAdapterPosition(), card);
                        adapter.notifyItemChanged(holder.getBindingAdapterPosition());
                        adapter.notifyDataSetChanged();
                        recreate();
                    }
                });

                editTask.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                editTask.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                    }
                });

                editTask.show();
                /*String[] editOrDelte = {"Edit", "Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(taskList.this);
                builder.setItems(editOrDelte, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                    }
                });
                builder.show();*/

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void singleClicked_checkBox(cardAdapter_taksLIst.cardViewHolder holder, cardHandler_tasklist card, int pos) {

                holder.task.setAlpha((float) 0.3);
                holder.task.setPaintFlags(holder.task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.task.setTextColor(getColor(R.color.grey));

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        task_array_list.remove(card);
                        adapter.notifyItemRemoved(holder.getBindingAdapterPosition());
                    }
                }, 1000);

                databaseHelperForNotes.delete_task(card.getId());
                adapter.notifyDataSetChanged();

                Cursor cursor = databaseHelperForNotes.viewDefault_for_task_list();
                if (cursor == null || cursor.getCount() <= 0) {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            coconutImage.setVisibility(View.VISIBLE);
                        }
                    }, 1000);

                }
            }
        });
    }


    private void inflate_This_Item(int id, String date, String task, String priority) {
        task_array_list.add(new cardHandler_tasklist(this.mCheckBox, id, task, priority, date));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tasklist_main_menu, menu);

        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            //noinspection RestrictedApi
            m.setOptionalIconsVisible(true);
            menu.findItem(R.id.sort_by_date_task).setIcon(R.drawable.tickfinal);
            if (sorted_by.getString("sorted_or_not", "null").equalsIgnoreCase("sorted_by_priority")) {

                menu.findItem(R.id.sort_by_priority_task).setIcon(R.drawable.tickfinal2);
                menu.findItem(R.id.sort_by_date_task).getIcon().setAlpha(0);

            }
        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(taskList.this, MainActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.add_task) {
            MaterialAlertDialogBuilder getTask = new MaterialAlertDialogBuilder(taskList.this, R.style.CustomMaterialDialog);
            getTask.setTitle("  Enter task");
            getTask.setIcon(R.drawable.add_icon);

            View viewInflated = LayoutInflater.from(taskList.this).inflate(R.layout.entertask_dialogue, (ViewGroup) recyclerView.getRootView(), false);
            TextInputEditText textInputLayout = viewInflated.findViewById(R.id.enter_task);
            RadioGroup radioGroup = viewInflated.findViewById(R.id.priority_radio_group);

            getTask.setView(viewInflated);
            getTask.setPositiveButton("Ok", ((dialogInterface, i) -> {
                //store string and do shit
                String priority;
                int checkRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (checkRadioButtonId == R.id.high_priority) {
                    priority = "high";
                } else {
                    priority = "normal";
                }

                AddTaskTo_Database_andInflate(Objects.requireNonNull(textInputLayout.getText().toString()), priority);
            }));

            getTask.setNegativeButton("Cancel", ((dialogInterface, i) -> {

            }));

            getTask.setOnDismissListener(dialogInterface -> {

            });

            getTask.show();
        } else if (item.getItemId() == R.id.sort_by_date_task) {
            sortByDate();
        } else if (item.getItemId() == R.id.sort_by_priority_task) {
            sortByPriority();
        } else if (item.getItemId() == R.id.delete_task_list) {
            DeleteList();
        }
        return super.onOptionsItemSelected(item);
    }

    private void DeleteList() {
        new MaterialAlertDialogBuilder(taskList.this, R.style.CustomMaterialDialog).setTitle("Delete entire list?")
                .setIcon(R.drawable.warning)
                .setPositiveButton(
                        "Yes", (dialogInterface, i) -> {
                            databaseHelperForNotes.delete_all_items();
                            recreate();
                        }
                ).setNegativeButton("No", (dialogInterface, i) -> {

        }).show();

    }

    private void sortByDate() {
        sorted_by.edit().putString("sorted_or_not", "sorted_by_date").apply();
        recreate();

    }

    private void sortByPriority() {
        sorted_by.edit().putString("sorted_or_not", "sorted_by_priority").apply();
        int redIndex = 0, blackIndex = 0;
        while (redIndex < task_array_list.size()) {
            if (task_array_list.get(redIndex).getPriority().equalsIgnoreCase("high")) {
                cardHandler_tasklist blackCard = task_array_list.get(blackIndex);
                task_array_list.set(blackIndex, task_array_list.get(redIndex));
                task_array_list.set(redIndex, blackCard);
                ++blackIndex;
            }
            ++redIndex;
        }

        configureRecyclerView(task_array_list);
    }

    private void AddTaskTo_Database_andInflate(String task, String priority) {

        databaseHelperForNotes.insert_task(task, priority, getDateTime(), "NOT_DONE");
        recreate();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}