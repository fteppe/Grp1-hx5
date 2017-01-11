package com.ihm.h4105.restif;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Thibault on 11/01/2017.
 */

public class AmiAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Ami> mDataSource;

    public AmiAdapter(Context context, ArrayList<Ami> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.ami_list_item, parent, false);

        Ami ami = mDataSource.get(position);

        TextView viewNom = (TextView) rowView.findViewById(R.id.nom);
        TextView viewPrenom = (TextView) rowView.findViewById(R.id.prenom);
        TextView viewMangeAvec = (TextView) rowView.findViewById(R.id.mangeAvec);

        viewNom.setText(ami.nom);
        viewPrenom.setText(ami.prenom);

        if(!ami.mangeAvec.isEmpty())
        {
            viewMangeAvec.setText("Mange avec : "+ ami.mangeAvec);
        }

        ImageView viewPhoto = (ImageView) rowView.findViewById(R.id.photo);
        switch (ami.idPhoto)
        {
            case "personne":
                viewPhoto.setImageResource(R.drawable.personne);
                break;
            case "personne2":
                viewPhoto.setImageResource(R.drawable.personne2);
                break;
            case "personne3":
                viewPhoto.setImageResource(R.drawable.personne3);
                break;
        }

        return rowView;
    }
}
