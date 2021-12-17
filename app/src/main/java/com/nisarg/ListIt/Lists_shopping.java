package com.nisarg.ListIt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Lists_shopping extends AppCompatActivity {

    ArrayList<cardHanlder_shoppingCard> shopping_list;
    private RecyclerView recyclerView;
    private cardAdapterShoppingElement adapter;
    private LinearLayout totalLayout;
    private LinearLayout list_shopping_main_view;
    private DatabaseHelperForNotes databaseHelperForNotes;
    private RecyclerView.LayoutManager layoutManager;
    private TextView textViewForNoList, total;
    private ImageView coconutImage;
    private WebView printWeb;
    private PrintJob printJob;
    private boolean printButtonPressed = false;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists_shopping);

        list_shopping_main_view = findViewById(R.id.layout_for_shopping_litsts);
        totalLayout = findViewById(R.id.total_layout);
        textViewForNoList = findViewById(R.id.textView_for_no_shoppingList);
        coconutImage = findViewById(R.id.coconut_shoppinglist);
        shopping_list = new ArrayList<>();
        printWeb = new WebView(Lists_shopping.this);
        WebView webView = findViewById(R.id.webViewList);
        webView.getSettings().setJavaScriptEnabled(true);
        printWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // initializing the printWeb Object
                printWeb = webView;
            }
        });
        total = findViewById(R.id.total);

        Toolbar toolbar = findViewById(R.id.toolbar_shopping_list);
        recyclerView = findViewById(R.id.view_for_shopping_list);
        layoutManager = new LinearLayoutManager(Lists_shopping.this);
        databaseHelperForNotes = new DatabaseHelperForNotes(Lists_shopping.this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //inflate_This_Item("honey",12.12,"500 gm",2);
        setRecyclerViewScrollListener();
        getShoppingItems();
        CalculateTotal();

    }

    private void setRecyclerViewScrollListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener((view, x, y, oldX, oldY) -> {
                int scroll = y - oldY;
                if (scroll > 0) {
                    totalLayout.setVisibility(View.GONE);
                } else if (scroll < 0) {
                    totalLayout.setVisibility(View.VISIBLE);
                } else {
                    totalLayout.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void CalculateTotal() {
        float totalOfPrice = 0;
        for (cardHanlder_shoppingCard c : shopping_list) {
            totalOfPrice += Integer.parseInt(c.getmQuant()) * Double.parseDouble(c.getmPrice());
        }
        total.append(String.valueOf(totalOfPrice));
    }


    private void getShoppingItems() {
        String name, quant, size, price;
        int id;
        Cursor cursor;
        cursor = databaseHelperForNotes.viewDefault_shoppingItem();
        if (cursor == null || cursor.getCount() <= 0) {
            textViewForNoList.setVisibility(View.VISIBLE);
            coconutImage.setVisibility(View.VISIBLE);
        } else {
            totalLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            cursor.moveToLast();
            do {
                name = cursor.getString(cursor.getColumnIndex("NAME"));
                quant = cursor.getString(cursor.getColumnIndex("QUANT"));
                size = cursor.getString(cursor.getColumnIndex("SIZE"));
                price = cursor.getString(cursor.getColumnIndex("PRICE"));
                id = cursor.getInt(cursor.getColumnIndex("_id"));

                if (size.equalsIgnoreCase("null")) {
                    size = "--";
                }
                if (quant.equalsIgnoreCase("null")) {
                    quant = "1";
                }
                inflate_This_Item(id, name, Double.parseDouble(price), size, Integer.parseInt(quant), getRandomColor());
                cursor.moveToPrevious();
            } while (!cursor.isBeforeFirst());

            configureRecyclerView();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("CALLED", "OnActivity Result");
        if (requestCode == 10 || requestCode == 50) {
            recreate();
        }
    }

    private void inflate_This_Item(int id, String name, double price, String size, int quantity, int color) {
        shopping_list.add(new cardHanlder_shoppingCard(name, quantity, price, size, color, id));
    }

    private void configureRecyclerView() {
        //configure adapter
        recyclerView = findViewById(R.id.view_for_shopping_list);
        //performance
        recyclerView.setHasFixedSize(true);

        adapter = new cardAdapterShoppingElement(shopping_list);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setmListener(new cardAdapterShoppingElement.OnItemClickListener() {
            @Override
            public void singleClicked(cardAdapterShoppingElement.cardViewHolder holder, cardHanlder_shoppingCard card) {
                Intent intentOfUpdate = new Intent(Lists_shopping.this, edit_product.class);

                intentOfUpdate.putExtra("Name", card.getmItemName());
                intentOfUpdate.putExtra("id", card.getId());
                intentOfUpdate.putExtra("quant", card.getmQuant());
                intentOfUpdate.putExtra("size", card.getmSize());
                intentOfUpdate.putExtra("price", card.getmPrice());
                startActivity(intentOfUpdate);
            }

        });

    }

    private void getAndInflateShoppingList() {
        textViewForNoList.setVisibility(View.VISIBLE);
        coconutImage.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(Lists_shopping.this, MainActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.add_product) {
            Intent newList = new Intent(Lists_shopping.this, addItem.class);
            startActivity(newList);
            return true;
        } else if (item.getItemId() == R.id.Print) {
            String htmlDocument = createWebViewOfList();
            printWeb.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null);
            printWebPage(printWeb);
            return true;
        } else if (item.getItemId() == R.id.Delete_product_list) {
            new MaterialAlertDialogBuilder(Lists_shopping.this, R.style.CustomMaterialDialog).setTitle("Delete the entire list?")
                    .setIcon(R.drawable.warning)
                    .setPositiveButton(
                            "Yes", (dialogInterface, i) -> {
                                int key_deleted = databaseHelperForNotes.delete_all_items();
                                recreate();
                            }
                    ).setNegativeButton("No", (dialogInterface, i) -> {
            }).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.productlistmainmenu, menu);
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void printWebPage(WebView webView) {
        printButtonPressed = true;
        PrintManager printManager = (PrintManager) this
                .getSystemService(Lists_shopping.PRINT_SERVICE);

        // setting the name of job
        String jobName = getString(R.string.app_name) + "_Product_list ";

        // Creating  PrintDocumentAdapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);

        // Create a print job with name and adapter instance
        assert printManager != null;
        printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (printJob != null && printButtonPressed) {
            if (printJob.isCompleted()) {
                // Showing Toast Message
                showToast("PDF saved..");

            } else if (printJob.isStarted()) {
                // Showing Toast Message
                showToast("isStarted");


            } else if (printJob.isBlocked()) {
                // Showing Toast Message
                showToast("isBlocked");


            } else if (printJob.isCancelled()) {
                // Showing Toast Message
                showToast("PDF was not generated..");


            } else if (printJob.isFailed()) {
                // Showing Toast Message
                showToast("PDF generation failed..");


            } else if (printJob.isQueued()) {
                // Showing Toast Message
                showToast("PDF queued..");

            }
            // set printBtnPressed false
            printButtonPressed = false;
        }
    }


    private String createWebViewOfList() {
        StringBuilder htmlDoc = new StringBuilder("<html><body><h2>" + "Product list </h2>" + "<br>" +
                "<br>" + "<p>Date : " + Html.escapeHtml(getDateTime()) + "</p>" +
                "<style>" +

                "p{ text-align:right;}" +
                //Styles for border,header,table data
                "h3, h2, h4{ text-align:center;}" +
                "table, th, td {" +
                "border: 1px solid black;" +
                "border-collapse: collapse;" +
                "text-align: center" +
                "}" +


                //Padding for table header,table data
                "th, td {" +
                "padding: 15px;" +
                "}" +

                "td.Border {" +
                "border:none;" +
                "width:60%;" +
                "padding:8px;" +
                "}" +

                "table.Border {" +
                "border:none;" +
                "}" +
                //Align style for table header
                "th {" +
                "text-align: center;" +
                "}" +

                "</style>" +
                "<table style=\"width:100%\">" +

                //Table Headers 'Price','Quantity','Total'
                "<tr>" +
                "<th>Name</th>" +
                "<th>Price</th>" +
                "<th>Quantity</th>" +
                "<th>Size/Weight</th>" +
                "</tr>");

        //S.No and Date inside non border table

        for (cardHanlder_shoppingCard c : shopping_list) {

            String name = c.getmItemName();
            String price = c.getmPrice();
            String quant = c.getmQuant();
            String size = c.getmSize();

            htmlDoc.append("<tr>").append("<td>" + Html.escapeHtml(name) + "</td>").append("<td>" + Html.escapeHtml(price) + "</td>").append("<td>" + Html.escapeHtml(quant) + "</td>").append("<td>" + Html.escapeHtml(size) + "</td>").append("</tr>");

        }
        htmlDoc.append("</table>" + "<br>" + "<br>").append("<h3>").append(Html.escapeHtml(total.getText())).append("</h3>");
        htmlDoc.append("</body></html>");

        return htmlDoc.toString();
    }

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
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

}