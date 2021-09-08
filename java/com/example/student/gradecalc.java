package com.example.student;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class gradecalc extends AppCompatActivity {

    private Spinner semspinner, sub1spin, sub2spin, sub3spin, sub4spin, sub5spin, sub6spin, sub7spin, sub8spin, sub9spin, sub10spin;
    private TextView deptdisplay, s1, s2, s3, s4, s5, s6, s7, s8, s9, s10;
    private Button calculate, otherdepartmentSelect;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private String semGotfromspin, deptName, dept;
    private String[] creditArray = new String[10];
    private String[] gradefromSpin = new String[10];
    private float[] gradePoints = new float[10];
    private int number_of_subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradecalc);

        deptdisplay = findViewById(R.id.dpView);

        Intent intent = getIntent();
        deptName = intent.getStringExtra("dptname");
        dept = intent.getStringExtra("department");
        String collgename = intent.getStringExtra("collegeName");


        deptdisplay.setText(deptName);

        //...................................................................................
        semspinner = findViewById(R.id.selsemsp);

        final String[] semesters = {"1", "2", "3", "4", "5", "6", "7", "8"};
        semspinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, semesters));

        semspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semGotfromspin = parent.getItemAtPosition(position).toString();

                //.........................................

                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("GradeInfo").child(dept).child(semGotfromspin);

                databaseReference.addListenerForSingleValueEvent (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            String sub = dataSnapshot1.getKey().toString();
                            assignSubcodetoTextview(i, sub);
                            creditArray[i] = dataSnapshot1.child("credit").getValue().toString();
                            i++;
                        }
                        disableExtraSubject(i);
                        number_of_subjects = i;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(gradecalc.this, "Database Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //.........................................

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(gradecalc.this, "Please Select", Toast.LENGTH_SHORT).show();
            }

        }); //end of Listener function

        //........................................................................................


        //............. linking with xml grade spinners ........
        sub1spin = findViewById(R.id.sub1sp);
        sub2spin = findViewById(R.id.sub2sp);
        sub3spin = findViewById(R.id.sub3sp);
        sub4spin = findViewById(R.id.sub4sp);
        sub5spin = findViewById(R.id.sub5sp);
        sub6spin = findViewById(R.id.sub6sp);
        sub7spin = findViewById(R.id.sub7sp);
        sub8spin = findViewById(R.id.sub8sp);
        sub9spin = findViewById(R.id.sub9sp);
        sub10spin = findViewById(R.id.sub10sp);
        //......................................................

        // ............. linking with xml text view.............
        s1 = findViewById(R.id.sub1View);
        s2 = findViewById(R.id.sub2View);
        s3 = findViewById(R.id.sub3View);
        s4 = findViewById(R.id.sub4View);
        s5 = findViewById(R.id.sub5View);
        s6 = findViewById(R.id.sub6View);
        s7 = findViewById(R.id.sub7View);
        s8 = findViewById(R.id.sub8View);
        s9 = findViewById(R.id.sub9View);
        s10 = findViewById(R.id.sub10View);
        //.........................................................


        final String[] grades = {"O", "A+", "A", "B+", "B", "U"};
        sub1spin.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, grades));
        sub2spin.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, grades));
        sub3spin.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, grades));
        sub4spin.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, grades));
        sub5spin.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, grades));
        sub6spin.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, grades));
        sub7spin.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, grades));
        sub8spin.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, grades));
        sub9spin.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, grades));
        sub10spin.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, grades));

        //.......................................................................................................
        sub1spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gradefromSpin[0] = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(gradecalc.this, "Please Select", Toast.LENGTH_SHORT).show();
            }
        }); //end of Listener function
        //.......................................................................................................
        sub2spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gradefromSpin[1] = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(gradecalc.this, "Please Select", Toast.LENGTH_SHORT).show();
            }
        }); //end of Listener function
        //.......................................................................................................
        sub3spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gradefromSpin[2] = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(gradecalc.this, "Please Select", Toast.LENGTH_SHORT).show();
            }
        }); //end of Listener function
        //.......................................................................................................
        sub4spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gradefromSpin[3] = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(gradecalc.this, "Please Select", Toast.LENGTH_SHORT).show();
            }
        }); //end of Listener function
        //.......................................................................................................
        sub5spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gradefromSpin[4] = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(gradecalc.this, "Please Select", Toast.LENGTH_SHORT).show();
            }
        }); //end of Listener function
        //.......................................................................................................
        sub6spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gradefromSpin[5] = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(gradecalc.this, "Please Select", Toast.LENGTH_SHORT).show();
            }
        }); //end of Listener function
        //.......................................................................................................
        sub7spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gradefromSpin[6] = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(gradecalc.this, "Please Select", Toast.LENGTH_SHORT).show();
            }
        }); //end of Listener function
        //.......................................................................................................
        sub8spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gradefromSpin[7] = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(gradecalc.this, "Please Select", Toast.LENGTH_SHORT).show();
            }
        }); //end of Listener function
        //.......................................................................................................
        sub9spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gradefromSpin[8] = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(gradecalc.this, "Please Select", Toast.LENGTH_SHORT).show();
            }
        }); //end of Listener function
        //.......................................................................................................
        sub10spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gradefromSpin[9] = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(gradecalc.this, "Please Select", Toast.LENGTH_SHORT).show();
            }
        }); //end of Listener function
        //.......................................................................................................


        calculate = findViewById(R.id.submitbtn);


        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float gpa;
                float gpaNumerator = 0, gpaDinomenator = 0, tempCredit = 0;
                for(int a=0; a<number_of_subjects; a++) {

                    if (gradefromSpin[a].equals("O"))
                        gradePoints[a] = 10;
                    else if (gradefromSpin[a].equals("A+"))
                        gradePoints[a] = 9;
                    else if (gradefromSpin[a].equals("A"))
                        gradePoints[a] = 8;
                    else if (gradefromSpin[a].equals("B+"))
                        gradePoints[a] = 7;
                    else if (gradefromSpin[a].equals("B"))
                        gradePoints[a] = 6;
                    else if (gradefromSpin[a].equals("U"))
                        gradePoints[a] = 0;
                    else
                        Toast.makeText(gradecalc.this, "Grade Error", Toast.LENGTH_SHORT).show();

                    try {
                        tempCredit = Integer.parseInt(creditArray[a]);
                        gpaNumerator += tempCredit * gradePoints[a];
                        gpaDinomenator += tempCredit;
                    }
                    catch (Exception e) {
                        Toast.makeText(gradecalc.this, "Error - " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                gpa = gpaNumerator / gpaDinomenator;
                float roundedGpa = (float) ((float)Math.round(gpa * 100.0) / 100.0);
                String tempGpa = Float.toString(roundedGpa);

                alertDialog(tempGpa);
            }
        });

        //.......................................................................................

        otherdepartmentSelect = findViewById(R.id.odpbtn);

        otherdepartmentSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherdeptselect = new Intent(getApplicationContext(), otherdeptselect.class);
                otherdeptselect.putExtra("collegeName", collgename);
                startActivity(otherdeptselect);
            }
        });

    }



