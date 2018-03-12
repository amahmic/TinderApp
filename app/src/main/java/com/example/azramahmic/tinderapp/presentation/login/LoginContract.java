package com.example.azramahmic.tinderapp.presentation.login;

import com.example.azramahmic.tinderapp.data.api.tinder_auth.model.UserAuthDetails;
import com.example.azramahmic.tinderapp.data.local.model.auth_user.TinderAppUser;
import com.example.azramahmic.tinderapp.presentation.base.BaseContract;
import com.google.firebase.auth.FirebaseUser;

public interface LoginContract extends BaseContract {

    interface View extends BaseContract.BaseView {
        void showProgress(boolean inProgress);
        void showError(boolean errorFromApi);
        void signInToFirebase(UserAuthDetails userAuthDetails);
        void updateUserInFirebaseDatabase(TinderAppUser tinderAppUser, FirebaseUser firebaseUser);
        void loggedIn(TinderAppUser tinderAppUser, String token);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void login(UserAuthDetails userAuthDetails);
        void signInToFirebaseUserSuccessful(FirebaseUser firebaseUser, UserAuthDetails userAuthDetails);
        void signInToFirebaseUserUnsuccessful(UserAuthDetails userAuthDetails, Exception exception);
    }
}
