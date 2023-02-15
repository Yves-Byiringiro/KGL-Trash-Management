package com.kgltrash.view;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kgltrash.R;
import com.kgltrash.callback.GetNotificationCallback;
import com.kgltrash.controller.CreateNotification;
import com.kgltrash.model.Notification;
import com.kgltrash.model.NotificationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/**
 * Grace Tcheukounang
 */

public class NotificationActivity extends AppCompatActivity
{
      private String phoneNumber;
      private ListView notificationListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_notifications_activity);

        //get phoneNumber from intent
        CreateNotification createNotification = (CreateNotification) getIntent().getSerializableExtra("createNotification");

        phoneNumber = (String) getIntent().getStringExtra("phoneNumber");
        //callback method here
        createNotification.getNotifications(phoneNumber, new GetNotificationCallback() {
            @Override
            public void getAllNotifications(ArrayList<Notification> returnNotificationList) {
                if (returnNotificationList.size() > 0) {
                    Collections.sort(returnNotificationList, new Comparator<Notification>(){
                        public int compare(Notification o1, Notification o2){
                            return o2.getDate().compareTo(o1.getDate());
                        }
                    });
                   ArrayList<NotificationView> arrayList =  new ArrayList<NotificationView>();
                   for(Notification n: returnNotificationList)
                   {
                       arrayList.add(new NotificationView(n.getTitle(),n.getDescription(), n.getDate()));
                   }

                   NotificationViewAdapter notificationArrayAdapter = new NotificationViewAdapter(getApplicationContext(),arrayList);
                   notificationListView = findViewById(R.id.notification_listView);
                   notificationListView.setAdapter(notificationArrayAdapter);

                } else {
                    Toast.makeText(NotificationActivity.this, R.string.notification_list, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
