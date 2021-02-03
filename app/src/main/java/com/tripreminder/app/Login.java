package com.tripreminder.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText Email,Password;
    Button btnLogin;
    private static  final String MY_PREFS_NAME= "Shared prefrence";


    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        Email = findViewById(R.id.emailTxt);
        Password = findViewById(R.id.passTxt);
        btnLogin = findViewById(R.id.loginBtn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                if(TextUtils.isEmpty(email))
                {
                    Email.setError("Please enter your email here");
                } else if (TextUtils.isEmpty(password)) {
                    Password.setError("Please enter your Password here");
                } else if(password.length()<8)
                {
                    Password.setError("Password should be at least 8 charaters");

                }
                else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // shared prefrences
                                String UserId = task.getResult().getUser().getUid();

                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("userId",UserId);
                                editor.apply();

                                Toast.makeText(Login.this, "Login done", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), TripsHistory.class));
                            } else {
                                // Toast.makeText(Login.this, "You did not enter a username", Toast.LENGTH_SHORT).show();

                                Toast.makeText(Login.this, "login fail" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                }




            }
        });

    }
}