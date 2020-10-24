package com.example.kidstodoapp;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Utility {
    private static Context applicationContext;
    private static final String PASSWORD_HASH_FILE_NAME = "pwd-hash";
    private static String passwordHash;
    private static String phoneNumber;
    private static Boolean inParentMode = false;
    private static Boolean phoneNumberSet = false;

    public static void setApplicationContext(Context context) {
        applicationContext = context;
    }

    public static boolean retrievePasswordHash() {
        try {
            FileInputStream fis = applicationContext.openFileInput(PASSWORD_HASH_FILE_NAME);
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            passwordHash = reader.readLine();
            return true;
        } catch (Exception e) {
            Log.e("Utility", Objects.requireNonNull(e.getMessage()));
            return false;
        }
    }
    public static void setPassword(String password) {
        Utility.passwordHash = hashPassword(password);
        storePasswordHash();
    }
    public static boolean isCorrectPassword(String password) {
        return hashPassword(password).equals(Utility.passwordHash);
    }

    public static Boolean isInParentMode() {return inParentMode;}
    public static void setInParentMode(Boolean inParentMode) {Utility.inParentMode = inParentMode;}

    public static String getPhoneNumber() {
        return phoneNumber;
    }
    public static void setPhoneNumber(String phoneNumber) {
        Utility.phoneNumber = phoneNumber;
        phoneNumberSet = true;
    }
    public static Boolean isPhoneNumberSet() {
        return phoneNumberSet;
    }

    public static void stopHandler(Handler handler, Runnable runnable) {
        handler.removeCallbacks(runnable);
    }
    public static void startHandler(Handler handler, Runnable runnable) {
        handler.postDelayed(runnable, 10000);
    }

    private static String hashPassword(String password) {
        return password;
    }
    private static void storePasswordHash() {
        try (FileOutputStream fos = applicationContext.openFileOutput(PASSWORD_HASH_FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(passwordHash.getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception e) {
            Log.e("Utility", Objects.requireNonNull(e.getMessage()));
        }
    }

}
