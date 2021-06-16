package com.malaab.ya.action.actionyamalaab.utils.googleAuthSignin;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.malaab.ya.action.actionyamalaab.R;


public class GoogleSignInHelper implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 100;
    private FragmentActivity mContext;
    private GoogleAuthResponse mListener;
    private GoogleApiClient mGoogleApiClient;


    /**
     * Public constructor
     *
     * @param context        instance of caller activity
     * @param serverClientId The client ID of the server that will verify the integrity of the token. If you don't have clientId pass null.
     *                       For more detail visit {@link 'https://developers.google.com/identity/sign-in/android/backend-auth'}
     */
    public GoogleSignInHelper(FragmentActivity context, @Nullable String serverClientId, @NonNull GoogleAuthResponse listener) {
        mContext = context;
        mListener = listener;

        //noinspection ConstantConditions
        if (listener == null) {
            throw new RuntimeException("GoogleAuthResponse listener cannot be null.");
        }

        //build api client
        buildGoogleApiClient(buildSignInOptions(serverClientId));
    }


    private GoogleSignInOptions buildSignInOptions(@Nullable String serverClientId) {
        GoogleSignInOptions.Builder gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .requestId();

        if (serverClientId != null)
            gso.requestIdToken(serverClientId);

        return gso.build();
    }

    private void buildGoogleApiClient(@NonNull GoogleSignInOptions gso) {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(mContext, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    public void performSignIn(Activity activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void performSignIn(Fragment fragment) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        fragment.startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                if (mListener != null) {
                    mListener.onGoogleAuthSignIn(parseToGoogleUser(acct));
                }
            } else {
                if (mListener != null) {
                    mListener.onGoogleAuthSignInFailed();
                }
            }
        }
    }

    private GoogleAuthUser parseToGoogleUser(GoogleSignInAccount account) {
        GoogleAuthUser user = new GoogleAuthUser();
        user.id = account.getId();
        user.name = account.getDisplayName();
        user.familyName = account.getFamilyName();
        user.idToken = account.getIdToken();
        user.email = account.getEmail();
        user.photoUrl = account.getPhotoUrl();
        return user;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (mListener != null) {
            mListener.onGoogleAuthSignInFailed();
        }
    }


    public void performSignOut() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (mListener != null) {
                                mListener.onGoogleAuthSignOut(status.isSuccess());
                            }
                        }
                    });
        } else {
            if (mListener != null) {
                mListener.onGoogleAuthSignOut(true);
            }
        }
    }

    public void onDestroy() {
        mListener = null;
    }
}
