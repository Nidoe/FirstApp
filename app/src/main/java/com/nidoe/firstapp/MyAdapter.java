package com.nidoe.firstapp;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by luvz on 4/7/2015.
 */
public class MyAdapter extends ArrayAdapter<Stream> {

    private final Context context;
    private final ArrayList<Stream> itemsArrayList;

    public MyAdapter(Context context, ArrayList<Stream> itemsArrayList) {

        super(context, R.layout.target_item, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.target_item, parent, false);

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.item_title);
        TextView valueView = (TextView) rowView.findViewById(R.id.item_counter);

        // 4. Set the text for textView
        labelView.setText(itemsArrayList.get(position).getDisplayName());
        valueView.setText(itemsArrayList.get(position).getFollowers());

        // 5. retrn rowView
        return rowView;
    }
}