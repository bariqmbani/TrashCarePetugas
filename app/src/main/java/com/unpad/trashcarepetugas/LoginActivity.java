package com.unpad.trashcarepetugas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.unpad.trashcarepetugas.models.Petugas;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    EditText etUsername, etPassword;
    private DatabaseReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static  final  String COLLECTION_NAME_KEY = "petugas";
    public static  final  String USERNAME_KEY = "username";
    public static  final  String PASSWORD_KEY = "password";
    public static final String NUMBER_KEY = "phoneNo:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.inputUsername);
        etPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("petugas");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (!etUsername.getText().toString().equals("") && !etPassword.getText().toString().equals("")) {
                        DocumentReference docRef = db.collection(COLLECTION_NAME_KEY).document(etUsername.getText().toString());
                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    Petugas user = documentSnapshot.toObject(Petugas.class);
                                    if (user.getPassword().equals(etPassword.getText().toString())) {
                                        Toast.makeText(getApplicationContext(), "welcome", Toast.LENGTH_SHORT).show();
                                        Intent it = new Intent(LoginActivity.this,MainActivity.class);
                                        it.putExtra("ID",etUsername.getText().toString());
                                        startActivity(it);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Passsword Mismatching", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Check your Username ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(LoginActivity.this, "Username or Password Cannot be Empty", Toast.LENGTH_SHORT).show();
                    }

                }
        });
    }
}
