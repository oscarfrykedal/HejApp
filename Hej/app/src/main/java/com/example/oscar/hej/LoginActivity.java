package com.example.oscar.hej;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{




    private Button buttonLogin;
    EditText email;
    EditText passWord;
    private TextView textViewSignUp;

    private ProgressDialog progressDialog;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        //if (firebaseAuth.getCurrentUser() != null){
        //    //mainActivity
        ////    startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        //}

        progressDialog = new ProgressDialog(this);

        buttonLogin = findViewById(R.id.buttonLogin);
        email = findViewById(R.id.editTextEmail);
        passWord = findViewById(R.id.editTextPass);
        textViewSignUp = findViewById(R.id.textViewSignUp);

        buttonLogin.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = passWord.getText().toString();

                if (TextUtils.isEmpty(txt_email)| TextUtils.isEmpty(txt_password)){
                    Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else{

                    auth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(LoginActivity.this, "authentication failed, please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

/*    private void UserLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassWord.getText().toString().trim();


        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }
        //if validation is ok
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }
                });

    }*/

    @Override
    public void onClick(View view) {
        if(view == buttonLogin){

        }

        if (view == textViewSignUp){
            finish();
            startActivity(new Intent(this, RegisterActivity.class));

        }
    }
}