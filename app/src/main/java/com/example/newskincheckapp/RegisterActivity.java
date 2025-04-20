package com.example.newskincheckapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail, etPassword, etAge;
    private RadioGroup rgGender;
    private Button btnRegister;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Start Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Show back button in ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Register");
        }

        // Link views
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etAge = findViewById(R.id.et_age);
        rgGender = findViewById(R.id.rg_gender);
        btnRegister = findViewById(R.id.btn_register);

        // Register button click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = etFirstName.getText().toString().trim();
                String lastName = etLastName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String age = etAge.getText().toString().trim();

                int selectedGenderId = rgGender.getCheckedRadioButtonId();
                String gender = "";
                if (selectedGenderId == R.id.rb_male) {
                    gender = "Erkek";
                } else if (selectedGenderId == R.id.rb_female) {
                    gender = "Kadın";
                }

                // access control
                if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your first and last name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "Enter Email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password) || password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(age)) {
                    Toast.makeText(RegisterActivity.this, "Enter your age!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(gender)) {
                    Toast.makeText(RegisterActivity.this, "Select your gender!", Toast.LENGTH_SHORT).show();
                    return;
                }

                registerUser(email, password, firstName, lastName, age, gender);
            }
        });
    }

    // Signing up with Firebase
    private void registerUser(String email, String password, String firstName, String lastName, String age, String gender) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                        Log.d("RegisterActivity", "Name: " + firstName + " " + lastName);
                        Log.d("RegisterActivity", "Email: " + user.getEmail());
                        Log.d("RegisterActivity", "Age: " + age);
                        Log.d("RegisterActivity", "Gender: " + gender);

                        // Send email verification
                        if (user != null) {
                            user.sendEmailVerification()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Verification email sent.", Toast.LENGTH_LONG).show();
                                            finish(); // Aktiviteyi kapat
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
