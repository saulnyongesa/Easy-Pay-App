package com.example.qrscannerapp;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;

public class WithdrawFragment extends Fragment {
    TextView name;
    Database dbHelper;
    ProgressBar progressBar;
    CodeScanner mCodeScanner;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_withdrawal, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        dbHelper = new Database(getContext());
        name = view.findViewById(R.id.name_app);

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserDetails userDetails = getUserDetailsFromDatabase();
        String Name = userDetails.getPhone();
        name.setText(Name);
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