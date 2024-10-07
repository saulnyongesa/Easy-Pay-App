package com.example.qrscannerapp;
import android.app.Activity;
import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PayBillFragment extends Fragment {
    Database dbHelper;
    EditText amount_to_pay;
    Button payBtn;
    CodeScanner mCodeScanner;
    PayBillAPICall apiCall;
    ProgressBar progressBar;
    String till;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_paybill, container, false);
        dbHelper = new Database(getContext());
        amount_to_pay = view.findViewById(R.id.amount);
        payBtn = view.findViewById(R.id.payBtn);
        progressBar = view.findViewById(R.id.progressBar);
        startScanner();
        return view;
    }
    public  void startScanner(){
        final Activity activity = getActivity();
        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        assert activity != null;
        mCodeScanner = new CodeScanner(activity, scannerView);

        mCodeScanner.setDecodeCallback(result -> activity.runOnUiThread(() ->{
                    till = result.getText();
                    Toast.makeText(activity, result.getText(), Toast.LENGTH_SHORT).show();
                    scannerView.setVisibility(View.GONE);
                    amount_to_pay.setVisibility(View.VISIBLE);
                    payBtn.setVisibility(View.VISIBLE);
                }
        ));

        scannerView.setOnClickListener(view1 -> mCodeScanner.startPreview());
    }



    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        payBtn.setOnClickListener(payInit);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://json.extendsclass.com/bin/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiCall = retrofit.create(PayBillAPICall.class);
    }

    private String getUsernameFromDatabase() {
        String username = "";
        if (dbHelper != null) {
            Cursor cursor = dbHelper.getReadableDatabase().query(
                    "user_details", // Table name
                    new String[]{"phone"}, // Columns to retrieve
                    "login_status = ?", // Selection
                    new String[]{"1"}, // Selection args (where login_status = 1)
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                // Retrieve username from the cursor
                username = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                cursor.close();
            }
        }
        return username;
    }

    private final View.OnClickListener payInit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                String str = till;
                String[] separated = str.split(",");

                String business_number = separated[0];
                String account_number = separated[1];
                String phone = getUsernameFromDatabase();
                String amount = amount_to_pay.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                Call<ResponsePay> call = apiCall.getData(phone, amount, business_number, account_number);
                call.enqueue(new Callback<ResponsePay>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponsePay> call, @NonNull Response<ResponsePay> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Payment initiated successfully", Toast.LENGTH_LONG).show();
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout, new WithdrawFragment());
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponsePay> call, @NonNull Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Failed to initiate payment", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                Log.e("PayInit", "Error: " + e.getMessage());
                e.printStackTrace();
                Toast.makeText(getActivity(), "An error occurred", Toast.LENGTH_LONG).show();
            }
        }
    };

}