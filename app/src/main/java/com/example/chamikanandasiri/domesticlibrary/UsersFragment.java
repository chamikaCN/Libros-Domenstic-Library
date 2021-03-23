package com.example.chamikanandasiri.domesticlibrary;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UsersFragment extends Fragment {

    private DataBaseHelper dataBaseHelper;
    private View view;
    private TextView completeReceipt_userView, completeReceipt_telephoneView;
    private ListView Users_listView, completeReceipt_listView;
    private EditText Users_searchText;
    private ImageButton Users_searchButton,Users_resetButton;
    private Button completeReceipt_closeButton, completeReceipt_okButton;
    private Dialog Users_completeReceiptPopoup;
    private ArrayList<String[]> userDetails;
    private RecyclerView Users_recyclerView;
    private UserViewAdapter userViewAdapter;

    private String TAG = "Test";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_users, container, false);
        Users_recyclerView = view.findViewById(R.id.recyclerViewUser);
        Users_searchButton = view.findViewById(R.id.btnUserSearch);
        Users_searchText = view.findViewById(R.id.edtUserSearch);
        Users_searchButton.setOnClickListener(this::SearchName);
        Users_resetButton = view.findViewById(R.id.btnUserReset);
        Users_resetButton.setOnClickListener(view1 -> {
            LoadAllUsers();
            Users_searchText.setText("");
            Users_resetButton.setVisibility(View.GONE);
        });
        Users_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                ((StorageActivity) Objects.requireNonNull(getActivity())).controlFABVisibility(dy);
            }
        });
        LoadAllUsers();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBaseHelper = new DataBaseHelper(getActivity());
        Users_completeReceiptPopoup = new Dialog(Objects.requireNonNull(getActivity()));
        Users_completeReceiptPopoup.setContentView(R.layout.popup_completereceipt);
        //setupCompleteReceiptPopup();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((StorageActivity) Objects.requireNonNull(getActivity())).setUserFragment(this);
    }

    public void dataSetUpdate() {
        userDetails.clear();
        userDetails.addAll(dataBaseHelper.getAllUserDetails());
        userViewAdapter.notifyDataSetChanged();
    }

//    public void setupCompleteReceiptPopup(){
//        completeReceipt_userView = Users_completeReceiptPopoup.findViewById(R.id.txvCompleteReceiptName);
//        completeReceipt_telephoneView = Users_completeReceiptPopoup.findViewById(R.id.txvCompleteReceiptTelephone);
//        completeReceipt_listView = Users_completeReceiptPopoup.findViewById(R.id.lstCompleteReceipt);
//        completeReceipt_closeButton = Users_completeReceiptPopoup.findViewById(R.id.btnCompleteReceiptClose);
//        completeReceipt_okButton = Users_completeReceiptPopoup.findViewById(R.id.btnCompleteReceiptOK);
//    }
//
//    public void showCompleteReceiptPopup(View v1) {
//        completeReceipt_closeButton.setOnClickListener(v2 -> Users_completeReceiptPopoup.dismiss());
//        completeReceipt_okButton.setOnClickListener(v2 -> {});
//        Users_completeReceiptPopoup.show();
//    }

    public void SearchName(View v1) {
        String q = Users_searchText.getText().toString();
        if (!q.equals("") && q.length() > 1) {
            userDetails = dataBaseHelper.getSimilarUserDetails(q);
            userListGenerate();
            Users_resetButton.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(getActivity(),"Search query is not long enough",Toast.LENGTH_SHORT).show();
        }
    }

    public static void ListClick(String id) {
        Log.d("test", "ListClick: " + id);
    }

    private void LoadAllUsers() {
        userDetails = dataBaseHelper.getAllUserDetails();
        userListGenerate();
    }


    private void userListGenerate() {
//        UserArrayAdapter adapter = new UserArrayAdapter(getActivity(), R.layout.listitem_user, userDetails, dataBaseHelper, Users_completeReceiptPopoup);
//        Users_listView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        Users_recyclerView.setLayoutManager(layoutManager);
        userViewAdapter = new UserViewAdapter(getActivity(), userDetails, dataBaseHelper);
        Users_recyclerView.setAdapter(userViewAdapter);
    }
}
