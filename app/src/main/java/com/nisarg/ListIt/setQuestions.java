package com.nisarg.ListIt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

public class setQuestions extends AppCompatActivity {
    String title = "", content = "";
    int id = 0;
    TextInputEditText animal, movie, food;
    TextInputLayout animal_layout, movie_layout, food_layout;
    ExtendedFloatingActionButton done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_questions);

        animal = findViewById(R.id.question_animal);
        movie = findViewById(R.id.question_movie);
        food = findViewById(R.id.question_food);
        done = findViewById(R.id.done_questions);

        animal_layout = findViewById(R.id.textField_animal);
        movie_layout = findViewById(R.id.textField_movie);
        food_layout = findViewById(R.id.textField_food);


        getIntentData();
        HandleDone();

    }

    private void HandleDone() {
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (animal.getText().toString().isEmpty() || movie.getText().toString().isEmpty() || food.getText().toString().isEmpty()) {
                    showToast("Answer all three questions..");
                } else {

                    if (animal.getText().length() > 19 || movie.getText().length() > 19 || food.getText().length() > 19) {
                        showToast("Shorten your answer..");
                        return;
                    }

                    SharedPreferences answers_pref = (setQuestions.this).getSharedPreferences("ANSWERS", MODE_PRIVATE);
                    answers_pref.edit().putString("ANIMAL", animal.getText().toString()).apply();
                    answers_pref.edit().putString("MOVIE", movie.getText().toString()).apply();
                    answers_pref.edit().putString("FOOD", food.getText().toString()).apply();
                    Intent pattern_activity_first_time = new Intent(setQuestions.this, PatternActivity_FirstTime.class);
                    if (!title.isEmpty() && !content.isEmpty() && id != 0) {
                        pattern_activity_first_time.putExtra("Title", title);
                        pattern_activity_first_time.putExtra("Note", content);
                        pattern_activity_first_time.putExtra("id", id);
                    } else if (!title.isEmpty() && !content.isEmpty() && id == 0) {
                        pattern_activity_first_time.putExtra("Title", title);
                        pattern_activity_first_time.putExtra("Note", content);
                    }
                    showToast(answers_pref.getString("ANIMAL", " ") + answers_pref.getString("MOVIE", " ") + answers_pref.getString("FOOD", " "));
                    startActivity(pattern_activity_first_time);
                }
            }
        });
    }


    private void showToast(String string) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toastl_layout_custom, (ViewGroup) findViewById(R.id.toast_root));
        TextView toastText = layout.findViewById(R.id.toast_text);
        toastText.setText(string);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }


    private void getIntentData() {
        if (getIntent().hasExtra("Title") && (getIntent().hasExtra("Note") || getIntent().hasExtra("id"))) {
            title = getIntent().getStringExtra("Title");
            content = getIntent().getStringExtra("Note");
            id = getIntent().getIntExtra("id", 0);
        }
    }
}