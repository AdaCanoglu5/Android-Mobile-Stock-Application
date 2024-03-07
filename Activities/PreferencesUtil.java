package com.sutock2;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {

    private static final String MY_APP_PREFS = "MyAppPrefs";

    public static void saveLoginStatus(Context context, boolean isLoggedIn, String username, String password, String id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_APP_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.putString("username", username); // Save the username
        editor.putString("password", password); // Save the password
        editor.putString("id", id); // Save the id
        editor.apply();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_APP_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    public static String getLoggedInUsername(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", ""); // Return an empty string or a default value if not found
    }

    public static String getLoggedInId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("id", ""); // Return an empty string or a default value if not found
    }
    public static void clearLoginDetails(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("isLoggedIn");
        editor.remove("username"); // Clear the username
        editor.remove("password"); // Clear the username
        editor.remove("id"); // Clear the id
        editor.apply();
    }
}
