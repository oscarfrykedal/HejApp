package com.example.oscar.hej.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oscar.hej.Adapter.UserAdapter;
import com.example.oscar.hej.Chat;
import com.example.oscar.hej.LoginActivity;
import com.example.oscar.hej.NewChat;
import com.example.oscar.hej.R;
import com.example.oscar.hej.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class GroupChatsFragment extends Fragment {

    private FloatingActionButton fab;

    Button logoutButton;

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<User> mUser;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<String> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groupchats,container, false);

        logoutButton = view.findViewById(R.id.button1);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestNewGroup();

            }
        });


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        userList = new ArrayList<>();




        return view;
    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
        builder.setTitle("Enter group name :");

        final EditText groupNameField = new EditText(getActivity());
        groupNameField.setHint("e.g Family chat");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String groupName = groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName))
                {
                    Toast.makeText(getActivity(), "Enter a group name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    CreateNewGroup(groupName);
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which)
            {
                dialogInterface.cancel();

            }
        });

        builder.show();
    }

    private void CreateNewGroup(final String groupName)
    {
        reference = FirebaseDatabase.getInstance().getReference("Groups");

        reference.child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful())
                   {
                       Toast.makeText(getActivity(), groupName + " group is created", Toast.LENGTH_SHORT).show();
                   }

                   }
                });

    }



}

