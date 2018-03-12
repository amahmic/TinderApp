package com.example.azramahmic.tinderapp.presentation.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.azramahmic.tinderapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ChatMessagesAdapter extends FirebaseRecyclerAdapter<ChatMessage, ChatMessagesAdapter.ChatMessageViewHolder> {

    private String tinderAppUserUid;

    public ChatMessagesAdapter(@NonNull FirebaseRecyclerOptions<ChatMessage> options, String tinderAppUserUid) {
        super(options);
        this.tinderAppUserUid = tinderAppUserUid;
    }

    @Override
    public ChatMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ChatMessageViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position, @NonNull ChatMessage model) {
        holder.bind(model);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getSenderId().equals(tinderAppUserUid)) {
            return R.layout.chat_message_item_sent;
        } else {
            return R.layout.chat_message_item_received;
        }
    }

    public static class ChatMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_message;
        private TextView tv_sender;

        public ChatMessageViewHolder(View itemView) {
            super(itemView);
            tv_message = itemView.findViewById(R.id.tv_chat_message);
            tv_sender = itemView.findViewById(R.id.tv_chat_message_sender);
        }

        public void bind(ChatMessage chatMessage) {
            tv_message.setText(chatMessage.getTextMessage());
            tv_sender.setText(chatMessage.getSenderId());
        }
    }
}
