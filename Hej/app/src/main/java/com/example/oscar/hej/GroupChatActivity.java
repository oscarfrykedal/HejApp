package com.example.oscar.hej;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oscar.hej.Adapter.GroupMessageAdapter;

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

    Toolbar mToolbar;
    ImageButton btn_send;
    EditText text_send;
    ScrollView mScrollView;
    TextView displayText;

    CircleImageView profile_image;
    TextView username;
    List<GroupChat> mchat;
    GroupMessageAdapter groupmessageAdapter;
    Intent intent;

    FirebaseAuth mAuth;
    DatabaseReference userRef, groupRef, groupMessageKeyRef;

    String currentGroupName, currentUserId, currentUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        if (Build.VERSION.SDK_INT >=21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        Toolbar toolbar = findViewById(R.id.toolbar_group_chat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send= findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        currentGroupName = getIntent().getExtras().get("groupName").toString();
        Toast.makeText(this, currentGroupName, Toast.LENGTH_SHORT).show();



        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName);

        intent = getIntent();
        final String userid =intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        InitializeFields();

        GetUserInfo();

        btn_send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if (!msg.equals("")){
                    sendMessage(fuser.getUid(), userid, msg);

                }else{
                    Toast.makeText(GroupChatActivity.this, "you need to write something", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
                //SendMessageToDatabase();

                text_send.setText("");
            }
        });


        reference = FirebaseDatabase.getInstance().getReference("Groups").child("julen 2018");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                GroupUser user = dataSnapshot.getValue(GroupUser.class);
                username.setText(user.getUsername());

                readMessage(fuser.getUid(),userid);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                    //DisplayMessage(dataSnapshot);

                }


            }



            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                User users = dataSnapshot.getValue(User.class);
                username.setText(users.getUsername());
                User user = dataSnapshot.getValue(User.class);
                if (dataSnapshot.exists());
                {
                    //DisplayMessage(dataSnapshot);

                }
                if (user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    Glide.with(GroupChatActivity.this).load(user.getImageURL()).into(profile_image);
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

        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        //displayText = findViewById(R.id.show_message_left);
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

/*
    private void SendMessageToDatabase()
    {
        String message = text_send.getText().toString();
        String messageKey = groupRef.push().getKey();
        if (TextUtils.isEmpty(message))
        {
            Toast.makeText(this, "You need to write a message...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, Object> groupMessageKey = new HashMap<>();
            groupRef.updateChildren(groupMessageKey);


            groupMessageKeyRef = groupRef.child(messageKey);

            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("name", currentUserName);
            hashMap.put("message", message);

            groupMessageKeyRef.updateChildren(hashMap);

        }

    }*//*
private void sendMessage(String sender, String receiver, String message)
{
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("sender", sender);
    hashMap.put("reciever", receiver);
    hashMap.put("message", message);

    reference.child("Groups").push().setValue(hashMap);
}*/
   private void sendMessage(String sender, String reciever,String message) {

        String message1 = text_send.getText().toString();
        String messageKey = groupRef.push().getKey();

        if (TextUtils.isEmpty(message1)) {
            Toast.makeText(this, "You need to write a message...", Toast.LENGTH_SHORT).show();
        } else {

            HashMap<String, Object> groupMessageKey = new HashMap<>();
            groupRef.updateChildren(groupMessageKey);

            groupMessageKeyRef = groupRef.child(messageKey);


            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("sender", currentUserName);
            hashMap.put("reciever", reciever);
            hashMap.put("message", message);


            groupMessageKeyRef.updateChildren(hashMap);
            reference.child("Groups").push().setValue(hashMap);
            //reference = FirebaseDatabase.getInstance().getReference("Groups").child("julen 2018");

        }


    }

        private void readMessage(final String myid, final String userid){
        mchat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    GroupChat groupchat = snapshot.getValue(GroupChat.class);
                        mchat.add(groupchat);

                    groupmessageAdapter = new GroupMessageAdapter(GroupChatActivity.this, mchat);
                    recyclerView.setAdapter(groupmessageAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

/*

    private void DisplayMessage(DataSnapshot dataSnapshot)
    {

        Iterator iterator = dataSnapshot.getChildren().iterator();

        while(iterator.hasNext())
        {
            //String chatDate = (String)((DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String)((DataSnapshot)iterator.next()).getValue();
            String chatName = (String)((DataSnapshot)iterator.next()).getValue();
            //String chatTime = (String)((DataSnapshot)iterator.next()).getValue();

            //displayText.append(chatName + ": " + chatMessage + "\n" +  "        " + "\n");



        }
    }
*/


}
