package com.kgltrash.controller;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.kgltrash.callback.AllPhoneNumbersInHouse;
import com.kgltrash.callback.AllPhoneNumbersInStreet;
import com.kgltrash.callback.GetAllHousesOnStreet;
import com.kgltrash.callback.GetAllStreets;
import com.kgltrash.callback.GetAllUsersInAHouseCallback;
import com.kgltrash.callback.GetNotificationCallback;
import com.kgltrash.callback.GetReceipts;
import com.kgltrash.callback.HandleAsync;
import com.kgltrash.dao.UserDatabase;
import com.kgltrash.DBLayout.UserEntity;
import com.kgltrash.model.Notification;
import com.kgltrash.model.Payment;
import com.kgltrash.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kgltrash.model.UserType;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public abstract class ProxyApp {
    private String code = "";
    private static FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();

    private String pNumber ;
    private String pwd;
    private String name;
    private String hNumber;
    private String sNumber;
    private String userType;
    private boolean isActive;
    private String userLocation;
    private ArrayList<User> allUsersInHouse_;

    /**
     * Author: Aanuoluwapo Orioke
     */
    public boolean createUser(User user) {
        firebaseDB.collection("users").document("" + user.getPhoneNumber()).set(user);

//        return true;
        final boolean[] exists_ = new boolean[1];
        firebaseDB.collection("users")
                .whereEqualTo("phoneNumber", user.getPhoneNumber().trim())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int resultSize  = task.getResult().size();
                            if (resultSize > 0){
                                exists_[0] = true;
                            }
                            else {
                                firebaseDB.collection("users").document("" + user.getPhoneNumber()).set(user);
                                exists_[0] = false;
                            }
                        }
                    }
                });
        return !exists_[0];
    }

    /**
     * Author: Aanuoluwapo Orioke
     */
    public boolean checkIfPrimaryUserExistsForAHouse(String streetNum, String houseNum) {
        getUsersInHouse(streetNum, houseNum, new GetAllUsersInAHouseCallback() {
            @Override
            public void getAllUsersInAHouse(ArrayList<User> allUsersInHouse) {
                if (allUsersInHouse.size() > 0) {
                    allUsersInHouse_ = allUsersInHouse;
                } else {
                    allUsersInHouse_ = null;
                }
            }
        });
                if (allUsersInHouse_ == null)
                    return false;
                for (User user : allUsersInHouse_)
                {
                    if (user.getUserType().toString().equalsIgnoreCase(UserType.PRIMARY_USER.toString()))
                        return true;
                }
                return false;
    }

    /**
     * Author: Aanuoluwapo Orioke
     */
    private void getAllUsersInHouse(String streetNum, String houseNum, GetAllUsersInAHouseCallback getAllUsersInAHouseCallback) {
        ArrayList<User> usersInHouse = new ArrayList<User>();
        firebaseDB.collection("users")
                .whereEqualTo("streetNumber", streetNum)
                .whereEqualTo("houseNumber", houseNum)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                usersInHouse.add(user);
                            }
                            getAllUsersInAHouseCallback.getAllUsersInAHouse(usersInHouse);
                        } else {
                            getAllUsersInAHouseCallback.getAllUsersInAHouse(usersInHouse);
                        }
                    }
                });

    }

    public void getUsersInHouse(String streetNum, String houseNum, GetAllUsersInAHouseCallback getAllUsersInAHouseCallback){
        getAllUsersInHouse(streetNum, houseNum, getAllUsersInAHouseCallback);
    }

    /**
     * Author: Aanuoluwapo Orioke
     */
    public boolean verifyUser(User user, String enteredCode) {
        if (user.getCode().equalsIgnoreCase(enteredCode)) {
            user.setActive(true);
            firebaseDB.collection("users").document("" + user.getPhoneNumber()).set(user);
            return true;
        }
        return false;
    }

    /**
     * Grace Tcheukounang
     */
    public void getUsers() {
        ArrayList<User> allUsers = new ArrayList<>();
        firebaseDB.collection("users")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        User u = d.toObject(User.class);
                        allUsers.add(u);
                    }
                } else {
                    Log.d("Getting Users", "No users found in database");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Operation Failed", "Fail to get the data");
            }
        });
    }

    /**
     * author Grace Tcheukounang
     */
    private void checkUserExist(String phoneNumber, String password,UserDatabase userDatabase, HandleAsync onlogin)
    {
        DocumentReference documentReference = firebaseDB.collection("users").document(phoneNumber);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        pNumber = document.getString("phoneNumber");
                        pwd =document.getString("password");
                        name = document.getString("name");
                        hNumber = document.getString("houseNumber");
                        sNumber = document.getString("streetNumber");
                        userType = document.getString("userType");
                        isActive = document.getBoolean("isActive");
                        userLocation = document.getString("city");

                        if(isActive == true && pNumber.equalsIgnoreCase(phoneNumber) && pwd.equalsIgnoreCase(password))
                        {
                            onlogin.loginProcess(true);
                            //store values in the room DB
                            UserEntity userEntity = new UserEntity(pNumber,name,pwd,sNumber,hNumber,userType,isActive, userLocation);
                            //clean DB at start
                            userDatabase.userDao().deleteAllUsers();
                            userEntity.setActive(isActive);
                            userDatabase.userDao().addUser(userEntity);
                        }
                        else{
                            onlogin.loginProcess(false);
                        }
                    }else{
                        onlogin.loginProcess(false);
                    }
                }
            }
        });
    }

    /**
     * author Grace Tcheukounang
     */
    public void loginUser(String phoneNumber, String password,UserDatabase userDatabase, HandleAsync onlogin) {
        checkUserExist(phoneNumber,password, userDatabase,onlogin);
    }


    /**
     * Author: Yves Byiringiro
     */
    private void checkPayment(Payment payment,Map<String, String> paymentEntity, HandleAsync onPayment)
    {
        firebaseDB.collection("payments")
                .whereEqualTo("confirmation", payment.getConfirmation().trim())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int resultSize  = task.getResult().size();
                            if (resultSize > 0){
                                onPayment.paymentProcess(false);
                            }
                            else {
                                onPayment.paymentProcess(true);
                                firebaseDB.collection("payments").add(paymentEntity);

                            }
                        } else {
                            onPayment.paymentProcess(false);
                        }
                    }
                });
    }

    /**
     * Author: Yves Byiringiro
     */
    public void submitPaymentReceipt(Payment payment, HandleAsync onPayment) {
        code = "";
        Map<String, String> paymentEntity = new HashMap<>();
        paymentEntity.put("phone_number", payment.getPhone_number().trim());
        paymentEntity.put("street_number", payment.getStreet_number().trim());
        paymentEntity.put("house_number", payment.getHouse_number().trim());
        paymentEntity.put("month", payment.getMonth().trim());
        paymentEntity.put("picture", payment.getPicture().trim());
        paymentEntity.put("confirmation", payment.getConfirmation().trim());

        checkPayment(payment,paymentEntity, onPayment);
    }


    /**
     * Author by Grace Tcheukounang
     */
    private void getUserPhoneNumbersInStreet(String streetNumber,AllPhoneNumbersInStreet phoneListCallback){
        firebaseDB.collection("users")
                .whereEqualTo("streetNumber", streetNumber.trim())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<String>phoneNumberList = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot d : task.getResult())
                            {
                                phoneNumberList.add(d.getString("phoneNumber"));
                            }
                            phoneListCallback.getAllPhoneNumbersInStreet(phoneNumberList);
                        } else {
                            //empty list here
                            phoneListCallback.getAllPhoneNumbersInStreet(phoneNumberList);
                        }
                    }
                });
    }
    /**
     * Author by Grace Tcheukounang
     * Getting the phone numbers of users in a street
     */
    public void allPhoneNumberInStreet(String streetNumber, AllPhoneNumbersInStreet phoneListCallback){
        getUserPhoneNumbersInStreet(streetNumber,phoneListCallback);
    }

    /**
     * Grace Tcheukounang
     */
    private void getUserPhoneNumbersInHouse(String streetNumber,String houseNumber, AllPhoneNumbersInHouse phoneListCallback){

        firebaseDB.collection("users")
                .whereEqualTo("streetNumber", streetNumber.trim())
                .whereEqualTo("houseNumber", houseNumber.trim())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<String>phoneNumberList = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot d : task.getResult())
                            {
                                phoneNumberList.add(d.getString("phoneNumber"));
                            }
                            phoneListCallback.getAllPhoneNumbersInHouse(phoneNumberList);
                        } else {
                            //empty list here
                            phoneListCallback.getAllPhoneNumbersInHouse(phoneNumberList);
                        }
                    }
                });

    }

    /**
     * Author by Grace Tcheukounang
     * Getting the phone numbers of users in a house
     */
    public void allPhoneNumberInHouse(String streetNumber,String houseNumber, AllPhoneNumbersInHouse phoneListCallback){
        getUserPhoneNumbersInHouse(streetNumber,houseNumber,phoneListCallback);
    }

    /**
     * Author by Grace Tcheukounang
     * Getting all streets
     */
    private void listStreet(GetAllStreets getAllStreetsCallback)
    {
        firebaseDB.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        HashSet<String> streetList = new HashSet<String>();
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot d : task.getResult())
                            {
                                streetList.add(d.getString("streetNumber"));
                            }
                            getAllStreetsCallback.getStreets(streetList);
                        } else {
                            //empty list here
                            getAllStreetsCallback.getStreets(streetList);
                        }
                    }
                });
    }


    /**
     * Author by Grace Tcheukounang
     * Getting all streets
     */

    public void getAllStreets(GetAllStreets getStreetsCallback)
    {
        listStreet(getStreetsCallback);
    }


    /*
    * Author: Aanuoluwapo Orioke
     */
    public void getAllHousesOnStreet(String streetNumber, GetAllHousesOnStreet getAllHousesOnStreet)
    {
        firebaseDB.collection("users")
                .whereEqualTo("streetNumber", streetNumber.trim())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        HashSet<String> houseList = new HashSet<String>();
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot d : task.getResult())
                            {
                                houseList.add(d.getString("houseNumber"));
                            }
                            getAllHousesOnStreet.getHouses(houseList);
                        } else {
                            //empty list here
                            getAllHousesOnStreet.getHouses(houseList);
                        }
                    }
                });
    }

    /*
     * Author: Aanuoluwapo Orioke
     */

    public void getAllHouses(String streetNumber, GetAllHousesOnStreet getAllHousesOnStreetCallBack)
    {
        getAllHousesOnStreet(streetNumber, getAllHousesOnStreetCallBack);
    }

    /**
     * Grace Tcheukounang
     */

    public void addNotification(Notification notification){
        firebaseDB.collection("notifications").add(notification);
    }


    /**
     * Grace Tcheukounang
     */
    private void getUserNotifications(String phoneNumber, GetNotificationCallback getNotificationCallback){
        ArrayList<Notification> allNotification = new ArrayList<>();
        firebaseDB.collection("notifications")
                .whereEqualTo("phoneNumber", phoneNumber.trim())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Notification n = d.toObject(Notification.class);
                                allNotification.add(n);
                            }
                            getNotificationCallback.getAllNotifications(allNotification);
                        } else {
                            Log.d("Getting Notification", "No notifications found in database");
                            getNotificationCallback.getAllNotifications(allNotification);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Operation Failed", "Fail to get the data");
                    }
                });

    }
    /**
     * Grace Tcheukounang
     */
    public void getNotifications(String phoneNumber, GetNotificationCallback getNotificationCallback){
        getUserNotifications(phoneNumber, getNotificationCallback);
    }


    /**
     * Author: Yves Byiringiro
     */
    private void getAllPaymentReceipts(GetReceipts getReceiptsCallBack){
        ArrayList<Payment> allReceipts = new ArrayList<>();
        firebaseDB.collection("payments")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Payment payment = d.toObject(Payment.class);
                                allReceipts.add(payment);
                            }
                            getReceiptsCallBack.getReceipts(allReceipts);
                        } else {
                            Log.d("Firebase payments", "No payments found in database");
                            getReceiptsCallBack.getReceipts(allReceipts);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("500", "server error");
                    }
                });

    }

    /**
     * Author: Yves Byiringiro
     */
    public void getPaymentReceipts(GetReceipts getReceiptsCallBack){
        getAllPaymentReceipts(getReceiptsCallBack);
    }
}