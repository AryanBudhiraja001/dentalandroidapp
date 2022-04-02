package com.arumdental.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.arumdental.authentication.signUp.model.response.SignUpResponse;
import com.arumdental.base.BaseApplication;
import com.arumdental.profile.updateProfile.model.Billing;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.http.POST;

public class UserSharedPrefences {
    private String TOKEN="token";
    private  String ISLOGIN="islogin";
    private  String ID="id";
    private  String POSITON="position";
    private  String EMAIL="email";
    private  String NAME="name";
    private  String USERNAME="username";
    private  String PHONE="phone";
    private   String COUNTRYCODE="countryCode";
    private  String OFITEMS="ofitems";
    private  String PRICE="price";
    private  String ISADDRESSSELECTED="isAddressSeleted";



    SharedPreferences sharedPreferences;
    static UserSharedPrefences userSharedPrefernce = null;
    Context context;

    public UserSharedPrefences() {

    }

    public UserSharedPrefences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

    }

    public static UserSharedPrefences

    getInstance() {
        if (userSharedPrefernce == null) {
            userSharedPrefernce = new UserSharedPrefences(BaseApplication.Companion.getInstance().getContext());
        }
        return userSharedPrefernce;
    }


    public void setTOKEN(String token) {
        sharedPreferences.edit().putString(TOKEN,token).commit();
    }

    public String getTOKEN() {
        return sharedPreferences.getString(TOKEN, "");
    }

    public void setISLOGIN(String islogin) {
        sharedPreferences.edit().putString(ISLOGIN,islogin).commit();
    }

    public String getISLOGIN() {
        return sharedPreferences.getString(ISLOGIN, "no");
    }

    public void setID( int id) {
        sharedPreferences.edit().putInt(ID,id).commit();
    }

    public int getID() {
        return sharedPreferences.getInt(ID,0);
    }

    public void setPOSITON( int id) {
        sharedPreferences.edit().putInt(POSITON,id).commit();
    }

    public int getPOSITON() {
        return sharedPreferences.getInt(POSITON,0);
    }

    public void setEMAIL(String email) {
        sharedPreferences.edit().putString(EMAIL,email).commit();
    }

    public String getEMAIL() {
        return sharedPreferences.getString(EMAIL, "");
    }


    public void setNAME(String name) {
        sharedPreferences.edit().putString(NAME,name).commit();
    }

    public String getNAME() {
        return sharedPreferences.getString(NAME, "");
    }

    public void setUSERNAME(String username) {
        sharedPreferences.edit().putString(USERNAME,username).commit();
    }

    public String getUSERNAME() {
        return sharedPreferences.getString(USERNAME, "");
    }

    public void saveBillingModel(Billing billingModel){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(billingModel);
        editor.putString("billingModel", json);
        editor.apply();

    }

    public Billing getBillingModel( ){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("billingModel", null);
        Type type = new TypeToken<Billing>() {}.getType();
        return gson.fromJson(json, type);
    }


    public  void saveAddresslist(ArrayList<Billing> addresslist)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(addresslist);
        editor.putString("addresslist", json);
        editor.apply();
    }

    public  ArrayList<Billing> getAddresslist()
    {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("addresslist", null);
        Type type = new TypeToken< ArrayList<Billing>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void setPHONE(String phone) {
        sharedPreferences.edit().putString(PHONE,phone).commit();
    }

    public String getPHONE() {
        return sharedPreferences.getString(PHONE, "");
    }

    public  void Logout()
    {
        sharedPreferences.edit().clear();
    }
    public void setCOUNTRYCODE(String countrycode) {
        sharedPreferences.edit().putString(COUNTRYCODE,countrycode).commit();
    }
    public String getCOUNTRYCODE() {
        return sharedPreferences.getString(COUNTRYCODE, "");
    }

    public void setOFITEMS(String ofitems) {
        sharedPreferences.edit().putString(OFITEMS,ofitems).commit();
    }
    public String getOFITEMS() {
        return sharedPreferences.getString(OFITEMS, "");
    }

    public void setPRICE(String price) {
        sharedPreferences.edit().putString(PRICE,price).commit();
    }
    public String getPRICE() {
        return sharedPreferences.getString(PRICE, "");
    }

    public void setISADDRESSSELECTED(Boolean setISADDRESSSELECTED) {
        sharedPreferences.edit().putBoolean(ISADDRESSSELECTED,setISADDRESSSELECTED).commit();
    }
    public boolean getISADDRESSSELECTED() {
        return sharedPreferences.getBoolean(ISADDRESSSELECTED, false);
    }



}
