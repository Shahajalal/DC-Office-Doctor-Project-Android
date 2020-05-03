package com.future.it.park.doctorapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

	public static void setUserName (Context context, String email){
		getSharedPreferences(context).edit().putString("USERNAME", email).commit();
	}

	public static String getUserName(Context context) {
		return getSharedPreferences(context).getString("USERNAME", "");
	}

	public static void setFather (Context context, String email){
		getSharedPreferences(context).edit().putString("Father", email).commit();
	}

	public static String getFather(Context context) {
		return getSharedPreferences(context).getString("Father", "");
	}

	public static void setAge (Context context, String email){
		getSharedPreferences(context).edit().putString("AGE", email).commit();
	}

	public static String getAge (Context context) {
		return getSharedPreferences(context).getString("AGE", "");
	}

	public static void setMobile (Context context, String email){
		getSharedPreferences(context).edit().putString("Mobile", email).commit();
	}

	public static String getMobile (Context context) {
		return getSharedPreferences(context).getString("Mobile", "");
	}

	public static void setWeight (Context context, String email){
		getSharedPreferences(context).edit().putString("Weight", email).commit();
	}

	public static String setRogerBornona (Context context) {
		return getSharedPreferences(context).getString("RogerBornona", "");
	}

	public static void getRogerBornona (Context context, String email){
		getSharedPreferences(context).edit().putString("RogerBornona", email).commit();
	}

	public static String getWeight (Context context) {
		return getSharedPreferences(context).getString("Weight", "");
	}

	public static void setDoctorName (Context context, String email){
		getSharedPreferences(context).edit().putString("DoctortName", email).commit();
	}

	public static String getDoctorName (Context context) {
		return getSharedPreferences(context).getString("DoctortName", "");
	}

	public static void setDoctorPassword (Context context, String email){
		getSharedPreferences(context).edit().putString("DoctorPass", email).commit();
	}

	public static String getDoctorPassword (Context context) {
		return getSharedPreferences(context).getString("DoctorPass", "");
	}

	public static void setDoctorID (Context context, String email){
		getSharedPreferences(context).edit().putString("DoctorID", email).commit();
	}

	public static String getDoctorID (Context context) {
		return getSharedPreferences(context).getString("DoctorID", "");
	}

	public static void setAddress (Context context, String email){
		getSharedPreferences(context).edit().putString("setAddress", email).commit();
	}

	public static String getAddress (Context context) {
		return getSharedPreferences(context).getString("setAddress", "");
	}


	public static String getIP(Context context) {
		return getSharedPreferences(context).getString("getip", "http://doortodoorkhulna.com/hospital/api/");
		//return getSharedPreferences(context).getString("getip", "http://10.0.2.2:8000/api/");
	}

	public static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}



}