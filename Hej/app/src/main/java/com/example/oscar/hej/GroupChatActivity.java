package com.example.oscar.hej;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oscar.hej.Adapter.MessageAdapter;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Group;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatActivity extends AppCompatActivity {




    FirebaseUser fuser;
    DatabaseReference reference;


    RecyclerView recyclerView;


    private Toolbar mToolbar;
    private ImageButton sendMessageButton;
    private EditText userText;
    private ScrollView mScrollView;
    private TextView displayText;


    private FirebaseAuth mAuth;
    private DatabaseReference userRef, groupRef, groupMessageKeyRef;

    private String currentGroupName, currentUserId, currentUserName, currentDate, currentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        currentGroupName = getIntent().getExtras().get("groupName").toString();
        Toast.makeText(this, currentGroupName, Toast.LENGTH_SHORT).show();


        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName);

        InitializeFields();

        GetUserInfo();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessageToDatabase();

                userText.setText("");
            }
        });



    }





    @Override
    protected void onStart()
    {
        super.onStart();



        groupRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                User user = dataSnapshot.getValue(User.class);
                if (dataSnapshot.exists());
                {
                    assert user != null;
                    DisplayMessage(dataSnapshot);

                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                User user = dataSnapshot.getValue(User.class);
                if (dataSnapshot.exists());
                {
                    DisplayMessage(dataSnapshot);
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void InitializeFields()
    {
        mToolbar = findViewById(R.id.toolbar_group_chat);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(currentGroupName);

        sendMessageButton = findViewById(R.id.btn_send);
        userText = findViewById(R.id.text_send);
        displayText = findViewById(R.id.show_message_left);
        mScrollView = findViewById(R.id.my_scroll_view);


    }

    private void GetUserInfo()
    {
        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                    if (dataSnapshot.exists())
                    {
                        currentUserName = dataSnapshot.child("username").getValue().toString();

                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void SendMessageToDatabase()
    {
        String message = userText.getText().toString();
        String messageKey = groupRef.push().getKey();
        if (TextUtils.isEmpty(message))
        {
            Toast.makeText(this, "You need to write a message...", Toast.LENGTH_SHORT).show();
        }
        else
        {


            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentDate = currentDateFormat.format(calForDate.getTime());


            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormat.format(calForTime.getTime());


            HashMap<String, Object> groupMessageKey = new HashMap<>();
            groupRef.updateChildren(groupMessageKey);


            groupMessageKeyRef = groupRef.child(messageKey);

            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("name", currentUserName);
            hashMap.put("message", message);
            //hashMap.put("date", currentDate);
            //hashMap.put("time", currentTime);

            groupMessageKeyRef.updateChildren(hashMap);

        }

    }


    private void DisplayMessage(DataSnapshot dataSnapshot)
    {

        Iterator iterator = dataSnapshot.getChildren().iterator();

        while(iterator.hasNext())
        {
            //String chatDate = (String)((DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String)((DataSnapshot)iterator.next()).getValue();
            String chatName = (String)((DataSnapshot)iterator.next()).getValue();
            //String chatTime = (String)((DataSnapshot)iterator.next()).getValue();

            displayText.append(chatName + ": " + chatMessage + "\n" +  "        " + "\n");



        }
    }


}
