package com.sutock2;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private CommentsAdapter adapter;
    private TextView tvLoadingComments;
    public String StockSymbol;

    public void getStockIdBySymbol(String commentText, String stockSymbol) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiService.getIdBySymbol(stockSymbol);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String stockId = response.body();
                    // Use the stockId as needed
                    Log.d("StockID", "Stock ID: " + stockId);
                    postComment(commentText, stockId);
                } else {
                    // Handle the scenario where response isn't successful
                    Log.e("APIError", "Response not successful");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Handle network errors or other failures
                Log.e("APIError", "Error fetching stock ID: " + t.getMessage());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        recyclerView = view.findViewById(R.id.commentsRecyclerView);
        tvLoadingComments = view.findViewById(R.id.tvLoadingComments);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CommentsAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Replace "stock_id" with the actual stock ID you want to fetch comments for
        String stockSymbol = null;
        if (getArguments() != null && getArguments().containsKey("stockSymbol")) {
            stockSymbol = getArguments().getString("stockSymbol");
        }

        StockSymbol = stockSymbol;

        Log.d("APIError", "symbol is: " + stockSymbol);

        loadComments(StockSymbol);

        Button btnAddComment = view.findViewById(R.id.btnAddComment);
        btnAddComment.setOnClickListener(v -> showAddCommentPopup());

        return view;
    }
    private void showAddCommentPopup() {
        // Inflate and setup the popup layout
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_add_comment, null);
        final EditText etCommentText = popupView.findViewById(R.id.etCommentText);
        Button btnPostComment = popupView.findViewById(R.id.btnPostComment);

        // Create the popup window
        final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        // Set up the "Post" button click listener
        btnPostComment.setOnClickListener(view -> {
            String commentText = etCommentText.getText().toString();
            if (!commentText.isEmpty()) {
                String stockSymbol = StockSymbol;
                getStockIdBySymbol(commentText, stockSymbol);
                popupWindow.dismiss();
            }
        });

        // Show the popup window
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
    }

    private void postComment(String commentText, String currentStockId) {
        // Prepare the CommentRequest object
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setText(commentText);
        // Set userId and stockId as required
        commentRequest.setUserId(PreferencesUtil.getLoggedInId(getActivity())); // Replace with actual user ID
        commentRequest.setStockId(currentStockId); // Replace with actual stock ID
        // Make the API call
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Comment> call = apiService.addComment(commentRequest);

        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    loadComments(StockSymbol);
                    Toast.makeText(getActivity(), "Comment posted", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle the error scenario
                    Toast.makeText(getActivity(), "Failed to post comment", Toast.LENGTH_SHORT).show();
                    Log.e("APIError", "Response not successful");
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                // Handle the failure case
                Log.e("APIError", "Error posting comment: " + t.getMessage());
            }
        });
    }
    private void loadComments(String stockId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Comment>> call = apiService.getComments(stockId);

        tvLoadingComments.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                tvLoadingComments.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    adapter.commentList.clear();
                    adapter.commentList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    // Handle the error scenario
                    Log.e("APIError", "Response not successful");
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                tvLoadingComments.setVisibility(View.GONE);
                Log.e("APIError", "Error fetching comments: " + t.getMessage());
                // Handle the failure case
            }
        });
    }
}