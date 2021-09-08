package com.example.student;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class facultyinfoedit extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference sdatabaseReference, fdatabaseReference;

    TextView changePassBtn;
    EditText ffirstname, flastname, staffcode, scolg, fcolgcode, sphnum, femail;
    Button ssignupbtn4;

    String fcode, ffname, flname, fcolg, fphno, fcolcode, femailid, fcpass;

    FacultyInfo facultyInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facultyinfoedit);

        facultyInfo = new FacultyInfo();

        Intent intent = getIntent();
        fcode = intent.getStringExtra("fcode");

        ffirstname = findViewById(R.id.ffirstname);
        flastname = findViewById(R.id.flastname);
        staffcode = findViewById(R.id.staffcode);
        scolg = findViewById(R.id.scolg);
        fcolgcode = findViewById(R.id.fcolgcode);
        sphnum = findViewById(R.id.sphnum);
        femail = findViewById(R.id.femail);
        ssignupbtn4 = findViewById(R.id.ssignupbtn4);




        fdatabaseReference = firebaseDatabase.getReference("FacultyInfo");

        Query checkUser = fdatabaseReference.orderByChild("scode").equalTo(fcode);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ffname = dataSnapshot.child(fcode).child("firstName").getValue(String.class);
                flname = dataSnapshot.child(fcode).child("lastName").getValue(String.class);
                fcolg = dataSnapshot.child(fcode).child("college").getValue(String.class);
                fphno = dataSnapshot.child(fcode).child("phNumber").getValue(String.class);
                fcolcode = dataSnapshot.child(fcode).child("fcollegeCode").getValue(String.class);
                femailid = dataSnapshot.child(fcode).child("email").getValue(String.class);
                fcpass = dataSnapshot.child(fcode).child("fpassword").getValue(String.class);

                ffirstname.setText(ffname);
                flastname.setText(flname);
                staffcode.setText(fcode);
                scolg.setText(fcolg);
                fcolgcode.setText(fcolcode);
                sphnum.setText(fphno);
                femail.setText(femailid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        changePassBtn = findViewById(R.id.changepassbtn);
        changePassBtn.setPaintFlags(changePassBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String snumber = staffcode.getText().toString();
                String sfname = ffirstname.getText().toString();
                String slname = flastname.getText().toString();
                String sdept = scolg.getText().toString();
                String sdob = fcolgcode.getText().toString();
                String sphno = sphnum.getText().toString();
                String semailid = femail.getText().toString();

                if (TextUtils.isEmpty(snumber) || TextUtils.isEmpty(sfname) || TextUtils.isEmpty(slname) ||
                        TextUtils.isEmpty(sdept) || TextUtils.isEmpty(sdob) || TextUtils.isEmpty(sphno) ||
                        TextUtils.isEmpty(semailid)) {
                    Toast.makeText(facultyinfoedit.this, "Please avoid empty data.", Toast.LENGTH_SHORT).show();
                } else {

                    Intent changePassBtn = new Intent(facultyinfoedit.this, changepassword.class);
                    changePassBtn.putExtra("Flag", 1);
                    changePassBtn.putExtra("menuFlag", 1);

                    changePassBtn.putExtra("fcode", snumber);
                    changePassBtn.putExtra("ffname", sfname);
                    changePassBtn.putExtra("flname", slname);
                    changePassBtn.putExtra("fcolcode", sdob);
                    changePassBtn.putExtra("fcolg", sdept);
                    changePassBtn.putExtra("fphno", sphno);
                    changePassBtn.putExtra("femailid", semailid);

                    startActivity(changePassBtn);
                    finish();

                }
            }
        });


        ssignupbtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String snumber = staffcode.getText().toString();
                String sfname = ffirstname.getText().toString();
                String slname = flastname.getText().toString();
                String sdept = scolg.getText().toString();
                String sdob = fcolgcode.getText().toString();
                String sphno = sphnum.getText().toString();
                String semailid = femail.getText().toString();

                if (TextUtils.isEmpty(snumber) || TextUtils.isEmpty(sfname) || TextUtils.isEmpty(slname) ||
                        TextUtils.isEmpty(sdept) || TextUtils.isEmpty(sdob) || TextUtils.isEmpty(sphno) ||
                        TextUtils.isEmpty(semailid)) {
                    Toast.makeText(facultyinfoedit.this, "Please add all data.", Toast.LENGTH_SHORT).show();
                } else {
                    alertDialog(snumber, sfname, slname, sdept, sdob, sphno, semailid);
                }
            }
        });


    }


    private void alertDialog(String snumber, String sfname, String slname, String sdept, String sdob, String sphno,
                             String semailid){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Your data has added to database. If you want to edit the data added click edit.");
        dialog.setTitle("Firebase");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addDatatoFirebase(snumber, sfname, slname, sdept, sdob, sphno, semailid);
                        Intent facultylogin = new Intent(facultyinfoedit.this, facultylogin.class);
                        startActivity(facultylogin);
                        finish();
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


    private void addDatatoFirebase(String snumber, String sfname, String slname, String sdept, String sdob, String sphno,
                                   String semailid) {
        facultyInfo.setsSode(snumber);
        facultyInfo.setFirstName(sfname);
        facultyInfo.setLastName(slname);
        facultyInfo.setCollege(sdept);
        facultyInfo.setPhNumber(sphno);
        facultyInfo.setFcollegeCode(sdob);
        facultyInfo.setEMail(semailid);
        facultyInfo.setFPassword(fcpass);

        sdatabaseReference = firebaseDatabase.getReference("FacultyInfo");

        sdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sdatabaseReference.child(snumber).setValue(facultyInfo);
                Toast.makeText(facultyinfoedit.this, "Data added", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(facultyinfoedit.this, "Fail to add data " + databaseError, Toast.LENGTH_SHORT).show();
            }
        });



    }
}


