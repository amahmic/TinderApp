package com.example.azramahmic.tinderapp.di.component.app_component.user_component;

import com.example.azramahmic.tinderapp.di.module.ChatModule;
import com.example.azramahmic.tinderapp.presentation.chat.ChatActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ChatModule.class})
public interface ChatComponent {

    void inject(ChatActivity chatActivity);

    @Subcomponent.Builder
    interface Builder {
        ChatComponent build();
    }

}
