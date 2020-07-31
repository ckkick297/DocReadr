package com.example.keerat666.listviewpdfui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.keerat666.listviewpdfui.db.MyDbHelper;
import com.example.keerat666.listviewpdfui.models.LoginData;
import com.example.keerat666.listviewpdfui.rest.ApiService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.ssw.linkedinmanager.dto.LinkedInAccessToken;
import com.ssw.linkedinmanager.dto.LinkedInEmailAddress;
import com.ssw.linkedinmanager.dto.LinkedInUserProfile;
import com.ssw.linkedinmanager.events.LinkedInManagerResponse;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements Callback<Objects> {

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    LoginData loginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestServerAuthCode(getString(R.string.client_id), true)
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/gmail.compose"))//new Scope("https://www.googleapis.com/auth/gmail.send"),new Scope("https://www.googleapis.com/auth/gmail.compose"))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        loginData = new LoginData();
    }

    private void startNextActivity(){
        startActivity(new Intent(Login.this,MainActivity.class));
        finish();
    }

    public void googleSignInClick(View view) {
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                new GetToken(account).execute();

            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    class GetToken extends AsyncTask<String, Void, String> {

        GoogleSignInAccount account;
        GoogleTokenResponse tokenResponse = null;

        public GetToken(GoogleSignInAccount account) {
            this.account = account;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                        new NetHttpTransport(),
                        JacksonFactory.getDefaultInstance(),
                        "https://oauth2.googleapis.com/token", getString(R.string.client_id),
                        getString(R.string.client_secret),
                        account.getServerAuthCode(),
                        getString(R.string.client_redirect_uri))  // Specify the same redirect URI that you use with your web
                        // app. If you don't have a web version of your app, you can
                        // specify an empty string.
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String accessToken = tokenResponse.getAccessToken();
            String refreshToken = tokenResponse.getRefreshToken();
            String accessTokenRefreshTokenSplit = accessToken+"&"+refreshToken;

            return accessTokenRefreshTokenSplit;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            final String tokens[] = s.split("&");
            loginData.setUSER_NAME(account.getDisplayName());
            loginData.setUSER_EMAIL(account.getEmail());
            loginData.setUSER_ACCESS_TOKEN(tokens[0]);
            loginData.setUSER_REFRESH_TOKEN(tokens[1]);
            loginData.setLogin_user_picture(account.getPhotoUrl().toString());
            googleLinkedInRestApiAuth();
        }
    }

    private void googleLinkedInRestApiAuth(){
        ApiService.getService().getLoginData(loginData).enqueue((Callback)this);
    }


    @Override
    public void onResponse(Call<Objects> call, Response<Objects> response) {
        if (response.isSuccessful()) {
            Object object = response.body();

            if (object instanceof LoginData) {

                LoginData loginDataResp = (LoginData) object;
                loginData.setLogin_user_app_user_id((loginDataResp.getData().getUser_id()));
                loginData.setLogin_user_token_access(loginDataResp.getData().getToken());

                new MyDbHelper(Login.this).insertUser(loginData);
                Toast.makeText(getApplicationContext(), getString(R.string.login_success), Toast.LENGTH_SHORT).show();

                MySharedPreference.setPrefLogIn(Login.this, true);
                startNextActivity();
            }

        }
    }

    @Override
    public void onFailure(Call<Objects> call, Throwable t) {
        Toast.makeText(getApplicationContext(), getString(R.string.login_failed)+" \nError :"+t.getMessage(), Toast.LENGTH_SHORT).show();
    }


    private LinkedInRequest linkedInRequest;
    public void linkedInSignInClick(View view) {

        linkedInRequest = new LinkedInRequest(this, new LinkedInManagerResponse(){

            @Override
            public void onGetAccessTokenFailed() {
            }

            @Override
            public void onGetAccessTokenSuccess(LinkedInAccessToken linkedInAccessToken) {
                loginData.setUSER_ACCESS_TOKEN(linkedInAccessToken.getAccess_token());
            }

            @Override
            public void onGetCodeFailed() {
            }

            @Override
            public void onGetCodeSuccess(String code) {
            }

            @Override
            public void onGetProfileDataFailed() {
            }

            @Override
            public void onGetProfileDataSuccess(LinkedInUserProfile linkedInUserProfile) {

                loginData.setUSER_NAME(linkedInUserProfile.getUserName().getFirstName().getLocalized().getEn_US()+
                        " "+ linkedInUserProfile.getUserName().getLastName().getLocalized().getEn_US());

                loginData.setLogin_user_picture(linkedInUserProfile.getImageURL());

                linkedInRequest.dismissAuthenticateView();
            }

            @Override
            public void onGetEmailAddressFailed() {
            }

            @Override
            public void onGetEmailAddressSuccess(final LinkedInEmailAddress linkedInEmailAddress) {

                loginData.setUSER_EMAIL(linkedInEmailAddress.getEmailAddress());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        googleLinkedInRestApiAuth();
                    }
                }, 1000);


            }
        },
                "81s5pt9urgzokl", "FlIemzpxMafjjgg5", "http://www.vocso.com");

        linkedInRequest.showAuthenticateView(LinkedInRequest.MODE_BOTH_OPTIONS);
    }



    public void showAlertDialog() {
        AlertDialog dialog = new AlertDialog.Builder(Login.this).create();
        dialog.setTitle("Error");
        dialog.setMessage("Authentication Failed...");
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                signOut();
            }
        });
        dialog.show();
    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(Login.this, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(Login.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Intent intent = new Intent(Login.this, Login.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), getString(R.string.log_out), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}