package com.kgltrash.view;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kgltrash.R;
import com.google.android.material.textfield.TextInputEditText;
import com.kgltrash.controller.CreateUser;
import com.kgltrash.model.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



/**
 * Author: Aanuoluwapo Orioke
 */
public class VerificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_verification_activity);

        TextInputEditText codeInput = (TextInputEditText) findViewById(R.id.code_input);
        Button verifyButton = (Button) this.findViewById(R.id.verifyButton);

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeEntered = codeInput.getText().toString();
                User user = (User) getIntent().getSerializableExtra("user");
                CreateUser createUser = (CreateUser) getIntent().getSerializableExtra("createUser");
                Boolean verIsSuccessful = createUser.verifyUser(user, codeEntered);
                if (verIsSuccessful)
                {
                    Toast.makeText(VerificationActivity.this, "Verification Successfully", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(VerificationActivity.this, LoginUserActivity.class);
                    loginIntent.putExtra("createUser", createUser);
                    finish();
                    startActivity(loginIntent);
                }
                else
                    Toast.makeText(VerificationActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}