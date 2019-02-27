package com.dyin.findyourpl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class LocationFragment extends Fragment implements View.OnClickListener{

    private MapView mMapView = null;
    private UiSettings mUisettings;
    BaiduMap mbaiduMap;
    private  Button infoButton;
    private int numOfAll = 25;
    private int numOfGreen = 0;
    private int numOfRed = 0;
    private int numOfYellow = 0;
    private int numOfGray = 25;
    private String showNum = String.valueOf(numOfAll);
    private boolean selectFlag = false;
    private int selectId=0;
    private boolean selectFinish = false;
    private boolean arriveNow = false;
    private boolean parking = false;
    private boolean leaveNow = false;
    private int selectFlagInt=0;
    private int parkingState;

    public LocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_location, container, false);
        Button bdButton = (Button) layout.findViewById(R.id.bdButton);
        bdButton.setOnClickListener(this);

        mMapView = (MapView) layout.findViewById(R.id.bdMap2);
        mbaiduMap = mMapView.getMap();
        mUisettings = mbaiduMap.getUiSettings();
        mUisettings.setCompassEnabled(true);
        mUisettings.setRotateGesturesEnabled(true);
        mUisettings.setOverlookingGesturesEnabled(true);
        LatLng point = new LatLng(31.291102,121.221732);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        OverlayOptions options = new MarkerOptions().position(point).icon(bitmap)
                .title("5")
                .animateType(MarkerOptions.MarkerAnimateType.grow)
                .draggable(false)
                .visible(false);
        mbaiduMap.addOverlay(options);

        mbaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

        showNum = String.valueOf(numOfGray);

        infoButton = new Button(getActivity());

        infoButton.setBackgroundResource(R.drawable.icon_gcoding);
        infoButton.setId(R.id.markerId);
        infoButton.setText(showNum);
        infoButton.setHeight(250);
        infoButton.setWidth(200);
        infoButton.setTextSize(25);
        infoButton.setTextColor(getResources().getColor(R.color.white));
        InfoWindow infoWindow = new InfoWindow(infoButton,point,-0);
        mbaiduMap.showInfoWindow(infoWindow);

        infoButton.setOnClickListener(this);

        LatLng center =new LatLng(31.291102,121.221732);
        MapStatus mapStatus = new MapStatus.Builder().target(center).zoom(16).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mbaiduMap.setMapStatus(mapStatusUpdate);
        running(layout);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onPause();

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.bdButton)
        {
        LatLng center =new LatLng(31.291102,121.221732);
        MapStatus mapStatus = new MapStatus.Builder().target(center).zoom(17).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mbaiduMap.setMapStatus(mapStatusUpdate);


        }
        else if(view.getId()==R.id.markerId)
        {
            Intent intent = new Intent(getActivity(),MapViewActivity.class);
            intent.putExtra("selectFlag",selectFlag);
            intent.putExtra("selectId",selectId);
            intent.putExtra("parkingState",parkingState);
            startActivityForResult(intent,31);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        numOfGreen=data.getExtras().getInt("numOfGreen");
        numOfRed = data.getExtras().getInt("numOfRed");
        numOfYellow = data.getExtras().getInt("numOfYellow");
        numOfGray = data.getExtras().getInt("numOfGray");
        selectFlag = data.getExtras().getBoolean("selectFlag");
        if(selectFlag){
            selectId=data.getExtras().getInt("selectId");
            selectFinish=true;



        }else {
            selectId=0;
            selectFinish=false;
            selectFlagInt=0;
        }

    }

    public void running(View layout){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                showNum = String.valueOf(numOfGray);
                infoButton.setText(showNum);
                if(selectFlag&&(selectFlagInt<=30)){
                    mStateBus p = new mStateBus(selectFinish,arriveNow,parking,leaveNow);
                    p.setSelectId(selectId);
                EventBus.getDefault().post(p);
                selectFlagInt++;}
            handler.postDelayed(this,100);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEventBus(mStateBus stateBus){
        selectFinish = stateBus.isSelectFinish();
        arriveNow = stateBus.isArriveNow();
        parking = stateBus.isParking();
        leaveNow = stateBus.isLeaveNow();
        parkingState = stateBus.getParkingState();

    }

}
