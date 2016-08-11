package com.orgfree.safetyfirst;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class TrackLocation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Context context;
    String temp_u = "http://safetyfirst.orgfree.com/index.php?type=retv";
    String tbnm;
    Timer timer = new Timer();
    String l;
    int last_le=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        DataBase db = new DataBase(this);
        Cursor c = db.getData();
        c.moveToLast();
        tbnm="ph"+c.getString(1);
        context = this;
        //Toast.makeText(this, "Hello zfff", Toast.LENGTH_SHORT).show();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Add a marker in Sydney and move the camera
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
        mMap.setMyLocationEnabled(true);
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                new SendData().execute();

            }

        }, 0,10000);
    }
    class SendData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(l.length()>10)
            {
                Log.v("if reaches",l);
                String[] str =  l.split("%");
                int ps= str.length;
                int i;
                if(last_le==0)
                    i=0;
                else
                    i=last_le+1;
                while(i<ps)
                {
                    String[] point = str[i].split("&");
                    String[] latlng =point[0].split(";");
                /*Toast.makeText(context,latlng[0], Toast.LENGTH_LONG).show();
                 double d= Double.parseDouble(latlng[0]);
                 // Toast.makeText(context,""+d,Toast.LENGTH_LONG).show();*/
                    LatLng pos= new LatLng(Double.parseDouble(latlng[0]),Double.parseDouble(latlng[1]));
                    if(i==0)
                    {
                        LatLng fi = new LatLng(Double.parseDouble(latlng[0]),Double.parseDouble(latlng[1]));
                        mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(fi,17));
                    }
                    mMap.addMarker( new MarkerOptions().position(pos).title(point[1]));
                    i++;
                }
                last_le=ps-1;
            }
            else
            {
                Log.v("Else reaches","asdgahdgasdygasd\nsdgasydaygdasd\n");
               final  AlertDialog.Builder sd = new AlertDialog.Builder(context);
                sd.setTitle("Safet First");
                sd.setMessage("Everyone feels safe right now saty connected");
                sd.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                sd.show();
                //Toast.makeText(context,"Everyone feels safe",Toast.LENGTH_LONG);
                timer.cancel();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                String u = temp_u + "&tbnm="+tbnm;
                URL url = new URL(u);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.connect();
                int code = con.getResponseCode();
                InputStream in = con.getInputStream();
                Scanner obj= new Scanner( in);
                 l = obj.nextLine();


            } catch (Exception ex) {
            }
            return null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }
}
