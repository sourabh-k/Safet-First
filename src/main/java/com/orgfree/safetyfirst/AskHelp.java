package com.orgfree.safetyfirst;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Timer;

public class AskHelp extends AppCompatActivity {

    Context context = this;
    Timer timer = new Timer();
    String temp_u = "http://safetyfirst.orgfree.com/index.php?type=send";
    String tb1, tb2, tb3;
    String count;
    LocationManager mLocationManager;
    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            // Log.v("dfd",location.getLatitude()+"fgdfgdfgdfgdfgdgdffd\ngfgdfgdfgdfgg");
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            count = lat + ";" + lng;
            new SendData().execute();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.v("asdadA", "fgdfgdfgdfgdfgdgdffd\ngfgdfgdfgdfgg");
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.v("dadA", "fgdfgdfgdfgdfgdgdffd\ngfgdfgdfgdfgg");

        }

        @Override
        public void onProviderDisabled(String s) {
            Log.v("123456", "fgdfgdfgdfgdfgdgdffd\ngfgdfgdfgdfgg");
        }
    };;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_help);
        DataBase db = new DataBase(this);
        Cursor c = db.getData();
        c.moveToFirst();
        tb1 = c.getString(1);
        tb1 = "ph" + tb1;
        //-------------
        c.moveToNext();
        tb2 = c.getString(1);
        tb2 = "ph" + tb2;
        //------------
        c.moveToNext();
        tb3 = c.getString(1);
        tb3 = "ph" + tb3;
       // Toast.makeText(this, "MAin", Toast.LENGTH_LONG).show();
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000, 2, mLocationListener);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

//        timer.scheduleAtFixedRate(new TimerTask() {
//
//            public void run() {
//                new SendData().execute();
//
//            }
//
//        }, 0,10000);

    }

    public void stop(View view) {
        mLocationManager.removeUpdates(mLocationListener);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        finish();
    }

    class SendData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Calendar c= Calendar.getInstance();
                String date =c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+","+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH);
                String u = temp_u + "&tbnm=" + tb1 + "&time=" + date + "&latlng=" +count;
                //u="http://safetyfirst.orgfree.com/SafetyFirst/sendlocation.php?tbnm=ph7&time=2&latlng=2";
                URL url = new URL(u);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.connect();
                int code = con.getResponseCode();
                u = temp_u + "&tbnm=" + tb2 + "&time=" + date + "&latlng=" + count;
                URL url1 = new URL(u);
                HttpURLConnection con1 = (HttpURLConnection) url1.openConnection();
                con1.connect();
                code = con1.getResponseCode();
                u = temp_u + "&tbnm=" + tb3 + "&time=" + date + "&latlng=" + count;
                URL url2 = new URL(u);
                HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
                con2.connect();
                code = con2.getResponseCode();

            } catch (Exception ex) {
            }
            return null;
        }
    }
}
