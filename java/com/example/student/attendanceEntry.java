package com.example.student;


import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class attendanceEntry extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference retrievedatabaseReference, uploaddatabaseReference;

    int arrayPointer = 0, totalStrength;
    private String clgCode, deptCode, attendanceType, semester, subjectCode, totalWorkingdays;
    private String[] registerNumberArray = new String[100];
    private String[] daysPresentArray = new String[100];


    EditText totalNumberEdt, presentNumberEdt;
    Button leftButton, rightButton, doneButton;
    TextView registerNumberView, collegeView, attTypeView;

    AttendanceInfo attendanceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_entry);

       Arrays.fill(daysPresentArray, "0");

        uploaddatabaseReference = firebaseDatabase.getReference("AttendanceInfo");
        attendanceInfo = new AttendanceInfo();

        Intent intent = getIntent();

        collegeView = findViewById(R.id.collegeView);
        collegeView.setText(intent.getStringExtra("CollegeName"));

        attTypeView = findViewById(R.id.attTypeView);
        attendanceType = intent.getStringExtra("AttendanceType");
        attTypeView.setText(attendanceType);

        clgCode =  intent.getStringExtra("CollegeCode");
        deptCode =  intent.getStringExtra("DepartmentCode");
        semester =  intent.getStringExtra("Semester");
        subjectCode = intent.getStringExtra("SubjectCode");

        leftButton = findViewById(R.id.lmove);
        rightButton = findViewById(R.id.rmove);
        doneButton = findViewById(R.id.donebtn);
        presentNumberEdt = findViewById(R.id.numberpresentEdit);

        totalNumberEdt = findViewById(R.id.totaldaysEdit);


        leftButton.setEnabled(false);
        presentNumberEdt.setText("0");
        totalNumberEdt.setText("1");

        retrievedatabaseReference = firebaseDatabase.getReference("StudentList").child(clgCode).child(deptCode).child(semester);
        retrievedatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    registerNumberArray[i] = dataSnapshot1.child("regNo").getValue().toString();
                    i++;
                }
                totalStrength = i-1;
                //------------------------------------------------------------------------------------------
                registerNumberView = findViewById(R.id.regNoView);
                registerNumberView.setText(registerNumberArray[0]);
                //------------------------------------------------------------------------------------------
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daysPresentArray[arrayPointer] = presentNumberEdt.getText().toString();
                arrayPointer++;
                nextData();
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daysPresentArray[arrayPointer] = presentNumberEdt.getText().toString();
                arrayPointer--;
                previousData();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDatatoFirebase();

            }
        });

    }

    void nextData(){

        if(arrayPointer >= totalStrength){
            rightButton.setEnabled(false);
            registerNumberView.setText(registerNumberArray[arrayPointer]);
            presentNumberEdt.setText( daysPresentArray[arrayPointer]);
        }
        else {
            registerNumberView.setText(registerNumberArray[arrayPointer]);
            presentNumberEdt.setText( daysPresentArray[arrayPointer]);
            leftButton.setEnabled(true);

        }

    }

    void previousData(){

        if(arrayPointer <= 0){
            leftButton.setEnabled(false);
            registerNumberView.setText(registerNumberArray[arrayPointer]);
            presentNumberEdt.setText( daysPresentArray[arrayPointer]);
        }
        else {
            registerNumberView.setText(registerNumberArray[arrayPointer]);
            presentNumberEdt.setText( daysPresentArray[arrayPointer]);
            rightButton.setEnabled(true);
        }
    }


//-----------------------------------------------------------------------------------------------------------------------------------------
    private void addDatatoFirebase(){
        //uploaddatabaseReference = firebaseDatabase.getReference("AttendanceInfo");
        uploaddatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalWorkingdays = totalNumberEdt.getText().toString();
                for (int j = 0; j < totalStrength; j++){
                attendanceInfo.setRegNo(registerNumberArray[j]);
                attendanceInfo.setConducted(totalWorkingdays);
                attendanceInfo.setPresent(daysPresentArray[j]);
                String percentage = Integer.toString((Integer.parseInt(daysPresentArray[j]) * 100 / Integer.parseInt(totalWorkingdays)));
                attendanceInfo.setPercentage(percentage);
                final String keyValue = Integer.toString(j+1);
                uploaddatabaseReference.child(clgCode).child(deptCode).child(semester).child(subjectCode).child(attendanceType).child(keyValue).setValue(attendanceInfo);

                }
                Toast.makeText(attendanceEntry.this, "Data added successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(attendanceEntry.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

//-----------------------------------------------------------------------------------------------------------------------------------------

} // end of class

