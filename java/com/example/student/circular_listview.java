package com.example.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.List;

public class circular_listview extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference fetchCircularDatabase, onClickFetchDB;

    Button addCircularBtn;
    ListView listViewCircular;

    private String collegeName, collegeCode;

    private String[] circularTimeList = new String[100];
    private String[] tempKey = new String[100];
    private int Flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular_listview);

        Intent intent = getIntent();
        collegeName = intent.getStringExtra("CollegeName");
        collegeCode = intent.getStringExtra("CollegeCode");
        Flag = intent.getIntExtra("Flag", 2);

        listViewCircular = findViewById(R.id.listviewcircular);

        List<TwoStrings> twoStringsList = new ArrayList<>();

        MyAdapter myAdapter = new MyAdapter(this, R.layout.clistview, twoStringsList);
        listViewCircular.setAdapter(myAdapter);

        fetchCircularDatabase = firebaseDatabase.getReference("CircularInfo").child(collegeCode);

        fetchCircularDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String title = dataSnapshot1.child("title").getValue().toString();
                    String time = dataSnapshot1.child("time").getValue().toString();
                    tempKey[i] = dataSnapshot1.getKey().toString();
                    circularTimeList[i] =  time;
                    TwoStrings twoStrings = new TwoStrings(title, time);
                    twoStringsList.add(twoStrings);
                    myAdapter.notifyDataSetChanged();
                    i++;


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(circular_listview.this, "Database Error", Toast.LENGTH_SHORT).show();

            }
        });

        addCircularBtn = findViewById(R.id.addCircularbtn);

        addCircularBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent circularUpload = new Intent(circular_listview.this, circularUpload.class);
                circularUpload.putExtra("CollegeName", collegeName);
                circularUpload.putExtra("CollegeCode", collegeCode);
                startActivity(circularUpload);
            }
        });

        listViewCircular.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String s = circularTimeList[position];
                String key = tempKey[position];
               // Toast.makeText(circular_listview.this, key, Toast.LENGTH_SHORT).show();

                Intent studentcircularview = new Intent(circular_listview.this, studentcircularview.class);
                studentcircularview.putExtra("CollegeName", collegeName);
                studentcircularview.putExtra("CollegeCode", collegeCode);
                studentcircularview.putExtra("Time", s);
                studentcircularview.putExtra("Key", key);
                startActivity(studentcircularview);
            }
        });

    }

    @Override
    protected void onResume() {
        if (Flag == 0){
            addCircularBtn.setVisibility(View.INVISIBLE);
        }
        super.onResume();
    }
}





/*

-------------------------------------------------------------------------------------

//        TwoStrings twoStrings = new TwoStrings("a", "b");
//        twoStringsList.add(twoStrings);


        listViewCircular.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = listViewCircular.getItemAtPosition(position).toString();
                Toast.makeText(circular_listview.this, s, Toast.LENGTH_SHORT).show();

                Intent studentcircularview = new Intent(circular_listview.this, studentcircularview.class);
                startActivity(studentcircularview);
            }
        });


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    circularTitleList[i] = dataSnapshot1.child("title").getValue().toString();
                    circularTimeList[i] = dataSnapshot1.child("time").getValue().toString();
                    //sard.notifyDataSetChanged();
                    i++;
                }





 */