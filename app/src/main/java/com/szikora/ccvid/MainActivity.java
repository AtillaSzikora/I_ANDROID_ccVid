package com.szikora.ccvid;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText email, password;
    private SharedPreferences mPrefs;
    private Map<String, ?> usersMap;
    private String userJson, passwordRegex;
    private Gson gson;
    private User user;
    private String[] toastMessages;

    private void initialization() {
        email = (EditText) findViewById(R.id.emailField);
        password = (EditText) findViewById(R.id.passwordField);
        mPrefs = getPreferences(MODE_PRIVATE);
        gson = new Gson();
        passwordRegex = "^[a-zA-Z0-9]{4,}$";
        toastMessages = new String[]{
                "Successfully signed in!",
                "Wrong email or password!",
                "User already exists!",
                " has successfully signed up!",
                "Wrong email format!",
                "Password has to be alphanumeric\nand at least 4 characters long!"};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
    }

    public void signInButtonClick(View view) {
        if (areEmailAndPassExist()) {
            Toast.makeText(MainActivity.this, toastMessages[0], Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, toastMessages[1], Toast.LENGTH_LONG).show();
        }
    }

    public void signUpButtonClick(View view) {
        if (areInputsValid()) {
            if (isEmailExist()) {
                Toast.makeText(MainActivity.this, toastMessages[2], Toast.LENGTH_SHORT).show();
            } else {
                signUpUser(new User(email.getText().toString(), password.getText().toString()));
                Toast.makeText(MainActivity.this, email.getText().toString() + toastMessages[3],
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean areInputsValid() {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            Toast.makeText(MainActivity.this, toastMessages[4], Toast.LENGTH_LONG).show();
            return false;
        } else if (!password.getText().toString().matches(passwordRegex)) {
            Toast.makeText(MainActivity.this, toastMessages[5], Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean areEmailAndPassExist() {
        usersMap = mPrefs.getAll();
        for (Map.Entry<String, ?> entry : usersMap.entrySet()) {
            userJson = (String) entry.getValue();
            user = gson.fromJson(userJson, User.class);
            if (user.getEmail().equals(email.getText().toString())
                    && user.getPassword().equals(password.getText().toString())) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmailExist() {
        usersMap = mPrefs.getAll();
        for (Map.Entry<String, ?> entry : usersMap.entrySet()) {
            userJson = (String) entry.getValue();
            user = gson.fromJson(userJson, User.class);
            if (user.getEmail().equals(email.getText().toString())) {
                return true;
            }
        }
        return false;
    }

    private void signUpUser(User user) {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        userJson = gson.toJson(user);
        prefsEditor.putString(user.getEmail(), userJson);
        prefsEditor.apply();
    }
}