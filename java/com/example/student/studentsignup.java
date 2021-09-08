package com.example.student;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class studentsignup extends AppCompatActivity {

    private EditText regNoEdt2, passwordEdt2, sfNameEdt, slNameEdt, sdeptEdt, sdobEdt, sphnoEdt, scnPassEdt, semailEdt;
    private Button sendDatabtn2;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StudentInfo studentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentsignup);

        regNoEdt2 = findViewById(R.id.sregno);
        passwordEdt2 = findViewById(R.id.scpass);
        sfNameEdt = findViewById(R.id.sfirstname);
        slNameEdt= findViewById(R.id.slastname);
        sdeptEdt= findViewById(R.id.sdepartment);
        sdobEdt= findViewById(R.id.sdob);
        sphnoEdt= findViewById(R.id.sphno);
        scnPassEdt= findViewById(R.id.scnpass);
        semailEdt = findViewById(R.id.semail);

        sendDatabtn2 = findViewById(R.id.ssignupbtn2);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("StudentInfo");
        studentInfo = new StudentInfo();

        regNoEdt2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    String registerKey = regNoEdt2.getText().toString();

                    Query checkUser = databaseReference.orderByChild("regNo").equalTo(registerKey);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                AlertDialog.Builder registerdialog = new AlertDialog.Builder(studentsignup.this);
                                registerdialog.setMessage("The register number you entered was already registered." +
                                        "\n\nTry logging in or try with a new register number.");
                                registerdialog.setTitle("Register Number Exists");
                                registerdialog.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                regNoEdt2.setSelection(registerKey.length());
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alertDialog=registerdialog.create();
                                alertDialog.setCancelable(false);
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.show();
                            }else {
                                sendDatabtn2.setVisibility(View.VISIBLE);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(studentsignup.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        semailEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){

                    String mailKey = semailEdt.getText().toString();

                    Query checkUser = databaseReference.orderByChild("email").equalTo(mailKey);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                AlertDialog.Builder emaildiaog = new AlertDialog.Builder(studentsignup.this);
                                emaildiaog.setMessage("The E-Mail ID you entered was already registered." +
                                        "\n\nTry with a new E-Mail Address.");
                                emaildiaog.setTitle("Mail ID Exists");
                                emaildiaog.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                semailEdt.setSelection(mailKey.length());
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alertDialog=emaildiaog.create();
                                alertDialog.setCancelable(false);
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.show();
                            }else {
                                passwordEdt2.setEnabled(true);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(studentsignup.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        sphnoEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){

                    String phnoKey = sphnoEdt.getText().toString();

                    Query checkUser = databaseReference.orderByChild("phNumber").equalTo(phnoKey);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                AlertDialog.Builder phnodialog = new AlertDialog.Builder(studentsignup.this);
                                phnodialog.setMessage("The mobile number you entered was already registered." +
                                        "\n\nTry with a new Mobile Number.");
                                phnodialog.setTitle("Mobile Number Exists");
                                phnodialog.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                sphnoEdt.setSelection(phnoKey.length());
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alertDialog=phnodialog.create();
                                alertDialog.setCancelable(false);
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.show();
                            }else {
                                scnPassEdt.setEnabled(true);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(studentsignup.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        passwordEdt2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus){ AlertDialog.Builder passworddialog = new AlertDialog.Builder(studentsignup.this);
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
                    String scpass = passwordEdt2.getText().toString();
                    boolean psstate = validatePassword(scpass);

                    if (psstate) {
                        sendDatabtn2.setEnabled(true);
                    } else {
                        AlertDialog.Builder passworddialog = new AlertDialog.Builder(studentsignup.this);
                        passworddialog.setMessage("Your password doesn't match our criteria");
                        passworddialog.setTitle("Password Warning");
                        passworddialog.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        passwordEdt2.setSelection(scpass.length());
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


        sendDatabtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String snumber = regNoEdt2.getText().toString();
                String scpass = passwordEdt2.getText().toString();
                String scnpass = scnPassEdt.getText().toString();
                String sfname = sfNameEdt.getText().toString();
                String slname = slNameEdt.getText().toString();
                String sdept = sdeptEdt.getText().toString();
                String sdob = sdobEdt.getText().toString();
                String sphno = sphnoEdt.getText().toString();
                String semailid = semailEdt.getText().toString();

                if (TextUtils.isEmpty(snumber) || TextUtils.isEmpty(sfname) || TextUtils.isEmpty(slname) ||
                        TextUtils.isEmpty(sdept) || TextUtils.isEmpty(sdob) || TextUtils.isEmpty(sphno) ||
                        TextUtils.isEmpty(semailid) || TextUtils.isEmpty(scpass) || TextUtils.isEmpty(scnpass)) {
                    Toast.makeText(studentsignup.this, "Please add all data.", Toast.LENGTH_SHORT).show();
                } else {
                    if(scpass.equals(scnpass)) {
                        alertDialog(snumber, sfname, slname, sdept, sdob, sphno, semailid, scpass);
                    }else{
                        Toast.makeText(studentsignup.this, "Both password not maches.", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }
    private void addDatatoFirebase(String snumber, String sfname, String slname, String sdept, String sdob, String sphno,
                                   String semailid, String scpass) {
        studentInfo.setRegNo(snumber);
        studentInfo.setFirstName(sfname);
        studentInfo.setLastName(slname);
        studentInfo.setDepartment(sdept);
        studentInfo.setBirthDate(sdob);
        studentInfo.setPhNumber(sphno);
        studentInfo.setEMail(semailid);
        studentInfo.setSPassword(scpass);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.child(snumber).setValue(studentInfo);
                Toast.makeText(studentsignup.this, "data added", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(studentsignup.this, "Fail to add data " + databaseError, Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void alertDialog(String snumber, String sfname, String slname, String sdept, String sdob, String sphno,
                             String semailid, String scpass){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Your data has added to database. If you want to edit the data added click edit.");
        dialog.setTitle("Firebase");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addDatatoFirebase(snumber, sfname, slname, sdept, sdob, sphno, semailid, scpass);
                        Intent studentlogin = new Intent(getApplicationContext(), studentlogin.class);
                        startActivity(studentlogin);
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

    public static boolean validatePassword(String password){
                String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(password);
                return matcher.matches();
    }


}
