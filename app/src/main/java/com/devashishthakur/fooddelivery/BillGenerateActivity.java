package com.devashishthakur.fooddelivery;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class BillGenerateActivity extends AppCompatActivity {

    Button addView,clearView,proceedView;
    Button proceed;
    TextView tv;
    LinearLayout linearLayout;
    Spinner itemList,itemNo;
    TextView totalPriceView;
    int totalPrice = 0;
    MyTextView currentPrice;
    HashMap<String,HashMap<String,String>> hashBill;

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/font_app.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_generate);

        //Declaring two spinner's

        itemList = (Spinner) findViewById(R.id.itemList);
        itemNo = (Spinner) findViewById(R.id.itemNo);

        //Getting shop id passed from last activity

        final String shopId = getIntent().getStringExtra("shopid");

        proceed = (Button) findViewById(R.id.proceed_alter);

        currentPrice = (MyTextView) findViewById(R.id.currentPrice);
        totalPriceView = (TextView) findViewById(R.id.totalPriceView);

        //Getting arraylist passed from last activity

        ArrayList<String> arrayList = getIntent().getStringArrayListExtra("productName");
        ArrayList<String> productPrice = getIntent().getStringArrayListExtra("productPrice");

        hashBill = new HashMap<String, HashMap<String,String>>();

        final HashMap<String,Integer> hashList = new HashMap<String, Integer>();

        //Generating a hashList of ("Item" : "Price")
        proceedView = (Button) findViewById(R.id.proceed);

        Iterator<String> itName= arrayList.iterator();
        Iterator<String> itPrice= productPrice.iterator();

        while(itName.hasNext() && itPrice.hasNext())
        {
            hashList.put(itName.next().toString(),Integer.valueOf(itPrice.next().toString()));
        }

        //Adding Count

        ArrayList<String> arrayListP = new ArrayList<String>();

        arrayListP.add("1");
        arrayListP.add("2");
        arrayListP.add("3");
        arrayListP.add("4");

        //Populating Spinner

        MySpinnerAdapter adapterSpinner = new MySpinnerAdapter(this,R.layout.spinner_row, arrayList);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        itemList.setAdapter(adapterSpinner);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_row, arrayListP);
        itemNo.setAdapter(adapter1);

        //This line was useless : final TextView[] myTextViews = new TextView[10];

        addView = (Button) findViewById(R.id.addView);
        clearView = (Button) findViewById(R.id.clearView);
        linearLayout = (LinearLayout) findViewById(R.id.linear_list);

        //On item selected set current Price

        itemList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentPrice.setText(hashList.get(itemList.getSelectedItem().toString()).toString() + " ₹");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Adding views Dynamically
        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTextView rowTextView = new MyTextView(BillGenerateActivity.this);

                String item = itemList.getSelectedItem().toString();
                int price = 0;
                // set some properties of rowTextView or something
                try{
                    price = Integer.valueOf(itemNo.getSelectedItem().toString()) * hashList.get(itemList.getSelectedItem().toString());
                }
                catch(Exception e)
                {

                }
                rowTextView.setText(item + "   X   " + itemNo.getSelectedItem().toString() + "  =  " + hashList.get(item.toString()) * Integer.parseInt(itemNo.getSelectedItem().toString()) + " ₹");
                rowTextView.setTextSize(18);
                rowTextView.setTextColor(Color.BLACK);
                totalPrice = totalPrice + price;

                totalPriceView.setText(String.valueOf(totalPrice) + " ₹");

                HashMap<String,String> hash = new HashMap<String, String>();

                if(hashBill.containsKey(item))
                {
                    hash = hashBill.get(item);
                    int priceItem = Integer.parseInt(hash.get("price"));
                    int quantItem = Integer.parseInt(hash.get("quant"));
                    priceItem = priceItem + price;
                    quantItem = quantItem + Integer.parseInt(itemNo.getSelectedItem().toString());
                    hash.put("item",item);
                    hash.put("quant",String.valueOf(quantItem));
                    hash.put("price",String.valueOf(priceItem));
                    hashBill.put(item,hash);
                }
                else {
                    hash.put("item",item);
                    hash.put("quant",itemNo.getSelectedItem().toString());
                    hash.put("price",String.valueOf(price));
                    hashBill.put(item, hash);
                }
                // add the textview to the linearlayout
                linearLayout.addView(rowTextView);
            }
        });
        clearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayout.removeAllViews();
                totalPrice = 0;
                hashBill.clear();
                totalPriceView.setText("0");
            }
        });
        //On proceed pass the Bill and shop ID to next activity
        proceedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BillGenerateActivity.this,OrderDetailActivity.class);
                intent.putExtra("shopid",shopId);
                intent.putExtra("map",hashBill);
                startActivity(intent);
                finish();
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BillGenerateActivity.this,OrderDetailActivity.class);
                intent.putExtra("shopid",shopId);
                intent.putExtra("map",hashBill);
                startActivity(intent);
                finish();
            }
        });
    }
}
