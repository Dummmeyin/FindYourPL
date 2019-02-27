package com.dyin.findyourpl;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class ConfigFragment extends Fragment {


    public boolean selectFinish = false;
    public boolean arriveNow = false;
    public boolean parking = true;
    public boolean leaveNow = false;

    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";
    String address = null;

    private ProgressDialog progress;
    ListView devicelist;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private String sendMsg = null;
    private String receiveMsg=null;
    private  InputStream mmInStream ;
    private  OutputStream mmOutStream ;

    private int parkingState = 0;

    public ConfigFragment() {
        // Required empty public constructor
    }

    private Button testButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout=  inflater.inflate(R.layout.fragment_config, container, false);
        devicelist = layout.findViewById(R.id.listView);
        testButton = layout.findViewById(R.id.config);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String s = String.valueOf(parkingState);
                    msg(s);
            }
        });


        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if ( myBluetooth==null ) {
            Toast.makeText(getActivity(), "Bluetooth device not available", Toast.LENGTH_LONG).show();
        } else if ( !myBluetooth.isEnabled() ) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }


        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if ( pairedDevices.size() > 0 ) {
            for ( BluetoothDevice bt : pairedDevices ) {
                list.add(bt.getName().toString() + "\n" + bt.getAddress().toString());
            }
        } else {
            Toast.makeText(getActivity(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener);

        return layout;
    }



    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String info = ((TextView) view).getText().toString();
            address = info.substring(info.length()-17);
            Toast.makeText(getActivity(), address, Toast.LENGTH_LONG).show();
            new ConnectBT().execute();


        }
    };


    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected  void onPreExecute () {
            progress = ProgressDialog.show(getActivity(), "Connecting...", "Please Wait!!!");
        }

        @Override
        protected Void doInBackground (Void... devices) {
            try {
                if ( btSocket==null || !isBtConnected ) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute (Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
            } else {
                msg("Connected");
                isBtConnected = true;
            }

            progress.dismiss();

            if(isBtConnected)
            {
                new BTCommunicate().execute();
            }
        }
    }


    private void msg (String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    private void sendSignal ( String sendMsg ) {
        if ( btSocket != null ) {
            try {
                btSocket.getOutputStream().write(sendMsg.getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }


    private class BTCommunicate extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try{
                tmpIn = btSocket.getInputStream();
                tmpOut = btSocket.getOutputStream();
            }catch (IOException e){
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;

            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            while (true){
                    try{
                        // Read from the InputStream
                        bytes = mmInStream.read(buffer);
                        if(bytes>0)
                        parkingState = buffer[bytes-1];
                        EventBus.getDefault().post(new mStateBus(parkingState));

                    }catch (IOException e){
                        break;
                    }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

}
