package com.example.student;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    Button slbtn, flbtn, adminbtn;
    private String VersionIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     slbtn = findViewById(R.id.studentbutton);
     flbtn = findViewById(R.id.facultybutton);
     adminbtn = findViewById(R.id.adminbutton);

        slbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent studentlogin = new Intent(getApplicationContext(), studentlogin.class);
                startActivity(studentlogin);

            }
        });
        flbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

              Intent facultylogin = new Intent(getApplicationContext(), facultylogin.class);
              startActivity(facultylogin);
            }
        });

        adminbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminlogin = new Intent(MainActivity.this, adminlogin.class);
                startActivity(adminlogin);
            }
        });

    }

    @Override
    protected void onResume() {

        TextView sars;
        sars = findViewById(R.id.sars);

        boolean internetFlag = isNetworkAvailable(getBaseContext());
        if (internetFlag == true) {

            try{
                databaseReference = firebaseDatabase.getReference("VersionControlManager");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            VersionIdentifier = dataSnapshot1.child("Version").getValue().toString();
                        }

                        if (VersionIdentifier.equals("1")) {

                            slbtn.setEnabled(true);
                            flbtn.setEnabled(true);
                            adminbtn.setEnabled(true);

                            Typeface typeface1 = getResources().getFont(R.font.f1);
                            sars.setTypeface(typeface1);

                            ObjectAnimator animator = ObjectAnimator.ofInt(sars, "textColor", Color.RED, Color.BLUE,
                                    Color.YELLOW, Color.MAGENTA, Color.GREEN);
                            animator.setDuration(500);
                            animator.setEvaluator(new ArgbEvaluator());
                            animator.setRepeatMode(ValueAnimator.REVERSE);
                            animator.setRepeatCount(Animation.INFINITE);
                            animator.start();

                            //   changeFont();
                        } else {
                            AlertDialog.Builder versionDialog = new AlertDialog.Builder(MainActivity.this);
                            versionDialog.setMessage("Your app version is exhausted. So your app cannot be accessed hereafter. " +
                                    "To gain access to the application please update to the latest version immediately");
                            versionDialog.setTitle("Version Control Manager");
                            versionDialog.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            MainActivity.this.finish();
                                            System.exit(0);
                                        }
                                    });
                            AlertDialog alertDialog = versionDialog.create();
                            alertDialog.setCancelable(false);
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "databaseError", Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (DatabaseException exception){
                Toast.makeText(MainActivity.this, "Database Exception =" + exception.toString(), Toast.LENGTH_SHORT).show();
            }


        }
        else{
            Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_LONG).show();

            AlertDialog.Builder versionDialog = new AlertDialog.Builder(MainActivity.this);
            versionDialog.setMessage("No Internet is Connected. Please check your Internet Connection...");
            versionDialog.setTitle("Version Control Manager");
            versionDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            MainActivity.this.finish();
                            System.exit(0);
                        }
                    });
            AlertDialog alertDialog = versionDialog.create();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }


        super.onResume();
    }

    public static boolean isNetworkAvailable(Context context){

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null){

            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null){

                for (int i = 0; i < info.length; i++){

                    Log.i("Class", info[i].getState().toString());
                    if (info[i].getState() == NetworkInfo.State.CONNECTED){return  true;}
                }
            }
        }
        return  false;
    }


//    public void changeFont(){
//
//        if (fontCh){
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    sars.setTypeface(typeface1);
//                    fontCh = !fontCh;
//                    changeFont();
//                }
//            }, 5000);
//
//        }else {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    sars.setTypeface(typeface2);
//                    fontCh = !fontCh;
//                    changeFont();
//                }
//            }, 5000);
//        }
//
//    }

}

/*
 public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private String FLAG;

public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, "FlagStudent");

    }
 */