package com.commax.ocf.server.app.ServiceManager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

import com.commax.ocf.server.app.MyOCFServer;
import com.commax.ocf.server.app.OnServiceStateUpdateListener;

import static com.commax.ocf.server.app.Typedefine.MSG_QUERY_READY;
import static com.commax.ocf.server.app.Typedefine.MSG_REGISTER_CLIENT;

public class SwitchlightOneServiceManager implements OnServiceStateUpdateListener {

    MyOCFServer myOCFServer;

    Messenger serviceInput;

    InCommingHandler inCommingHandler;

    Messenger serviceOutput;

    private static boolean isBounded = false;

    private static final String SWITCH_LIGHT_ONE_SERVICE_PKG = "com.commax.ocf.service.switchlight";
    private static final String SWITCH_LIGHT_ONE_SERVICE_NAME = "com.commax.ocf.service.switchlight.SWITCHLightService";



    public SwitchlightOneServiceManager(MyOCFServer myOCFServer, InCommingHandler handler) {
        this.myOCFServer = myOCFServer;
        inCommingHandler = handler;
        inCommingHandler.setOnListener(this);
        serviceOutput=new Messenger(inCommingHandler);
    }

    public void startService(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName(SWITCH_LIGHT_ONE_SERVICE_PKG,SWITCH_LIGHT_ONE_SERVICE_NAME));
        myOCFServer.startService(intent);
    }

    public void stopService(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName(SWITCH_LIGHT_ONE_SERVICE_PKG,SWITCH_LIGHT_ONE_SERVICE_NAME));
        myOCFServer.stopService(intent);
    }

    public void bindService(){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(SWITCH_LIGHT_ONE_SERVICE_PKG,SWITCH_LIGHT_ONE_SERVICE_NAME));
        myOCFServer.bindService(intent, SwitchlightCon, myOCFServer.BIND_AUTO_CREATE);
    }

    public void unbindService(){
        if(isBounded) {
            isBounded=false;
            myOCFServer.unbindService(SwitchlightCon);
        }else{
            Toast.makeText(myOCFServer.mContext,"Switchlight is not binded,", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setServiceDisconnected(boolean status) {
        isBounded = status;
    }

    public boolean getBindStatus(){
        return isBounded;
    }

    public void checkServiceReady(){
        if(serviceInput!=null){
            Message msg = Message.obtain();
            msg.what =  MSG_QUERY_READY;
            msg.replyTo = serviceInput;

            try{
                serviceInput.send(msg);
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }
    }

    ServiceConnection SwitchlightCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceInput = new Messenger(service);

            isBounded= true;

            Message msg = Message.obtain();
            msg.what = MSG_REGISTER_CLIENT;
            msg.replyTo = serviceOutput;

            try {
                serviceInput.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceInput = null;
        }
    };

}
