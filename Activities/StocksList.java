package com.sutock2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StocksList extends Fragment {

    private RecyclerView recyclerView;
    private StockAdapter stockAdapter;
    private List<Stock> stockList = new ArrayList<>();
    private TextView tvLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_stocks, container, false);
        recyclerView = view.findViewById(R.id.rvStocks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        stockAdapter = new StockAdapter(stockList, stockName -> {
        // Handle the stock name click event here
            Bundle bundle = new Bundle();
            bundle.putString("stockSymbol", stockName);
            NavHostFragment.findNavController(StocksList.this)
                    .navigate(R.id.action_SecondFragment_to_StockTabPage, bundle);
        });
        recyclerView.setAdapter(stockAdapter);
        // Fetch the stock data from the API
        tvLoading = view.findViewById(R.id.tvLoading);

        getAllStocks();

        return view;
    }
    private void getAllStocks() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Stock>> call = apiService.getAllSymbols();

        tvLoading.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<List<Stock>>() {
            @Override
            public void onResponse(Call<List<Stock>> call, Response<List<Stock>> response) {

                tvLoading.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    stockList.clear();
                    stockList.addAll(response.body());
                    stockAdapter.notifyDataSetChanged();
                } else {
                    // Handle the error scenario
                }
            }

            @Override
            public void onFailure(Call<List<Stock>> call, Throwable t) {
                // Handle
                tvLoading.setVisibility(View.GONE);

                Log.e("APIError", "Error fetching all stocks: " + t.getMessage());
            }

        });
    }
    private static class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {
        private List<Stock> stocks;
        private final Consumer<String> onStockClicked;

        StockAdapter(List<Stock> stocks, Consumer<String> onStockClicked) {
            this.stocks = stocks;
            this.onStockClicked = onStockClicked;
        }

        @NonNull
        @Override
        public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new StockViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
            final Stock stock = stocks.get(position);
            holder.textView.setText(stock.getSymbol() + " - " +stock.getCompanyName());
            holder.itemView.setOnClickListener(v -> onStockClicked.accept(stock.getSymbol()));
        }
        public int getItemCount() {
            return stocks.size();
        }

        static class StockViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            StockViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(android.R.id.text1);
            }
        }
    }

}
