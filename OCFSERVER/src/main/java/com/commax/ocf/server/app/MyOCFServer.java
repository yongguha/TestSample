package com.commax.ocf.server.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.commax.lib.ZigbeeData.DataManager;
import com.commax.lib.ZigbeeData.DeviceInfo;
import com.commax.ocf.server.app.ServiceManager.AirMonitorServiceManager;
import com.commax.ocf.server.app.ServiceManager.DoorLockServiceManager;
import com.commax.ocf.server.app.ServiceManager.GasLeakServiceManager;
import com.commax.ocf.server.app.ServiceManager.GaslockServiceManager;
import com.commax.ocf.server.app.ServiceManager.InCommingHandler;
import com.commax.ocf.server.app.ServiceManager.MagneticServiceManager;
import com.commax.ocf.server.app.ServiceManager.MainLightServiceManager;
import com.commax.ocf.server.app.ServiceManager.SmartCurtainServiceManager;
import com.commax.ocf.server.app.ServiceManager.SmartDimmerServiceManager;
import com.commax.ocf.server.app.ServiceManager.SmartPlugServiceManager;
import com.commax.ocf.server.app.ServiceManager.StandPlugServiceManager;
import com.commax.ocf.server.app.ServiceManager.SwitchlightOneServiceManager;
import com.commax.ocf.server.app.ServiceManager.SwitchlightTwoServiceManager;
import com.commax.ocf.server.app.ServiceManager.WaterleakServiceManager;
import com.commax.ocf.server.app.listview.MainListviewAdapter;
import com.commax.ocf.server.app.listview.MainListviewItem;
import com.commax.ocf.server.app.listview.SubMenuAdapter;
import com.commax.ocf.server.app.listview.SubMenuItem;
import com.commax.ocf.server.app.preference.DeviceStatusPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.commax.ocf.server.app.Typedefine.OCF_SERVER_SERVICE_NAME_AIRMONITOR;
import static com.commax.ocf.server.app.Typedefine.OCF_SERVER_SERVICE_NAME_DIMMINGLIGHT;
import static com.commax.ocf.server.app.Typedefine.OCF_SERVER_SERVICE_NAME_DOORLOCK;
import static com.commax.ocf.server.app.Typedefine.OCF_SERVER_SERVICE_NAME_MAGNETIC;
import static com.commax.ocf.server.app.Typedefine.OCF_SERVER_SERVICE_NAME_MAINLIGHT;
import static com.commax.ocf.server.app.Typedefine.OCF_SERVER_SERVICE_NAME_SMARTDIMMER;
import static com.commax.ocf.server.app.Typedefine.OCF_SERVER_SERVICE_NAME_SMARTPLUG;
import static com.commax.ocf.server.app.Typedefine.OCF_SERVER_SERVICE_NAME_STANDPLUG;
import static com.commax.ocf.server.app.Typedefine.OCF_SERVER_SERVICE_NAME_SWITCH_ONE;
import static com.commax.ocf.server.app.Typedefine.OCF_SERVER_SERVICE_NAME_SWITCH_TWO;

public class MyOCFServer extends AppCompatActivity {

    public static final int RESULT_OK           = -1;
    public static final int PASSCODE_PROVIDER_INDEX = 12;

    ArrayList<DeviceInfo> mCategory_eFavorite;
    ArrayList<DeviceInfo> mCategory_eLight;
    ArrayList<DeviceInfo> mCategory_eIndoorEnv;
    ArrayList<DeviceInfo> mCategory_eEnergy;
    ArrayList<DeviceInfo> mCategory_eSafe;

    public DataManager dataManager;  //Data Manager

    public Context mContext;

    private static final String PAM_SERVER_PACKAGE = "com.commax.pam";
    private static final String PAM_SERVER_ACTION = "com.commax.pam.action.REQUEST_CONTROL";

    ArrayList<MainListviewItem> MainListData;
    ArrayList<SubMenuItem> MainMenuData;

