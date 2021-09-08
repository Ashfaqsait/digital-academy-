package com.example.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class forgotpassword extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference sdatabaseReference, fdatabaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private EditText keyEdt, otpGetter;
    private Button getOtpBtn, checkOtp;
    private TextView keynamedisplay, phnodisplay, phmessagedisplay, otptv2, rsotptv;

    ProgressDialog pd;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private String mVarificationId, phfromDB, keyValue;
    private static final String TAG ="MAIN_TAG";

    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        Intent intent = getIntent();
        flag = intent.getIntExtra("Flag", 2);

        keynamedisplay = findViewById(R.id.keynamedisplay);

        if (flag == 0){
            keynamedisplay.append("Register Number:");
        }else if (flag == 1){
            keynamedisplay.append("Staff Code:");
        }

        keyEdt = findViewById(R.id.keyGetter);
        getOtpBtn = findViewById(R.id.sendOtp);
        phnodisplay = findViewById(R.id.phnodispaly);
        phmessagedisplay = findViewById(R.id.textView);
        otptv2 = findViewById(R.id.otptv2);
        otpGetter = findViewById(R.id.otpGetter);
        rsotptv = findViewById(R.id.rsotptv);
        checkOtp = findViewById(R.id.checkOtp);




        //init progress dialogue
        pd=new ProgressDialog(forgotpassword.this);
        pd.setTitle("Please Wait.....");
        pd.setCanceledOnTouchOutside(false);

        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential){
                signInWhithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e){
                pd.dismiss();
                Toast.makeText(forgotpassword.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, forceResendingToken);

                Log.d(TAG,"onCodeSent:"+ verificationId);
                mVarificationId = verificationId;
                forceResendingToken = token;
                pd.dismiss();

                phmessagedisplay.setVisibility(View.VISIBLE);
                phnodisplay.setVisibility(View.VISIBLE);
                otptv2.setVisibility(View.VISIBLE);
                otpGetter.setVisibility(View.VISIBLE);
                rsotptv.setVisibility(View.VISIBLE);
                checkOtp.setVisibility(View.VISIBLE);

                Toast.makeText(forgotpassword.this,"Verification code sent....",Toast.LENGTH_SHORT).show();
                phnodisplay.setText(phfromDB);
            }
        };






        getOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOtpBtn.setEnabled(false);
                //..................................................................................
                keyValue = keyEdt.getText().toString();
                if (TextUtils.isEmpty(keyValue)) {
                    Toast.makeText(forgotpassword.this, "Please enter a valid key", Toast.LENGTH_SHORT).show();
                } else {

                    if (flag == 0){

                        sdatabaseReference = firebaseDatabase.getReference("StudentInfo");
                        Query checkUser = sdatabaseReference.orderByChild("regNo").equalTo(keyValue);
                        //..............................................................................
                        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String phnum = dataSnapshot.child(keyValue).child("phNumber").getValue(String.class);
                                    phfromDB = "+91" + phnum;
                                    startPhoneNumberVerification(phfromDB);
                                } else {
                                    Toast.makeText(forgotpassword.this, "Account not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(forgotpassword.this, "Fail to get data " + databaseError, Toast.LENGTH_SHORT).show();
                            }
                        });
                        //..............................................................................


                    }

                    else if (flag == 1) {
                        fdatabaseReference = firebaseDatabase.getReference("FacultyInfo");
                        Query checkUser = fdatabaseReference.orderByChild("scode").equalTo(keyValue);
                        //..............................................................................
                        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String phnum = dataSnapshot.child(keyValue).child("phNumber").getValue(String.class);
                                    phfromDB = "+91" + phnum;
                                    startPhoneNumberVerification(phfromDB);
                                } else {
                                    Toast.makeText(forgotpassword.this, "Account not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(forgotpassword.this, "Fail to get data " + databaseError, Toast.LENGTH_SHORT).show();
                            }
                        });
                        //..............................................................................
                    }
                }
            }
        });


        rsotptv = findViewById(R.id.rsotptv);
        rsotptv.setPaintFlags(rsotptv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        rsotptv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                    resendVerificationCode(phfromDB, forceResendingToken);
                }
        });


        checkOtp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String code= otpGetter.getText().toString().trim();
                if (TextUtils.isEmpty(code)){
                    Toast.makeText(forgotpassword.this,"Please Enter Verification Code",Toast.LENGTH_SHORT).show();
                }
                else{
                    verifyPhonNumberWhithCode(mVarificationId,code);
                }
            }
        });
    }







    private void startPhoneNumberVerification(String phone){
        pd.setMessage("Verifying Phone Number");
        pd.show();

        PhoneAuthOptions options=
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }
    private void resendVerificationCode(String phone, PhoneAuthProvider.ForceResendingToken token){
        pd.setMessage("ReSending Code");
        pd.show();

        PhoneAuthOptions options=
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }
    private void verifyPhonNumberWhithCode(String verificationId, String code) {
        pd.setMessage("Verifying code");
        pd.show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        signInWhithPhoneAuthCredential(credential);

    }

    private void signInWhithPhoneAuthCredential(PhoneAuthCredential credential) {
        pd.setMessage("Logging In");

        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>(){
                    @Override
                    public void onSuccess(AuthResult authResult){
                        pd.dismiss();
                        String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                        Toast.makeText(forgotpassword.this,"LoggedIn as"+phone,Toast.LENGTH_SHORT).show();

                        if (flag == 0){
                            Intent changepassword = new Intent(forgotpassword.this, changepassword.class);
                            changepassword.putExtra("regno", keyValue);
                            changepassword.putExtra("Flag", 0);
                            changepassword.putExtra("menuFlag", 0);
                            startActivity(changepassword);
                            finish();
                        }else if (flag == 1) {
                            Intent changepassword = new Intent(forgotpassword.this, changepassword.class);
                            changepassword.putExtra("fcode", keyValue);
                            changepassword.putExtra("Flag", 1);
                            changepassword.putExtra("menuFlag", 0);
                            startActivity(changepassword);
                            finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e){
                        pd.dismiss();
                        Toast.makeText(forgotpassword.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
    }









}


/*
                AlertDialog.Builder dialog = new AlertDialog.Builder(forgotpassword.this);
                dialog.setMessage("Password will be sent to your mail id with 30 seconds.");
                dialog.setTitle("Forgot Password");
                dialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog=dialog.create();
                alertDialog.show();



                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getOtpBtn.setText("Resend Passwword");
                        getOtpBtn.setEnabled(true);
                    }
                }, 30000);

  public static  String addColor(String msg, short color){
        String hexColor = String.format("#%06X", (0XFFFFFF & color));
        return "<font color=\"" + hexColor + "\">" + msg + "</font>";
    }




                                final String userName  = "hk893917@gmail.com", password = "SRD3541420840";

                                String messageHead = "<h1>" + addColor("Your Password", (short) 0x2196F3) + "</h1>"
                                        + "<br>" + "<h5>" + addColor(passfromDB, (short) 0x2196F3) + "</h5>";


                                //String messageToSend = "<p style = "font-size: 50px; color:blue;"> Hello"




                                Properties props = new Properties();
                                props.put("mail.smtp.auth", "true");
                                props.put("mail.smtp.starttls.enable", "true");
                                props.put("mail.smtp.host", "smtp.gmail.com");
                                props.put("mail.smtp.port", "587");
                                Session session = Session.getInstance(props, new javax.mail.Authenticator(){
                                    @Override
                                    protected PasswordAuthentication getPasswordAuthentication() {
                                        return new PasswordAuthentication(userName, password);
                                    }
                                });

                                try{
                                    Message message = new MimeMessage(session);
                                    message.setFrom(new InternetAddress(userName, "SARS Develops"));
                                    assert eMailfromDB != null;
                                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(eMailfromDB));
                                    message.setSubject("Forgot Password");
                                    message.setText("Hello");
                                    //message.setContent(messageHead, "text/html");
                                    Transport.send(message);
                                    Toast.makeText(forgotpassword.this, "Send Successfully...", Toast.LENGTH_SHORT).show();
                                }catch (MessagingException | UnsupportedEncodingException e){
                                    throw new RuntimeException(e);
                                }


 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

 */