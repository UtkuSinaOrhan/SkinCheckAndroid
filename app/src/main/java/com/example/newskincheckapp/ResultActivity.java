package com.example.newskincheckapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Enable back key in ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Analysis Results");
        }

        // Get data from intent
        String resultTitle = getIntent().getStringExtra("RESULT_TITLE");
        String resultDetails = getIntent().getStringExtra("RESULT_DETAILS");

        // Assigning default values ​​(null check)
        if (resultTitle == null) {
            resultTitle = "No Title Available";
        }
        if (resultDetails == null) {
            resultDetails = "No Details Available";
        }

        // Define TextViews
        TextView tvResultTitle = findViewById(R.id.tvResultTitle);
        TextView tvResultDetails = findViewById(R.id.tvResultDetails);

        // Assign incoming data to TextViews
        tvResultTitle.setText(resultTitle);

        // Add color and style with SpannableString
        SpannableString spannableDetails = new SpannableString(resultDetails);

        // Color the "Recommended Products:" section and make it bold
        int startIndex = resultDetails.indexOf("Recommended Products:");
        if (startIndex >= 0) {
            spannableDetails.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#FFB74D")),
                    startIndex, startIndex + "Recommended Products:".length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            spannableDetails.setSpan(
                    new StyleSpan(Typeface.BOLD),
                    startIndex, startIndex + "Recommended Products:".length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        // Make "Symptoms:" bold
        int startIndexSymptoms = resultDetails.indexOf("Symptoms:");
        if (startIndexSymptoms >= 0) {
            spannableDetails.setSpan(
                    new StyleSpan(Typeface.BOLD),
                    startIndexSymptoms,
                    startIndexSymptoms + "Symptoms:".length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        // Assign formatted text to TextView
        tvResultDetails.setText(spannableDetails);

        // btnViewProducts button definition and click event
        Button btnViewProducts = findViewById(R.id.btnViewProducts);
        String finalResultTitle = resultTitle;
        btnViewProducts.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProductRecommendationActivity.class);
            intent.putExtra("PRODUCT_CATEGORY", finalResultTitle);
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Close current activity when back key is pressed
        finish();
        return true;
    }

}
