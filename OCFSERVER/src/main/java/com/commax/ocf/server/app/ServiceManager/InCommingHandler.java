package com.commax.ocf.server.app.ServiceManager;

import android.os.Handler;
import android.os.Message;

import com.commax.ocf.server.app.MyOCFServer;
import com.commax.ocf.server.app.OnServiceStateUpdateListener;

import static com.commax.ocf.server.app.Typedefine.MSG_QUERY_READY;
import static com.commax.ocf.server.app.Typedefine.MSG_REGISTER_CLIENT;
import static com.commax.ocf.server.app.Typedefine.MSG_UNREGISTER_CLIENT;

public class InCommingHandler extends Handler {



    OnServiceStateUpdateListener listener;

    public void setOnListener(OnServiceStateUpdateListener listener){
        this.listener = listener;
    }

    @Override
    public void handleMessage(Message msg) {
        final String who;
        final String registerText = "등록";
        final String unregisterText = "해지";
        switch (msg.what) {
            case MSG_REGISTER_CLIENT:
                who = msg.getData().getString("who");
                if(who.equals("Dustpad")) {
                    listener.setServiceDisconnected(true);

                    MyOCFServer.getInstance().setSmartPlugStausText("먼지패드 : "+registerText);
                }else if(who.equals("Gasleak")) {
                    listener.setServiceDisconnected(true);
                    MyOCFServer.getInstance().setGasleakStatusText("가스누출 : "+registerText);
                }else if(who.equals("Doorlock")) {
                    listener.setServiceDisconnected(true);
                    MyOCFServer.getInstance().setDoorlockStatusText("도어락 : " + registerText);
                }else if(who.equals("Smartcurtain")) {
                    listener.setServiceDisconnected(true);
                    MyOCFServer.getInstance().setSmartCurtainStatusText("스마트커튼 : " + registerText);
                }else if(who.equals("Smartdimmer")) {
                    listener.setServiceDisconnected(true);
                    MyOCFServer.getInstance().setSmartdimmerStatusText("스마트라이트 : " + registerText);
                }else if(who.equals("Waterleak")) {
                    listener.setServiceDisconnected(true);
                    MyOCFServer.getInstance().setWaterleakStatusText("누수센서 : " +registerText);
                }else if(who.equals("Light1")) {
                    listener.setServiceDisconnected(true);
                    MyOCFServer.getInstance().setLightStatusText("라이트1 : "+registerText);
                }else if(who.equals("Gaslock")) {
                    listener.setServiceDisconnected(true);
                    MyOCFServer.getInstance().setGaslockStatusText("가스락 : "+registerText);
                }else if(who.equals("Plug_stand_fan")) {
                    listener.setServiceDisconnected(true);
                    MyOCFServer.getInstance().setGaslockStatusText("스탠드_선풍기 : "+registerText);
                }

                break;

            case MSG_UNREGISTER_CLIENT:
                who = msg.getData().getString("who");
                if(who.equals("Dustpad")) {
                    listener.setServiceDisconnected(false);
                    MyOCFServer.getInstance().setSmartPlugStausText("먼지패드 : "+unregisterText);
                }else if(who.equals("Gasleak")) {
                    listener.setServiceDisconnected(false);
                    MyOCFServer.getInstance().setGasleakStatusText("가스누출 : "+unregisterText);
                }else if(who.equals("Doorlock")) {
                    listener.setServiceDisconnected(false);
                    MyOCFServer.getInstance().setDoorlockStatusText("도어락 : " + unregisterText);
                }else if(who.equals("Smartcurtain")) {
                    listener.setServiceDisconnected(false);
                    MyOCFServer.getInstance().setSmartCurtainStatusText("스마트커튼 : " + unregisterText);
                }else if(who.equals("Smartdimmer")) {
                    listener.setServiceDisconnected(false);
                    MyOCFServer.getInstance().setSmartdimmerStatusText("스마트라이트 : " + unregisterText);
                }else if(who.equals("Waterleak")) {
                    listener.setServiceDisconnected(false);
                    MyOCFServer.getInstance().setWaterleakStatusText("누수센서 : " +unregisterText);
                }else if(who.equals("Light1")) {
                    listener.setServiceDisconnected(false);
                    MyOCFServer.getInstance().setLightStatusText("라이트1 : "+unregisterText);
                }else if(who.equals("Gaslock")) {
                    listener.setServiceDisconnected(false);
                    MyOCFServer.getInstance().setGaslockStatusText("가스락 : "+unregisterText);
                }else if(who.equals("Plug_stand_fan")) {
                    listener.setServiceDisconnected(false);
                    MyOCFServer.getInstance().setGaslockStatusText("스탠드_선풍기 : "+registerText);
                }
                break;

            case MSG_QUERY_READY:
                MyOCFServer.getInstance().showConSoleText("Service is already Ready");
                break;

        }
    }
}