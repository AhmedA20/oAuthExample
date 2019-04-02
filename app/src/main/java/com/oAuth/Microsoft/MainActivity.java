package com.oAuth.Microsoft;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private OAuthProvider.Builder provider;
    private String email = null;
    private String password = null;
    private final String TAG = "Sign In";
    private Button submit;
    private EditText mailView ;
    private EditText passView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if(mUser!=null){
            Toast.makeText(getBaseContext(), "Login Succeeded", Toast.LENGTH_LONG).show();
        }
        else{
            Respond();
        }

    }

    //responsible for Listening to signIn request
    //and directing logIn flow
    private void Respond() {
        provider = OAuthProvider.newBuilder("microsoft.com");
        //custom parameters
        customParam();
        scopeSetter();
        //listener for signIn requests
        Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can also be retrieved:
                                    // authResult.getCredential().getAccessToken().
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                }
                            });
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.
            signIn();
        }

    }

    private void signIn() {
        mAuth
                .startActivityForSignInWithProvider(/* activity= */ this, provider.build())
                .addOnSuccessListener(
                        new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // User is signed in.
                                // IdP data available in
                                // authResult.getAdditionalUserInfo().getProfile().
                                // The OAuth access token can also be retrieved:
                                // authResult.getCredential().getAccessToken().
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure.
                            }
                        });
    }

    private void customParam(){
        //todo:add your custom parameters here
        // Force re-consent.
        provider.addCustomParameter("prompt", "consent");
        // Target specific email with login hint.
        provider.addCustomParameter("domain_hint", "user@science.helwan.edu.eg");
    }

    private void scopeSetter(){
        List<String> scopes = new ArrayList<String>() {
            {
                //TODO: add the needed permissions here
                add("mail.read");
                add("calendars.read");
            }
        };
        provider.setScopes(scopes);
    }
}
