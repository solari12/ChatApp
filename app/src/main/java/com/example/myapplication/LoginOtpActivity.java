package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.utils.AndroidUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {

    Long timeOut = 60L;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    String phoneNumber;
    EditText otpInput;
    Button nextBtn;
    ProgressBar progressBar;
    TextView resendOtpTextView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        otpInput = findViewById(R.id.login_otp);
        nextBtn = findViewById(R.id.login_next_btn);
        progressBar = findViewById(R.id.login_progress_bar);
        resendOtpTextView = findViewById(R.id.resend_otp_textview);

        phoneNumber = getIntent().getExtras().getString("phone");

        sendOtp(phoneNumber, false);

        nextBtn.setOnClickListener((v -> {
            String enteredOtp = otpInput.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, enteredOtp);
            signIn(credential);
            setInprogress(true);
        }));
    }

    void sendOtp(String phoneNumber, boolean isResend){
        setInprogress(true);
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(timeOut, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signIn(phoneAuthCredential);
                                setInprogress(false);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                AndroidUtils.showToast(getApplicationContext(), "không gửi được");
                                setInprogress(false);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                resendingToken = forceResendingToken;
                                AndroidUtils.showToast(getApplicationContext(), "Đã gửi mã");
                                setInprogress(false);
                            }
                        });

        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }

    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        setInprogress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInprogress(false);
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginOtpActivity.this, LoginUsernameActivity.class);
                    intent.putExtra("phone", phoneNumber);
                    startActivity(intent);
                }else{
                    AndroidUtils.showToast(getApplicationContext(), "Mã sai");
                }
            }
        });
    }

    void setInprogress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
        }
    }
}