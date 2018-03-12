package com.example.azramahmic.tinderapp.presentation.feed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.azramahmic.tinderapp.R;
import com.example.azramahmic.tinderapp.data.api.tinder.TinderService;
import com.example.azramahmic.tinderapp.data.local.model.feed.User;
import com.example.azramahmic.tinderapp.presentation.TinderApp;
import com.example.azramahmic.tinderapp.presentation.base.BaseActivity;
import com.example.azramahmic.tinderapp.presentation.utils.Utils;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipeDirectionalView;
import com.mindorks.placeholderview.listeners.ItemRemovedListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class FeedActivity extends BaseActivity implements FeedContract.View, TinderCard.TinderCardCallback {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, FeedActivity.class);
    }

    @BindView(R.id.pb_loading_users)
    ProgressBar pb_loading;
    @BindView(R.id.tv_no_more_data)
    TextView tv_noMoreData;

    private SwipeDirectionalView mSwipeView;

    @Inject
    FeedContract.Presenter presenter;

    @Inject
    TinderService tinderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_activity);
        ButterKnife.bind(this);
        setInitialState();
        mSwipeView = findViewById(R.id.swipeView);

        int bottomMargin = Utils.dpToPx(160);
        Point windowSize = Utils.getDisplaySize(getWindowManager());

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setIsUndoEnabled(true)
                .setSwipeVerticalThreshold(Utils.dpToPx(50))
                .setSwipeHorizontalThreshold(Utils.dpToPx(50))
                .setHeightSwipeDistFactor(10)
                .setWidthSwipeDistFactor(5)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x)
                        .setViewHeight(windowSize.y - bottomMargin)
                        .setViewGravity(Gravity.TOP)
                        .setPaddingTop(20)
                        .setSwipeAnimTime(300)
                        .setRelativeScale(0.01f)
                        .setSwipeMaxChangeAngle(2f));

        //Deactivation is handled in TinderCard!
        mSwipeView.activatePutBack();

        mSwipeView.addItemRemoveListener(new ItemRemovedListener() {
            @Override
            public void onItemRemoved(int count) {
                //Activate again after item removal
                mSwipeView.activatePutBack();
                Log.d("TAG", "COUNT: " + count);
                presenter.viewRemoved(count);
            }
        });


        presenter.attach(this);
    }

    private void setInitialState() {
        showProgress(false);
        tv_noMoreData.setVisibility(View.GONE);
    }

    @Override
    public void showProgress(boolean inProgress) {
        pb_loading.setVisibility(inProgress ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showData(RealmResults<User> users) {
        Log.d("FeedActivity", "show data");
        Log.d("FeedActivity", "user count: " + users.size());
        for (User user : users) {
            addUserCard(user);
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, getString(R.string.error_loading_feed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoMoreData() {
        tv_noMoreData.setVisibility(View.VISIBLE);
    }

    public void refreshData(RealmResults<User> users) {
        Log.d("FeedActivity", "refresh data");
        mSwipeView.removeAllViews();
        for (User user : users) {
            addUserCard(user);
        }
    }

    private void addUserCard(User user) {
        TinderCardDataWrapper tinderCardDataWrapper = new TinderCardDataWrapper(user);
        TinderCard tinderCard = new TinderCard(mSwipeView, tinderCardDataWrapper,this);
        mSwipeView.addView(tinderCard);
    }

    @Override
    public void onSwipeUp(TinderCardDataWrapper tinderCardDataWrapper) {
        Toast.makeText(this, "UP", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSwipeDown(TinderCardDataWrapper tinderCardDataWrapper) {
        Toast.makeText(this, "Down", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSwipeLeft(TinderCardDataWrapper tinderCardDataWrapper) {
        Toast.makeText(this, "Reject", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSwipeRight(TinderCardDataWrapper tinderCardDataWrapper) {
        Toast.makeText(this, "Accept", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setupActivityComponent() {
        TinderApp.get(this)
                .userComponent()
                .plusFeedComponent()
                .build()
                .inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.feed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chat_item:
                navigator.navigateToChatUsers(this);
                return true;
            case R.id.logout:
                TinderApp.get(this).logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }
}
