package com.example.qrscannerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.qrscannerapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new WithdrawFragment());
        binding.bottomNavView.setBackground(null);
        binding.bottomNavView.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.home) {
                replaceFragment(new WithdrawFragment());
            }
            else if (menuItem.getItemId() == R.id.paybill) {
                replaceFragment(new PayBillFragment());
            }
            else if (menuItem.getItemId() == R.id.buy) {
                replaceFragment(new BuyGoodsFragment());
            }
            else if (menuItem.getItemId() == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}

