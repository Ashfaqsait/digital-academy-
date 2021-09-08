package com.example.student;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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

public class AttendanceListView extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference, updatabaseReference;

    ListView attListDisplay;
    Button addDataBtn;

    private String[] tempMenu = {};
    private String[] AttendanceMenu = {"Internal 1", "Internal 2", "Internal 3", "Internal 4", "Overall"};

    private String collegename, collegecode, departmentcode, semester, subjectcode, attendanceFromSpinner;

    int i = 0;

    AttendanceInfo attendanceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list_view);

        Intent intent = getIntent();

            collegename = intent.getStringExtra("CollegeName");
            collegecode = intent.getStringExtra("CollegeCode");
            departmentcode = intent.getStringExtra("DepartmentCode");
            semester = intent.getStringExtra("Semester");
            subjectcode = intent.getStringExtra("SubjectCode");

        final List<String> ListElementsArrayList = new ArrayList<>(Arrays.asList(tempMenu));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.textView, ListElementsArrayList);

        attListDisplay = findViewById(R.id.listView);
        attListDisplay.setAdapter(arrayAdapter);

        attendanceInfo = new AttendanceInfo();
        //---------------------------------------------------------------------------------------------------------------------------------

        databaseReference = firebaseDatabase.getReference("AttendanceInfo").child(collegecode).child(departmentcode).
                child(semester).child(subjectcode);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String attendanceMenu = dataSnapshot1.getKey().toString();
                        ListElementsArrayList.add(attendanceMenu);
                        arrayAdapter.notifyDataSetChanged();
                        i++;
                    }
                } else {
                    Toast.makeText(AttendanceListView.this, "Not Upoaded - Contact Admin", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AttendanceListView.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

        //---------------------------------------------------------------------------------------------------------------------------------



        attListDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = arrayAdapter.getItem(position);

                Intent attendanceModify = new Intent(AttendanceListView.this, attendanceModify.class);
                attendanceModify.putExtra("AttendanceType", value);
                attendanceModify.putExtra("CollegeName", collegename);
                attendanceModify.putExtra("CollegeCode", collegecode);
                attendanceModify.putExtra("DepartmentCode", departmentcode);
                attendanceModify.putExtra("Semester", semester);
                attendanceModify.putExtra("SubjectCode", subjectcode);
                startActivity(attendanceModify);
            }
        });

        addDataBtn = findViewById(R.id.newDatabtn);
        addDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AttendanceListView.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                mBuilder.setTitle("Select an Attendance");
                Spinner mSpinner = (Spinner) mView.findViewById(R.id.newAttendanceSelect);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AttendanceListView.this, android.R.layout.simple_spinner_dropdown_item, AttendanceMenu);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner.setAdapter(adapter);

                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        attendanceFromSpinner = mSpinner.getSelectedItem().toString();
                        if (ListElementsArrayList.contains(attendanceFromSpinner)){
                            Toast.makeText(AttendanceListView.this, attendanceFromSpinner + " is already entered.", Toast.LENGTH_SHORT).show();
                        }else {

                            updatabaseReference = firebaseDatabase.getReference("AttendanceMenuInfo");

                            attendanceInfo.setName(attendanceFromSpinner);
                            updatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String index = Integer.toString(i+1);
                                    updatabaseReference.child(index).setValue(attendanceInfo);
                                    Toast.makeText(AttendanceListView.this, "Data added to Menu", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(AttendanceListView.this, "Database Error", Toast.LENGTH_SHORT).show();
                                }
                            });


                            Intent attendanceEntry = new Intent(AttendanceListView.this, attendanceEntry.class);

                            attendanceEntry.putExtra("CollegeName", collegename);
                            attendanceEntry.putExtra("CollegeCode", collegecode);
                            attendanceEntry.putExtra("DepartmentCode", departmentcode);
                            attendanceEntry.putExtra("Semester", semester);
                            attendanceEntry.putExtra("SubjectCode", subjectcode);
                            attendanceEntry.putExtra("AttendanceType", attendanceFromSpinner);

                            startActivity(attendanceEntry);
                        }
                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });




    }

}


/*




 */