//.....................assigning subject code to the Text View....................

    void assignSubcodetoTextview(int iter, String subcd) {
        switch (iter){
            case 0:
                s1.setText(subcd);
                break;
            case 1:
                s2.setText(subcd);
                break;
            case 2:
                s3.setText(subcd);
                break;
            case 3:
                s4.setVisibility(View.VISIBLE);
                s4.setText(subcd);
                sub4spin.setVisibility(View.VISIBLE);
                break;
            case 4:
                s5.setVisibility(View.VISIBLE);
                s5.setText(subcd);
                sub5spin.setVisibility(View.VISIBLE);
                break;
            case 5:
                s6.setVisibility(View.VISIBLE);
                s6.setText(subcd);
                sub6spin.setVisibility(View.VISIBLE);
                break;
            case 6:
                s7.setVisibility(View.VISIBLE);
                s7.setText(subcd);
                sub7spin.setVisibility(View.VISIBLE);
                break;
            case 7:
                s8.setVisibility(View.VISIBLE);
                s8.setText(subcd);
                sub8spin.setVisibility(View.VISIBLE);
                break;
            case 8:
                s9.setVisibility(View.VISIBLE);
                s9.setText(subcd);
                sub9spin.setVisibility(View.VISIBLE);
            case 9:
                s10.setVisibility(View.VISIBLE);
                s10.setText(subcd);
                sub10spin.setVisibility(View.VISIBLE);
        }
    }
    void disableExtraSubject(int j){
        for(int ashfaq=j; ashfaq<=9; ashfaq++){
            switch (ashfaq) {
                case 3:
                    s4.setVisibility(View.GONE);
                    sub4spin.setVisibility(View.GONE);
                    break;
                case 4:
                    s5.setVisibility(View.GONE);
                    sub5spin.setVisibility(View.GONE);
                    break;
                case 5:
                    s6.setVisibility(View.GONE);
                    sub6spin.setVisibility(View.GONE);
                    break;
                case 6:
                    s7.setVisibility(View.GONE);
                    sub7spin.setVisibility(View.GONE);
                    break;
                case 7:
                    s8.setVisibility(View.GONE);
                    sub8spin.setVisibility(View.GONE);
                    break;
                case 8:
                    s9.setVisibility(View.GONE);
                    sub9spin.setVisibility(View.GONE);
                case 9:
                    s10.setVisibility(View.GONE);
                    sub10spin.setVisibility(View.GONE);
            }
        }

    }


    private void alertDialog(String gpa_print){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Your GPA is " + gpa_print);
        dialog.setTitle("GPA CALCULATOR");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog=dialog.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

} //end of class