/*

        fdatabaseReference = firebaseDatabase.getReference("FacultyInfo");

        Query checkUser = fdatabaseReference.orderByChild("sCode").equalTo(fcode);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ffname = dataSnapshot.child(fcode).child("firstName").getValue(String.class);
                flname = dataSnapshot.child(fcode).child("lastName").getValue(String.class);
                fcolg = dataSnapshot.child(fcode).child("college").getValue(String.class);
                fphno = dataSnapshot.child(fcode).child("phNumber").getValue(String.class);
                fcolcode = dataSnapshot.child(fcode).child("fcollegeCode").getValue(String.class);
                femailid = dataSnapshot.child(fcode).child("eMail").getValue(String.class);
                fcpass = dataSnapshot.child(fcode).child("fPassword").getValue(String.class);

                ffirstname.setText(ffname);
                flastname.setText(flname);
                staffcode.setText(fcode);
                scolg.setText(fcolg);
                fcolgcode.setText(fcolcode);
                sphnum.setText(fphno);
                femail.setText(femailid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        changePassBtn = findViewById(R.id.changepassbtn);
        changePassBtn.setPaintFlags(changePassBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String snumber = staffcode.getText().toString();
                String sfname = ffirstname.getText().toString();
                String slname = flastname.getText().toString();
                String sdept = scolg.getText().toString();
                String sdob = scolg.getText().toString();
                String sphno = sphnum.getText().toString();
                String semailid = femail.getText().toString();

                if (TextUtils.isEmpty(snumber) || TextUtils.isEmpty(sfname) || TextUtils.isEmpty(slname) ||
                        TextUtils.isEmpty(sdept) || TextUtils.isEmpty(sdob) || TextUtils.isEmpty(sphno) ||
                        TextUtils.isEmpty(semailid)) {
                    Toast.makeText(facultyinfoedit.this, "Please avoid empty data.", Toast.LENGTH_SHORT).show();
                } else {

                    Intent changePassBtn = new Intent(getApplicationContext(), changepassword.class);
                    changePassBtn.putExtra("Flag", 1);
                    changePassBtn.putExtra("menuFlag", 1);

                    changePassBtn.putExtra("fcode", snumber);
                    changePassBtn.putExtra("ffname", sfname);
                    changePassBtn.putExtra("flname", slname);
                    changePassBtn.putExtra("fcolcode", sdob);
                    changePassBtn.putExtra("fcolg", sdept);
                    changePassBtn.putExtra("fphno", sphno);
                    changePassBtn.putExtra("femailid", semailid);

                    startActivity(changePassBtn);

                }
            }
        });


        ssignupbtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String snumber = staffcode.getText().toString();
                String sfname = ffirstname.getText().toString();
                String slname = flastname.getText().toString();
                String sdept = scolg.getText().toString();
                String sdob = scolg.getText().toString();
                String sphno = sphnum.getText().toString();
                String semailid = femail.getText().toString();

                if (TextUtils.isEmpty(snumber) || TextUtils.isEmpty(sfname) || TextUtils.isEmpty(slname) ||
                        TextUtils.isEmpty(sdept) || TextUtils.isEmpty(sdob) || TextUtils.isEmpty(sphno) ||
                        TextUtils.isEmpty(semailid)) {
                    Toast.makeText(facultyinfoedit.this, "Please add all data.", Toast.LENGTH_SHORT).show();
                } else {
                    alertDialog(snumber, sfname, slname, sdept, sdob, sphno, semailid);
                }
            }
        });
 */