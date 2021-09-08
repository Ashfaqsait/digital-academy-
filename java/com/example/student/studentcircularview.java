package com.example.student;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

public class studentcircularview extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference fetchDatabase;

    TextView clgname, titleview, timeview, descriptionview, pvt1, pvt2;

    String collegename, collegecode, time, key;
    String pdflink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentcircularview);

        Intent intent = getIntent();

        collegename = intent.getStringExtra("CollegeName");
        collegecode = intent.getStringExtra("CollegeCode");
        time = intent.getStringExtra("Time");
        key = intent.getStringExtra("Key");

        clgname = findViewById(R.id.clgname);
        clgname.setText(collegename);

        titleview = findViewById(R.id.titleview);
        timeview = findViewById(R.id.timeview);
        descriptionview = findViewById(R.id.descriptionview);

        pvt1 = findViewById(R.id.pvt1);
        pvt2 = findViewById(R.id.pvt2);
        pvt2.setPaintFlags(pvt2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        fetchDatabase = firebaseDatabase.getReference("CircularInfo").child(collegecode);

        Query getCircularInfo = fetchDatabase.orderByChild("time").equalTo(time);

        getCircularInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {

                    String titleCir = dataSnapshot.child(key).child("title").getValue(String.class);
                    String descript = dataSnapshot.child(key).child("description").getValue(String.class);
                            pdflink  = dataSnapshot.child(key).child("pdflink").getValue(String.class);

                    if (pdflink.equals("null")) {
                        pvt1.setVisibility(View.INVISIBLE);
                        pvt2.setVisibility(View.INVISIBLE);
                    }

                    titleview.setText(titleCir);
                    timeview.setText(time);
                    descriptionview.setText(descript);

                    //Toast.makeText(studentcircularview.this, descript, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(studentcircularview.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });


        pvt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence options[] = new CharSequence[]{"Download", "View", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Choose One");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
// we will be downloading the pdf
                        if (which == 0) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdflink));
                            startActivity(intent);
                        }
// We will view the pdf
                        if (which == 1) {
                            Intent intent = new Intent(v.getContext(), ViewPdf.class);
                            intent.putExtra("Url", pdflink);
                            startActivity(intent);
                        }
                    }
                });
                builder.show();
            }
        });


    }
}


/*

 */