package com.example.visitid.userAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.visitid.mainPage.MainActivity;
import com.example.visitid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Iterator;


public class signUp extends AppCompatActivity {
    EditText nUser, nPass;
    Button registerBtn, backToMain;
    ImageButton passVis;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mDatabaseRef, mUserCheckData;
    public static String USER_KEY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        nUser =  findViewById(R.id.nUser);
        nPass =  findViewById(R.id.nPass);
        registerBtn =  findViewById(R.id.registerBtn);
        backToMain =  findViewById(R.id.backToMain);
        passVis =  findViewById(R.id.passVis);
        passVis.setImageResource(R.drawable.closed);
        passVis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = passVis.getDrawable();
                if(drawable.getConstantState().equals(getResources().getDrawable(R.drawable.closed,null).getConstantState())){
                    passVis.setImageResource(R.drawable.open);
                    nPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
                }else{
                    passVis.setImageResource(R.drawable.closed);
                    nPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mUserCheckData = FirebaseDatabase.getInstance().getReference().child("User");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    signUp.USER_KEY=user.getUid();
                    Log.d("TAGUSER", signUp.USER_KEY);
                } else {
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
            }

        };
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nUserString, nPassString;
                nUserString = (nUser.getText().toString().trim());
                nPassString = (nPass.getText().toString().trim());
                if(nPassString.length()>=6){
                    if (!TextUtils.isEmpty(nUserString) && !TextUtils.isEmpty(nPassString)) {

                        mAuth.createUserWithEmailAndPassword(nUserString, nPassString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    Log.d("taskUser", task.getException()+"");
                                }else{
                                    Log.d("taskUser", task.getException()+" didnt work");
                                }
                                if (task.isSuccessful()) {
                                    DatabaseReference mChildDatabase = mDatabaseRef.child("Users").push();
                                    String key_user = mChildDatabase.getKey();
                                    Log.d("TAGUSER", key_user);
                                    mChildDatabase.child("isVerified").setValue("unverified");
                                    mChildDatabase.child("userKey").setValue(key_user);
                                    mChildDatabase.child("EmailUser").setValue(nUserString);
                                    mChildDatabase.child("PasswordUser").setValue(nPassString);
                                    Toast.makeText(signUp.this, "User Account Created", Toast.LENGTH_LONG).show();
                                    USER_KEY = mChildDatabase.getKey();
                                    startActivity(new Intent(signUp.this, Profile.class));

                                } else {
                                    Toast.makeText(signUp.this, "Failed to create User Account 1", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(signUp.this, "Failed to create User Account 2", Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(signUp.this, "Failed to create User Account 3", Toast.LENGTH_LONG).show();
                }
            }
        });
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signUp.this, Login.class));
            }
        });


    }

    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);

    }
}
