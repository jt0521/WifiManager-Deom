package com.iwdael.wifimanager.example;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Bundle;
import android.os.Handler;
import android.os.PatternMatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.iwdael.wifimanager.IWifi;
import com.iwdael.wifimanager.IWifiManager;
import com.iwdael.wifimanager.OnWifiChangeListener;
import com.iwdael.wifimanager.OnWifiConnectListener;
import com.iwdael.wifimanager.WifiManager;

import java.util.ArrayList;
import java.util.List;

public class WifiActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    IWifiManager manager;
    List<IWifi> list = new ArrayList<>();
    String[] passwords = new String[]{
            "11111111",
            "22222222",
            "33333333",
            "44444444",
            "55555555",
            "66666666",
            "77777777",
            "88888888",
    };
    int index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        linearLayout = findViewById(R.id.ll);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, 1);
        }
        manager = WifiManager.create(this);
        manager.scanWifi();
        manager.setOnWifiChangeListener(new OnWifiChangeListener() {
            @Override
            public void onWifiChanged(List<IWifi> wifis) {
                add(wifis);

            }
        });
        manager.setOnWifiConnectListener(new OnWifiConnectListener() {
            @Override
            public void onConnectChanged(boolean status) {
                Log.e("tgl===", "connect changed: " + status);
            }
        });

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index == passwords.length-1) {
                    index = 0;
                }
                index++;
                button.setText("切换密码： " + passwords[index]);
            }
        });
        findViewById(R.id.button_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectWifi == null) {
                    android.net.wifi.WifiManager wm = (android.net.wifi.WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    String SSID = wm.getConnectionInfo().getSSID();
                    Log.e("tgl===", "remvoeWIfi：" + SSID);
                    ((WifiManager) manager).removeWifi(SSID);
                    return;
                }
                manager.removeWifi(connectWifi);
            }
        });
        findViewById(R.id.btn_sq).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                connectSq("Honor V10",passwords[index]);
            }
        });
    }

    private void add(List<IWifi> l) {
        for (IWifi i : l) {

            int tag = 0;
            for (IWifi w : list) {
                if (w.name().equals(i.name())) {
                    tag = 1;
                    break;
                }
            }
            if (tag == 0) {
                list.add(i);
                addView(i);
            }
        }


    }

    private IWifi connectWifi;

    private void addView(final IWifi wifi) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 180);
        layoutParams.topMargin = 10;
        textView.setLayoutParams(layoutParams);
        textView.setText(wifi.name());
        textView.setTextSize(20);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundColor(Color.BLACK);

        linearLayout.addView(textView);
        textView.setTag(wifi);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final IWifi wifi1 = (IWifi) view.getTag();
//                manager.connectEncryptWifi(wifi1, "TtJj@2814");

                if (connectWifi!=null) {
                    manager.removeWifi(connectWifi);
                }else {
                    ((WifiManager)manager).removeWifi("Honor V10");

                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        connectWifi = wifi;
                        manager.connectEncryptWifi(wifi1, passwords[index]);
                        Toast.makeText(WifiActivity.this, wifi1.name() + " " + passwords[index], Toast.LENGTH_SHORT).show();

                    }
                },2000);

            }
        });
    }


    private void connectSq(String wifiName, String wifiPassword) {
//        try {
//            WifiNetworkSpecifier.Builder builder = null;
//            if (android.os.Build.VERSION.SDK_INT >= 9) {
//                builder = new WifiNetworkSpecifier.Builder();
//                builder.setSsid(wifiName);
//                builder.setWpa2Passphrase(wifiPassword);
//
//                WifiNetworkSpecifier wifiNetworkSpecifier = builder.build();
//                NetworkRequest.Builder networkRequestBuilder = new NetworkRequest.Builder();
//                networkRequestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
//                networkRequestBuilder.addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED);
//                networkRequestBuilder.addCapability(NetworkCapabilities.NET_CAPABILITY_TRUSTED);
//                networkRequestBuilder.setNetworkSpecifier(wifiNetworkSpecifier);
//                NetworkRequest networkRequest = networkRequestBuilder.build();
//                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//                if (cm != null) {
//                    cm.requestNetwork(networkRequest, new ConnectivityManager.NetworkCallback() {
//                        @Override
//                        public void onAvailable(@NonNull Network network) {
//                            //Use this network object to Send request.
//                            //eg - Using OkHttp library to create a service request
//
//                            super.onAvailable(network);
//                        }
//                    });
//                }
//            }
//        } catch (Exception e) {
//        }
    }

}
