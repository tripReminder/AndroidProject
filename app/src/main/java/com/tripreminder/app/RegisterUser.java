package com.tripreminder.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class RegisterUser extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText editTxtName, editTxtEmail, editTxtPassword;
    Button btnRegister;
    TextView signIntxt;


    private static final String TAG = "TAG";
    private static  final String MY_PREFS_NAME= "Shared prefrence";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();
        editTxtName = findViewById(R.id.txtName);
        editTxtEmail = findViewById(R.id.txtEmail);
        editTxtPassword = findViewById(R.id.txtPassword);
        signIntxt = findViewById(R.id.singInTxt);

        signIntxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterUser.this,Login.class);
                startActivity(intent);
            }
        });


        btnRegister = findViewById(R.id.RegisterBtn);

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), TripsHistory.class));
            finish();

        }
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTxtEmail.getText().toString().trim();
                String password = editTxtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    editTxtEmail.setError("Please enter your email here");
                } else if (TextUtils.isEmpty(password)) {
                    editTxtPassword.setError("Please enter your Password here");
                } else if (password.length() < 8) {
                    editTxtPassword.setError("Password should be at least 8 charaters");

                } else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                // shared prefrences
                                String UserId = task.getResult().getUser().getUid();

                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("userId",UserId);
                                editor.apply();


                                Toast.makeText(RegisterUser.this, "User created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Login.class));
                            } else {
                                Toast.makeText(RegisterUser.this, "Filed to create user" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }


            }
        });
    }
}
