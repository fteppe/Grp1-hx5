package com.ihm.h4105.restif.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ihm.h4105.restif.R;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Notifications");
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        final Button button = (Button) findViewById(R.id.notif_invit1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(NotificationsActivity.this, InfoRestoActivity.class);
                intent.putExtra("restau_selected", "Le Grillon");
                startActivity(intent);
            }
        });
        final Button button2 = (Button) findViewById(R.id.notif_annul1);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(NotificationsActivity.this, InfoRestoActivity.class);
                intent.putExtra("restau_selected", "Le Grillon");
                startActivity(intent);
            }
        });

        final Button button3 = (Button) findViewById(R.id.notif_annul2);
        button3.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(NotificationsActivity.this, InfoRestoActivity.class);
            intent.putExtra("restau_selected", "Le Grillon");
            startActivity(intent);
            }
        });}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
