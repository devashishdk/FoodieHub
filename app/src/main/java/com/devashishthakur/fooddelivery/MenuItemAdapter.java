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

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.TestViewHolder>{

    Context mCtx;
    List<MenuItem> menuItemsList;

    MenuItemAdapter(Context mCtx,List<MenuItem> menuItemsList)
    {
        this.mCtx = mCtx;
        this.menuItemsList = menuItemsList;
    }
    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.single_menu_item,
                parent, false);
        TestViewHolder testViewHolder = new TestViewHolder(view);
        return testViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        MenuItem menuItem = menuItemsList.get(position);

        holder.item.setText(menuItem.getItem());
        holder.price.setText(menuItem.getPrice() + " ₹");

        /*
        final String PushId = categoriesList.get(position).getPush_id();
        holder.card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(mCtx, MainActivity.class);
                mIntent.putExtra("Key", PushId);
                mCtx.startActivity(mIntent);
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return menuItemsList.size();
    }

    class TestViewHolder extends RecyclerView.ViewHolder
    {
        TextView item,price;
        CardView card_layout;
        public TestViewHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.menu_item_name);
            price = (TextView) itemView.findViewById(R.id.menu_item_price);
            card_layout = (CardView) itemView.findViewById(R.id.card_layout);
        }
    }
}
