package com.example.chatzz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileUpdate extends AppCompatActivity {

    CircleImageView imageViewProfileUpdate;
    TextInputEditText textInputusernameUpdate;
    Button buttonUpdateProfile;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    boolean imageControl = false;
    Uri imageUri;

    String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_update2);

        imageViewProfileUpdate = findViewById(R.id.imageViewProfileUpdate);
        textInputusernameUpdate = findViewById(R.id.textInputusernameUpdate);
        buttonUpdateProfile = findViewById(R.id.buttonUpdateProfile);

        showData();

        imageViewProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = textInputusernameUpdate.getText().toString();
                updateUserProfile(username);
            }
        });

    }

    public void showData()
    {
        reference.child("User").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue().toString();
                textInputusernameUpdate.setText(username);
                imageURL = snapshot.child("image").getValue().toString();
                if (imageURL.equals("null"))
                {
                    imageViewProfileUpdate.setImageResource(R.drawable.account_circle);
                }
                else
                {
                    Picasso.get().load(imageURL).into(imageViewProfileUpdate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void imageChooser() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imageViewProfileUpdate);
            imageControl = true;
        } else {
            imageControl = false;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void updateUserProfile(String username)
    {
        reference.child("User").child(user.getUid()).child("username").setValue(username);

        if (imageControl) {
            UUID randomID = UUID.randomUUID();
            String imageName = "image"+randomID+".jpg";
            storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference myStorageRef = firebaseStorage.getReference(imageName);
                    myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String filPath = uri.toString();
                            reference.child("User").child(auth.getUid()).child("image").setValue(filPath).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), ".", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), ".", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
        }
        else {
            reference.child("User").child(auth.getUid()).child("image").setValue(imageURL);
        }
        Intent i = new Intent(UserProfileUpdate.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}