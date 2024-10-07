package com.example.qrscannerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private ProgressBar progressBar;
    private Button loginButton;
    private Database dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new Database(this);
        initializeViews();
        setupLoginButton();
    }

    private void initializeViews() {
        usernameEditText = findViewById(R.id.username);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupLoginButton() {
        if (dbHelper.isUserLoggedIn()) {
            Toast.makeText(LoginActivity.this, "WELCOME!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish(); // Finish LoginActivity to prevent going back to it from MainActivity
        } else {
            loginButton.setOnClickListener(v -> loginUser());
        }
    }
    private void loginUser() {
        String phone = usernameEditText.getText().toString().trim();

        if (phone.isEmpty()) {
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
        }
        else if (phone.length() != 10){
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
        }
        else {
            userLogin(phone);
        }
    }
    public void userLogin(String phone1){
        String phone = phone1;
        if (phone.startsWith("07") || phone.startsWith("01")) {
            phone = "254" + phone.substring(1);
            progressBar.setVisibility(View.VISIBLE);
            dbHelper.addUser(phone);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish(); // Finish LoginActivity to prevent going back to it from MainActivity
            Toast.makeText(LoginActivity.this, "Logged in successful", Toast.LENGTH_SHORT).show();
        }
    }
}


