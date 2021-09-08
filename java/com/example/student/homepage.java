package com.example.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class homepage extends AppCompatActivity {

    private TextView fullName, colgName;
    private Button notesbtn, gcbtn, attbtn, infobtn, circularbtn, aboutbtn;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference deptdatabaseReference;
    DatabaseReference colgdatabaseReference;

    String regNofromlogin, sNamefromlogin, fCodefromlogin, fNamefromlogin;
    String colgcode, colgNamefromDB;
    int flagfromlogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        fullName = findViewById(R.id.nameView);
        colgName = findViewById(R.id.colgView);

        notesbtn = findViewById(R.id.btn1);
        gcbtn = findViewById(R.id.btn2);
        attbtn = findViewById(R.id.btn3);
        circularbtn = findViewById(R.id.btn4);
        infobtn = findViewById(R.id.btn5);
        aboutbtn = findViewById(R.id.btn6);


        Intent intent = getIntent();
        flagfromlogin = intent.getIntExtra("UserFlag", 0);

        if(flagfromlogin == 0) {

            regNofromlogin = intent.getStringExtra("srn");
            sNamefromlogin = intent.getStringExtra("snm");

            colgcode = regNofromlogin.substring(0, 4);
            getCollegeName();

            fullName.setText("Hi, " + sNamefromlogin);

        }else if(flagfromlogin == 1){

            fCodefromlogin = intent.getStringExtra("fcn");
            fNamefromlogin = intent.getStringExtra("fnm");
            colgcode = intent.getStringExtra("FclgCode");
            getCollegeName();

            fullName.setText("Hi, " + fNamefromlogin);

        } else{
            Toast.makeText(homepage.this, "Invalid User Found --- May be an error", Toast.LENGTH_SHORT).show();
        }

        //..........................................notes menu button.......................................

        notesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent attendancedetailselect = new Intent(homepage.this, attendancedetailselect.class);
                attendancedetailselect.putExtra("collegeName", colgNamefromDB);
                attendancedetailselect.putExtra("collegeCode", colgcode);
                attendancedetailselect.putExtra("menuFlag", 1);
                attendancedetailselect.putExtra("Flag", flagfromlogin);
                startActivity(attendancedetailselect);
            }
        });


        //..........................................grade calculator button.......................................


        gcbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (flagfromlogin == 0) {
                    String dept = regNofromlogin.substring(6, 9);

                    deptdatabaseReference = firebaseDatabase.getReference("DepartmentInfo").child(dept);

                    deptdatabaseReference.addListenerForSingleValueEvent (new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String deptname = dataSnapshot.child("deptName").getValue().toString();

                            //......................................................................
                            Intent gradecalc = new Intent(getApplicationContext(), gradecalc.class);
                            gradecalc.putExtra("dptname", deptname);
                            gradecalc.putExtra("department", dept);
                            gradecalc.putExtra("collegeName", colgNamefromDB);
                            startActivity(gradecalc);
                            //......................................................................

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(homepage.this, "Database Error", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else if (flagfromlogin == 1){

                    Intent otherdeptselect = new Intent(getApplicationContext(), otherdeptselect.class);
                    otherdeptselect.putExtra("collegeName", colgNamefromDB);
                    startActivity(otherdeptselect);
            }
        }
        });
        
        attbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagfromlogin == 0){
                    Intent StudentAttendanceListView = new Intent(homepage.this, StudentAttendanceListView.class);
                    StudentAttendanceListView.putExtra("name", sNamefromlogin);
                    StudentAttendanceListView.putExtra("regNo", regNofromlogin);
                    startActivity(StudentAttendanceListView);

                }
                else if (flagfromlogin == 1){
                    Intent attendancedetailselect = new Intent(homepage.this, attendancedetailselect.class);
                    attendancedetailselect.putExtra("collegeName", colgNamefromDB);
                    attendancedetailselect.putExtra("collegeCode", colgcode);
                    attendancedetailselect.putExtra("menuFlag", 3);
                    startActivity(attendancedetailselect);
                }
                
            }
        });

        circularbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent circular_listview = new Intent(homepage.this, circular_listview.class);
                circular_listview.putExtra("CollegeName", colgNamefromDB);
                circular_listview.putExtra("CollegeCode", colgcode);
                circular_listview.putExtra("Flag", flagfromlogin);
                startActivity(circular_listview);
            }
        });

        infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagfromlogin == 0){
                    Intent studentinfoedit = new Intent(homepage.this, studentinfoedit.class);
                    studentinfoedit.putExtra("regno", regNofromlogin);
                    startActivity(studentinfoedit);
                }else if (flagfromlogin == 1){
                    Intent facultyinfoedit = new Intent(homepage.this, facultyinfoedit.class);
                    facultyinfoedit.putExtra("fcode", fCodefromlogin);
                    startActivity(facultyinfoedit);
                }
            }
        });


        aboutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(homepage.this, infopage.class);
                startActivity(intent1);
            }
        });
    } //end of oncreate

    @Override
    protected void onResume() {

        super.onResume();
    }

    void getCollegeName(){

        //..............................................................................................

        colgdatabaseReference = firebaseDatabase.getReference("CollegeInfo").child(colgcode);

        colgdatabaseReference.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                colgNamefromDB = dataSnapshot.child("colgName").getValue().toString();
                colgName.setText(colgNamefromDB);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(homepage.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

        //..............................................................................................


    }
    } //end of class













/*
 alertbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(homepage.this);
                builder.setTitle("Title");

// Set up the input
                final EditText input = new EditText(homepage.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       String m_Text = input.getText().toString();
                       Toast.makeText(homepage.this, m_Text, Toast.LENGTH_LONG).show();
                       dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
 */