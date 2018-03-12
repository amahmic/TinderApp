package com.example.azramahmic.tinderapp.presentation;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.example.azramahmic.tinderapp.data.api.tinder_auth.UserCredentials;
import com.example.azramahmic.tinderapp.di.component.AppComponent;
import com.example.azramahmic.tinderapp.di.component.DaggerAppComponent;
import com.example.azramahmic.tinderapp.di.component.app_component.UserComponent;
import com.example.azramahmic.tinderapp.presentation.login.LoginActivity;
import com.facebook.stetho.Stetho;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class TinderApp extends MultiDexApplication {

    public static TinderApp get(Context context) {
        return (TinderApp) context.getApplicationContext();
    }

    private AppComponent appComponent;
    private UserComponent userComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        Logger.addLogAdapter(new AndroidLogAdapter());

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        appComponent = DaggerAppComponent.builder()
                .aplication(this)
                //TODO: Add account endpoint
                .baseAuthURL("")
                //TODO: Add feed endpoint
                .baseURL("")
                .tinderApp(this)
                .build();

    }

    public AppComponent appComponent() {
        return appComponent;
    }

    public void login(UserCredentials userCredentials) {
        Log.d("App", "Create user component");
        userComponent = appComponent.plusUserComponent()
                .userCredentials(userCredentials)
                .build();
    }

    public UserComponent userComponent() {
        return userComponent;
    }

    public void logout() {
        userComponent = null;
        try(Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.deleteAll();
                }
            });
        }
        //Log out all
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
