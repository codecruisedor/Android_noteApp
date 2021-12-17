package com.nisarg.ListIt;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

public class edit_product extends AppCompatActivity {
    Toolbar toolbar;
    TextInputEditText nameEditText, quantEditText, sizeEditText, priceEditText;
    String name, size, price, quantity;
    int id;
    DatabaseHelperForNotes databaseHelperForNotes;
    ImageView imageViewDelete;
    ExtendedFloatingActionButton done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        toolbar = findViewById(R.id.toolbar_edit_item);
        nameEditText = findViewById(R.id.product_name_edit);
        quantEditText = findViewById(R.id.product_quant_edit);
        sizeEditText = findViewById(R.id.product_size_edit);
        priceEditText = findViewById(R.id.product_price_edit);
        imageViewDelete = findViewById(R.id.delete_product);
        done = findViewById(R.id.extendedFloatingActionButton_done_item_edit);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        databaseHelperForNotes = new DatabaseHelperForNotes(edit_product.this);
        populateData();
        editDone();

    }

    private void editDone() {
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameEditText.getText().toString().isEmpty() || priceEditText.getText().toString().isEmpty()) {
                    Toast.makeText(edit_product.this, "Name or price cant be empty..", Toast.LENGTH_SHORT).show();
                } else {
                    if (quantEditText.getText().toString().isEmpty()) {
                        quantity = "null";
                    } else
                        quantity = quantEditText.getText().toString();
                    if (sizeEditText.getText().toString().isEmpty()) {
                        size = "null";
                    } else
                        size = sizeEditText.getText().toString();
                    databaseHelperForNotes.update_item(id, nameEditText.getText().toString(), priceEditText.getText().toString(), quantity, size);
                    Intent intentOfupdate = new Intent(edit_product.this, Lists_shopping.class);
                    startActivity(intentOfupdate);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_product, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            //noinspection RestrictedApi
            m.setOptionalIconsVisible(true);

        }

        return true;

    }

    private void populateData() {
        name = getIntent().getStringExtra("Name");
        quantity = getIntent().getStringExtra("quant");
        size = getIntent().getStringExtra("size");
        price = getIntent().getStringExtra("price");
        id = getIntent().getIntExtra("id", 0);

        nameEditText.setText(name);
        quantEditText.setText(quantity);
        sizeEditText.setText(size);
        priceEditText.setText(price);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(edit_product.this, Lists_shopping.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.delete_product) {
            new MaterialAlertDialogBuilder(edit_product.this, R.style.CustomMaterialDialog).setTitle("Delete this product?")
                    .setIcon(R.drawable.warning)
                    .setPositiveButton(
                            "Yes", (dialogInterface, i) -> {
                                int key_deleted = databaseHelperForNotes.delete_item(id);
                                Intent delete_intent = new Intent(edit_product.this, Lists_shopping.class);
                                startActivity(delete_intent);
                            }
                    ).setNegativeButton("No", (dialogInterface, i) -> {

            }).show();
        }

        return super.onOptionsItemSelected(item);
    }
}