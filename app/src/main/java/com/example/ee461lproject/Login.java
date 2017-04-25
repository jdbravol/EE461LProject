package com.example.ee461lproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by juanbravo on 4/22/17.
 */

public class Login extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private Button newUserButton;
    private Button forgotPasswordButton;
    private Button loginButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Link EditText and Buttons in layouts
        email = (EditText) findViewById(R.id.emailLine);
        password = (EditText) findViewById(R.id.passwordLine);
        newUserButton = (Button) findViewById(R.id.newUserButton);
        forgotPasswordButton = (Button) findViewById(R.id.forgotPassButton);
        loginButton = (Button) findViewById(R.id.loginButton);

        /* Returns an instance of the FirebaseAuth class corresponding to the default
         * FirebaseApp instance.
         */
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
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

        // Add onclick functionality to each button

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2.1.1 redirect to Register page
                Intent registrationIntent = new Intent(Login.this, Register.class);
                Login.this.startActivity(registrationIntent);
            }
        });

        //2.2 forgotPasswordButton
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //2.3 Login Button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        if (!AuthenticationTools.validateForm()) {
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
                        // TODO: Remove after writing updateUI
                        Toast.makeText(Login.this,
                                "Sign-in successful for: " + user.getEmail(),
                                Toast.LENGTH_SHORT).show();

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

        // TODO: Retrieve user information and update UI accordingly

    }

}
