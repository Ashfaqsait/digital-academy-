package com.example.student;

import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class changepassword extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference studentfetchDB, studentstoreDB, facultyfetchDB, facultystoreDB;

    EditText scpass, scnpass;
    Button ssignupbtn5;
    TextView welcomemessage;

    int Flag, menuFlag;
    String regNo, scode;

    String sfname, slname, sdept, sdob, sphno, semail;
    String ffname, flname, fcolg, fphno, fcolcode, femailid;

    StudentInfo studentInfo;
    FacultyInfo facultyInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        studentInfo = new StudentInfo();
        facultyInfo = new FacultyInfo();

        scpass = findViewById(R.id.scpass);
        scnpass = findViewById(R.id.scnpass);
        ssignupbtn5 = findViewById(R.id.ssignupbtn3);
        welcomemessage = findViewById(R.id.welcomemessage);

        Intent intent = getIntent();

        Flag = intent.getIntExtra("Flag", 2);

        if (Flag == 0){

            menuFlag = intent.getIntExtra("menuFlag", 2);
            regNo = intent.getStringExtra("regno");

            if (menuFlag == 0){
                getStudentDetails(regNo);

            }else if (menuFlag == 1) {
                sfname = intent.getStringExtra("sfname");
                slname = intent.getStringExtra("slname");
                sdept = intent.getStringExtra("sdept");
                sdob = intent.getStringExtra("sdob");
                sphno = intent.getStringExtra("sphno");
                semail = intent.getStringExtra("semail");
                welcomemessage.append(sfname);
            }
        }

        else if (Flag == 1){

            menuFlag = intent.getIntExtra("menuFlag", 2);
            scode = intent.getStringExtra("fcode");

            if (menuFlag == 0){
                getFacultyDetails(scode);

            }else if (menuFlag == 1) {
                ffname = intent.getStringExtra("ffname");
                flname = intent.getStringExtra("flname");
                fcolg = intent.getStringExtra("fcolg");
                fcolcode = intent.getStringExtra("fcolcode");
                fphno = intent.getStringExtra("fphno");
                femailid = intent.getStringExtra("femailid");
                welcomemessage.append(ffname);
            }
        }



        scpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus){ AlertDialog.Builder passworddialog = new AlertDialog.Builder(changepassword.this);
                    passworddialog.setMessage("1. Must have at least one numeric character\n" +
                            "2. Must have at least one lowercase character\n" +
                            "3. Must have at least one uppercase character\n" +
                            "4. Must have at least one special symbol among @#$%\n" +
                            "5. Password length should be between 8 and 20");
                    passworddialog.setTitle("Password Restrictions");
                    passworddialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog=passworddialog.create();
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                }else {
                    String pass = scpass.getText().toString();

                    String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(pass);

                    if (matcher.matches()) {
                        ssignupbtn5.setEnabled(true);
                    } else {
                        AlertDialog.Builder passworddialog = new AlertDialog.Builder(changepassword.this);
                        passworddialog.setMessage("Your password doesn't match our criteria");
                        passworddialog.setTitle("Password Warning");
                        passworddialog.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        scpass.setSelection(pass.length());
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alertDialog = passworddialog.create();
                        alertDialog.setCancelable(false);
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();

                    }
                }
            }
        });




        ssignupbtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fcpass = scpass.getText().toString();
                String fcnpass = scnpass.getText().toString();

                if (TextUtils.isEmpty(fcpass) || TextUtils.isEmpty(fcnpass)) {
                    Toast.makeText(changepassword.this, "Please enter both the password.", Toast.LENGTH_SHORT).show();
                } else {
                    if(fcpass.equals(fcnpass)) {

                        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(fcpass);
                        if (matcher.matches()){
                            alertDialog(fcpass);
                        }else {
                            Toast.makeText(changepassword.this, "Password does not matches the requirements...", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(changepassword.this, "Both password not maches.", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

    }

    private void alertDialog(String fcpass){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Your password has been changed...");
        dialog.setTitle("Firebase");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (Flag == 0) {
                            addSDatatoFirebase(fcpass);
                            finish();
                        }else if (Flag == 1){
                            addFDatatoFirebase(fcpass);
                            finish();
                        }
                    }
                });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();

    }

    private void addSDatatoFirebase(String fcpass) {

        studentInfo.setRegNo(regNo);
        studentInfo.setFirstName(sfname);
        studentInfo.setLastName(slname);
        studentInfo.setBirthDate(sdob);
        studentInfo.setPhNumber(sphno);
        studentInfo.setDepartment(sdept);
        studentInfo.setEMail(semail);
        studentInfo.setSPassword(fcpass);

        studentstoreDB = firebaseDatabase.getReference("StudentInfo");

        studentstoreDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentstoreDB.child(regNo).setValue(studentInfo);
                Toast.makeText(changepassword.this, "Data added", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(changepassword.this, "Fail to add data " + databaseError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addFDatatoFirebase(String fcpass) {

        facultyInfo.setsSode(scode);
        facultyInfo.setFirstName(ffname);
        facultyInfo.setLastName(flname);
        facultyInfo.setCollege(fcolg);
        facultyInfo.setPhNumber(fphno);
        facultyInfo.setFcollegeCode(fcolcode);
        facultyInfo.setEMail(femailid);
        facultyInfo.setFPassword(fcpass);

        facultystoreDB = firebaseDatabase.getReference("FacultyInfo");

        facultystoreDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                facultystoreDB.child(scode).setValue(facultyInfo);
                Toast.makeText(changepassword.this, "Data added", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(changepassword.this, "Fail to add data " + databaseError, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getStudentDetails(String key){

        studentfetchDB = firebaseDatabase.getReference("StudentInfo");

        Query checkUser = studentfetchDB.orderByChild("regNo").equalTo(key);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                sfname = dataSnapshot.child(key).child("firstName").getValue(String.class);
                slname = dataSnapshot.child(key).child("lastName").getValue(String.class);
                sdept = dataSnapshot.child(key).child("department").getValue(String.class);
                sdob = dataSnapshot.child(key).child("birthDate").getValue(String.class);
                sphno = dataSnapshot.child(key).child("phNumber").getValue(String.class);
                semail = dataSnapshot.child(key).child("email").getValue(String.class);
                welcomemessage.append(sfname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getFacultyDetails(String key){


        facultyfetchDB = firebaseDatabase.getReference("FacultyInfo");

        Query checkUser = facultyfetchDB.orderByChild("scode").equalTo(key);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ffname = dataSnapshot.child(key).child("firstName").getValue(String.class);
                flname = dataSnapshot.child(key).child("lastName").getValue(String.class);
                fcolg = dataSnapshot.child(key).child("college").getValue(String.class);
                fphno = dataSnapshot.child(key).child("phNumber").getValue(String.class);
                fcolcode = dataSnapshot.child(key).child("fcollegeCode").getValue(String.class);
                femailid = dataSnapshot.child(key).child("email").getValue(String.class);
                welcomemessage.append(ffname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}