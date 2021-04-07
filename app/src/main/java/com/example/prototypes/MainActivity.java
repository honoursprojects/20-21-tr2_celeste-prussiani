package com.example.prototypes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.example.prototypes.CovidTracker.CovidTracker;
import com.example.prototypes.stopCovid.StopCovid;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private Boolean hasBluetoothPermission() {
        boolean flag;
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    private Boolean hasAdminBluetoothPermission() {
        boolean flag;
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    public void requestPermission(View view) {
        ArrayList<String> permissionsToRequest = new ArrayList<String>();
        if(!hasBluetoothPermission()) {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH);
        }
        if(!hasAdminBluetoothPermission()) {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_ADMIN);
        }

        if(!permissionsToRequest.isEmpty()) {
            //Convert to array to feed to method
            String permissions[] = new String[permissionsToRequest.size()];
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(permissions), 0);
        } else {
            System.out.println("wella");
        }

        openHomeActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0 && grantResults.length > 0) {
            for(int i = 0; i<grantResults.length; i++) {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println(grantResults[i] + " granted!");
                }
            }
        }
    }

    public void openHomeActivity() {
        intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}