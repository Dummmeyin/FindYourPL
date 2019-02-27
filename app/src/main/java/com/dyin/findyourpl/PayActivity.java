package com.dyin.findyourpl;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class PayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        getSupportActionBar().hide();
        TextView actionBar = (TextView) findViewById(R.id.actionBarName2);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Qonquer_sans_2.otf");
        actionBar.setTypeface(typeface);
    }

}
