package com.example.student;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class otherdeptselect extends AppCompatActivity {

    TextView collegeNameView;
    Spinner selectOtherdept;
    Button proceedtogpa;

    String deptCodefromspin, deptNamefromspin;

    final String[] deptNamefromDB = {"Computer Science and Engineering", "Civil Engineering", "Electrical and Electronics Engineering",
    "Electronics and Communication Engineering", "Mechanical Engineering", "Information Technology", "Aeronautical Engineering",
    "Biomedical Engineering", "Electronics and Instrumentation Engineering", "Bio - Technology" };
    final String[] deptCodefromDB = {"104", "103", "105", "106", "114", "205", "101", "121", "107", "214" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherdeptselect);

        Intent intent = getIntent();
        String collegename = intent.getStringExtra("collegeName");

        collegeNameView = findViewById(R.id.collegeViewinod);
        collegeNameView.setText(collegename);

        //.......................................................................................................

        selectOtherdept = findViewById(R.id.otherdeptSelect);
        selectOtherdept.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, deptNamefromDB));

        //........................................................................................................
        selectOtherdept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int temp = parent.getSelectedItemPosition();
                deptCodefromspin = deptCodefromDB[temp];
                deptNamefromspin = deptNamefromDB[temp];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(otherdeptselect.this, "Please Select", Toast.LENGTH_SHORT).show();
            }
        }); //end of Listener function
        //........................................................................................................
        proceedtogpa = findViewById(R.id.proceedtogpa);
        proceedtogpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gradecalc = new Intent(getApplicationContext(), gradecalc.class);
                gradecalc.putExtra("dptname", deptNamefromspin);
                gradecalc.putExtra("department", deptCodefromspin);
                gradecalc.putExtra("collegeName", collegename);
                startActivity(gradecalc);
            }
        });
    }

}