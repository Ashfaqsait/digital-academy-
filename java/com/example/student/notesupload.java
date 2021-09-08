package com.example.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class notesupload extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference uploadpdfDB, deptlistdatabaseReference;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    String CollegeName, CollegeCode, deptCodefromSpin, semSelected, subjectSelected;
    String myurl = "null";
    String timestamp;

    TextView colgView, deptView, semesterView, subcodeView, addnotesbtn;
    EditText pdfNameEdt, pdfTagsEdt;
    Button uploadnotesbtn;

    ProgressDialog dialog;
    Uri imageuri;

    NotesInfo notesInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notesupload);

        Intent intent = getIntent();
        CollegeName = intent.getStringExtra("CollegeName");
        CollegeCode = intent.getStringExtra("CollegeCode");
        deptCodefromSpin = intent.getStringExtra("DepartmentCode");
        semSelected = intent.getStringExtra("Semester");
        subjectSelected = intent.getStringExtra("SubjectCode");

        colgView = findViewById(R.id.colgView);
        colgView.setText(CollegeName);

        semesterView = findViewById(R.id.semesterView);
        semesterView.append(semSelected);

        subcodeView = findViewById(R.id.subcodeView);
        subcodeView.setText(subjectSelected);

        pdfNameEdt = findViewById(R.id.notesname);
        pdfTagsEdt = findViewById(R.id.tagsname);

        deptView = findViewById(R.id.deptView);
        deptlistdatabaseReference = firebaseDatabase.getReference("DepartmentInfo").child(deptCodefromSpin);

        deptlistdatabaseReference.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String departmentList = dataSnapshot.child("deptName").getValue(String.class);
                deptView.setText(departmentList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(notesupload.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

        addnotesbtn = findViewById(R.id.addnotesbtn);

        addnotesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nTitle = pdfNameEdt.getText().toString();
                String nTags = pdfTagsEdt.getText().toString();

                if (nTitle.isEmpty() || nTags.isEmpty()){
                    Toast.makeText(notesupload.this, "Please add PDF name and Some tags...", Toast.LENGTH_SHORT).show();
                }else {
                    Intent galleryIntent = new Intent();
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                    // We will be redirected to choose pdf
                    galleryIntent.setType("application/pdf");
                    startActivityForResult(galleryIntent, 1);
                }
            }
        });


        uploadnotesbtn = findViewById(R.id.uploadnotesbtn);
        notesInfo = new NotesInfo();
        uploadnotesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nTitle = pdfNameEdt.getText().toString();
                String nTags = pdfTagsEdt.getText().toString();

                if (nTitle.isEmpty() || nTags.isEmpty()){
                    Toast.makeText(notesupload.this, "Please add PDF name and Some tags...", Toast.LENGTH_SHORT).show();
                }else {
                    notesInfo.setTitle(nTitle);
                    notesInfo.setTags(nTags);
                    notesInfo.setTimestamp(timestamp);
                    notesInfo.setPdflink(myurl);

                    uploadpdfDB = firebaseDatabase.getReference("NotesInfo").child(CollegeCode).child(subjectSelected);

                    uploadpdfDB.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            uploadpdfDB.child(timestamp).setValue(notesInfo);
                            Toast.makeText(notesupload.this, "Data added", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(notesupload.this, "Database Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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
            timestamp = "" + System.currentTimeMillis();
            String nTitle = pdfNameEdt.getText().toString();
            final String messagePushID = subjectSelected + " - " + nTitle;

            Toast.makeText(notesupload.this, imageuri.toString(), Toast.LENGTH_SHORT).show();

            // Here we are uploading the pdf in firebase storage with the name of current time
            final StorageReference filepath = storageReference.child(messagePushID + "." + "pdf");

            Toast.makeText(notesupload.this, filepath.getName(), Toast.LENGTH_SHORT).show();

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
                        uploadnotesbtn.setVisibility(View.VISIBLE);
                        addnotesbtn.setClickable(false);
                        Toast.makeText(notesupload.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(notesupload.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
                    }

                }

            });

        }

    }

}

/*



 */