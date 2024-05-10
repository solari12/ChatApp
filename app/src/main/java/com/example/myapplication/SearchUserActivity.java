package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.adapter.SearchUserRecyclerAdapter;
import com.example.myapplication.model.UserModel;
import com.example.myapplication.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;

public class SearchUserActivity extends AppCompatActivity {

    EditText searchInput;
    ImageButton searchButton;
    ImageButton backButton;
    RecyclerView recyclerView;

    SearchUserRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        searchInput = findViewById(R.id.search_username_input);
        searchButton = findViewById(R.id.search_user_btn);
        backButton = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.search_user_recycler_view);

        searchInput.requestFocus();

        backButton.setOnClickListener(v ->{
            onBackPressed();
        });

        searchButton.setOnClickListener(v -> {
            String searchTerm = searchInput.getText().toString();
//            if(searchTerm.isEmpty() || searchTerm.length() < ){
//                searchInput.setError("Invaild Username");
//                return;
//            }
            setupSearchRecyclerView(searchTerm);
        });
    }

    private void setupSearchRecyclerView(String searchTerm) {

        Query query = FirebaseUtils.allUserCollectionReference().whereGreaterThanOrEqualTo("username",searchTerm);

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query,UserModel.class).build();

        adapter = new SearchUserRecyclerAdapter(options,getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.startListening();
        }
    }
}