package com.example.azramahmic.tinderapp.presentation.splash;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.azramahmic.tinderapp.R;
import com.example.azramahmic.tinderapp.data.api.tinder_auth.TinderAuthService;
import com.example.azramahmic.tinderapp.data.api.tinder_auth.UserCredentials;
import com.example.azramahmic.tinderapp.data.local.TinderAppUserLocalStore;
import com.example.azramahmic.tinderapp.data.local.model.auth_user.TinderAppUser;
import com.example.azramahmic.tinderapp.presentation.TinderApp;
import com.example.azramahmic.tinderapp.presentation.base.BaseActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity {

    @Inject
    TinderAppUserLocalStore tinderAppUserLocalStore;
    @Inject
    TinderAuthService tinderAuthService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Here only for testing -> later check for google play services (in login/register) and
        //call makeGooglePlayServicesAvailable!
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status_code = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status_code != ConnectionResult.SUCCESS) {
            Log.i("TinderApp", "Google play services not available/updated, status code: " + status_code);
            Toast.makeText(this, "Google play services not available/updated", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            initialize();
        }
    }

    private void initialize() {
        setNotificationChannel();
        TinderApp tinderApp = TinderApp.get(this);
        UserCredentials userCredentials = tinderAppUserLocalStore.getUserCredentials();
        TinderAppUser tinderAppUser = tinderAppUserLocalStore.getTinderAppUser();
        if (tinderAppUser != null && userCredentials != null) {
            tinderApp.login(userCredentials);
            //If from notification
            if (getIntent().getExtras() != null && getIntent().getExtras().get("senderId") != null) {
                Bundle notificationData = getIntent().getExtras();
                String senderId = notificationData.getString("senderId");
                Log.d("SPLASH", "SenderId: " + senderId);
                navigator.navigateToChatWithBackStack(this, notificationData.getString("senderId"));
            } else {
                navigator.navigateToFeed(SplashActivity.this);
            }
            finish();
        } else {
            navigator.navigateToLogin(SplashActivity.this);
            finish();
        }
    }

    @Override
    public void setupActivityComponent() {
        TinderApp.get(this)
                .appComponent()
                .plusSplashComponent()
                .build()
                .inject(this);
    }

    private void setNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationManager notificationManager =
                    (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            //Create notification channel
            if(notificationManager.getNotificationChannel(getString(R.string.default_notification_channel_id)) == null) {
                NotificationChannel channel = new NotificationChannel(
                        // The id of the channel.
                        getString(R.string.default_notification_channel_id),
                        // The user-visible name of the channel.
                        getString(R.string.channel_name),
                        //Importance za kanal
                        NotificationManager.IMPORTANCE_DEFAULT);

                // Configure the notification channel.
                channel.setDescription(getString(R.string.channel_description));
                channel.enableLights(true);
                // Sets the notification light color for notifications posted to this
                // channel, if the device supports this feature.
                channel.setLightColor(Color.YELLOW);
                channel.enableVibration(true);
                //mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(channel);
                //Notification channel already exists
            }
        }
    }
}
