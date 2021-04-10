package com.example.chamikanandasiri.domesticlibrary;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserViewAdapter extends RecyclerView.Adapter<UserViewAdapter.ViewHolder> {

    private ArrayList<String[]> userDetails;
    private Context context;
    private DataBaseHelper dataBaseHelper;
    private String TAG = "Test";
    private BookListViewAdapter currentBookListViewAdapter, previousBookListViewAdapter;

    public UserViewAdapter(Context context, ArrayList<String[]> objects, DataBaseHelper db) {
        this.dataBaseHelper = db;
        this.userDetails = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user, parent, false);
        Log.d(TAG, "onCreateViewHolder: view called");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.userNameView.setText(userDetails.get(position)[1]);
        holder.userTelephoneView.setText(userDetails.get(position)[2]);
        String userID = userDetails.get(position)[0];
        Log.d(TAG, "onCreateViewHolder: bind called");
        ArrayList<String[]> books = dataBaseHelper.getBorrowedBooksByUserID(Integer.parseInt(userID));
        int bookCount = books.size();
        ArrayList<String[]> currentBooks = new ArrayList<>();
        ArrayList<String[]> previousBooks = new ArrayList<>();

        for (String[] s : books) {
            String book = dataBaseHelper.getBookTitleByID(Integer.parseInt(s[0]));
            if (s[2].equals("1")) {
                previousBooks.add(new String[]{book,s[1]});
            }else{
                currentBooks.add(new String[]{book,s[1]});
            }
        }

        LinearLayoutManager childLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.userBooksCurrentList.setLayoutManager(childLayoutManager1);
        currentBookListViewAdapter= new BookListViewAdapter(context, currentBooks,true);
        holder.userBooksCurrentList.setAdapter(currentBookListViewAdapter);

        LinearLayoutManager childLayoutManager2 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.userBooksPreviousList.setLayoutManager(childLayoutManager2);
        previousBookListViewAdapter= new BookListViewAdapter(context, previousBooks,false);
        holder.userBooksPreviousList.setAdapter(previousBookListViewAdapter);

        String countText = bookCount + " Books";
        holder.userCountView.setText(countText);
        if (bookCount <= 0) {
            holder.userExpandButton.setVisibility(View.GONE);
        } else {
            holder.userExpandButton.setOnClickListener(view -> {
                if (holder.userExpandableCard.getVisibility() == View.GONE) {
                    holder.userExpandButton.setBackgroundResource(R.drawable.ic_arrow_up);
                    holder.userExpandableCard.setVisibility(View.VISIBLE);
                    holder.userBooksCurrentList.setVisibility(View.VISIBLE);
                } else {
                    holder.userExpandButton.setBackgroundResource(R.drawable.ic_arrow_down);
                    holder.userExpandableCard.setVisibility(View.GONE);
                    //TransitionManager.beginDelayedTransition(holder.userCardView,new AutoTransition());
                }
            });
        }
        holder.userCardView.setOnClickListener(view -> ((StorageActivity) context).showViewUserPopup(Integer.parseInt(userID)));

    }
    public  void updateData(){
        currentBookListViewAdapter.notifyDataSetChanged();
        previousBookListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return userDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userNameView, userTelephoneView, userCountView;
        Button userExpandButton;
        ConstraintLayout userExpandableCard;
        CardView userCardView;
        RecyclerView userBooksCurrentList,userBooksPreviousList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userCardView = itemView.findViewById(R.id.crdUserCard);
            userNameView = itemView.findViewById(R.id.txvUserCardName);
            userTelephoneView = itemView.findViewById(R.id.txvUserCardTelephone);
            userCountView = itemView.findViewById(R.id.txvUserCardCount);
            userExpandButton = itemView.findViewById(R.id.btnUserCardExpand);
            userExpandableCard = itemView.findViewById(R.id.cloUserCardExpandableLayout);
            userBooksCurrentList = itemView.findViewById(R.id.recyclerViewCurrentBookList);
            userBooksPreviousList = itemView.findViewById(R.id.recyclerViewPreviousBookList);
        }
    }


}
