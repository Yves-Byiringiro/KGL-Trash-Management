package com.kgltrash.view;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.kgltrash.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Author: Rose Mary

public class MessageActivity extends Activity implements AdapterView.OnItemSelectedListener
{
    private TextView streetView, houseView, messageView;
    private EditText messageText, phoneText;
    private Button sendButton;
    private Spinner houseSpinner;
    private DatabaseReference fDataBase;
    String [] houses = {"56","20"};

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;

    /**
     * Author: Rose Mary
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_message_activity);

        fDataBase = FirebaseDatabase.getInstance().getReference();
        houseView = this.findViewById(R.id.house_number);
        messageView = this.findViewById(R.id.message);

        houseSpinner = this.findViewById(R.id.house_spinner_message_house);
        houseSpinner.setOnItemSelectedListener(this);
        ArrayAdapter houseAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, houses);
        houseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        houseSpinner.setAdapter(houseAdapter);

        messageText = this.findViewById(R.id.message_box);
        sendButton = this.findViewById(R.id.send_message);

        sendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                sendSMS();
                finish();
            }
        });
    }
    /**
     * Author: Rose Mary
     */
    protected void sendSMS()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        else
        {
            SendTextMsg();
        }
    }
    /**
     * Author: Rose Mary
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_SEND_SMS:
            {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    SendTextMsg();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    //Author: Rose Mary
    private void SendTextMsg()
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneText.getText().toString(), null, messageText.getText().toString(), null, null);

        Toast.makeText(getApplicationContext(), "SMS sent.",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected (AdapterView<?> arg0, View arg1, int position, long id)
    {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }

}