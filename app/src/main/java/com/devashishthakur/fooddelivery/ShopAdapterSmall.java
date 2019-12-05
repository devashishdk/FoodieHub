package com.devashishthakur.fooddelivery;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devashishthakur.fooddelivery.MainActivity;
import com.devashishthakur.fooddelivery.R;
import com.devashishthakur.fooddelivery.Shop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShopAdapterSmall extends RecyclerView.Adapter<ShopAdapterSmall.ShopViewHolder>{

    Context mCtx;
    List<Shop> ShopList;
    String canLike = "1";
    String shopFav = " ";

    double mRating = 0,avRating = 0;

    ShopAdapterSmall(Context mCtx,List<Shop> ShopList)
    {
        this.mCtx = mCtx;
        this.ShopList = ShopList;
    }
    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.single_shop_small,
                parent, false);
        ShopViewHolder ShopViewHolder = new ShopViewHolder(view);
        return ShopViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ShopViewHolder holder, int position) {
        final Shop Shop = ShopList.get(position);

        holder.name.setText(Shop.getName());
        holder.rating.setText(Shop.getRating());
        //holder.ratingBar.setRating(Float.valueOf(Shop.getRating()));
        holder.add.setText(Shop.getShopaddress());
        holder.heartCount.setText(Shop.getLikes());
        holder.shopTiming.setText(Shop.getTimings());

        String image = Shop.getImage();
        Picasso.with(mCtx).load(image).placeholder(R.drawable.shopone).into(holder.imageView);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Getting User UID

        final String user_id = firebaseUser.getUid().toString();

        final DatabaseReference dbProducts = FirebaseDatabase.getInstance().getReference("Shops");
        final DatabaseReference dbUser = FirebaseDatabase.getInstance().getReference("Users").child(user_id);



        dbProducts.child(Shop.getId()).child("ratinglist").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for(DataSnapshot productSnapshot : dataSnapshot.getChildren()){
                        Shop p = productSnapshot.getValue(Shop.class);

                        String rating = productSnapshot.child("rating").getValue().toString();
                        mRating = mRating + Double.valueOf(rating);
                    }
                    avRating = mRating / dataSnapshot.getChildrenCount();
                    holder.rating.setText(String.valueOf(avRating));
                    holder.ratingBar.setRating(Float.valueOf(String.valueOf(avRating)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dbUser.child("favshop").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                try {
                    shopFav = snapshot.getValue().toString();
                    if (shopFav.equals(Shop.getId())) {
                        holder.likesView.setBackgroundResource(R.drawable.heart_red);
                    } else
                        holder.likesView.setBackgroundResource(R.drawable.heart);
                }
                catch (Exception e)
                {

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        final String PushId = ShopList.get(position).getId();
        holder.likesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbUser.child("canlike").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        canLike = snapshot.getValue().toString();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                if(canLike.equals("1"))
                {
                    dbUser.child("canlike").setValue("0");

                    String val = String.valueOf(Integer.parseInt(Shop.getLikes().toString()) + 1);
                    dbProducts.child(Shop.getId()).child("likes").setValue(val);
                    dbUser.child("favshop").setValue(Shop.getId().toString());
                    holder.likesView.setBackgroundResource(R.drawable.heart_red);
                    holder.heartCount.setText(val);
                }
                else
                {
                    if (shopFav.equals(Shop.getId())) {

                        int valS = Integer.parseInt(Shop.getLikes().toString());
                        holder.likesView.setBackgroundResource(R.drawable.heart);
                        dbUser.child("canlike").setValue("1");
                        dbProducts.child(Shop.getId()).child("likes").setValue(String.valueOf(valS));
                        dbUser.child("favshop").setValue(" ");
                        holder.heartCount.setText(String.valueOf(valS));

                    }
                    else {

                    }
                }
            }
        });

        //On click card move to ShopMenu
        holder.card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(mCtx, ShopMenuActivity.class);
                //Getting push id of the shop by getID() function
                mIntent.putExtra("shopid",String.valueOf(PushId));
                mCtx.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ShopList.size();
    }

    class ShopViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView,likesView;
        TextView name,rating,add,shopTiming,heartCount;
        CardView card_layout;
        AppCompatRatingBar ratingBar;

        public ShopViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.shop_image);
            likesView = (ImageView) itemView.findViewById(R.id.heart_image);
            name = (TextView) itemView.findViewById(R.id.shop_name);
            add = (TextView) itemView.findViewById(R.id.shop_add);
            shopTiming = (TextView) itemView.findViewById(R.id.shop_timing);
            heartCount = (TextView) itemView.findViewById(R.id.heart_count);

            rating = (TextView) itemView.findViewById(R.id.shop_rating);
            card_layout = (CardView) itemView.findViewById(R.id.card_layout);
            ratingBar = (AppCompatRatingBar) itemView.findViewById(R.id.ratingBarMain);
        }
    }
}
