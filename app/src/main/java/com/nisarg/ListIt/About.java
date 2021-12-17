package com.nisarg.ListIt;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {
    TextView icon_string;
    String resource_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        icon_string = findViewById(R.id.icon_string);
        //icon_string.setText(Html.fromHtml(getString(R.string.Resources)));
        // icon_string.setText(getString(R.string.Resources, "Freepik"));


        icon_string.setText(R.string.about);

        icon_string.setTextColor(Color.BLACK);

        icon_string.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.freepik.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


    }

}