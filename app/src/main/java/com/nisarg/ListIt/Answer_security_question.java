package com.nisarg.ListIt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

public class Answer_security_question extends AppCompatActivity {
    EditText editText;
    TextView question;
    ExtendedFloatingActionButton done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_security_question);

        editText = findViewById(R.id.answer_here);
        question = findViewById(R.id.security_question);
        done = findViewById(R.id.done_question_answered);
        HandleDone();


    }

    private void HandleDone() {
        SharedPreferences answer = Answer_security_question.this.getSharedPreferences("ANSWERS", MODE_PRIVATE);
        int no = getRandom();
        String ori_answer;
        if (no == 1) {
            question.setText(R.string.question_food);
            ori_answer = answer.getString("FOOD", "null");

        } else if (no == 2) {
            question.setText(R.string.question_movie);
            ori_answer = answer.getString("MOVIE", "null");
        } else {
            question.setText(R.string.question_animal);
            ori_answer = answer.getString("ANIMAL", "null");
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ori_answer.contentEquals(editText.getText())) {
                    //matches
                    SharedPreferences sharedPref_first_time_lock = Answer_security_question.this.getSharedPreferences(getString(R.string.first_time_set_lock), MODE_PRIVATE);
                    SharedPreferences.Editor editorForFirstTimeLock = sharedPref_first_time_lock.edit();
                    editorForFirstTimeLock.putString(getString(R.string.first_time_set_lock), "null");
                    editorForFirstTimeLock.apply();
                    Intent intent = new Intent(Answer_security_question.this, PatternActivity_FirstTime.class);
                    startActivity(intent);
                } else {
                    editText.setError("Enter correct answer");
                    //dosent match
                }
            }
        });

    }

    private int getRandom() {
        int no = (int) Math.random();
        if (no > 0.0 && no < 0.3) {
            return 1;
        } else if (no > 0.3 && no < 0.5) {
            return 2;
        } else {
            return 3;
        }
    }
}