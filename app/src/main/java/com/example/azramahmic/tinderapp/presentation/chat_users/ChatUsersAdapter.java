package com.example.azramahmic.tinderapp.presentation.chat_users;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.azramahmic.tinderapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ChatUsersAdapter extends FirebaseRecyclerAdapter<ChatUser, ChatUsersAdapter.ChatUserViewHolder> {

    interface ChatUserClickListener {
        void onClick(ChatUser chatUser);
    }

    private ChatUserClickListener chatUserClickListener;

    public ChatUsersAdapter(@NonNull FirebaseRecyclerOptions<ChatUser> options, ChatUserClickListener chatUserClickListener) {
        super(options);
        this.chatUserClickListener = chatUserClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatUserViewHolder holder, int position, @NonNull ChatUser model) {
        Log.d("TAG", "Bind message: " + model);
        holder.bind(model);
    }

    @Override
    public ChatUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_item, parent, false);
        return new ChatUserViewHolder(v);
    }

    public class ChatUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View itemView;
        private TextView tv_user;

        public ChatUserViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.tv_user = itemView.findViewById(R.id.tv_chat_user);
            this.itemView.setOnClickListener(this);
        }

        public void bind(ChatUser chatUser) {
            this.tv_user.setText(chatUser.getEmail());
            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            chatUserClickListener.onClick(getItem(getAdapterPosition()));
        }
    }
}
