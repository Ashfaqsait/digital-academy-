package com.example.student;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class facultylogin extends AppCompatActivity {

    private EditText sCodeEdt, fpasswordEdt;
    private TextView forgotPasswordView;
    private Button fsendDatabtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FacultyInfo facultyInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facultylogin);

        sCodeEdt = findViewById(R.id.sfcode);
        fpasswordEdt = findViewById(R.id.flpass);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("FacultyInfo");
        facultyInfo = new FacultyInfo();
        fsendDatabtn = findViewById(R.id.floginbtn);

        fsendDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fnumber = sCodeEdt.getText().toString();
                String fpass = fpasswordEdt.getText().toString();


                if (TextUtils.isEmpty(fnumber) || TextUtils.isEmpty(fpass)) {
                    Toast.makeText(facultylogin.this, "Please add both data.", Toast.LENGTH_SHORT).show();
                }

                else {

                    Query checkUser = databaseReference.orderByChild("scode").equalTo(fnumber);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                String passwordFromDB = dataSnapshot.child(fnumber).child("fpassword").getValue(String.class);
                                String nameFromDB = dataSnapshot.child(fnumber).child("firstName").getValue(String.class);
                                String fcolcodeFromDB = dataSnapshot.child(fnumber).child("fcollegeCode").getValue(String.class);

                                if (passwordFromDB.equals(fpass)) {
                                    int flag=1;

                                    Intent intent = new Intent(getApplicationContext(), homepage.class);
                                    intent.putExtra("fcn", fnumber);
                                    intent.putExtra("fnm", nameFromDB);
                                    intent.putExtra("UserFlag", flag);
                                    intent.putExtra("FclgCode", fcolcodeFromDB);
                                    startActivity(intent);
                                    finish();
                                } else {

                                    Toast.makeText(facultylogin.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(facultylogin.this, "Account not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(facultylogin.this, "Fail to get data " + databaseError, Toast.LENGTH_SHORT).show();
                        }
                    });



                }
            }
        });

//..........................................forgot password.......................................

        forgotPasswordView = findViewById(R.id.forgotpassView);
        forgotPasswordView.setPaintFlags(forgotPasswordView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        forgotPasswordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotpassword = new Intent(getApplicationContext(), forgotpassword.class);
                forgotpassword.putExtra("Flag", 1);
                startActivity(forgotpassword);
            }
        });


    }
}