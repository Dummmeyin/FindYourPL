package com.dyin.findyourpl;


import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimeFragment extends Fragment implements View.OnClickListener {

    private int seconds = 0;
    private boolean running;
    private boolean wasRunning;
    private int parkState=0;

    private boolean selectFinish = false;
    private boolean arriveNow = false;
    private boolean parking = false;
    private boolean leaveNow = false;
    private int selectId=0;
    private Button stopButton;
    public TimeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            seconds = savedInstanceState.getInt("seconds");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
            running = savedInstanceState.getBoolean("running");
            if(wasRunning){
                running=true;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout =inflater.inflate(R.layout.fragment_time, container, false);
        runTimer(layout);

        stopButton = (Button) layout.findViewById(R.id.stopButton);
        stopButton.setOnClickListener(this);
        stopButton.setAlpha(0f);
        stopButton.setClickable(false);
        return layout;
    }

    @Override
    public void onPause() {
        super.onPause();
        wasRunning = running;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(wasRunning){
            running=true;
        }
        EventBus.getDefault().register(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEventBus(mStateBus stateBus){
        selectFinish = stateBus.isSelectFinish();
        arriveNow = stateBus.isArriveNow();
        parking = stateBus.isParking();
        leaveNow = stateBus.isLeaveNow();
        selectId = stateBus.getSelectId();
        parkState = stateBus.getParkingState();

    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds",seconds);
        outState.putBoolean("running",running);
        outState.putBoolean("wasRunning",wasRunning);
    }

    public void onClickFindCar(View v){


    }

    public void onClickPay(View v){
//        Intent intent = new Intent(getActivity(),PayActivity.class);
//        startActivityForResult(intent,28);

        PackageManager packageManager
                = this.getActivity().getPackageManager();
        Intent intent = packageManager.
                getLaunchIntentForPackage("com.eg.android.AlipayGphone");
        startActivity(intent);

    }


    public void runTimer(View layout){
        final TextView timeView = (TextView) layout.findViewById(R.id.time);
        final ImageView imageView1 = (ImageView) layout.findViewById(R.id.state1);
        final ImageView imageView2 = (ImageView) layout.findViewById(R.id.state2);
        final ImageView imageView3 = (ImageView) layout.findViewById(R.id.state3);
        final TextView moneyView = (TextView) layout.findViewById(R.id.fee);
        final Handler handler = new Handler();


        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds/3600;
                int minutes = (seconds%3600)/60;
                int secs = seconds%60;
                double money =0.5*(seconds/15);
                String time = String.format("%d:%02d:%02d",hours,minutes,secs);
                String moneyshow= String.format("$%02.2f",money);
                timeView.setText(time);
                moneyView.setText(moneyshow);

                if(running)
                {
                    seconds=seconds+1;
                }


                switch (parkState){

                    case 0:{
                        imageView1.setImageAlpha(20);
                        imageView2.setImageAlpha(20);
                        imageView3.setImageAlpha(20);
                        break;
                    }
                    case 1:
                    {
                        imageView1.setImageAlpha(250);
                        imageView2.setImageAlpha(20);
                        imageView3.setImageAlpha(20);
                        break;
                    }
                    case 2:
                    {
                        imageView1.setImageAlpha(20);
                        imageView2.setImageAlpha(250);
                        imageView3.setImageAlpha(20);
                        break;
                    }
                    case 3:
                    {
                        imageView1.setImageAlpha(20);
                        imageView2.setImageAlpha(250);
                        imageView3.setImageAlpha(20);
                        running=true;
                        break;
                    }
                    case 4:
                        {
                            imageView1.setImageAlpha(20);
                            imageView2.setImageAlpha(20);
                            imageView3.setImageAlpha(250);
                            running=false;
                            stopButton.setClickable(true);
                            stopButton.setAlpha(1f);
                            break;
                        }


                }

                handler.postDelayed(this,1000);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.stopButton:
                onClickPay(view);
                break;
        }
    }





}
