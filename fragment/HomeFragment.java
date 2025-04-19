package com.example.taskmanagerapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taskmanagerapp.R;
import com.example.taskmanagerapp.network.ApiClient;
import com.example.taskmanagerapp.network.ApiService;
import com.example.taskmanagerapp.network.QuoteResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TextView quoteTextView;
    private TextView authorTextView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        quoteTextView = view.findViewById(R.id.quote_text);
        authorTextView = view.findViewById(R.id.quote_author);

        fetchDailyQuote();

        return view;
    }

    private void fetchDailyQuote() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<QuoteResponse>> call = apiService.getDailyQuote();

        call.enqueue(new Callback<List<QuoteResponse>>() {
            @Override
            public void onResponse(Call<List<QuoteResponse>> call, Response<List<QuoteResponse>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    QuoteResponse quote = response.body().get(0);
                    quoteTextView.setText(quote.getQuote());
                    authorTextView.setText("- " + quote.getAuthor());
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch quote", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<QuoteResponse>> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}