package com.example.chamikanandasiri.domesticlibrary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UsersFragment extends Fragment {

    DataBaseHelper dataBaseHelper;
    View view;
    ListView Users_listView;
    EditText Users_searchText;
    Button Users_searchButton;
    ArrayList<String[]> userDetails;

    private String TAG = "Test";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_users,container,false);
        Users_listView = view.findViewById(R.id.lstUsers);
        Users_searchButton = view.findViewById(R.id.btnUserSearch);
        Users_searchText = view.findViewById(R.id.edtUserSearch);
        Users_searchButton.setOnClickListener(this::SearchName);
        LoadAllUsers();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBaseHelper = new DataBaseHelper(getActivity());
    }

    public void SearchName(View v1) {
        String q = Users_searchText.getText().toString();
        if (!q.equals("") && q.length() > 1) {
            userDetails = dataBaseHelper.getSimilarUserDetails(q);
            userListGenerate();
        } else {
            LoadAllUsers();
        }
    }

    private void LoadAllUsers() {
        userDetails = dataBaseHelper.getAllUserDetails();
        userListGenerate();
    }


    private void userListGenerate() {
        UserArrayAdapter adapter = new UserArrayAdapter(getActivity(), R.layout.listitem_user, userDetails,dataBaseHelper);
        Users_listView.setAdapter(adapter);
    }
}
