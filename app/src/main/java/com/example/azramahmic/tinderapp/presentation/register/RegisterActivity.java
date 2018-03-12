package com.example.azramahmic.tinderapp.presentation.register;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.azramahmic.tinderapp.R;
import com.example.azramahmic.tinderapp.data.api.tinder_auth.UserCredentials;
import com.example.azramahmic.tinderapp.data.api.tinder_auth.model.UserAuthDetails;
import com.example.azramahmic.tinderapp.data.local.model.auth_user.TinderAppUser;
import com.example.azramahmic.tinderapp.presentation.TinderApp;
import com.example.azramahmic.tinderapp.presentation.base.BaseActivity;
import com.example.azramahmic.tinderapp.presentation.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class RegisterActivity extends BaseActivity implements RegisterContract.View {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, RegisterActivity.class);
    }

    @BindView(R.id.til_username_register)
    TextInputLayout til_username_register;
    @BindView(R.id.tiet_username_register)
    TextInputEditText tiet_username_register;
    @BindView(R.id.til_password_register)
    TextInputLayout til_password_register;
    @BindView(R.id.tiet_password_register)
    TextInputEditText tiet_password_register;
    @BindView(R.id.bt_register)
    Button bt_register;
    @BindView(R.id.pb_register)
    ProgressBar pb_register;

    @Inject
    CompositeDisposable compositeDisposable;
    @Inject
    RegisterContract.Presenter presenter;

    private AlertDialog alertDialog;

    private FirebaseAuth firebaseAuth;

    private static final String TAG = "TEST";

    private DatabaseReference usersDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        ButterKnife.bind(this);

        presenter.attach(this);
        setInitialState();

        Observable<Boolean> emailObservable = RxTextView.textChanges(tiet_username_register)
                .debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .skip(1)
                .map(inputText -> Utils.isEmailValid(inputText.toString()))
                .distinctUntilChanged();

        Observable<Boolean> passwordObservable = RxTextView.textChanges(tiet_password_register)
                .debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .skip(1)
                .map(inputText -> Utils.isPasswordValid(inputText.toString()))
                .distinctUntilChanged();

        compositeDisposable.add(emailObservable.subscribe(valid -> {
            til_username_register.setErrorEnabled(!valid);
            til_username_register.setError(valid ? null : getString(R.string.email_invalid));
        }));
        compositeDisposable.add(passwordObservable.subscribe(valid -> {
            til_password_register.setErrorEnabled(!valid);
            til_password_register.setError(valid ? null : getString(R.string.password_invalid));
        }));

        compositeDisposable.add(Observable.combineLatest (
                emailObservable,
                passwordObservable,
                (emailValid, passwordValid) -> emailValid && passwordValid)
                .distinctUntilChanged()
                .subscribe(valid -> bt_register.setEnabled(valid)));

        compositeDisposable.add(RxView.clicks(bt_register)
                .subscribe(v -> {
                    Log.d(TAG, "registerView -> presenter.register");
                    presenter.register(new UserAuthDetails(tiet_username_register.getText().toString(), tiet_password_register.getText().toString()));
                }));

        firebaseAuth = FirebaseAuth.getInstance();
        usersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
    }

    private void setInitialState() {
        showProgress(false);
        bt_register.setEnabled(false);
    }

    @Override
    public void showProgress(boolean inProgress) {
        Log.d(TAG, "showProgress: " + inProgress);
        tiet_username_register.setEnabled(!inProgress);
        tiet_password_register.setEnabled(!inProgress);
        bt_register.setEnabled(!inProgress);
        pb_register.setVisibility(inProgress ? View.VISIBLE : View.GONE);
    }

    @Override
    public void createFirebaseUser(UserAuthDetails userAuthDetails) {
        Log.d(TAG, "createFirebaseUser: " + userAuthDetails.toString());
        firebaseAuth.createUserWithEmailAndPassword(userAuthDetails.email, userAuthDetails.password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createFirebaseUser:success");
                            presenter.createFirebaseUserSuccessful(firebaseAuth.getCurrentUser(), userAuthDetails);
                        } else {
                            Log.w(TAG, "createFirebaseUser:failure", task.getException());
                            presenter.createFirebaseUserUnsuccessful(userAuthDetails, task.getException());
                        }
                    }
                });
    }

    @Override
    public void showError(boolean errorFromApi) {
        Log.d(TAG, "showError: " + errorFromApi);
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this)
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {})
                    .create();
        }
        alertDialog.setMessage(errorFromApi ? getString(R.string.register_error) : getString(R.string.check_internet));
        alertDialog.show();
    }

    @Override
    public void addUserToFirebaseDatabase(TinderAppUser tinderAppUser) {
        Log.d(TAG, "addUserToFirebaseDatabase: " + tinderAppUser.toString());
        usersDatabaseReference
                .child(tinderAppUser.getUid()).setValue(tinderAppUser);

        String instanceId = FirebaseInstanceId.getInstance().getToken();
        if (instanceId != null) {
            usersDatabaseReference
                    .child(tinderAppUser.getUid())
                    .child("instanceId")
                    .setValue(instanceId);
        }
    }

    @Override
    public void registered(TinderAppUser tinderAppUser, String token) {
        Log.d(TAG, "registered: " + tinderAppUser.toString());
        UserCredentials userCredentials = new UserCredentials(tiet_username_register.getText().toString(),
                tiet_password_register.getText().toString(),
                token);
        TinderApp.get(this).login(userCredentials);
        navigator.navigateToFeed(this);
        finishAffinity();
    }

    @Override
    public void deleteUserFromFirebase(FirebaseUser firebaseUser, boolean errorFromApi) {
        Log.d(TAG, "deleteUserFromFirebase: " + firebaseUser.toString());
        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                presenter.deleteUserFromFirebaseSuccessful(errorFromApi);
            }
        });
    }

    @Override
    public void setupActivityComponent() {
        TinderApp.get(this)
                .appComponent()
                .plusRegisterComponent()
                .build()
                .inject(this);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        super.onDestroy();
    }
}
