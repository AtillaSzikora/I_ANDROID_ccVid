package com.szikora.ccvid;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    EditText newPassword, passwordAgain;
    TextView profileLabel;
    TextInputLayout passwordLayout;
    String intentData;
    String [] messagesToUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialization();
        setProfileEmailToUser();
    }

    private void initialization() {
        newPassword = (EditText) findViewById(R.id.newPasswordField);
        passwordAgain = (EditText) findViewById(R.id.passwordAgainField);
        profileLabel = (TextView) findViewById(R.id.profileLabel);
        passwordLayout = (TextInputLayout) findViewById(R.id.passwordAgainLayout);
        messagesToUser = new String[] {
                "Passwords do not match!",
                "Successfully saved changes!",
        };
    }

    private void setProfileEmailToUser() {
        intentData = getIntent().getStringExtra("email");
        profileLabel.setText(intentData);
    }

    public void saveButtonClick(View view) {
        if (!newPassword.getText().toString().equals(passwordAgain.getText().toString())) {
            passwordLayout.setError(messagesToUser[0]);
        } else {
            passwordLayout.setError(null);
            Toast.makeText(this, messagesToUser[1], Toast.LENGTH_LONG).show();
        }
    }
}
