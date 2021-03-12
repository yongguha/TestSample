package com.commax.ocf.server.app.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class DeviceStatusPreference {

    Context mContext=null;

    public DeviceStatusPreference(Context context) {
        mContext=context;
    }

    public void setPreference(String deviceName, int value){

        SharedPreferences pref = mContext.getSharedPreferences("OCFServerDeviceStatus", 0);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(deviceName, value);
        editor.commit();
    }

    public int getSharedPreference(String deviceName){
        int ret=0;

        SharedPreferences pref = mContext.getSharedPreferences("OCFServerDeviceStatus",0);
        ret = pref.getInt(deviceName, 0);
        return ret;
    }


}
