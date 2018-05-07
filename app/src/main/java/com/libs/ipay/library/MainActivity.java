package com.libs.ipay.library;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.libs.ipay.ipayLibrary.Channels;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String live = "1";
        String vid  = "0000";
        String cbk  = "http://test.tickets4u.co.ke/ipayAndroidLibraryCbk.php";
        String key  = "00003";

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Channels fragment1 = new Channels();
        Bundle data = new Bundle();
        data.putString("live", live);
        data.putString("vid", vid);
        data.putString("cbk", cbk);
        data.putString("key", key);
        fragment1.setArguments(data);
        fragmentTransaction.add(R.id.layouta, fragment1, "fragment");
        fragmentTransaction.commit();


    }
}
