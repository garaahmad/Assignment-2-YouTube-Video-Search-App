package com.example.mymob2;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mymob2.adapter.VideoAdapter;
import com.example.mymob2.Item;
import com.example.mymob2.YoutubeResponse;
import com.example.mymob2.YoutubeApiService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "AIzaSyAEk7F_bbhTFUWxwJXDn5fzxviwCJYk7EY";
    private static final int MAX_RESULTS = 10;
    private EditText searchEditText;
    private Button searchButton;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<Item> videoList = new ArrayList<>();
    private YoutubeApiService youtubeApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideoAdapter(videoList, this);
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        youtubeApi = retrofit.create(YoutubeApiService.class);

        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (query.isEmpty()) {
                Toast.makeText(MainActivity.this, "Enter search query", Toast.LENGTH_SHORT).show();
            } else {
                searchYouTube(query);
            }
        });

        searchEditText.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchButton.performClick();
                return true;
            }
            return false;
        });
    }

    private void searchYouTube(String query) {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        Call<YoutubeResponse> call = youtubeApi.searchVideos(query, MAX_RESULTS, API_KEY);
        call.enqueue(new Callback<YoutubeResponse>() {
            @Override
            public void onResponse(Call<YoutubeResponse> call, Response<YoutubeResponse> response) {
                progressBar.setVisibility(ProgressBar.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    videoList.clear();
                    videoList.addAll(response.body().getItems());
                    adapter.notifyDataSetChanged();
                    if (videoList.isEmpty()) {
                        Toast.makeText(MainActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "API Error: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<YoutubeResponse> call, Throwable t) {
                progressBar.setVisibility(ProgressBar.GONE);
                Toast.makeText(MainActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}