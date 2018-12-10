package com.example.oscar.hej;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewChat extends AppCompatActivity {

    EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);


        editText = findViewById(R.id.editText);


        button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewChat.this, MainActivity.class);
                //intent.putExtra("chatname", editText.getText().toString()));
                startActivity(intent);


            }
        });

    }
}
