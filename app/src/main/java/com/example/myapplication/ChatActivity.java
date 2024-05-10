package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.adapter.ChatRecyclerAdapter;
import com.example.myapplication.adapter.SearchUserRecyclerAdapter;
import com.example.myapplication.model.ChatMessageModel;
import com.example.myapplication.model.ChatRoomModel;
import com.example.myapplication.model.UserModel;
import com.example.myapplication.utils.AndroidUtils;
import com.example.myapplication.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {

    UserModel otherUser;
    ChatRoomModel chatRoomModel;
    EditText messageInput;
    ImageButton sendMessageBtn;
    ImageButton BackBtn;
    TextView otherUsername;
    RecyclerView recyclerView;
    ChatRecyclerAdapter adapter;

    String chatRoomId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        otherUser = AndroidUtils.getUserModelFromIntent(getIntent());
        chatRoomId = FirebaseUtils.getChatRoomId(FirebaseUtils.currentUserId(), otherUser.getUserId());

        messageInput = findViewById(R.id.chat_message_input);
        sendMessageBtn = findViewById(R.id.message_send_btn);
        BackBtn = findViewById(R.id.back_btn);
        otherUsername = findViewById(R.id.other_username);
        recyclerView = findViewById(R.id.chat_recycler_view);

        BackBtn.setOnClickListener(v ->{
            onBackPressed();
        });

        sendMessageBtn.setOnClickListener((v ->{
            String message = messageInput.getText().toString().trim();
            if(message.isEmpty()){
                return;
            }
            sendMessageToUser(message);
        }));

        otherUsername.setText(otherUser.getUsername());

        getOrCreateChatRoomMode();
        setupRecyclerView();
    }

    private void setupRecyclerView() {

        Query query = FirebaseUtils.getChatRoomMessageReference(chatRoomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query,ChatMessageModel.class).build();

        adapter = new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    private void sendMessageToUser(String message) {
        chatRoomModel.setLastMessageSenderId(FirebaseUtils.currentUserId());
        chatRoomModel.setLasMessageTimestamp(Timestamp.now());
        FirebaseUtils.getChatRoomReference(chatRoomId).set(chatRoomModel);
        ChatMessageModel chatMessageModel = new ChatMessageModel(message, FirebaseUtils.currentUserId(), Timestamp.now());
        FirebaseUtils.getChatRoomMessageReference(chatRoomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            messageInput.setText("");
                        }
                    }
                });

    }

    void getOrCreateChatRoomMode() {
        FirebaseUtils.getChatRoomReference(chatRoomId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                chatRoomModel = task.getResult().toObject(ChatRoomModel.class);
                if(chatRoomModel==null){
                    chatRoomModel = new ChatRoomModel(
                            chatRoomId,
                            Arrays.asList(FirebaseUtils.currentUserId(), otherUser.getUserId()),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtils.getChatRoomReference(chatRoomId).set(chatRoomModel);

                }
            }
        });
    }
}