package com.example.azramahmic.tinderapp.presentation.feed;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.example.azramahmic.tinderapp.R;
import com.example.azramahmic.tinderapp.data.local.model.feed.User;
import com.example.azramahmic.tinderapp.data.local.model.feed.UserPhoto;
import com.example.azramahmic.tinderapp.presentation.GlideApp;
import com.mindorks.placeholderview.SwipeDirection;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeHead;
import com.mindorks.placeholderview.annotations.swipe.SwipeInDirectional;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutDirectional;
import com.mindorks.placeholderview.annotations.swipe.SwipeView;

import java.util.List;

@Layout(R.layout.tinder_card_view)
public class TinderCard {

    public interface TinderCardCallback {
        void onSwipeUp(TinderCardDataWrapper tinderCardDataWrapper);
        void onSwipeDown(TinderCardDataWrapper tinderCardDataWrapper);
        void onSwipeLeft(TinderCardDataWrapper tinderCardDataWrapper);
        void onSwipeRight(TinderCardDataWrapper tinderCardDataWrapper);
    }

    @View(R.id.iv_profile_image)
    private ImageView iv_profile_image;

    @SwipeView
    android.view.View swipeView;

    TinderCardDataWrapper tinderCardDataWrapper;
    private Context context;
    private SwipePlaceHolderView swipePlaceHolderView;
    private TinderCardCallback tinderCardCallback;

    public TinderCard(SwipePlaceHolderView swipePlaceHolderView,  TinderCardDataWrapper tinderCardDataWrapper, TinderCardCallback tinderCardCallback) {
        this.context = swipePlaceHolderView.getContext();
        this.swipePlaceHolderView = swipePlaceHolderView;
        this.tinderCardDataWrapper = tinderCardDataWrapper;
        this.tinderCardCallback = tinderCardCallback;
    }

    @Resolve
    private void onResolved(){
        Log.d("EVENT", "on resolved");
        loadImage(tinderCardDataWrapper.getUser().getProfileImageUrl());
    }

    public void loadImage(String imageUrl) {
        Log.d("UP", "LOAD IMAGE WITH URL: " + imageUrl);
        GlideApp.with(context).load(imageUrl)
                .into(iv_profile_image);
    }

    @SwipeHead
    private void onSwipeHeadCard() {
        Log.d("EVENT", "swipe head");
    }

    @Click(R.id.iv_profile_image)
    private void onClick(){
        Log.d("EVENT", "profileImageView click");
    }

    private boolean hasMoreImages() {
        User user = tinderCardDataWrapper.getUser();
        if (!user.getUserPhotos().isEmpty()) {
            Log.d("UP", "not empty");
            int nextUserPhotoIndex = tinderCardDataWrapper.getIndex() + 1;
            Log.d("UP", "Next index: " + nextUserPhotoIndex);
            List<UserPhoto> userPhotoList = user.getUserPhotos();
            if (nextUserPhotoIndex < userPhotoList.size()) {
                Log.d("UP", "Show image with index: " + nextUserPhotoIndex);
                tinderCardDataWrapper.setIndex(nextUserPhotoIndex);
                return true;
            }
        }
        return false;
    }

    private void loadImageFromGallery() {
        loadImage(tinderCardDataWrapper.getUser().getUserPhotos().get(tinderCardDataWrapper.getIndex()).getPhotoUrl());
    }

    @SwipeOutDirectional
    public void onSwipeOutDirectional(SwipeDirection direction) {
        Log.d("EVENT", "SwipeOutDirectional " + direction.name());
        int dir = direction.getDirection();
        if (dir == SwipeDirection.TOP.getDirection()) {
            tinderCardCallback.onSwipeUp(tinderCardDataWrapper);
            if (hasMoreImages()) {
                loadImageFromGallery();
                return;
            }
            swipePlaceHolderView.deactivatePutBack();
        } else if (dir == SwipeDirection.RIGHT_TOP.getDirection()) {
            tinderCardCallback.onSwipeRight(tinderCardDataWrapper);
            swipePlaceHolderView.deactivatePutBack();
        } else {
            tinderCardCallback.onSwipeLeft(tinderCardDataWrapper);
            swipePlaceHolderView.deactivatePutBack();
        }
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeInDirectional
    public void onSwipeInDirectional(SwipeDirection direction) {
        Log.d("EVENT", "SwipeInDirectional " + direction.name());
        int dir = direction.getDirection();
        if (dir == SwipeDirection.BOTTOM.getDirection()) {
            tinderCardCallback.onSwipeDown(tinderCardDataWrapper);
            if (hasMoreImages()) {
                loadImageFromGallery();
                return;
            }
            swipePlaceHolderView.deactivatePutBack();
        } else if (dir == SwipeDirection.LEFT_BOTTOM.getDirection()) {
            tinderCardCallback.onSwipeLeft(tinderCardDataWrapper);
            swipePlaceHolderView.deactivatePutBack();
        } else {
            tinderCardCallback.onSwipeRight(tinderCardDataWrapper);
            swipePlaceHolderView.deactivatePutBack();
        }
    }
}