package com.unpad.trashcarepetugas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.unpad.trashcarepetugas.models.Feedback;

public class FeedbackActivity extends AppCompatActivity {

    RatingBar ratingBar;
    EditText feedbackBox;
    Button kirimFeedback;

    private float rating;
    private String feedbackText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ratingBar = findViewById(R.id.ratingBar);
        feedbackBox = findViewById(R.id.inputFeedback);
        kirimFeedback = findViewById(R.id.btnKirimFeedback);

        kirimFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = ratingBar.getRating();
                feedbackText = feedbackBox.getText().toString();
                String pengirim = ((UserClient)(getApplicationContext())).getPetugas().getNama();
                Feedback feedback = new Feedback(
                        null,
                        pengirim,
                        rating,
                        feedbackText);
                FirebaseFirestore.getInstance().collection("feedback").document().set(feedback);
                ratingBar.setRating(0F);
                feedbackBox.getText().clear();

                Toast.makeText(FeedbackActivity.this, "Terima kasih atas feedbacknya", Toast.LENGTH_SHORT).show();
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbarFeedback);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
