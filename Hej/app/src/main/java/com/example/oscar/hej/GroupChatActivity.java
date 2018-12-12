package com.example.oscar.hej;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.security.acl.Group;

public class GroupChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton sendMessageButton;
    private EditText userText;
    private ScrollView mScrollView;
    private TextView displayText;

    private String currentGroupName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);



        currentGroupName = getIntent().getExtras().get("groupName").toString();
        Toast.makeText(this, currentGroupName, Toast.LENGTH_SHORT).show();

        InitializeFields();



    }

    private void InitializeFields()
    {
        mToolbar = findViewById(R.id.toolbar_group_chat);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(currentGroupName);

        sendMessageButton = findViewById(R.id.btn_send_group_chat);
        userText = findViewById(R.id.text_send_group_chat);
        displayText = findViewById(R.id.group_chat_text);
        mScrollView = findViewById(R.id.my_scroll_view);

    }
}
