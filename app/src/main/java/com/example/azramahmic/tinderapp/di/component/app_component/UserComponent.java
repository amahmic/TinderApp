package com.example.azramahmic.tinderapp.di.component.app_component;

import com.example.azramahmic.tinderapp.data.api.tinder_auth.UserCredentials;
import com.example.azramahmic.tinderapp.di.component.app_component.user_component.ChatComponent;
import com.example.azramahmic.tinderapp.di.component.app_component.user_component.ChatUsersComponent;
import com.example.azramahmic.tinderapp.di.component.app_component.user_component.FeedComponent;
import com.example.azramahmic.tinderapp.di.module.TinderNetworkModule;
import com.example.azramahmic.tinderapp.di.module.UserModule;
import com.example.azramahmic.tinderapp.di.scope.UserScope;

import dagger.BindsInstance;
import dagger.Subcomponent;

@Subcomponent(modules = {TinderNetworkModule.class, UserModule.class})
@UserScope
public interface UserComponent {

    FeedComponent.Builder plusFeedComponent();
    ChatUsersComponent.Builder plusChatUsersComponent();
    ChatComponent.Builder plusChatComponent();

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        Builder userCredentials(UserCredentials userCredentials);
        UserComponent build();
    }
}
