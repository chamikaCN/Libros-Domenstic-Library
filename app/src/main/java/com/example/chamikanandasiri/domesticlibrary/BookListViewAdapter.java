package com.example.chamikanandasiri.domesticlibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookListViewAdapter extends RecyclerView.Adapter<BookListViewAdapter.ViewHolder> {

    private ArrayList<String[]> bookDetails;
    private Context context;
    private String TAG = "Test";
    private boolean isCurrent;

    public BookListViewAdapter(Context context, ArrayList<String[]> objects, boolean isCurrent) {

        this.bookDetails = objects;
        this.context = context;
        this.isCurrent = isCurrent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_user_subbook, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String book = bookDetails.get(position)[0];
        String date = bookDetails.get(position)[1].substring(0, 10);
        String c = book + " - " + date;
        holder.bookTitleView.setText(isCurrent ? c : book);
        holder.bookTitleView.setTextColor(isCurrent ? context.getColor(R.color.colorAccentDark) : context.getColor(R.color.colorPrimary));
    }

    @Override
    public int getItemCount() {
        return bookDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView bookTitleView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitleView = itemView.findViewById(R.id.txvBookReceiptCardBook);
        }
    }
}
