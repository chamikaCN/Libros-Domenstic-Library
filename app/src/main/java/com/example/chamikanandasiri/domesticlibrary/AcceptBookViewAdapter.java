package com.example.chamikanandasiri.domesticlibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AcceptBookViewAdapter extends RecyclerView.Adapter<AcceptBookViewAdapter.ViewHolder> {

    private ArrayList<String[]> bookDetails;
    private String userID;
    private Context context;
    private DataBaseHelper dataBaseHelper;
    private String TAG = "Test";

    public AcceptBookViewAdapter(Context context, ArrayList<String[]> objects, DataBaseHelper db, String userID) {
        this.dataBaseHelper = db;
        this.bookDetails = objects;
        this.context = context;
        this.userID = userID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_accept, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String bookID = bookDetails.get(position)[0];
        String receiptID = bookDetails.get(position)[3];
        holder.bookTitleView.setText(bookDetails.get(position)[1]);
        holder.dateView.setText(bookDetails.get(position)[2]);
        holder.acceptButton.setOnClickListener(view -> {
            boolean success = dataBaseHelper.updateReceiptCompletion(receiptID,bookID);
            if(success) {
                bookDetails.remove(position);
                notifyDataSetChanged();
                ((StorageActivity) Objects.requireNonNull(context)).updateTabs();
            }else{
                Toast.makeText(context,"cannot complete",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView bookTitleView, dateView;
        Button acceptButton;
        CardView acceptCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitleView = itemView.findViewById(R.id.txvAcceptCardTitle);
            dateView = itemView.findViewById(R.id.txvAcceptCardDate);
            acceptButton = itemView.findViewById(R.id.btnAcceptCardComplete);
            acceptCardView = itemView.findViewById(R.id.crdAcceptCard);
        }
    }
}
