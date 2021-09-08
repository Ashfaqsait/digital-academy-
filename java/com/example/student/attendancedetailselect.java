package com.example.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class attendancedetailselect extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference deptlistdatabaseReference, subjectlistdatabaseReference;

    private String[] departmentList = new String[10];
    private String[] subjectList = new String[10];
    private String[] semesterList = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private String[] departmentCodeList = new String[10];

    private String deptSelected, semSelected, deptCodefromSpin, subjectSelected;

    private String[] tempMenu = {};

    TextView menuTypeView, CollegeNameView;
    Spinner departmentSelect, semesterSelect, subjectSelect;
    Button proceedBtn;

    AttendanceInfo attendanceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendancedetailselect);

        Intent intent = getIntent();
        int menuType = intent.getIntExtra("menuFlag", 0);
        String CollegeName = intent.getStringExtra("collegeName");
        String CollegeCode = intent.getStringExtra("collegeCode");
        int Flag = intent.getIntExtra("Flag", 2);

        CollegeNameView = findViewById(R.id.collegeView);
        CollegeNameView.setText(CollegeName);
        menuTypeView = findViewById(R.id.menuTypeView);

        if (menuType == 1){
            if (Flag == 0){
                menuTypeView.setText("Notes Section");
            }else if (Flag == 1){
                menuTypeView.setText("Notes Upload");
            }
        }else if (menuType == 3){
        menuTypeView.setText("Attendance Entry");
        }else{menuTypeView.setText("Invalid Menu Type");}

        //.................................. Spinner to select department.................................................
        departmentSelect = findViewById(R.id.deptSelect);
        final List<String> ListDepartments = new ArrayList<>(Arrays.asList(tempMenu));
        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ListDepartments);
        departmentSelect.setAdapter(arrayAdapter1);

        departmentSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                deptSelected = parent.getItemAtPosition(position).toString();
                deptCodefromSpin = departmentCodeList[position];
                selectSemesterfromSpin();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(attendancedetailselect.this, "Please Select", Toast.LENGTH_SHORT).show();
            }

        }); //end of Listener function

        //........................................................................................

        //......................................... Firebase Event to get Department Names and Codes
        firebaseDatabase = FirebaseDatabase.getInstance();
        deptlistdatabaseReference = firebaseDatabase.getReference("DepartmentInfo");

        deptlistdatabaseReference.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    departmentCodeList[i] = dataSnapshot1.getKey().toString();
                    departmentList[i] = dataSnapshot1.child("deptName").getValue().toString();
                    ListDepartments.add(departmentList[i]);
                    arrayAdapter1.notifyDataSetChanged();
                    i++;
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(attendancedetailselect.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

        //.........................................
        proceedBtn = findViewById(R.id.proceedbtn);
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (menuType == 1){
                    Intent noteslistview = new Intent(attendancedetailselect.this, noteslistview.class);
                    noteslistview.putExtra("CollegeName", CollegeName);
                    noteslistview.putExtra("CollegeCode", CollegeCode);
                    noteslistview.putExtra("DepartmentCode", deptCodefromSpin);
                    noteslistview.putExtra("Semester", semSelected);
                    noteslistview.putExtra("SubjectCode", subjectSelected);
                    noteslistview.putExtra("Flag", Flag);
                    startActivity(noteslistview);
                } else if (menuType == 3) {
                    Intent AttendanceListView = new Intent(attendancedetailselect.this, AttendanceListView.class);
                    AttendanceListView.putExtra("CollegeName", CollegeName);
                    AttendanceListView.putExtra("CollegeCode", CollegeCode);
                    AttendanceListView.putExtra("DepartmentCode", deptCodefromSpin);
                    AttendanceListView.putExtra("Semester", semSelected);
                    AttendanceListView.putExtra("SubjectCode", subjectSelected);
                    startActivity(AttendanceListView);
                }

            }
        });

    }

    void selectSemesterfromSpin(){

        //........................................ Spinner to select semester...........................................
        semesterSelect = findViewById(R.id.semSelect);

        semesterSelect.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, semesterList));

        semesterSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semSelected = parent.getItemAtPosition(position).toString();
                getSubjectsfromDB();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(attendancedetailselect.this, "Please Select", Toast.LENGTH_SHORT).show();
            }

        }); //end of Listener function

        //........................................................................................

    }
    void getSubjectsfromDB(){

        if(deptCodefromSpin.isEmpty() || semSelected.isEmpty()){Toast.makeText(attendancedetailselect.this, "Please Select", Toast.LENGTH_SHORT).show();}
        else {

            //....................................... Spinner to select Subjet ............................................
            subjectSelect = findViewById(R.id.subjectSelect);
            final List<String> ListSubjects = new ArrayList<>(Arrays.asList(tempMenu));
            final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ListSubjects);
            subjectSelect.setAdapter(arrayAdapter2);

            subjectSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    subjectSelected = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(attendancedetailselect.this, "Please Select", Toast.LENGTH_SHORT).show();
                }

            }); //end of Listener function

            //........................................................................................

            //----------------------------------------------------------------------------------
            subjectlistdatabaseReference = firebaseDatabase.getReference("GradeInfo").child(deptCodefromSpin).child(semSelected);
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

                }
            });

            //----------------------------------------------------------------------------------
        }
    }
}

/*
Toast.makeText(attendancedetailselect.this, "College Name = "+ CollegeName, Toast.LENGTH_SHORT).show();
                Toast.makeText(attendancedetailselect.this, "College Code = " + CollegeCode, Toast.LENGTH_SHORT).show();
                Toast.makeText(attendancedetailselect.this, "Department Code = " + deptCodefromSpin, Toast.LENGTH_SHORT).show();
                Toast.makeText(attendancedetailselect.this, "Semester = " + semSelected, Toast.LENGTH_SHORT).show();
                Toast.makeText(attendancedetailselect.this, "Subject Code = " + subjectSelected, Toast.LENGTH_SHORT).show();
 */