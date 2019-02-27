package com.dyin.findyourpl;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.map.MapView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class MapViewActivity extends AppCompatActivity implements View.OnClickListener{

    private int numOfAll = 25;
    private int numOfGreen = 0;
    private int numOfGray =0;
    private int numOfRed = 0;
    private int numOfYellow = 0;
    private Button parklot1,parklot2,parklot3,parklot4,parklot5,
            parklot6,parklot7,parklot8,parklot9,parklot10,
            parklot11,parklot12,parklot13,parklot14,parklot15,
            parklot16,parklot17,parklot18,parklot19,parklot20,
            parklot21,parklot22,parklot23,parklot24,parklot25;
    private Button arrow1,arrow2,arrow3,arrow4,arrow5,
            arrow6,arrow7,arrow8,arrow9,arrow10,
            arrow11,arrow12,arrow13,arrow14,arrow15,
            arrow16,arrow17,arrow18,arrow19,arrow20,
            arrow21,arrow22,arrow23,arrow24,arrow25;
    private Button ctrl1,ctrl2,ctrl3;

    private Button nowButton=null;
    private Button selectButton=null;
    private boolean selectFlag = false;
    private int selectId=0;
    private int parkingState =0;

    ArrayList<Button> buttonlist = new ArrayList<Button>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        getSupportActionBar().hide();

        Intent mIntent = getIntent();
        selectFlag=mIntent.getBooleanExtra("selectFlag",false);
        selectId=mIntent.getIntExtra("selectId",0);
        parkingState = mIntent.getIntExtra("parkingState",0);

        viewInit();
        running();

    }

    public void viewInit(){
        parklot1 = findViewById(R.id.parklot1);parklot2 = findViewById(R.id.parklot2);
        parklot3 = findViewById(R.id.parklot3);parklot4 = findViewById(R.id.parklot4);
        parklot5 = findViewById(R.id.parklot5);parklot6 = findViewById(R.id.parklot11);
        parklot7 = findViewById(R.id.parklot12);parklot8 = findViewById(R.id.parklot13);
        parklot9 = findViewById(R.id.parklot14);parklot10 = findViewById(R.id.parklot15);
        parklot11 = findViewById(R.id.parklot16);parklot12 = findViewById(R.id.parklot17);
        parklot13 = findViewById(R.id.parklot18);parklot14 = findViewById(R.id.parklot31);
        parklot15 = findViewById(R.id.parklot20);parklot16 = findViewById(R.id.parklot21);
        parklot17 = findViewById(R.id.parklot22);parklot18 = findViewById(R.id.parklot23);
        parklot19 = findViewById(R.id.parklot24);parklot20 = findViewById(R.id.parklot25);
        parklot21 = findViewById(R.id.parklot26);parklot22 = findViewById(R.id.parklot27);
        parklot23 = findViewById(R.id.parklot28);parklot24 = findViewById(R.id.parklot29);
        parklot25 = findViewById(R.id.parklot30);

        parklot1.setOnClickListener(this);parklot2.setOnClickListener(this);
        parklot3.setOnClickListener(this);parklot4.setOnClickListener(this);
        parklot5.setOnClickListener(this);parklot6.setOnClickListener(this);
        parklot7.setOnClickListener(this);parklot8.setOnClickListener(this);
        parklot9.setOnClickListener(this);parklot10.setOnClickListener(this);
        parklot11.setOnClickListener(this);parklot12.setOnClickListener(this);
        parklot13.setOnClickListener(this);parklot14.setOnClickListener(this);
        parklot15.setOnClickListener(this);parklot16.setOnClickListener(this);
        parklot17.setOnClickListener(this);parklot18.setOnClickListener(this);
        parklot19.setOnClickListener(this);parklot20.setOnClickListener(this);
        parklot21.setOnClickListener(this);parklot22.setOnClickListener(this);
        parklot23.setOnClickListener(this);parklot24.setOnClickListener(this);
        parklot25.setOnClickListener(this);

        arrow1 = findViewById(R.id.arrow);arrow2 = findViewById(R.id.arrow1);
        arrow3 = findViewById(R.id.arrow2);arrow4 = findViewById(R.id.arrow3);
        arrow5 = findViewById(R.id.arrow4);arrow6 = findViewById(R.id.arrow7);
        arrow7 = findViewById(R.id.arrow8);arrow8 = findViewById(R.id.arrow9);
        arrow9 = findViewById(R.id.arrow10);arrow10 = findViewById(R.id.arrow11);
        arrow11 = findViewById(R.id.arrow12);arrow12 = findViewById(R.id.arrow13);
        arrow13 = findViewById(R.id.arrow14);arrow14 = findViewById(R.id.arrow15);
        arrow15 = findViewById(R.id.arrow16);arrow16 = findViewById(R.id.arrow17);
        arrow17 = findViewById(R.id.arrow18);arrow18 = findViewById(R.id.arrow19);
        arrow19 = findViewById(R.id.arrow20);arrow20 = findViewById(R.id.arrow21);
        arrow21 = findViewById(R.id.arrow22);arrow22 = findViewById(R.id.arrow23);
        arrow23 = findViewById(R.id.arrow24);


        if(selectFlag){
            selectButton = findViewById(selectId);
            selectButton.setBackgroundResource(R.drawable.parklotyellow);
        }


        ctrl1= findViewById(R.id.ctrl1);
        ctrl2= findViewById(R.id.ctrl2);
        ctrl3=findViewById(R.id.ctrl3);
        ctrl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((nowButton!=null)&&(selectButton==null)){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MapViewActivity.this);
                    dialog.setTitle("预约车位");
                    dialog.setMessage("您确定要预约这个车位吗");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            selectButton = nowButton;
                            selectButton.setBackgroundResource(R.drawable.parklotyellow);
                            setAllParklotVisible();
                            nowButton=null;
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setAllParklotVisible();
                            nowButton=null;
                        }
                    });

                    dialog.show();

                }
                else if(nowButton==null&&selectButton==null){
                    Toast.makeText(MapViewActivity.this,"请选择一个车位",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MapViewActivity.this,"若要选择请先取消选择的车位",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ctrl2.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAllParklotVisible();
                nowButton=null;
                if(selectButton!=null){
                    selectButton.setBackgroundResource(R.drawable.parklotgray);
                    selectButton=null;
                    Toast.makeText(MapViewActivity.this,"你取消了预定",Toast.LENGTH_SHORT).show();
                }
            }
        });


        ctrl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numOfGray=0;
                numOfYellow=0;
                numOfRed=0;
                numOfGreen=0;
                for(int i=0;i<buttonlist.size();i++){
                    final int current=i;
                    Drawable drawable = buttonlist.get(i).getBackground();
                    Drawable drawable1 = getDrawable(R.drawable.parklotred);
                    Drawable drawable2 = getDrawable(R.drawable.parklotgreen);
                    Drawable drawable3 = getDrawable(R.drawable.parklotyellow);
                    Drawable drawable4 = getDrawable(R.drawable.parklotgray);
                    if(areDrawablesIdentical(drawable,drawable1))
                        numOfRed++;
                    if(areDrawablesIdentical(drawable,drawable2))
                        numOfGreen++;
                    if(areDrawablesIdentical(drawable,drawable3))
                        numOfYellow++;
                    if(areDrawablesIdentical(drawable,drawable4))
                        numOfGray++;
                }

                Intent intent = getIntent();
                intent.putExtra("numOfRed",numOfRed);
                intent.putExtra("numOfYellow",numOfYellow);
                intent.putExtra("numOfGreen",numOfGreen);
                intent.putExtra("numOfGray",numOfGray);
                if(selectButton!=null){
                    int selectId=selectButton.getId();
                    boolean selectFlag =true;
                    intent.putExtra("selectFlag",selectFlag);
                    intent.putExtra("selectId",selectId);
                }
                else{
                    boolean selectFlag =false;
                    int selectId=0;
                    intent.putExtra("selectFlag",selectFlag);
                    intent.putExtra("selectId",selectId);
                }

                setResult(31,intent);
                MapViewActivity.this.finish();
            }
        });

        buttonlist.add(parklot1);buttonlist.add(parklot2);buttonlist.add(parklot3);
        buttonlist.add(parklot4);buttonlist.add(parklot5);buttonlist.add(parklot6);
        buttonlist.add(parklot7);buttonlist.add(parklot8);buttonlist.add(parklot9);
        buttonlist.add(parklot10);buttonlist.add(parklot11);buttonlist.add(parklot12);
        buttonlist.add(parklot13);buttonlist.add(parklot14);buttonlist.add(parklot15);
        buttonlist.add(parklot16);buttonlist.add(parklot17);buttonlist.add(parklot18);
        buttonlist.add(parklot19);buttonlist.add(parklot20);buttonlist.add(parklot21);
        buttonlist.add(parklot22);buttonlist.add(parklot23);buttonlist.add(parklot24);
        buttonlist.add(parklot25);

        buttonlist.add(arrow1);buttonlist.add(arrow2);buttonlist.add(arrow3);
        buttonlist.add(arrow4);buttonlist.add(arrow5);buttonlist.add(arrow6);
        buttonlist.add(arrow7);buttonlist.add(arrow8);buttonlist.add(arrow9);
        buttonlist.add(arrow10);buttonlist.add(arrow11);buttonlist.add(arrow12);
        buttonlist.add(arrow13);buttonlist.add(arrow14);buttonlist.add(arrow15);
        buttonlist.add(arrow16);buttonlist.add(arrow17);buttonlist.add(arrow18);
        buttonlist.add(arrow19);buttonlist.add(arrow20);buttonlist.add(arrow21);
        buttonlist.add(arrow22);buttonlist.add(arrow23);

        //arrows become transparent
        for(int i=25;i<buttonlist.size();i++){
            final int current=i;
            buttonlist.get(i).setVisibility(View.INVISIBLE);
        }

        numOfGray=0;
        numOfYellow=0;
        numOfRed=0;
        numOfGreen=0;

        for(int i=0;i<buttonlist.size();i++){
            final int current=i;

            Drawable drawable = buttonlist.get(i).getBackground();
            Drawable drawable1 = getDrawable(R.drawable.parklotred);
            Drawable drawable2 = getDrawable(R.drawable.parklotgreen);
            Drawable drawable3 = getDrawable(R.drawable.parklotyellow);
            Drawable drawable4 = getDrawable(R.drawable.parklotgray);
            if(areDrawablesIdentical(drawable,drawable1))
                numOfRed++;
            if(areDrawablesIdentical(drawable,drawable2))
                numOfGreen++;
            if(areDrawablesIdentical(drawable,drawable3))
                numOfYellow++;
            if(areDrawablesIdentical(drawable,drawable4))
                numOfGray++;
        }


    }


    public void setAllParklotTransparent(){
        for(int i=0;i<25;i++){
            final int current=i;
            buttonlist.get(i).setAlpha(0.1f);
        }
    }

    public void setAllParklotVisible(){
        for(int i=0;i<25;i++){
            final int current=i;
            buttonlist.get(i).setAlpha(1f);
        }
    }

    public static boolean areDrawablesIdentical(Drawable drawableA, Drawable drawableB) {
        Drawable.ConstantState stateA = drawableA.getConstantState();
        Drawable.ConstantState stateB = drawableB.getConstantState();
        // If the constant state is identical, they are using the same drawable resource.
        // However, the opposite is not necessarily true.
        return (stateA != null && stateB != null && stateA.equals(stateB))
                || getBitmap(drawableA).sameAs(getBitmap(drawableB));
    }


    public static Bitmap getBitmap(Drawable drawable) {
        Bitmap result;
        if (drawable instanceof BitmapDrawable) {
            result = ((BitmapDrawable) drawable).getBitmap();
        } else {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            // Some drawables have no intrinsic width - e.g. solid colours.
            if (width <= 0) {
                width = 1;
            }
            if (height <= 0) {
                height = 1;
            }

            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return result;
    }


    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEventBus(mStateBus stateBus){
        parkingState = stateBus.getParkingState();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }



    @Override
    public void onClick(View view) {

        setAllParklotTransparent();
        view.setAlpha(1f);
        nowButton = (Button) view;
    }

    public void running(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                switch (parkingState){
                    case 2:{
                        selectButton = findViewById(selectId);
                        selectButton.setBackgroundResource(R.drawable.parklotgreen);
                        preShowArrow(selectId);
                        break;
                    }
                    case 3:{
                        selectButton = findViewById(selectId);
                        selectButton.setBackgroundResource(R.drawable.parklotgreen);
                        hideArrow();
                        break;
                    }case 4:{
                        selectButton = findViewById(selectId);
                        selectButton.setBackgroundResource(R.drawable.parklotgray);
                        break;
                    }
                    default:break;
                }
                handler.postDelayed(this,500);
            }
        });
    }


    public void preShowArrow(int id){
        ArrayList<Integer> ids = new ArrayList<>();
        switch (id){
            case R.id.parklot18:{
                ids.clear();
                ids.add(R.id.arrow1);ids.add(R.id.arrow);
                break;
            }default:{
                ids.clear();
                ids.add(R.id.arrow);
                break;
            }
        }

        showArrow(ids);
    }

    public void showArrow(ArrayList<Integer> target){
        for(int i=25;i<buttonlist.size();i++){
            for(int j=0;j<target.size();j++){
            if(buttonlist.get(i).getId()==target.get(j))
            buttonlist.get(i).setVisibility(View.VISIBLE);
            buttonlist.get(i).setAlpha(1f);
            }
        }
    }

    public void hideArrow(){
        for(int i=25;i<buttonlist.size();i++){
            buttonlist.get(i).setVisibility(View.INVISIBLE);
        }
    }




}
