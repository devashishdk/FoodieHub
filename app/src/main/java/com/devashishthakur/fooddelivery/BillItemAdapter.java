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

public class BillItemAdapter extends RecyclerView.Adapter<BillItemAdapter.TestViewHolder>{

    Context mCtx;
    List<BillItem> BillItemsList;

    BillItemAdapter(Context mCtx,List<BillItem> BillItemsList)
    {
        this.mCtx = mCtx;
        this.BillItemsList = BillItemsList;
    }
    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.single_bill_display,
                parent, false);
        TestViewHolder testViewHolder = new TestViewHolder(view);
        return testViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        BillItem BillItem = BillItemsList.get(position);
        holder.item.setText(BillItem.getItem());
        holder.price.setText(BillItem.getPrice() + " ₹");
        holder.quant.setText(BillItem.getQuant());
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
        return BillItemsList.size();
    }

    class TestViewHolder extends RecyclerView.ViewHolder
    {
        TextView item,price,quant;
        CardView card_layout;
        public TestViewHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.billitemname);
            price = (TextView) itemView.findViewById(R.id.billitemprice);
            quant = (TextView) itemView.findViewById(R.id.billitemquantity);
            card_layout = (CardView) itemView.findViewById(R.id.card_layout);
        }
    }
}
