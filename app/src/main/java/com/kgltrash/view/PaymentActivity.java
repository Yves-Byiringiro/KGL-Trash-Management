package com.kgltrash.view;

import static com.google.android.gms.maps.MapsInitializer.initialize;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.kgltrash.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kgltrash.controller.CreateNotification;
import com.kgltrash.controller.CreatePayment;
import com.kgltrash.callback.HandleAsync;
import com.kgltrash.handler.LocationHandler;
import com.kgltrash.handler.UserRepository;
import com.kgltrash.handler.Handler;
import com.kgltrash.model.Notification;
import com.kgltrash.model.Payment;
import com.kgltrash.handler.UserTypeHandler;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.net.Uri;



public class PaymentActivity extends AppCompatActivity {
    private Handler handler;
    private Spinner monthSelected;
    private ProgressBar progressBar;private Button browseButton;
    private Button uploadButton;
    private ImageView home_button;
    private Uri imagePath;
    private String choosenImageFilePath;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final String SECONDARY_CHANNEL_ID = "secondary_notification_channel";
    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;
    private String currentUserPhone;
    private String currentDate;
    private String notificationTitle;
    private String notificationDescription;
    private CreatePayment createPayment;
    private  CreateNotification createNotification;


    /**
     * Author: Yves Byiringiro
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_payment_activity);

        monthSelected = (Spinner) findViewById(R.id.month_type);
        uploadButton = (Button) findViewById(R.id.upload_receipt);
        home_button = (ImageView) findViewById(R.id.home_button);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        createPayment = (CreatePayment) extras.getSerializable("createPayment");
        createNotification = (CreateNotification) extras.getSerializable("createNotification");
        currentUserPhone = extras.getString("phoneNumber");
        String currentUserStreetNumber= extras.getString("streetNumber");
        String currentUserHouseNumber= extras.getString("houseNumber");
        Boolean currentUserStatus = extras.getBoolean("userStatus");
        String currentUserType = extras.getString("userType");
        String currentUserLocation = extras.getString("userLocation");
        String image = extras.getString("imagePath");
        imagePath = Uri.parse(image);

        choosenImageFilePath = imagePath.toString();

        Bundle bundle = new Bundle();
        bundle.putString("image_path", choosenImageFilePath);
        ReceiptImageFragment receiptImageFragment = ReceiptImageFragment.newInstance(choosenImageFilePath);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.receipt_image_frame_layout, receiptImageFragment)
                .commit();

        uploadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String month = monthSelected.getSelectedItem().toString();
                checkHandlers(v,createPayment,createNotification, currentUserPhone, currentUserStatus, currentUserType, currentUserLocation, currentUserHouseNumber, currentUserStreetNumber,month, imagePath);

            }
        });
        createNotificationChannel();
        createSecondNotificationChannel();

        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                homeIntent.putExtra("phoneNumber", currentUserPhone);
                startActivity(homeIntent);
            }
        });
    }

    /**
     * Author: Yves Byiringiro
     */
    private void uploadImage(Uri imagePath, String currentUserPhone, String currentUserHouseNumber, String month)
    {
        if (imagePath != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference ref = storageRef.child("receipts/" + month + "_" + currentUserHouseNumber + "_" +  currentUserPhone);

            // adding listeners on upload failure image
            ref.putFile(imagePath) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                        }
                    });
        }
    }


    /**
     * Author: Yves Byiringiro
     */
    public void checkHandlers(View view, CreatePayment createPayment, CreateNotification createNotification, String userPhone, Boolean userStatus, String userType, String userLocation, String userHouseNumber, String userStreetNumber, String month, Uri imagePath){

        UserRepository repository = new UserRepository();
        handler = new UserTypeHandler(repository);
        handler.setNextHandler(new LocationHandler(repository));
        handler.setNextHandler(new UserTypeHandler(repository));

        payTrash(createPayment, createNotification, userPhone, userStatus, userType, userLocation,userHouseNumber, userStreetNumber, month, imagePath, view);
    }

    /**
     * Author: Yves Byiringiro
     */
    public void payTrash(CreatePayment createPayment,CreateNotification createNotification, String userPhone, Boolean userStatus, String userType, String userLocation, String userHouseNumber, String userStreetNumber, String month, Uri imagePath, View view) {
        if (handler.handle(userPhone, userStatus, userType, userLocation).equals("true")) {

            Payment newPayment = new Payment();
            newPayment.setPhone_number(userPhone);
            newPayment.setStreet_number(userStreetNumber);
            newPayment.setHouse_number(userHouseNumber);
            newPayment.setMonth(month);
            newPayment.setPicture("receipts/" + month + "_" + userHouseNumber + "_" +  userPhone);
            newPayment.setConfirmation(newPayment.getMonth() +"_"+ Calendar.getInstance().get(Calendar.YEAR)+"_"+ newPayment.getPhone_number());
            createPayment.submitPaymentReceipt(newPayment, new HandleAsync() {
                @Override
                public void loginProcess(boolean param) {
                }

                @Override
                public void paymentProcess(boolean param) {

                    if (param) {
                        uploadImage(imagePath,userPhone, userHouseNumber, month);
                        Toast.makeText(view.getContext(), R.string.payment_success, Toast.LENGTH_SHORT).show();
                        new CountDownTimer(5000, 1000) {

                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                               sendNotification(createNotification);
                            }}.start();

                    } else {
//                       progressBar.setVisibility(View.GONE);
                        Toast.makeText(view.getContext(), R.string.payment_failed, Toast.LENGTH_SHORT).show();
                        new CountDownTimer(5000, 1000) {

                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                sendNotification1(createNotification);
                            }}.start();
                    }
                }
            });

        } else {
            String msg = handler.handle(userPhone, userStatus, userType, userLocation);
            Toast.makeText(view.getContext(), msg, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Grace Tcheukounang
     */
    private NotificationCompat.Builder getNotificationBuilder(CreateNotification createNotification){
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        notificationTitle = "Payment Confirmation";
        notificationDescription = "Payment successfully received and validated";

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder( this,PRIMARY_CHANNEL_ID)
                .setContentTitle(notificationTitle)
                .setContentText(notificationDescription)
                .setSmallIcon(R.drawable.ic_android);
        Notification notification =  new Notification(currentDate,notificationTitle, notificationDescription,currentUserPhone);
        createNotification.addNotification(notification);
        return notifyBuilder;
    }

    /**
     * Grace Tcheukounang
     */
    private NotificationCompat.Builder getSecondNotificationBuilder(CreateNotification createNotification){
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        notificationTitle = "Payment Failed";
        notificationDescription = "Payment for this month has already been done";

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder( this,PRIMARY_CHANNEL_ID)
                .setContentTitle(notificationTitle)
                .setContentText(notificationDescription)
                .setSmallIcon(R.drawable.ic_android);
        Notification notification =  new Notification(currentDate,notificationTitle, notificationDescription,currentUserPhone);
        createNotification.addNotification(notification);
        return notifyBuilder;
    }

    public void createNotificationChannel()
    {
        mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=
            android.os.Build.VERSION_CODES.O) {
        // Create a NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    getString(R.string.notification_channel), NotificationManager
                    .IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription(getString(R.string.notification_from));
            mNotifyManager.createNotificationChannel(notificationChannel);
         }
    }

    /**
     * Grace Tcheukounang
     */
    public void createSecondNotificationChannel()
    {
        mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel(SECONDARY_CHANNEL_ID,
                    getString(R.string.notification_channel), NotificationManager
                    .IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription(getString(R.string.notification_from));
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    /**
     * Grace Tcheukounang
     */
    public void sendNotification(CreateNotification createNotification){
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder(createNotification);
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    /**
     * Grace Tcheukounang
     * sending notification when payment already done
     */
    public void sendNotification1(CreateNotification createNotification){
        NotificationCompat.Builder notifyBuilder = getSecondNotificationBuilder(createNotification);
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

}