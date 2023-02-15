package com.kgltrash.view;

import static com.google.android.gms.maps.MapsInitializer.initialize;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kgltrash.R;
import com.kgltrash.callback.GetAllUsersInAHouseCallback;
import com.kgltrash.controller.CreateUser;
import com.kgltrash.model.User;
import com.kgltrash.model.UserFactory;
import com.kgltrash.model.UserType;

import java.util.ArrayList;

/*
 * Author: Aanuoluwapo Orioke
 */

public class RegistrationActivity extends AppCompatActivity {

    private EditText name;
    private EditText phoneNumber;
    private EditText password;
    private boolean isActive = false;
    private String code ="000";
    private EditText address;
    private UserType userType;
    private EditText houseNumber = null;
    private EditText streetNumber = null;
    private EditText city = null;
    private String nameStr;
    private String phoneNumberStr;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static final int LOCATION_REQUEST_CODE = 1;
    private String verificationCode;
    private BroadcastReceiver smsBroadcastSent;
    private BroadcastReceiver smsBroadcastDelivered;
    private int pendingFlag = PendingIntent.FLAG_MUTABLE;
    private static final String SMS_SENT = "Sms sent";
    private static final String SMS_DELIVERED = "Sms delivered";
    private ArrayList<User> allUsersInHouse_;


    /**
     * Author: Aanuoluwapo Orioke
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_registration_activity);
        Spinner userTypeDropDown = (Spinner) this.findViewById(R.id.language);
        Button registerButton = (Button) this.findViewById(R.id.register_button);
        Button mapCityButton = (Button) this.findViewById(R.id.locate_city_button);

        name =((EditText) findViewById(R.id.input_name)) ;
        phoneNumber = ((EditText) findViewById(R.id.input_phone));
        password = ((EditText) findViewById(R.id.password));
        String userTypeString = userTypeDropDown.getSelectedItem().toString();
        userType = UserType.fromStringToUserType(userTypeString);
        streetNumber = ((EditText) findViewById(R.id.street_number));
        houseNumber = ((EditText) findViewById(R.id.house_number));
        city = ((EditText) findViewById(R.id.city_name));
        allUsersInHouse_ = new ArrayList<User>();
        initialize(this);
        //calling the receiver here for sending sms
        initialiseSmsReceiver();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateUser createUser = (CreateUser) getIntent().getSerializableExtra("createUser");

                if (!checkIfInputsAreEmpty() && !checkIfPhoneNumberContainsLetters()) {
                    createUser.getUsersInHouse(streetNumber.getText().toString(), houseNumber.getText().toString(), new GetAllUsersInAHouseCallback() {
                        @Override
                        public void getAllUsersInAHouse(ArrayList<User> allUsersInHouse) {
                            if (allUsersInHouse.size() > 0 && userTypeDropDown.getSelectedItem().toString().equalsIgnoreCase(UserType.PRIMARY_USER.toString())) {
                                Toast.makeText(getBaseContext(),
                                        "Registration Failed. Primary User already Exists", Toast.LENGTH_LONG).show();

                            } else {

                                verificationCode = "" + 1000 + (int) (Math.random() * 9999);
                                User nUser = new User(name.getText().toString(), phoneNumber.getText().toString(),
                                        password.getText().toString(), userType, false, city.getText().toString(),verificationCode);
                                User newUser = UserFactory.createUser(userTypeString, nUser,
                                        houseNumber.getText().toString(), streetNumber.getText().toString(), city.getText().toString());

                                createUser.createUser(newUser);
                                sendSms(verificationCode, phoneNumber.getText().toString());

                                Intent verificationIntent = new Intent(RegistrationActivity.this, VerificationActivity.class);
                                verificationIntent.putExtra("user", newUser);
                                verificationIntent.putExtra("createUser", createUser);
                                finish();
                                startActivity(verificationIntent);
                            }
                        }
                    });
                }
                else {
                    if (checkIfPhoneNumberContainsLetters())
                        Toast.makeText(getBaseContext(),
                                "Phone number must contain numbers only", Toast.LENGTH_LONG).show();
                    if (checkIfInputsAreEmpty())
                        Toast.makeText(getBaseContext(),
                                "Fill all fields", Toast.LENGTH_LONG).show();
                }
            }
        });

        mapCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        nameStr = String.valueOf(s);
                    }
                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                Intent locationIntent = new Intent(RegistrationActivity.this, LocationActivity.class);
                startActivityForResult(locationIntent, LOCATION_REQUEST_CODE);

            }
        });

    }

    /**
     * Author: Aanuoluwapo Orioke
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("nameVal", nameStr);
    }

    /**
     * Author: Aanuoluwapo Orioke
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nameStr = savedInstanceState.getString("nameVal");
        city.setText((String) getIntent().getSerializableExtra("city"));
    }

    /**
     * Author: Aanuoluwapo Orioke
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == LOCATION_REQUEST_CODE) {
            if (data.hasExtra("city")) {
                city.setText(data.getExtras().getString("city"));
            }
        }
    }
    /**
     * Author: Aanuoluwapo Orioke
     */
    private boolean checkIfInputsAreEmpty(){
        if (name.getText().toString().equalsIgnoreCase("")
            || phoneNumber.getText().toString().equalsIgnoreCase("")
            || streetNumber.getText().toString().equalsIgnoreCase("")
            || houseNumber.getText().toString().equalsIgnoreCase("")
            || password.getText().toString().equalsIgnoreCase("")
            || city.getText().toString().equalsIgnoreCase("")
        )
        {
            return true;
        }
        return false;
    }

    private boolean checkIfPhoneNumberContainsLetters(){
        if (phoneNumber.getText().toString().matches("^[0-9]+$"))
        {
            return false;
        }
        return true;
    }

    /**
     * author Grace Tcheukounang
     */
    private void initialiseSmsReceiver() {
        smsBroadcastSent = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(),
                                "SMS sent successfully", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getBaseContext(),
                                "Error: SMS was not sent", Toast.LENGTH_SHORT).show();
                        break;
                }

                unregisterReceiver(smsBroadcastSent);
            }
        };


        smsBroadcastDelivered = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(),
                                "SMS delivered successfully", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        Toast.makeText(getBaseContext(),
                                "Error: SMS was not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
                unregisterReceiver(smsBroadcastDelivered);
            }
        };
    }

    /**
     * author Grace Tcheukounang
     */
  private void sendSms(String code, String phoneNumber)
  {
      checkSmsPermission();
      IntentFilter filter = new IntentFilter(SMS_SENT);
      registerReceiver(smsBroadcastSent, filter);
      filter = new IntentFilter(SMS_DELIVERED);
      registerReceiver(smsBroadcastDelivered, filter);

      String message = "Your verification code is"+ code;

      String scAddress = null;
      //have to set this value later
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S)
          pendingFlag = 0;


      PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), pendingFlag);

      PendingIntent deliveryIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED),pendingFlag);
      SmsManager smsManager = SmsManager.getDefault();
      smsManager.sendTextMessage
              (phoneNumber, scAddress, message,
                      sentIntent, deliveryIntent);


  }

    /**
     * author Grace Tcheukounang
     */
    private void checkSmsPermission() {
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
    }

    /**
     * author Grace Tcheukounang
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSms(verificationCode, phoneNumber.getText().toString());
                }
            }
        }
    }
}