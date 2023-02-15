package com.kgltrash.DBLayout;

/**
 * author Grace Tcheukounang
 */
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey()
    @ColumnInfo(name = "phone_number")
    @NonNull
    private String phoneNumber;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "password")
    private String password;
    @ColumnInfo(name = "street_number")
    private String streetNumber;
    @ColumnInfo(name = "house_number")
    private String houseNumber;
    @ColumnInfo(name = "user_type")
    private String userType;
    @ColumnInfo(name = "is_active")
    private boolean isActive;
    @ColumnInfo(name = "user_location")
    private String city;

    public UserEntity() {}

    public UserEntity(@NonNull String phoneNumber, String name, String password, String streetNumber, String houseNumber, String userType, boolean isActive, String userLocation) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.password = password;
        this.streetNumber = streetNumber;
        this.houseNumber = houseNumber;
        this.userType = userType;
        this.isActive = isActive;
        this.city = userLocation;
    }
//    public UserEntity(String _phoneNumber, String _name, String _password,
//                      String _streetNumber, String _houseNumber, String _userType, boolean _isActive, String _userLocation) {
//        phoneNumber = _phoneNumber;
//        name = _name;
//        password = _password;
//        streetNumber = _streetNumber;
//        houseNumber = _houseNumber;
//        userType = _userType;
//        isActive = _isActive;
//        userLocation = _userLocation;
//    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String _phoneNumber) {
        phoneNumber = _phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        name = _name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String _password) {
        password = _password;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String _streetNumber) {
        streetNumber = _streetNumber;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String _houseNumber) {
        houseNumber = _houseNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String _userType) {
        userType = _userType;
    }

//    public boolean getIsActive() {
//        return isActive;
//    }

    public void setActive(boolean _isActive) {isActive = isActive;}

    public boolean isActive() { return isActive; }

    public String getCity() { return city;}

    public void setCity(String userLocation) { this.city = userLocation;}

    public String toString(){
        return "user has name " + name + "and number " + phoneNumber;
    }
}
