package com.kgltrash.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kgltrash.R;
import com.kgltrash.controller.CreateNotification;
import com.kgltrash.controller.CreatePayment;


/**
 * Co-author by  Yves Byiringiro and Aanuoluwapo Orioke
 */
public class UploadImageActivity extends AppCompatActivity {
    private Button browseButton;
    private Button nextButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imagePath;
    private String choosenImageFilePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_upload_image_activity);

        browseButton = (Button) findViewById(R.id.browse_image);
        nextButton = (Button) findViewById(R.id.next_btn);

        nextButton.setVisibility(View.GONE);
        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        Bundle extras = getIntent().getExtras();
        String currentUserPhone = extras.getString("phoneNumber");
        String currentUserStreetNumber = extras.getString("streetNumber");
        String currentUserHouseNumber = extras.getString("houseNumber");
        Boolean currentUserStatus = extras.getBoolean("userStatus");
        String currentUserType = extras.getString("userType");
        String currentUserLocation = extras.getString("userLocation");
        CreatePayment createPayment = (CreatePayment) extras.getSerializable("createPayment");
        CreateNotification createNotification = (CreateNotification) getIntent().getSerializableExtra("createNotification");


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri img  = getImagePath();
                Intent mainIntent = new Intent(getApplicationContext(),PaymentActivity.class);
                Bundle dataBundle = new Bundle();
                dataBundle.putString("phoneNumber",currentUserPhone);
                dataBundle.putString("streetNumber",currentUserStreetNumber);
                dataBundle.putString("houseNumber",currentUserHouseNumber);
                dataBundle.putBoolean("userStatus",currentUserStatus);
                dataBundle.putString("userType",currentUserType);
                dataBundle.putString("userLocation",currentUserLocation);
                dataBundle.putString("imagePath", img.toString());
                dataBundle.putSerializable("createPayment", createPayment);
                dataBundle.putSerializable("createNotification", createNotification);

                mainIntent.putExtras(dataBundle);
                startActivity(mainIntent);

            }
        });


    }


    private void openFileChooser() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image"),
                PICK_IMAGE_REQUEST);
        nextButton.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            imagePath = data.getData();
            choosenImageFilePath = imagePath.toString();

            Bundle bundle = new Bundle();
            bundle.putString("image_path", choosenImageFilePath);
            ReceiptImageFragment receiptImageFragment = ReceiptImageFragment.newInstance(choosenImageFilePath);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.receipt_image_frame_layout, receiptImageFragment)
                    .commit();
        }
    }

    private Uri getImagePath(){
        if (imagePath != null)
            return imagePath;
        else
            return null;
    }

}