/*
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".gradecalc">

    <TextView
        android:id="@+id/dpView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"

        android:text="Computer Science Engineering"
        android:textColor="#0D0D0E"
        android:textColorHighlight="#0E73E1"
        android:textColorLink="#E1DBDB"
        android:textSize="20dp"
        android:textStyle="italic"

        android:textAlignment="center"
        android:gravity="center_horizontal"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.03" />
    <!-- android:text="Welcome !!!" -->

    <View
        android:layout_width="match_parent"
        android:layout_height="10dp"
        android:background="#D53232"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0"
        app:layout_constraintStart_toStartOf="parent"

        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.08" />

    <TextView
        android:id="@+id/txtView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Select your Semester : "
        android:textColor="#0D0D0E"
        android:textColorHighlight="#0E73E1"
        android:textColorLink="#E1DBDB"
        android:textSize="20dp"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.2"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.12" />

    <Spinner
        android:id="@+id/selsemsp"
        android:layout_width="100dp"
        android:layout_height="wrap_content"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.9"
        app:layout_constraintStart_toStartOf="parent"

        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.12" />

    <!--android:prompt="@string/sem_prompt" -->

    <View
        android:layout_width="match_parent"
        android:layout_height="10dp"
        android:background="#D53232"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0"
        app:layout_constraintStart_toStartOf="parent"

        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.17" />

    <TextView
        android:id="@+id/sub1View"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Subject 1 : "
        android:textColor="#0D0D0E"
        android:textColorHighlight="#0E73E1"
        android:textColorLink="#E1DBDB"
        android:textSize="20dp"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.2"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.22" />

    <Spinner
        android:id="@+id/sub1sp"
        android:layout_width="100dp"
        android:layout_height="wrap_content"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.7"
        app:layout_constraintStart_toStartOf="parent"

        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.22" />

    <TextView
        android:id="@+id/sub2View"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Subject 1 : "
        android:textColor="#0D0D0E"
        android:textColorHighlight="#0E73E1"
        android:textColorLink="#E1DBDB"
        android:textSize="20dp"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.2"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.28" />

    <Spinner
        android:id="@+id/sub2sp"
        android:layout_width="100dp"
        android:layout_height="wrap_content"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.7"
        app:layout_constraintStart_toStartOf="parent"

        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.28" />

    <!-- ........................................... -->

    <TextView
        android:id="@+id/sub3View"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Subject 2 : "
        android:textColor="#0D0D0E"
        android:textColorHighlight="#0E73E1"
        android:textColorLink="#E1DBDB"
        android:textSize="20dp"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.2"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.34" />

    <Spinner
        android:id="@+id/sub3sp"
        android:layout_width="100dp"
        android:layout_height="wrap_content"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.7"
        app:layout_constraintStart_toStartOf="parent"

        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.34" />

    <!-- ........................................... -->

    <TextView
        android:id="@+id/sub4View"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Subject 3 : "
        android:textColor="#0D0D0E"
        android:textColorHighlight="#0E73E1"
        android:textColorLink="#E1DBDB"
        android:textSize="20dp"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.2"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.40" />

    <Spinner
        android:id="@+id/sub4sp"
        android:layout_width="100dp"
        android:layout_height="wrap_content"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.7"
        app:layout_constraintStart_toStartOf="parent"

        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.40" />

    <!-- ........................................... -->

    <TextView
        android:id="@+id/sub5View"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Subject 4 : "
        android:textColor="#0D0D0E"
        android:textColorHighlight="#0E73E1"
        android:textColorLink="#E1DBDB"
        android:textSize="20dp"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.2"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.46" />

    <Spinner
        android:id="@+id/sub5sp"
        android:layout_width="100dp"
        android:layout_height="wrap_content"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.7"
        app:layout_constraintStart_toStartOf="parent"

        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.46" />

    <!-- ........................................... -->

    <TextView
        android:id="@+id/sub6View"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Subject 5 : "
        android:textColor="#0D0D0E"
        android:textColorHighlight="#0E73E1"
        android:textColorLink="#E1DBDB"
        android:textSize="20dp"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.2"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.52" />

    <Spinner
        android:id="@+id/sub6sp"
        android:layout_width="100dp"
        android:layout_height="wrap_content"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.7"
        app:layout_constraintStart_toStartOf="parent"

        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.52" />

    <!-- ........................................... -->

    <TextView
        android:id="@+id/sub7View"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Subject 6 : "
        android:textColor="#0D0D0E"
        android:textColorHighlight="#0E73E1"
        android:textColorLink="#E1DBDB"
        android:textSize="20dp"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.2"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.58" />

    <Spinner
        android:id="@+id/sub7sp"
        android:layout_width="100dp"
        android:layout_height="wrap_content"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.7"
        app:layout_constraintStart_toStartOf="parent"

        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.58" />

    <!-- ........................................... -->

    <TextView
        android:id="@+id/sub8View"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Subject 7 : "
        android:textColor="#0D0D0E"
        android:textColorHighlight="#0E73E1"
        android:textColorLink="#E1DBDB"
        android:textSize="20dp"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.2"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.64" />

    <Spinner
        android:id="@+id/sub8sp"
        android:layout_width="100dp"
        android:layout_height="wrap_content"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.7"
        app:layout_constraintStart_toStartOf="parent"

        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.64" />

    <!-- ........................................... -->

    <TextView
        android:id="@+id/sub9View"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Subject 8 : "
        android:textColor="#0D0D0E"
        android:textColorHighlight="#0E73E1"
        android:textColorLink="#E1DBDB"
        android:textSize="20dp"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.2"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.70" />

    <Spinner
        android:id="@+id/sub9sp"
        android:layout_width="100dp"
        android:layout_height="wrap_content"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.7"
        app:layout_constraintStart_toStartOf="parent"

        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.70" />

    <!-- ........................................... -->

    <TextView
        android:id="@+id/sub10View"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Subject 9 : "
        android:textColor="#0D0D0E"
        android:textColorHighlight="#0E73E1"
        android:textColorLink="#E1DBDB"
        android:textSize="20dp"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.2"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.76" />

    <Spinner
        android:id="@+id/sub10sp"
        android:layout_width="100dp"
        android:layout_height="wrap_content"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.7"
        app:layout_constraintStart_toStartOf="parent"

        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.76" />

    <!-- ........................................... -->

    <Button
        android:id="@+id/submitbtn"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="calculate gpa"
        android:textSize="20sp"
        app:backgroundTint="#00BCD4"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.5"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.86" />

    <!-- ........................................... -->

    <TextView
        android:id="@+id/odpView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="To see other department grades : "
        android:textColor="#0D0D0E"
        android:textColorHighlight="#0E73E1"
        android:textColorLink="#E1DBDB"
        android:textSize="15dp"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.1"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.94" />

    <Button
        android:id="@+id/odpbtn"
        android:layout_width="80dp"
        android:layout_height="30dp"
        android:text="Click here"
        android:textSize="10sp"
        app:backgroundTint="#00BCD4"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.9"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.94" />

</androidx.constraintlayout.widget.ConstraintLayout>
 */