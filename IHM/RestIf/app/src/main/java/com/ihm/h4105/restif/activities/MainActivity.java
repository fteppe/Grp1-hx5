package com.ihm.h4105.restif.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ihm.h4105.restif.R;
import com.ihm.h4105.restif.resources.GoogleMapServices;
import com.ihm.h4105.restif.resources.SeekBarHint;


import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, SeekBar.OnSeekBarChangeListener, LocationListener {

    TextView textSeekBar;
    private GoogleMap mMap;
    private SeekBarHint mSeekBar;
    private static int iconSize = 200;
    private LocationManager locationManager;
    private String provider;
    private Location mCurrentLocation;
    private Marker mkrCurrentPosition;
    private GoogleMapServices googleMapServices;
    private List<Marker> listMarkersMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] items = new String[] {"Amis", "Attente"};
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 1) {
                    RelativeLayout tl = (RelativeLayout)findViewById(R.id.layoutSeekBar);
                    tl.setVisibility(View.VISIBLE);
                } else {
                    RelativeLayout tl = (RelativeLayout)findViewById(R.id.layoutSeekBar);
                    tl.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        System.out.println(drawer);
        toggle.syncState();

        googleMapServices = new GoogleMapServices(this.getApplicationContext());
        listMarkersMap = new ArrayList<>();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Obtain the MapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Gestion du slider pour sélectionner son horaire
        mSeekBar = (SeekBarHint) findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(this);
        textSeekBar = (TextView) findViewById(R.id.myTextLLLLLLL);
        RelativeLayout tl = (RelativeLayout)findViewById(R.id.layoutSeekBar);

        tl.setVisibility(View.INVISIBLE);

        // ******************** Geolocation ************************

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        int hours = 11 + (progress) / 12; // it will return hours.
        int minutes = (progress * 5) % 60;
        Date date = new Date();
        date.setHours(hours);
        date.setMinutes(minutes);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(date);
        Rect thumbRect = mSeekBar.getSeekBarThumb().getBounds();

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)textSeekBar.getLayoutParams();
        //params.addRule(RelativeLayout.ABOVE, mSeekBar.getId());
        params.setMargins(
                thumbRect.centerX(),0, 0, 0);
        textSeekBar.requestLayout();

        /*RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.ABOVE, mSeekBar.getId());
        p.setMargins(
                thumbRect.centerX(),0, 0, 0);*/
        //textSeekBar.setLayoutParams(p);
        textSeekBar.setText(time);

        if(mMap != null) {
            for (Marker marker : listMarkersMap) {
                googleMapServices.changeColorIcon(marker, time, progress, resizeMapIcons("icon_restau",iconSize,iconSize));
            }
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.notif) {
            // Handle the camera action

        } else if (id == R.id.restaurant) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng posCastorPollux = new LatLng(45.781181, 4.873553);
        Marker mrkCastorPollux = mMap.addMarker(new MarkerOptions()
                .position(posCastorPollux)
                .title("Castor et Pollux (Le Beurk)")
                .snippet("Horaires : " + "\n" +"Midi : 11h30 - 13h30" + "\n" + "Soir : 18h - 20h" + "\n" +"Lun-Ven"));
        mrkCastorPollux.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("icon_restau",iconSize,iconSize)));
        listMarkersMap.add(mrkCastorPollux);


        LatLng posPrevert = new LatLng(45.781151, 4.873417);
        Marker mrkPrevert = mMap.addMarker(new MarkerOptions()
                .position(posPrevert)
                .title("Le Prévert")
                .snippet("Horaires : " + "\n" +"Midi : 11h30 - 13h30" + "\n" + "Soir : 18h - 20h" + "\n" +"Lun-Ven"));
        mrkPrevert.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("icon_restau",iconSize,iconSize)));
        listMarkersMap.add(mrkPrevert);

        LatLng posGrillon = new LatLng(45.783926, 4.875050);
        Marker mrkGrillon = mMap.addMarker(new MarkerOptions()
                .position(posGrillon)
                .title("Le Grillon")
                .snippet("Horaires : " + "\n" +"Midi : 11h30 - 13h30" + "\n" + "Soir : Fermé" + "\n" +"Lun-Ven"));

        mrkGrillon.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("icon_restau",iconSize,iconSize)));
        listMarkersMap.add(mrkGrillon);

        LatLng posOlivier = new LatLng(45.784242, 4.874808);
        Marker mrkOlivier = mMap.addMarker(new MarkerOptions()
                .position(posOlivier)
                .title("L'Olivier")
                .snippet("Horaires : " + "\n" +"Midi : 11h30 - 13h30" + "\n" + "Soir : Fermé" + "\n" +"Lun-Ven"));
        mrkOlivier.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("icon_restau",iconSize,iconSize)));
        listMarkersMap.add(mrkOlivier);


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick (Marker marker) {
                if(!marker.getTitle().equals("Ma position")) {
                    Intent intent = new Intent(MainActivity.this, InfoRestoActivity.class);
                    intent.putExtra("restau_selected", marker.getTitle());
                    startActivity(intent);
                }
            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getBaseContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getBaseContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getBaseContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        //  ****** GeoLocation ******
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            mCurrentLocation=location;
            LatLng currentPosition =  new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 16.0f));
            onLocationChanged(location);
        } else {

        }

    }

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }
        long minTime = 400;
        float minDistance = 1;
        locationManager.requestLocationUpdates(provider, minTime, minDistance, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        float lat = (float) (location.getLatitude());
        float lng = (float) (location.getLongitude());
        LatLng ri = new LatLng(lat, lng);
        // On evite que ca bouge trop
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ri, 16.0f));
        if(mkrCurrentPosition==null) {
            mkrCurrentPosition = mMap.addMarker(new MarkerOptions().position(ri).title("Ma position"));
            mkrCurrentPosition.showInfoWindow();
        }
        else {
            mkrCurrentPosition.setPosition(ri);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

}
