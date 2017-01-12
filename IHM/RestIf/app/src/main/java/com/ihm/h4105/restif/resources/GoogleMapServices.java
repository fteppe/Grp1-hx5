package com.ihm.h4105.restif.resources;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.ihm.h4105.restif.R;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by utilisateur on 11/01/2017.
 */

public class GoogleMapServices {
    private static Context context;

    public GoogleMapServices(Context context) {
    this.context = context;
    }

    public static void changeColorIcon(Marker marker, int progress){
        int timeMin = 5;
        int timeMed = 10;
        int timeMax = 15;

        String restaurant = marker.getTitle();
        System.out.println(restaurant);
        restaurant = restaurant.replaceAll("[' ()]", "").toLowerCase();

        String strTemp = Normalizer.normalize(restaurant, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        restaurant = pattern.matcher(strTemp).replaceAll("");

        System.out.println(restaurant);
        int identifier = context.getResources().getIdentifier(restaurant, "array", context.getPackageName());
        int [] mIdsArray = context.getResources().getIntArray(identifier);
        int waitingTime = mIdsArray[progress];

        if(waitingTime <timeMin) {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_restau3_vert_clair));
        } else if (waitingTime < timeMed) {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_restau3_jaune));
        } else if (waitingTime < timeMax) {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_restau3_orange));
        } else if (waitingTime > timeMax) {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_restau3_rouge));
        }

    }

}
