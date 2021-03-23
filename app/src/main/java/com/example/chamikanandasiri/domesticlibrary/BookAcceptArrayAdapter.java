package com.example.chamikanandasiri.domesticlibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

class BookAcceptArrayAdapter extends ArrayAdapter<String[]> {
    private Context context;
    private int resource;
    private DataBaseHelper dataBaseHelper;
    ArrayList<String[]> objects;

    private String TAG = "Test";

    public BookAcceptArrayAdapter(Context context, int resource, ArrayList<String[]> objects, DataBaseHelper db) {
        super(context, resource, objects);
        this.context = context;
        this.dataBaseHelper = db;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String bookId = Objects.requireNonNull(getItem(position))[0];
        String name = Objects.requireNonNull(getItem(position))[1];
        String date = Objects.requireNonNull(getItem(position))[2];
        String userId = Objects.requireNonNull(getItem(position))[3];

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView txvName = convertView.findViewById(R.id.txvUserBookCardTitle);
        TextView txvDate = convertView.findViewById(R.id.txvUserBookCardDate);
        Button btnComplete = convertView.findViewById(R.id.btnUserBookCardComplete);

        txvName.setText(name);
        txvDate.setText(date);
        btnComplete.setOnClickListener(view -> {
            dataBaseHelper.updateReceiptCompletion(bookId, userId, date);
            objects.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }

}
