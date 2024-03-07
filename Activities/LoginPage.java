package com.sutock2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.sutock2.databinding.LoginPageBinding;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.widget.Toast;

import android.util.Log;

import java.io.IOException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPage extends Fragment {

    private LoginPageBinding binding;

    private static String extractUserID(String responseBody) {
        Pattern pattern = Pattern.compile("User ID: ([a-f0-9]+)");
        Matcher matcher = pattern.matcher(responseBody);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    private void loginUser(String username, String password) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        User user = new User(username, password);
        Call<ResponseBody> call = apiService.loginUser(user);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Handle success
                    // You can parse the response body if needed
                    try {
                        String responseBody = response.body().string();
                        String userID = extractUserID(responseBody);
                        Log.d("LoginSuccess", responseBody);
                        Log.d("LoginSuccess", "User id is: " + userID);
                        Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                        PreferencesUtil.saveLoginStatus(getActivity(), true, username, password, userID);
                        NavHostFragment.findNavController(LoginPage.this)
                                .navigate(R.id.action_FirstFragment_to_SecondFragment);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle failure
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("LoginError", "Login failed. Status code: " + response.code() + ", Body: " + errorBody);
                        Toast.makeText(getActivity(), "Login failed: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Network error or server error
                Log.e("NetworkError", "Failed to reach server: " + t.getMessage(), t);
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = LoginPageBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.UsernameEnter.getText().toString();
                String password = binding.PasswordEnter.getText().toString();

                // Proceed with API call
                loginUser(username, password);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}