package com.commax.ocf.server.app.ServiceManager;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;

import com.commax.ocf.server.app.MyOCFServer;
import com.commax.ocf.server.app.OnServiceStateUpdateListener;

public abstract class CommonServiceManager implements ServiceConnection,OnServiceStateUpdateListener{

    MyOCFServer myOCFServer;
    Messenger serviceInput;
    InCommingHandler inCommingHandler;
    Messenger serviceOutput;



    public CommonServiceManager(MyOCFServer myOCFServer, Messenger serviceInput, InCommingHandler inCommingHandler, Messenger serviceOutput) {
        this.myOCFServer = myOCFServer;
        this.serviceInput = serviceInput;
        this.inCommingHandler = inCommingHandler;
        this.serviceOutput = serviceOutput;
    }

    public abstract void bindService();

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    public void onBindingDied(ComponentName name) {

    }

    @Override
    public void setServiceDisconnected(boolean status) {

    }
}
