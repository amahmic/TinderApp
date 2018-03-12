package com.example.azramahmic.tinderapp.presentation.chat_users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.azramahmic.tinderapp.R;
import com.example.azramahmic.tinderapp.presentation.TinderApp;
import com.example.azramahmic.tinderapp.presentation.base.BaseActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatUsersActivity extends BaseActivity implements ChatUsersAdapter.ChatUserClickListener {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ChatUsersActivity.class);
    }

    @BindView(R.id.rv_chat_users)
    RecyclerView rv_chatUsers;

    private ChatUsersAdapter chatUsersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_users_activity);
        ButterKnife.bind(this);

        rv_chatUsers.setLayoutManager(new LinearLayoutManager(this));
        rv_chatUsers.setItemAnimator(new DefaultItemAnimator());
        rv_chatUsers.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("users");

        FirebaseRecyclerOptions<ChatUser> options = new FirebaseRecyclerOptions.Builder<ChatUser>()
                        .setQuery(query, ChatUser.class)
                        .build();

        chatUsersAdapter = new ChatUsersAdapter(options, this);
        rv_chatUsers.setAdapter(chatUsersAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        chatUsersAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatUsersAdapter.stopListening();
    }

    @Override
    public void setupActivityComponent() {
        TinderApp.get(this)
                .userComponent()
                .plusChatUsersComponent()
                .build()
                .inject(this);
    }

    @Override
    public void onClick(ChatUser chatUser) {
        navigator.navigateToChat(this, chatUser.getUid());
    }
}
