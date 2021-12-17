package com.nisarg.ListIt;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class addItem extends AppCompatActivity {

    TextInputEditText name, price, quant, size;
    ExtendedFloatingActionButton done;
    Toolbar toolbar;
    String name_string, price_string, quant_string = "null", size_string = "null";
    DatabaseHelperForNotes databaseHelperForNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelperForNotes = new DatabaseHelperForNotes(addItem.this);
        ;
        setContentView(R.layout.activity_add_item);
        name = findViewById(R.id.product_name);
        price = findViewById(R.id.product_price);
        toolbar = findViewById(R.id.toolbar_create_item);
        quant = findViewById(R.id.product_quant);
        size = findViewById(R.id.product_size);
        done = findViewById(R.id.extendedFloatingActionButton_done_item);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        HandleDone();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(addItem.this, Lists_shopping.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void HandleDone() {
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().toString().isEmpty() || price.getText().toString().isEmpty()) {
                    Toast.makeText(addItem.this, "Enter the fields required..", Toast.LENGTH_SHORT).show();
                } else {
                    name_string = name.getText().toString();
                    price_string = price.getText().toString();
                    if (quant.getText().toString().isEmpty()) {
                        quant_string = "null";
                    } else
                        quant_string = quant.getText().toString();
                    if (size.getText().toString().isEmpty()) {
                        size_string = "null";
                    } else
                        size_string = size.getText().toString();

                    databaseHelperForNotes.insertData_shoppingItem(name_string, quant_string, size_string, price_string);

                    Intent addedItem = new Intent(addItem.this, Lists_shopping.class);
                    startActivity(addedItem);

                }
            }
        });
    }


}