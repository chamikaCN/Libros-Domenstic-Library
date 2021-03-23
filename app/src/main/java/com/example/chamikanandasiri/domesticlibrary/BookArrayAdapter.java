package com.example.chamikanandasiri.domesticlibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import androidx.cardview.widget.CardView;

class BookArrayAdapter extends ArrayAdapter<String[]> {
    private Context context;
    private int resource;
    private DataBaseHelper dataBaseHelper;

    private String TAG = "Test";

    public BookArrayAdapter(Context context, int resource, ArrayList<String[]> objects, DataBaseHelper db) {
        super(context, resource, objects);
        this.context = context;
        this.dataBaseHelper = db;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String id = Objects.requireNonNull(getItem(position))[0];
        String title = Objects.requireNonNull(getItem(position))[1];
        String author = Objects.requireNonNull(getItem(position))[2];
        String availability = Objects.requireNonNull(getItem(position))[3];

        if (availability.equals("1")) {
            availability = "Available";
        } else if (availability.equals("0")) {
            availability = dataBaseHelper.getBorrowedUserByBookID(Integer.parseInt(id));
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);


        TextView txvName = convertView.findViewById(R.id.txvBookCardTile);
        TextView txvAuthor = convertView.findViewById(R.id.txvBookCardAuthor);
        TextView txvID = convertView.findViewById(R.id.txvBookCardID);
        TextView txvBorrow = convertView.findViewById(R.id.txvBookCardBorrower);

        txvID.setText(id);
        txvName.setText(title);
        txvAuthor.setText(author);
        txvBorrow.setText(availability);

        return convertView;
    }

}
