package com.example.chamikanandasiri.domesticlibrary;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

public class UserViewAdapter extends RecyclerView.Adapter<UserViewAdapter.ViewHolder> {

    private ArrayList<String[]> userDetails;
    private Context context;
    private DataBaseHelper dataBaseHelper;
    private String TAG = "Test";

    public UserViewAdapter(Context context, ArrayList<String[]> objects, DataBaseHelper db) {
        this.dataBaseHelper = db;
        this.userDetails = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.userNameView.setText(userDetails.get(position)[1]);
        holder.userTelephoneView.setText(userDetails.get(position)[2]);
        String userID = userDetails.get(position)[0];
        ArrayList<String[]> books = dataBaseHelper.getBorrowedBooksByUserID(Integer.parseInt(userID));
        int bookCount = books.size();
        String countText = bookCount + " Books";
        holder.userCountView.setText(countText);
        if (bookCount <= 0) {
            holder.userExpandButton.setVisibility(View.GONE);
        } else {
            holder.userExpandButton.setOnClickListener(view -> {
                if (holder.userExpandableCard.getVisibility() == View.GONE) {
                    holder.userExpandButton.setBackgroundResource(R.drawable.ic_arrow_up);
                    holder.userExpandableCard.setVisibility(View.VISIBLE);
                } else {
                    holder.userExpandButton.setBackgroundResource(R.drawable.ic_arrow_down);
                    holder.userExpandableCard.setVisibility(View.GONE);
                    //TransitionManager.beginDelayedTransition(holder.userCardView,new AutoTransition());
                }
            });
        }


        ArrayList<String[]> bookAcceptList = new ArrayList<>();
        ArrayList<String[]> bookList = new ArrayList<>();

        for (String[] s : books) {
            String book = dataBaseHelper.getBookTitleByID(Integer.parseInt(s[0]));
            bookList.add(new String[]{book + " - " + s[1].substring(0, 10).trim(), s[2]});
            if (s[2].equals("0")) {
                bookAcceptList.add(new String[]{s[0], book, s[1], userID, s[2]});
            }
        }
        holder.userBooksList.removeAllViews();
        for (int i = 0; i < bookList.size(); i++) {
            TextView child = (TextView) LayoutInflater.from(context).inflate(R.layout.listitem_user_subbook, holder.userBooksList, false);
            child.setText(new StringBuilder().append(i + 1).append(".").append((bookList.get(i))[0]).toString());
            child.setTextColor(context.getResources().getColor(bookList.get(i)[1].equals("1") ? R.color.colorPrimaryDark : R.color.colorAccentDark));
            holder.userBooksList.addView(child);
        }
//        holder.textView.setTextColor(holder.itemView.isSelected() ? context.getResources().getColor(R.color.commonAccentText) : context.getResources().getColor(R.color.commonPrimaryText));
//        holder.animatedView.setColorFilter(holder.itemView.isSelected() ? context.getResources().getColor(R.color.commonAccentText) : context.getResources().getColor(R.color.commonPrimaryText));
//        holder.cardView.setCardBackgroundColor(holder.itemView.isSelected() ? context.getResources().getColor(R.color.commonAccent) : context.getResources().getColor(R.color.commonPrimary));
//
//        holder.animatedView.setVisibility(downloadedContent.get(position).isAnimated() ? View.VISIBLE : View.GONE);
//
//        holder.textView.setText(downloadedContent.get(position).getContName());
//
//        holder.itemView.setOnClickListener(view -> {
//            if (position != selectedPos) {
//                notifyItemChanged(selectedPos);
//                selectedPos = holder.getLayoutPosition();
//                notifyItemChanged(selectedPos);
//                ArViewActivity.setSelectedARModel(downloadedContent.get(position).getFile());
//            } else {
//                notifyItemChanged(selectedPos);
//                selectedPos = RecyclerView.NO_POSITION;
//                ArViewActivity.setSelectedARModel(null);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return userDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userNameView, userTelephoneView, userCountView;
        Button userEditButton, userExpandButton;
        ConstraintLayout userExpandableCard;
        CardView userCardView;
        LinearLayout userBooksList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userCardView = itemView.findViewById(R.id.crdUserCard);
            userNameView = itemView.findViewById(R.id.txvUserCardName);
            userTelephoneView = itemView.findViewById(R.id.txvUserCardTelephone);
            userCountView = itemView.findViewById(R.id.txvUserCardCount);
            userEditButton = itemView.findViewById(R.id.btnUserCardEdit);
            userExpandButton = itemView.findViewById(R.id.btnUserCardExpand);
            userExpandableCard = itemView.findViewById(R.id.cloUserCardExpandableLayout);
            userBooksList = itemView.findViewById(R.id.lloUserBooks);
        }
    }
}
