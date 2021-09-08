package com.example.student;


import android.content.DialogInterface;
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

public class facultysignup extends AppCompatActivity {

    private EditText fCodeEdt2, fpasswordEdt2, ffNameEdt, flNameEdt, fcolgEdt, fphNoEdt, fcolcodeEdt, fcnPassEdt, femailEdt;
    private Button fsendDatabtn2;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FacultyInfo facultyInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facultysignup);

        fCodeEdt2 = findViewById(R.id.staffcode);
        fpasswordEdt2 = findViewById(R.id.fcpass);
        ffNameEdt = findViewById(R.id.ffirstname);
        flNameEdt= findViewById(R.id.flastname);
        fcolgEdt= findViewById(R.id.scolg);
        fphNoEdt= findViewById(R.id.sphnum);
        fcolcodeEdt= findViewById(R.id.fcolgcode);
        fcnPassEdt= findViewById(R.id.fcnpass);
        femailEdt = findViewById(R.id.femail);

        fsendDatabtn2 = findViewById(R.id.fsignupbtn2);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("FacultyInfo");
        facultyInfo = new FacultyInfo();

        fCodeEdt2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    String registerKey = fCodeEdt2.getText().toString();

                    Query checkUser = databaseReference.orderByChild("scode").equalTo(registerKey);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                AlertDialog.Builder registerdialog = new AlertDialog.Builder(facultysignup.this);
                                registerdialog.setMessage("The staff code you entered was already registered." +
                                        "\n\nTry logging in or try with a new staff code.");
                                registerdialog.setTitle("Staff Code Exists");
                                registerdialog.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                fCodeEdt2.setSelection(registerKey.length());
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alertDialog=registerdialog.create();
                                alertDialog.setCancelable(false);
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.show();
                            }else {
                                fsendDatabtn2.setVisibility(View.VISIBLE);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(facultysignup.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        femailEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){

                    String mailKey = femailEdt.getText().toString();

                    Query checkUser = databaseReference.orderByChild("email").equalTo(mailKey);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                AlertDialog.Builder emaildiaog = new AlertDialog.Builder(facultysignup.this);
                                emaildiaog.setMessage("The E-Mail ID you entered was already registered." +
                                        "\n\nTry with a new E-Mail Address.");
                                emaildiaog.setTitle("Mail ID Exists");
                                emaildiaog.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                femailEdt.setSelection(mailKey.length());
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alertDialog=emaildiaog.create();
                                alertDialog.setCancelable(false);
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.show();
                            }else {
                                fcnPassEdt.setEnabled(true);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(facultysignup.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        fphNoEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){

                    String phnoKey = fphNoEdt.getText().toString();

                    Query checkUser = databaseReference.orderByChild("phNumber").equalTo(phnoKey);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                AlertDialog.Builder phnodialog = new AlertDialog.Builder(facultysignup.this);
                                phnodialog.setMessage("The mobile number you entered was already registered." +
                                        "\n\nTry with a new Mobile Number.");
                                phnodialog.setTitle("Mobile Number Exists");
                                phnodialog.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                fphNoEdt.setSelection(phnoKey.length());
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alertDialog=phnodialog.create();
                                alertDialog.setCancelable(false);
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.show();
                            }else {
                                fpasswordEdt2.setEnabled(true);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(facultysignup.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        fpasswordEdt2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus){ AlertDialog.Builder passworddialog = new AlertDialog.Builder(facultysignup.this);
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
                    String scpass = fpasswordEdt2.getText().toString();
                    boolean psstate = validatePassword(scpass);

                    if (psstate) {
                        fsendDatabtn2.setEnabled(true);
                    } else {
                        AlertDialog.Builder passworddialog = new AlertDialog.Builder(facultysignup.this);
                        passworddialog.setMessage("Your password doesn't match our criteria");
                        passworddialog.setTitle("Password Warning");
                        passworddialog.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        fpasswordEdt2.setSelection(scpass.length());
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

        fsendDatabtn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String fcode = fCodeEdt2.getText().toString();
                String fcpass = fpasswordEdt2.getText().toString();
                String fcnpass = fcnPassEdt.getText().toString();
                String ffname = ffNameEdt.getText().toString();
                String flname = flNameEdt.getText().toString();
                String fcolg = fcolgEdt.getText().toString();
                String fphno = fphNoEdt.getText().toString();
                String fcolcode = fcolcodeEdt.getText().toString();
                String femailid = femailEdt.getText().toString();

                if (TextUtils.isEmpty(fcode) || TextUtils.isEmpty(ffname) || TextUtils.isEmpty(flname) ||
                        TextUtils.isEmpty(fcolg) || TextUtils.isEmpty(fphno) || TextUtils.isEmpty(fcolcode) ||
                        TextUtils.isEmpty(femailid) || TextUtils.isEmpty(fcpass) || TextUtils.isEmpty(fcnpass)) {
                    Toast.makeText(facultysignup.this, "Please add all data.", Toast.LENGTH_SHORT).show();
                } else {
                    if(fcpass.equals(fcnpass)) {
                        alertDialog(fcode, ffname, flname, fcolg, fphno, fcolcode, femailid, fcpass);
                    }else{
                        Toast.makeText(facultysignup.this, "Both password not maches.", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }
    private void addDatatoFirebase(String fcode, String ffname, String flname, String fcolg, String fphno, String fcolcode,
                                   String femailid, String fcpass) {
        facultyInfo.setsSode(fcode);
        facultyInfo.setFirstName(ffname);
        facultyInfo.setLastName(flname);
        facultyInfo.setCollege(fcolg);
        facultyInfo.setPhNumber(fphno);
        facultyInfo.setFcollegeCode(fcolcode);
        facultyInfo.setEMail(femailid);
        facultyInfo.setFPassword(fcpass);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.child(fcode).setValue(facultyInfo);
                Toast.makeText(facultysignup.this, "Data added", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(facultysignup.this, "Fail to add data " + databaseError, Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void alertDialog(String fcode, String ffname, String flname, String fcolg, String fphno, String fcolcode,
                             String femailid, String fcpass){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Your data has added to database. If you want to edit the data added click edit.");
        dialog.setTitle("Firebase");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addDatatoFirebase(fcode, ffname, flname, fcolg, fphno, fcolcode, femailid, fcpass);
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