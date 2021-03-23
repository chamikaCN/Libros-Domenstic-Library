package com.example.chamikanandasiri.domesticlibrary;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

class UserArrayAdapter extends ArrayAdapter<String[]> {
    private Context context;
    private int resource;
    private DataBaseHelper dataBaseHelper;
    private Dialog dialog;
    private Button completeReceipt_closeButton, completeReceipt_okButton;
    private ListView completeReceipt_listView;
    private TextView completeReceipt_userView, completeReceipt_telephoneView;

    private String TAG = "Test";

    public UserArrayAdapter(Context context, int resource, ArrayList<String[]> objects, DataBaseHelper db, Dialog dialog) {
        super(context, resource, objects);
        this.context = context;
        this.dataBaseHelper = db;
        this.resource = resource;
        this.dialog = dialog;
        completeReceipt_userView = dialog.findViewById(R.id.txvCompleteReceiptName);
        completeReceipt_telephoneView = dialog.findViewById(R.id.txvCompleteReceiptTelephone);
        completeReceipt_listView = dialog.findViewById(R.id.lstCompleteReceipt);
        completeReceipt_closeButton = dialog.findViewById(R.id.btnCompleteReceiptClose);
        completeReceipt_okButton = dialog.findViewById(R.id.btnCompleteReceiptOK);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String userID = Objects.requireNonNull(getItem(position))[0];
        String userName = Objects.requireNonNull(getItem(position))[1];
        String telephone = Objects.requireNonNull(getItem(position))[2];

        ArrayList<String[]> books = dataBaseHelper.getBorrowedBooksByUserID(Integer.parseInt(userID));
        ArrayList<String[]> bookAcceptList = new ArrayList<>();
        ArrayList<String[]> bookList = new ArrayList<>();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView txvName = convertView.findViewById(R.id.txvUserCardName);
        TextView txvPhone = convertView.findViewById(R.id.txvUserCardTelephone);
        LinearLayout lloBooks = convertView.findViewById(R.id.lloUserBooks);

        for (String[] s : books) {
            String book = dataBaseHelper.getBookTitleByID(Integer.parseInt(s[0]));
            bookList.add(new String[]{book + " - " + s[1].substring(0, 10).trim(), s[2]});
            if (s[2].equals("0")) {
                bookAcceptList.add(new String[]{s[0], book, s[1], userID, s[2]});
            }
        }

        txvName.setText(userName);
        txvPhone.setText(telephone);

        for (int i = 0; i < bookList.size(); i++) {
            TextView child = (TextView) inflater.inflate(R.layout.listitem_user_subbook, null);
            child.setText(new StringBuilder().append(i + 1).append(".").append((bookList.get(i))[0]).toString());
            child.setTextColor(context.getResources().getColor(bookList.get(i)[1].equals("1") ? R.color.colorPrimaryDark : R.color.colorAccentDark));
            lloBooks.addView(child);
        }

        convertView.setOnClickListener(view -> {
            completeReceipt_userView.setText(userName);
            completeReceipt_telephoneView.setText(telephone);
            completeReceipt_closeButton.setOnClickListener(v2 -> dialog.dismiss());
            completeReceipt_okButton.setOnClickListener(v2 -> {
            });
            BookAcceptArrayAdapter adapter = new BookAcceptArrayAdapter(context, R.layout.listitem_userbookaccept, bookAcceptList, dataBaseHelper);
            completeReceipt_listView.setAdapter(adapter);
            dialog.show();

        });

        return convertView;
    }
}

