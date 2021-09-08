package com.example.student;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class studentattendance extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference subjectlistdatabaseReference, fetchDatabase;

    Spinner subjectSelect;
    TextView collegeNameView, attendanceTypeView, registerNumberView, studentNameView;
    TextView hoursConductedView, hoursPresentView, attendancePercentageView, errorMessageView, adminTextView;
    View DetailsView;
    private String regNo, studentName, attendanceType, collegename, collegecode, departmentcode, semester, subjectSelected;

    private String[] tempMenu = {};
    private String[] subjectList = new String[10];

    private String daysConducted;
    private String[] registerNumberArray = new String[100];
    private String[] daysPresentArray = new String[100];
    private String[] percentageArray = new String[100];

    int totalStrength;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentattendance);


        Intent intent = getIntent();
        collegename = intent.getStringExtra("CollegeName");
        collegecode = intent.getStringExtra("CollegeCode");
        departmentcode = intent.getStringExtra("DepartmentCode");
        semester = intent.getStringExtra("Semester");
        regNo = intent.getStringExtra("regNo");
        studentName = intent.getStringExtra("Name");
        attendanceType = intent.getStringExtra("AttendanceType");


        collegeNameView = findViewById(R.id.collegeView);
        collegeNameView.setText(collegename);

        attendanceTypeView = findViewById(R.id.attendanceTypeView);
        attendanceTypeView.setText(attendanceType);

        registerNumberView = findViewById(R.id.RegisterNumberView);
        registerNumberView.setText(regNo);

        studentNameView = findViewById(R.id.NameView);
        studentNameView.setText(studentName);

        hoursConductedView = findViewById(R.id.totalhoursview);
        hoursPresentView = findViewById(R.id.presentview);
        attendancePercentageView = findViewById(R.id.percentageview);
        errorMessageView = findViewById(R.id.errormessage);

        DetailsView = findViewById(R.id.detailsView);


        //....................................... Spinner to select Subjet ............................................
        subjectSelect = findViewById(R.id.subjectSelect);
        final List<String> ListSubjects = new ArrayList<>(Arrays.asList(tempMenu));
        final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ListSubjects);
        subjectSelect.setAdapter(arrayAdapter2);

        subjectSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectSelected = parent.getItemAtPosition(position).toString();
                getAttendancefromDB();
                //assignValuestoTextView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(studentattendance.this, "Please Select", Toast.LENGTH_SHORT).show();
            }

        }); //end of Listener function

        //........................................................................................

        //----------------------------------------------------------------------------------
        firebaseDatabase = FirebaseDatabase.getInstance();
        subjectlistdatabaseReference = firebaseDatabase.getReference("GradeInfo").child(departmentcode).child(semester);
        subjectlistdatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    subjectList[i] = dataSnapshot1.getKey().toString();
                    ListSubjects.add(subjectList[i]);
                    arrayAdapter2.notifyDataSetChanged();
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(studentattendance.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

        //----------------------------------------------------------------------------------


        adminTextView = findViewById(R.id.admintv);
        adminTextView.setPaintFlags(adminTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        adminTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(studentattendance.this, infopage.class);
                startActivity(intent1);
            }
        });

    }

public void getAttendancefromDB() {

        fetchDatabase = firebaseDatabase.getReference("AttendanceInfo").child(collegecode).child(departmentcode).child(semester)
                .child(subjectSelected).child(attendanceType);

        fetchDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    DetailsView.setVisibility(View.VISIBLE);
                    errorMessageView.setVisibility(View.INVISIBLE);
                    int i = 0;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        registerNumberArray[i] = dataSnapshot1.child("regNo").getValue().toString();
                        daysConducted = dataSnapshot1.child("conducted").getValue().toString();
                        daysPresentArray[i] = dataSnapshot1.child("present").getValue().toString();
                        percentageArray[i] = dataSnapshot1.child("percentage").getValue().toString();
                        i++;
                    }
                    totalStrength = i - 1;
                    assignValuestoTextView();
                } else {
                    errorMessageView.setVisibility(View.VISIBLE);
                    DetailsView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(studentattendance.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void assignValuestoTextView(){

        int j;
        for (j = 0; j < totalStrength; j++) {
            if (registerNumberArray[j].equals(regNo)) {
                break;
            }
        }
        if (j == totalStrength){
            Toast.makeText(studentattendance.this, "Please try again some time...", Toast.LENGTH_SHORT).show();
        }else{
            hoursConductedView.setText(daysConducted);
            hoursPresentView.setText(daysPresentArray[j]);
            attendancePercentageView.setText(percentageArray[j]);
        }
    }
}