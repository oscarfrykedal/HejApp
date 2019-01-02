package com.example.oscar.hej.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oscar.hej.Chat;
import com.example.oscar.hej.GroupChat;
import com.example.oscar.hej.GroupChatActivity;
import com.example.oscar.hej.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import static com.example.oscar.hej.Adapter.GroupMessageAdapter.MSG_TYPE_LEFT;
import static com.example.oscar.hej.Adapter.GroupMessageAdapter.MSG_TYPE_RIGHT;

public class GroupMessageAdapter extends RecyclerView.Adapter<GroupMessageAdapter.ViewHolder> {

    public static final  int MSG_TYPE_LEFT = 0;
    public static final  int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<GroupChat> mChat;



    FirebaseUser fuser;


    public GroupMessageAdapter(Context mContext, List<GroupChat> mChat) {
        this.mChat = mChat;
        this.mContext = mContext;


    }




    @NonNull
    @Override
    public GroupMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent,false);
            return new GroupMessageAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent,false);
            return new GroupMessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        GroupChat groupchat = mChat.get(position);

        holder.show_message.setText(groupchat.getMessage());

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }

    //CHAT TEXT SIDE
    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
     /*   if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{*/
            return MSG_TYPE_LEFT;
        }
   // }
}


