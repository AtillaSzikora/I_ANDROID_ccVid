package com.szikora.ccvid;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;

public class ProfileActivity extends AppCompatActivity {

    private SharedPreferences mPrefs;
    private EditText newPassword, passwordAgain;
    private TextView profileLabel;
    private TextInputLayout newPasswordLayout, passwordAgainLayout;
    private ImageView profileImage;
    private String currentUserEmail;
    private String [] messagesToUser;
    private Gson gson;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialization();
        setProfile();
    }

    private void initialization() {
        currentUserEmail = getIntent().getStringExtra("email");
        newPassword = (EditText) findViewById(R.id.newPasswordField);
        passwordAgain = (EditText) findViewById(R.id.passwordAgainField);
        profileLabel = (TextView) findViewById(R.id.profileLabel);
        newPasswordLayout = (TextInputLayout) findViewById(R.id.newPasswordLayout);
        passwordAgainLayout = (TextInputLayout) findViewById(R.id.passwordAgainLayout);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        gson = new Gson();
        user = new User();
        messagesToUser = new String[] {
                "Passwords do not match!",
                "Successfully saved changes!",
        };
    }

    private void setProfile() {
        profileLabel.setText(currentUserEmail);
        String profileImageName = currentUserEmail.replace("@", "").replace(".", "");
        int profileImageId = getResources().getIdentifier(profileImageName, "drawable", getPackageName());
        profileImage.setImageResource(profileImageId);
    }
    

    public void saveButtonClick(View view) {
        if (!newPassword.getText().toString().matches(SigningActivity.passwordRegex)) {
            newPasswordLayout.setError(SigningActivity.messageToUser[5]);
            passwordAgainLayout.setError(null);
        } else if (!newPassword.getText().toString().equals(passwordAgain.getText().toString())) {
            passwordAgainLayout.setError(messagesToUser[0]);
            newPasswordLayout.setError(null);
        } else {
            updateUser();
            newPasswordLayout.setError(null);
            passwordAgainLayout.setError(null);
            Toast.makeText(this, messagesToUser[1], Toast.LENGTH_LONG).show();
        }
    }
    
    private void updateUser() {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        user.setEmail(currentUserEmail);
        user.setPassword(newPassword.getText().toString());
        String userJson = gson.toJson(user);
        prefsEditor.putString(user.getEmail(), userJson);
        prefsEditor.apply();
    }
}
