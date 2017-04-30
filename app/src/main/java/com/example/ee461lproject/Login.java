package com.example.ee461lproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by juanbravo on 4/22/17.
 */

public class Login extends AppCompatActivity {

    //Variable Declaration
    private EditText email;
    private EditText password;
    private Button newUserButton;
    private Button forgotPasswordButton;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "Login.this";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //1.0 Link EditText and Buttons in layouts
        email = (EditText) findViewById(R.id.emailLine);
        password = (EditText) findViewById(R.id.passwordLine);
        newUserButton = (Button) findViewById(R.id.newUserButton);
        forgotPasswordButton = (Button) findViewById(R.id.forgotPassButton);
        loginButton = (Button) findViewById(R.id.loginButton);

        /* Returns an instance of the FirebaseAuth class corresponding to the default
         * FirebaseApp instance.
         */
        mAuth = FirebaseAuth.getInstance();

        mAuthListener  = new FirebaseAuth.AuthStateListener() {
            // This method gets invoked in the UI thread on changes in the authentication state.
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // Returns the currently signed-in FirebaseUser or null if there is none.
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        //2.0 Add onclick functionality to each button

        //2.1 set listener for newUser Button
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2.1.1 redirect to CreateUser page
                loadCreateUserPage();
            }
        });

        //2.2 set listener for forgotPassword Button
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2.2.1 redirect to ForgotPassword page
                loadForgotPasswordPage();
            }
        });

        //2.3 Login Button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2.3.1 validate Log In
                signIn(email.getText().toString(), password.getText().toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    // TODO: Add onRestart method

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.signOut();
        if (mAuthListener != null) {
            // Unregisters a listener to authentication changes.
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    // ------------------------------------- PRIVATE METHODS ------------------------------------- //


    private void loadCreateUserPage() {
        Intent intent = new Intent(this, CreateUser.class);
        startActivity(intent);
    }

    private void loadForgotPasswordPage() {
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        ArrayList<String> fields = new ArrayList<String>(Arrays.asList(email, password));
        if (!AuthenticationTools.validateForm(fields)) {

            Toast.makeText(Login.this,
                    "Fill out the form completely.",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        /* The signInWithEmailAndPassword method tries to sign in a user with the given email
         * and password. This method also returns a Task object.
         *
         * This method and the createUserWithEmailAndPassword method (in CreateUser) both trigger an
         * onAuthStateChanged event.
         */
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user.isEmailVerified()) {
                        Log.d(TAG, "verificationCheck:success");

                        /*
                        Toast.makeText(Login.this,
                                "Sign-in successful for: " + user.getEmail(),
                                Toast.LENGTH_SHORT).show();
                        */

                        updateUI(user);
                    }
                    else {
                        Log.d(TAG, "verificationCheck:failure");
                        Toast.makeText(Login.this,
                                "Email not verified. Verify before trying again.",
                                Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(Login.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // [END sign_in_with_email]
    }

    private void updateUI(FirebaseUser user) {
        String type = Database.getUserType(user.getUid());
        switch (type){
            case "Student":
                loadUserOptions(user);
                break;
            case "Organization":
                loadOrganizationOptions(user);
        }
    }

    private void loadUserOptions(FirebaseUser user){
        Intent userOptionsIntent = new Intent(this, StudentOptions.class);
        startActivity(userOptionsIntent);
    }
    private void loadOrganizationOptions(FirebaseUser user){
        Intent userOptionsIntent = new Intent(this, OrganizationOptions.class);
        startActivity(userOptionsIntent);
    }

}
