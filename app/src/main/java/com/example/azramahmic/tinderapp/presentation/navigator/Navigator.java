package com.example.azramahmic.tinderapp.presentation.navigator;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;

import com.example.azramahmic.tinderapp.presentation.chat.ChatActivity;
import com.example.azramahmic.tinderapp.presentation.chat_users.ChatUsersActivity;
import com.example.azramahmic.tinderapp.presentation.feed.FeedActivity;
import com.example.azramahmic.tinderapp.presentation.login.LoginActivity;
import com.example.azramahmic.tinderapp.presentation.register.RegisterActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Navigator {

    @Inject
    public Navigator() {}

    public void navigateToLogin(Context context) {
        context.startActivity(LoginActivity.getCallingIntent(context));
    }

    public void navigateToRegister(Context context) {
        context.startActivity(RegisterActivity.getCallingIntent(context));
    }

    public void navigateToFeed(Context context) {
        context.startActivity(FeedActivity.getCallingIntent(context));
    }

    public void navigateToChatUsers(Context context) {
        context.startActivity(ChatUsersActivity.getCallingIntent(context));
    }

    public void navigateToChat(Context context, String userId) {
        context.startActivity(ChatActivity.getCallingIntent(context, userId));
    }

    public void navigateToChatWithBackStack(Context context, String userId) {
        // Create an Intent for the activity you want to start
        Intent chatActivity = ChatActivity.getCallingIntent(context, userId);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(chatActivity);
        stackBuilder.startActivities();

        /*TaskStackBuilder.create(ProfileSelectActivity.this)
                .addNextIntentWithParentStack(intent)
                .startActivities();*/
    }
}
