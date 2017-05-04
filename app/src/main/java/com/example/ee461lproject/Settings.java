package com.example.ee461lproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
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

import java.util.ArrayList;
import java.util.Arrays;


public class Settings extends AppCompatActivity {


    // The entry point of the Firebase Authentication SDK
    private FirebaseAuth mAuth;
    private static final String TAG = "SettingsActivity";
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "User logged in is: " + mAuth.getCurrentUser());
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Entered settings");
        setContentView(R.layout.activity_settings);

        Button changePwdButton = (Button) findViewById(R.id.changePasswordButton);
        Button changeNameButton = (Button) findViewById(R.id.changeNameButton);
        Button deleteButton = (Button) findViewById(R.id.deleteAccountButton);
        final EditText newPwdField = (EditText) findViewById(R.id.newPasswordField);
        final EditText newPwdField2 = (EditText) findViewById(R.id.newPasswordField2);
        final EditText newNameField = (EditText) findViewById(R.id.newNameField);

        changePwdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd1 = newPwdField.getText().toString();
                String pwd2 = newPwdField2.getText().toString();

                ArrayList<String> passwords = new ArrayList<String>(Arrays.asList(new String[]{pwd1, pwd2}));
                if (AuthenticationTools.textFieldsEmpty(passwords)) {
                    Toast.makeText(Settings.this,
                            "Enter both password fields.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //change password
                if (pwd1.equals(pwd2)) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    user.updatePassword(pwd1)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Settings.this,
                                                "Password updated succesfully!",
                                                Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "User password updated.");
                                        logOut();
                                    }
                                }
                            });
                }

                //ask for them to fix passwords
                else {
                    Toast.makeText(Settings.this,
                            "The passwords do not match.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // If a display name is changed, the events associated with that org account are not
        // updated. After changing a display name, logging out then logging back in wipes an
        // org's feed.
        changeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = newNameField.getText().toString();
                if (!newName.equals("")) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    String old =  user.getDisplayName();
                    UserProfileChangeRequest.Builder requestBuilder = new UserProfileChangeRequest.Builder();
                    requestBuilder.setDisplayName(newName);
                    UserProfileChangeRequest request = requestBuilder.build();
                    user.updateProfile(request);
                    if(Database.getUserType(user.getUid()).equals("Organization")){
                        ArrayList<Event> allEvents = Database.allEvents();
                        for(Event e : allEvents){
                            if(e.getOrganizer().equals(old)){
                                e.setOrganizer(newName);
                                Database.updateEvent(e);
                            }
                        }
                    }
                    else if (Database.getUserType(user.getUid()).equals("Student")){
                        ArrayList<Event> allEvents = Database.allEvents();
                        for(Event e : allEvents){
                            if(e.isInRSVPList(old)){
                                Log.d(TAG, "Removing" + old + " adding " + newName);
                                e.removeOfRSVP(old);
                                e.addToRSVP(newName);
                                Database.updateEvent(e);
                            }
                        }
                    }
                    Toast.makeText(Settings.this,
                            "Updated name successfully",
                            Toast.LENGTH_SHORT).show();
                    logOut();
                } else {
                    Toast.makeText(Settings.this,
                            "Please write a new display name.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    Toast.makeText(Settings.this,
                            "Click this button 5 more times to delete account.",
                            Toast.LENGTH_SHORT).show();
                    count++;
                } else if (count > 0 && count < 6) {
                    Toast.makeText(Settings.this,
                            Integer.toString(count),
                            Toast.LENGTH_SHORT).show();
                    count++;
                } else {
                    FirebaseUser user = mAuth.getCurrentUser();

                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User account deleted.");
                                        Toast.makeText(Settings.this,
                                                "User account deleted. Bye!",
                                                Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                        Intent splashIntent = new Intent(Settings.this, SplashActivity.class);
                                        Settings.this.startActivity(splashIntent);
                                        Settings.this.finish();
                                    }
                                }
                            });
                }

            }

        });
    }

    /* Called after onCreate(Bundle) — or after onRestart() when the activity had been stopped,
     * but is now again being displayed to the user. It will be followed by onResume().
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /* Called when you are no longer visible to the user. You will next receive either onRestart(),
     * onDestroy(), or nothing, depending on later user activity.
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            updateUI(mAuth.getCurrentUser());
        }

        return super.onKeyDown(keyCode, event);
    }

    private void updateUI(FirebaseUser user) {
        String type = Database.getUserType(user.getUid());
        switch (type) {
            case "Student":
                loadUserOptions(user);
                break;
            case "Organization":
                loadOrganizationOptions(user);
        }
    }

    private void loadUserOptions(FirebaseUser user) {
        Intent userOptionsIntent = new Intent(this, StudentOptions.class);
        startActivity(userOptionsIntent);
    }

    private void loadOrganizationOptions(FirebaseUser user) {
        Intent userOptionsIntent = new Intent(this, OrganizationOptions.class);
        Database.writeToOrg = true;
        startActivity(userOptionsIntent);
    }

    private void logOut(){
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        this.finish();

    }
}