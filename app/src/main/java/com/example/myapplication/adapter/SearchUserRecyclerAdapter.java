package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ChatActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.UserModel;
import com.example.myapplication.utils.AndroidUtils;
import com.example.myapplication.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUserRecyclerAdapter.UserModelViewHolder> {

    Context context;
    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder userModelViewHolder, int i, @NonNull UserModel userModel) {
        userModelViewHolder.usernameText.setText(userModel.getUsername());
        userModelViewHolder.phoneText.setText(userModel.getPhone());
        if(userModel.getUserId().equals(FirebaseUtils.currentUserId())){
            userModelViewHolder.usernameText.setText(userModel.getUsername()+ " (Tôi)");
        }

        userModelViewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            AndroidUtils.passUserModelAsIntent(intent, userModel);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.search_recycler_row, parent, false);
        return new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder{
        TextView usernameText;
        TextView phoneText;
        ImageView profilePic;
        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.username_text);
            phoneText = itemView.findViewById(R.id.phone_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image);
        }
    }
}
