package com.example.azramahmic.tinderapp.presentation.register;

import android.util.Log;

import com.example.azramahmic.tinderapp.data.api.tinder_auth.TinderAuthService;
import com.example.azramahmic.tinderapp.data.api.tinder_auth.model.UserAuthDetails;
import com.example.azramahmic.tinderapp.data.api.tinder_auth.model.AuthData;
import com.example.azramahmic.tinderapp.data.local.TinderAppUserLocalStore;
import com.example.azramahmic.tinderapp.data.local.model.auth_user.TinderAppUser;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterPresenter implements RegisterContract.Presenter {

    private CompositeDisposable compositeDisposable;
    private TinderAuthService tinderAuthService;
    private TinderAppUserLocalStore tinderAppUserLocalStore;

    private RegisterContract.View registerView;

    @Inject
    public RegisterPresenter(CompositeDisposable compositeDisposable, TinderAuthService tinderAuthService, TinderAppUserLocalStore tinderAppUserLocalStore) {
        this.compositeDisposable = compositeDisposable;
        this.tinderAuthService = tinderAuthService;
        this.tinderAppUserLocalStore = tinderAppUserLocalStore;
    }

    @Override
    public void attach(RegisterContract.View view) {
        registerView = view;
    }

    @Override
    public void register(UserAuthDetails userAuthDetails) {
        Log.d("TEST", "register: " + userAuthDetails.toString());
        registerView.showProgress(true);
        //First try to sign in user to firebase
        registerView.createFirebaseUser(userAuthDetails);
    }

    @Override
    public void createFirebaseUserSuccessful(FirebaseUser firebaseUser, UserAuthDetails userAuthDetails) {
        //And then login to api
        Log.d("TEST", "createFirebaseUserSuccessful: " + firebaseUser.toString());
        Log.d("TEST", "Register to api");
        compositeDisposable.add(tinderAuthService.register(userAuthDetails)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authResponse -> {
                            if (authResponse.getErrorData() != null) {
                                Log.d("TEST", "Register unsuccessul");
                                registerView.deleteUserFromFirebase(firebaseUser, true);
                            } else {
                                Log.d("TEST", "Register successful");
                                registerView.showProgress(false);
                                AuthData authData = authResponse.getAuthData();
                                TinderAppUser tinderAppUser = new TinderAppUser(userAuthDetails.email, firebaseUser.getUid());
                                registerView.addUserToFirebaseDatabase(tinderAppUser);
                                tinderAppUserLocalStore.insertTinderAppUser(tinderAppUser);
                                registerView.registered(tinderAppUser, authData.getToken());
                            }
                        },
                        throwable -> {
                            registerView.deleteUserFromFirebase(firebaseUser, false);
                        }));
    }

    @Override
    public void createFirebaseUserUnsuccessful(UserAuthDetails userAuthDetails, Exception exception) {
        registerView.showProgress(false);
        if (exception instanceof FirebaseNetworkException) {
            registerView.showError(false);
        } else {
            registerView.showError(true);
        }
    }

    @Override
    public void deleteUserFromFirebaseSuccessful(boolean errorFromApi) {
        registerView.showProgress(false);
        registerView.showError(errorFromApi);
    }

    @Override
    public void detach() {
        compositeDisposable.clear();
        registerView = null;
    }

}
