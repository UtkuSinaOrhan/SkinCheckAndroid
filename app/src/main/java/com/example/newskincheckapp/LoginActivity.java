package com.example.newskincheckapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    // Request code for Google Sign-In
    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    // View elements
    private Button btnGoogleSignIn, btnRegister;
    private TextView tvSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Show back button in the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Login");
        }

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Retrieve web client ID
                .requestEmail() // Request user's email address
                .build();

        // Create Google Sign-In client
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Bind view elements
        btnGoogleSignIn = findViewById(R.id.btn_google_sign_in);
        btnRegister = findViewById(R.id.btn_register);
        tvSignIn = findViewById(R.id.tv_sign_in);

        // Set click listener for Google Sign-In button
        btnGoogleSignIn.setOnClickListener(v -> signInWithGoogle());

        // Set click listener for Register button: navigate to RegisterActivity
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Highlight "Already have an account? Sign In" text using SpannableString
        setColoredSignInText();

        // Set click listener for "Already have an account? Sign In" text: navigate to SignInActivity
        tvSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }

    // Highlight "Already have an account? Sign in" text
    private void setColoredSignInText() {
        String fullText = "Already have an account? Sign in";

        SpannableString spannableString = new SpannableString(fullText);

        // Set "Already have an account?" text to white
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set "Sign in" text to blue
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#007AFF")), 25, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Underline the "Sign in" text
        spannableString.setSpan(new android.text.style.UnderlineSpan(),
                24, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvSignIn.setText(spannableString);
    }

    // Start Google Sign-In process
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    // Link Google account with Firebase Authentication
                    firebaseAuthWithGoogle(account.getIdToken());
                }
            } catch (ApiException e) {
                // Log the error and notify the user
                Log.w("LoginActivity", "Google sign in failed", e);
                Toast.makeText(this, "Google Sign-In Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Authenticate Google account with Firebase
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Successful login: navigate to the main activity
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(LoginActivity.this, "Welcome " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    } else {
                        // Login failed: show error message
                        Toast.makeText(LoginActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Close the activity when the back button is pressed
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
