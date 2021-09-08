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

public class studentlogin extends AppCompatActivity {

    private EditText regNoEdt, passwordEdt;
    private Button sendDatabtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StudentInfo studentInfo;

    TextView forgotPasswordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlogin);

        regNoEdt = findViewById(R.id.lregno);
        passwordEdt = findViewById(R.id.slpass);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("StudentInfo");
        studentInfo = new StudentInfo();
        sendDatabtn = findViewById(R.id.sloginbtn);

        sendDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = regNoEdt.getText().toString();
                String pass = passwordEdt.getText().toString();


                if (TextUtils.isEmpty(number) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(studentlogin.this, "Please add both data.", Toast.LENGTH_SHORT).show();
                }

                else {

                    Query checkUser = databaseReference.orderByChild("regNo").equalTo(number);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                    String passwordFromDB = dataSnapshot.child(number).child("spassword").getValue(String.class);
                    String nameFromDB = dataSnapshot.child(number).child("firstName").getValue(String.class);

                                if (passwordFromDB.equals(pass)) {
                                    int flag=0;

                                    Intent intent = new Intent(getApplicationContext(), homepage.class);
                                    intent.putExtra("srn", number);
                                    intent.putExtra("snm", nameFromDB);
                                    intent.putExtra("UserFlag", flag);
                                    startActivity(intent);
                                    finish();
                                } else {

                                    Toast.makeText(studentlogin.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }

                } else {
                    Toast.makeText(studentlogin.this, "Account not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(studentlogin.this, "Fail to get data " + databaseError, Toast.LENGTH_SHORT).show();
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
                forgotpassword.putExtra("Flag", 0);
                startActivity(forgotpassword);
            }
        });


//..........................................signup button.......................................

        Button ssbtn = findViewById(R.id.ssignupbtn);

        ssbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                System.out.println("Button Clicked");

                Intent studentsignup = new Intent(getApplicationContext(), studentsignup.class);
                startActivity(studentsignup);
            }
        });



    }

}