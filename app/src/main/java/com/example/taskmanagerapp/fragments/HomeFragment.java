package com.example.taskmanagerapp.fragments;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taskmanagerapp.R;
import com.example.taskmanagerapp.network.ApiClient;
import com.example.taskmanagerapp.network.ApiService;
import com.example.taskmanagerapp.network.QuoteResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TextView quoteTextView;
    private TextView authorTextView;
    private FloatingActionButton refreshButton;
    private ImageView quoteIcon;
    private View rootView;
    
    // Cache quotes locally to provide variety
    private List<QuoteResponse> quotesCache = new ArrayList<>();
    private Random random = new Random();
    private QuoteResponse currentQuote;
    private Handler handler = new Handler(Looper.getMainLooper());

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        quoteTextView = rootView.findViewById(R.id.quote_text);
        authorTextView = rootView.findViewById(R.id.quote_author);
        refreshButton = rootView.findViewById(R.id.fab_new_quote);
        quoteIcon = rootView.findViewById(R.id.quote_icon);

        // Set initial alpha for smooth animation
        quoteTextView.setAlpha(0f);
        authorTextView.setAlpha(0f);

        // Set up refresh button click
        refreshButton.setOnClickListener(v -> {
            // Animate button rotation
            ObjectAnimator rotation = ObjectAnimator.ofFloat(refreshButton, "rotation", 0f, 360f);
            rotation.setDuration(800);
            rotation.setInterpolator(new AccelerateDecelerateInterpolator());
            rotation.start();

            // Reset text visibility for new animation
            quoteTextView.setAlpha(0f);
            authorTextView.setAlpha(0f);

            // Show a different quote
            showNextQuote();
        });

        // Add long press listener for sharing
        rootView.findViewById(R.id.quote_card).setOnLongClickListener(v -> {
            if (currentQuote != null) {
                shareQuote(currentQuote.getQuote(), currentQuote.getAuthor());
            }
            return true;
        });

        // Show default quote immediately while API loads
        showDefaultQuote();
        
        // Fetch initial quotes to build cache
        fetchQuotes();

        return rootView;
    }

    private void fetchQuotes() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<QuoteResponse>> call = apiService.getDailyQuote();

        call.enqueue(new Callback<List<QuoteResponse>>() {
            @Override
            public void onResponse(Call<List<QuoteResponse>> call, Response<List<QuoteResponse>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Save quotes to cache
                    quotesCache.addAll(response.body());
                    
                    // Show first quote from API
                    showNextQuote();
                    
                    // If we only got one quote, try to fetch more
                    if (response.body().size() <= 1) {
                        fetchMoreQuotes();
                    }
                } else {
                    // Keep showing default quote
                }
            }

            @Override
            public void onFailure(Call<List<QuoteResponse>> call, Throwable t) {
                // Keep showing default quote
            }
        });
    }
    
    private void fetchMoreQuotes() {
        // This is a workaround for APIs that might only return one quote at a time
        // Make multiple calls to build a cache of quotes
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        
        for (int i = 0; i < 5; i++) {
            final int delay = i * 300; // 300ms between requests to avoid overwhelming the API
            
            handler.postDelayed(() -> {
                Call<List<QuoteResponse>> call = apiService.getDailyQuote();
                call.enqueue(new Callback<List<QuoteResponse>>() {
                    @Override
                    public void onResponse(Call<List<QuoteResponse>> call, Response<List<QuoteResponse>> response) {
                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                            // Add to cache, avoiding duplicates
                            for (QuoteResponse quote : response.body()) {
                                boolean isDuplicate = false;
                                for (QuoteResponse existingQuote : quotesCache) {
                                    if (existingQuote.getQuote().equals(quote.getQuote())) {
                                        isDuplicate = true;
                                        break;
                                    }
                                }
                                if (!isDuplicate) {
                                    quotesCache.add(quote);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<QuoteResponse>> call, Throwable t) {
                        // Ignore errors in background fetches
                    }
                });
            }, delay);
        }
    }
    
    private void showNextQuote() {
        if (quotesCache.isEmpty()) {
            // If cache is empty, show default quote
            showDefaultQuote();
            return;
        }
        
        // Choose a random quote, but make sure it's different from current one
        QuoteResponse newQuote;
        if (quotesCache.size() == 1) {
            newQuote = quotesCache.get(0);
        } else {
            do {
                newQuote = quotesCache.get(random.nextInt(quotesCache.size()));
            } while (currentQuote != null && 
                     newQuote.getQuote().equals(currentQuote.getQuote()) && 
                     quotesCache.size() > 1);
        }
        
        // Update current quote
        currentQuote = newQuote;
        
        // Set text
        quoteTextView.setText(currentQuote.getQuote());
        authorTextView.setText("- " + currentQuote.getAuthor());
        
        // Animate text fade-in
        animateTextFadeIn();
    }
    
    private void showDefaultQuote() {
        // Fallback quote in case API fails
        quoteTextView.setText("The best way to predict the future is to create it.");
        authorTextView.setText("- Abraham Lincoln");
        
        // Create a mock quote for sharing
        currentQuote = new QuoteResponse();
        currentQuote.setQuote("The best way to predict the future is to create it.");
        currentQuote.setAuthor("Abraham Lincoln");
        
        // Animate text fade-in
        animateTextFadeIn();
    }

    private void animateTextFadeIn() {
        // Animate quote text
        ObjectAnimator quoteAnimator = ObjectAnimator.ofFloat(quoteTextView, "alpha", 0f, 1f);
        quoteAnimator.setDuration(1000);
        quoteAnimator.setStartDelay(300);
        quoteAnimator.start();

        // Animate author text with a slight delay
        ObjectAnimator authorAnimator = ObjectAnimator.ofFloat(authorTextView, "alpha", 0f, 1f);
        authorAnimator.setDuration(1000);
        authorAnimator.setStartDelay(800);
        authorAnimator.start();
    }
    
    private void shareQuote(String quote, String author) {
        String shareText = "\"" + quote + "\" - " + author;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Share this quote"));
    }

    @Override
    public void onDestroyView() {
        // Remove any pending tasks when fragment is destroyed
        handler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }
}