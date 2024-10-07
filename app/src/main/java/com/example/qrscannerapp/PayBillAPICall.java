package com.example.qrscannerapp;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PayBillAPICall {
    @GET("https://donkey-blessed-cub.ngrok-free.app/scan-pay/{phone}/{amount}/{business_number}/{account_number}/")
    Call<ResponsePay> getData(@Path("phone") String phone, @Path("amount")  String amount, @Path("business_number") String business_number, @Path("account_number") String account_number);
}

