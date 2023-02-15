package com.kgltrash.view;

import static com.google.android.gms.maps.MapsInitializer.initialize;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.kgltrash.R;
import com.kgltrash.controller.CreateAnalytics;
import com.kgltrash.controller.CreateNotification;
import com.kgltrash.controller.CreatePayment;
import com.kgltrash.controller.CreateUser;
import com.kgltrash.dao.UserDatabase;
import com.kgltrash.DBLayout.UserEntity;

import java.util.List;

/**
 * co-author by Yves Byiringiro and Grace Tcheukounang
 */

public class HomeActivity extends AppCompatActivity {
    private String phoneNumber;
    private UserDatabase userDatabase;
    private List<UserEntity> userEntities;
    private UserEntity loggedInUser;
    private TextView loggedIn_username;
    private String[] usernameFromArray = new String[1];


    CardView payment_card;
    CardView notifications_card;
    CardView sendSMS_card;
    CardView logout_card;
    CardView sendSMSToHouseCard;
    CardView analyticsCard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_home_activity);

        CreateUser createUser = (CreateUser) getIntent().getSerializableExtra("createUser");
        CreatePayment createPayment = (CreatePayment) getIntent().getSerializableExtra("createPayment");
        CreateNotification createNotification = (CreateNotification) getIntent().getSerializableExtra("createNotification");
        CreateAnalytics createAnalytics = (CreateAnalytics) getIntent().getSerializableExtra("createAnalytics");
        phoneNumber = (String) getIntent().getStringExtra("phoneNumber");
        userDatabase = UserDatabase.getDatabase(this.getApplicationContext());

        payment_card = findViewById(R.id.payment_card);
        notifications_card =  findViewById(R.id.notifications_card);
        sendSMS_card = findViewById(R.id.send_sms_card);
        sendSMSToHouseCard = findViewById(R.id.send_sms_house_card);
        analyticsCard = findViewById(R.id.get_analytics_card);
        logout_card = findViewById(R.id.logout_card);
        loggedIn_username = (TextView) findViewById(R.id.username);

        /**
         * author Grace Tcheukounang
         */

        payment_card.setVisibility(View.GONE);
        notifications_card.setVisibility(View.GONE);
        sendSMS_card.setVisibility(View.GONE);
        sendSMSToHouseCard.setVisibility(View.GONE);
        analyticsCard.setVisibility(View.GONE);

        //get the logged in user from the room database
        loggedInUser = userDatabase.userDao().getSingleUser(phoneNumber);
        usernameFromArray = loggedInUser.getName().split(" ");
        loggedIn_username.setText(loggedInUser.getName());
        if (loggedInUser.getUserType().equalsIgnoreCase("Trash_collector")){
            sendSMS_card.setVisibility(View.VISIBLE);
            notifications_card.setVisibility(View.VISIBLE);
            sendSMSToHouseCard.setVisibility(View.VISIBLE);
            analyticsCard.setVisibility(View.VISIBLE);
        } else if(loggedInUser.getUserType().equalsIgnoreCase("Primary_user") || loggedInUser.getUserType().equalsIgnoreCase("Secondary_user")){
            payment_card.setVisibility(View.VISIBLE);
            notifications_card.setVisibility(View.VISIBLE);
        }

        /**
         * Author: Yves Byiringiro
         */
        payment_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(getApplicationContext(),UploadImageActivity.class);
                Bundle dataBundle = new Bundle();
                dataBundle.putString("phoneNumber",loggedInUser.getPhoneNumber());
                dataBundle.putString("streetNumber",loggedInUser.getStreetNumber());
                dataBundle.putString("houseNumber",loggedInUser.getHouseNumber());
                dataBundle.putBoolean("userStatus",loggedInUser.isActive());
                dataBundle.putString("userType",loggedInUser.getUserType());
                dataBundle.putString("userLocation",loggedInUser.getCity());
                dataBundle.putSerializable("createPayment", createPayment);
                dataBundle.putSerializable("createNotification", createNotification);

                mainIntent.putExtras(dataBundle);
                startActivity(mainIntent);
            }
        });


        sendSMS_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendSMSToStreets = new Intent(getApplicationContext(), MessageStreetActivity.class);
                sendSMSToStreets.putExtra("createUser", createUser);
                startActivity(sendSMSToStreets);
            }

        });

        /**
         * Author: Aanuoluwapo Orioke
         */
        sendSMSToHouseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendSMSToHouses = new Intent(getApplicationContext(), MessageHouseActivity.class);
                sendSMSToHouses.putExtra("createUser", createUser);
                startActivity(sendSMSToHouses);
            }

        });

        /**
         * Author: Yves Byiringiro
         */
        analyticsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(getApplicationContext(),AnalyticsActivity.class);
                Bundle dataBundle = new Bundle();
                dataBundle.putSerializable("createAnalytics", createAnalytics);
                mainIntent.putExtras(dataBundle);
                startActivity(mainIntent);
            }
        });

        /**
         * author Grace Tcheukounang
         */

        notifications_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loggedInUser.getUserType().equalsIgnoreCase("Trash_collector")){
                    Toast.makeText(HomeActivity.this, "This feature has not been yet implemented!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent notificationIntent = new Intent(getApplicationContext(), NotificationActivity.class);
                    notificationIntent.putExtra("phoneNumber",phoneNumber);
                    notificationIntent.putExtra("createNotification", createNotification);
                    startActivity(notificationIntent);
                }
            }
        });

        /**
         * author Grace Tcheukounang
         */
        logout_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userDatabase.userDao().deleteUser(phoneNumber) >= 1) {
                    Toast.makeText(HomeActivity.this, R.string.logout_success, Toast.LENGTH_SHORT).show();
                    Intent logoutIntent = new Intent(HomeActivity.this, LoginUserActivity.class);
                    finish();
                    startActivity(logoutIntent);
                }else
                {
                    Toast.makeText(HomeActivity.this, R.string.logout_failed, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
