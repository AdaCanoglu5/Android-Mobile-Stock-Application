package com.sutock2;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.sutock2.databinding.LoginPageBinding;
import com.sutock2.databinding.StockPageBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockPage extends Fragment {

    private StockPageBinding binding;

    public String stockSymbol;

    private CandleStickChart chart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = StockPageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        chart = view.findViewById(R.id.chart);

        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.setDragEnabled(true);
        chart.getDescription().setEnabled(false);

        chart.setScaleMinima(4f, 1f);

        String stockSymbol = null;
        if (getArguments() != null && getArguments().containsKey("stockSymbol")) {
            stockSymbol = getArguments().getString("stockSymbol");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(stockSymbol);
            getActivity().setTitle(stockSymbol);
        }

        this.stockSymbol = stockSymbol;

        loadContent();

        return view;
    }

    private void loadContent() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Stock> call = apiService.getStock(stockSymbol); // Example for Apple Inc.

        call.enqueue(new Callback<Stock>() {
            @Override
            public void onResponse(Call<Stock> call, Response<Stock> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Stock stockData = response.body();
                    List<StockPrice> stockDataList = stockData.getPrices();

                    if (stockDataList != null) {
                        ArrayList<CandleEntry> entries = new ArrayList<>();
                        for (int i = 0; i < stockDataList.size(); i++) {
                            StockPrice data = stockDataList.get(i);
                            entries.add(new CandleEntry(
                                    i, data.getHigh(), data.getLow(), data.getOpen(), data.getClose()));
                        }

                        CandleDataSet dataSet = new CandleDataSet(entries, stockData.getSymbol() + " - " + stockData.getCompanyName());
                        dataSet.setColor(Color.rgb(80, 80, 80));
                        dataSet.setShadowColor(Color.DKGRAY);
                        dataSet.setShadowWidth(0.8f);
                        dataSet.setDecreasingColor(Color.rgb(122, 242, 84));
                        dataSet.setDecreasingPaintStyle(Paint.Style.FILL);
                        dataSet.setIncreasingColor(Color.RED);
                        dataSet.setIncreasingPaintStyle(Paint.Style.FILL);
                        dataSet.setNeutralColor(Color.BLUE);

                        CandleData candleData = new CandleData(dataSet);
                        chart.setData(candleData);
                        chart.invalidate(); // Refresh the chart

                        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                        Call<Set<Stock>> followedStocksCall = service.getFollowedStocks(PreferencesUtil.getLoggedInId(getActivity()));

                        followedStocksCall.enqueue(new Callback<Set<Stock>>() {
                            @Override
                            public void onResponse(Call<Set<Stock>> call, Response<Set<Stock>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Set<Stock> followedStocks = response.body();

                                    boolean isFollowed = false;
                                    for (Stock followedStock : followedStocks) {
                                        if (followedStock.getSymbol().equals(stockData.getSymbol())) {
                                            isFollowed = true;
                                            break;
                                        }
                                    }
                                    if (isFollowed) {
                                        // Change button action to show toast
                                        binding.buttonFollow.setText("Unfollow Stock");
                                        binding.buttonFollow.setOnClickListener(v -> {
                                            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                                            String userId = PreferencesUtil.getLoggedInId(getActivity()); // Replace with actual user ID retrieval method

                                            // Call the API to unfollow the stock
                                            Call<User> unfollowCall = service.unfollowStock(userId, stockData.getSymbol());

                                            unfollowCall.enqueue(new Callback<User>() {
                                                @Override
                                                public void onResponse(Call<User> call, Response<User> response) {
                                                    if (response.isSuccessful()) {
                                                        // Unfollow successful, update UI accordingly
                                                        Toast.makeText(getActivity(), "Unfollowed stock", Toast.LENGTH_SHORT).show();
                                                        loadContent();
                                                        // You might want to update the button text or state here
                                                    } else {
                                                        // Handle the case where the response is not successful
                                                        Toast.makeText(getActivity(), "Failed to unfollow stock", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<User> call, Throwable t) {
                                                    // Handle the failure case (like a network error)
                                                    Log.e("APIError", "Error unfollowing stock: " + t.getMessage());
                                                    Toast.makeText(getActivity(), "Error unfollowing stock", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        });
                                    } else {
                                        binding.buttonFollow.setText("Follow Stock");
                                        binding.buttonFollow.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                                                // Now use the service to make a call
                                                Call<User> call = service.followStock(PreferencesUtil.getLoggedInId(getActivity()), stockSymbol);
                                                call.enqueue(new Callback<User>() {
                                                    @Override
                                                    public void onResponse(Call<User> call, Response<User> response) {
                                                        if (response.isSuccessful()) {
                                                            // Handle the successful response
                                                            User user = response.body();
                                                            Toast.makeText(getActivity(), "Followed stock", Toast.LENGTH_SHORT).show();
                                                            loadContent();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<User> call, Throwable t) {
                                                        Log.e("APIError", "Error post follow: " + t.getMessage());
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Set<Stock>> call, Throwable t) {
                                Log.e("APIError", "Error fetching followed stocks: " + t.getMessage());
                                // Handle the failure
                            }
                        });

                    } else {
                        Log.e("APIError", "Response not successful or body is null");
                        // Handle the error
                    }
                } else {
                    Log.e("APIError", "Response not successful or body is null");
                }
            }

            @Override
            public void onFailure(Call<Stock> call, Throwable t) {
                Log.e("APIError", "Error fetching stock data: " + t.getMessage());
                // Handle the failure
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                // Now use the service to make a call
                Call<User> call = service.followStock(PreferencesUtil.getLoggedInId(getActivity()), stockSymbol);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            // Handle the successful response
                            User user = response.body();
                            Toast.makeText(getActivity(), "Followed stock", Toast.LENGTH_SHORT).show();
                            loadContent();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("APIError", "Error post follow: " + t.getMessage());
                    }
                });
            }
        });
    }
}


