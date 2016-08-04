package com.szikora.ccvid;

import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText email, password;
    private TextInputLayout emailLayout, passwordLayout;
    private SharedPreferences mPrefs;
    private Map<String, ?> usersMap;
    private String userJson, passwordRegex;
    private Gson gson;
    private User user;
    private String[] messageToUser;

    private void initialization() {
        email = (EditText) findViewById(R.id.emailField);
        password = (EditText) findViewById(R.id.passwordField);
        emailLayout = (TextInputLayout) findViewById(R.id.emailLayout);
        passwordLayout = (TextInputLayout) findViewById(R.id.passwordLayout);
        mPrefs = getPreferences(MODE_PRIVATE);
        gson = new Gson();
        passwordRegex = "^[a-zA-Z0-9]{4,}$";
        messageToUser = new String[]{
                "Successfully signed in!",
                "Wrong email or password!",
                "User already exists!",
                " has successfully signed up!",
                "Wrong email format!",
                "4+ characters long alphanumeric password needed!"};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
    }

    public void signInButtonClick(View view) {
        if (areEmailAndPassExist()) {
            Toast.makeText(MainActivity.this, messageToUser[0], Toast.LENGTH_LONG).show();
            emailLayout.setError(null);
            passwordLayout.setError(null);
        } else {
            Toast.makeText(MainActivity.this, messageToUser[1], Toast.LENGTH_LONG).show();
            emailLayout.setError(null);
            passwordLayout.setError(null);
        }
    }

    public void signUpButtonClick(View view) {
        if (areInputsValid()) {
            if (isEmailExist()) {
                emailLayout.setError(messageToUser[2]);
                passwordLayout.setError(null);
            } else {
                signUpUser(new User(email.getText().toString(), password.getText().toString()));
                Toast.makeText(MainActivity.this, email.getText().toString() + messageToUser[3],
                        Toast.LENGTH_LONG).show();
                emailLayout.setError(null);
                passwordLayout.setError(null);
            }
        }
    }

    private boolean areInputsValid() {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            emailLayout.setError(messageToUser[4]);
            passwordLayout.setError(null);
            return false;
        } else if (!password.getText().toString().matches(passwordRegex)) {
            passwordLayout.setError(messageToUser[5]);
            emailLayout.setError(null);
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