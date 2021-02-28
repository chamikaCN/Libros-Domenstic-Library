package com.example.chamikanandasiri.domesticlibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

class BookArrayAdapter extends ArrayAdapter<String[]> {
    private Context context;
    private int resource;

    private String TAG = "Test";

    public BookArrayAdapter(Context context, int resource, ArrayList<String[]> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String title = Objects.requireNonNull(getItem(position))[0];
        String author = Objects.requireNonNull(getItem(position))[1];

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView txvName = convertView.findViewById(R.id.txvBookCardTile);
        TextView txvAuthor = convertView.findViewById(R.id.txvBookCardAuthor);

        txvName.setText(title);
        txvAuthor.setText(author);

        return convertView;
    }

}
