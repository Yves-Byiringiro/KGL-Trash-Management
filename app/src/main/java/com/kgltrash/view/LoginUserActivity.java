package com.kgltrash.view;

import static com.google.android.gms.maps.MapsInitializer.initialize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kgltrash.R;
import com.kgltrash.controller.ConfigAPI;
import com.kgltrash.controller.CreateAnalytics;
import com.kgltrash.controller.CreateNotification;
import com.kgltrash.controller.CreatePayment;
import com.kgltrash.controller.CreateUser;
import com.kgltrash.callback.HandleAsync;
import com.kgltrash.dao.UserDatabase;

import java.util.Locale;

/**
 * author Grace Tcheukounang
 */
public class LoginUserActivity extends AppCompatActivity {

    private EditText phoneNumber;
    private EditText password;
    private Button loginBtn;
    private TextView createAccount;
    private ProgressBar spinner;
    private UserDatabase userDatabase;
    private Spinner languageDropDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_login_user_activity);

        //initializing userDatabase
        userDatabase = UserDatabase.getDatabase(this.getApplicationContext());

        phoneNumber = findViewById(R.id.input_phone);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        languageDropDown = (Spinner) this.findViewById(R.id.language);

        /**
         * Author: Aanuoluwapo Orioke (language dropdown listener)
         */

        initialize(this);
        languageDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            Locale locale;
            Configuration config;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        break;
                    case 1:
                        setLocale("en");
                        break;
                    case 2:
                        setLocale("fr");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        createAccount = findViewById(R.id.create_account);
        ConfigAPI api = new ConfigAPI();
        CreateUser createUser = api;
        CreatePayment createPayment = api;
        CreateNotification createNotification = api;
        CreateAnalytics createAnalytics = api;

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIfInputsAreEmpty()) {
                spinner.setVisibility(View.VISIBLE);
                //call to the proxy api
                createUser.loginUser(phoneNumber.getText().toString().trim(), password.getText().toString().trim(),userDatabase, new HandleAsync() {
                    @Override
                    public void loginProcess(boolean param) {
                        if (param) {
                            spinner.setVisibility(View.GONE);
                            Toast.makeText(LoginUserActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(LoginUserActivity.this, HomeActivity.class);
                            homeIntent.putExtra("createUser", createUser);
                            homeIntent.putExtra("createPayment", createPayment);
                            homeIntent.putExtra("createNotification", createNotification);
                            homeIntent.putExtra("createAnalytics",createAnalytics);
                            homeIntent.putExtra("phoneNumber", phoneNumber.getText().toString());
                            finish();
                            startActivity(homeIntent);
                        } else {
                            spinner.setVisibility(View.GONE);
                            Toast.makeText(LoginUserActivity.this,R.string.login_failed, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void paymentProcess(boolean param) {

                    }
                });
                } else {
                    Toast.makeText(getBaseContext(),R.string.enter_number_and_password, Toast.LENGTH_LONG).show();
                }
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrationIntent = new Intent(LoginUserActivity.this, RegistrationActivity.class);
                registrationIntent.putExtra("createUser", createUser);
                finish();
                startActivity(registrationIntent);

            }
        });
    }

    /**
     * Author: Aanuoluwapo Orioke
     */
    private void setLocale(String language) {
        Locale myLocale = new Locale(language);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Configuration configuration = getResources().getConfiguration();
        configuration.locale = myLocale;
        getResources().updateConfiguration(configuration, displayMetrics);
        Intent reCreateLoginActivity = new Intent(this, LoginUserActivity.class);
        finish();
        startActivity(reCreateLoginActivity);
    }

    /**
     * Author: Aanuoluwapo Orioke
     */
    private boolean checkIfInputsAreEmpty(){
        if (phoneNumber.getText().toString().equalsIgnoreCase("")
            || password.getText().toString().equalsIgnoreCase("")
        )
        {
            return true;
        }
        return false;
    }
    // end of code segment by Aanuoluwapo Orioke

}