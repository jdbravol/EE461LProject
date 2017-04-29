package com.example.ee461lproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class CreateUser extends AppCompatActivity {

    private EditText passwordLine;
    private EditText emailLine;
    private Button createButton;
    private Spinner accountSpinner;
    private Button backButton;
    private EditText nameLine;

    // The entry point of the Firebase Authentication SDK
    private FirebaseAuth mAuth;
    // Listener called when there is a change in the authentication state
    private FirebaseAuth.AuthStateListener mAuthListener;
    // Used to identify the source of a log message
    private static final String TAG = "CreateUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        // Get the references for the text fields, buttons, and spinner
        passwordLine = (EditText) findViewById(R.id.passwordLine);
        emailLine = (EditText) findViewById(R.id.emailLine);
        createButton = (Button) findViewById(R.id.createButton);
        accountSpinner = (Spinner) findViewById(R.id.accountSpinner);
        backButton = (Button) findViewById(R.id.backToLogin);
        nameLine = (EditText) findViewById(R.id.nameLine);

        // Create an ArrayAdapter using the array defined in the strings.xml file
            // Using code from Android API guides:
            // https://developer.android.com/guide/topics/ui/controls/spinner.html
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.account_types, android.R.layout.simple_spinner_item);

        // This code specifies how to display the list of choices in the spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // We finish the process by attaching the adapter to the spinner
        accountSpinner.setAdapter(adapter);

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

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailLine.getText().toString();
                String password = passwordLine.getText().toString();
                String displayName = nameLine.getText().toString();

                if(!email.equals("") && !password.equals("") && !displayName.equals("")){
                    createAccount(emailLine.getText().toString(), passwordLine.getText().toString(), nameLine.getText().toString());
                    passwordLine.setText("");
                    emailLine.setText("");
                    nameLine.setText("");
                }
                else{
                    Toast.makeText(CreateUser.this,
                            "Fill out completely the form.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToLogin();
            }
        });
    }

    /* Called after onCreate(Bundle) — or after onRestart() when the activity had been stopped,
     * but is now again being displayed to the user. It will be followed by onResume().
     */
    @Override
    protected void onStart() {
        super.onStart();
        /* Registers a listener to changes in the authentication state. There can be more than
         * one listener registered at the same time for one or more FirebaseAuth instances.
         */
        mAuth.addAuthStateListener(mAuthListener);
    }

    /* Called when you are no longer visible to the user. You will next receive either onRestart(),
     * onDestroy(), or nothing, depending on later user activity.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            // Unregisters a listener to authentication changes.
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    // ------------------------------------- PRIVATE METHODS ------------------------------------- //


    private void goBackToLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    private void createAccount(String email, String password, final String displayName) {
        Log.d(TAG, "createAccount:" + email);
        if (!AuthenticationTools.validateForm()) {
            return;
        }

        // [START create_user_with_email]

        /* The createUserWithEmailAndPassword method tries to create a new user account
         * with the given email address and password. It signs the user out if creation is successful
         * as Firebase authenticates by default.
         *
         * The method returns a Task object, which represents an asynchronous operation. We add an Activity-scoped
         * listener that is called when the Task completes.
         */


        Task<AuthResult> creationTask =  mAuth.createUserWithEmailAndPassword(email, password);
        creationTask.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            // Called when the Task is completed.
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {    // Parameter = completed Task
                if (task.isSuccessful()) {
                    final FirebaseUser user = mAuth.getCurrentUser();
                    UserProfileChangeRequest.Builder requestBuilder = new UserProfileChangeRequest.Builder();
                    requestBuilder.setDisplayName(displayName);
                    UserProfileChangeRequest request = requestBuilder.build();
                    user.updateProfile(request);
                    String accountType = accountSpinner.getSelectedItem().toString();
                    addUserToDatabase(user, accountType);
                    Log.d(TAG, "createUserWithEmail:success");

                    Toast.makeText(CreateUser.this,
                            "Sending verification email to " + user.getUid(),
                            Toast.LENGTH_SHORT).show();

                    sendEmailVerification(user);    // TODO: Place in separate thread
                    mAuth.signOut();    // TODO: Determine optimal place to sign out
                } else {
                    /* If the default sign-in fails, we display a message.
                     *
                     * The getException method returns the exception that caused the Task to
                     * fail. It returns null if the Task is not yet complete, or completed
                     * successfully.
                     */
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(CreateUser.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });
        // [END create_user_with_email]
    }

    // Send verification email
    private void sendEmailVerification(FirebaseUser currentUser) {

        // [START send_email_verification]
        final FirebaseUser user = currentUser;

        /* The sendEmailVerification method initiates email verification for the user. It returns
         * a Task, for which we create and add an OnCompleteListener.
         */
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "sendEmailVerification", task.getException());
                    Toast.makeText(CreateUser.this,
                            "Failed to send verification email.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        // [END send_email_verification]
    }

    private void addUserToDatabase(FirebaseUser user, String accountType) {
        Database.makeUser(user.getUid(), accountType);
    }

}