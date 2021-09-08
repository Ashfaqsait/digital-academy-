package com.example.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class circularUpload extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference circularDatabase;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private String collegeName, collegeCode;
    private String myurl = "null";

    Uri imageuri;

    TextView collegenameView, linkClick;
    Button circularUploadBtn;
    EditText circularTitleEdt, circularDespEdt;
    Switch pdfSwitch;

    ProgressDialog dialog;

    private String lastValue;

    CircularInfo circularInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular_upload);

        Intent intent = getIntent();
        collegeName = intent.getStringExtra("CollegeName");
        collegeCode = intent.getStringExtra("CollegeCode");

        collegenameView = findViewById(R.id.collegeName);
        collegenameView.setText(collegeName);

        linkClick = findViewById(R.id.linkclick);
        linkClick.setPaintFlags(linkClick.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        pdfSwitch = findViewById(R.id.pdfswitch);

        circularInfo = new CircularInfo();

//--------------------------------------------------------------------------------------------------
        circularTitleEdt = findViewById(R.id.circulartitleedt);
        circularTitleEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastValue = s.toString();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {          }
            @Override
            public void afterTextChanged(Editable s) {
                if (circularTitleEdt.getLineCount() > 2) {
                    Toast.makeText(circularUpload.this, "Title is limited to 2 lines...", Toast.LENGTH_SHORT).show();
                    int selectionStart = circularTitleEdt.getSelectionStart() - 1;
                    circularTitleEdt.setText(lastValue);
                    if (selectionStart >= circularTitleEdt.length()) {
                        selectionStart = circularTitleEdt.length();
                    }
                    circularTitleEdt.setSelection(selectionStart);
                }
            }
        });
//--------------------------------------------------------------------------------------------------


//--------------------------------------------------------------------------------------------------
        circularDespEdt = findViewById(R.id.circulardespedt);
        circularDespEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastValue = s.toString();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {          }
            @Override
            public void afterTextChanged(Editable s) {
                if (circularDespEdt.getLineCount() > 20) {
                    Toast.makeText(circularUpload.this, "Description is limited to 20 lines / 700 characters...", Toast.LENGTH_SHORT).show();
                    int selectionStart = circularDespEdt.getSelectionStart() - 1;
                    circularDespEdt.setText(lastValue);
                    if (selectionStart >= circularDespEdt.length()) {
                        selectionStart = circularDespEdt.length();
                    }
                    circularDespEdt.setSelection(selectionStart);
                }
            }
        });
//--------------------------------------------------------------------------------------------------

        pdfSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){linkClick.setVisibility(View.VISIBLE);}
                else{linkClick.setVisibility(View.INVISIBLE);}
            }
        });

        linkClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                // We will be redirected to choose pdf
                galleryIntent.setType("application/pdf");
                startActivityForResult(galleryIntent, 1);
            }
        });

        circularUploadBtn=findViewById(R.id.circularUploadbtn);

        circularUploadBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                String cTitle = circularTitleEdt.getText().toString();
                String cDescription = circularDespEdt.getText().toString();

                LocalDateTime dateObj = LocalDateTime.now();
                DateTimeFormatter fdateObj = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");
                String datePrint = dateObj.format(fdateObj);
                final String timestamp = "" + System.currentTimeMillis();

                circularInfo.setTitle(cTitle);
                circularInfo.setDescription(cDescription);
                circularInfo.setTime(datePrint);
                circularInfo.setPdflink(myurl);

                circularDatabase = firebaseDatabase.getReference("CircularInfo").child(collegeCode);

                circularDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        circularDatabase.child(timestamp).setValue(circularInfo);
                        Toast.makeText(circularUpload.this, "Data added", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(circularUpload.this, "Database Error" + datePrint, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // Here we are initialising the progress dialog box
            dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading");

            // this will show message uploading
            // while pdf is uploading

            dialog.show();
            imageuri = data.getData();
            final String timestamp = "" + System.currentTimeMillis();
            final String messagePushID = timestamp;

            Toast.makeText(circularUpload.this, imageuri.toString(), Toast.LENGTH_SHORT).show();

            // Here we are uploading the pdf in firebase storage with the name of current time
            final StorageReference filepath = storageReference.child(messagePushID + "." + "pdf");

            Toast.makeText(circularUpload.this, filepath.getName(), Toast.LENGTH_SHORT).show();

            filepath.putFile(imageuri).continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        // After uploading is done it progress
                        // dialog box will be dismissed
                        dialog.dismiss();

                        Uri uri = task.getResult();
                        myurl = uri.toString();
                        pdfSwitch.setEnabled(false);
                        linkClick.setClickable(false);
                        Toast.makeText(circularUpload.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(circularUpload.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
                    }

                }

            });

        }

    }

}