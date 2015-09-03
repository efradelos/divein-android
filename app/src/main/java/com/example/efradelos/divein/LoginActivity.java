package com.example.efradelos.divein;

import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

public class LoginActivity extends AppCompatActivity
        implements Firebase.AuthResultHandler, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static String TAG = "EFX";

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    private static final int RC_GOOGLE_LOGIN = 1;

    private EditText mUsername;
    private EditText mPassword;
    private ProgressBar mLoadingSpinner;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);
        Button signIn = (Button)findViewById(R.id.sign_in_button);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .build();

        mLoadingSpinner= (ProgressBar)findViewById(R.id.loading_spinner);

        mUsername = (EditText)findViewById(R.id.username_text);
        mPassword = (EditText)findViewById(R.id.password_text);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingSpinner.setVisibility(View.VISIBLE);
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                Constants.FIREBASE_REF.authWithPassword(username, password, (Firebase.AuthResultHandler) v.getContext());
            }
        });

        SignInButton google = (SignInButton)findViewById(R.id.google_sign_in_button);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingSpinner.setVisibility(View.VISIBLE);
                mShouldResolve = true;
                mGoogleApiClient.connect();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_GOOGLE_LOGIN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onAuthenticated(AuthData authData) {
        mLoadingSpinner.setVisibility(View.GONE);
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onAuthenticationError(FirebaseError firebaseError) {
        mLoadingSpinner.setVisibility(View.GONE);
        Log.e(TAG, firebaseError.toString());
        Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnected(Bundle bundle) {
        new GetIdTokenTask().execute();
        Log.d(TAG, "onConnected:" + bundle);
        mShouldResolve = false;
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        mLoadingSpinner.setVisibility(View.GONE);
        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    mLoadingSpinner.setVisibility(View.VISIBLE);
                    connectionResult.startResolutionForResult(this, RC_GOOGLE_LOGIN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    private class GetIdTokenTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String accountName = Plus.AccountApi.getAccountName(mGoogleApiClient);
            String scopes = String.format("oauth2:%s", Scopes.PLUS_LOGIN);

            try {
                Log.i(TAG, "Retrieving google auth token.");
                return GoogleAuthUtil.getToken(LoginActivity.this, accountName, scopes);
            } catch (Exception e) {
                Log.e(TAG, "Error retrieving google auth token.", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.i(TAG, "Retrieved google auth token: " + result);
                Constants.FIREBASE_REF.authWithOAuthToken("google", result, LoginActivity.this);
            }
        }

    }
}
