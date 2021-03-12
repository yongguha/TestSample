package com.commax.ocf.server.app.recyclerview;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.commax.lib.Common.TypeDef;
import com.commax.lib.ZigbeeData.DataManager;
import com.commax.lib.ZigbeeData.DeviceInfo;
import com.commax.ocf.server.app.R;
import com.commax.ocf.server.app.recyclerview.MyRecyclerAdapter;
import com.commax.ocf.server.app.recyclerview.RecyclerViewDecoration;
import com.commax.ocf.server.app.recyclerview.Recycler_item;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<DeviceInfo> mCategory_eFavorite;
    ArrayList<DeviceInfo> mCategory_eLight;
    ArrayList<DeviceInfo> mCategory_eIndoorEnv;
    ArrayList<DeviceInfo> mCategory_eEnergy;
    ArrayList<DeviceInfo> mCategory_eSafe;

    public DataManager dataManager;  //Data Manager

    public Context mContext;

    private static final String PAM_SERVER_PACKAGE = "com.commax.pam";
    private static final String PAM_SERVER_ACTION = "com.commax.pam.action.REQUEST_CONTROL";
    //public IAdapter mRemotePamService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        data_init(mContext);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new RecyclerViewDecoration(mContext,36));



        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        List<Recycler_item> items =new ArrayList<>();

        Recycler_item[] item = new Recycler_item[5];

        item[0] = new Recycler_item(R.drawable.img_smartplug, "smartplug");
        item[1] = new Recycler_item(R.drawable.img_window, "window");
        item[2] = new Recycler_item(R.drawable.img_doorlock, "doorlock");
        item[3] = new Recycler_item(R.drawable.img_curtain, "curtain");
        item[4] = new Recycler_item(R.drawable.img_gaslock, "gaslock");

        for(int i=0;i<5;i++)
            items.add(item[i]);

        MyRecyclerAdapter myRecyclerAdapter = new MyRecyclerAdapter(getApplicationContext(), items, R.layout.activity_main);

        recyclerView.setAdapter(myRecyclerAdapter);

        myRecyclerAdapter.setIemClick(new MyRecyclerAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {

            }
        });
    }

    private void start_IAdapter() {
        Intent serviceIntent = new Intent(PAM_SERVER_ACTION);
        serviceIntent.setPackage(PAM_SERVER_PACKAGE);
        boolean is =  bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
           // mRemotePamService = IAdapter.Stub.asInterface(iBinder);
           // pamControl = new PAMControl(mRemotePamService);

            Log.e("SmartPlugServer","onServiceConnected() : " + componentName);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //mRemotePamService = null;

        }
    };


    public void data_init(Context context){
        mCategory_eLight = new ArrayList<DeviceInfo>();
        mCategory_eIndoorEnv = new ArrayList<DeviceInfo>();
        mCategory_eEnergy = new ArrayList<DeviceInfo>();
        mCategory_eSafe = new ArrayList<DeviceInfo>();

        dataManager = new DataManager(mContext);

        start_IAdapter();
        ProcessReadDB();
    }

    private void ProcessReadDB(){
        new ReadDBFromPAMDB().execute();
    }

    private class ReadDBFromPAMDB extends AsyncTask<Void, String, DeviceInfo> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                mCategory_eEnergy.clear();
                mCategory_eIndoorEnv.clear();
                mCategory_eSafe.clear();
                mCategory_eLight.clear();

            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        @Override
        protected DeviceInfo doInBackground(Void... voids) {
            try {

                for(int i=2;i<6;i++){
                    readDevices(i);
                }

            }catch (Exception e){}

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(DeviceInfo s) {
            super.onPostExecute(s);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private void readDevices(int category_number) {
        // Note: DB 액세스는 꼭 thread를 이용해야 한다.


        try {
            ArrayList<DeviceInfo> readDeviceList;
            TypeDef.CategoryType categorytype;
            DeviceInfo virtual_device;
            int detectcounter = 0;

            switch (category_number) {

                case TypeDef.TAB_FAV : // eFavorite
                    categorytype = TypeDef.CategoryType.eFavorite;
                    // TODO: 2016-08-02
                    break;

                case TypeDef.TAB_LIGHR : //eLight Scan
                    categorytype = TypeDef.CategoryType.eLight;
                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_SWITCH, TypeDef.COMMAX_DEVICE_MAINLIGHT, categorytype);
                    mCategory_eLight.addAll(readDeviceList);
                    readDeviceList.clear();
                    //MLog.d(" --> readAllDevices() -> size  " + mCategory_eLight.size());

                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_SWITCH, TypeDef.COMMAX_DEVICE_LIGHT, categorytype);
                    detectcounter = readDeviceList.size();
                    mCategory_eLight.addAll(readDeviceList);
                    readDeviceList.clear();
                    //MLog.d(" --> readAllDevices() -> size  " + mCategory_eLight.size());

                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_DIMMER, TypeDef.COMMAX_DEVICE_LIGHT, categorytype);
                    detectcounter += readDeviceList.size();
                    mCategory_eLight.addAll(readDeviceList);
                    readDeviceList.clear();
                    //MLog.d(" --> readAllDevices() -> size  " + mCategory_eLight.size());

                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_DIMMER, TypeDef.COMMAX_DEVICE_DETECTDIMMER, categorytype);
                    detectcounter += readDeviceList.size();
                    mCategory_eLight.addAll(readDeviceList);
                    readDeviceList.clear();


                    break;

                case TypeDef.TAB_INDOOR : //eIndoorEnv Scan
                    categorytype = TypeDef.CategoryType.eIndoorEnv;

                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_THERMOSTAT, TypeDef.COMMAX_DEVICE_FCU, categorytype);
                    detectcounter = readDeviceList.size();
                    mCategory_eIndoorEnv.addAll(readDeviceList);
                    readDeviceList.clear();
                    //MLog.d(   " --> readAllDevices() -> size  " + mCategory_eIndoorEnv.size());


                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_THERMOSTAT, TypeDef.COMMAX_DEVICE_BOILER, categorytype);
                    detectcounter = readDeviceList.size();
                    mCategory_eIndoorEnv.addAll(readDeviceList);
                    readDeviceList.clear();
                    //MLog.d(" --> readAllDevices() -> size  " + mCategory_eIndoorEnv.size());

                    //2017-11-27,yslee::스마트보일러 추가
                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_SWITCH, TypeDef.COMMAX_DEVICE_BOILER, categorytype);
                    detectcounter = readDeviceList.size();
                    mCategory_eIndoorEnv.addAll(readDeviceList);
                    readDeviceList.clear();

                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_THERMOSTAT, TypeDef.COMMAX_DEVICE_AIRCON, categorytype);
                    detectcounter = readDeviceList.size();
                    mCategory_eIndoorEnv.addAll(readDeviceList);
                    readDeviceList.clear();
                    //MLog.d(" --> readAllDevices() -> size  " + mCategory_eIndoorEnv.size());


                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_SWITCH, TypeDef.COMMAX_DEVICE_FAN, categorytype);
                    detectcounter = readDeviceList.size();
                    mCategory_eIndoorEnv.addAll(readDeviceList);
                    readDeviceList.clear();
                    //MLog.d(" --> readAllDevices() -> size  " + mCategory_eIndoorEnv.size());



                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_SWITCH, TypeDef.COMMAX_DEVICE_CURTAIN, categorytype);
                    detectcounter = readDeviceList.size();
                    mCategory_eIndoorEnv.addAll(readDeviceList);
                    readDeviceList.clear();
                    //MLog.d(" --> readAllDevices() -> size  " + mCategory_eIndoorEnv.size());

                    //2017-11-27,yslee::스마트커튼 추가
                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_DIMMER, TypeDef.COMMAX_DEVICE_CURTAIN, categorytype);
                    detectcounter = readDeviceList.size();
                    mCategory_eIndoorEnv.addAll(readDeviceList);
                    readDeviceList.clear();



                    //MultiSensor
                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_DSENSOR, TypeDef.COMMAX_DEVICE_DETECTSENSOR, categorytype);
                    mCategory_eIndoorEnv.addAll(readDeviceList);
                    readDeviceList.clear();

                    //FloodSensor
                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_DSENSOR, TypeDef.COMMAX_DEVICE_WATERSENSOR, categorytype);
                    mCategory_eIndoorEnv.addAll(readDeviceList);
                    readDeviceList.clear();



                    //Air Quality
                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_DSENSOR, TypeDef.COMMAX_DEVICE_AIR_QUALITY, categorytype);
                    mCategory_eIndoorEnv.addAll(readDeviceList);
                    readDeviceList.clear();

                    //Zigbee Repeater
                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_SWITCH, TypeDef.COMMAX_DEVICE_ZIGBEE_REPEATER, categorytype);
                    mCategory_eIndoorEnv.addAll(readDeviceList);
                    readDeviceList.clear();


                    // Temperature, Humidity
                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_MSENSOR, TypeDef.COMMAX_DEVICE_TEMPHUMIDITY, categorytype);
                    mCategory_eIndoorEnv.addAll(readDeviceList);
                    readDeviceList.clear();

                    break;

                case TypeDef.TAB_ENERGY : //eEnergy Scan
                    categorytype = TypeDef.CategoryType.eEnergy;
                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_SWITCH, TypeDef.COMMAX_DEVICE_PLUG, categorytype);
                    mCategory_eEnergy.addAll(readDeviceList);
                    readDeviceList.clear();

                    //2017-11-30,yslee::스마트플러그IR 추가
                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_SWITCH, TypeDef.COMMAX_DEVICE_PLUGIR, categorytype);
                    mCategory_eEnergy.addAll(readDeviceList);
                    readDeviceList.clear();
                    //Log.d(TAG, " --> readAllDevices() -> size  " + mCategory_eIndoorEnv.size());


                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_SWITCH, TypeDef.COMMAX_DEVICE_STANDBYPOWER, categorytype);
                    detectcounter = readDeviceList.size();
                    mCategory_eEnergy.addAll(readDeviceList);
                    readDeviceList.clear();
                    //MLog.d(" --> readAllDevices() -> size  " + mCategory_eIndoorEnv.size());


                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_METER, TypeDef.COMMAX_DEVICE_METER, categorytype);
                    mCategory_eEnergy.addAll(readDeviceList);
                    readDeviceList.clear();

                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_SWITCH, TypeDef.COMMAX_DEVICE_WALL_PLUG, categorytype);
                    mCategory_eEnergy.addAll(readDeviceList);
                    readDeviceList.clear();

                    //MLog.d(" --> readAllDevices() -> size  " + mCategory_eIndoorEnv.size());
                    break;

                case TypeDef.TAB_SAFE : //eSafe Scan
                    if(!TypeDef.TAB_SAFE_ENABLE) break; //optional : 디바이스 검색안함

                    categorytype = TypeDef.CategoryType.eSafe;

                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_LOCK, TypeDef.COMMAX_DEVICE_GASLOCK, categorytype);
                    mCategory_eSafe.addAll(readDeviceList);
                    readDeviceList.clear();
                    //MLog.d(" --> readAllDevices() -> size  " + mCategory_eIndoorEnv.size());

                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_LOCK, TypeDef.COMMAX_DEVICE_DOORLOCK, categorytype); //todo : check ?꾩슂
                    mCategory_eSafe.addAll(readDeviceList);
                    readDeviceList.clear();
                    //MLog.d(" --> readAllDevices() -> size  " + mCategory_eIndoorEnv.size());

                    //가스 감지기
                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_DSENSOR, TypeDef.COMMAX_DEVICE_GASSENSOR, categorytype);
                    mCategory_eSafe.addAll(readDeviceList);
                    readDeviceList.clear();
                    //MLog.d(" --> readAllDevices() -> size  " + mCategory_eIndoorEnv.size());

                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_DSENSOR, TypeDef.COMMAX_DEVICE_FIRESENSOR, categorytype);
                    mCategory_eSafe.addAll(readDeviceList);
                    readDeviceList.clear();
                    //MLog.d(" --> readAllDevices() -> size  " + mCategory_eIndoorEnv.size());

                    //접점 센서 -> 일반적인 감지 미감지 카드
                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_DSENSOR, TypeDef.COMMAX_DEVICE_CONTACTSENSOR, categorytype);
                    mCategory_eSafe.addAll(readDeviceList);
                    readDeviceList.clear();
                    //MLog.d(" --> readAllDevices() -> size  " + mCategory_eIndoorEnv.size());

                    //Magnetic Sensor
                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_DSENSOR, TypeDef.COMMAX_DEVICE_DOORSENSOR, categorytype);
                    mCategory_eSafe.addAll(readDeviceList);
                    readDeviceList.clear();

                    //Motion Sensor //2017-09-20,yslee::모션센서 추가함
                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_DSENSOR, TypeDef.COMMAX_DEVICE_MOTIONSENSOR, categorytype);
                    mCategory_eSafe.addAll(readDeviceList);
                    readDeviceList.clear();

                    //Fire Sensor //2017-09-20,yslee::화재센서 추가함
                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_DSENSOR, TypeDef.COMMAX_DEVICE_FIRESENSOR, categorytype);
                    mCategory_eSafe.addAll(readDeviceList);
                    readDeviceList.clear();

                    //Smoke Sensor //2017-09-20,yslee::연기센서 추가함
                    readDeviceList = dataManager.getSelSubDeviceInfoList(TypeDef.ROOT_DEVICE_DSENSOR, TypeDef.COMMAX_DEVICE_SMOKESENSOR, categorytype);
                    mCategory_eSafe.addAll(readDeviceList);
                    readDeviceList.clear();


                    break;
                default:
                    categorytype = TypeDef.CategoryType.eNone;
                    break;
            }

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
}
