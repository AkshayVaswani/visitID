package com.example.visitid.mainPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.visitid.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class joinEvent extends AppCompatActivity {

    Button join, dont, back;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    String personName, personEmail, userKey, usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_event);

        join = findViewById(R.id.yesJoin);
        dont=findViewById(R.id.noJoin);
        back=findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(joinEvent.this, mapList.class));
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(joinEvent.this);
                if(acct != null){
                    personName = acct.getDisplayName();
                    personEmail = acct.getEmail();
                    userKey = acct.getId();
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user != null){
                        personName = user.getDisplayName();
                        personEmail = user.getEmail();
                        userKey = user.getUid();
                    }
                }
                Intent intent = getIntent();

                String pos = intent.getStringExtra("Event_Name");




                myRef.child("Events").child(pos).child("analysis").child("email").setValue(personEmail);
                myRef.child("Events").child(pos).child("analysis").child("name").setValue(personName);

            }
        });


        dont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(joinEvent.this, mapList.class));
            }
        });

    }
}
