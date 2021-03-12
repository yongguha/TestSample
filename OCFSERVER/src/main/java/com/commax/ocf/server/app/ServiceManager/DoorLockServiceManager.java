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
import com.commax.ocf.server.app.Typedefine;

import static com.commax.ocf.server.app.Typedefine.MSG_REGISTER_CLIENT;

public class DoorLockServiceManager implements OnServiceStateUpdateListener {

    MyOCFServer myOCFServer;
    Messenger serviceInput;
    InCommingHandler inCommingHandler;
    Messenger serviceOutput;

    private static boolean isBounded = false;

    private static final String DOORLOCK_SERVICE_PKG = "com.commax.ocf.doorlock";
    private static final String DOORLOCK_SERVICE_NAME = "com.commax.ocf.doorlock.DoorlockService";

    public DoorLockServiceManager(MyOCFServer myOCFServer, InCommingHandler handler) {
        this.myOCFServer = myOCFServer;
        inCommingHandler = handler;
        inCommingHandler.setOnListener(this);
        serviceOutput=new Messenger(inCommingHandler);

    }



    public void bindService(){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(DOORLOCK_SERVICE_PKG,DOORLOCK_SERVICE_NAME));
        myOCFServer.bindService(intent, DoorlockCon, myOCFServer.BIND_AUTO_CREATE);
    }


    public void unbindService(){
        if(isBounded) {
            isBounded=false;
            myOCFServer.unbindService(DoorlockCon);
        }else{
            Toast.makeText(myOCFServer.mContext,"DoorLock is not binded,", Toast.LENGTH_LONG).show();
        }
    }

    public void startService(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName(DOORLOCK_SERVICE_PKG,DOORLOCK_SERVICE_NAME));
        myOCFServer.startService(intent);
    }

    public void stopService(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName(DOORLOCK_SERVICE_PKG,DOORLOCK_SERVICE_NAME));
        myOCFServer.stopService(intent);
    }


    @Override
    public void setServiceDisconnected(boolean status) {
        isBounded = status;
    }

    public boolean getBindStatus(){
        return isBounded;
    }

    ServiceConnection DoorlockCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceInput = new Messenger(service);

            isBounded = true;
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