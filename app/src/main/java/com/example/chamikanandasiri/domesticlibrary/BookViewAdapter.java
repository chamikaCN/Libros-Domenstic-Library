package com.example.chamikanandasiri.domesticlibrary;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class BookViewAdapter extends RecyclerView.Adapter<BookViewAdapter.ViewHolder> {

    private ArrayList<String[]> bookDetails;
    private Context context;
    private DataBaseHelper dataBaseHelper;
    private String TAG = "Test";

    public BookViewAdapter(Context context, ArrayList<String[]> objects, DataBaseHelper db) {
        this.dataBaseHelper = db;
        this.bookDetails = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String bookID = bookDetails.get(position)[0];
        holder.bookIDView.setText(bookID);
        holder.bookTitleView.setText(bookDetails.get(position)[1]);
        holder.bookAuthorView.setText(bookDetails.get(position)[2]);

        String availability = bookDetails.get(position)[3];
        holder.bookAvailableView.setText(availability.equals("1") ?"Available":dataBaseHelper.getBorrowedUserByBookID(Integer.parseInt(bookID)));
        holder.bookCardView.setOnClickListener(view -> ((StorageActivity)context).showViewBookPopup(Integer.parseInt(bookID)));

    }

    @Override
    public int getItemCount() {
        return bookDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView bookIDView, bookTitleView, bookAuthorView, bookAvailableView;
        CardView bookCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCardView = itemView.findViewById(R.id.crdBookCard);
            bookIDView = itemView.findViewById(R.id.txvBookCardID);
            bookTitleView = itemView.findViewById(R.id.txvBookCardTitle);
            bookAuthorView = itemView.findViewById(R.id.txvBookCardAuthor);
            bookAvailableView = itemView.findViewById(R.id.txvBookCardAvailable);
        }
    }
}
