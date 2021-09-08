package com.example.student;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class attendanceModify extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference fetchDatabase, modifyDatabase;

    EditText regNoEdt, totalHoursEdt, hoursPresentEdt, percentageEdt;
    Button searchBtn, uploadBtn;
    TextView collegenameView;

    private String[] registerNumberArray = new String[100];
    private String[] daysPresentArray = new String[100];
    private String[] percentageArray = new String[100];
    private String daysConducted;

    private String collegename, collegecode, departmentcode, semester, subjectcode, attendancetype;
    private int totalStrength, arrayPointer;

    AttendanceInfo attendanceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_modify);

        attendanceInfo = new AttendanceInfo();

        Intent intent = getIntent();
        collegename = intent.getStringExtra("CollegeName");
        collegecode = intent.getStringExtra("CollegeCode");
        departmentcode = intent.getStringExtra("DepartmentCode");
        semester = intent.getStringExtra("Semester");
        subjectcode = intent.getStringExtra("SubjectCode");
        attendancetype = intent.getStringExtra("AttendanceType");

        regNoEdt = findViewById(R.id.regnoinput);
        searchBtn = findViewById(R.id.searchbtn);
        uploadBtn = findViewById(R.id.uploadbtn);

        totalHoursEdt = findViewById(R.id.totalhoursEdt);
        hoursPresentEdt = findViewById(R.id.hourspresentEdt);
        percentageEdt = findViewById(R.id.percentageEdt);

        collegenameView = findViewById(R.id.collegenameview);
        collegenameView.setText(collegename);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String regNo = regNoEdt.getText().toString();
                int j;
                for (j = 0; j < totalStrength; j++) {
                    if (registerNumberArray[j].equals(regNo)) {
                        break;
                    }
                }
                if (j == totalStrength){
                   Toast.makeText(attendanceModify.this, "Student not found...", Toast.LENGTH_SHORT).show();
                }else{
                    arrayPointer = j+1;
                    hoursPresentEdt.setEnabled(true);
                    totalHoursEdt.setText(daysConducted);
                    hoursPresentEdt.setText(daysPresentArray[j]);
                    percentageEdt.setText(percentageArray[j]);
                    uploadBtn.setEnabled(true);
                }
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String present = hoursPresentEdt.getText().toString();
                int percentage = (Integer.parseInt(present) * 100 / Integer.parseInt(daysConducted));
                alertDialog(present, percentage);
            }
        });

        hoursPresentEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND){
                    String present = hoursPresentEdt.getText().toString();
                    int percentage = (Integer.parseInt(present) * 100 / Integer.parseInt(daysConducted));
                    String s = Integer.toString(percentage);
                    percentageEdt.setText(s);
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {

        fetchDatabase = firebaseDatabase.getReference("AttendanceInfo").child(collegecode).child(departmentcode).child(semester)
                .child(subjectcode).child(attendancetype);

        fetchDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i =0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    registerNumberArray[i] = dataSnapshot1.child("regNo").getValue().toString();
                    daysConducted = dataSnapshot1.child("conducted").getValue().toString();
                    daysPresentArray[i] = dataSnapshot1.child("present").getValue().toString();
                    percentageArray[i] = dataSnapshot1.child("percentage").getValue().toString();
                    i++;
                }
                totalStrength = i - 1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(attendanceModify.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

        super.onResume();
    }

    private void alertDialog(String present, int percentage){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Check the following data once again before uploading in Database." +
                "\n Hours Conducted - " + daysConducted +
                "\n Hours Present - " + present +
                "\n Percentage - " + percentage);
        dialog.setTitle("Verify Credetials");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String percentageString = Integer.toString(percentage);
                        addDatatoFirebase(present, percentageString);
                    }
                });

        dialog.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();

    }

    private void addDatatoFirebase(String present, String percentage) {
        String regNo = registerNumberArray[arrayPointer];
        attendanceInfo.setRegNo(regNo);
        attendanceInfo.setConducted(daysConducted);
        attendanceInfo.setPresent(present);
        attendanceInfo.setPercentage(percentage);

        modifyDatabase = firebaseDatabase.getReference("AttendanceInfo").child(collegecode).child(departmentcode).child(semester)
                .child(subjectcode).child(attendancetype);

        modifyDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String position = Integer.toString(arrayPointer);
                modifyDatabase.child(position).setValue(attendanceInfo);
                Toast.makeText(attendanceModify.this, "Data added", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(attendanceModify.this, "Fail to add data " + databaseError, Toast.LENGTH_SHORT).show();
            }
        });



    }

}

/*
percentageEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                    int percentage = (Integer.parseInt(daysPresentArray[arrayPointer]) / Integer.parseInt(daysConducted)) * 100;
                    String s = Integer.toString(percentage);
                    Toast.makeText(attendanceModify.this, "Percentage = " + s, Toast.LENGTH_SHORT).show();

            }
        });
 */