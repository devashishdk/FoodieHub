package com.devashishthakur.fooddelivery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{

    Context mCtx;
    List<Order> OrderList;

    OrderAdapter(Context mCtx,List<Order> OrderList)
    {
        this.mCtx = mCtx;
        this.OrderList = OrderList;
    }
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.single_layout_order,
                parent, false);
        OrderViewHolder OrderViewHolder = new OrderViewHolder(view);
        return OrderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, final int position) {
        final Order Order = OrderList.get(position);
        holder.phone.setText(Order.getPhone());
        holder.room.setText(Order.getRoom());
        holder.hostel.setText(Order.getHostel());
        final String user_id = Order.getuserid();
        final String shopid = Order.getShopid();

        String status = Order.getStatus().toString();

        if(status.equals("completed")) {
            holder.status.setText("Completed");
            holder.status.setTextColor(Color.parseColor("#2ECC71"));

            //holder.cancelButton.setBackgroundTintList(mCtx.getResources().getColorStateList(R.color.color_list));
            holder.cancelButton.setVisibility(View.GONE);
            //holder.callButton.setBackgroundTintList(mCtx.getResources().getColorStateList(R.color.color_list));
            holder.callButton.setVisibility(View.GONE);
            holder.ratingButton.setVisibility(View.VISIBLE);
            holder.billButton.setBackgroundTintList(mCtx.getResources().getColorStateList(R.color.color_list));
            //holder.billButton.setClickable(false);
        }

        else if(status.equals("canceled")){
            holder.status.setText("Canceled By Show Owner");
            holder.status.setTextColor(Color.parseColor("#ef5350"));

            //holder.cancelButton.setBackgroundTintList(mCtx.getResources().getColorStateList(R.color.color_list));
            holder.cancelButton.setVisibility(View.GONE);
            //holder.callButton.setBackgroundTintList(mCtx.getResources().getColorStateList(R.color.color_list));
            holder.callButton.setVisibility(View.GONE);
            holder.billButton.setBackgroundTintList(mCtx.getResources().getColorStateList(R.color.color_list));
            //holder.billButton.setClickable(false);
        }
        else {
            holder.status.setText("Pending");
            holder.status.setTextColor(Color.parseColor("#ffd61d"));

        }

        final DatabaseReference dbShop = FirebaseDatabase.getInstance().getReference("Shops").child(shopid);
        final DatabaseReference dbUser = FirebaseDatabase.getInstance().getReference("Users").child(user_id);

        //Maintaining a hashmap of all details of user
        dbShop.child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                Picasso.with(mCtx).load(snapshot.getValue().toString()).placeholder(R.drawable.profile).into(holder.imageView);
                //personName.getEditText().setText(snapshot.getValue().toString());
                //prints "Do you have data? You'll love Firebase."
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        dbShop.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.child("name").getValue());
                holder.name.setText(snapshot.child("name").getValue().toString());

                holder.phone.setText(snapshot.child("phoneno").getValue().toString());
                //Picasso.with(mCtx).load(snapshot.getValue().toString()).placeholder(R.drawable.profile).into(holder.imageView);
                //personName.getEditText().setText(snapshot.getValue().toString());
                //prints "Do you have data? You'll love Firebase."
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //Picasso.with(mCtx).load(image).placeholder(R.drawable.shopone).into(holder.imageView);

        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = Order.getPhone().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                mCtx.startActivity(intent);
            }
        });

        holder.billButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx,BillDisplayActivity.class);
                intent.putExtra("userid",user_id);
                intent.putExtra("orderid",Order.getOrderid().toString());
                mCtx.startActivity(intent);
            }
        });

        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setMessage("Do you really wanna cancel your Order?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dbUser.child("orders").child(Order.getOrderid()).removeValue();
                        holder.cancelButton.setText("CANCELED");
                        holder.cancelButton.setBackgroundTintList(mCtx.getResources().getColorStateList(R.color.color_list));
                        holder.cancelButton.setClickable(false);
                        holder.callButton.setBackgroundTintList(mCtx.getResources().getColorStateList(R.color.color_list));
                        holder.callButton.setClickable(false);
                        holder.billButton.setBackgroundTintList(mCtx.getResources().getColorStateList(R.color.color_list));
                        holder.billButton.setClickable(false);

                    }
                }).setNegativeButton("cancel",null);

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        holder.ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx,RatingActivity.class);
                intent.putExtra("shopID",shopid);
                intent.putExtra("userID",user_id);
                mCtx.startActivity(intent);
            }
        });

        /*
        final String PushId = OrderList.get(position).getId();

        holder.card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(mCtx, OrderMenuActivity.class);
                mIntent.putExtra("Orderid",String.valueOf(PushId));
                mCtx.startActivity(mIntent);
            }
        });

*/
    }

    @Override
    public int getItemCount() {
        return OrderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView,heartImage;
        TextView name,hostel,room,phone,status;
        CardView card_layout;
        //AppCompatRatingBar ratingBar;
        Button callButton,billButton,cancelButton,ratingButton;
        public OrderViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.Order_image);

            name = (TextView) itemView.findViewById(R.id.person_name);
            hostel = (TextView) itemView.findViewById(R.id.person_hostel);
            room = (TextView) itemView.findViewById(R.id.person_room);
            phone = (TextView) itemView.findViewById(R.id.person_number);
            status = (TextView) itemView.findViewById(R.id.status);
            //rating = (TextView) itemView.findViewById(R.id.Order_rating);
            card_layout = (CardView) itemView.findViewById(R.id.card_layout);
            callButton = (Button) itemView.findViewById(R.id.callButton);
            billButton = (Button) itemView.findViewById(R.id.billButton);
            cancelButton = (Button) itemView.findViewById(R.id.cancelButton);
            ratingButton = (Button) itemView.findViewById(R.id.rateButton);
            //ratingBar = (AppCompatRatingBar) itemView.findViewById(R.id.ratingBarMain);
        }
    }
}
