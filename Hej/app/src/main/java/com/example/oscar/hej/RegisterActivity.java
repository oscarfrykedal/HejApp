package com.example.oscar.hej;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassWord;
    private TextView textViewSignIn;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Log.d("oscar ", "oscar" );
        firebaseAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(this);

        buttonRegister = findViewById(R.id.buttonRegister);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassWord = findViewById(R.id.editTextPass);
        textViewSignIn = findViewById(R.id.textViewSignIn);

        buttonRegister.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);


    }

    private void moveToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void moveToLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    private void registerUser() {
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
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //user is succesfully registered and logged in
                            //start next activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }else{
                            Log.d( "oscar ", "no sucess.." );
                            Toast.makeText(RegisterActivity.this, "Registered Failed, please try again", Toast.LENGTH_SHORT).show();

                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == buttonRegister){
            registerUser();
        }

        if (view == textViewSignIn){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}