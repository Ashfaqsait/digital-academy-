package com.example.student;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

public class noteslistview extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference fetchNotesDatabase;

    String CollegeName, CollegeCode, deptCodefromSpin, semSelected, subjectSelected;
    String[] tempKey = new String[100];

    Button addNotesBtn;
    ListView listViewNotes;
    private int Flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noteslistview);

        Intent intent = getIntent();
        CollegeName = intent.getStringExtra("CollegeName");
        CollegeCode = intent.getStringExtra("CollegeCode");
        deptCodefromSpin = intent.getStringExtra("DepartmentCode");
        semSelected = intent.getStringExtra("Semester");
        subjectSelected = intent.getStringExtra("SubjectCode");
        Flag = intent.getIntExtra("Flag", 2);

        listViewNotes = findViewById(R.id.listviewnotes);

        List<TwoStrings> twoStringsList = new ArrayList<>();

        MyAdapter myAdapter = new MyAdapter(this, R.layout.clistview, twoStringsList);
        listViewNotes.setAdapter(myAdapter);


        fetchNotesDatabase = firebaseDatabase.getReference("NotesInfo").child(CollegeCode).child(subjectSelected);

        fetchNotesDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String title = dataSnapshot1.child("title").getValue().toString();
                    String tag = dataSnapshot1.child("tags").getValue().toString();
                    tempKey[i] = dataSnapshot1.child("pdflink").getValue().toString();
                    TwoStrings twoStrings = new TwoStrings(title, tag);
                    twoStringsList.add(twoStrings);
                    myAdapter.notifyDataSetChanged();
                    i++;


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(noteslistview.this, "Database Error", Toast.LENGTH_SHORT).show();

            }
        });


        listViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String key = tempKey[position];
                // Toast.makeText(circular_listview.this, key, Toast.LENGTH_SHORT).show();

                CharSequence options[] = new CharSequence[]{"Download", "View", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Choose One");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
// we will be downloading the pdf
                        if (which == 0) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(key));
                            startActivity(intent);
                        }
// We will view the pdf
                        if (which == 1) {
                            Intent intent = new Intent(view.getContext(), ViewPdf.class);
                            intent.putExtra("Url", key);
                            startActivity(intent);
                        }
                    }
                });
                builder.show();

            }
        });


        addNotesBtn = findViewById(R.id.addnotesactbtn);

        addNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notesupload = new Intent(noteslistview.this, notesupload.class);
                notesupload.putExtra("CollegeName", CollegeName);
                notesupload.putExtra("CollegeCode", CollegeCode);
                notesupload.putExtra("DepartmentCode", deptCodefromSpin);
                notesupload.putExtra("Semester", semSelected);
                notesupload.putExtra("SubjectCode", subjectSelected);
                startActivity(notesupload);
            }
        });
    }

    @Override
    protected void onResume() {
        if (Flag == 0){
            addNotesBtn.setVisibility(View.INVISIBLE);
        }
        super.onResume();
    }
}