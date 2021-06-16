package com.malaab.ya.action.actionyamalaab.utils.facebookSignIn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class FacebookHelper {

    private FacebookResponse mListener;
    private String mFieldString;
    private CallbackManager mCallBackManager;

    /**
     * Public constructor.
     *
     * @param responseListener {@link FacebookResponse} listener to get call back response.
     * @param fieldString      name of the fields required. (e.g. id,name,email,gender,birthday,picture,cover)
     * @param activity         instance of the caller activity
     */
    public FacebookHelper(@NonNull FacebookResponse responseListener,
                          @NonNull String fieldString,
                          @NonNull Activity activity) {
        FacebookSdk.sdkInitialize(activity.getApplicationContext());

        //noinspection ConstantConditions
        if (responseListener == null)
            throw new IllegalArgumentException("FacebookResponse listener cannot be null.");

        //noinspection ConstantConditions
        if (fieldString == null)
            throw new IllegalArgumentException("field string cannot be null.");

        mListener = responseListener;
        mFieldString = fieldString;
        mCallBackManager = CallbackManager.Factory.create();

        //get access token
        FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (mListener != null) {
                    mListener.onFbSignInSuccess();
                }

                //get the user profile
                getUserProfile(loginResult);
            }

            @Override
            public void onCancel() {
                if (mListener != null) {
                    mListener.onFbSignInFail();
                }
            }

            @Override
            public void onError(FacebookException e) {
                if (mListener != null) {
                    mListener.onFbSignInFail();
                }
            }
        };
        LoginManager.getInstance().registerCallback(mCallBackManager, mCallBack);
    }

    /**
     * Get user facebook profile.
     *
     * @param loginResult login result with user credentials.
     */
    private void getUserProfile(LoginResult loginResult) {
        // App code
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        AppLogger.json(response.getJSONObject());

                        try {
                            if (mListener != null) {
                                mListener.onFbProfileReceived(parseResponse(object));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                            if (mListener != null) {
                                mListener.onFbSignInFail();
                            }
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", mFieldString);
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * Parse the response received into {@link FacebookUser} object.
     *
     * @param object response received.
     * @return {@link FacebookUser} with required fields.
     * @throws JSONException An exception
     */
    private FacebookUser parseResponse(JSONObject object) throws JSONException {
        FacebookUser user = new FacebookUser();
        user.response = object;

        if (object.has("id"))
            user.facebookID = object.getString("id");

        if (object.has("email"))
            user.email = object.getString("email");

        if (object.has("name"))
            user.name = object.getString("name");

        if (object.has("first_name"))
            user.firstName = object.getString("first_name");

        if (object.has("last_name"))
            user.lastName = object.getString("last_name");

        if (object.has("gender"))
            user.gender = object.getString("gender");

        if (object.has("about"))
            user.about = object.getString("about");

        if (object.has("bio"))
            user.bio = object.getString("bio");

        if (object.has("cover"))
            user.coverPicUrl = object.getJSONObject("cover").getString("source");

        if (object.has("picture"))
            user.profilePic = object.getJSONObject("picture").getJSONObject("data").getString("url");

        return user;
    }


    /**
     * This method handles onActivityResult callbacks from fragment or activity.
     *
     * @param requestCode request code received.
     * @param resultCode  result code received.
     * @param data        Data intent.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallBackManager.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * Get the {@link CallbackManager} for managing callbacks.
     *
     * @return {@link CallbackManager}
     */
    @NonNull
    @CheckResult
    public CallbackManager getCallbackManager() {
        return mCallBackManager;
    }


    /**
     * Perform facebook sign in.<p>
     * NOTE: If you are signing from the fragment than you should call {@link #performSignIn(Fragment)}.<p>
     * This method should generally call when user clicks on "Sign in with Facebook" button.
     *
     * @param activity instance of the caller activity.
     */
    public void performSignIn(Activity activity) {
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email"));
    }

    /**
     * Perform facebook login. This method should be called when you are signing in from
     * fragment.<p>
     * This method should generally call when user clicks on "Sign in with Facebook" button.
     *
     * @param fragment caller fragment.
     */
    public void performSignIn(Fragment fragment) {
        LoginManager.getInstance().logInWithReadPermissions(fragment, Arrays.asList("public_profile", "user_friends", "email"));
    }


    public void performSignOut() {
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }

        if (mListener != null) {
            mListener.onFBSignOut();
        }
    }

    public void onDestroy() {
        mListener = null;
    }
}
