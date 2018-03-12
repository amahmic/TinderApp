package com.example.azramahmic.tinderapp.presentation.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.azramahmic.tinderapp.R;
import com.example.azramahmic.tinderapp.data.local.ForegroundChatUserStore;
import com.example.azramahmic.tinderapp.data.local.TinderAppUserLocalStore;
import com.example.azramahmic.tinderapp.data.local.model.ForegroundChatUser;
import com.example.azramahmic.tinderapp.data.local.model.auth_user.TinderAppUser;
import com.example.azramahmic.tinderapp.presentation.TinderApp;
import com.example.azramahmic.tinderapp.presentation.base.BaseActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class ChatActivity extends BaseActivity {

    private static final String USER_ID = "user_id";

    public static Intent getCallingIntent(Context context, String userId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    @BindView(R.id.rv_messages)
    RecyclerView rv_messages;
    @BindView(R.id.et_chat_message)
    EditText et_chatMessage;
    @BindView(R.id.bt_send_chat_message)
    Button bt_sendMessage;

    private DatabaseReference messagesDatabaseReference;
    private DatabaseReference currentUserThreadReference;
    private DatabaseReference selectedUserThreadReference;
    private ChatMessagesAdapter chatMessagesAdapter;

    @Inject
    TinderAppUserLocalStore tinderAppUserLocalStore;
    @Inject
    ForegroundChatUserStore foregroundChatUserStore;

    @Inject
    CompositeDisposable compositeDisposable;

    boolean isCurrentUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        ButterKnife.bind(this);

        Observable<Boolean> messageObservable = RxTextView.textChanges(et_chatMessage)
                .map(inputText -> inputText == null || "".equals(inputText.toString().trim()))
                .distinctUntilChanged();

        compositeDisposable.add(messageObservable.subscribe(empty -> bt_sendMessage.setEnabled(!empty)));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rv_messages.setLayoutManager(linearLayoutManager);
        rv_messages.setItemAnimator(new DefaultItemAnimator());

        TinderAppUser tinderAppUser = tinderAppUserLocalStore.getTinderAppUser();
        String currentUser = tinderAppUser.getUid();
        String selectedUserId = getIntent().getStringExtra(USER_ID);

        foregroundChatUserStore.insertCurrentChat(new ForegroundChatUser(selectedUserId));

        isCurrentUser = currentUser.equals(selectedUserId);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        messagesDatabaseReference = firebaseDatabase.getReference()
                .child("messages");

        currentUserThreadReference = firebaseDatabase.getReference()
                .child("users")
                .child(currentUser)
                .child(selectedUserId)
                .child("messages");

        selectedUserThreadReference = firebaseDatabase.getReference()
                .child("users")
                .child(selectedUserId)
                .child(currentUser)
                .child("messages");

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(currentUser)
                .child(selectedUserId)
                .child("messages");

        FirebaseRecyclerOptions<ChatMessage> options = new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .build();

        chatMessagesAdapter = new ChatMessagesAdapter(options, tinderAppUser.getUid());
        chatMessagesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                Log.d("TEST", "Item count: " + itemCount);
                Log.d("TEST", "Item count: " + chatMessagesAdapter.getItemCount());
                int friendlyMessageCount = chatMessagesAdapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    rv_messages.scrollToPosition(positionStart);
                }
            }
        });
        rv_messages.setAdapter(chatMessagesAdapter);

        compositeDisposable.add(RxView.clicks(bt_sendMessage)
                .subscribe(v -> {
                    ChatMessage chatMessage = new ChatMessage(et_chatMessage.getText().toString(),
                            tinderAppUser.getEmail(),
                            tinderAppUser.getUid(),
                            selectedUserId);
                    sendMessage(chatMessage);
                }));
    }

    @Override
    protected void onStart() {
        super.onStart();
        chatMessagesAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatMessagesAdapter.stopListening();
    }

    @Override
    public void setupActivityComponent() {
        TinderApp.get(this).userComponent()
                .plusChatComponent()
                .build()
                .inject(this);
    }

    private void sendMessage(ChatMessage chatMessage) {
        messagesDatabaseReference.push().setValue(chatMessage);
        currentUserThreadReference.push().setValue(chatMessage);
        if (!isCurrentUser) {
            selectedUserThreadReference.push().setValue(chatMessage);
        }
        // Clear input box
        et_chatMessage.setText("");
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        foregroundChatUserStore.clear();
        super.onDestroy();
    }
}
