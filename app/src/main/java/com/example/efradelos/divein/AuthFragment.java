package com.example.efradelos.divein;

import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.identity.intents.AddressConstants;
import com.google.android.gms.plus.Plus;

public class AuthFragment extends Fragment
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, Firebase.AuthResultHandler  {

    private final String LOG_TAG = "EFX";
    private static final int RC_GOOGLE_LOGIN = 1;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;
    private GoogleApiClient mGoogleApiClient;
    private SignInListener mSignInListener;


    public void setLoginListener(SignInListener listener) {
        mSignInListener = listener;
    }

    public void loginInWithPassword(String username, String password) {
        Constants.FIREBASE_REF.authWithPassword(username, password, this);
    }

    public void loginInWithGoogle() {
        mShouldResolve = true;
        mGoogleApiClient.connect();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        AuthData authData = Constants.FIREBASE_REF.getAuth();
        if(authData != null) {
            if(mSignInListener != null) mSignInListener.signInSucceeded();
        } else {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        new GetGoogleAuthTokenTask().execute();
        mShouldResolve = false;
    }

    @Override
    public void onConnectionSuspended(int i) {}


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        Log.d(LOG_TAG, "onConnectionFailed:" + connectionResult);
        if(mSignInListener != null) mSignInListener.signInFailed(true);

        if (mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(getActivity(), RC_GOOGLE_LOGIN);
                } catch (IntentSender.SendIntentException e) {
                    Log.e(LOG_TAG, "Could not resolve ConnectionResult.", e);
                }
            }
        }
    }

    @Override
    public void onAuthenticated(AuthData authData) {
        if(mSignInListener != null) mSignInListener.signInSucceeded();
    }

    @Override
    public void onAuthenticationError(FirebaseError firebaseError) {
        if(mSignInListener != null) mSignInListener.signInFailed(false);
    }


    private class GetGoogleAuthTokenTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String accountName = Plus.AccountApi.getAccountName(mGoogleApiClient);
            String scopes = String.format("oauth2:%s", Scopes.PLUS_LOGIN);

            try {
                return GoogleAuthUtil.getToken(getActivity(), accountName, scopes);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error retrieving google auth token.", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Constants.FIREBASE_REF.authWithOAuthToken("google", result, AuthFragment.this);
            }
        }

    }

    public interface SignInListener {
        void signInSucceeded();
        void signInFailed(boolean autoTry);
    }
}
