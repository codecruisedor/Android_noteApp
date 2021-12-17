package com.nisarg.ListIt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class enterPattern extends AppCompatActivity {
    private PatternLockView mPatternLockView;
    private PatternLockViewListener mPatternLockViewListener;
    private int counter;
    private TextView textView_for_lock, forgot_pattern;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pattern);

        SharedPreferences sharedPref_Pattern = enterPattern.this.getSharedPreferences(getString(R.string.pattern), MODE_PRIVATE);
        mPatternLockView = findViewById(R.id.pattern_lock_view_enter);
        textView_for_lock = findViewById(R.id.textview_for_enter_lock);
        counter = 0;
        forgot_pattern = findViewById(R.id.forgot_pattern);

        handleForgotPattern();


        mPatternLockViewListener = new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                if (PatternLockUtils.patternToString(mPatternLockView, pattern).equalsIgnoreCase(sharedPref_Pattern.getString(getString(R.string.pattern), "null"))) {
                    mPatternLockView.setCorrectStateColor(getResources().getColor(R.color.design_default_color_secondary));
                    Intent intent_display = new Intent(enterPattern.this, Display_locked_notes.class);
                    startActivity(intent_display);

                } else {
                    showToast("Wrong!!");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            mPatternLockView.clearPattern();
                        }
                    }, 1000);
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

    private void handleForgotPattern() {
        forgot_pattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer;
                int key = 0;
                SharedPreferences sharedPref_answers = enterPattern.this.getSharedPreferences("ANSWERS", MODE_PRIVATE);

                int rand = getRandNUmber();
                switch (rand) {
                    case 0:
                        answer = sharedPref_answers.getString("ANIMAL", "null");
                        key = 0;
                        break;
                    case 1:
                        answer = sharedPref_answers.getString("FOOD", "null");
                        key = 1;
                        break;
                    case 2:
                        answer = sharedPref_answers.getString("MOVIE", "null");
                        key = 2;
                        break;
                    default:
                        answer = "null";
                        break;
                }
                Intent intent_answer_question = new Intent(enterPattern.this, Answer_security_question.class);
                intent_answer_question.putExtra("final_answer", answer);
                intent_answer_question.putExtra("key", key);
                startActivity(intent_answer_question);

            }
        });
    }

    private int getRandNUmber() {
        int Min = 0, Max = 2;
        return Min + (int) (Math.random() * ((Max - Min) + 1));
    }
}