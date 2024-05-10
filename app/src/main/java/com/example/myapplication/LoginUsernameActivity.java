package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.myapplication.model.UserModel;
import com.example.myapplication.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.Timestamp;

public class LoginUsernameActivity extends AppCompatActivity {

    EditText usernameInput;
    Button letMeIntBtn;
    ProgressBar progressBar;
    String phoneNumber;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_username);

        usernameInput = findViewById(R.id.login_username);
        letMeIntBtn = findViewById(R.id.let_me_in_btn);
        progressBar = findViewById(R.id.login_progress_bar);

        phoneNumber = getIntent().getExtras().getString("phone");
        getUsername();

        letMeIntBtn.setOnClickListener((v -> {
            setUsername();
        }));
    }

    void setUsername(){
        setInprogress(true);
        String username = usernameInput.getText().toString();
        if(username.isEmpty() || username.length()<3){
            setInprogress(false);
            usernameInput.setError("Tên quá ngắn");
            return;
        }

        if(userModel!=null){
            userModel.setUsername(username);
        }else{
           userModel = new UserModel(phoneNumber, username, Timestamp.now(), FirebaseUtils.currentUserId());
        }

        FirebaseUtils.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInprogress(false);
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginUsernameActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }



    private void getUsername() {
        setInprogress(true);
        FirebaseUtils.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInprogress(false);
                if(task.isSuccessful()){
                   userModel = task.getResult().toObject(UserModel.class);
                   if(userModel!=null){
                       usernameInput.setText(userModel.getUsername());
                   }
                }
            }
        });
    }

    void setInprogress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            letMeIntBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            letMeIntBtn.setVisibility(View.VISIBLE);
        }
    }
}

