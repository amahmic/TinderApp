package com.example.azramahmic.tinderapp.presentation.login;

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
import android.widget.TextView;

import com.example.azramahmic.tinderapp.R;
import com.example.azramahmic.tinderapp.data.api.tinder_auth.UserCredentials;
import com.example.azramahmic.tinderapp.data.api.tinder_auth.model.UserAuthDetails;
import com.example.azramahmic.tinderapp.data.local.model.auth_user.TinderAppUser;
import com.example.azramahmic.tinderapp.presentation.TinderApp;
import com.example.azramahmic.tinderapp.presentation.base.BaseActivity;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class LoginActivity extends BaseActivity implements LoginContract.View {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @BindView(R.id.til_username)
    TextInputLayout til_username;
    @BindView(R.id.tiet_username)
    TextInputEditText tiet_username;
    @BindView(R.id.til_password)
    TextInputLayout til_password;
    @BindView(R.id.tiet_password)
    TextInputEditText tiet_password;
    @BindView(R.id.bt_login)
    Button bt_login;
    @BindView(R.id.pb_login)
    ProgressBar pb_login;
    @BindView(R.id.tv_register_link)
    TextView tv_registerLink;

    @Inject
    LoginContract.Presenter presenter;
    @Inject
    CompositeDisposable compositeDisposable;

    private AlertDialog alertDialog;

    private FirebaseAuth firebaseAuth;
    private static final String TAG = "TEST_FIREBASE_AUTH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        showProgress(false);
        presenter.attach(this);

        Observable<Boolean> emailObservable = RxTextView.textChanges(tiet_username)
                .map(inputText -> inputText != null && !"".equals(inputText.toString().trim()))
                .distinctUntilChanged();

        Observable<Boolean> passwordObservable = RxTextView.textChanges(tiet_password)
                .map(inputText -> inputText != null && !"".equals(inputText.toString().trim()))
                .distinctUntilChanged();

        compositeDisposable.add(Observable.combineLatest (
                emailObservable,
                passwordObservable,
                (emailValid, passwordValid) -> emailValid && passwordValid)
                .distinctUntilChanged()
                .subscribe(valid -> bt_login.setEnabled(valid)));

        compositeDisposable.add(RxView.clicks(bt_login)
                .subscribe(v -> presenter.login(new UserAuthDetails(tiet_username.getText().toString(), tiet_password.getText().toString()))));

        compositeDisposable.add(RxView.clicks(tv_registerLink)
                .subscribe(v -> navigator.navigateToRegister(LoginActivity.this)));

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void showProgress(boolean inProgress) {
        tiet_username.setEnabled(!inProgress);
        tiet_password.setEnabled(!inProgress);
        bt_login.setEnabled(!inProgress);
        tv_registerLink.setEnabled(!inProgress);
        pb_login.setVisibility(inProgress ? View.VISIBLE : View.GONE);
    }

    @Override
    public void signInToFirebase(UserAuthDetails userAuthDetails) {
        // [START sign_in_with_email]
        firebaseAuth.signInWithEmailAndPassword(userAuthDetails.email, userAuthDetails.password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            presenter.signInToFirebaseUserSuccessful(firebaseAuth.getCurrentUser(), userAuthDetails);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            presenter.signInToFirebaseUserUnsuccessful(userAuthDetails, task.getException());
                        }
                    }
                });
    }

    @Override
    public void updateUserInFirebaseDatabase(TinderAppUser tinderAppUser, FirebaseUser firebaseUser) {
        Log.d(TAG, "updateUserInFirebaseDatabase: " + tinderAppUser.toString());
        DatabaseReference usersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        String instanceId = FirebaseInstanceId.getInstance().getToken();
        if (instanceId != null) {
            usersDatabaseReference
                    .child(tinderAppUser.getUid())
                    .child("instanceId")
                    .setValue(instanceId);
        }
    }

    @Override
    public void showError(boolean errorFromApi) {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this)
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {})
                    .create();
        }
        alertDialog.setMessage(getString(R.string.login_error,
                errorFromApi ? getString(R.string.check_credentials) : getString(R.string.check_internet)));
        alertDialog.show();
    }

    @Override
    public void loggedIn(TinderAppUser tinderAppUser, String token) {
        UserCredentials userCredentials = new UserCredentials(tiet_username.getText().toString(),
                tiet_password.getText().toString(), token);
        TinderApp.get(this).login(userCredentials);
        navigator.navigateToFeed(this);
        finish();
    }

    @Override
    public void setupActivityComponent() {
        TinderApp.get(this).appComponent()
                .plusLoginComponent()
                .build()
                .inject(this);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        compositeDisposable.dispose();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        super.onDestroy();
    }
}
