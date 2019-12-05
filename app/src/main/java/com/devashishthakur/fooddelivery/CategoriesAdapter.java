package com.devashishthakur.fooddelivery;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.TestViewHolder>{

    Context mCtx;
    List<Categories> categoriesList;

    CategoriesAdapter(Context mCtx,List<Categories> categoriesList)
    {
        this.mCtx = mCtx;
        this.categoriesList = categoriesList;
    }
    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.single_category,
                parent, false);
        TestViewHolder testViewHolder = new TestViewHolder(view);
        return testViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        Categories categories = categoriesList.get(position);

        holder.title.setText(categories.getName());
        String image = categories.getImage();

        //Picasso.with(mCtx).load(image).placeholder(R.drawable.shopone).into(holder.imgbg);


        /*
        final String PushId = categories.getId().toString();
        holder.card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(mCtx, CategoryListActivity.class);
                mIntent.putExtra("key", PushId);
                mCtx.startActivity(mIntent);
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    class TestViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        CardView card_layout;
        //ImageView imgbg;
        public TestViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.category_name);
            card_layout = (CardView) itemView.findViewById(R.id.category);
            //imgbg = (ImageView) itemView.findViewById(R.id.image_bg);
        }
    }
}