    ListView SubMenulistView;
    SubMenuAdapter SubMenuListAdatper;

    ListView MainMenulistview;
    MainListviewAdapter MainMenuAdapter;

    private static boolean isAllChecked =false;

    ArrayList<Class> ServiceCalss;


    SmartPlugServiceManager smartPlugServiceManager;
    DoorLockServiceManager doorLockServiceManager;
    MagneticServiceManager magneticServiceManager;
    SwitchlightOneServiceManager switchlightOneServiceManager;


    private int _mainlist_currentPosition = 0;
    private String _currentSelectedMainTitle = null;

    private int _sublist_currentPosition = 0;
    private String _currentSelectedSubTitle = null;

    MainListviewItem[] mainItems;
    SubMenuItem[] subMenuItem;

    public static MyOCFServer _instance;
    public static MyOCFServer getInstance(){
        return _instance;
    }

    DeviceStatusPreference mDevicePref;

    TextView subListStatus;

    InCommingHandler mIncommingHandler;

    TextView mConsoleTextView;

    TextView mSmartPlugtxt, mGasleaktxt, mLighttxt, mGaslocktxt, mSmartCurtaintxt, mSmartdimmertxt;
    TextView mWaterleaktxt, mDoorlocktxt;

    private static boolean isPasscheckPassed = false;

    ArrayList<Integer> checkedList;

    HashMap<String, Boolean> ServiceMap;
    HashMap<String, Boolean> runningServiceMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        _instance = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        getSupportActionBar().setTitle("OCF Servers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
        mDevicePref = new DeviceStatusPreference(this);

        mSmartPlugtxt = findViewById(R.id.dustpad);
        mGasleaktxt = findViewById(R.id.gasleak);
        mLighttxt = findViewById(R.id.light1);

        ServiceMap= new HashMap<>();
        runningServiceMap = new HashMap<>();

        initServiceMap();
        //RunDoinBackground();
    }

    private void launchPasscodeCheckActivity(){
        Intent intent = new Intent();
        intent.setClassName("com.commax.ocf.server.app", "com.commax.ocf.server.app.PasscodeCheckActivity");
        startActivityForResult(intent, PASSCODE_PROVIDER_INDEX);
    }



