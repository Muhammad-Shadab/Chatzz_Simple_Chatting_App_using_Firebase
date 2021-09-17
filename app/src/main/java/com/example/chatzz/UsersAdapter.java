package com.example.chatzz;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.ContentCaptureCondition;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    FirebaseDatabase database;
    DatabaseReference reference;

    List<String> userList;
    String username;
    Context mContext;

    public UsersAdapter(List<String> userList, String username, Context mContext) {
        this.userList = userList;
        this.username = username;
        this.mContext = mContext;

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card_view,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        reference.child("User").child(userList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String othername = snapshot.child("username").getValue().toString();
                String imageURL = snapshot.child("image").getValue().toString();

                holder.textViewMainActivity.setText(othername);

                if (imageURL.equals("null"))
                {
                    holder.circleImageViewMainActivity.setImageResource(R.drawable.account_circle);
                }
                else
                {
                    Picasso.get().load(imageURL).into(holder.circleImageViewMainActivity);
                }
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, MyChatActivity.class);
                        intent.putExtra("username",username);
                        intent.putExtra("othername",othername);
                        mContext.startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageViewMainActivity;
        TextView textViewMainActivity;
        CardView cardView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageViewMainActivity = itemView.findViewById(R.id.circleImageViewMainActivity);
            textViewMainActivity = itemView.findViewById(R.id.textViewMainActivity);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}
