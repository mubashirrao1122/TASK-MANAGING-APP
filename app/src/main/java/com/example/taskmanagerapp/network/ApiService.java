package com.example.taskmanagerapp.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("today")
    Call<List<QuoteResponse>> getDailyQuote();
}