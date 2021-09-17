package com.example.chatzz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyChatActivity extends AppCompatActivity {

    ImageView imageViewBackArrow;
    TextView textViewOtherUsername;
    EditText editTextTextMultiLineMyChatActivity;
    FloatingActionButton fab;
    RecyclerView rvChat;

    FirebaseDatabase database;
    DatabaseReference reference;

    String username, othername;

    MessageAdapter adapter;
    List<ModelClass> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chat);

        imageViewBackArrow = findViewById(R.id.imageViewBackArrow);
        textViewOtherUsername = findViewById(R.id.textViewOtherUsername);
        editTextTextMultiLineMyChatActivity = findViewById(R.id.editTextTextMultiLineMyChatActivity);
        fab = findViewById(R.id.fab);
        rvChat = findViewById(R.id.rvChat);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        rvChat.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        username = getIntent().getStringExtra("username");
        othername = getIntent().getStringExtra("othername");

        textViewOtherUsername.setText(othername);

        imageViewBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyChatActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editTextTextMultiLineMyChatActivity.getText().toString();
                if (!message.equals(""))
                {
                    sendMessage(message);
                    editTextTextMultiLineMyChatActivity.setText("");
                }
            }
        });
        getMessage();


    }
    public void sendMessage(String message)
    {
        String key = reference.child("Messages").child(username).child(othername).push().getKey();
        Map<String, Object> map = new HashMap<>();
        map.put("message",message);
        map.put("from",username);
        reference.child("Messages").child(username).child(othername).child(key).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    reference.child("Messages").child(othername).child(username).child(key).setValue(map);
                }
            }
        });
    }
    public void getMessage(){
         reference.child("Messages").child(username).child(othername).addChildEventListener(new ChildEventListener() {
             @Override
             public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ModelClass modelClass = snapshot.getValue(ModelClass.class);
                list.add(modelClass);
                adapter.notifyDataSetChanged();
                 rvChat.scrollToPosition(list.size() - 1);

             }

             @Override
             public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

             }

             @Override
             public void onChildRemoved(@NonNull DataSnapshot snapshot) {

             }

             @Override
             public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
         adapter = new MessageAdapter(list,username);
         rvChat.setAdapter(adapter);
    }
}