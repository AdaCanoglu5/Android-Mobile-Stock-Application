package com.sutock2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.sutock2.databinding.LoginPageBinding;
import com.sutock2.databinding.SignupPageBinding;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.widget.Toast;

import android.util.Log;

import java.io.IOException;
import java.util.Objects;

public class SignupPage extends Fragment {

    private SignupPageBinding binding;
    private void SignupUser(String username, String password) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        User user = new User(username, password);
        Call<ResponseBody> call = apiService.signupUser(user);

        if(Objects.equals(username, "") || Objects.equals(password, "")) {
            Toast.makeText(getActivity(), "Username or Password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Handle success
                    // You can parse the response body if needed
                    try {
                        String responseBody = response.body().string();
                        Log.d("SignupSuccess", responseBody);
                        Toast.makeText(getActivity(), "Sign up successful", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(SignupPage.this)
                                .navigate(R.id.action_SignupPage_to_FirstFragment);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle failure
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("SignupError", "Sign up failed. Status code: " + response.code() + ", Body: " + errorBody);
                        Toast.makeText(getActivity(), "Sign up failed: " + errorBody, Toast.LENGTH_SHORT).show();
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

        binding = SignupPageBinding.inflate(inflater, container, false);
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
                SignupUser(username, password);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
