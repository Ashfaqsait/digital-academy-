package com.example.student;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class infopage extends AppCompatActivity {
    //int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infopage);


        ImageView developimage = findViewById(R.id.devimg);
        TextView developname = findViewById(R.id.devname);

        int[] imgArray = {R.drawable.ashfaq, R.drawable.rohith, R.drawable.sarath, R.drawable.sathish};
        String[] nameArray = {"Ashfaq", "Rohith", "Sarath", "Sathish"};

        final Handler handler = new Handler();
        Runnable r = new Runnable(){
            int i = 0;
            public void run(){
                developimage.setImageResource(imgArray[i]);
                developname.setText(nameArray[i]);
                i++;
                if (i >= imgArray.length){
                    i = 0;
                }
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(r, 2000);



    }
}