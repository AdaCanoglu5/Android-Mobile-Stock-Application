package com.sutock2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.sutock2.databinding.FragmentSecondBinding;

public class StockTabPage extends Fragment {
    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.stock_tab_layout, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tabLayoutStock);
        tabLayout.addTab(tabLayout.newTab().setText("Graph"));
        tabLayout.addTab(tabLayout.newTab().setText("News"));
        tabLayout.addTab(tabLayout.newTab().setText("Forum"));

        String stockSymbol = null;
        if (getArguments() != null && getArguments().containsKey("stockSymbol")) {
            stockSymbol = getArguments().getString("stockSymbol");
        }

        if (stockSymbol != null) {

            Bundle bundle = new Bundle();
            bundle.putString("stockSymbol", stockSymbol);

            StockPage stockPageFragment = new StockPage();
            stockPageFragment.setArguments(bundle); // Set the arguments on the fragment

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container2, stockPageFragment) // Replace with your container ID
                    .commit();
        }
        else {
            Log.e("PackageEmpty", "Package is null");
        }

        String finalStockSymbol = stockSymbol;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        // Handle "News" tab
                        Bundle bundle = new Bundle();
                        bundle.putString("stockSymbol", finalStockSymbol);

                        selectedFragment = new StockPage();
                        selectedFragment.setArguments(bundle); // Set the arguments on the fragment
                        break;
                    case 1:
                        // Handle "Stocks" tab
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("stockSymbol", finalStockSymbol);

                        selectedFragment = new NewsFeedFragment();
                        selectedFragment.setArguments(bundle2);
                        break;
                    case 2:
                        // Handle "Stocks" tab
                        Bundle bundle3 = new Bundle();
                        bundle3.putString("stockSymbol", finalStockSymbol);

                        selectedFragment = new CommentsFragment();
                        selectedFragment.setArguments(bundle3);
                        break;
                }
                if (selectedFragment != null) {
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container2, selectedFragment) // Replace with your container ID
                            .commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle tab unselected if needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle tab reselected if needed
            }
        });

        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
