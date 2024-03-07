package com.sutock2;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.sutock2.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private void performLogout() {
        // Your logout logic here
        // Don't forget to update the login status in shared preferences
        PreferencesUtil.saveLoginStatus(this, false, "", "", "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        performLogout();
        PreferencesUtil.clearLoginDetails(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem logoutItem = menu.findItem(R.id.log_out);
        if (PreferencesUtil.isLoggedIn(this)) {
            // Assuming you have a method to get the logged-in username
            String username = PreferencesUtil.getLoggedInUsername(this);
            logoutItem.setTitle("Log out: " + username);
            logoutItem.setVisible(true);
        } else {
            logoutItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.log_out) {
            performLogout();
            PreferencesUtil.clearLoginDetails(this);
            Toast.makeText(this, "Log out successful", Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.AccountPage);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavDestination currentDestination = navController.getCurrentDestination();
        if (currentDestination != null && currentDestination.getId() == R.id.SecondFragment) {
            // If the current fragment is the main fragment, perform logout
            performLogout();
            PreferencesUtil.clearLoginDetails(this);
            Toast.makeText(this, "Log out successful", Toast.LENGTH_SHORT).show();
        }
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Log the user out when the main activity is destroyed
        performLogout();
        PreferencesUtil.clearLoginDetails(this);
    }
}