package com.example.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.Calendar;
import java.util.List;

public class StudentAttendanceListView extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference, colgdatabaseReference;

    ListView attListDisplay2;

    private String[] tempMenu = {};
    private String regNo, studentName, collegecode, departmentcode, semester, collegename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance_list_view);

        Intent intent = getIntent();
        regNo = intent.getStringExtra("regNo");
        studentName = intent.getStringExtra("name");
        getStudentsData(regNo);

        final List<String> ListElementsArrayList = new ArrayList<>(Arrays.asList(tempMenu));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.textView, ListElementsArrayList);

        attListDisplay2 = findViewById(R.id.listView2);
        attListDisplay2.setAdapter(arrayAdapter);

        databaseReference = firebaseDatabase.getReference("AttendanceMenuInfo");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String attendanceMenu = dataSnapshot1.child("name").getValue().toString();
                    ListElementsArrayList.add(attendanceMenu);
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StudentAttendanceListView.this, "Not Upoaded - Contact Admin", Toast.LENGTH_SHORT).show();
            }
        });

        attListDisplay2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = arrayAdapter.getItem(position);

                Intent studentattendance = new Intent(StudentAttendanceListView.this, studentattendance.class);
                studentattendance.putExtra("AttendanceType", value);
                studentattendance.putExtra("CollegeName", collegename);
                studentattendance.putExtra("regNo", regNo);
                studentattendance.putExtra("Name", studentName);
                studentattendance.putExtra("CollegeCode", collegecode);
                studentattendance.putExtra("DepartmentCode", departmentcode);
                studentattendance.putExtra("Semester", semester);
                startActivity(studentattendance);
            }
        });



    }


    public void getStudentsData(String regNo1){
        collegecode = regNo1.substring(0, 4);
        departmentcode = regNo1.substring(6, 9);

        String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
        semester = Integer.toString( 2 * (
                Integer.parseInt(year.substring(2, 4)) - Integer.parseInt(regNo1.substring(4, 6))
                )
        );

        //..............................................................................................

        colgdatabaseReference = firebaseDatabase.getReference("CollegeInfo").child(collegecode);

        colgdatabaseReference.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                collegename = dataSnapshot.child("colgName").getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StudentAttendanceListView.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

        //..............................................................................................


    }


}


/*





        public void getStudentsData(String regNo1){
            collegecode = regNo1.substring(0, 4);
            departmentcode = regNo1.substring(6, 9);

            String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
            semester = Integer.toString(
                    Integer.parseInt(year.substring(2, 4)) - Integer.parseInt(regNo1.substring(4, 6))
            );

            //..............................................................................................

            colgdatabaseReference = firebaseDatabase.getReference("CollegeInfo").child(collegecode);

            colgdatabaseReference.addListenerForSingleValueEvent (new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    collegename = dataSnapshot.child("colgName").getValue().toString();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AttendanceListView.this, "Database Error", Toast.LENGTH_SHORT).show();
                }
            });

            //..............................................................................................


        }


 */