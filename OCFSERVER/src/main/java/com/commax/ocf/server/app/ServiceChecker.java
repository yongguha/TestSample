package com.commax.ocf.server.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.HashMap;

public class ServiceChecker {

    Context mContext;
    Activity mActivity;

    HashMap<String, Boolean> runnServiceMap;

    public ServiceChecker(Activity activity, Context context, HashMap<String, Boolean> mMap) {
        mContext = context;
        mActivity = activity;
        runnServiceMap = mMap;
    }



    public boolean isServiceRunningCheck(String ServiceName){
        ActivityManager manager = (ActivityManager)mContext.getSystemService(Activity.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(ServiceName.equals(service.service.getClassName())) {
                runnServiceMap.put(ServiceName, true);
                return true;
            }else{
                runnServiceMap.put(ServiceName, false);
            }
        }
        return  false;
    }
}
