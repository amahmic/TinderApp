package com.example.azramahmic.tinderapp.di.component.app_component.user_component;

import com.example.azramahmic.tinderapp.di.module.ChatUsersModule;
import com.example.azramahmic.tinderapp.presentation.chat_users.ChatUsersActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ChatUsersModule.class})
public interface ChatUsersComponent {

    void inject(ChatUsersActivity chatUsersActivity);

    @Subcomponent.Builder
    interface Builder {
        ChatUsersComponent build();
    }
}
