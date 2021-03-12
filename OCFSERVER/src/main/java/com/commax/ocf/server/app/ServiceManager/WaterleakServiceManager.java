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

import static com.commax.ocf.server.app.Typedefine.MSG_REGISTER_CLIENT;

public class WaterleakServiceManager implements OnServiceStateUpdateListener {

    MyOCFServer myOCFServer;
    Messenger serviceInput;
    InCommingHandler inCommingHandler;
    Messenger serviceOutput;
    private static boolean isBounded = false;


    public WaterleakServiceManager(MyOCFServer myOCFServer, InCommingHandler handler) {
        this.myOCFServer = myOCFServer;
        inCommingHandler = handler;
        inCommingHandler.setOnListener(this);
        serviceOutput=new Messenger(inCommingHandler);
    }

    public void bindService(){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.commax.ocf.waterleak","com.commax.ocf.waterleak.WaterleakService"));
        myOCFServer.bindService(intent, WaterleakCon, myOCFServer.BIND_AUTO_CREATE);
    }


    public void unbindService(){
        if(isBounded) {
            isBounded=false;
            myOCFServer.unbindService(WaterleakCon);
        }else{
            Toast.makeText(myOCFServer.mContext,"Waterleak is not binded,", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setServiceDisconnected(boolean status) {
        isBounded = status;
    }

    public boolean getBindStatus(){
        return isBounded;
    }

    ServiceConnection WaterleakCon = new ServiceConnection() {
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
