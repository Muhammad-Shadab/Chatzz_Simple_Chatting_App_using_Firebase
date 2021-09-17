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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText emailSignUp, passwordSignUp, usernameSignUp;
    Button signUp;
    CircleImageView imageProfileSignUp;

    Uri imageUri;
    boolean imageControl = false;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        imageProfileSignUp = findViewById(R.id.imageViewProfileSignUp);
        emailSignUp = findViewById(R.id.textInputEditTextEmailSignUp);
        passwordSignUp = findViewById(R.id.textInputEditTextPasswordSignUp);
        usernameSignUp = findViewById(R.id.textInputEditTextUsernameSignUp);
        signUp = findViewById(R.id.buttonSignUp);

        imageProfileSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailSignUp.getText().toString();
                String password = passwordSignUp.getText().toString();
                String username = usernameSignUp.getText().toString();

                if (!email.equals("") && !password.equals("") && !username.equals("")) {
                    signUpUser(email, password, username);
                } else {
                    Toast.makeText(getApplicationContext(), "PLease enter email, password and username", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void imageChooser() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imageProfileSignUp);
            imageControl = true;
        } else {
            imageControl = false;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void signUpUser(String email, String password, String username) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    reference.child("User").child(auth.getUid()).child("username").setValue(username);
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
                                                Toast.makeText(getApplicationContext(), "Sign Up successfully.", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Sign Up not successfully.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    } else {
                        reference.child("User").child(auth.getUid()).child("image").setValue("null");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "There is a problem.", Toast.LENGTH_SHORT).show();
                }

                Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

}