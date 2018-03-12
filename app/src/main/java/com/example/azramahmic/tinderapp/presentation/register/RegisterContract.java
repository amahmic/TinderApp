package com.example.azramahmic.tinderapp.presentation.register;

import com.example.azramahmic.tinderapp.data.api.tinder_auth.model.UserAuthDetails;
import com.example.azramahmic.tinderapp.data.local.model.auth_user.TinderAppUser;
import com.example.azramahmic.tinderapp.presentation.base.BaseContract;
import com.google.firebase.auth.FirebaseUser;

public interface RegisterContract extends BaseContract {

    interface View extends BaseView {
        void showProgress(boolean inProgress);
        void createFirebaseUser(UserAuthDetails userAuthDetails);
        void showError(boolean errorFromApi);
        void addUserToFirebaseDatabase(TinderAppUser tinderAppUser);
        void deleteUserFromFirebase(FirebaseUser firebaseUser, boolean errorFromApi);
        void registered(TinderAppUser tinderAppUser, String token);
    }

    interface Presenter extends BasePresenter<View> {
        void register(UserAuthDetails userAuthDetails);
        void createFirebaseUserSuccessful(FirebaseUser firebaseUser, UserAuthDetails userAuthDetails);
        void createFirebaseUserUnsuccessful(UserAuthDetails userAuthDetails, Exception exception);
        void deleteUserFromFirebaseSuccessful(boolean errorFromApi);
    }
}
