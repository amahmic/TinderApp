package com.example.azramahmic.tinderapp.data.api.tinder;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.azramahmic.tinderapp.data.api.base_model.ErrorData;
import com.example.azramahmic.tinderapp.data.api.base_model.TinderBaseApiResponse;
import com.example.azramahmic.tinderapp.data.api.tinder_auth.TinderAuthService;
import com.example.azramahmic.tinderapp.data.api.tinder_auth.TokenManager;
import com.example.azramahmic.tinderapp.data.api.tinder_auth.UserCredentials;
import com.example.azramahmic.tinderapp.data.api.tinder_auth.model.AuthData;
import com.example.azramahmic.tinderapp.data.api.tinder_auth.model.AuthResponse;
import com.example.azramahmic.tinderapp.di.scope.UserScope;
import com.google.gson.Gson;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@UserScope
public class TinderInterceptor implements Interceptor {

    private TinderAuthService tinderAuthService;
    private TokenManager tokenManager;
    private Gson gson;

    @Inject
    public TinderInterceptor(TinderAuthService tinderAuthService, TokenManager tokenManager, Gson gson) {
        this.tinderAuthService = tinderAuthService;
        this.tokenManager = tokenManager;
        this.gson = gson;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Log.d("INTERCEPTOR", "Token: " + tokenManager.getToken());

        //Add Authorization header
        Request originalRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + tokenManager.getToken())
                .build();

        //Get response
        Response response = chain.proceed(originalRequest);
        ResponseBody body = response.body();

        if (body != null) {
            String bodyString = body.string();
            Log.d("TINDER_INTERCEPTOR", "Body: " + bodyString);
            TinderBaseApiResponse apiResponse = gson.fromJson(bodyString, TinderBaseApiResponse.class);
            ErrorData errorData = apiResponse.getErrorData();

            //Check if error httpCode is 401 and try to refresh token
            if (errorData != null && errorData.getHttpCode() == 401) {
                Log.d("TINDER_INTERCEPTOR", "Got 401 -> Try to login again.");

                synchronized (this) {
                    //Recreate response
                    MediaType contentType = body.contentType();
                    ResponseBody newBody = ResponseBody.create(contentType, bodyString);
                    Response originalResponse = response.newBuilder().body(newBody).build();

                    String currentToken = String.format("Bearer %s", tokenManager.getToken());
                    String tokenFromRequest = originalResponse.request().header("Authorization");

                    Log.d("TINDER_INTERCEPTOR", "Current token: " + currentToken);
                    Log.d("TINDER_INTERCEPTOR", "Token from the original request: " + tokenFromRequest);

                    //Compare current access token and token from request
                    //The same -> already updated, just reexecute the original request
                    if (currentToken.trim().equals(tokenFromRequest.trim())) {
                        Log.d("TINDER_INTERCEPTOR", "Tokens are the same -> login again");
                        //Refresh token -> Try to login again
                        UserCredentials userCredentials = tokenManager.getUserCredentials();
                        //UserAuthDetails userAuthDetails = new UserAuthDetails("userd@gmail.com", "11122122");
                        retrofit2.Response<AuthResponse> loginResponse = tinderAuthService.loginPom(userCredentials).execute();

                        AuthResponse authResponse = loginResponse.body();
                        if (authResponse != null) {
                            Log.d("TINDER_INTERCEPTOR", "Got login response");

                            if (authResponse.getAuthData() != null) {
                                AuthData authData = authResponse.getAuthData();
                                Log.d("TINDER_INTERCEPTOR", "Got new auth details: " + authData.toString());
                                //Refresh token and execute request again with new token
                                currentToken = authData.getToken();
                                tokenManager.updateToken(currentToken);
                            }

                            if (authResponse.getErrorData() != null) {
                                Log.d("TINDER_INTERCEPTOR", "Got error message while login, return original response");
                                ErrorData errorLoginData = authResponse.getErrorData();
                                Log.d("TINDER_INTERCEPTOR", "Error data message: " + errorLoginData.toString());
                                tokenManager.logout();
                            }

                        } else {
                            Log.d("TINDER_INTERCEPTOR", "Login response is null!");
                        }
                    } else {
                        Log.d("TINDER_INTERCEPTOR", "Token already refreshed!");
                    }

                    Request request = originalRequest.newBuilder()
                            .header("Authorization", "Bearer " + currentToken)
                            .build();
                    return chain.proceed(request);
                }
            }

            //Recreate body and return
            Log.d("INTERCEPTOR", "Recreate and return chain: " + bodyString);
            MediaType contentType = body.contentType();
            ResponseBody newBody = ResponseBody.create(contentType, bodyString);
            return response.newBuilder().body(newBody).build();
        } else {
            return response;
        }
    }
}
