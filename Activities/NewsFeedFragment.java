package com.sutock2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFeedFragment extends Fragment {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<News> newsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        recyclerView = view.findViewById(R.id.news_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(adapter);

        String stockSymbol = null;
        if (getArguments() != null && getArguments().containsKey("stockSymbol")) {
            stockSymbol = getArguments().getString("stockSymbol");
        }

        loadNews(stockSymbol); // Replace with desired ticker symbol
        return view;
    }

    private void loadNews(String tickerSymbol) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<News>> call = apiService.getNewsByTicker(tickerSymbol);
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    newsList.clear();
                    newsList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
