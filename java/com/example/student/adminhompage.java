package com.example.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class adminhompage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminhompage);

        //..........................................signup button.......................................
        Button addfaculty = findViewById(R.id.addfaculty);
        addfaculty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent facultysignup = new Intent(getApplicationContext(), facultysignup.class);
                startActivity(facultysignup);
            }
        });


    }
}

/*
<TextView
        android:id="@+id/sars"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"

        android:gravity="center_horizontal"
        android:text="SARS"
        android:textAlignment="center"
        android:textSize="60dp"
        android:textStyle="italic"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"

        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.92"/>
 */