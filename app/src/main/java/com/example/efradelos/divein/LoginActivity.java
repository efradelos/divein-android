package com.example.efradelos.divein;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.SignInButton;

public class LoginActivity extends AppCompatActivity implements AuthFragment.SignInListener {

    private final String LOG_TAG = "EFX";
    private final String AUTH_FRAGMENT = "AUTH_FRAGMENT";
    private static final int RC_GOOGLE_LOGIN = 1;

    private EditText mUsername;
    private EditText mPassword;
    private ProgressBar mLoadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);
        Button signIn = (Button) findViewById(R.id.sign_in_button);

        mLoadingSpinner = (ProgressBar) findViewById(R.id.loading_spinner);

        mUsername = (EditText) findViewById(R.id.username_text);
        mPassword = (EditText) findViewById(R.id.password_text);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingSpinner.setVisibility(View.VISIBLE);
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                AuthFragment fragment = getAuthFragment();
                fragment.loginInWithPassword(username, password);
            }
        });

        SignInButton google = (SignInButton) findViewById(R.id.google_sign_in_button);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingSpinner.setVisibility(View.VISIBLE);
                AuthFragment fragment = getAuthFragment();
                fragment.loginInWithGoogle();
            }
        });

        AuthFragment fragment = new AuthFragment();
        fragment.setLoginListener(this);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(fragment, AUTH_FRAGMENT).commit();
   }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_LOGIN) {
            mLoadingSpinner.setVisibility(View.VISIBLE);
            AuthFragment fragment = getAuthFragment();
            fragment.loginInWithGoogle();
        }
    }

    @Override
    public void signInSucceeded() {
        mLoadingSpinner.setVisibility(View.GONE);
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void signInFailed(boolean autoTry) {
        mLoadingSpinner.setVisibility(View.GONE);
        if (!autoTry) Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_LONG).show();

    }

    private AuthFragment getAuthFragment() {
        return (AuthFragment)getFragmentManager().findFragmentByTag(AUTH_FRAGMENT);
    }
}
