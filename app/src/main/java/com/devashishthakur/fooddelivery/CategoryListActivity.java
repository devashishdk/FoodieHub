package com.devashishthakur.fooddelivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryListActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    RecyclerView mShopList;//,mCategoryList;
    List<Shop> ShopList;
    ShopAdapterSmall shopAdapter;
    //List<Categories> CategoryList;
    //CategoriesAdapter categoryAdapter;

    ProgressDialog pd;

    private FirebaseAuth mAuth;

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/font_app.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_shop);

        mAuth = FirebaseAuth.getInstance();

        setUpToolBar();

        //ProgressDialog launched at start of the activity

        String pushid = getIntent().getStringExtra("key");
        pd = new ProgressDialog(CategoryListActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(true);
        pd.setTitle("Loading....");
        pd.setMessage("Please Wait");
        pd.show();

        //NavigationDrawer OnClickListener

        navigationView = (NavigationView) findViewById(R.id.navigationView);

        navigationView.setItemIconTintList(null);
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case (R.id.home):
                        drawerLayout.closeDrawers();
                        Intent intent = new Intent(CategoryListActivity.this,CategoryListActivity.class);
                        startActivity(intent);
                        finish();
                        //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case (R.id.contact):
                        drawerLayout.closeDrawers();
                        Intent intent2 = new Intent(CategoryListActivity.this,LoginActivity.class);
                        startActivity(intent2);
                        //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case (R.id.profile):
                        drawerLayout.closeDrawers();
                        Intent intentPro = new Intent(CategoryListActivity.this,ProfileActivity.class);
                        startActivity(intentPro);
                        //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case (R.id.Orders):
                        drawerLayout.closeDrawers();
                        Intent intentOrder = new Intent(CategoryListActivity.this,OrdersActivity.class);
                        startActivity(intentOrder);
                        //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;

                }
                return false;
            }
        });


        //Populating RecyclerView With ShopList
        mShopList = (RecyclerView) findViewById(R.id.shop_list);
        mShopList.setHasFixedSize(true);
        mShopList.setLayoutManager(new LinearLayoutManager(this));
        ShopList = new ArrayList<>();
        DatabaseReference dbProducts = FirebaseDatabase.getInstance().getReference("categories").child(pushid).child("shops");
        dbProducts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for(DataSnapshot productSnapshot : dataSnapshot.getChildren()){
                        Shop p = productSnapshot.getValue(Shop.class);
                        ShopList.add(p);
                    }

                    shopAdapter = new ShopAdapterSmall(CategoryListActivity.this, ShopList);
                    mShopList.setAdapter(shopAdapter);

                    //If ProgressDialog is showing Dismiss it
                    if(pd.isShowing())
                    {
                        pd.dismiss();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Populating Category RecyclerView

        /*

        mCategoryList = (RecyclerView) findViewById(R.id.category_list);
        mCategoryList.setHasFixedSize(true);
        mCategoryList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        CategoryList = new ArrayList<>();

        DatabaseReference dbCategories = FirebaseDatabase.getInstance().getReference("categories");
        dbCategories.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for(DataSnapshot productSnapshot : dataSnapshot.getChildren()){
                        Categories p = productSnapshot.getValue(Categories.class);
                        CategoryList.add(p);
                    }

                    categoryAdapter = new CategoriesAdapter(CategoryListActivity.this, CategoryList);
                    mCategoryList.setAdapter(categoryAdapter);

                    //If ProgressDialog is showing Dismiss it
                    if(pd.isShowing())
                    {
                        pd.dismiss();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
    }


    //Options Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case (R.id.logout):
                drawerLayout.closeDrawers();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CategoryListActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case (R.id.settings):
                drawerLayout.closeDrawers();
                Intent intentP = new Intent(CategoryListActivity.this,ProfileActivity.class);
                startActivity(intentP);
                break;
        }
        return true;
    }

    //Setting ToolBar

    void setUpToolBar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBarDrawerToggle =  new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null)
        {
            pd.dismiss();
            Intent startIntent = new Intent(CategoryListActivity.this,LoginActivity.class);
            startActivity(startIntent);
            finish();
        }
    }
}

