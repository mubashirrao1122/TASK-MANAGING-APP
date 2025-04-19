package com.example.taskmanagerapp.network;

import com.google.gson.annotations.SerializedName;

public class QuoteResponse {
    @SerializedName("q")
    private String quote;
    
    @SerializedName("a")
    private String author;
    
    // Add default constructor
    public QuoteResponse() {
    }
    
    public String getQuote() {
        return quote;
    }
    
    public void setQuote(String quote) {
        this.quote = quote;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
}