package com.example.qrscannerapp;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
public class ProfileFragment extends Fragment {
    Button logout_btn;
    ProgressBar progressBar;
    Database dbHelper;

    TextView phone_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        logout_btn = view.findViewById(R.id.logoutBtn);
        phone_text = view.findViewById(R.id.phone);
        progressBar = view.findViewById(R.id.progressBar);

        dbHelper = new Database(getContext());
        UserDetails userDetails = getUserDetailsFromDatabase();
        String Phone = userDetails.getPhone();
        phone_text.setText(Phone);
        logout_btn.setOnClickListener(v -> logoutUser());
        return view;
    }
    private void logoutUser() {
        progressBar.setVisibility(View.VISIBLE);
        // Delete user details from the database
        dbHelper.deleteUserDetails();
        // Navigate back to the login screen
        startActivity(new Intent(getActivity(), LoginActivity.class));
        requireActivity().finish(); // Finish the current activity (ProfileFragment)
    }

    private UserDetails getUserDetailsFromDatabase() {
        UserDetails userDetails = new UserDetails();
        if (dbHelper != null) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    "user_details", // Table name
                    new String[]{"phone"}, // Columns to retrieve
                    "login_status = ?", // Selection
                    new String[]{"1"}, // Selection args (where login_status = 1)
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                // Retrieve fields from the cursor
                userDetails.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
                cursor.close();
            }
        }
        return userDetails;
    }

}