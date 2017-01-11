package com.ihm.h4105.restif.resources;

import android.content.Context;
import android.content.res.Resources;

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

    public static void changeColorIcon(Marker marker, String time, int progress){
        int timeMin = 5;
        int timeMedMin = 10;
        int timeMedMax = 15;
        int timeMax = 20;
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

        try {
            date = formatter.parse(time);
            System.out.println(date);
            System.out.println(formatter.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String restaurant = marker.getTitle();
        System.out.println(time);
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

        } else if (waitingTime < timeMedMin) {

        } else if (waitingTime < timeMedMax) {

        } else if (waitingTime < timeMax) {

        } else {

        }

    }

}