    @Override
    protected void onResume() {
        super.onResume();

        if(BuildOption.isUsePasschek && !isPasscheckPassed)
            launchPasscodeCheckActivity();
        else {

            initListData();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean bResult = false;
        if (requestCode == PASSCODE_PROVIDER_INDEX) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    int condition = data.getExtras().getInt(PasscodeCheckActivity.KEY, -1);
                    if (condition == Symbol.VALID) {
                        bResult = true;
                        isPasscheckPassed = true;
                        //return;
                    } else {
                        bResult = false;
                    }
                }
            }
        }

        if(bResult){
            //init_mainlistview();
        }else{
            finish();
        }

    }

    ActivityManager manager;
    Iterator<Map.Entry<String,Boolean>> itr = null;

    private void RunDoinBackground(){
        manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        Set<Map.Entry<String,Boolean>> set = ServiceMap.entrySet();
        itr = set.iterator();

        new ServiceRunningCheckNUiUpdate().execute(itr);
    }


    private class ServiceRunningCheckNUiUpdate extends AsyncTask<Iterator<Map.Entry<String,Boolean>>, Void, Void > {
        @Override
        protected Void doInBackground(Iterator<Map.Entry<String, Boolean>>... iterators) {
            while (itr.hasNext()){
                Map.Entry<String,Boolean> e = (HashMap.Entry<String, Boolean>)itr.next();

                for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
                    if(e.getKey().equals(service.service.getClassName())) {
                        ServiceMap.put(e.getKey(), true);
                        break;
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            int i=0;

            if(runningServiceMap.containsKey(OCF_SERVER_SERVICE_NAME_SMARTPLUG)) {
                MainListData.get(i).setExp("Register");
                i++;
            }
            else
                MainListData.get(i).setExp("Release");

            if(runningServiceMap.containsKey(OCF_SERVER_SERVICE_NAME_SWITCH_ONE)) {
                MainListData.get(i).setExp("등Register록");
                i++;
            }
            else
                MainListData.get(i).setExp("Release");

            if(runningServiceMap.containsKey(OCF_SERVER_SERVICE_NAME_MAGNETIC)) {
                MainListData.get(i).setExp("Register");
                i++;
            }
            else
                MainListData.get(i).setExp("Release");


            if(runningServiceMap.containsKey(OCF_SERVER_SERVICE_NAME_DOORLOCK)) {
                MainListData.get(i).setExp("Register");
                i++;
            }
            else
                MainListData.get(i).setExp("Release");



            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }



    public void isServiceRunningCheck(){
        manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        Set<Map.Entry<String,Boolean>> set = ServiceMap.entrySet();
        final Iterator<Map.Entry<String,Boolean>> itr = set.iterator();

        while (itr.hasNext()){
            Map.Entry<String,Boolean> e = (HashMap.Entry<String, Boolean>)itr.next();

            for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
                if(e.getKey().equals(service.service.getClassName())) {
                    runningServiceMap.put(e.getKey(), true);
                    break;
                }else
                    runningServiceMap.put(e.getKey(), false);
            }
        }

    }

    private void initServiceMap(){
        ServiceMap.put(OCF_SERVER_SERVICE_NAME_SMARTPLUG,false);
        ServiceMap.put(OCF_SERVER_SERVICE_NAME_SWITCH_ONE,false);
        ServiceMap.put(OCF_SERVER_SERVICE_NAME_MAGNETIC,false);
        ServiceMap.put(OCF_SERVER_SERVICE_NAME_DOORLOCK,false);


    }

    private void initListData(){

        isServiceRunningCheck();

        init_mainlistview();
        init_sublistview();


        mIncommingHandler = new InCommingHandler();
        switchlightOneServiceManager = new SwitchlightOneServiceManager(this, mIncommingHandler);
        smartPlugServiceManager = new SmartPlugServiceManager(this, mIncommingHandler);
        magneticServiceManager = new MagneticServiceManager(this,mIncommingHandler);
        doorLockServiceManager = new DoorLockServiceManager(this, mIncommingHandler);



        mConsoleTextView = (TextView)findViewById(R.id.consoleTextView);

    }


    @Override
    protected void onPause() {
        super.onPause();
        isPasscheckPassed = false;
    }

    public void setMainExpSetText(){
        for(Map.Entry<String, Boolean> element : runningServiceMap.entrySet()){

            if(element.getKey().equals(OCF_SERVER_SERVICE_NAME_SMARTPLUG)){
                if(element.getValue())
                    mainItems[0].setExp("Register");
                else
                    mainItems[0].setExp("Release");

            }
            else if(element.getKey().equals(OCF_SERVER_SERVICE_NAME_SWITCH_ONE)) {
                if (element.getValue())
                    mainItems[1].setExp("Register");
                else
                    mainItems[1].setExp("Release");

            }
            else if(element.getKey().equals(OCF_SERVER_SERVICE_NAME_MAGNETIC)){
                if(element.getValue())
                    mainItems[2].setExp("Register");
                else
                    mainItems[2].setExp("Release");

            }
            else if(element.getKey().equals(OCF_SERVER_SERVICE_NAME_DOORLOCK)){
                if(element.getValue())
                    mainItems[3].setExp("Register");
                else
                    mainItems[3].setExp("Release");

            }


        }
    }

    public void init_mainlistview(){

        MainMenulistview = (ListView)findViewById(R.id.listview);

        MainListData = new ArrayList<>();
        mainItems = new MainListviewItem[4];



        mainItems[0] = new MainListviewItem(R.drawable.img_light, "Light","");
        mainItems[1] = new MainListviewItem(R.drawable.img_smartplug, "SmartPlug","");
        mainItems[2] = new MainListviewItem(R.drawable.img_smartplug, "Window","");
        mainItems[3] = new MainListviewItem(R.drawable.img_smartplug, "DoorLock","");


        _currentSelectedMainTitle = "Light";

        setMainExpSetText();

        for(int i=0;i<mainItems.length;i++) {
            MainListData.add(mainItems[i]);
        }

        MainMenuAdapter = new MainListviewAdapter(this, R.layout.item_view_mainlist, MainListData);
        MainMenulistview.setAdapter(MainMenuAdapter);


        MainMenulistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                _mainlist_currentPosition=position;//현재 선택된 위치
                _currentSelectedMainTitle = mainItems[_mainlist_currentPosition].getTitle();

                MainMenuAdapter.setPositionSelected(_mainlist_currentPosition);


                /** * 메인리스트 변경 시, 메인 항목에 따라 서브리스트 위치도 변경시켜준다.   */
                if(SubMenuListAdatper!=null) {
                    SubMenuListAdatper.setPositionSelected(getSubListPostion(_mainlist_currentPosition));
                    SubMenuListAdatper.notifyDataSetChanged();
                }
            }
        });
        MainMenuAdapter.notifyDataSetChanged();

    }



    public void init_sublistview(){
        SubMenulistView = (ListView)findViewById(R.id.sublistview);
        MainMenuData = new ArrayList<>();

        subMenuItem = new SubMenuItem[2];

        subMenuItem[0] = new SubMenuItem("Register");
        subMenuItem[1] = new SubMenuItem("Release");


        for(int i=0;i<subMenuItem.length;i++)
            MainMenuData.add(subMenuItem[i]);


        SubMenuListAdatper = new SubMenuAdapter(this,R.layout.item_ocf_menu_title_view, MainMenuData);
        SubMenulistView.setAdapter(SubMenuListAdatper);


        if(mainItems[0].getExp().equals("Register"))
            SubMenuListAdatper.setPositionSelected(0);
        else
            SubMenuListAdatper.setPositionSelected(1);


        SubMenulistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("SmartPlug", "SubMenuListAdatper : onItemClick");
                _sublist_currentPosition = position;
                _currentSelectedSubTitle = subMenuItem[_sublist_currentPosition].getTitle();
                SubMenuListAdatper.setPositionSelected(_sublist_currentPosition);

                setMainExpText(_mainlist_currentPosition,_sublist_currentPosition);
                _OperateService(_mainlist_currentPosition, _sublist_currentPosition);


                MainMenuAdapter.notifyDataSetChanged();
                SubMenuListAdatper.notifyDataSetChanged();
                //TODO 실제 서비스 unbinding 부분 추가 필요
            }
        });
        MainMenuAdapter.notifyDataSetChanged();
        SubMenuListAdatper.notifyDataSetChanged();

    }

    public void setMainExpText(int mainPos,int subPos){
        if(mainPos==0) {
            if (subPos == 0) {
                mainItems[0].setExp("Register");
            }
            else {
                mainItems[0].setExp("Release");
            }
        }else if(mainPos==1) {
            if (subPos == 0) {
                mainItems[1].setExp("Register");
            }
            else {
                mainItems[1].setExp("Release");
            }
        }else if(mainPos==2) {
            if (subPos == 0) {
                mainItems[2].setExp("Register");
            }
            else {
                mainItems[2].setExp("Release");
            }
        }else if(mainPos==3) {
            if (subPos == 0) {
                mainItems[2].setExp("Register");
            }
            else {
                mainItems[2].setExp("Release");
            }
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_actions,menu);
        return true;
    }

    public int getSubListPostion(int mainPos){

        if(mainPos==0) {
            if(mainItems[0].getExp().equals("Register"))
                return 0;
            else
                return 1;
        }else if(mainPos==1) {
            if(mainItems[1].getExp().equals("Register"))
                return 0;
            else
                return 1;
        }else if(mainPos==2) {
            if(mainItems[2].getExp().equals("Register"))
                return 0;
            else
                return 1;
        }else if(mainPos==3) {
            if(mainItems[2].getExp().equals("Register"))
                return 0;
            else
                return 1;
        }

       else
            return 0;


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==android.R.id.home) {
            finish();
            return true;
        }else if(id==R.id.action_all_register) {
            checkedList = MainMenuAdapter.getChecked();

            for(int i=0;i<checkedList.size();i++){
                _OperateService(true,checkedList.get(i));

                if(mainItems[i].getExp().equals("Register"))
                    SubMenuListAdatper.setPositionSelected(0);
                else
                    SubMenuListAdatper.setPositionSelected(1);

            }

            // 전체 등록 했을 때, 서브메뉴 위치도 갱신하기 위해서

            MainMenuAdapter.notifyDataSetChanged();
            SubMenuListAdatper.notifyDataSetChanged();
            //_startAllServices();
        }else if(id==R.id.action_all_release) {
            //_stopAllServices();
            checkedList = MainMenuAdapter.getChecked();

            for(int i=0;i<checkedList.size();i++){
                _OperateService(false,checkedList.get(i));
                if(mainItems[i].getExp().equals("Register"))
                    SubMenuListAdatper.setPositionSelected(0);
                else
                    SubMenuListAdatper.setPositionSelected(1);
            }



            MainMenuAdapter.notifyDataSetChanged();
            SubMenuListAdatper.notifyDataSetChanged();

        }else if(id==R.id.all_check){
            if(isAllChecked) {
                MainMenuAdapter.setAllCheck(false);
                isAllChecked = false;
            }
            else {
                MainMenuAdapter.setAllCheck(true);
                isAllChecked = true;
            }
            MainMenuAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }


    public void _unbindAllServices(){
        smartPlugServiceManager.unbindService();
        doorLockServiceManager.unbindService();
    }


    public void _stopAllServices(){
        doorLockServiceManager.stopService();
        switchlightOneServiceManager.stopService();

    }

    public void _startAllServices(){
        doorLockServiceManager.startService();
        switchlightOneServiceManager.startService();
    }



    @Override
    protected void onDestroy() {

        mContext=null;

        if(MainListData!=null) {
            MainListData.clear();
            MainListData = null;
        }

        if(MainMenuData!=null) {
            MainMenuData.clear();
            MainMenuData = null;
        }

        MainMenuAdapter = null;
        SubMenuListAdatper = null;
        MainMenulistview = null;
        SubMenulistView = null;

        mDevicePref=null;

        dataManager = null;

        if(mCategory_eFavorite!=null) mCategory_eFavorite = null;
        if(mCategory_eLight!=null)mCategory_eLight = null;
        if(mCategory_eIndoorEnv!=null)mCategory_eIndoorEnv = null;
        if(mCategory_eEnergy!=null)mCategory_eEnergy = null;
        if(mCategory_eSafe!=null)mCategory_eSafe=null;


        if(mIncommingHandler!=null)
            mIncommingHandler = null;

        if(checkedList!=null)
            checkedList = null;

        if(runningServiceMap!=null){
            runningServiceMap.clear();
            runningServiceMap=null;
        }

        if(ServiceMap!=null){
            ServiceMap.clear();
            ServiceMap=null;
        }




        super.onDestroy();
    }



    @Override
    public void onBackPressed() {
/*
        if(smartPlugServiceManager!=null) {
            smartPlugServiceManager.unbindService();
            smartPlugServiceManager=null;
        }

        if(gaslockServiceManager!=null) {
            gaslockServiceManager.unbindService();
            gaslockServiceManager=null;
        }
        if(gasLeakServiceManager!=null) {
            gasLeakServiceManager.unbindService();
            gasLeakServiceManager=null;
        }
        if(doorLockServiceManager!=null) {
            doorLockServiceManager.unbindService();
            doorLockServiceManager=null;
        }
        if(smartCurtainServiceManager!=null) {
            smartCurtainServiceManager.unbindService();
            smartCurtainServiceManager=null;
        }
        if(smartDimmerServiceManager!=null) {
            smartDimmerServiceManager.unbindService();
            smartDimmerServiceManager=null;
        }
        if(magneticServiceManager!=null) {
            magneticServiceManager.unbindService();
            magneticServiceManager=null;
        }
        if(lightswitchServiceManager!=null){
            lightswitchServiceManager.unbindService();
            lightswitchServiceManager=null;
        }
        if(waterleakServiceManager!=null){
            waterleakServiceManager.unbindService();
            waterleakServiceManager=null;
        }



*/

        super.onBackPressed();
    }


    public void showConSoleText(String text){
        mConsoleTextView.setText(text);
    }

    public void setSmartPlugStausText(String text){
        mSmartPlugtxt.setVisibility(View.VISIBLE);
        mSmartPlugtxt.setText("");
        mSmartPlugtxt.setText(text);
    }

    public void setGasleakStatusText(String text){
        mGasleaktxt.setVisibility(View.VISIBLE);
        mGasleaktxt.setText("");
        mGasleaktxt.setText(text);
    }

    public void setLightStatusText(String text){
        mLighttxt.setVisibility(View.VISIBLE);
        mLighttxt.setText("");
        mLighttxt.setText(text);
    }

    public void setDoorlockStatusText(String text){
        mDoorlocktxt.setVisibility(View.VISIBLE);
        mDoorlocktxt.setText("");
        mDoorlocktxt.setText(text);
    }


    public void setGaslockStatusText(String text){
        mGaslocktxt.setVisibility(View.VISIBLE);
        mGaslocktxt.setText("");
        mGaslocktxt.setText(text);
    }

    public void setSmartCurtainStatusText(String text){
        mSmartCurtaintxt.setVisibility(View.VISIBLE);
        mSmartCurtaintxt.setText("");
        mSmartCurtaintxt.setText(text);
    }

    public void setSmartdimmerStatusText(String text){
        mSmartdimmertxt.setVisibility(View.VISIBLE);
        mSmartdimmertxt.setText("");
        mSmartdimmertxt.setText(text);
    }

    public void setWaterleakStatusText(String text){
        mWaterleaktxt.setVisibility(View.VISIBLE);
        mWaterleaktxt.setText("");
        mWaterleaktxt.setText(text);
    }

    public void _OperateService(boolean isStart, int i){

        if(isStart) {//startService
            if (i == 0)
                smartPlugServiceManager.startService();

            else if (i == 1)
                switchlightOneServiceManager.startService();

            else if (i == 2)
                magneticServiceManager.startService();

            else if (i == 3)

            setMainExpText(i,0);
        }else{//stopService
            if (i == 0)
                smartPlugServiceManager.stopService();
            else if (i == 1)
                switchlightOneServiceManager.stopService();
            else if (i == 2)
                magneticServiceManager.stopService();
            else if (i == 2)
                doorLockServiceManager.stopService();

            setMainExpText(i,1);
        }

    }

    public void _OperateService(int mainPos , int subPos) {
       if(mainPos==0) {
           if (subPos == 0) {
               smartPlugServiceManager.startService();
           }
           else {
               smartPlugServiceManager.stopService();
           }

        }if(mainPos==1) {
            if (subPos == 0) {
                switchlightOneServiceManager.startService();
            }
            else {
                switchlightOneServiceManager.stopService();
            }
        }if(mainPos==2) {
            if (subPos == 0) {
                magneticServiceManager.startService();
            }
            else {
                magneticServiceManager.stopService();
            }
        }if(mainPos==3) {
            if (subPos == 0) {
                doorLockServiceManager.startService();
            }
            else {
                doorLockServiceManager.stopService();
            }
        }


    }

}
