package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ChatActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.ChatMessageModel;
import com.example.myapplication.utils.AndroidUtils;
import com.example.myapplication.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessageModel, ChatRecyclerAdapter.ChatModelViewHolder> {

    Context context;
    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int i, @NonNull ChatMessageModel model) {
        holder.rightChatTextView.setText(model.getMessage());
        holder.leftChatTextView.setText(model.getMessage());
        if(model.getSenderId().equals(FirebaseUtils.currentUserId().toString())){
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatTextView.setText("cc");
        }else{
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.leftChatTextView.setText("cc");
        }
    }

    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row, parent, false);
        return new ChatModelViewHolder(view);
    }

    class ChatModelViewHolder extends RecyclerView.ViewHolder{

        LinearLayout leftChatLayout, rightChatLayout;
        TextView leftChatTextView, rightChatTextView;
        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextView = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextView = itemView.findViewById(R.id.right_chat_textview);
        }
    }
}
