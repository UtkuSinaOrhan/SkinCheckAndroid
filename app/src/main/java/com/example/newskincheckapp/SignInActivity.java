package com.example.newskincheckapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Start Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Show back button in ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Sign In");
        }

        // Link views
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnSignIn = findViewById(R.id.btn_sign_in);

        // Sign In butonu
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(SignInActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Login with Firebase
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null && user.isEmailVerified()) {
                                    Toast.makeText(SignInActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(SignInActivity.this, "Please verify your email first!", Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();
                                }
                            } else {
                                Toast.makeText(SignInActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Close activity when back key is pressed
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
