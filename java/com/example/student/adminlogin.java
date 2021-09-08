package com.example.student;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class adminlogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);

        EditText regNoEdt = findViewById(R.id.lregno);
        EditText passwordEdt = findViewById(R.id.slpass);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("AdminInfo");
        Button sendDatabtn = findViewById(R.id.sloginbtn);

        sendDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = regNoEdt.getText().toString();
                String pass = passwordEdt.getText().toString();


                if (TextUtils.isEmpty(number) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(adminlogin.this, "Please add both data.", Toast.LENGTH_SHORT).show();
                }

                else {

                    Query checkUser = databaseReference.orderByChild("code").equalTo(number);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                String passwordFromDB = dataSnapshot.child(number).child("password").getValue(String.class);
                                String nameFromDB = dataSnapshot.child(number).child("name").getValue(String.class);

                                if (passwordFromDB.equals(pass)) {
                                    Intent intent = new Intent(getApplicationContext(), adminhompage.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(adminlogin.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(adminlogin.this, "Account not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(adminlogin.this, "Fail to get data " + databaseError, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}