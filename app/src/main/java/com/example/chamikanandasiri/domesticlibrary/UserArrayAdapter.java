package com.example.chamikanandasiri.domesticlibrary;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

class UserArrayAdapter extends ArrayAdapter<String[]> {
    private Context context;
    private int resource;
    private DataBaseHelper dataBaseHelper;

    private String TAG = "Test";

    public UserArrayAdapter(Context context, int resource, ArrayList<String[]> objects, DataBaseHelper db) {
        super(context, resource, objects);
        this.context = context;
        this.dataBaseHelper = db;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String userID = Objects.requireNonNull(getItem(position))[0];
        String userName = Objects.requireNonNull(getItem(position))[1];
        String telephone = Objects.requireNonNull(getItem(position))[2];

        ArrayList<String[]> books = dataBaseHelper.getBorrowedBooksByUserID(Integer.parseInt(userID));
        ArrayList<String> bookList = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();
        for (String[] s: books) {
            String book = dataBaseHelper.getBookTitleByID(Integer.parseInt(s[0]));
            bookList.add(book);
            dateList.add(s[1]);
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView txvName = convertView.findViewById(R.id.txvUserCardName);
        TextView txvPhone = convertView.findViewById(R.id.txvUserCardTelephone);
        TextView txvBooks = convertView.findViewById(R.id.txvUserCardBooks);
        TextView txvDates = convertView.findViewById(R.id.txvUserCardDates);


        txvName.setText(userName);
        txvPhone.setText(telephone);
        txvBooks.setText( TextUtils.join("\n", bookList));
        txvDates.setText(TextUtils.join("\n",dateList));

        return convertView;
    }

}
