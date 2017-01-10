package com.ihm.h4105.restif.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Rect;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ihm.h4105.restif.R;
import com.ihm.h4105.restif.resources.SeekBarHint;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {

    TextView textSeekBar;
    private GoogleMap mMap;
    private SeekBarHint mSeekBar;
    private HashMap<Marker, Integer> idRestos;

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        System.out.println(drawer);
        toggle.syncState();


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

        } else if (id == R.id.with) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        idRestos = new HashMap<Marker, Integer>();
        // Add a marker in Sydney and move the camera
        LatLng ri = new LatLng(45.781084, 4.873575);
        Marker markRI = mMap.addMarker(new MarkerOptions().position(ri).title("Restaurant INSA"));
        idRestos.put(markRI,0);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ri, 16.0f));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(MainActivity.this, InfoRestoActivity.class);
                Bundle b = new Bundle();
                b.putInt("idresto", idRestos.get(marker)); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
                return true;
            }
        });
    }
}
