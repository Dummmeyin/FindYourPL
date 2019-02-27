package com.dyin.findyourpl;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private ViewPager mViewPager;
    private LocationFragment locationFragment= new LocationFragment();
    private TimeFragment timeFragment = new TimeFragment();
    private ConfigFragment configFragment = new ConfigFragment();
    private FragmentTransaction ft;
    List<Fragment> mFragments;
    private int lastIndex=0;
    private FragmentManager fm;
    private boolean selectFinish = false;
    private boolean arriveNow = false;
    private boolean parking = false;
    private boolean leaveNow = false;
    private TextView actionBarView;

    private BottomNavigationView.OnNavigationItemSelectedListener   mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                {
                    setFragmentPosition(0);
                }
                return true;
                case R.id.navigation_dashboard:
                {
                    setFragmentPosition(1);
                }
                return true;
                case R.id.navigation_notifications:
                {

                    setFragmentPosition(2);
                }
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        TextView actionBar = (TextView) findViewById(R.id.actionBarName);
        actionBarView =actionBar;
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Qonquer_sans_2.otf");
        actionBar.setTypeface(typeface);

        mFragments = new ArrayList<>();
        mFragments.add(locationFragment);
        mFragments.add(timeFragment);
        mFragments.add(configFragment);
        setFragmentPosition(0);


    }

    public boolean getState(String state){
        switch (state){
            case "arriveNow":
                return arriveNow;
            case"selectFinish":
                return selectFinish;
            case "parking":
                return parking;
            case "leaveNow":
                return leaveNow;
                default:
                    break;
        }
        return false;
    }

    private void setFragmentPosition(int position) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment currentFragment = mFragments.get(position);
        Fragment lastFragment = mFragments.get(lastIndex);
        lastIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.frameLayout, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }


    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEventBus(mStateBus stateBus){
        selectFinish = stateBus.isSelectFinish();
        arriveNow = stateBus.isArriveNow();
        parking = stateBus.isParking();
        leaveNow = stateBus.isLeaveNow();

    }

}



