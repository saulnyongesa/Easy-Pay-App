package com.example.qrscannerapp;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PaymentAPICall {
    @GET("https://donkey-blessed-cub.ngrok-free.app/scan-pay/{phone}/{amount}/{till_number}/")
    Call<ResponsePay> getData(@Path("phone") String phone, @Path("amount")  String amount, @Path("till_number") String till_number);
}

