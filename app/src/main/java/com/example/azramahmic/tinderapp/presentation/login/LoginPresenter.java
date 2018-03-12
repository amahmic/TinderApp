package com.example.azramahmic.tinderapp.presentation.login;

import com.example.azramahmic.tinderapp.data.api.tinder_auth.TinderAuthService;
import com.example.azramahmic.tinderapp.data.api.tinder_auth.model.AuthData;
import com.example.azramahmic.tinderapp.data.api.tinder_auth.model.UserAuthDetails;
import com.example.azramahmic.tinderapp.data.local.TinderAppUserLocalStore;
import com.example.azramahmic.tinderapp.data.local.model.auth_user.TinderAppUser;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter {

    private TinderAuthService tinderAuthService;
    private TinderAppUserLocalStore tinderAppUserLocalStore;
    private CompositeDisposable compositeDisposable;

    private LoginContract.View loginView;
    private UserAuthDetails userAuthDetails;

    @Inject
    public LoginPresenter(CompositeDisposable compositeDisposable, TinderAuthService tinderAuthService, TinderAppUserLocalStore tinderAppUserLocalStore) {
        this.compositeDisposable = compositeDisposable;
        this.tinderAuthService = tinderAuthService;
        this.tinderAppUserLocalStore = tinderAppUserLocalStore;
    }

    @Override
    public void attach(LoginContract.View view) {
        loginView = view;
    }

    @Override
    public void login(UserAuthDetails userAuthDetails) {
        this.userAuthDetails = userAuthDetails;
        loginView.showProgress(true);
        //First, sign in to firebase
        loginView.signInToFirebase(userAuthDetails);
    }

    @Override
    public void signInToFirebaseUserSuccessful(FirebaseUser firebaseUser, UserAuthDetails userAuthDetails) {
        //And then login to api
        compositeDisposable.add(tinderAuthService.login(userAuthDetails)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authResponse -> {
                            loginView.showProgress(false);
                            if (authResponse.getErrorData() != null) {
                                loginView.showError(true);
                            } else {
                                AuthData authData = authResponse.getAuthData();
                                TinderAppUser tinderAppUser = new TinderAppUser(userAuthDetails.email, firebaseUser.getUid());
                                tinderAppUserLocalStore.insertTinderAppUser(tinderAppUser);
                                loginView.updateUserInFirebaseDatabase(tinderAppUser, firebaseUser);
                                loginView.loggedIn(tinderAppUser, authData.getToken());
                            }
                        },
                        throwable -> {
                            loginView.showProgress(false);
                            loginView.showError(false);
                        }));
    }

    @Override
    public void signInToFirebaseUserUnsuccessful(UserAuthDetails userAuthDetails, Exception exception) {
        loginView.showProgress(false);
        if (exception instanceof FirebaseNetworkException) {
            loginView.showError(false);
        } else {
            loginView.showError(true);
        }
    }

    @Override
    public void detach() {
        compositeDisposable.dispose();
        loginView = null;
    }
}
