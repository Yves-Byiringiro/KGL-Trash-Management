package com.kgltrash.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kgltrash.R;
import com.kgltrash.callback.AllPhoneNumbersInHouse;
import com.kgltrash.callback.AllPhoneNumbersInStreet;
import com.kgltrash.callback.GetAllHousesOnStreet;
import com.kgltrash.callback.GetAllStreets;
import com.kgltrash.controller.CreateUser;

import java.util.ArrayList;
import java.util.HashSet;

/*
 * Author: Aanuoluwapo Orioke
 */

public class MessageHouseActivity extends AppCompatActivity {

    private Spinner streetsDropDown;
    private Spinner houseDropDown;
    private EditText message;
    private Button sendMessageButton;
    private String phoneNumber_;
    private String selectedStreet = "";
    private String selectedHouse = "";
    private ArrayList<String> phoneNumbers = new ArrayList<String>();
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    PendingIntent sentPendingIntent;
    PendingIntent deliveredPendingIntent;
    private BroadcastReceiver sentBroadcastReceiver;
    private BroadcastReceiver deliveredBroadcastReceiver;
    private int pendingFlag = PendingIntent.FLAG_MUTABLE;
    private static final String SMS_SENT = "Sms sent";
    private static final String SMS_DELIVERED = "Sms delivered";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_house);

        streetsDropDown = (Spinner) findViewById(R.id.street_spinner_message_house);
        message = (EditText) findViewById(R.id.message_box_message_house);
        sendMessageButton = (Button) findViewById(R.id.send_message_message_house);
        houseDropDown = (Spinner) findViewById(R.id.house_spinner_message_house);

        CreateUser createUser = (CreateUser) getIntent().getSerializableExtra("createUser");

        sentBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getApplicationContext(), "SMS sent successfully.",
                                Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),
                                "SMS failed to send, please try again.", Toast.LENGTH_LONG).show();
                        break;
                }
                unregisterReceiver(sentBroadcastReceiver);

            }
        };

        deliveredBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getApplicationContext(), "SMS delivered succesfully.",
                                Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),
                                "SMS failed to deliver, please try again.", Toast.LENGTH_LONG).show();
                        break;
                }
                unregisterReceiver(deliveredBroadcastReceiver);
            }

        };

        createUser.getAllStreets(new GetAllStreets() {
            @Override
            public void getStreets(HashSet<String> streetList) {
                if (!streetList.isEmpty())
                {
                    ArrayList<String> streetArrList = new ArrayList<String>();
                    for (String street : streetList)
                    {
                        streetArrList.add(street);
                        System.out.println(street);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, streetArrList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    streetsDropDown.setAdapter(arrayAdapter);
                }
            }
        });

        streetsDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String street = parent.getItemAtPosition(position).toString();
                selectedStreet = street;

                createUser.getAllHousesOnStreet(selectedStreet, new GetAllHousesOnStreet() {
                    @Override
                    public void getHouses(HashSet<String> houseList) {
                        if (!houseList.isEmpty())
                        {
                            ArrayList<String> houseArrList = new ArrayList<String>();
                            for (String house : houseList)
                            {
                                houseArrList.add(house);
                                System.out.println(house);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, houseArrList);
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            houseDropDown.setAdapter(arrayAdapter);
                        }
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });


        houseDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String house = parent.getItemAtPosition(position).toString();
                selectedHouse = house;

                Toast.makeText(parent.getContext(), "Selected house: " + house + " on street: " + selectedStreet, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        sendMessageButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                createUser.allPhoneNumberInHouse(selectedStreet, selectedHouse, new AllPhoneNumbersInHouse() {
                    @Override
                    public void getAllPhoneNumbersInHouse(ArrayList<String> phoneNumberList) {
                        if (!phoneNumberList.isEmpty() && !message.getText().toString().equalsIgnoreCase(""))
                        {
                            for (String phoneNumber : phoneNumberList)
                            {
                                //send SMS to each number
                                phoneNumbers.add(phoneNumber);
                                phoneNumber_ = phoneNumber;
                                sendSms(message.getText().toString(), phoneNumber);
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        IntentFilter sentIntent = new IntentFilter("SMS Sent");
        IntentFilter deliveredIntent = new IntentFilter("SMS Delivered");
        registerReceiver(sentBroadcastReceiver, sentIntent);
        registerReceiver(deliveredBroadcastReceiver, deliveredIntent);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    sentPendingIntent = PendingIntent.getBroadcast(this, 1, new Intent("SMS Sent"), PendingIntent.FLAG_UPDATE_CURRENT);
                    deliveredPendingIntent = PendingIntent.getBroadcast(this, 2, new Intent("SMS Delivered"), PendingIntent.FLAG_UPDATE_CURRENT);
                    smsManager.sendTextMessage(phoneNumber_, null, message.getText().toString(), sentPendingIntent, deliveredPendingIntent);
                }
            }
        }

    }



    private void sendSms(String message, String phoneNumber)
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
        IntentFilter sentIntent = new IntentFilter("SMS Sent");
        IntentFilter deliveredIntent = new IntentFilter("SMS Delivered");
        registerReceiver(sentBroadcastReceiver, sentIntent);
        registerReceiver(deliveredBroadcastReceiver, deliveredIntent);
        SmsManager smsManager = SmsManager.getDefault();
        sentPendingIntent = PendingIntent.getBroadcast(this, 1, new Intent("SMS Sent"), PendingIntent.FLAG_UPDATE_CURRENT);
        deliveredPendingIntent = PendingIntent.getBroadcast(this, 2, new Intent("SMS Delivered"), PendingIntent.FLAG_UPDATE_CURRENT);
        smsManager.sendTextMessage(phoneNumber, null, message, sentPendingIntent, deliveredPendingIntent);
    }

}