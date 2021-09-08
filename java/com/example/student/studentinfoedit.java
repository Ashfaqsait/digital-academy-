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

public class studentinfoedit extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference sdatabaseReference, fdatabaseReference;

    EditText sfirstname, slastname, sregno, sdepartment, sdobEdt, sphnoEdt, semail;
    TextView changePassBtn;
    Button ssignupbtn3;
    String regno;

    String sfname, slname, sdept, sdob, sphno, semailid, password;

    StudentInfo studentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentinfoedit);


        Intent intent = getIntent();
        regno = intent.getStringExtra("regno");

        studentInfo = new StudentInfo();

        sfirstname = findViewById(R.id.sfirstname);
        slastname = findViewById(R.id.slastname);
        sregno = findViewById(R.id.sregno);
        sdepartment = findViewById(R.id.sdepartment);
        sdobEdt = findViewById(R.id.sdob);
        sphnoEdt = findViewById(R.id.sphno);
        semail = findViewById(R.id.semail);
        ssignupbtn3 = findViewById(R.id.ssignupbtn3);

        sdatabaseReference = firebaseDatabase.getReference("StudentInfo");

        Query checkUser2 = sdatabaseReference.orderByChild("regNo").equalTo(regno);

        checkUser2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                sfname = dataSnapshot.child(regno).child("firstName").getValue(String.class);
                slname = dataSnapshot.child(regno).child("lastName").getValue(String.class);
                sdept = dataSnapshot.child(regno).child("department").getValue(String.class);
                sdob = dataSnapshot.child(regno).child("birthDate").getValue(String.class);
                sphno = dataSnapshot.child(regno).child("phNumber").getValue(String.class);
                semailid = dataSnapshot.child(regno).child("email").getValue(String.class);
                password = dataSnapshot.child(regno).child("spassword").getValue(String.class);

                sfirstname.setText(sfname);
                slastname.setText(slname);
                sregno.setText(regno);
                sdepartment.setText(sdept);
                sdobEdt.setText(sdob);
                sphnoEdt.setText(sphno);
                semail.setText(semailid);;

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

                String fcode = sregno.getText().toString();
                String ffname = sfirstname.getText().toString();
                String flname = slastname.getText().toString();
                String fcolg = sdobEdt.getText().toString();
                String fphno = sphnoEdt.getText().toString();
                String fcolcode = sdepartment.getText().toString();
                String femailid = semail.getText().toString();

                if (TextUtils.isEmpty(fcode) || TextUtils.isEmpty(ffname) || TextUtils.isEmpty(flname) ||
                        TextUtils.isEmpty(fcolg) || TextUtils.isEmpty(fphno) || TextUtils.isEmpty(fcolcode) ||
                        TextUtils.isEmpty(femailid)) {
                    Toast.makeText(studentinfoedit.this, "Please avoid empty data.", Toast.LENGTH_SHORT).show();
                } else {

                    Intent changePassBtn = new Intent(studentinfoedit.this, changepassword.class);
                    changePassBtn.putExtra("Flag", 0);
                    changePassBtn.putExtra("menuFlag", 1);

                    changePassBtn.putExtra("regno", fcode);
                    changePassBtn.putExtra("sfname", ffname);
                    changePassBtn.putExtra("slname", flname);
                    changePassBtn.putExtra("sdept", fcolcode);
                    changePassBtn.putExtra("sdob", fcolg);
                    changePassBtn.putExtra("sphno", fphno);
                    changePassBtn.putExtra("semail", femailid);

                    startActivity(changePassBtn);
                    finish();

                }
            }
        });




        ssignupbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fcode = sregno.getText().toString();
                String ffname = sfirstname.getText().toString();
                String flname = slastname.getText().toString();
                String fcolg = sdobEdt.getText().toString();
                String fphno = sphnoEdt.getText().toString();
                String fcolcode = sdepartment.getText().toString();
                String femailid = semail.getText().toString();

                if (TextUtils.isEmpty(fcode) || TextUtils.isEmpty(ffname) || TextUtils.isEmpty(flname) ||
                        TextUtils.isEmpty(fcolg) || TextUtils.isEmpty(fphno) || TextUtils.isEmpty(fcolcode) ||
                        TextUtils.isEmpty(femailid)) {
                    Toast.makeText(studentinfoedit.this, "Please add all data.", Toast.LENGTH_SHORT).show();
                } else {
                    alertDialog(fcode, ffname, flname, fcolg, fphno, fcolcode, femailid);
                }
            }
        });



    }

    private void alertDialog(String fcode, String ffname, String flname, String fcolg, String fphno, String fcolcode,
                             String femailid){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Your data has added to database. If you want to edit the data added click edit.");
        dialog.setTitle("Firebase");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addDatatoFirebase(fcode, ffname, flname, fcolg, fphno, fcolcode, femailid);
                        Intent facultylogin = new Intent(studentinfoedit.this, studentlogin.class);
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



    private void addDatatoFirebase(String fcode, String ffname, String flname, String fcolg, String fphno, String fcolcode,
                                   String femailid) {
        studentInfo.setRegNo(fcode);
        studentInfo.setFirstName(ffname);
        studentInfo.setLastName(flname);
        studentInfo.setBirthDate(fcolg);
        studentInfo.setPhNumber(fphno);
        studentInfo.setDepartment(fcolcode);
        studentInfo.setEMail(femailid);
        studentInfo.setSPassword(password);

        fdatabaseReference = firebaseDatabase.getReference("StudentInfo");

        fdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fdatabaseReference.child(fcode).setValue(studentInfo);
                Toast.makeText(studentinfoedit.this, "Data added", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(studentinfoedit.this, "Fail to add data " + databaseError, Toast.LENGTH_SHORT).show();
            }
        });



    }
}






/*





        ssignupbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fcode = sregno.getText().toString();
                String ffname = sfirstname.getText().toString();
                String flname = slastname.getText().toString();
                String fcolg = sdobEdt.getText().toString();
                String fphno = sphnoEdt.getText().toString();
                String fcolcode = sdepartment.getText().toString();
                String femailid = semail.getText().toString();

                if (TextUtils.isEmpty(fcode) || TextUtils.isEmpty(ffname) || TextUtils.isEmpty(flname) ||
                        TextUtils.isEmpty(fcolg) || TextUtils.isEmpty(fphno) || TextUtils.isEmpty(fcolcode) ||
                        TextUtils.isEmpty(femailid)) {
                    Toast.makeText(studentinfoedit.this, "Please add all data.", Toast.LENGTH_SHORT).show();
                } else {
                        alertDialog(fcode, ffname, flname, fcolg, fphno, fcolcode, femailid);
                }
            }
        });

 */